package org.webdev.kpoint.api.mobile.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.mobile.util.WSApplicationError;
import org.webdev.kpoint.api.mobile.util.WSApplicationException;
import org.webdev.kpoint.bl.api.mapper.response.kinekpoint.KinekPointStatistics;
import org.webdev.kpoint.bl.api.mapper.request.kinekpoint.OperatingHours;
import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.manager.KinekPointManager;
import org.webdev.kpoint.bl.manager.NotificationManager;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.PickupDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KPPackageRate;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Notification;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.PackageWeightGroup;
import org.webdev.kpoint.bl.pojo.Pickup;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.comparator.DistanceComparator;
import org.webdev.kpoint.bl.util.GeoCalculator;
import org.webdev.kpoint.bl.util.GeoCalculator.NumberSystem;

public class KinekPointsDomain extends Domain{
	
	private final static String KINEKPOINT_NOT_FOUND = "INVALID_KINEKPOINT_ID"; 
			
	public static KinekPoint getKinekPoint(int depotId) throws Exception{
		KinekPointDao kinekPointDao = new KinekPointDao();
		KinekPoint kinekPoint = kinekPointDao.read(depotId);
		
		if(kinekPoint == null){
			WSApplicationError err = new WSApplicationError(KINEKPOINT_NOT_FOUND, "No KinekPoint was found, KinekPoint ID is invalid: " + depotId,Response.Status.BAD_REQUEST);
			throw new WSApplicationException(err, err.getResponse());
		}
		
		return kinekPoint;
	}
	
	public static List<KinekPoint> getKinekPoints(Double minLat, Double maxLat, Double minLong, Double maxLong, Double searchPointLatitude, Double searchPointLongitude, boolean isBorderLocation) throws Exception{
		KinekPointDao kpointDao = new KinekPointDao();
		List<KinekPoint> kpoints = new ArrayList<KinekPoint>();		
		List<KinekPoint> formattedKinekPoints = new ArrayList<KinekPoint>();
		if (maxLong != 0 && minLong != 0 && maxLat != 0 && minLat != 0 && searchPointLongitude != 0 && searchPointLatitude != 0){
    		kpoints = kpointDao.fetch(maxLat, minLat, maxLong, minLong, isBorderLocation);
    		
    		//Sort depots based on distance from search point
    		if(kpoints != null)
    			Collections.sort(kpoints, new DistanceComparator<KinekPoint>(searchPointLatitude, searchPointLongitude, NumberSystem.IMPERIAL));
    		
    		//add distance from search point for each KinekPoint domain object
    		GeoCalculator cal = new GeoCalculator(NumberSystem.IMPERIAL);
        	for(int i=0; i<kpoints.size(); i++){
        		KinekPoint depot = kpoints.get(i);
    			double distance = cal.getDistance(searchPointLatitude, searchPointLongitude, depot.getGeolat(), depot.getGeolong());
    			depot.setDistance(distance);
    			//clean up data that is not needed by the API (set it to null so that it's not returned as part of the JSON)    			
    			depot.setAssociation(null);
    			depot.setCards(null);
    			depot.setCreatedDate(null);
    			depot.setEmail(null);
    			depot.setEnabledDate(null);
    			depot.setExtraInfo(null);
    			depot.setFeatures(null);
    			depot.setKpExtendedStorageRates(null);
    			depot.setKpSkidRate(null);
    			depot.setLanguages(null);
    			depot.setOperatingHours(null);
    			depot.setOrganization(null);
    			depot.setPayMethod(null);
    			depot.setPhone(null);
    			depot.setReferralSource(null);
    			depot.setRegion(null);
    			depot.setStatus(null);
    			depot.getState().getCountry().setCountryCode(null);
    			depot.getState().getCountry().setCurrencyCode(null);
    			depot.getState().setStateProvCode(null);
				depot.setBorderStates(null);
				
				//only return the first price, which represents the lowest price available for that KP
				if(depot.getKpPackageRates() != null && depot.getKpPackageRates().size() > 1)
				{
					KPPackageRate lowestRate = depot.getKpPackageRates().iterator().next();
					depot.setKpPackageRates(new HashSet<KPPackageRate>());
					depot.getKpPackageRates().add(lowestRate);
				}
				
				formattedKinekPoints.add(depot);
    		}
		}
    	return formattedKinekPoints;
	}
	
