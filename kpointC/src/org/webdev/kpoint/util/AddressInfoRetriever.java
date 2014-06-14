package org.webdev.kpoint.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.xml.sax.InputSource;

import com.google.gson.Gson;

public class AddressInfoRetriever {
	private static final KinekLogger logger = new KinekLogger(AddressInfoRetriever.class);

	private final String HTTP_ADDRESS = ExternalSettingsManager.getAdminPortalGoogleGDataUrl();
	private final String GOOGLE_API_KEY = ExternalSettingsManager.getAdminPortalGoogleGDataAuthorizationKey();
	
	private final String HTTP_V3ADDRESS = ExternalSettingsManager.getAdminPortalGoogleGDataV3Url();
	private final String GOOGLE_V3API_KEY = ExternalSettingsManager.getConsumerPortalGoogleGDataV3AuthorizationKey();
	
	
	private final String STATUS_OK = "OK";
	
	private String zip;
	private String city;
	private String country;
	private String state;
		
	public AddressInfoRetriever(String zip) {
		this.zip = zip;
		getAddressV3();
	}
	
	/**
	 * Attempts to retrieve the latitude of a provided address
	 * A default value is returned if no value exists for the provided address
	 * @return The latitude of the provided address
	 */
	public String getCity() {
		return this.city;
	}
	
	/**
	 * Attempts to retrieve the longitude of a provided address
	 * A default value is returned if no value exists for the provided address
	 * @return The longitude of the provided address
	 */
	public String getCountry() {
		return this.country;	
	}
	
	public String getState() {
		return this.state;	
	}
	
	
	/**
	 * Initiates the HTTP request to get the GeoCodes (Latitude & Longitude) based on the provided address
	 */
	private void getAddress() {
		String requestParameters = "";
		try {
			requestParameters = getRequestParameters(ConfigurationManager.getUrlEncoding());
		}
		catch (UnsupportedEncodingException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("RequestParams", requestParameters);
			logger.error(new ApplicationException("No corresponding geographic location could be found for the specified address.", ex), logProps);
            return;
		}
		String response;
		try {
			response = HttpRequestPoster.sendGetRequest(HTTP_ADDRESS, requestParameters);
			if(response == null) return;
		}
		catch (IOException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("GETAddress", HTTP_ADDRESS);
			logProps.put("RequestParams", requestParameters);
            logger.error(new ApplicationException("Geocode request failed to send.", ex), logProps);
			return;
		}

		try {
			String statusCode;
			InputSource inputXml = new InputSource(new StringReader(response));			
		    XPathFactory factory = XPathFactory.newInstance();
		    XPath xpath = factory.newXPath();
		    Node root = (Node) xpath.evaluate("/", inputXml, XPathConstants.NODE);
			NodeList status = (NodeList) xpath.evaluate("/GeocodeResponse/status/text()", root, XPathConstants.NODESET);
			if(status.getLength() > 0) {
		        statusCode = status.item(0).getTextContent();
		    }
			else{
				statusCode = "NO RESPONSE";
			}
			if(statusCode.equals(STATUS_OK)){
			  NodeList addressNodes = (NodeList) xpath.evaluate("/GeocodeResponse/result/address_component", root, XPathConstants.NODESET);      
		      /*
		       * administrative_area_level_1 - Province/State
		       * locality - City
		       * country - country
		       */
			  for(int i = 0; i < addressNodes.getLength();i++){
		    	  NodeList type = (NodeList) xpath.evaluate("type/text()", addressNodes.item(i), XPathConstants.NODESET);
		    	  for(int j = 0; j < type.getLength();j++){
		    		  if(type.item(j).getTextContent().equals("administrative_area_level_1")){
		    			  NodeList shortName = (NodeList) xpath.evaluate("short_name/text()", addressNodes.item(i), XPathConstants.NODESET);
		    	 		  if(shortName.getLength() > 0){
		    				  this.state = shortName.item(0).getTextContent();
		    			  }
		    		  }
		    		  else if(type.item(j).getTextContent().equals("locality")){
		    			  NodeList shortName = (NodeList) xpath.evaluate("short_name/text()", addressNodes.item(i), XPathConstants.NODESET);
		    			  if(shortName.getLength() > 0){
		    				  this.city = shortName.item(0).getTextContent();
		    			  }
		    		  }
		    		  else if(type.item(j).getTextContent().equals("country")){
		    			  NodeList shortName = (NodeList) xpath.evaluate("short_name/text()", addressNodes.item(i), XPathConstants.NODESET);
		    			  if(shortName.getLength() > 0){
		    				  this.country = shortName.item(0).getTextContent();
		    			  }
		    		  }
		    	  }
		      }
			}
			else {
				Hashtable<String,String> logProps = new Hashtable<String,String>();
				logProps.put("StatusCode", statusCode);
	            logger.warn(new ApplicationException("Invalid status code found."), logProps);
				return;
			}
		}
		catch (XPathExpressionException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("GETAddress", HTTP_ADDRESS);
			logProps.put("RequestParams", requestParameters);
            logger.error(new ApplicationException("Error parsing addres.", ex), logProps);
            return;
	    }
		catch (Exception ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("GETAddress", HTTP_ADDRESS);
			logProps.put("RequestParams", requestParameters);
            logger.warn(new ApplicationException("Address data was not found by Google Maps.", ex), logProps);
			return;
		}
		
	}
	
