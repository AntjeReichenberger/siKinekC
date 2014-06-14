package org.webdev.kpoint.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.util.ApplicationProperty;

import com.trendrr.beanstalk.BeanstalkClient;
import com.trendrr.beanstalk.BeanstalkException;
import com.trendrr.beanstalk.BeanstalkPool;

// http://localhost/kpoint/api/delivery/version

@Path("/delivery")
public class Delivery {

	private static final String api_version = "{ \"version\": \"00.01.04\"	}"  ;
	private static final String nouserMessage  = "{ \"results\" : [],   \"status\" : \"NO_USER\" } " ;
	private static final String noversionMessage  = "{ \"results\" : [],   \"status\" : \"NO_VERSION\" } " ;
	private static final String notokenMessage  = "{ \"results\" : [],   \"status\" : \"NO_TOKEN\" } " ;
	private static final String invalidtokenMessage  = "{ \"results\" : [],   \"status\" : \"INVALID_TOKEN\" } " ;
	private static final String expiredTokenMessage  = "{ \"results\" : [],   \"status\" : \"EXPIRED_TOKEN\" } " ;
	private static final String unauthMessage  = "{ \"results\" : [],   \"status\" : \"ZERO_RESULTS\" } " ;
	private static HashMap tokens = new HashMap() ;
	
	protected static Log log = LogFactory.getLog(Delivery.class);
	
	
	//BeanstalkClient client = new BeanstalkClient("hazen.dev.kinek.com", 8010,"packages");
		
	@Path("/systemcheck") 
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String returnProperties() {
		
		String check = "failed to load REST properties." ;
		if (loadRESTProperties()) {
			check = "loaded REST properties sucessfully." ;
		} 
		return check ;
	}

	
	
	@Path("/version")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String returnVersion() {
		return api_version  ;
	}

	@Path("/beanstalkdstatus2")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnBeanstalkStatus2() throws BeanstalkException {
		
		try {
			BeanstalkPool pool = new BeanstalkPool("hazen.dev.kinek.com",8010,30,"~example" );
			BeanstalkClient client = pool.getClient();
			
			log.info("Putting a job");
			//client.tubeStats("~example") ;
			client.close(); //closes the connection
		} catch (Exception ex) {
			log.info("Exception " + ex.getCause().getMessage());
			ex.printStackTrace() ;
		}
		return "beanstalkdstatus2 = Pool" ;
	}
	
	@Path("/beanstalkdmailbox")
	@GET
	// @Produces(MediaType.TEXT_HTML)
	public String returnBeanstalkMailbox() throws BeanstalkException {
		
		try {
			BeanstalkPool pool = new BeanstalkPool("localhost", 8010,30,"example" );
			BeanstalkClient client = pool.getClient();
			log.info("Putting a job in example");
			client.put(1l, 0, 5000, "this is some data".getBytes());
			client.tubeStats("~example") ;
			client.close(); //closes the connection
		} catch (Exception ex) {
			log.info("Exception " + ex.getCause().getMessage());
			ex.printStackTrace() ;
		}
		return "beanstalkd mailbox" ; 
	}
	
	
	@Path("/beanstalkdstatus")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnBeanstalkStatus() throws BeanstalkException {
		
		String tmp = "xxyy" ;
		
		try {
			if (loadRESTProperties()) {
				ApplicationProperty ap = ApplicationProperty.getInstance() ;
				String bs_server = ap.getProperty("beanstalkd.server") ;
				String bs_port = ap.getProperty("beanstalkd.port") ;
				Integer bs_IntPort = Integer.valueOf(bs_port) ;
				String bs_tube = ap.getProperty("beanstalkd.tube.package") ;
				System.out.println (String.format("server:",bs_server)) ;
				System.out.println (String.format("port:",bs_port)) ;
				System.out.println (String.format("intPort:",bs_IntPort)) ;
				System.out.println (String.format("tube:",bs_tube)) ;
						
				BeanstalkClient client = new BeanstalkClient(bs_server,bs_IntPort, bs_tube);
				log.info("Getting stats");
				tmp = client.tubeStats("packages") ;
				client.close(); 			//closes the connection
			}
		} catch (Exception ex) {
			log.info("Exception " + ex.getCause().getMessage());
			ex.printStackTrace() ;
		}
		return "beanstalkdstatus: " + tmp ;
	}
	

	
	@Path("/beanstalkd")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnBeanstalk() throws BeanstalkException {
		
		try {
			if (loadRESTProperties()) {
				ApplicationProperty ap = ApplicationProperty.getInstance() ;
				BeanstalkClient client = new BeanstalkClient(ap.getProperty("beanstalkd.server"),Integer.valueOf(ap.getProperty("beanstalkd.port")),ap.getProperty("beanstalkd.tube.package"));
				// BeanstalkClient client = new BeanstalkClient("hazen.dev.kinek.com", 8010, "example");
			
				log.info("Putting a job");
				client.put(1l, 0, 5000, "this is some data".getBytes());
				//BeanstalkJob job = client.reserve(60);
				//log.info("GOt job: " + job);
				//client.deleteJob(job);
				client.close(); //closes the connection
			}
		} catch (Exception ex) {
			log.info("Exception " + ex.getCause().getMessage());
			ex.printStackTrace() ;
		}
		return "beanstalkd" ;
	}
	
	
	@Path("/message2")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnMessage2(
			@QueryParam("v") String version ,
			@QueryParam("token") String token) {

		
		// Check if parameters passed.
		if ( version == null ) {
			return noversionMessage ;
		} 

		// Check if parameters passed.
		if ( token == null ) {
			return notokenMessage ;
		} 
		
		try { 
			if ( ! tokens.containsKey(UUID.fromString(token))) {
				return expiredTokenMessage  + tokens.toString() ;
			}
		} catch (Exception ex) {
			return invalidtokenMessage + tokens.toString() ;
		}
		
		UserSession us = (UserSession) tokens.get(UUID.fromString(token)) ;
		
		if ( us == null || ! isSessionValid (us) ) {
			return expiredTokenMessage ;
		}

		return "hello1 " + us.getDatetime()  ;
	}
	
