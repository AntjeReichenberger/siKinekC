package org.webdev.kpoint.action;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.AssociationDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PromotionDao;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.pojo.Association;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Promotion;
import org.webdev.kpoint.bl.pojo.Redemption;
import org.webdev.kpoint.bl.pojo.State;


@UrlBinding("/ExportPromotions.action")
public class ExportPromotionsActionBean extends SecureActionBean {
	private static final KinekLogger logger = new KinekLogger(ExportPromotionsActionBean.class);

	private static final Resolution EXPORT_PAGE = UrlManager.getExportPromotions();
	List<Promotion> promosForExport = new ArrayList<Promotion>();
	@Validate(on="search", required=false)
	String promotionCode;
	@Validate(on="search", required=false)
	String startDate;
	@Validate(on="search", required=false)
	String endDate;
	int redemptionId;
	int stateId;
	int associationId;
	int depotId;

	@DefaultHandler
    public Resolution view() throws Exception {
		search();
		
        return EXPORT_PAGE;
    }

	public Resolution reset() throws Exception 
	{
		return new RedirectResolution(ExportPromotionsActionBean.class);
    }

	public Resolution export() throws Exception {
		search();
		StreamingResolution r = new StreamingResolution("text/csv", new StringReader(buildPromoExportCSV()));
		r.setFilename("KinekPromotionsExport.csv");
		return r;
	}

	@SuppressWarnings("unused")
	@ValidationMethod(on={"search", "export"})
	public void searchPromotionsValidation(ValidationErrors errors)
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date start = null;
		Date end = null;

		try
		{
			if (startDate != null && !startDate.isEmpty()) start = df.parse(startDate);
		}
		catch(Exception e)
		{
			Hashtable<String,String> logValues = new Hashtable<String,String>();
			logValues.put("StartDate", startDate);
            logger.error(new ApplicationException("Error occurred processing the start date", e), logValues);
            
            String field = "startDate";
			ValidationError error = new SimpleError("The Start Date value must entered in a valid Date format");
			errors.add(field, error);
		}

		try
		{
			if (endDate != null && !endDate.isEmpty()) end = df.parse(endDate);
		}
		catch(Exception e)
		{
			Hashtable<String,String> logValues = new Hashtable<String,String>();
			logValues.put("EndDate", endDate);
            logger.error(new ApplicationException("Error occurred processing the end date", e), logValues);

            String field = "endDate";
			ValidationError error = new SimpleError("The End Date value must entered in a valid Date format");
			errors.add(field, error);
		}
		