	/**
	 * Initiates the HTTP request to get the GeoCodes (Latitude & Longitude) based on the provided address
	 */
	private void getAddressV3() {
		String requestParameters = "";
		try {
			requestParameters = getRequestParameters(ConfigurationManager.getUrlEncoding());
		}
		catch (UnsupportedEncodingException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("RequestParams", requestParameters);
			logger.error(new ApplicationException("No corresponding geographic location could be found for the specified address.", ex), logProps);
            return;
		}
		String response;
		try {
			response = HttpRequestPoster.sendGetRequest(HTTP_V3ADDRESS, requestParameters);
			if(response == null) return;
		}
		catch (IOException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("GETAddress", HTTP_V3ADDRESS);
			logProps.put("RequestParams", requestParameters);
            logger.error(new ApplicationException("Geocode request failed to send.", ex), logProps);
			return;
		}

		try {
			String statusCode;
			InputSource inputXml = new InputSource(new StringReader(response));			
		    XPathFactory factory = XPathFactory.newInstance();
		    XPath xpath = factory.newXPath();
		    Node root = (Node) xpath.evaluate("/", inputXml, XPathConstants.NODE);
			NodeList status = (NodeList) xpath.evaluate("/GeocodeResponse/status/text()", root, XPathConstants.NODESET);
			if(status.getLength() > 0) {
		        statusCode = status.item(0).getTextContent();
		    }
			else{
				statusCode = "NO RESPONSE";
			}
			if(statusCode.equals(STATUS_OK)){
			  NodeList addressNodes = (NodeList) xpath.evaluate("/GeocodeResponse/result/address_component", root, XPathConstants.NODESET);      
		      /*
		       * administrative_area_level_1 - Province/State
		       * locality - City
		       * country - country
		       */
			  for(int i = 0; i < addressNodes.getLength();i++){
		    	  NodeList type = (NodeList) xpath.evaluate("type/text()", addressNodes.item(i), XPathConstants.NODESET);
		    	  for(int j = 0; j < type.getLength();j++){
		    		  if(type.item(j).getTextContent().equals("administrative_area_level_1")){
		    			  NodeList shortName = (NodeList) xpath.evaluate("short_name/text()", addressNodes.item(i), XPathConstants.NODESET);
		    	 		  if(shortName.getLength() > 0){
		    				  this.state = shortName.item(0).getTextContent();
		    			  }
		    		  }
		    		  else if(type.item(j).getTextContent().equals("locality")){
		    			  NodeList shortName = (NodeList) xpath.evaluate("short_name/text()", addressNodes.item(i), XPathConstants.NODESET);
		    			  if(shortName.getLength() > 0){
		    				  this.city = shortName.item(0).getTextContent();
		    			  }
		    		  }
		    		  else if(type.item(j).getTextContent().equals("country")){
		    			  NodeList shortName = (NodeList) xpath.evaluate("short_name/text()", addressNodes.item(i), XPathConstants.NODESET);
		    			  if(shortName.getLength() > 0){
		    				  this.country = shortName.item(0).getTextContent();
		    			  }
		    		  }
		    	  }
		      }
			}
			else {
				Hashtable<String,String> logProps = new Hashtable<String,String>();
				logProps.put("StatusCode", statusCode);
	            logger.warn(new ApplicationException("Invalid status code found."), logProps);
				return;
			}
		}
		catch (XPathExpressionException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("GETAddress", HTTP_V3ADDRESS);
			logProps.put("RequestParams", requestParameters);
            logger.error(new ApplicationException("Error parsing addres.", ex), logProps);
            return;
	    }
		catch (Exception ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("GETAddress", HTTP_V3ADDRESS);
			logProps.put("RequestParams", requestParameters);
            logger.warn(new ApplicationException("Address data was not found by Google Maps.", ex), logProps);
			return;
		}
		
	}

	
	/**
	 * Builds the query string parameter list based on the provided address and the current API key
	 * @param encoding The encoding to use when encoding the QueryString
	 * @return Query string request parameters
	 * @throws UnsupportedEncodingException 
	 */
	private String getRequestParameters(String encoding) throws UnsupportedEncodingException {
		String requestParameters = "address=";
		try {
			requestParameters += URLEncoder.encode(this.zip, encoding);
		}
		catch (UnsupportedEncodingException e) {
			throw e;
		}
		//requestParameters += "&key=" + this.GOOGLE_API_KEY;
		requestParameters += "&sensor=false";
		return requestParameters;
	}
}
