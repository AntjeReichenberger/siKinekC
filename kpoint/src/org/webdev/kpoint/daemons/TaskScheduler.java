package org.webdev.kpoint.daemons;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;

public class TaskScheduler extends Thread {
	private ITask task;
	private long sleepTimeInMillis;
	
	private static final KinekLogger logger = new KinekLogger(TaskScheduler.class);

	public TaskScheduler(ITask process, long sleepTimeInMillis)
	{
		this.task = process;
		this.sleepTimeInMillis = sleepTimeInMillis;
	}
	
	public void run()
	{
		while (true)
		{
			task.run();
			sleepForIntervalDuration();
		}
	}
	
	private void sleepForIntervalDuration()
	{
		try {
			TaskScheduler.sleep(sleepTimeInMillis);
		} catch (InterruptedException e) {
            logger.error(new ApplicationException("Task Thread interrupted.", e));
		}
	}
}
