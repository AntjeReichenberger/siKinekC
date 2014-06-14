package org.webdev.kpoint.api.mobile.domain;

import java.util.List;

import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.mobile.util.WSApplicationError;
import org.webdev.kpoint.api.mobile.util.WSApplicationException;
import org.webdev.kpoint.bl.persistence.CourierDao;
import org.webdev.kpoint.bl.persistence.TrackingDao;
import org.webdev.kpoint.bl.persistence.UserTrackingDao;
import org.webdev.kpoint.bl.pojo.Courier;
import org.webdev.kpoint.bl.pojo.Tracking;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.UserTracking;
import org.webdev.kpoint.bl.tracking.TrackedPackage;
import org.webdev.kpoint.bl.tracking.TrackingManager;


public class TrackingDomain extends Domain{
	
	private final static String TRACKING_DATA_ERROR = "TRACKING_DATA_NOT_RETRIEVED"; 
	private final static String TRACKING_RECORD_NOT_FOUND = "TRACKING_RECORD_NOT_FOUND"; 
	private final static String TRACKING_NUMBER_INVALID = "INVALID_TRACKING_NUMBER"; 
	private final static String TRACKING_RECORD_ALREADY_EXISTS = "TRACKING_RECORD_ALREADY_EXISTS";
	
	public static List<UserTracking> getTrackingRecords(int userId, User authenticatedUser) throws Exception{
		authorizationCheck(userId, authenticatedUser, "User does not have access to the target user's Tracking data. Target User ID: " + userId);
		
		List<UserTracking> trackings = new UserTrackingDao().fetch(authenticatedUser);

		//loop through and set user to null so it is not returned with the JSON ... not required
		for(int i=0; i<trackings.size(); i++){
			trackings.get(i).setUser(null);
		}
		return trackings;
	}
	
	public static TrackedPackage getTrackingDetails(int trackingId, User authenticatedUser) throws Exception{
		TrackingDao trackingDao = new TrackingDao();
		Tracking tracking = trackingDao.read(trackingId);
				
		UserTracking userTracking = new UserTrackingDao().read(tracking, authenticatedUser);
		if(userTracking == null){
			WSApplicationError err = new WSApplicationError(TRACKING_RECORD_NOT_FOUND, "No Tracking record was found. Tracking Id is invalid: " + trackingId, Response.Status.BAD_REQUEST);
			throw new WSApplicationException(err, err.getResponse());
		}
		authorizationCheck(userTracking.getUser().getUserId(), authenticatedUser, "User does not have access to the Tracking details. Tracking Id: " + trackingId);
		
		TrackedPackage trackedPackage;
		try {
			trackedPackage = TrackingManager.getTrackingDetails(userTracking.getTracking().getTrackingNumber(), null);
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(TRACKING_DATA_ERROR, ex);
			throw new WSApplicationException(err, err.getResponse());
		}
		
		trackedPackage.setNickname(userTracking.getPackageNickname());
		return trackedPackage;
	}
	
	public static UserTracking createTrackingRecord(org.webdev.kpoint.bl.api.mapper.request.Tracking requestTracking, User authenticatedUser) throws Exception{
		UserTrackingDao userTrackingDao = new UserTrackingDao();
		TrackedPackage trackingDetails = null;
		
		//Determine if the user is already tracking this package
		List<UserTracking> userTrackings = userTrackingDao.fetch(authenticatedUser);
		for(UserTracking userTrack : userTrackings){
			if(userTrack.getTracking().getTrackingNumber().equals(requestTracking.getTrackingNumber())){
				WSApplicationError err = new WSApplicationError(TRACKING_RECORD_ALREADY_EXISTS, "You already have this tracking number added to your list.  Tracking number: " + requestTracking.getTrackingNumber(),Response.Status.BAD_REQUEST);
				throw new WSApplicationException(err, err.getResponse());
			}
		}
		
		//validate tracking number and retrieve the tracking details
		try {
			if(requestTracking.getCourierCode() != null){
				//get a courier id based on the courier code
				Courier courier = new CourierDao().read(requestTracking.getCourierCode());
				trackingDetails = TrackingManager.getTrackingDetails(requestTracking.getTrackingNumber(), courier.getCourierId());
			}
			else{
				trackingDetails = TrackingManager.getTrackingDetails(requestTracking.getTrackingNumber(), null);
			}
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(TRACKING_DATA_ERROR, ex);
			throw new WSApplicationException(err, err.getResponse());
		}
		
		//check if the tracking number is validate
		if(trackingDetails.getIsValidTrackingNumber() == false){
			WSApplicationError err = new WSApplicationError(TRACKING_NUMBER_INVALID, "The provided tracking number is invalid. Tracking Number: " + requestTracking.getTrackingNumber(), Response.Status.BAD_REQUEST);
			throw new WSApplicationException(err, err.getResponse());
		}
			
		//update tracking record with third party data
		Tracking tracking = new Tracking();
		tracking.setTrackingNumber(requestTracking.getTrackingNumber());
		tracking.setCourier(trackingDetails.getCourier());
		if(trackingDetails.getIsAvailableInCourierSystem())
		{
			tracking.setCurrentLocation(trackingDetails.getCurrentLocation());
			tracking.setEstimatedArrival(trackingDetails.getArrivalDate());
			
			if(trackingDetails.getHasMap())
				tracking.setCurrentStatus(trackingDetails.getPackageHistory().get(0).getActivity());
				
			if(trackingDetails.getWeight() != null && trackingDetails.getWeightType() != null)
			{
				tracking.setWeight(Float.parseFloat(trackingDetails.getWeight()));
				tracking.setWeightType(trackingDetails.getWeightType());
			}
		}
				
		//create user tracking record
		UserTracking userTrackingRecord = new UserTracking();
		if(requestTracking.getCourierCode() != null) userTrackingRecord.setAutoSelected(false);
		userTrackingRecord.setPackageNickname(requestTracking.getPackageNickname());
		userTrackingRecord.setUser(authenticatedUser);
		userTrackingRecord.setTracking(tracking);
		userTrackingDao.create(userTrackingRecord);
		
		//set user to null so it is not returned with the JSON ... not required
		userTrackingRecord.setUser(null);
		
		return userTrackingRecord;	
	}
		
	public static void deleteTrackingRecord(int userId, int trackingId, User authenticatedUser) throws Exception{
		authorizationCheck(userId, authenticatedUser, "User does not have access to delete the Tracking record. Tracking ID: " + trackingId + ", Target User ID: " + userId);
		
		TrackingDao trackingDao = new TrackingDao();
		Tracking tracking = trackingDao.read(trackingId);
		
		UserTrackingDao dao = new UserTrackingDao();
		UserTracking userTracking = dao.read(tracking, authenticatedUser);
		userTracking.setIsActive(false);
		
		dao.update(userTracking);
	}
}
