package com.kinek.worker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.trendrr.beanstalk.BeanstalkClient;
import com.trendrr.beanstalk.BeanstalkException;

public class TestStatusQueue {

	/**
	 * @param args
	 */
	
	protected static Log log = LogFactory.getLog(TestStatusQueue.class);
	
	
	public static void main(String[] args) {
		// args: C:\\development\\KinekWorkspaces\\KpointWorker\\res\\external.properties
		
		BeanstalkClient client = new BeanstalkClient("hazen.dev.kinek.com", 8010,"packages");

		try {
			client.useTube("packages") ;
			client.tubeStats("packages");
			
			client.useTube("email") ;
			client.tubeStats("email") ;

			client.useTube("sms") ;
			client.tubeStats("sms") ;

			
			client.close(); //closes the connection
		} catch (BeanstalkException ex){
			ex.printStackTrace() ;
		}
		
	}

}
