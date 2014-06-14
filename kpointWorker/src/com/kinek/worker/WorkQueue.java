package com.kinek.worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.webdev.kpoint.bl.manager.DeliveryManager;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.NotificationSummary;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.util.ApplicationProperty;

import com.kinek.worker.tube.QueueHandler;
import com.trendrr.beanstalk.BeanstalkClient;
import com.trendrr.beanstalk.BeanstalkException;
import com.trendrr.beanstalk.BeanstalkJob;

public class WorkQueue {

	/**
	 * @param args
	 * 
	 * arg1 = location of beanstalkd.properties
	 * 
	 */
	private static ApplicationProperty ap = ApplicationProperty.getInstance() ;
	
	
	public static void main(String[] args)  {
	
		String queueproperties = args != null && args.length > 0  ? args[0] : ""  ;
		System.out.println (String.format("main property file: '%s' ", queueproperties)) ;
		
		// Load all properties.
		if (loadAllProperties (queueproperties)) {
			System.out.println ("loaded all properties") ;
			
			// Main code logic.
			String tubelist = ap.getProperty("beanstalkd.tube.listenlist") ; 
			Integer tubewait = ap.getProperty("beanstalkd.tube.wait").equalsIgnoreCase("null") ? null : Integer.valueOf(ap.getProperty("beanstalkd.tube.wait")) ;
			
			String bs_server = ap.getProperty("beanstalkd.server") ;
			String bs_port = ap.getProperty("beanstalkd.port") ;
			
			String bs_tube_packages=ap.getProperty("beanstalkd.tube.package") ;
			String bs_tube_email= ap.getProperty("beanstalkd.tube.email") ;
			String bs_tube_sms= ap.getProperty("beanstalkd.tube.sms") ;
			
			String[] splitTubeList = tubelist.split(",") ; 
					
			// Setup the watch connections.
			BeanstalkClient client = new BeanstalkClient(bs_server,Integer.valueOf(bs_port),null);
			BeanstalkJob job = null ;
			
			while (true) {																	// Loop Endlessly thru all the tube lists.  
				
				// Loop thru all the tubes in the list
				for (String t : splitTubeList) {
					
					//  System.out.println (String.format("Processing tube '%s'",t)) ;
					
					try {
						client.watchTube(t) ;												// Watch tube t
						
						while (true) {
							System.out.println (String.format("\nwatching tube '%s' wait for a job.  Leaving after '%s' seconds.", t, tubewait )) ;
							job = client.reserve(tubewait);									// Reserve while waiting.
							
							if (job == null) {
								System.out.println (String.format("no jobs in tube '%s', moving to the next tube",t )) ;
								break ;
							}
							
							String jsonData = new String (job.getData()) ;					// Get JSON data
							JSONObject inputJsonObj = new JSONObject (jsonData) ;			// Convert it.
							
							if (t.equalsIgnoreCase("packages")) {
							
								try {	
									QueueHandler.ProcessPackage(client, inputJsonObj, bs_tube_email, bs_tube_sms) ;
		
									// Delete the job
									System.out.println ("deleting job "  + job.getId()) ;
									client.deleteJob(job) ;
								} catch (Exception ex) {
									System.out.println ("buried the job "  + ex.getMessage()) ;
									client.bury(job, 0) ;
								}
							
							} else if (t.equalsIgnoreCase("email")) {
								
							} else if (t.equalsIgnoreCase("sms")) {  									// If SMS // TODO: change this to compare an int.
								
								int packageid = inputJsonObj.getInt("packageid") ;						// packageid
								String kineknumber = inputJsonObj.getString("kineknumber") ;			// kineknumber
								
								User recipient = new UserDao().readConsumer(kineknumber) ;
								PackageReceipt receipt = new PackageReceiptDao().read(packageid);
								
								List<String> consumerIds = new ArrayList<String>() ;
								consumerIds.add(kineknumber) ;
								
								NotificationSummary ns = recipient.getNotificationSummary() ;
								
								System.out.println ("UserId: " + recipient.getUserId() ) ;
								System.out.println ("UserId: " + recipient.getFullName()  ) ;
								System.out.println ("Is Delivery Push :" + ns.isDeliveryPushSupported()) ;
								System.out.println ("Is Delivery Text :" + ns.isDeliveryTextSupported()) ;
			
								System.out.println ("Is Tracking Push :" + ns.isTrackingPushSupported()) ;
								System.out.println ("Is Tracking Email:" + ns.isTrackingEmailSupported()) ;
								System.out.println ("Is Tracking Text :" + ns.isTrackingTextSupported()) ;
										
								DeliveryManager.sendSMSDeliveryNotification (recipient, receipt) ;
								client.deleteJob(job) ;
							} else {												// No jobs left, break.  // TODO: remove this.
								System.err.println (String.format("No queue handler for tube '%s'",t )) ;
								break ;
							}
						}
						
						
					} catch (BeanstalkException e) {
						String error = String.format("failed to watch tube '%s due to '%s",t,e.getMessage()) ;
						try {
							loggly("WorkQueueError", error) ;
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
			
			}
			
			
		} else {
			System.err.println ("Failed to load all properties") ;	
		}
	}

	
	private static boolean loadAllProperties (String mainPropertyFile) {
	
		if (QueuePropertiesExists (mainPropertyFile)) {
			loadProperties (mainPropertyFile) ;									// load workqueue.properties
			
			try {
				String externalProperties = ap.getProperty("beanstalkd.properties.external") ;
				String applicationProperties = ap.getProperty("beanstalkd.properties.application") ;
			
				if (QueuePropertiesExists (externalProperties)) {
					loadProperties (externalProperties) ;									// load externalProperties
					
					if (QueuePropertiesExists (externalProperties)) {
						System.out.println (externalProperties) ;
						loadProperties (applicationProperties) ;									// load applicationProperties
						System.out.println (applicationProperties) ;
						return true ;
					} else {
						return false ;
					}
				} else {
					return false ;
				}
			} catch (Exception ex) {
				System.err.println (String.format("property '%s' was not found in '%s'",ex.getMessage(),mainPropertyFile)) ;
			}
			
		} else {
			return false ;
		}
		
		return false ;
	}
	
	private static boolean QueuePropertiesExists (String queuefile) {
		
		boolean found = false ;
		
		System.out.print(String.format("looking for property file '%s'", queuefile )) ;
		File f = new File(queuefile);
			
		if (f.exists()){
			System.out.println(String.format("\nfound '%s'",queuefile));
			return true ;
		} else {
			System.out.println(String.format("\nfile '%s' could not be found",queuefile)) ;
		}
		return found ;
	}
	
	private static void loadProperties(String propertyfile) {
			
		Properties temp = new Properties();
		ApplicationProperty ap = ApplicationProperty.getInstance() ; 
		
		// Load properties.
		try {
			temp.load(new FileInputStream(propertyfile));
			Enumeration<Object> enumeration = temp.keys();
				while (enumeration.hasMoreElements()) {
				String key = (String) enumeration.nextElement();
				String val = (String) temp.getProperty(key) ;
				ap.addProperty(key,val);
			}
			System.out.println (String.format("loaded property file '%s'",propertyfile)) ;	
		} catch (IOException e) {
			System.out.println(String.format("failed to find property file '%s'", propertyfile)) ;
			System.exit(0);
		}
	}
	
	private static void loggly(String tag, String tmp) throws Exception { 
		
		String urlParameters = "message=" + tmp ;
		URL url = new URL("https://logs-01.loggly.com/inputs/e73ea1f0-3e79-4886-a373-54125fe8b164/tag/" + tag);
		URLConnection conn = url.openConnection();

		conn.setDoOutput(true);

		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		writer.write(urlParameters);
		writer.flush();

		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		while ((line = reader.readLine()) != null) {
		    System.out.println(line);
		}
		writer.close();
		reader.close(); 
	
	}
	
}
