package org.webdev.kpoint.daemons;

import javax.servlet.http.HttpServlet;

import org.webdev.kpoint.bl.manager.ExternalSettingsManager;

@SuppressWarnings("serial")
public class DaemonManager extends HttpServlet {
	
    public DaemonManager() {
    	super();
    	spawnConsumerCreditEmailNotificationThread();
    	spawnConsumerCreditExpirationCheckThread();
    }
    
    private void spawnConsumerCreditEmailNotificationThread()
    {
		boolean enabled = ExternalSettingsManager.getConsumerCreditNotificationsEnabled();
		if (enabled)
		{
///			logger.info("Spawning ConsumerCreditEmailNotificationThread");
			int delayTimeInHours = ExternalSettingsManager.getConsumerCreditNotificationsDelayTimeInHours();
			Thread thread = new TaskScheduler(new ConsumerCreditExpiryNotifier(), hoursToMillis(delayTimeInHours));
			thread.start();
		}
    }
    
    private void spawnConsumerCreditExpirationCheckThread()
    {
		boolean enabled = ExternalSettingsManager.getConsumerCreditExpirationCheckEnabled();
		if (enabled)
		{
///			logger.info("Spawning ConsumerCreditExpirationCheckThread");
			int delayTimeInHours = ExternalSettingsManager.getConsumerCreditExpirationCheckDelayTimeInHours();
			Thread thread = new TaskScheduler(new ConsumerCreditExpiryChecker(), hoursToMillis(delayTimeInHours));
			thread.start();
		}
    }

    private long hoursToMillis(int hours)
    {
    	return 1000L * 60L * 60L * (long)hours;
    }
}
