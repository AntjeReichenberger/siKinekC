package org.webdev.kpoint.action;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.persistence.AssociationDao;
import org.webdev.kpoint.bl.persistence.CreditCalculationTypeDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PromotionDao;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.pojo.Association;
import org.webdev.kpoint.bl.pojo.CreditCalculationType;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Promotion;
import org.webdev.kpoint.bl.pojo.State;

@UrlBinding("/ManagePromotions.action")
public class ManagePromotionsActionBean extends SecureActionBean
{
	private static final KinekLogger logger = new KinekLogger(ManagePromotionsActionBean.class);

	private static final Resolution FORM_PAGE = UrlManager.getManagePromotionsForm();
	private static final Resolution EDIT_PAGE = UrlManager.getManagePromotionsEmailEdit();
	private static final Resolution PREVIEW_PAGE = UrlManager.getManagePromotionsEmailPreview();
	private static final Resolution SUCCESS_PAGE = UrlManager.getManagePromotionsSuccess();

	@Validate(on="submitPromotion", required=true)
	String promotionCode;

	@Validate(on="submitPromotion", required=true)
	String promotionTitle;
	
	@Validate(on="submitPromotion", required=true, maxlength=200)
	String promotionDescription;

	@Validate(on="submitPromotion", required=true)
	int quantity;
	
	@Validate(on="submitPromotion", required=true)
	String startDate;
	
	@Validate(on="submitPromotion", required=true)
	String endDate;

	@Validate(on="submitPromotion", required=false)
	//double consumerCredit;
	String consumerCredit;
	String consumerCreditType = "dollar";
	
	@Validate(on="submitPromotion", required=false)
	String depotCredit;
	//double depotCredit;
	String depotCreditType = "dollar";
	
	int promotionId;
	int stateId;
	int associationId;
	int depotId;
	String emailSubject;
	String emailBody;	
	
	//Preview page fields
	private Promotion promotion;
	private String emailMessage;
	private Date startingDate;
	private Date endingDate;

	@DefaultHandler
	public Resolution view()
	{
		if (consumerCredit == null) consumerCredit = "0";
		if (depotCredit == null) depotCredit = "0";
		return FORM_PAGE;
		//return UrlManager.getManagePromotionsForm();
	}

