package com.kinek.worker;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webdev.kpoint.bl.util.ApplicationProperty;

import com.trendrr.beanstalk.BeanstalkClient;
import com.trendrr.beanstalk.BeanstalkException;
import com.trendrr.beanstalk.BeanstalkJob;

public class QueueWorker {

	/**
	 * @param args
	 */
	
	protected static Log log = LogFactory.getLog(QueueWorker.class);
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		loadProperties (args) ;
	
		BeanstalkClient client = new BeanstalkClient("hazen.dev.kinek.com", 8010,"example");
		try {
			client.watchTube("example") ;
			
			while (true) {
				log.info("Waiting..." );
				BeanstalkJob job = client.reserve(60);
			
				log.info("Got job: " + job.getId());
				String s = new String(job.getData());
			
				log.info("Got data:" + s) ;
				client.deleteJob(job) ;
			}
			
			//BeanstalkJob job = client.reserve(60);
			//log.info("GOt job: " + job);
			//;
			
		} catch (BeanstalkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println ("\r\nHello World") ;
//		UserDao userDao = new UserDao(); 
//		
//		try {
//			User user = new UserDao().authenticateAdmin("jeremy","kppass1");
//			
//			if (user != null) {
//				String address1 = user.getAddress1() ;
//				String email = user.getEmail() ;
//				System.out.println ("ADDRESS:" + address1) ;
//				System.out.println ("EMAIL:" + email) ;
//			} else {
//				System.out.println ("\r\nNO USER!") ;
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	
	private static void loadProperties(String[] args) {
		
		Properties temp = new Properties();
		
		// See if any arguments were supplied.
		if ( args == null || args.length < 1) {
			System.out.println(("path to external.properties not supplied")) ;
		} else {
			System.out.print((String.format("your argument was '%s'", args[0]))) ;
			
			// Load properties.
			try {
				temp.load(new FileInputStream(args[0]));
				Enumeration<Object> enumeration = temp.keys();
				while (enumeration.hasMoreElements()) {
					String key = (String) enumeration.nextElement();
					String val = (String) temp.getProperty(key) ;
					ApplicationProperty.getInstance().addProperty(key,temp.getProperty(key));
				}
	 
			} catch (IOException e) {
				System.out.println(String.format("\n\rFailed to find external.properties at '%s'",args[0])) ;
				System.exit(0);
			}
		}
	}

}