	@Path("/message")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnTitle(
			@QueryParam("v") String version ,
			@QueryParam("token") String token) {

		
		
		// Check if parameters passed.
		if ( version == null ) {
			return noversionMessage ;
		} 

		// Check if parameters passed.
		if ( token == null ) {
			return notokenMessage ;
		} 
		
		try { 
			if ( ! tokens.containsKey(UUID.fromString(token))) {
				return expiredTokenMessage  + tokens.toString() ;
			}
		} catch (Exception ex) {
			return invalidtokenMessage + tokens.toString() ;
		}
		
		UserSession us = (UserSession) tokens.get(UUID.fromString(token)) ;
		
		if ( us == null || ! isSessionValid (us) ) {
			return expiredTokenMessage ;
		}

		return "hello1 " + us.getDatetime()  ;
	}
	
	
	
	@Path("/hashmap")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnHashMap() {
		
		String output = "" ;
		Iterator iter = tokens.entrySet().iterator();
		
		ArrayList<String> al = new ArrayList<String>() ;
		
		while (iter.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter.next();
			UserSession us = (UserSession) mEntry.getValue() ;
			output = output + mEntry.getKey() + " : " + us.getDatetime() + "\r" ;
		}
		
		return output  ;
	}


	// ------------------------------------------------------------------------------------------------------------------------------
	// Receive Package
	// ------------------------------------------------------------------------------------------------------------------------------
	@Path("/receivepackage")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response receivePackage (JSONObject inputJsonObj)  {

		JSONObject message = new JSONObject() ;
		// Validate passed object.
		
		try {
			
			String version = inputJsonObj.getString("version") ;			// ignore version for now.
			String token = inputJsonObj.getString("token") ;
			String kineknumber = inputJsonObj.getString("kineknumber") ;
			String courier = inputJsonObj.getString("courier") ;
			String weight = inputJsonObj.getString("weight") ;
			String dutytaxes = inputJsonObj.getString("dutytaxes") ;
			String shipped = inputJsonObj.getString("shipped") ;
			String info = inputJsonObj.getString("info") ;
			

			// System.out.println ("/receivepackage version: " + version) ;
			// System.out.println ("/receivepackage token: " + token) ;
			// System.out.println ("/receivepackage Kineknumber: " + kineknumber) ;
			// System.out.println ("/receivepackage Courier: " + courier) ;
			// System.out.println ("/receivepackage Weight: " + weight) ;
			// System.out.println ("/receivepackage DutyTaxes: " + dutytaxes) ;
			// System.out.println ("/receivepackage Shipped: " + shipped) ;
			// System.out.println ("/receivepackage Info: " + info ) ;
			
			// Is the token empty?
			if ( token == null || token.isEmpty()) {
				message.put("status","-1") ;
				message.put("message","missing token") ;
				return Response.status(Response.Status.FORBIDDEN).entity(message.toString()).build();
				
				// return Response.ok(message.toString()).build();
			}
			
			// Is token current?
			if ( ! tokens.containsKey(UUID.fromString(token))) {
				message.put("status","-1") ;
				message.put("message","Your token has expired. Please login again.") ;
				return Response.status(Response.Status.UNAUTHORIZED).entity(message.toString()).build();
				
//				return Response.ok(message.toString()).build();
			}

			UserSession us = (UserSession) tokens.get(UUID.fromString(token)) ;
			
			// Is User Session ok?
			if ( us == null || ! isSessionValid (us) ) {
				message.put("status","-1") ;
				message.put("message","token has expired") ;
				return Response.ok(message.toString()).build();
			}

			User kpuser = us.getKpuser() ;
			KinekPoint kp = us.getKinekpoint() ;
				
			if (kpuser != null) {

				// Receive Package
				if (loadRESTProperties()) {
					String packageJSON = String.format("{ version: \"1\", token: \"%s\", app: \"MOBILE_WEB\", depotuserid: \"%s\", depotnumber: \"%s\", kineknumber: \"%s\", courier: \"%s\", weight: \"%s\", skid: \"%s\", taxes: \"%s\", shipped: \"%s\", info: \"%s\" }",token,kpuser.getUserId(),kp.getDepotId(),kineknumber,courier,weight,"false",dutytaxes,shipped,info) ;
					System.out.println ("JSON:" + packageJSON) ;
					
					ApplicationProperty ap = ApplicationProperty.getInstance() ;
					
					BeanstalkClient client = new BeanstalkClient(ap.getProperty("beanstalkd.server"),Integer.valueOf(ap.getProperty("beanstalkd.port")),ap.getProperty("beanstalkd.tube.package"));
					System.out.println (String.format("After BeanstalkClient %s, %s, %s",ap.getProperty("beanstalkd.server"),Integer.valueOf(ap.getProperty("beanstalkd.port")),ap.getProperty("beanstalkd.tube.package") )) ;

		
					try {
						byte[] pkg = packageJSON.getBytes() ;
						client.put(1l, 0, 5000, pkg);
						message.put("status","1") ;
						message.put("message","package accepted") ;
						client.close() ;

					} catch (BeanstalkException ex) {
						Throwable cause = ex.getCause();
						System.out.println ("Exception: " + cause != null ? cause.getMessage() : "unknown" ) ;
						message.put("status","-1") ;
						message.put("message","failed to submit package") ;
						// return Response.serverError().entity("failed to submit package").build();
						try {
							client.close() ;
						} catch (Exception ex2) {}
						
						return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(message.toString()).build();
						// return Response.serverError().message.toString()).build();
						
						// return Response.(message.toString()).build();
					}
				}
			

			} else {
				message.put("status","0") ;
				message.put("message",String.format("Kinek user (%s) was not found",kineknumber)) ;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//message.put("status", "-1") ;
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return Response.ok(message.toString()).build();
	}

	// ------------------------------------------------------------------------------------------------------------------------------
	//  Get Customer
	// ------------------------------------------------------------------------------------------------------------------------------
	@Path("/getcustomers")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCustomers (JSONObject inputJsonObj)  {

		JSONArray  messageArray = new JSONArray() ;
		JSONObject message = new JSONObject() ;
		// Validate passed object.
		
		try {
			String version = inputJsonObj.getString("version") ;			// ignore version for now.
			String token = inputJsonObj.getString("token") ;
			String firstname = inputJsonObj.getString("firstname") ;
			String lastname = inputJsonObj.getString("lastname") ;
			String phonenumber = inputJsonObj.getString("phonenumber") ;

			// Is the token empty?
			if ( token == null || token.isEmpty()) {
				message.put("status","-1") ;
				message.put("message","missing token") ;
				return Response.ok(message.toString()).build();
			}
			
			// Is token current?
			if ( ! tokens.containsKey(UUID.fromString(token))) {
				message.put("status","-1") ;
				message.put("message","Your token has expired. Please login again.") ;
				return Response.ok(message.toString()).build();
			}

			UserSession us = (UserSession) tokens.get(UUID.fromString(token)) ;
			
			// Is User Session ok?
			if ( us == null || ! isSessionValid (us) ) {
				message.put("status","-1") ;
				message.put("message","token has expired") ;
				return Response.ok(message.toString()).build();
			}

			List<User> kpusers = new UserDao().fetchConsumersByNamesOrPhone(firstname,lastname,phonenumber) ;
			
			if ( kpusers == null || kpusers.size() < 1) {
				message.put("status","-1") ;
				message.put("message","Customer not found") ;
				return Response.ok(message.toString()).build();
			} else {
				//
				
				JSONObject stat = new JSONObject() ;
				stat.put("status","1") ;
				stat.put("message",kpusers.size()) ;
				
				for ( User usr : kpusers) {
					
					message = new JSONObject() ;
					String fulladdressLine1 = getFullAddress (usr, 1) ;
					String fulladdressLine2 = getFullAddress (usr, 2) ;
					
					message.put("number", usr.getKinekNumber() ) ; 
					message.put("fullname", usr.getFullName() ) ;
					message.put("addressLine1", usr.getAddress1() ) ;
					message.put("addressLine2", usr.getAddress2() ) ;
					message.put("city", usr.getCity() ) ;
					message.put("state", usr.getState() != null ? usr.getState().getName() : "" ) ;
					message.put("zip", usr.getZip() ) ;
					message.put("fulladdress1", fulladdressLine1) ;
					message.put("fulladdress2", fulladdressLine2) ;
					
					System.out.println ("KPUSER : " + usr.getFullName() + ", " + fulladdressLine1 + " -- " + fulladdressLine2 ) ;
					
					messageArray.put(message) ;
				}
				stat.put("data", messageArray) ;
				System.out.println ("KPUSER 2: " + kpusers.size()) ;
				return Response.ok(stat.toString()).build();
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//message.put("status", "-1") ;
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return Response.ok(message.toString()).build();
	}

	
	// ------------------------------------------------------------------------------------------------------------------------------
	//  Get Customer
	// ------------------------------------------------------------------------------------------------------------------------------
	@Path("/getaddress")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAddress (JSONObject inputJsonObj)  {

		JSONObject message = new JSONObject() ;
		// Validate passed object.
		
		try {
			String version = inputJsonObj.getString("version") ;			// ignore version for now.
			String token = inputJsonObj.getString("token") ;
			String kineknumber = inputJsonObj.getString("kineknumber") ;

			// Is the token empty?
			if ( token == null || token.isEmpty()) {
				message.put("status","-1") ;
				message.put("message","missing token") ;
				return Response.ok(message.toString()).build();
			}
			
			// Is token current?
			if ( ! tokens.containsKey(UUID.fromString(token))) {
				message.put("status","-1") ;
				message.put("message","Your token has expired. Please login again.") ;
				return Response.ok(message.toString()).build();
			}

			UserSession us = (UserSession) tokens.get(UUID.fromString(token)) ;
			
			// Is User Session ok?
			if ( us == null || ! isSessionValid (us) ) {
				message.put("status","-1") ;
				message.put("message","token has expired") ;
				return Response.ok(message.toString()).build();
			}

			User kpuser = new UserDao().readConsumer(kineknumber);
			
			//List<User> kpuser2 = new UserDao().fetchConsumersByNamesOrPhone("glen" , "graham", null) ;
			//System.out.println ("KPUSER 2: " + kpuser2.size()) ;
			
			if (kpuser != null) {

				System.out.println("Status:1") ; 
			
				String fulladdressLine1 = getFullAddress (kpuser, 1) ;
				String fulladdressLine2 = getFullAddress (kpuser, 2) ;
				
				message.put("status","1") ;
				message.put("message","found " + kpuser.getFullName() ) ;
				message.put("number", kineknumber) ;
				message.put("fullname", kpuser.getFullName() ) ;
				message.put("addressLine1", kpuser.getAddress1()) ;
				message.put("addressLine2", kpuser.getAddress2()) ;
				message.put("city", kpuser.getCity()) ;
				message.put("state", kpuser.getState().getName()) ;
				message.put("zip", kpuser.getZip().toUpperCase()) ;
				message.put("fulladdress1", fulladdressLine1) ;
				message.put("fulladdress2", fulladdressLine2) ;
				
			} else {
				message.put("status","0") ;
				message.put("message",String.format("Kinek user (%s) was not found",kineknumber)) ;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//message.put("status", "-1") ;
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return Response.ok(message.toString()).build();
	}
		 
		 
		
//		String message = "{ \"results\" : [],   \"status\" : \"ZERO_RESULTS\" } " ;
//		User user = null ;
//		
//		try {
//			user = new UserDao().authenticateAdmin((String) inputJsonObj.get("username"), (String) inputJsonObj.get("password"));
//			
//			if (user != null) {
//				String address1 = user.getAddress1() ;
//				String email = user.getEmail() ;
//		
//				UUID token = UUID.randomUUID() ;	
//				UserSession us = UserSession.getInstance(user);
//				
//				tokens.put(token,us) ;				
//				
//				message = "{ \"token\": \"" + token.toString() + "\"	}"  ;
//			} 
//		} catch (Exception ex) {
//			ex.printStackTrace() ;
//			
//		}
//		 return Response.ok(message).build();
//	}
	
	
	
	// ------------------------------------------------------------------------------------------------------------------------------
	//  Authenticate
	// ------------------------------------------------------------------------------------------------------------------------------
	@Path("/authenticate")
	@POST 
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authenticateToken (JSONObject inputJsonObj) {

		String message = "{ \"results\" : [],   \"status\" : \"ZERO_RESULTS\" } " ;
		User user = null ;
		KinekPoint receivingKinekPoint = null ;
		String acceptsDutyAndTaxes = "" ;
		
		try {
			UserDao userDao = new UserDao();
			
			user = userDao.authenticateAdmin((String) inputJsonObj.get("username"), (String) inputJsonObj.get("password"));
			
			if ( user != null) {
				System.out.println ("UserId:" + user.getUserId()) ;
				List<KinekPoint> userDepots = userDao.fetchUserKinekPoints(user.getUserId());
				if (userDepots.size() > 1){
					KinekPointDao kinekPointDao = new KinekPointDao();
					receivingKinekPoint = kinekPointDao.read(user.getDepot().getDepotId());
				} else {
					receivingKinekPoint = userDepots.get(0);
				}
				System.out.println ("Receiving KP:" + receivingKinekPoint.getName()) ;
				System.out.println ("Receiving KP:" + receivingKinekPoint.getDepotId() ) ;
				System.out.println ("Receiving KP acceptsDutyAndTaxes:" + receivingKinekPoint.getAcceptsDutyAndTax()  ) ;
				acceptsDutyAndTaxes = receivingKinekPoint != null && receivingKinekPoint.getAcceptsDutyAndTax() ? "false" : "false" ;

			} else {
				System.out.println ("UserId: unknown") ;
			}
			 
			if (user != null && receivingKinekPoint != null) {
				String address1 = user.getAddress1() ;
				String email = user.getEmail() ;
		
				
				UUID token = UUID.randomUUID() ;	
				UserSession us = UserSession.getInstance(user,receivingKinekPoint);
				
				tokens.put(token,us) ;				
				
				message = "{ \"token\": \"" + token.toString() + "\", \"acceptsDutyAndTax\": " + acceptsDutyAndTaxes + " }"  ;
			}  else {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace() ;
			
		}
		 return Response.ok(message).build();
	}
	
	// ------------------------------------------------------------------------------------------------------------------------------
	//  Authenticate
	// ------------------------------------------------------------------------------------------------------------------------------
	@Path("/logout")
	@POST 
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logout (JSONObject inputJsonObj) {

		String message = "{ \"results\" : [],   \"status\" : \"ZERO_RESULTS\" } " ;
		String strToken = "" ;
		
		try {
			strToken =  (String) inputJsonObj.get("token") ;
			UUID token = UUID.fromString(strToken) ;
			tokens.remove(token) ;	
			message = "{ \"results\" : [],   \"status\" : \"TOKEN_REMOVED\" } " ;

		} catch (Exception ex) {
			message = "{ \"results\" : [],   \"status\" : \"UNKNOWN_TOKEN\" } " ;
		}
		
	    return Response.ok(message).build();
	}
	

	public static class UserSession {
		
		private User kpuser ;
		private Date datetime ;
		private Date expires ;
		private int counter ;
		private KinekPoint kinekpoint ;
		
		public static UserSession getInstance(User usr, KinekPoint kp) {
			
			UserSession us = new UserSession() ;			// Creates object
			us.datetime = new Date()  ;
			
			Calendar cal = Calendar.getInstance(); 			// creates calendar
			cal.setTime(us.datetime); 						// sets calendar time/date
			cal.add(Calendar.HOUR_OF_DAY, 3); 				// adds 3 hours
			cal.getTime(); 									// returns new date object, one hour in the future
			
			us.expires = cal.getTime();
			us.kpuser = usr ;
			us.counter = 0 ;								// Initialized.
			us.kinekpoint = kp ;
			
			return us ;
		}
		
		
		public KinekPoint getKinekpoint() {
			return kinekpoint;
		}


		public void setKinekpoint(KinekPoint kinekpoint) {
			this.kinekpoint = kinekpoint;
		}


		public User getKpuser() {
			return kpuser;
		}

		public void setKpuser(User kpuser) {
			this.kpuser = kpuser;
		}

		public Date getDatetime() {
			return datetime ;
		}
		
		public Date getExpires() {
			return expires ;
		}

		public int getCounter() {
			return counter;
		}

		public void setCounter(int counter) {
			this.counter = counter;
		}
		
		
	}
	
	private boolean isSessionValid (UserSession us) {

		try {
			Calendar now = Calendar.getInstance(); 			// creates calendar
			
			Calendar sessionCalendar = Calendar.getInstance() ;
			sessionCalendar.setTime(us.getExpires()) ;
			
			if (now.after(sessionCalendar)){
			    return false ; 
			} else {
			    return true ;  
			}
		} catch (Exception ex) {
			return false ;
		}
		
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
			
		}
	}
	
	private static boolean loadRESTProperties() {
		
		ApplicationProperty ap = ApplicationProperty.getInstance() ;
		boolean proploaded = false ;
		
		try {
			if (ap.getProperty("beanstalkd.properties.loaded").equalsIgnoreCase("true")) {
				System.out.println ("beanstalkd.properties are in cache") ;
				proploaded = true ;	
			}
			
		} catch (Exception ex) {
			try {
				String beanstalkdProperties = ap.getProperty("beanstalkd.properties.file") ;
				if (QueuePropertiesExists(beanstalkdProperties)) {
					loadProperties (beanstalkdProperties) ;
					ap.addProperty("beanstalkd.properties.loaded","true") ;
					System.out.println ("beanstalkd.properties were loaded and put in cache") ;
					proploaded = true ;
				}
			} catch (Exception ex2) {
				System.out.println ("Failed to loadRESTProperties due to " + ex2.getMessage()) ;
			}
		}
			
		return proploaded ;
		
	}
	
	private static String getFullAddress (User usr, int line) {
		
		String fulladdress = "" ;
		
		boolean addressLine1ok = ( usr.getAddress1() != null && ! usr.getAddress1().isEmpty()) ;
		boolean addressLine2ok = ( usr.getAddress2() != null && ! usr.getAddress2().isEmpty()) ;
		
		if ( line == 1 ) {
			if (addressLine1ok)  {
				fulladdress += usr.getAddress1() ;
			}
			if (usr.getCity() != null) {
				fulladdress += ", " + usr.getCity() ;
			}

			
		} else {
			if (addressLine2ok)  {
				fulladdress += ", " + usr.getAddress2() ;
			} 

			if (usr.getState() != null) {
				if (usr.getState().getName() != null ) {
					fulladdress += usr.getState().getName() ;	
				}
			}

			if ( usr.getZip() != null ) {
				String zip = (" " + usr.getZip()).toUpperCase() ; 
				fulladdress += zip  ;
			}

		}
		
		return fulladdress ;
			
	}
		
		//	ap.addProperty(key,val);
		
		
		
		
//		//TODO: This is expensive ... clean this up. But need ability to tell properties are not configured.
//		boolean loaded = false ;
//		boolean debug = debugFlag != null && debugFlag.equalsIgnoreCase("true") ? true : false ;
//		String tmp = null ;
//		String[] propertylist = {"beanstalkd.server","beanstalkd.port","beanstalkd.tube.package","beanstalkd.tube.email","beanstalkd.tube.sms"} ;
//				
		
		
//		if ( ap.get)
//		try {
//			// Check that all required properties exist.
//			for (String s : propertylist) {
//				tmp = ap.getProperty(s) ;	
//			}
//			loaded = true ;
//		} catch (Exception ex) {
//			try {
//				String beanstalkdProperties = ap.getProperty("beanstalkd.properties.file") ;
//				
//				if (QueuePropertiesExists(beanstalkdProperties)) {
//					loadProperties (beanstalkdProperties) ;
//					tmp = ap.getProperty("beanstalkd.server") ;
//					tmp = ap.getProperty("beanstalkd.port") ;
//					tmp = ap.getProperty("beanstalkd.tube.package") ;
//					tmp = ap.getProperty("beanstalkd.tube.email") ;
//					tmp = ap.getProperty("beanstalkd.tube.sms") ;
//					loaded = true ;
//				}
//			
//			} catch (Exception ex2) {
//				
//			}
		
		
		
		
		
		
		
		
		
		
		
//		
//		StringWriter sw = new StringWriter() ;
//		sw.append("SystemCheck loadRESTProperties");
//		sw.append("\n------------------------------------"); 
//		
//		String bs_server = null ;
//		String bs_port = null ;
//		String beanstalkdProperties = null ;
//		String bs_tube_packages= null ;
//		String bs_tube_email= null ;
//		String bs_tube_sms= null ; 
//		
//		try {
//			
//			bs_server = ap.getProperty("beanstalkd.server") ;
//			sw.append(String.format("\n%s= %s","beanstalkd.server",bs_server)) ;
//			bs_port = ap.getProperty("beanstalkd.port") ;
//			sw.append(String.format("\n%s= %s","beanstalkd.port",bs_port)) ;
//			bs_tube_packages=ap.getProperty("beanstalkd.tube.package") ;
//			sw.append(String.format("\n%s= %s","beanstalkd.tube.package",bs_tube_packages)) ;
//			
//			bs_tube_email= ap.getProperty("beanstalkd.tube.email") ;
//			sw.append(String.format("\n%s= %s","beanstalkd.tube.email",bs_tube_email)) ;
//
//			bs_tube_sms= ap.getProperty("beanstalkd.tube.sms") ;
//			sw.append(String.format("\n%s= %s","beanstalkd.tube.sms",bs_tube_sms)) ;
//			
//			sw.append("\n\nProperties were previously loaded") ;
//			loaded = true ;
//			
//		} catch (Exception ex) {
//			
//			try {
//				beanstalkdProperties = ap.getProperty("beanstalkd.properties.file") ;
//				sw.append("\nbeanstalkd.properties.file=" + beanstalkdProperties);
//				
//				if (! QueuePropertiesExists(beanstalkdProperties)) {
//					sw.append(String.format("\n'%s' does not exist",beanstalkdProperties)) ;
//				} else {
//					sw.append(String.format("\nfile '%s' exists: %s",beanstalkdProperties, QueuePropertiesExists(beanstalkdProperties) )) ;  
//					
//					sw.append(String.format("\nloading '%s'",beanstalkdProperties)) ;
//					loadProperties (beanstalkdProperties) ;
//					sw.append(String.format("\n'%s' loaded",beanstalkdProperties)) ;
//					
//					bs_server = ap.getProperty("beanstalkd.server") ;
//					sw.append(String.format("\n%s= %s","beanstalkd.server",bs_server)) ;
//
//					bs_port = ap.getProperty("beanstalkd.port") ;
//					sw.append(String.format("\n%s= %s","beanstalkd.port",bs_port)) ;
//					
//					bs_tube_packages=ap.getProperty("beanstalkd.tube.package") ;
//					sw.append(String.format("\n%s= %s","beanstalkd.tube.package",bs_tube_packages)) ;
//					
//					bs_tube_email= ap.getProperty("beanstalkd.tube.email") ;
//					sw.append(String.format("\n%s= %s","beanstalkd.tube.email",bs_tube_email)) ;
//
//					bs_tube_sms= ap.getProperty("beanstalkd.tube.sms") ;
//					sw.append(String.format("\n%s= %s","beanstalkd.tube.sms",bs_tube_sms)) ;
//					
//					sw.append("\n\nProperties were NOT previously loaded") ;
//					loaded = true ;
//				}
//
//			} catch (Exception ex2) {
//				sw.append(String.format("\n could not find the property file '%s' or load its properties",beanstalkdProperties)) ;
//				
//				
//			}
//			
//		}
//		System.out.println (sw.toString()) ;
//		return loaded   ;
//		
//		
//		
//	}

}
