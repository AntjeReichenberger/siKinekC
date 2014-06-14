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
public class SettingsManagerHelper {

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
