package org.webdev.kpoint.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.PickupDao;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.Pickup;
import org.webdev.kpoint.bl.pojo.State;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.ui.ConsumerActivityContainer;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/ConsumerActivity.action")
public class ConsumerActivityActionBean extends SecureActionBean
{
	private static final KinekLogger logger = new KinekLogger(ConsumerActivityActionBean.class);
	
	//Resolution constant
	private static final Resolution CONSUMER_ACTIVITY_PAGE = UrlManager.getConsumerActivityForm();
	
	//Consumer list
	List<ConsumerActivityContainer> consumersForReport = new ArrayList<ConsumerActivityContainer>();
	
	//Filter fields
	@Validate(on="search", required=false)
	String city;
	@Validate(on="search", required=false)
	String startDate;
	@Validate(on="search", required=false)
	String endDate;
	int stateId;
	int depotId;

	@DefaultHandler
	public Resolution view() throws Exception 
	{
		search();
		return CONSUMER_ACTIVITY_PAGE;
	}

	@ValidationMethod(on={"search"})
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

		if (start != null && end != null && start.after(end))
		{
			String field = "startDate";
			ValidationError error = new SimpleError("Invalid Start Date was entered");
			errors.add(field, error);
		}

		/*
		if (city != null && city.length() >= 10)
		{
			String field = "promotionCode";
			ValidationError error = new SimpleError("Promotion Code must be less than 10 characters long");
			errors.add(field, error);
		}
		*/
	}

	public Resolution search() throws Exception 
	{
		consumersForReport = filterConsumers();
		return CONSUMER_ACTIVITY_PAGE;
	}
	
	public boolean getNoResults()
	{
		return (consumersForReport.isEmpty());
	}

	public boolean getTooManyResults()
	{
		return (consumersForReport.size() > 100000);
	}
	
	public boolean getGoodResults()
	{
		return(!getTooManyResults() && !getNoResults());
	}

	public List<ConsumerActivityContainer> filterConsumers() throws Exception 
	{
		List<User>allConsumers = new UserDao().fetchConsumersForUserReport();
		List<ConsumerActivityContainer>selectedConsumers = new ArrayList<ConsumerActivityContainer>();
		Iterator<User> iterator = allConsumers.iterator();
		
		while (iterator.hasNext())
		{
			boolean include = true;
			User consumer = iterator.next();

			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Date start = null;
			Date end = null;
			Date createdDate = null;

			try
			{
				createdDate = df.parse(consumer.getCreatedDate().get(Calendar.DAY_OF_MONTH) + "/" + (consumer.getCreatedDate().get(Calendar.MONTH)+1) + "/" + consumer.getCreatedDate().get(Calendar.YEAR));
				if (startDate != null && !startDate.isEmpty()) start = df.parse(startDate);
				if (endDate != null && !endDate.isEmpty()) end = df.parse(endDate);
			}
			catch(Exception e){
				Hashtable<String,String> logValues = new Hashtable<String,String>();
				logValues.put("StartDate", startDate);
	            logValues.put("EndDate", endDate);
	            logger.error(new ApplicationException("Error occurred processing the dates", e), logValues);
			}

			if (city != null && !city.isEmpty()){
				if(consumer.getCity() == null || !consumer.getCity().equalsIgnoreCase(city)) include = false;
			}
			if (start != null && createdDate.before(start)) include = false;
			if (end != null && createdDate.after(end)) include = false;
			if (stateId != 0){
				if(consumer.getState() == null || consumer.getState().getStateId() != stateId) include = false;
			}
			if (depotId != 0){
				if(consumer.getKinekPoint() == null || consumer.getKinekPoint().getDepotId() != depotId) include = false;
			}

			if (include)
			{
				ConsumerActivityContainer c = new ConsumerActivityContainer();
				c.setParcelsPendingCount(this.getParcelsPendingCount(consumer));
				c.setParcelsPickedupCount(this.getParcelsPickedupCount(consumer));
				c.setLastPickupDate(this.getLastPickupDate(consumer));
				c.setConsumer(consumer);
				selectedConsumers.add(c);
			}
		}

		return selectedConsumers;
	}

	public List<ConsumerActivityContainer> getConsumersForReport()
	{
		return consumersForReport;
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
		return new KinekPointDao().fetchLight();
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
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
	
	public int getParcelsPendingCount(User consumer) throws Exception 
	{
		List<PackageReceipt> packageReceipts = new PackageReceiptDao().fetch(consumer.getKinekNumber());
		int pendingCount = 0;
		for (PackageReceipt receipt : packageReceipts) {
			for (Package packageObj : receipt.getPackages()) {
				if (packageObj.getPackageStatus(receipt) == ConfigurationManager.getPackageStatusInDepot()) {
					pendingCount++;
				}
			}
		}
		
		return pendingCount;
	}

	public int getParcelsPickedupCount(User consumer) throws Exception 
	{
		List<Pickup> pickups = new PickupDao().fetch(consumer.getKinekNumber());
		int pickedUpCount = 0;
		for (Pickup pickup : pickups) {
			pickedUpCount += pickup.getPackages().size();
		}
		
		return pickedUpCount;
	}

	public Date getLastPickupDate(User consumer) throws Exception 
	{
		List<Pickup> pickups = new PickupDao().fetch(consumer.getKinekNumber());
		Date lastPickupDate = null;

		for (Pickup pickup : pickups) {
			if (lastPickupDate == null)
				lastPickupDate = pickup.getPickupDate();
			else if (pickup.getPickupDate().after(lastPickupDate))
				lastPickupDate = pickup.getPickupDate();
		}

		return lastPickupDate;
	}

	public int getConsumerCount()
	{
		return(consumersForReport.size());
	}

	public int getParcelsPendingCount()
	{
		int tally = 0;
		Iterator<ConsumerActivityContainer> iterator = consumersForReport.listIterator();
		
		while (iterator.hasNext())
		{
			ConsumerActivityContainer consumer = iterator.next();
			tally += consumer.getParcelsPendingCount();
		}

		return(tally);
	}
	
	public int getParcelsPickedupCount()
	{
		int tally = 0;
		Iterator<ConsumerActivityContainer> iterator = consumersForReport.listIterator();
		
		while (iterator.hasNext())
		{
			ConsumerActivityContainer consumer = iterator.next();
			tally += consumer.getParcelsPickedupCount();
		}

		return(tally);
	}
	
	public int getListLength() {
		return consumersForReport.size();
	}
}
