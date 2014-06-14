package org.webdev.kpoint.bl.util;

public class GeoCalculator {
	/**
	 * The number system to use in making calculations
	 *
	 */
	public enum NumberSystem { IMPERIAL, METRIC };
	
	private final double radiusInMiles = 3958.76;
	private final double radiusInKM = 6371;
	
	/**
	 * Earth's radius.
	 */
	private double r = 0;
	
	/**
	 * Constructor for the GeoCalculator
	 * @param numberSystem
	 */
	public GeoCalculator(NumberSystem numberSystem) {
		if (numberSystem == NumberSystem.METRIC) {
			r = radiusInKM;
		}
		else {
			r = radiusInMiles;
		}
	}

	/**
     * Calculates the distance between two points given by latitude and longitude 
	 * @param lat1 The latitude of the 1st point
	 * @param long1 The longitude of the 1st point
	 * @param lat2 The latitude of the 2nd point
	 * @param long2 The longitude of the 2nd point
	 * @return The distance between the two provided points.
	 */
	public double getDistance(double lat1, double long1, double lat2, double long2) {
		double rLat = Math.toRadians(lat1 - lat2);
		double rLong = Math.toRadians(long1 - long2);
		double a = Math.pow(Math.sin(rLat/2), 2) +
        		Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
        		Math.pow(Math.sin(rLong/2), 2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		double distance = r * c;
		
		return distance;
	}
}
