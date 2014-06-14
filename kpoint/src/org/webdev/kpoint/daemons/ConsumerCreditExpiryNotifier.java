package org.webdev.kpoint.daemons;

import java.util.Calendar;
import java.util.List;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.bl.persistence.ConsumerCreditDao;
import org.webdev.kpoint.bl.persistence.CreditStatusDao;
import org.webdev.kpoint.bl.persistence.PromotionDao;
import org.webdev.kpoint.bl.pojo.ConsumerCredit;
import org.webdev.kpoint.bl.pojo.CreditStatus;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.Promotion;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.manager.SMSManager;

public class ConsumerCreditExpiryNotifier implements ITask
{
	private CreditStatus available = null;
	private EmailManager emailManager = new EmailManager();
	private SMSManager smsManager = new SMSManager();
	private static final KinekLogger logger = new KinekLogger(ConsumerCreditExpiryNotifier.class);
	
	public void run()
	{
		try{
			initAvailableCreditStatus();
			sendExpiryNotifications();
		}
		catch(Exception ex){
			logger.error(new ApplicationException("An error occurred in ConsumerCreditExpiryNotifier.", ex));
		}
	}
	
	private void initAvailableCreditStatus() throws Exception
	{
		if (available == null)
			available = new CreditStatusDao().read(ExternalSettingsManager.getCreditStatus_Available());		
	}
	
	private void sendExpiryNotifications() throws Exception
	{
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = (Calendar)startDate.clone();
		endDate.add(Calendar.DATE, getDayThreshold());
		
		List<Promotion> promotions = new PromotionDao().fetch(startDate, endDate);
		for (Promotion promotion : promotions)
		{
			sendPromotionNotifications(promotion);
		}
	}
	
	/**
	 * When adding dates, any threshold value will add one too many days. 
	 * This returns the correct number of days that are to be used when adding to a date. 
	 * (Nov 4th + 5 days is Nov 9th, if we include the 4th in the count the total days included is 6)
	 * @return dayThreshold - 1;
	 */
	private int getDayThreshold() {
		return ExternalSettingsManager.getConsumerCreditNotificationDayThreashold() - 1; 		
	}
	
	private void sendPromotionNotifications(Promotion promotion) throws Exception
	{
		List<ConsumerCredit> credits = new ConsumerCreditDao().fetchNonNotified(promotion, available);
		for (ConsumerCredit credit : credits)
		{
			sendEmail(credit);
			sendSMS(credit);
			updateCreditAsNotified(credit);
		}
	}
	
	private void sendEmail(ConsumerCredit credit) throws Exception
	{
		emailManager.sendConsumerCreditExpiryEmail(credit);
	}
	
	private void sendSMS(ConsumerCredit credit) throws Exception
	{
		User consumer = credit.getUser(); 
		if (consumer.getCellPhone() != null)
		{
			smsManager.sendConsumerCreditExpirySMS(consumer);
		}
	}
	
	private void updateCreditAsNotified(ConsumerCredit credit) throws Exception
	{
		credit.setExpiryNotificationDate(Calendar.getInstance());
		new ConsumerCreditDao().update(credit);
	}
}
