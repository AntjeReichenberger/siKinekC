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

import org.webdev.kpoint.bl.manager.DeliveryManager;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.util.ApplicationProperty;

import com.trendrr.beanstalk.BeanstalkClient;
import com.trendrr.beanstalk.BeanstalkException;
import com.trendrr.beanstalk.BeanstalkJob;

public class ProcessEmailQueue {

	/**
	 * @param args
	 */
	
	protected static Log log = LogFactory.getLog(ProcessEmailQueue.class);
	
	
	public static void main(String[] args) throws Exception {
		// args: C:\\development\\KinekWorkspaces\\KpointWorker\\res\\external.properties
		
		System.out.println ("loading properties") ;
		for (String arg : args) {
			System.out.println ("  " + arg) ;
		}
		
		loadProperties (args) ;
		
		ApplicationProperty ap = ApplicationProperty.getInstance() ;
		
		String bs_server = ap.getProperty("beanstalkd.server") ;
		String bs_port =   ap.getProperty("beanstalkd.port") ;
		String bs_tube_packages=ap.getProperty("beanstalkd.tube.package") ;
		String bs_tube_email=ap.getProperty("beanstalkd.tube.email") ;
		String bs_tube_sms=ap.getProperty("beanstalkd.tube.sms") ;
		
		System.out.println ("connecting to Beanstalkd") ;
		System.out.println ("  bs_server:" + bs_server)  ;
		System.out.println ("  bs_port:" + bs_port)  ;
		System.out.println ("  bs_tube_packages:" + bs_tube_packages)  ;
		System.out.println ("  bs_tube_email:" + bs_tube_email)  ;
		System.out.println ("  bs_tube_sms:" + bs_tube_sms)  ;
		
		Integer port = Integer.valueOf(bs_port) ;
		
		BeanstalkClient client = new BeanstalkClient(bs_server, port ,bs_tube_email);
		
		System.out.println ("starting to work email queue") ;
		try {
			
			int ctr = 0 ;
			while (true) {

				client.useTube(bs_tube_email) ;								// Packages tube.
				
				System.out.println ("starting reserve " + ctr++ ) ;
				BeanstalkJob job = client.reserve(1);							// Wait 1 second for job.
				
				if (job == null) {												// No jobs left, break.
					System.out.println ("no jobs left, breaking " ) ;
					break ;
				}
				String jsonData = new String (job.getData()) ;					// Get JSON data
				System.out.println ("GOT:" + jsonData) ;
				
				loggly("email" , jsonData) ;
				JSONObject inputJsonObj = new JSONObject (jsonData) ;			// Convert it.

				// String emailJSON = "{ depotuserid: \"" + userid +"\", packageid: \"" + receipt.getId() + "\", kineknumber: \"" + kineknumber + "\"}" ;
				
				int depotuserid = inputJsonObj.getInt("depotuserid") ;				// depot userid
				int packageid = inputJsonObj.getInt("packageid") ;					// packageid
				String kineknumber = inputJsonObj.getString("kineknumber") ;		// kineknumber
				
				
				User depotUser = new UserDao().read(depotuserid) ;
				PackageReceipt receipt = new PackageReceiptDao().read(packageid);
				
				List<String> consumerIds = new ArrayList<String>() ;
				consumerIds.add(kineknumber) ;
				
				DeliveryManager.sendEmailDeliveryNotification (depotUser,receipt,consumerIds);
				
				// Delete the job
				System.out.println ("deleting job "  + job.getId()) ;
				// client.deleteJob(job) ;

			}
			client.close() ;
			
		} catch (BeanstalkException ex) {
			ex.printStackTrace() ;
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	private static void loadProperties(String[] args) {
		
		Properties temp = new Properties();
		
		// See if any arguments were supplied.
		if ( args == null || args.length < 1) {
			System.out.println(("path to external.properties not supplied")) ;
		} else {
			System.out.print((String.format("your argument was '%s'", args[0]))) ;
			
			for (String arg : args) {
				// Load properties.
				try {
					File file = new File (arg) ;
					temp.load(new FileInputStream(file));
					Enumeration<Object> enumeration = temp.keys();
					while (enumeration.hasMoreElements()) {
						String key = (String) enumeration.nextElement();
						String val = (String) temp.getProperty(key) ;
						ApplicationProperty.getInstance().addProperty(key,val);
					}
		 
				} catch (IOException e) {
					System.out.println(String.format("\n\rFailed to find external.properties at '%s'",args[0])) ;
					System.exit(0);
				}
			}
		}
	}

}
