package org.webdev.kpoint.util;

import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;

/**
 * Static methods cannot be referenced from within a JSP when using <jsp:useBean>
 * This class exists as a wrapper for any external settings at must be used in JSP files.
 * Methods should be added here as required.
 * @author Jamie
 *
 */
public class ExternalSettingsManagerHelper {

	
	
	public String getExternalKinekCopyright() {
		return ExternalSettingsManager.getProperty("external.kinek.copyright"); 
	}
	
	public String getExternalKinekContactUsName() {
		return ExternalSettingsManager.getProperty("external.kinek.contactus.name"); 
	}
	public String getExternalKinekContactUsAddressLine1() {
		return ExternalSettingsManager.getProperty("external.kinek.contactus.address.line1"); 
	}
	public String getExternalKinekContactUsAddressLine2() {
		return ExternalSettingsManager.getProperty("external.kinek.contactus.address.line2"); 
	}
	public String getExternalKinekContactUsAddressLine3() {
		return ExternalSettingsManager.getProperty("external.kinek.contactus.address.line3"); 
	}

	public String getExternalKinekPrivacyOfficerContactLine() {
		return ExternalSettingsManager.getProperty("external.kinek.privacyofficer.contactline"); 
	}
	
	/**
	 * Retrieves the authorization key used when communicating with the Google Maps API
	 * @return The key used when communicating with the Google Maps API
	 */
	public String getConsumerPortalGoogleGDataAuthorizationKey() {
		return ExternalSettingsManager.getConsumerPortalGoogleGDataAuthorizationKey();
	}
	
	/**
	 * Retrieves the Base URL (the domain) for the Depot Portal 
	 * @return The Base URL (the domain) for the Depot Portal
	 */
	public String getDepotPortalBaseUrl() {
		return ExternalSettingsManager.getDepotPortalBaseUrl();
	}
	
	public String getConsumerPortalBaseUrl() {
		return ExternalSettingsManager.getConsumerPortalBaseUrl();
	}
	
	public Boolean getIsInDebugMode() {
		return ExternalSettingsManager.getIsInDebugMode();
	}
	
	public Integer getDepotPaymentTypeCreditCardId() {
		return ConfigurationManager.getDepotPaymentTypeCreditCardId();
	}
	
}
