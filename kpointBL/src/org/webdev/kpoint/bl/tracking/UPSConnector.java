/* 
 ** 
 ** Filename: JAXBTrackClient.java
 ** Authors: United Parcel Service of America
 ** 
 ** The use, disclosure, reproduction, modification, transfer, or transmittal 
 ** of this work for any purpose in any form or by any means without the 
 ** written permission of United Parcel Service is strictly prohibited. 
 ** 
 ** Confidential, Unpublished Property of United Parcel Service. 
 ** Use and Distribution Limited Solely to Authorized Personnel. 
 ** 
 ** Copyright 2009 United Parcel Service of America, Inc.  All Rights Reserved. 
 ** 
 */
package org.webdev.kpoint.bl.tracking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.xml.sax.InputSource;

import com.ups.xolt.accessrequest.jaxb.AccessRequest;
import com.ups.xolt.request.jaxb.Request;
import com.ups.xolt.request.jaxb.TrackRequest;

public class UPSConnector {

	private static final KinekLogger logger = new KinekLogger(UPSConnector.class);
	
	private static final String LICENSE_NUMBER = ExternalSettingsManager.getUPSLicense();
	private static final String USER_NAME = ExternalSettingsManager.getUPSUsername();
	private static final String PASSWORD = ExternalSettingsManager.getUPSPassword();
	private static final String ENDPOINT_URL= ExternalSettingsManager.getUPSEndpoint();
	private static final int upsId= ExternalSettingsManager.getUPSId();
	public  static final String TRACKING_NUMBER_REGEX = "([0-9]{10}$)|(1(Z|z) ?[0-9A-Za-z]{16})";
	
	private static SimpleDateFormat upsFull =new SimpleDateFormat("yyyyMMddhhmmss");
	private static SimpleDateFormat ups =new SimpleDateFormat("yyyyMMdd");

	public static Boolean checkTrackID(String trackID){
		trackID = trackID.replaceAll("\\s", "").trim();
		if(trackID.matches(TRACKING_NUMBER_REGEX))
			return true;	
		return false;	
	}	

