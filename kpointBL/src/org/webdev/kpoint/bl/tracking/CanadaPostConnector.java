package org.webdev.kpoint.bl.tracking;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.KinekPointDao;

public class CanadaPostConnector {
	
	private static final KinekLogger logger = new KinekLogger(CanadaPostConnector.class);

	static SimpleDateFormat canadaPostDateTime =new SimpleDateFormat("yyyy/MM/dd,hh:mm:ss");
	static SimpleDateFormat canadaPostDate =new SimpleDateFormat("yyyy/MM/dd");
	private static final int canadaPostId= ExternalSettingsManager.getCanadaPostId();
	public  static final String TRACKING_NUMBER_REGEX = "[0-9]{16}$";
	
	public static Boolean checkTrackID(String trackID){
		trackID = trackID.replaceAll("\\s", "").trim();
		if(trackID.matches(TRACKING_NUMBER_REGEX))
			return true;	
		return false;	
	}

	public TrackedPackage getPackageInfo(String trackID) throws Exception{
	 	URL url;
	    InputStream is = null;
	    DataInputStream dis;
	    String s;

	    TrackedPackage result = new TrackedPackage(trackID,canadaPostId);
	    try {
	       String cp = ExternalSettingsManager.getCanadaPostUrl();
	       url = new URL(cp + trackID);
	       is = url.openStream();         // throws an IOException
	       dis = new DataInputStream(new BufferedInputStream(is));
	       boolean applicable = false;
	       String trackTable = "";
	       while ((s = dis.readLine()) != null) {
	    	  if(applicable){
	    		  trackTable = trackTable + s;
	    	  }    	   
	    	  if(s.contains("id=\"tapListResultForm:rdwf_res_div_2\"")){
	    		  trackTable = trackTable + s;
	    		  applicable = true;
	    	  }
	    	  else if(s.contains("type=\"hidden\" name=\"ID_" + trackID + "\" value=\"true\"") && applicable){
				  applicable = false;
				  break;
			  }
	       }
	       if(!trackTable.isEmpty()) result = populateTracking(result, trackTable);
	       else {
	    	   result.setIsAvailableInCourierSystem(false);
	    	   result.setHasMap(false);
	       }
	       
	    } catch (Exception e) {
	       Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Tracking Number", trackID);
	
			ApplicationException aex = new ApplicationException("Unable to convert CanadaPost HTML to tracking object.", e);
			logger.error(aex, logProps);
			
			throw aex;
			
	    } finally {
	       try {
	          if(is != null)
	        	  is.close();
	       } catch (Exception e) {
	       }
	    }
	    return result;
	}

	public static TrackedPackage populateTracking(TrackedPackage trackPackage, String canadaPostScrape){
	  //Make empty cells a bit easier to go through with regular expressions
	  canadaPostScrape = canadaPostScrape.replace("<td></td>", "<td> </td>");
	  
	  String productType = "<strong>Product Type:\\s</strong>" + "([^\\<]+)";
	  
	  String productData = matchSingleData(productType, canadaPostScrape);
	  
	  if(productData != null){
		  trackPackage.setShipmentType(productData);
		  
		  String arrivalDate = "<strong>Expected\\sDelivery\\s\\t:\\t</strong>" + "([^\\<]+)";
		  Calendar arrival = dateStringCal(matchSingleData(arrivalDate, canadaPostScrape));
		  trackPackage.setArrivalDate(arrival);
		  trackPackage.setArrivalDateHasTime(false);
		  
		  String tableRowData = "<tr class=\".*?\"><td>([^\\<]+)</td>" +
			"<td>([^\\<]+)</td>" + "<td>([^\\<]+)</td>" + "<td>([^\\<]+)</td>";
		  trackPackage.setPackageHistory(populateActivities(tableRowData, canadaPostScrape));
		  
		  List<PackageActivity> list = trackPackage.getPackageHistory();
		  
		  trackPackage.setStatus(list.get(0).getActivity());
		  
		  if(list.get(0).getActivity().toLowerCase().equals(ExternalSettingsManager.getCanadaPostDeliveredStatus())){
			  trackPackage.setIsDelivered(true);
		  }
		  
		  //Gets oldest object and calls it the estimated departure date
		  trackPackage.setDateShipped(list.get(list.size()-1).getDateTime());
		  trackPackage.setDateShippedHasTime(true);
	  }
	  else{
		  trackPackage.setIsAvailableInCourierSystem(false);
		  trackPackage.setHasMap(false);
	  }
	  return trackPackage;	  
	}

	public static String matchSingleData(String expr, String source){
	  Pattern patt = Pattern.compile(expr, Pattern.DOTALL | Pattern.UNIX_LINES);		
	  Matcher m = patt.matcher(source);	  
	  if(m.find()){
		  String results = m.group(1);
		  return results;
	  }
	  else{
		  return null;
	  }
	}

	public static List<PackageActivity> populateActivities(String expr, String source){
	  List<PackageActivity> results = new ArrayList<PackageActivity>();
	  Pattern patt = Pattern.compile(expr, Pattern.DOTALL | Pattern.UNIX_LINES);		
	  Matcher m = patt.matcher(source);
	  int i = 0;
	  //Ignore the first results as they are a duplicate copy of the most recent activity
	  while (m.find()) {
		  if(i != 0){
			  PackageActivity newActivity = new PackageActivity();
			  			  
			  String date = m.group(1);
			  String time = m.group(2);
			  if(date.equals(" ")){
				  	Calendar lastCalendar = results.get(i-2).getDateTime();
					date = lastCalendar.get(Calendar.YEAR) + "/" + (lastCalendar.get(Calendar.MONTH)+1) + "/" + lastCalendar.get(Calendar.DAY_OF_MONTH);  		
			  }			  			  
			  if(time.toLowerCase().equals("am") || time.toLowerCase().equals("pm")){  
				  time = "00:00";
			  } 
			  String dateTime= date + "," + time + ":00";
			  newActivity.setDateTime(dateTimeStringCal(dateTime));
			  
			  String city = m.group(3);
			  if(city.equals(" ") || city.equals(""))
				  newActivity.setCity(results.get(i-2).getCity());
			  else
				  newActivity.setCity(city);
			  			  
			  newActivity.setActivity(m.group(4));
			  //Assuming Canada Post only operates in Canada, we set the country to Canada for more accurate results back from google
			  newActivity.setCountry("Canada");
			  results.add(newActivity);
		  }
		  i++;
	  }
	  return results;
	}

	public static Calendar dateTimeStringCal(String s){
	  Calendar cal=Calendar.getInstance();
	  try{   
	    Date d1=canadaPostDateTime.parse(s);
	    cal.setTime(d1);
	  }
	  catch (Exception e) {
	        e.printStackTrace();
	  }
	  return cal;
	}

	public static Calendar dateStringCal(String s){
	  Calendar cal=Calendar.getInstance();
	  try{   
	    Date d1=canadaPostDate.parse(s);
	    cal.setTime(d1);
	  }
	  catch (Exception e) {
	        e.printStackTrace();
	  }
	  return cal;
	}
}
