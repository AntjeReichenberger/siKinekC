package org.webdev.kpoint.bl.manager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.webdev.kpoint.bl.util.ApplicationProperty;

public class ConfigurationManager {

	// *** QUERY STRING VARIABLES ***
	/**
	 * Retrieves the string used to identify to the KinekNumberSearchActionBean what page is doing the search
	 * Two values currently exist Accept (Deliver.action) and Redirect (Redirect.action) 
	 * @return Accept string
	 */
	public static String getViaAccept() {
		return ApplicationProperty.getInstance().getProperty("via.accept");
	}

	/**
	 * Retrieves the string used to identify to the KinekNumberSearchActionBean what page is doing the search
	 * Two values currently exist Accept (Deliver.action) and Redirect (Redirect.action) 
	 * @return Redirect string
	 */
	public static String getViaRedirect() {
		return ApplicationProperty.getInstance().getProperty("via.redirect");
	}
	
	// *** MISC ***
	/**
	 * Retrieves the encoding format used when encoding text for use in query strings  
	 */
	public static String getUrlEncoding() {
		return ApplicationProperty.getInstance().getProperty("url.encoding");
	}
	
	public static String getTimeFormat() {
		return ApplicationProperty.getInstance().getProperty("time.format");
	}
	
	/**
	 * Retrieves the fee charged to the consumer by Kinek.
	 * @return Retrieves the fee charged to the consumer by Kinek.
	 */
	public static BigDecimal getKinekFee() {
		return new BigDecimal(ApplicationProperty.getInstance().getProperty("kinek.general.fee"));
	}
	
	/**
	 * Retrieves the fee charged to consumers for redirecting a package
	 * @return The fee charged to consumers for redirecting a package
	 */
	public static BigDecimal getRedirectFee() {
		return new BigDecimal(ApplicationProperty.getInstance().getProperty("kinek.redirect.fee"));
	}
	
	/**
	 * Retrieves the string of text that precedes the Kinek # digits
	 * IE: KINEK56876 -> 'KINEK'
	 * @return The text that precedes the Kinek # digits 
	 */
	public static String getKinekNumberPrecedingText() {
		return ApplicationProperty.getInstance().getProperty("kinekNumber.precedingText");
	}

	/**
	 * Retrieves the package status "Redirected"
	 * @return The package status "Redirected"
	 */
	public static String getPackageStatusRedirected() {
		return ApplicationProperty.getInstance().getProperty("package.status.redirected");
	}
	
	/**
	 * Retrieves the package status "In-Depot"
	 * @return The package status "In-Depot"
	 */
	public static String getPackageStatusInDepot() {
		return ApplicationProperty.getInstance().getProperty("package.status.indepot");
	}

	/**
	 * Retrieves the package status "Picked Up"
	 * @return The package status "Picked Up"
	 */
	public static String getPackageStatusPickedUp() {
		return ApplicationProperty.getInstance().getProperty("package.status.pickedup");
	}
	
	/**
	 * Retrieves the package status "Enroute"
	 * @return The package status "Enroute"
	 */
	public static String getPackageStatusEnroute() {
		return ApplicationProperty.getInstance().getProperty("package.status.enroute");
	}
	
	/**
	 * Retrieves the Kinek email address that all contact emails are sent to
	 * @return The Kinek email address that all contact emails are sent to
	 */
	public static String getContactEmailAddress() {
		return ApplicationProperty.getInstance().getProperty("email.target.contactus");
	}
	
	public static String getEncryptionKey() {
		return ApplicationProperty.getInstance().getProperty("encryption.key");
	}
	
	public static Map<String, String> getHotLocations() {
		Map<String,String> map = new HashMap<String, String>();
		String unparsedString = ApplicationProperty.getInstance().getProperty("hot.location.list");

		String[] locations = unparsedString.split("\\|\\|");

		for (String keyValue : locations) {
			String[] pair = keyValue.split("\\|");
			map.put(pair[0].trim(), pair[1].trim());
		}
		return map;
	}
	
	public static String getDefaultDepotSearchCriteria() {
		return ApplicationProperty.getInstance().getProperty("kinek.depotSearch.defaultCriteria");
	}
	
	public static int getKinekPointRadiusRange(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("kinek.point.radius.range"));
	}

	/**
	 * The number of milliseconds that a login key is valid for use in the case where
	 * a user logs in to the wrong portal
	 * @return The number of milliseconds that a login key is valid for use in the case where
	 * a user logs in to the wrong portal
	 */
	public static long getLoginKeyExpireTime() {
		return Long.parseLong(ApplicationProperty.getInstance().getProperty("loginKey.expireTime"));
	}
	
	/**
	 * The number of records to display per page on the My Parcels ADP tab
	 */
	public static int getMyParcelsPageSize() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("paging.myParcels.pageSize"));
	}
	
	/**
	 * The number of records to display per page on the My Credits ADP tab
	 */
	public static int getMyCreditsPageSize() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("paging.myCredits.pageSize"));
	}
	
	/**
	 * The maximum number of days a package can remain at a depot before it is considered 'abandoned'
	 * @return The number of days a package can remain at a depot before it is considered 'abandoned'
	 */
	public static Integer getMaxDaysInDepot() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("max.days.inDepot"));
	}
	
	public static Integer getDepotPaymentTypeCreditCardId() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("depot.paymentType.creditCardId"));
	}
	
	/**
	 * The string that describes when a depot changes it's contact information
	 * @return The string that describes when a depot changes it's contact information
	 */
	public static String getKinekPointContactChange() {
		return ApplicationProperty.getInstance().getProperty("depot.change.contact");
	}
	
	/**
	 * The string that describes when a depot changes it's services and features information
	 * @return The string that describes when a depot changes it's services and features information
	 */
	public static String getKinekPointServicesAndFeaturesChange() {
		return ApplicationProperty.getInstance().getProperty("depot.change.servicesAndFeatures");
	}
	
	/**
	 * The string that describes when a depot changes it's hours of operation
	 * @return The string that describes when a depot changes it's hours of operation
	 */
	public static String getKinekPointHoursOfOperationChange() {
		return ApplicationProperty.getInstance().getProperty("depot.change.hoursOfOperation");
	}
	
	public static String getKinekPointPricesChange() {
		return ApplicationProperty.getInstance().getProperty("depot.change.prices");
	}
	
	/**
	 * The lowest selectable year when generating reports 
	 * @return The lowest selectable year when generating reports
	 */
	public static Integer getReportsStartYear() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("reports.startYear"));
	}
	
	/**
	 * The maximum number of days that a package can go without a tracking update. 
	 * If no updates are received for this many days, automatic updates will be turned off for that package.
	 */
	public static Integer getMaxDaysWithoutTrackingUpdateThreshold() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("tracking.autoupdates.maxdayswithoutupdate"));
	}
}