	@ValidationMethod(on="submitPromotion")
	public void submitPromotionValidation(ValidationErrors errors) throws Exception 
	{
		boolean nocCredit = false;
		boolean nodCredit = false;
		double cCredit = 0;
		double dCredit = 0;
		
		if (consumerCredit.contains(","))
		{
			nocCredit = true;
			String field = "consumerCredit";
			ValidationError error = new SimpleError("The value entered for the Consumer Credit Amount must be in a valid format");
			errors.add(field, error);
		}

		if (depotCredit.contains(","))
		{
			nodCredit = true;
			String field = "depotCredit";
			ValidationError error = new SimpleError("The value entered for the KinekPoint Credit Amount must be in a valid format");
			errors.add(field, error);
		}
		
		try
		{
			cCredit = Double.parseDouble(consumerCredit);
		}
		catch(Exception e)
		{
			Hashtable<String,String> logValues = new Hashtable<String,String>();
			logValues.put("ConsumerCredit", consumerCredit);
            logger.error(new ApplicationException("Consumer credit is not valid", e), logValues);
            
            nocCredit = true;
			String field = "consumerCredit";
			ValidationError error = new SimpleError("The value entered for the Consumer Credit Amount must be in a valid format");
			errors.add(field, error);
		}

		try
		{
			dCredit = Double.parseDouble(depotCredit);
		}
		catch(Exception e)
		{
			Hashtable<String,String> logValues = new Hashtable<String,String>();
			logValues.put("DepotCredit", depotCredit);
            logger.error(new ApplicationException("Depot credit is not valid", e), logValues);
            
            nodCredit = true;
			String field = "depotCredit";
			ValidationError error = new SimpleError("The value entered for the KinekPoint Credit Amount must be in a valid format");
			errors.add(field, error);
		}

		if (new PromotionDao().read(promotionCode) != null)
		{
			String field = "promotionCode";
			ValidationError error = new SimpleError("Promotion Code has already been assigned");
			errors.add(field, error);
		}

		if (promotionCode.length() >= 10)
		{
			String field = "promotionCode";
			ValidationError error = new SimpleError("Promotion Code must be less than 10 characters long");
			errors.add(field, error);
		}

		if (promotionTitle.length() >= 30)
		{
			String field = "promotionTitle";
			ValidationError error = new SimpleError("Promotion Title must be less than 30 characters long");
			errors.add(field, error);
		}
		
		if (promotionDescription.length() >= 200)
		{
			String field = "promotionDescription";
			ValidationError error = new SimpleError("Promotion Description must be less than 200 characters long");
			errors.add(field, error);
		}

		if (quantity <= 0)
		{
			String field = "quantity";
			ValidationError error = new SimpleError("The Quantity value must be a number that is greater than 0");
			errors.add(field, error);
		}
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date start = null;
		Date end = null;
		Date today = null;

		try
		{
			start = df.parse(startDate);
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
			end = df.parse(endDate);
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

		try
		{
			today = df.parse(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "/" + Calendar.getInstance().get(Calendar.YEAR));
		}
		catch (Exception e) {
			logger.error(new ApplicationException("Error occurred processing the created date", e));
		}
		
		if (start != null && start.before(today))
		{
			String field = "startDate";
			ValidationError error = new SimpleError("Invalid Start Date was entered");
			errors.add(field, error);
		}
		
		if (start != null && end != null && start.after(end))
		{
			String field = "endDate";
			ValidationError error = new SimpleError("Invalid Start Date was entered");
			errors.add(field, error);
		}

		if (start != null && end != null && end.before(start))
		{
			String field = "endDate";
			ValidationError error = new SimpleError("Invalid End Date was entered");
			errors.add(field, error);
		}

		if (!nocCredit)
		{
			if (consumerCreditType.equals("dollar") && !(cCredit >= 0 && cCredit < 999999999))
			{
				String field = "consumerCredit";
				ValidationError error = new SimpleError("The value entered for the Consumer Credit Amount must be a valid dollar amount that is between $0.00 and less than $999,999,999");
				errors.add(field, error);
			}

			if (consumerCreditType.equals("percentage") && !(cCredit >= 0 && cCredit <= 100 && Math.ceil(cCredit) == cCredit))
			{
				String field = "consumerCredit";
				ValidationError error = new SimpleError("The value entered for the Consumer Credit Amount must be a valid percentage that is between 0% and 100%");
				errors.add(field, error);
			}
		}

		if (!nodCredit)
		{
			if (depotCreditType.equals("dollar") && !(dCredit >= 0 && dCredit < 999999999))
			{
				String field = "depotCredit";
				ValidationError error = new SimpleError("The value entered for the KinekPoint Credit Amount must be a valid dollar amount that is between $0.00 and less than $999,999,999");
				errors.add(field, error);
			}

			if (depotCreditType.equals("percentage") && !(dCredit >= 0 && dCredit <= 100 && Math.ceil(dCredit) == dCredit))
			{
				String field = "depotCredit";
				ValidationError error = new SimpleError("The value entered for the KinekPoint Credit Amount must be a valid percentage that is between 0% and 100%");
				errors.add(field, error);
			}
		}
	}

	public Resolution submitPromotion() throws Exception 
	{
		CreditCalculationType consumerCreditCalcType;
		CreditCalculationType depotCreditCalcType;
		if (depotCreditType.equals("dollar")) depotCreditCalcType = new CreditCalculationTypeDao().read(1);
		else depotCreditCalcType = new CreditCalculationTypeDao().read(2);
		if (consumerCreditType.equals("dollar")) consumerCreditCalcType = new CreditCalculationTypeDao().read(1);
		else consumerCreditCalcType = new CreditCalculationTypeDao().read(2);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		Calendar cStart = Calendar.getInstance();
		Calendar cEnd = Calendar.getInstance();

		try
		{
			cStart.setTime(df.parse(startDate));
			cEnd.setTime(df.parse(endDate));
		}
		catch (Exception e){
			Hashtable<String,String> logValues = new Hashtable<String,String>();
			logValues.put("StartDate", startDate);
            logValues.put("EndDate", endDate);
            logger.error(new ApplicationException("Error occurred processing the dates", e), logValues);
		}

		Promotion promotion = new Promotion();
		promotion.setAvailabilityCount(quantity);
		promotion.setCode(promotionCode);
		promotion.setConsumerCreditAmount(new BigDecimal(consumerCredit));
		promotion.setConsumerCreditCalcType(consumerCreditCalcType);
		promotion.setCreatedDate(Calendar.getInstance().getTime());
		if (depotId > 0) promotion.setDepot(new KinekPointDao().read(depotId));
		promotion.setDepotCreditAmount(new BigDecimal(depotCredit));
		promotion.setDepotCreditCalcType(depotCreditCalcType);
		promotion.setDescription(promotionDescription);
		promotion.setEndDate(cEnd.getTime());
		promotion.setStartDate(cStart.getTime());
		if (stateId > 0) promotion.setState(new StateDao().read(stateId));
		if (associationId > 0) promotion.setAssociation(new AssociationDao().read(associationId));
		promotion.setTitle(promotionTitle);
		promotionId = new PromotionDao().create(promotion);

		//setSuccessMessage(new SimpleMessage("Successfully created promotion"));

		if (depotId > 0) return EDIT_PAGE;

		return SUCCESS_PAGE;
	}

	public Resolution previewEmail() throws Exception 
	{
		promotion = new PromotionDao().read(promotionId);
		startingDate = promotion.getStartDate();
		endingDate = promotion.getEndDate();
		return PREVIEW_PAGE;
	}

	public Resolution editEmail()
	{
		return EDIT_PAGE;
	}

	public Resolution sendEmail() throws Exception 
	{
		EmailManager manager = new EmailManager();
		KinekPoint depot = new KinekPointDao().read(depotId);
		Promotion promotion = new PromotionDao().read(promotionId);
		manager.sendPromotionEmail(depot, promotion, emailMessage);

		setSuccessMessage(new SimpleMessage("Successfully sent promotion details to KinekPoint"));

		return SUCCESS_PAGE;
	}

	public Resolution success()
	{
		return new RedirectResolution(ViewKinekPointActionBean.class);
	}

	public String getPromotionCode()
	{
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode)
	{
		this.promotionCode = promotionCode;
	}

	public String getPromotionTitle()
	{
		return promotionTitle;
	}

	public void setPromotionTitle(String promotionTitle)
	{
		this.promotionTitle = promotionTitle;
	}

	public String getPromotionDescription()
	{
		return promotionDescription;
	}

	public void setPromotionDescription(String promotionDescription)
	{
		this.promotionDescription = promotionDescription;
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
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

	public String getConsumerCredit()
	{
		return consumerCredit;
	}

	public void setConsumerCredit(String consumerCredit)
	{
		this.consumerCredit = consumerCredit;
	}
	
	public String getConsumerCreditType()
	{
		return consumerCreditType;
	}

	public void setConsumerCreditType(String consumerCreditType)
	{
		this.consumerCreditType = consumerCreditType;
	}

	public String getDepotCredit()
	{
		return depotCredit;
	}

	public void setDepotCredit(String depotCredit)
	{
		this.depotCredit = depotCredit;
	}
	
	public String getDepotCreditType()
	{
		return depotCreditType;
	}

	public void setDepotCreditType(String depotCreditType)
	{
		this.depotCreditType = depotCreditType;
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

	public int getAssociationId()
	{
		return associationId;
	}

	public void setAssociationId(int associationId)
	{
		this.associationId = associationId;
	}

	public String getDepotName() throws Exception
	{
		KinekPoint depot = new KinekPointDao().read(depotId);
		return depot.getName();
	}

	public String getDepotEmail() throws Exception
	{
		KinekPoint depot = new KinekPointDao().read(depotId);
		return depot.getEmail();
	}

	public String getEmailMessage()
	{
		return emailMessage;
	}

	public void setEmailMessage(String emailMessage)
	{
		this.emailMessage = emailMessage;
	}

	public String getEmailSubject()
	{
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject)
	{
		this.emailSubject = emailSubject;
	}

	public int getPromotionId()
	{
		return promotionId;
	}

	public void setPromotionId(int promotionId)
	{
		this.promotionId = promotionId;
	}

	public Promotion getPromotion() {
		return promotion;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public Date getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(Date endingDate) {
		this.endingDate = endingDate;
	}
}