	public TrackedPackage getPackageInfo(String trackID) throws Exception {    
		StringWriter strWriter = null;
		String strResults = "";
		try {	    
			//Create JAXBContext and marshaller for AccessRequest object        			
			JAXBContext accessRequestJAXBC = JAXBContext.newInstance(AccessRequest.class.getPackage().getName() );	            
			Marshaller accessRequestMarshaller = accessRequestJAXBC.createMarshaller();
			com.ups.xolt.accessrequest.jaxb.ObjectFactory accessRequestObjectFactory = new com.ups.xolt.accessrequest.jaxb.ObjectFactory();
			AccessRequest accessRequest = accessRequestObjectFactory.createAccessRequest();
			populateAccessRequest(accessRequest);

			//Create JAXBContext and marshaller for TrackRequest object
			JAXBContext trackRequestJAXBC = JAXBContext.newInstance(TrackRequest.class.getPackage().getName() );	            
			Marshaller trackRequestMarshaller = trackRequestJAXBC.createMarshaller();
			com.ups.xolt.request.jaxb.ObjectFactory requestObjectFactory = new com.ups.xolt.request.jaxb.ObjectFactory();
			TrackRequest trackRequest = requestObjectFactory.createTrackRequest();
			populateTrackRequest(trackRequest,trackID);

			//Get String out of access request and track request objects.
			strWriter = new StringWriter();       		       
			accessRequestMarshaller.marshal(accessRequest, strWriter);
			trackRequestMarshaller.marshal(trackRequest, strWriter);
			strWriter.flush();
			strWriter.close();
			//Debug only System.out.println("Request: " + strWriter.getBuffer().toString());

			strResults =contactService(strWriter.getBuffer().toString());

		} catch (Exception e) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Tracking Number", trackID);

			ApplicationException aex = new ApplicationException("Unable to retrieve tracking information from UPS.", e);
			logger.error(aex, logProps);
			throw aex;
			
		} finally{
			try{
				if(strWriter != null){
					strWriter.close();
					strWriter = null;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		//Debug only System.out.println(strResults);
		TrackedPackage results = convertUPS(strResults, trackID);
		return results;

	}    

	private static String contactService(String xmlInputString) throws Exception{		
		String outputStr = null;
		OutputStream outputStream = null;
		try {
			URL url = new URL(ENDPOINT_URL);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			//Debug only System.out.println("Client established connection with " + url.toString());
			
			// Setup HTTP POST parameters
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);

			outputStream = connection.getOutputStream();		
			outputStream.write(xmlInputString.getBytes());
			outputStream.flush();
			outputStream.close();
			//Debug only System.out.println("Http status = " + connection.getResponseCode() + " " + connection.getResponseMessage());

			outputStr = readURLConnection(connection);		
			connection.disconnect();
		} catch (Exception e) {
			throw e;
		} finally {						
			if(outputStream != null){
				outputStream.close();
				outputStream = null;
			}
		}		
		return outputStr;
	}

	/**
	 * This method read all of the data from a URL connection to a String
	 */

	public static String readURLConnection(URLConnection uc) throws Exception {
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			int letter = 0;			

			while ((letter = reader.read()) != -1){
				buffer.append((char) letter);
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("Could not read from URL: " + e.toString());
			throw e;
		} finally {
			if(reader != null){
				reader.close();
				reader = null;
			}
		}
		return buffer.toString();
	}

	/**
	 * Populates the access request object.
	 * @param accessRequest
	 */
	private static void populateAccessRequest(AccessRequest accessRequest){
		accessRequest.setAccessLicenseNumber(LICENSE_NUMBER);
		accessRequest.setUserId(USER_NAME);
		accessRequest.setPassword(PASSWORD);
	}

	/**
	 * Populate TrackRequest object
	 * @param trackRequest
	 */
	private static void populateTrackRequest(TrackRequest trackRequest, String inTrackID){   	
		Request request = new Request(); 

		List<String> optionsList = request.getRequestOption();
		optionsList.add("activity");

		request.setRequestAction("Track");
		trackRequest.setRequest(request);
		trackRequest.setTrackingNumber(inTrackID);
		trackRequest.setIncludeFreight("01");
	}

	public static TrackedPackage convertUPS(String xmlInput, String trackingNumber) throws Exception {
		TrackedPackage result = new TrackedPackage(trackingNumber,upsId);
		boolean statusFromActivity = false;

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();

			is.setCharacterStream(new StringReader(xmlInput));
			Document doc = db.parse(is);
			//Gets shipment Type
			result.setShipmentType(TrackingManager.getXMLValue(doc, "ShipmentType","Description"));

			//Gets shipment weight
			result.setWeight(TrackingManager.getXMLValue(doc, "ShipmentWeight","Weight"));
			String weightType = TrackingManager.getXMLValue(doc, "ShipmentWeight","Code");
			if(weightType != null){
				result.setWeightType(weightType.toLowerCase());
			}

			//Gets status (gotten from first activity now)
			String currentStatus = TrackingManager.getXMLValue(doc, "CurrentStatus","Description");
			if(currentStatus == null){
				statusFromActivity = true;
			}
			else{
				result.setStatus(currentStatus);
			}

			//Gets arrival date
			String arrivalDate = TrackingManager.getXMLValue(doc, "DeliveryDetails","Date");
			String arrivalTime = TrackingManager.getXMLValue(doc, "DeliveryDetails","Time");
			
			if(arrivalDate == null){
				arrivalDate = TrackingManager.getXMLValue(doc, "EstimatedDeliveryDetails","Date");
				if(arrivalDate != null){
					result.setArrivalDate(TrackingManager.dateStringCal(arrivalDate,ups));
				}
			}
			else{				
				if(arrivalTime != null){
					result.setArrivalDate(TrackingManager.dateStringCal(arrivalDate + arrivalTime,upsFull));
					result.setArrivalDateHasTime(true);
				}
				else{
					result.setArrivalDate(TrackingManager.dateStringCal(arrivalDate,ups));	
				}			
			}
			
			//Gets estimated departure
			String originDate = TrackingManager.getXMLValue(doc, "OriginPortDetails","Date");
			String originTime;
			if(originDate != null){
				originTime = TrackingManager.getXMLValue(doc, "OriginPortDetails","Time");
				if(originTime != null){
					result.setDateShipped(TrackingManager.dateStringCal(originDate + originTime, upsFull));
					result.setDateShippedHasTime(true);
				}
				else{
					result.setDateShipped(TrackingManager.dateStringCal(originDate, ups));
				}
			}
			
			//Gets Shipped From Address
			result.setShipFromAddress(TrackingManager.getXMLValue(doc, "Shipper","AddressLine1") + "," +
					TrackingManager.getXMLValue(doc, "Shipper","StateProvinceCode") + "," + TrackingManager.getXMLValue(doc, "Shipper","CountryCode"));

			//Gets Shipped To Address
			result.setShipToAddress(TrackingManager.getXMLValue(doc, "ShipTo","City") + "," +
					TrackingManager.getXMLValue(doc, "ShipTo","StateProvinceCode") + "," + TrackingManager.getXMLValue(doc, "ShipTo","CountryCode"));				
			
			NodeList nodes = doc.getElementsByTagName("Activity");
			List<PackageActivity> packageHistory = new ArrayList<PackageActivity>();
			
			//location scan cannot be the latest status, if they are, this number is incremented.
			int locationScanLatest = 0;
			
			for (int i = 0; i < nodes.getLength(); i++) {
				PackageActivity activity = new PackageActivity();
				Element element = (Element) nodes.item(i);

				NodeList city = element.getElementsByTagName("City");
				Element line = (Element) city.item(0);
				String testData = TrackingManager.getCharacterDataFromElement(line);
				activity.setCity(testData);

				NodeList state = element.getElementsByTagName("StateProvinceCode");
				line = (Element) state.item(0);
				activity.setStateProv(TrackingManager.getCharacterDataFromElement(line));

				NodeList country = element.getElementsByTagName("CountryCode");
				line = (Element) country.item(0);
				activity.setCountry(TrackingManager.getCharacterDataFromElement(line));
					
				NodeList status = element.getElementsByTagName("Status");
				String theActivity = TrackingManager.getXMLValue(status,"StatusType","Description");
				activity.setActivity(theActivity);
				String statusCode = TrackingManager.getXMLValue(status,"StatusCode","Code");
				String statusType = TrackingManager.getXMLValue(status,"StatusType","Code");
				
				if(statusFromActivity && i == locationScanLatest && !statusCode.equals("LC")) result.setStatus(theActivity);
				else if(statusFromActivity && i == locationScanLatest && statusCode.equals("LC")) locationScanLatest++;
			
				NodeList date = element.getElementsByTagName("Date");
				Element lineDate = (Element) date.item(0);
				
				NodeList time = element.getElementsByTagName("Time");
				Element lineTime = (Element) time.item(0);
				
				String dateTimeStr = TrackingManager.getCharacterDataFromElement(lineDate) + TrackingManager.getCharacterDataFromElement(lineTime); 
				activity.setDateTime(TrackingManager.dateStringCal((dateTimeStr),upsFull));
				
				if(statusType != null && statusType.equals(ExternalSettingsManager.getUPSDeliveredStatus())){
					result.setArrivalDate(TrackingManager.dateStringCal(dateTimeStr,upsFull));
					result.setArrivalDateHasTime(true);
					result.setIsDelivered(true);
				}
				
				if(!statusCode.equals("LC")){
					packageHistory.add(activity);
				}
			}
			if(packageHistory.size() > 0){
				result.setPackageHistory(packageHistory);
			}
			else{
				result.setIsAvailableInCourierSystem(false);
				result.setHasMap(false);
			}
		}
		catch (Exception e) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Tracking Number", trackingNumber);

			ApplicationException aex = new ApplicationException("Unable to convert UPS XML to tracking object.", e);
			logger.error(aex, logProps);
			
			throw aex;
		}   
		return result;
	}
}