	public static List<KinekPoint> getKinekPoints(String stateProvCode) throws Exception{
		KinekPointDao kinekPointDao = new KinekPointDao();
		List<KinekPoint> kinekPoints = kinekPointDao.fetchBorderKinekPoints(stateProvCode);

		return kinekPoints;		
	}
	
	public static void mapKPFavorite(int userId, KinekPoint favorite, User authenticatedUser) throws Exception{
		authorizationCheck(userId, authenticatedUser, "User does not have access to the target user's KinekPoint data. Target User ID: " + userId);
		
		UserDao userDao = new UserDao();
		User user = userDao.read(userId);
		
		boolean kpAlreadyMapped = false;
		List<KinekPoint> favKinekPoints = new ArrayList<KinekPoint>(user.getKinekPoints());
		for(int i=0; i<favKinekPoints.size(); i++){
			if(favKinekPoints.get(i).getDepotId() == favorite.getDepotId()){
				kpAlreadyMapped = true;
				break;
			}
		}
		if(!kpAlreadyMapped)
		{
			favKinekPoints.add(favorite);
			user.setKinekPoints(new HashSet<KinekPoint>(favKinekPoints));
		}
		
		int baseDepotId = 1;
		if(user.getKinekPoint()== null || user.getKinekPoint().getDepotId() == baseDepotId)
		{
			//this is the first KinekPoint that the user has chosen, set their default and send welcome email
			user.setKinekPoint(favorite);
			
			EmailManager emailManager = new EmailManager();
			emailManager.sendFinalSignupEmail(user);
		}
		userDao.update(user);
	}
	
	public static void removeKPFavorite(int userId, int kinekPointId, User authenticatedUser) throws Exception{
		authorizationCheck(userId, authenticatedUser, "User does not have access to the target user's KinekPoint data. Target User ID: " + userId);
		
		UserDao userDao = new UserDao();
		User user = userDao.read(userId);
		
		List<KinekPoint>favKinekPoints = new ArrayList<KinekPoint>(user.getKinekPoints());		
		//remove the selected depot from the kps object and call user object to update to delete the row from mapping table		
		for(int i=0; i<favKinekPoints.size(); i++){
			if(favKinekPoints.get(i).getDepotId() == kinekPointId){
				favKinekPoints.remove(i);
				break;
			}			
		}
		
		user.setKinekPoints(new HashSet<KinekPoint>(favKinekPoints));		
		userDao.update(user);		
	}
	
