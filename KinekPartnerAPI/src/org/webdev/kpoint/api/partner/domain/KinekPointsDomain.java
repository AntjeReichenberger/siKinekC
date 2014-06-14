package org.webdev.kpoint.api.partner.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.partner.util.GeoLocation;
import org.webdev.kpoint.api.partner.util.GeocodeRetriever;
import org.webdev.kpoint.api.partner.util.WSApplicationError;
import org.webdev.kpoint.api.partner.util.WSApplicationException;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.comparator.DistanceComparator;
import org.webdev.kpoint.bl.util.GeoCalculator;
import org.webdev.kpoint.bl.util.GeoCalculator.NumberSystem;

public class KinekPointsDomain extends Domain{
	
	private final static String KINEKPOINT_NOT_FOUND = "INVALID_KINEKPOINT_ID"; 
			
	public static KinekPoint getKinekPoint(int depotId) throws Exception {
		KinekPointDao kinekPointDao = new KinekPointDao();
		KinekPoint kinekPoint = kinekPointDao.read(depotId);
		
		if(kinekPoint == null){
			WSApplicationError err = new WSApplicationError(KINEKPOINT_NOT_FOUND, "No KinekPoint was found, KinekPoint ID is invalid: " + depotId,Response.Status.BAD_REQUEST);
			throw new WSApplicationException(err, err.getResponse());
		}
		
		return kinekPoint;
	}
	
	public static List<KinekPoint> getKinekPoints(String address1,String address2,String city,String stateprov,String country, boolean onlyBorderLocations, double searchDistance)
		throws Exception {
		String address = address1;
		if (address2 != null && address2.length() > 0)
			address += " " + address2;
		address += " " + city;
		address += " " + stateprov;
		address += " " + country;
		
		GeocodeRetriever retriever = new GeocodeRetriever(address);			
		GeoLocation myLocation = GeoLocation.fromDegrees(retriever.getGeoLat(), retriever.getGeoLong());
		
		//in km
		double earthRadius = 6371.01;
		
		GeoLocation[] boundingCoordinates = myLocation.boundingCoordinates(searchDistance, earthRadius);
		double minLong = boundingCoordinates[0].getLongitudeInDegrees(); 
		double maxLong = boundingCoordinates[1].getLongitudeInDegrees(); 
		double minLat = boundingCoordinates[0].getLatitudeInDegrees();
		double maxLat =	boundingCoordinates[1].getLatitudeInDegrees();
		double searchPointLongitude = myLocation.getLongitudeInDegrees();
		double searchPointLatitude = myLocation.getLatitudeInDegrees();

		KinekPointDao kpointDao = new KinekPointDao();
		List<KinekPoint> kpoints = new ArrayList<KinekPoint>();		
    	if (maxLong != 0 && minLong != 0 && maxLat != 0 && minLat != 0 && searchPointLongitude != 0 && searchPointLatitude != 0){
    		kpoints = kpointDao.fetch(maxLat, minLat, maxLong, minLong, onlyBorderLocations);
    		
    		//Sort depots based on distance from search point
    		if(kpoints != null)
    			Collections.sort(kpoints, new DistanceComparator<KinekPoint>(searchPointLatitude, searchPointLongitude, NumberSystem.IMPERIAL));
    		
    		//add distance from search point for each KinekPoint domain object
    		GeoCalculator cal = new GeoCalculator(NumberSystem.IMPERIAL);
        	for(int i=0; i<kpoints.size(); i++){
        		KinekPoint depot = kpoints.get(i);
    			double distance = cal.getDistance(searchPointLatitude, searchPointLongitude, depot.getGeolat(), depot.getGeolong());
    			depot.setDistance(distance);
    		}
		}
    	
    	return kpoints;
	}
	
	public static List<KinekPoint> getKinekPoints(String stateProvCode) throws Exception {
		KinekPointDao kinekPointDao = new KinekPointDao();
		List<KinekPoint> kinekPoints = kinekPointDao.fetchBorderKinekPoints(stateProvCode);

		return kinekPoints;		
	}
	
}
