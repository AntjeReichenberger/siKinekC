package org.webdev.kpoint.bl.tracking;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.KinekPointDao;

import com.fedex.ship.stub.*;

public class FedExConnector {
	
	private static final KinekLogger logger = new KinekLogger(FedExConnector.class);
	private static final int fedexId= ExternalSettingsManager.getFedexId();
	public  static final String TRACKING_NUMBER_REGEX = "([0-9]{12}|[0-9]{15}|[0-9]{20})";
	
	public static Boolean checkTrackID(String trackID){
		trackID = trackID.replaceAll("\\s", "").trim(); //ensure leading and trailing spaces / tabs are removed
		if(trackID.matches(TRACKING_NUMBER_REGEX))
			return true;	
		return false;	
	}

	public TrackedPackage getPackageInfo(String trackID) throws Exception{   
		TrackedPackage results = new TrackedPackage(trackID, fedexId);
		trackID = trackID.replaceAll(" ", "");
		TrackRequest request = new TrackRequest();

		request.setClientDetail(createClientDetail());
		request.setWebAuthenticationDetail(createWebAuthenticationDetail());
		TransactionDetail transactionDetail = new TransactionDetail();

		//This is a reference field for the customer.  Any value can be used and will be provided in the response.
		transactionDetail.setCustomerTransactionId("Kinek Tracking Request"); 
		request.setTransactionDetail(transactionDetail);

		VersionId versionId = new VersionId("trck", 4, 1, 0);
		request.setVersion(versionId);

		TrackPackageIdentifier packageIdentifier = new TrackPackageIdentifier();
		packageIdentifier.setValue(trackID);
		packageIdentifier.setType(TrackIdentifierType.TRACKING_NUMBER_OR_DOORTAG);
		request.setPackageIdentifier(packageIdentifier);
		request.setIncludeDetailedScans(new Boolean(true));

		try {
			TrackServiceLocator service = new TrackServiceLocator();;
			updateEndPoint(service);
			TrackPortType port = service.getTrackServicePort();
			TrackReply reply = port.track(request); // This is the call to the web service passing in a request object and returning a reply object
			
			if (isResponseOk(reply.getHighestSeverity())) // check if the call was successful
			{
				TrackDetail td[] = reply.getTrackDetails();
				for (int i=0; i< td.length; i++) { // package detail information
					if(td[i].getPackageWeight() != null){
						results.setWeight(td[i].getPackageWeight().getValue().toString());
						results.setWeightType(td[i].getPackageWeight().getUnits().getValue());
					}
					if(td[i].getServiceInfo() != null){
						results.setShipmentType(td[i].getServiceInfo());
					}
					if(td[i].getShipTimestamp() != null){
						results.setDateShipped(td[i].getShipTimestamp());
					}
					results.setArrivalDateHasTime(true);
					results.setDateShippedHasTime(true);

					Calendar delivery = td[i].getActualDeliveryTimestamp();
					if(delivery == null){
						delivery = td[i].getEstimatedDeliveryTimestamp();	
					}
					else{
						results.setIsDelivered(true);
					}
					results.setArrivalDate(delivery);

					List<String> shipLocation = new ArrayList<String>();

					if(td[i].getDestinationAddress() != null){	
					   if(td[i].getDestinationAddress().getCity() != null){
						   shipLocation.add(td[i].getDestinationAddress().getCity());
					   }
					   if(td[i].getDestinationAddress().getStateOrProvinceCode() != null){
						   shipLocation.add(td[i].getDestinationAddress().getStateOrProvinceCode());
					   }
					   if(td[i].getDestinationAddress().getCountryCode() != null){
						   shipLocation.add(td[i].getDestinationAddress().getCountryCode());
					   } 
					}
				   String shipTo = "";
				   for(int j = 0; j < shipLocation.size(); j++){
					   shipTo += shipLocation.get(j);
					   if(j < shipLocation.size()-1) 
						   shipTo += ", "; 
				   }
				   if(!shipLocation.isEmpty()){
					   results.setShipToAddress(shipTo);
				   }

					List<PackageActivity> packageActivities = new ArrayList<PackageActivity>();
					TrackEvent[] trackEvents = td[i].getEvents();
					if (trackEvents != null) {
						for (int k = 0; k < trackEvents.length; k++) {
							PackageActivity activity = new PackageActivity();
							TrackEvent trackEvent = trackEvents[k];
							if (trackEvent != null) {
								activity.setActivity(trackEvent.getEventDescription());
								activity.setDateTime(trackEvent.getTimestamp());

								Address address = trackEvent.getAddress();
								if (address != null) {
									activity.setCity(address.getCity());
									activity.setStateProv(address.getStateOrProvinceCode());
									activity.setCountry(address.getCountryCode());
								}
								
								if(k == 0)
								{
									//this is the most current event, set the status on the tracked package to the current event description
									results.setStatus(activity.getActivity());
								}
								packageActivities.add(activity);
							}
						}
					}else{
						results.setHasMap(false);
					}
					results.setPackageHistory(packageActivities);
				}
			}
			else{
				results.setIsAvailableInCourierSystem(false);
				results.setHasMap(false);
			}
		} catch (Exception e) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Tracking Number", trackID);

			ApplicationException aex = new ApplicationException("Unable to convert Fedex object to tracking object.", e);
			logger.error(aex, logProps);
			
			throw aex;
		} 
		return results;
	}

	private static boolean isResponseOk(NotificationSeverityType notificationSeverityType) {
		if (notificationSeverityType == null) {
			return false;
		}
		if (notificationSeverityType.equals(NotificationSeverityType.WARNING) ||
				notificationSeverityType.equals(NotificationSeverityType.NOTE)    ||
				notificationSeverityType.equals(NotificationSeverityType.SUCCESS)) {
			return true;
		}
		return false;
	}

	private static ClientDetail createClientDetail() {
        ClientDetail clientDetail = new ClientDetail();
        String accountNumber = ExternalSettingsManager.getFedexAccountNumber();
        String meterNumber = ExternalSettingsManager.getFedexMeterNumber();
        clientDetail.setAccountNumber(accountNumber);
        clientDetail.setMeterNumber(meterNumber);
        return clientDetail;
	}
	
	private static WebAuthenticationDetail createWebAuthenticationDetail() {
        WebAuthenticationCredential wac = new WebAuthenticationCredential();
        String key = ExternalSettingsManager.getFedexClientKey(); 
        String password = ExternalSettingsManager.getFedexPassword();    

        wac.setKey(key);
        wac.setPassword(password);
        
		return new WebAuthenticationDetail(wac);
	}
	
	private static void updateEndPoint(TrackServiceLocator serviceLocator) {
		String endPoint = ExternalSettingsManager.getFedexEndpoint();
		if (endPoint != null) {
			serviceLocator.setTrackServicePortEndpointAddress(endPoint);
		}
	}
}
