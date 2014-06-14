package org.webdev.kpoint.bl.tracking;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.TrackingDao;
import org.webdev.kpoint.bl.pojo.Tracking;

public class TrackingManager {
	private static TrackedPackage getTrackingDetails(String trackNum) throws Exception{	
		TrackedPackage activePackage = null;
    	
		if(USPSConnector.checkTrackID(trackNum)){
			USPSConnector uspsclient = new USPSConnector();
			activePackage = uspsclient.getPackageInfo(trackNum);	
		}
		else if(FedExConnector.checkTrackID(trackNum)){
			FedExConnector fedclient = new FedExConnector();
			activePackage = fedclient.getPackageInfo(trackNum);
		}
		else if(UPSConnector.checkTrackID(trackNum)){
			UPSConnector upsclient = new UPSConnector();
			activePackage = upsclient.getPackageInfo(trackNum);	
		}
		else if(CanadaPostConnector.checkTrackID(trackNum)){
			CanadaPostConnector cpclient = new CanadaPostConnector();
			activePackage = cpclient.getPackageInfo(trackNum);	
		}	
		return activePackage;
	}
	
	private static int determineCourierFromTrackingNumber(String trackNum) throws Exception{	
		int courierId = -1;
		if(USPSConnector.checkTrackID(trackNum))
			courierId = ExternalSettingsManager.getUSPSId();
		else if(FedExConnector.checkTrackID(trackNum))
			courierId = ExternalSettingsManager.getFedexId();
		else if(UPSConnector.checkTrackID(trackNum))
			courierId = ExternalSettingsManager.getUPSId();
		else if(CanadaPostConnector.checkTrackID(trackNum))
			courierId = ExternalSettingsManager.getCanadaPostId();

		return courierId;
	}
	
	public static TrackedPackage getTrackingDetails(String trackNum, Integer courierId) throws Exception{	
		TrackedPackage activePackage = null;
		trackNum = trackNum.toUpperCase();
		
		int actualCourierId = -1;
		if(courierId == null || courierId.equals(0)){
			actualCourierId = determineCourierFromTrackingNumber(trackNum);
			if(actualCourierId < 0){
				activePackage = new TrackedPackage();
				activePackage.setTrackingNumber(trackNum);
				activePackage.setCourier(courierId);
				activePackage.setIsValidTrackingNumber(false);

				return activePackage;
			}
		}
		else{
			actualCourierId = courierId.intValue();
		}
		
		if(actualCourierId == ExternalSettingsManager.getFedexId()){
			FedExConnector fedclient = new FedExConnector();
			activePackage = fedclient.getPackageInfo(trackNum);
		}
		else if(actualCourierId == ExternalSettingsManager.getCanadaPostId()){
			CanadaPostConnector cpclient = new CanadaPostConnector();
			activePackage = cpclient.getPackageInfo(trackNum);	
		}
		else if(actualCourierId == ExternalSettingsManager.getUPSId()){
			UPSConnector upsclient = new UPSConnector();
			activePackage = upsclient.getPackageInfo(trackNum);
		}
		else if(actualCourierId == ExternalSettingsManager.getUSPSId()){
			USPSConnector uspsclient = new USPSConnector();
			activePackage = uspsclient.getPackageInfo(trackNum);
		}
		
		if(activePackage != null){
			updateTrackingEntry(activePackage);
		}
		else{
			activePackage = new TrackedPackage();
			activePackage.setTrackingNumber(trackNum);
			activePackage.setCourier(actualCourierId);
			activePackage.setIsAvailableInCourierSystem(false);
		}
		return activePackage;
	}
	
	public static void updateTrackingEntry(TrackedPackage newUpdate) throws Exception {
		Tracking trackingEntry = new TrackingDao().read(newUpdate.getTrackingNumber(), newUpdate.getCourier().getCourierId());
		
		if(trackingEntry != null){
			if(newUpdate.getStatus() == null){
				newUpdate.setStatus("");
			}
			if(trackingEntry.getCurrentStatus() == null){
				trackingEntry.setCurrentStatus("");
			}
	
			if(newUpdate.getPackageHistory() != null && newUpdate.getPackageHistory().size() > 0)
			{
				if(!newUpdate.getStatus().equals(trackingEntry.getCurrentStatus()) || !newUpdate.getCurrentLocation().equals(trackingEntry.getCurrentLocation())){
					trackingEntry.setCurrentStatus(newUpdate.getStatus());
					trackingEntry.setEstimatedArrival(newUpdate.getArrivalDate());
					trackingEntry.setCurrentLocation(newUpdate.getCurrentLocation());
					trackingEntry.setIsDelivered(newUpdate.getIsDelivered());
					trackingEntry.setLastModifiedDate(Calendar.getInstance());
					new TrackingDao().update(trackingEntry);
					newUpdate.setHasChanged(true);
				}
				else if(newUpdate.getIsDelivered()){
					trackingEntry.setIsDelivered(newUpdate.getIsDelivered());
					trackingEntry.setLastModifiedDate(Calendar.getInstance());
					new TrackingDao().update(trackingEntry);
				}
			}	
		}
	}
	
	public static Integer getCourier(String trackNum){	
		trackNum = trackNum.toUpperCase();
		if(USPSConnector.checkTrackID(trackNum)){
			return ExternalSettingsManager.getUSPSId();
		}
		else if(FedExConnector.checkTrackID(trackNum)){
			return ExternalSettingsManager.getFedexId();
		}
		else if(UPSConnector.checkTrackID(trackNum)){
			return ExternalSettingsManager.getUPSId();	
		}
		else if(CanadaPostConnector.checkTrackID(trackNum)){
			return ExternalSettingsManager.getCanadaPostId();
		}	
		return null;
	}
	
	
	public static Calendar dateStringCal(String s, SimpleDateFormat d){
		Calendar cal=Calendar.getInstance();
		try{   
			Date d1=d.parse(s);
			cal.setTime(d1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return cal;
	}

	public static String getCharacterDataFromElement(Element e) {
		try{
			if(e != null)
			{
				Node child = e.getFirstChild();
				if (child instanceof CharacterData) {
					CharacterData cd = (CharacterData) child;
					return cd.getData();
				}
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return null;	
	}

	public static String getXMLValue(Document doc, String containerTag, String valueTag){
		NodeList nodes = doc.getElementsByTagName(containerTag);
		return getXMLValue(nodes, valueTag);
	}
	
	public static String getXMLValue(NodeList nodes, String valueTag){
		Element element = (Element) nodes.item(0);
		if(element != null){
			NodeList value = element.getElementsByTagName(valueTag);
			Element line = (Element) value.item(0);
			return getCharacterDataFromElement(line);
		}
		else{
			return null;
		}
	}

	public static String getXMLValue(NodeList nodes, String containerTag, String valueTag) {
		Element element = (Element) nodes.item(0);
		if(element != null){
			NodeList container = element.getElementsByTagName(containerTag);
			return getXMLValue(container, valueTag);
		}
		else{
			return null;
		}
	}
}

