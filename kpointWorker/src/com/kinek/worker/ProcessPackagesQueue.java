package com.kinek.worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.webdev.kpoint.bl.manager.DeliveryManager;
import org.webdev.kpoint.bl.persistence.CourierDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PackageWeightGroupDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.util.ApplicationProperty;

import com.trendrr.beanstalk.BeanstalkClient;
import com.trendrr.beanstalk.BeanstalkException;
import com.trendrr.beanstalk.BeanstalkJob;

public class ProcessPackagesQueue {

	/**
	 * @param args
	 */
	
	protected static Log log = LogFactory.getLog(ProcessPackagesQueue.class);
	
	
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
		
		BeanstalkClient client = new BeanstalkClient(bs_server, port ,bs_tube_packages);
		
		System.out.println ("starting to work packages queue") ;
		try {
			
			int ctr = 0 ;
			while (true) {

				client.useTube(bs_tube_packages) ;								// Packages tube.
				
				System.out.println ("starting reserve " + ctr++ ) ;
				BeanstalkJob job = client.reserve(1);							// Wait 1 second for job.
				
				if (job == null) {												// No jobs left, break.
					System.out.println ("no jobs left, breaking " ) ;
					break ;
				}
				String jsonData = new String (job.getData()) ;					// Get JSON data
				
				loggly("packages" , jsonData) ;
				JSONObject inputJsonObj = new JSONObject (jsonData) ;			// Convert it.
				
				String userAPP = inputJsonObj.getString("app") ;				// app
				int userid = inputJsonObj.getInt("depotuserid") ;					// userid
				String version = inputJsonObj.getString("version") ;			// ignore version for now.
				String token = inputJsonObj.getString("token") ;
				int depotId = inputJsonObj.getInt("depotnumber") ;
				String kineknumber = inputJsonObj.getString("kineknumber") ;

				// Get the Depot.
				KinekPointDao kinekPointDao = new KinekPointDao();
				KinekPoint receivingKinekPoint = kinekPointDao.read(depotId) ;
						
				boolean skid = inputJsonObj.getBoolean("skid") ;
				BigDecimal taxes = new BigDecimal (inputJsonObj.getDouble("taxes")) ;
				int courier = inputJsonObj.getInt("courier") ;
				int weightId = inputJsonObj.getInt("weight") ;
				String shippedFrom = inputJsonObj.getString("shipped") ;
				String customInfo = inputJsonObj.getString("info") ;
				System.out.println ( inputJsonObj.toString()) ;
				
				Set<Package> packages = new HashSet<Package>();
				Package packageObj = new Package();
				
				packageObj.setCourier(new CourierDao().read(courier));

				packageObj.setDutyAndTax(taxes);
				packageObj.setCustomInfo(customInfo);
				packageObj.setShippedFrom(shippedFrom);
				packageObj.setPackageWeightGroup(new PackageWeightGroupDao().read(weightId));
				packageObj.setPickupFee(DeliveryManager.getPackagePickupFee(receivingKinekPoint, weightId));
				packageObj.setOnSkid(skid);
				packageObj.setSkidFee(DeliveryManager.getPackageSkidFee(receivingKinekPoint, skid));
				packages.add(packageObj);

				//CALL BL
				List<String> selectedConsumerList = new ArrayList<String>()  ;
				selectedConsumerList.add(kineknumber) ;
				
				User kpuser = new UserDao().read(userid) ;						// Authenticated user.
				kpuser.setApp(User.App.valueOf(userAPP)) ;						// Set the userapp.
				
				PackageReceipt receipt = DeliveryManager.acceptDeliveryWithoutNotification (kpuser, receivingKinekPoint, selectedConsumerList, packages);
				
				// Place receipt in email Queue
				client.useTube(bs_tube_email) ;
				String emailJSON = "{ depotuserid: \"" + userid +"\", packageid: \"" + receipt.getId() + "\", kineknumber: \"" + kineknumber + "\"}" ;
				client.put(1l, 0, 5000, emailJSON.getBytes());
				
				// Place receipt in SMS Queue
				client.useTube(bs_tube_sms) ;
				String smsJSON = "{ kineknumber: \"" + kineknumber +"\", packageid: \"" + receipt.getId() + "\"}" ;
				client.put(1l, 0, 5000, smsJSON.getBytes());
				
				// Delete the job
				System.out.println ("deleting job "  + job.getId()) ;
				client.deleteJob(job) ;

			}
			client.close() ;
			
		} catch (BeanstalkException ex) {
			ex.printStackTrace() ;
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//				
//				String version = inputJsonObj.getString("version") ;			// ignore version for now.
//				String token = inputJsonObj.getString("token") ;
//				String kineknumber = inputJsonObj.getString("kineknumber") ;
//				String courier = inputJsonObj.getString("courier") ;
//				String weight = inputJsonObj.getString("weight") ;
//				String shipped = inputJsonObj.getString("shipped") ;
//				String info = inputJsonObj.getString("info") ;
//				System.out.println ( inputJsonObj.toString()) ;
// catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			
			
			
			
//			while (true) {
//				log.info("Waiting..." );
//				BeanstalkJob job = client.reserve(60);
//			
//				log.info("Got job: " + job.getId());
//				String s = new String(job.getData());
//			
//				log.info("Got data:" + s) ;
//				client.deleteJob(job) ;
//			}
//	
//		} catch (BeanstalkException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	//}
		
	private static void loggly(String tag, String tmp) throws Exception { 
	
		String urlParameters = "message=" + tmp ;
		URL url = new URL("https://logs-01.loggly.com/inputs/e73ea1f0-3e79-4886-a373-54125fe8b164/tag/" + tag );
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
						ApplicationProperty.getInstance().addProperty(key,temp.getProperty(key));
					}
		 
				} catch (IOException e) {
					System.out.println(String.format("\n\rFailed to find external.properties at '%s'",args[0])) ;
					System.exit(0);
				}
			}
		}
	}

}
