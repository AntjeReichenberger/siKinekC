package org.webdev.kpoint.daemons;

import java.util.Calendar;
import java.util.List;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.ConsumerCreditDao;
import org.webdev.kpoint.bl.persistence.CreditStatusDao;
import org.webdev.kpoint.bl.pojo.ConsumerCredit;
import org.webdev.kpoint.bl.pojo.CreditStatus;
import org.webdev.kpoint.bl.util.CalendarUtil;

public class ConsumerCreditExpiryChecker implements ITask
{
	private CreditStatus available = null;
	private CreditStatus expired = null;
	private static final KinekLogger logger = new KinekLogger(ConsumerCreditExpiryChecker.class);
	
	public void run()
	{
		try{
		initAvailableCreditStatus();
		setConsumerCreditExpiration();
		}catch(Exception ex){
			logger.error(new ApplicationException("An error occurred in ConsumerCreditExpiryChecker.", ex));
		}
	}
	
	private void initAvailableCreditStatus() throws Exception
	{
		if (available == null)
			available = new CreditStatusDao().read(ExternalSettingsManager.getCreditStatus_Available());		
		if (expired == null)
			expired = new CreditStatusDao().read(ExternalSettingsManager.getCreditStatus_Expired());
	}
	
	private void setConsumerCreditExpiration() throws Exception
	{
		Calendar today = new CalendarUtil(Calendar.getInstance()).getStartOfDay();
		List<ConsumerCredit> credits = new ConsumerCreditDao().fetch(available);

		for (ConsumerCredit credit : credits)
		{
			if (credit.getPromotion().getEndDate().before(today.getTime()))
			{
				credit.setCreditStatus(expired);
				new ConsumerCreditDao().update(credit);
			}
		}
	}
}
