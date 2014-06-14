package org.webdev.kpoint.bl.pojo.comparator;

import java.util.Comparator;

import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.util.GeoCalculator;

/**
 * Comparator used for sorting a collections of KPDepots based on their distance (In Miles) from a given origin.
 * All values used in calculations are latitude and longitude
 * @author Jamie Thompson
 *
 * @param <KPdepot>
 */
public class DistanceComparator<KPdepot> implements Comparator<KinekPoint> {
	private double originLat;
	private double originLong;
	private GeoCalculator calculator;
	
	/**
	 * Constructor for comparator
	 * @param originLat The latitude of the origin.
	 * @param originLong The longitude of the origin.
	 */
	public DistanceComparator(double originLat, double originLong, GeoCalculator.NumberSystem numberSystem) {
		this.originLat = originLat;
		this.originLong = originLong;
		calculator = new GeoCalculator(numberSystem);
	}

	@Override
	public int compare(KinekPoint depot1, KinekPoint depot2) {
		Double d1 = calculator.getDistance(originLat, originLong, depot1.getGeolat(), depot1.getGeolong());
		Double d2 = calculator.getDistance(originLat, originLong, depot2.getGeolat(), depot2.getGeolong());
		return d1.compareTo(d2);
	}
}