	public static KinekPointStatistics getStatistics(int kinekPointId, User authenticatedUser) throws Exception {
		kinekPointAuthorizationCheck(kinekPointId, authenticatedUser, "User does not have access to the target KinekPoint data. Target KinekPoint ID: " + kinekPointId);
				
		KinekPointStatistics stats = new KinekPointStatistics();
		stats.setId(kinekPointId);
		
		Calendar date = Calendar.getInstance();
		//Set date to the beginning of today
		date.add(Calendar.HOUR_OF_DAY, -date.get(Calendar.HOUR_OF_DAY));
		date.add(Calendar.MINUTE, -date.get(Calendar.MINUTE));
		date.add(Calendar.SECOND, -date.get(Calendar.SECOND));
		date.add(Calendar.MILLISECOND, -date.get(Calendar.MILLISECOND));
		
		//Daily Stats
		stats.setDailyReceivedPackages(GetReceivedPackagesSince(kinekPointId, date));
		stats.setDailyPickedUpPackages(GetPickedUpPackagesSince(kinekPointId, date));

		//Weekly Stats
		date.add(Calendar.DAY_OF_WEEK, -date.get(Calendar.DAY_OF_WEEK)); //Set date to beginning of this week
		stats.setWeeklyReceivedPackages(GetReceivedPackagesSince(kinekPointId, date));
		stats.setWeeklyPickedUpPackages(GetPickedUpPackagesSince(kinekPointId, date));

		//Monthly Stats
		date.add(Calendar.DAY_OF_MONTH, -date.get(Calendar.DAY_OF_MONTH)); //Set date to beginning of this month
		stats.setMonthlyReceivedPackages(GetReceivedPackagesSince(kinekPointId, date));
		stats.setMonthlyPickedUpPackages(GetPickedUpPackagesSince(kinekPointId, date));

		//All time Stats
		stats.setAllTimeReceivedPackages(GetReceivedPackagesSince(kinekPointId));
		stats.setAllTimePickedUpPackages(GetPickedUpPackagesSince(kinekPointId));
		
		return stats;
	}
	
	private static int GetReceivedPackagesSince(int kpId) throws Exception {
		return GetReceivedPackagesSince(kpId, null);
	}
	
	private static int GetPickedUpPackagesSince(int kpId) throws Exception {
		return GetPickedUpPackagesSince(kpId, null);
	}
	
	private static int GetReceivedPackagesSince(int kpId, Calendar date) throws Exception {
		PackageReceiptDao packageReceiptDao = new PackageReceiptDao();
		
		List<PackageReceipt> receipts;
		if (date != null)
			receipts = packageReceiptDao.fetch(kpId, date, Calendar.getInstance());
		else {
			KinekPointDao kpDao = new KinekPointDao();
			receipts = packageReceiptDao.fetch(kpDao.read(kpId));
		}
		
		int totalReceivedPackages = 0;
		for (PackageReceipt receipt : receipts) {
			totalReceivedPackages += receipt.getPackages().size();
		}
		
		return totalReceivedPackages;
	}
	
	private static int GetPickedUpPackagesSince(int kpId, Calendar date) throws Exception {
		PickupDao pickupDao = new PickupDao();
		
		List<Pickup> pickups;
		if (date != null)
			pickups = pickupDao.fetch(kpId, date, Calendar.getInstance());
		else
			pickups = pickupDao.fetch(kpId);
			
		int totalPickedUpPackages = 0;
		for (Pickup pickup : pickups) {
			totalPickedUpPackages += pickup.getPackages().size();
		}
		
		return totalPickedUpPackages;
	}
	
	public static List<PackageWeightGroup> getWeightGroups(int kinekPointId) throws Exception{
		List<PackageWeightGroup> weightGroups = KinekPointManager.getWeightGroups(kinekPointId);
		return weightGroups;
	}
	
	public static KinekPoint updateKP(org.webdev.kpoint.bl.api.mapper.request.kinekpoint.KinekPoint kinekPoint, User authenticatedUser) throws Exception {
		kinekPointAuthorizationCheck(kinekPoint.getDepotId(), authenticatedUser, "User does not have access to update the target KinekPoint. Target Depot ID: " + kinekPoint.getDepotId());
		
		//Do a read of the user to fetch full object data
		KinekPointDao kinekPointDao = new KinekPointDao();				
		KinekPoint existingKinekPointRecord = kinekPointDao.read(kinekPoint.getDepotId());
		if(existingKinekPointRecord == null){							
			WSApplicationError err = new WSApplicationError(KINEKPOINT_NOT_FOUND, "KinekPoint could not be updated, no KinekPoint record was found.  Requested Depot ID: " + kinekPoint.getDepotId(), Response.Status.BAD_REQUEST);				
			throw new WSApplicationException(err, err.getResponse());
		}
		
		KinekPoint updatedKP = mergeKinekPointData(existingKinekPointRecord, kinekPoint);
		kinekPointDao.update(updatedKP);

		return updatedKP;	
	}
	
