package org.webdev.kpoint.api.partner.util;


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

public class GeocodeRetriever {
	private static final KinekLogger logger = new KinekLogger(GeocodeRetriever.class);

	private final String HTTP_ADDRESS = ExternalSettingsManager.getAdminPortalGoogleGDataUrl();
	private final double DEFAULT_LAT = 0;
	private final double DEFAULT_LONG = 0;
	
	private final String STATUS_OK = "OK";
	private final String STATUS_OVER_LIMIT = "OVER_QUERY_LIMIT";
	private final String STATUS_ZERO_RESULTS = "ZERO_RESULTS";
	private final String STATUS_REQUEST_DENIED = "REQUEST_DENIED";
	private final String STATUS_INVALID_REQUEST = "INVALID_REQUEST";
	private final String STATUS_NO_RESPONSE = "NO_RESPONSE";
	
	private String address;
	private double latitude;
	private double longitude;
	/**
	 * True once the request to Google has been made
	 */
	private boolean hasMadeRequest = false;
	
	public GeocodeRetriever(String address) {
		this.address = address;
	}
	
	/**
	 * Attempts to retrieve the latitude of a provided address
	 * A default value is returned if no value exists for the provided address
	 * @return The latitude of the provided address
	 */
	public double getGeoLat() {
		if (!this.hasMadeRequest) {
			this.getGeocodes();
		}
		return this.latitude;
	}
	
	/**
	 * Attempts to retrieve the longitude of a provided address
	 * A default value is returned if no value exists for the provided address
	 * @return The longitude of the provided address
	 */
	public double getGeoLong() {
		if (!this.hasMadeRequest) {
			this.getGeocodes();
		}
		return this.longitude;	
	}
	
	/**
	 * Initiates the HTTP request to get the GeoCodes (Latitude & Longitude) based on the provided address
	 */
	private void getGeocodes() {
		// Set default values in case an exception occurs.
		// We don't want an exception to bring down the entire App 
		// for something as trivial as Geo Location codes
		this.latitude = DEFAULT_LAT;
		this.longitude = DEFAULT_LONG;
		
		String requestParameters = "";
		try {
			requestParameters = getRequestParameters(ConfigurationManager.getUrlEncoding());
		}
		catch (UnsupportedEncodingException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("RequestParams", requestParameters);
            logger.error(new ApplicationException("Geocode request parameters could not be processed.", ex), logProps);
		
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
		String statusCode="",latitudeVal="",longitudeVal = "";
		try {
			InputSource inputXml = new InputSource(new StringReader(response));
			
		    XPathFactory factory = XPathFactory.newInstance();
		    XPath xpath = factory.newXPath();
		    Node root = (Node) xpath.evaluate("/", inputXml, XPathConstants.NODE);
			NodeList status = (NodeList) xpath.evaluate("/GeocodeResponse/status/text()", root, XPathConstants.NODESET);
			if(status.getLength() > 0) {
		        statusCode = status.item(0).getTextContent();
		    }
			else{
				statusCode = STATUS_NO_RESPONSE;
			}
			
			if(statusCode.equals(STATUS_OK)){
			  NodeList latitudeNodes = (NodeList) xpath.evaluate("/GeocodeResponse/result/geometry/location/lat/text()", root, XPathConstants.NODESET);      
		      NodeList longitudeNodes = (NodeList) xpath.evaluate("/GeocodeResponse/result/geometry/location/lng/text()", root, XPathConstants.NODESET);
		      /*Only expecting one value for longitude and latitude*/
		      if(latitudeNodes.getLength() > 0) {	
		        latitudeVal = latitudeNodes.item(0).getTextContent();
		        this.latitude = Double.parseDouble(latitudeVal);
		      }
		      else{
		    	latitudeVal = "0";
		      }		      
		      if(longitudeNodes.getLength() > 0){
		    	longitudeVal = longitudeNodes.item(0).getTextContent();
		    	this.longitude = Double.parseDouble(longitudeVal);
		      }
		      else{
		    	longitudeVal = "0";
		      }		      
			}
			else {
				Hashtable<String,String> logProps = new Hashtable<String,String>();
				logProps.put("StatusCode", statusCode);
	            logger.error(new ApplicationException("Invalid status code found."), logProps);
				return;
			}
		}
		catch (NumberFormatException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("StatusCode", statusCode);
			logProps.put("Latitude", latitudeVal);
			logProps.put("Longitude", longitudeVal);
            logger.error(new ApplicationException("Error parsing latitude and longitude.", ex), logProps);
        
            return;
		}catch (XPathExpressionException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("StatusCode", statusCode);
			logProps.put("Latitude", latitudeVal);
			logProps.put("Longitude", longitudeVal);
            logger.error(new ApplicationException("Error parsing latitude and longitude.", ex), logProps);
	    } 
		this.hasMadeRequest = true;
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
			requestParameters += URLEncoder.encode(this.address, encoding);
		}
		catch (UnsupportedEncodingException e) {
			throw e;
		}
		//requestParameters += "&key=" + this.GOOGLE_API_KEY;
		requestParameters += "&sensor=false";
		return requestParameters;
	}
}