		if (promotionCode != null && promotionCode.length() >= 10)
		{
			String field = "promotionCode";
			ValidationError error = new SimpleError("Promotion Code must be less than 10 characters long");
			errors.add(field, error);
		}
	}
	
	public Resolution search() throws Exception 
	{
		promosForExport = filterPromotions();
		return EXPORT_PAGE;
	}

	public boolean getNoResults()
	{
		return (promosForExport.isEmpty());
	}

	public boolean getTooManyResults()
	{
		return (promosForExport.size() > 100000);
	}

	public boolean getGoodResults()
	{
		return(!getTooManyResults() && !getNoResults());
	}

	public List<Promotion> filterPromotions() throws Exception 
	{
		List<Promotion>allPromotions = new PromotionDao().fetch();
		List<Promotion>selectedPromotions = new ArrayList<Promotion>();
		
		Iterator<Promotion> iterator = allPromotions.iterator();
		
		while (iterator.hasNext())
		{
			boolean include = true;
			Promotion promotion = iterator.next();

			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Date start = null;
			Date end = null;
			Date today = null;

			try
			{
				if (startDate != null && !startDate.isEmpty()) start = df.parse(startDate);
				if (endDate != null && !endDate.isEmpty()) end = df.parse(endDate);
				today = df.parse(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "/" + Calendar.getInstance().get(Calendar.YEAR));
			}
			catch(Exception e){
				Hashtable<String,String> logValues = new Hashtable<String,String>();
				logValues.put("StartDate", startDate);
	            logValues.put("EndDate", endDate);
	            logger.error(new ApplicationException("Error occurred processing the dates", e), logValues);
			}

			if (promotionCode != null && !promotionCode.isEmpty() && !promotion.getCode().equals(promotionCode)) include = false;
			if (start != null && promotion.getStartDate().before(start)) include = false;
			if (end != null && promotion.getEndDate().after(end)) include = false;
			if (stateId != 0 && promotion.getState() == null) include = false;
			else if (stateId != 0 && promotion.getState().getStateId() != stateId) include = false;
			if (associationId != 0 && promotion.getAssociation() == null) include = false;
			else if (associationId != 0 && promotion.getAssociation().getAssociationId() != associationId) include = false;
			if (depotId != 0 && promotion.getDepot() == null) include = false;
			else if (depotId != 0 && promotion.getDepot().getDepotId() != depotId) include = false;
			if (redemptionId == 1 && promotion.getEndDate().before(today)) include = false;
			if (redemptionId == 2 && !promotion.getEndDate().before(today)) include = false;

			if (include) selectedPromotions.add(promotion);
		}

		return selectedPromotions;
	}
	
	/**
	 * Returns a csv string of all KinekPoint Changes 
	 * @return
	 */
	public String buildPromoExportCSV() {
		StringBuilder csv = new StringBuilder();
				
		csv.append("Code, Title, # Available, Start Date, End Date, Consumer Credit, Consumer Credit Type, Depot Credit, Depot Credit Type, Region, Association, KinekPoint \n");
		
		//Double quotes surround each value in order to handle any values that contain commas
		for (Promotion promo : promosForExport) {
			addColumnValue(csv, promo.getCode(), false);
			addColumnValue(csv, promo.getTitle(), false);
			addColumnValue(csv, promo.getAvailabilityCount(), false);
			addColumnValue(csv, promo.getStartDate().toString(), false);
			addColumnValue(csv, promo.getEndDate().toString(), false);
			addColumnValue(csv, promo.getConsumerCreditAmount(), false);
			addColumnValue(csv, promo.getConsumerCreditCalcType().getName(), false);
			addColumnValue(csv, promo.getDepotCreditAmount(), false);
			addColumnValue(csv, promo.getDepotCreditCalcType().getName(), false);
			addColumnValue(csv, (promo.getState() != null) ? promo.getState().getName() : "", false);
			addColumnValue(csv, (promo.getAssociation() != null) ? promo.getAssociation().getName() : "", false);
			addColumnValue(csv, (promo.getDepot() != null) ? promo.getDepot().getName() : "", false);
			addColumnValue(csv, "\n", true);
		}
		return csv.toString();
	}
	
	/**
	 * Adds a column value to a CSV. 
	 * @param sb String Builder
	 * @param columnValue The column value
	 */
	private void addColumnValue(StringBuilder sb, Object columnValue, boolean isLastColumn) {
		String value = "";
		if (columnValue != null) value = columnValue.toString();
		sb.append("\"");
		sb.append(value.replaceAll("\"", "\"\""));
		sb.append("\"");
		if (!isLastColumn)
			sb.append(",");
		else
			sb.append("\n");
	}

	public List<Promotion> getPromosForExport() {
		return promosForExport;
	}
	
	public void setPromosForExport(List<Promotion> promosForExport) {
		this.promosForExport = promosForExport;
	}

	public String getPromotionCode()
	{
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode)
	{
		this.promotionCode = promotionCode;
	}

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public List<KinekPoint> getDepots() throws Exception 
	{
		return new KinekPointDao().fetch();
	}

	public List<Association> getAssociations() throws Exception 
	{
		return(new AssociationDao().fetchAllAssociations());
	}

	public int getStateId()
	{
		return stateId;
	}

	public void setStateId(int stateId)
	{
		this.stateId = stateId;
	}

	public List<Redemption> getRedemptions()
	{
		List<Redemption> redemptions = new ArrayList<Redemption>();
		
		Redemption redemption1 = new Redemption();
		Redemption redemption2 = new Redemption();
		
		redemption1.setRedemptionId(1);
		redemption1.setName("Active");
		redemption2.setRedemptionId(2);
		redemption2.setName("Inactive");
		
		redemptions.add(redemption1);
		redemptions.add(redemption2);
		
		return(redemptions);
	}

	public List<State> getStates() throws Exception 
	{
		return new StateDao().fetch();
	}

	public int getDepotId()
	{
		return depotId;
	}

	public void setDepotId(int depotId)
	{
		this.depotId = depotId;
	}
	
	public int getRedemptionId()
	{
		return redemptionId;
	}

	public void setRedemptionId(int redemptionId)
	{
		this.redemptionId = redemptionId;
	}

	public int getAssociationId()
	{
		return associationId;
	}

	public void setAssociationId(int associationId)
	{
		this.associationId = associationId;
	}
}
