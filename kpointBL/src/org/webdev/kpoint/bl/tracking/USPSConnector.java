package org.webdev.kpoint.bl.tracking;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.xml.sax.InputSource;

public class USPSConnector {
	
	private static final KinekLogger logger = new KinekLogger(USPSConnector.class);
	
	private static SimpleDateFormat usps =new SimpleDateFormat("MMMMMMMM dd, yyyy hh:mm aa");
	private static SimpleDateFormat uspsNoTime =new SimpleDateFormat("MMMMMMMM dd, yyyy");
	private final String baseURL = ExternalSettingsManager.getUSPSEndpoint();
	private String userName = ExternalSettingsManager.getUSPSUsername();
	private static final int uspsId= ExternalSettingsManager.getUSPSId();
	public  static final String TRACKING_NUMBER_REGEX = "(\\D{2}\\d{9}\\D{2})|(9\\d{21})";
		
	public static Boolean checkTrackID(String trackingNumber){
		trackingNumber = trackingNumber.replaceAll("\\s", "").trim(); //ensure leading and trailing spaces / tabs are removed
		if(trackingNumber.matches(TRACKING_NUMBER_REGEX))
			return true;
		else if(trackingNumber.length() == 20)
			return checkDigitMatch(trackingNumber);
		
		return false;	
	}

	/**
	 * Check digit algorithm for 20 digit USPS numbers
	 * @param trackID
	 * @return
	 */
	private static boolean checkDigitMatch(String trackingNumber){
		char[] trackDigitArray = trackingNumber.toCharArray();
		int step1 = 0;
		for(int i=trackDigitArray.length-2; i>=0; i=i-2)
		{
			step1 += Integer.parseInt(String.valueOf(trackDigitArray[i]));
		}
		
		int step2 = step1*3;
		
		int step3 = 0;
		for(int i=trackDigitArray.length-3; i>=0; i=i-2)
		{
			step3 += Integer.parseInt(String.valueOf(trackDigitArray[i]));
		}
		
		int step4 = step2 + step3;
		
		int checkDigit = step4 % 10;
		if(checkDigit != 0)
			checkDigit = 10 - checkDigit;
		
		if(checkDigit == Integer.parseInt(String.valueOf(trackDigitArray[trackDigitArray.length-1])))
			return true;
		
		return false;
	}
	
	public TrackedPackage getPackageInfo(String trackID) throws Exception{
		trackID = trackID.replaceAll(" ", "");
		String output = "";
		String URL = baseURL + "?API=TrackV2&XML=<TrackFieldRequest%20USERID=\"" + userName + "\"><TrackID%20ID=\"" + trackID + "\"></TrackID></TrackFieldRequest>";
		output = requestUSPS(URL); 
		return convertUSPS(output, trackID);
	}		

	public String requestUSPS(String requestURL){
		String output = ""; 
		try {
			URL url = new URL(requestURL);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				output += inputLine;
			}
			in.close();       
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	public static TrackedPackage convertUSPS(String xmlInput, String trackingNumber) throws Exception{
		TrackedPackage result = new TrackedPackage(trackingNumber,uspsId);
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();

			is.setCharacterStream(new StringReader(xmlInput));
			Document doc = db.parse(is);

			String status = TrackingManager.getXMLValue(doc, "TrackSummary","Event");
			
			if(status == null){
				return null;
			}
			result.setStatus(status);	

			if(status.toLowerCase().equals(ExternalSettingsManager.getUSPSDeliveredStatus())){
				result.setIsDelivered(true);
			}
			
			result.setArrivalDateHasTime(true);
			result.setDateShippedHasTime(true);

			NodeList nodes = doc.getElementsByTagName("TrackDetail");
			List<PackageActivity> packageHistory = new ArrayList<PackageActivity>();

			PackageActivity newestActivity = new PackageActivity();
			newestActivity.setActivity(status);

			String timeStr = TrackingManager.getXMLValue(doc, "TrackSummary","EventTime");
			String dateStr = TrackingManager.getXMLValue(doc, "TrackSummary","EventDate");
			
			if(timeStr == null){
				newestActivity.setDateTime(TrackingManager.dateStringCal(dateStr,uspsNoTime));
			}
			else{
				newestActivity.setDateTime(TrackingManager.dateStringCal((dateStr + " " + timeStr),usps));
			}
			
			newestActivity.setCity(TrackingManager.getXMLValue(doc, "TrackSummary","EventCity"));
			newestActivity.setStateProv(TrackingManager.getXMLValue(doc, "TrackSummary","EventState"));
			newestActivity.setCountry(TrackingManager.getXMLValue(doc, "TrackSummary","EventCountry"));
			packageHistory.add(newestActivity);
			
			if(nodes.getLength() != 0){

			for (int i = 0; i < nodes.getLength(); i++) {
				PackageActivity activity = new PackageActivity();
				Element element = (Element) nodes.item(i);

				NodeList city = element.getElementsByTagName("EventCity");
				Element line = (Element) city.item(0);
				activity.setCity(TrackingManager.getCharacterDataFromElement(line));
				
				NodeList state = element.getElementsByTagName("EventState");
				line = (Element) state.item(0);
				activity.setStateProv(TrackingManager.getCharacterDataFromElement(line));

				NodeList country = element.getElementsByTagName("EventCountry");
				line = (Element) country.item(0);
				activity.setCountry(TrackingManager.getCharacterDataFromElement(line));
				
				NodeList description = element.getElementsByTagName("Event");
				line = (Element) description.item(0);
				
				String event = TrackingManager.getCharacterDataFromElement(line);
				activity.setActivity(event);
				
				NodeList timeNode = element.getElementsByTagName("EventTime");
				line = (Element) timeNode.item(0);

				NodeList dateNode = element.getElementsByTagName("EventDate");
				Element line2 = (Element) dateNode.item(0);
				
				String date = TrackingManager.getCharacterDataFromElement(line2);
				String time = TrackingManager.getCharacterDataFromElement(line);
				if(time == null){
					activity.setDateTime(TrackingManager.dateStringCal(date,uspsNoTime));
				}
				else{
					activity.setDateTime(TrackingManager.dateStringCal((date + " " + time),usps));
				}

				packageHistory.add(activity);

				if(i == nodes.getLength()-1){
					result.setDateShipped(activity.getDateTime());
					String shipFromAddress = null;
					if(activity.getCity() != null && activity.getStateProv() != null){
						shipFromAddress = activity.getCity() + "," + activity.getStateProv();
					}
					String countryStr = activity.getCountry();
					if(countryStr != null && shipFromAddress != null){
						shipFromAddress = shipFromAddress + "," + countryStr;
					}
					
					if(shipFromAddress != null){
						result.setShipFromAddress(shipFromAddress);
					}
				}
			}
			result.setPackageHistory(packageHistory);
			}
			else{
				result.setHasMap(false);
				result.setIsAvailableInCourierSystem(false);
			}
		}
		catch (Exception e) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Tracking Number", trackingNumber);

			ApplicationException aex = new ApplicationException("Unable to convert USPS XML to tracking object.", e);
			logger.error(aex, logProps);
			
			throw aex;
		}   
		return result;
	}
}