	private static KinekPoint mergeKinekPointData(KinekPoint existingKinekPointRecord, org.webdev.kpoint.bl.api.mapper.request.kinekpoint.KinekPoint kinekPoint){
		OperatingHours hours = kinekPoint.getOperatingHours();
		if(hours.getMondayStart() != null)
			existingKinekPointRecord.getOperatingHours().setMondayStart(hours.getMondayStart());
		if(hours.getMondayEnd() != null)
			existingKinekPointRecord.getOperatingHours().setMondayEnd(hours.getMondayEnd());
		if(hours.getClosedMonday() != null)
			existingKinekPointRecord.getOperatingHours().setClosedMonday(Boolean.valueOf(hours.getClosedMonday()));
		if(hours.getTuesdayStart() != null)
			existingKinekPointRecord.getOperatingHours().setTuesdayStart(hours.getTuesdayStart());
		if(hours.getTuesdayEnd() != null)
			existingKinekPointRecord.getOperatingHours().setTuesdayEnd(hours.getTuesdayEnd());
		if(hours.getClosedTuesday() != null)
			existingKinekPointRecord.getOperatingHours().setClosedTuesday(Boolean.valueOf(hours.getClosedTuesday()));
		if(hours.getWednesdayStart() != null)
			existingKinekPointRecord.getOperatingHours().setWednesdayStart(hours.getWednesdayStart());
		if(hours.getWednesdayEnd() != null)
			existingKinekPointRecord.getOperatingHours().setWednesdayEnd(hours.getWednesdayEnd());
		if(hours.getClosedWednesday() != null)
			existingKinekPointRecord.getOperatingHours().setClosedWednesday(Boolean.valueOf(hours.getClosedWednesday()));
		if(hours.getThursdayStart() != null)
			existingKinekPointRecord.getOperatingHours().setThursdayStart(hours.getThursdayStart());
		if(hours.getThursdayEnd() != null)
			existingKinekPointRecord.getOperatingHours().setThursdayEnd(hours.getThursdayEnd());
		if(hours.getClosedThursday() != null)
			existingKinekPointRecord.getOperatingHours().setClosedThursday(Boolean.valueOf(hours.getClosedThursday()));
		if(hours.getFridayStart() != null)
			existingKinekPointRecord.getOperatingHours().setFridayStart(hours.getFridayStart());
		if(hours.getFridayEnd() != null)
			existingKinekPointRecord.getOperatingHours().setFridayEnd(hours.getFridayEnd());
		if(hours.getClosedFriday() != null)
			existingKinekPointRecord.getOperatingHours().setClosedFriday(Boolean.valueOf(hours.getClosedFriday()));
		if(hours.getSaturdayStart() != null)
			existingKinekPointRecord.getOperatingHours().setSaturdayStart(hours.getSaturdayStart());
		if(hours.getSaturdayEnd() != null)
			existingKinekPointRecord.getOperatingHours().setSaturdayEnd(hours.getSaturdayEnd());
		if(hours.getClosedSaturday() != null)
			existingKinekPointRecord.getOperatingHours().setClosedSaturday(Boolean.valueOf(hours.getClosedSaturday()));
		if(hours.getSundayStart() != null)
			existingKinekPointRecord.getOperatingHours().setSundayStart(hours.getSundayStart());
		if(hours.getSundayEnd() != null)
			existingKinekPointRecord.getOperatingHours().setSundayEnd(hours.getSundayEnd());
		if(hours.getClosedSunday() != null)
			existingKinekPointRecord.getOperatingHours().setClosedSunday(Boolean.valueOf(hours.getClosedSunday()));
		if(hours.getHoursInfo() != null)
			existingKinekPointRecord.getOperatingHours().setHoursInfo(hours.getHoursInfo());
		
		return existingKinekPointRecord;
	}
	
}
