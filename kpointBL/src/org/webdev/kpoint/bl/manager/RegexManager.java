package org.webdev.kpoint.bl.manager;

import org.webdev.kpoint.bl.util.ApplicationProperty;

public class RegexManager {
	
	/**
	 * Returns the regular expression used to determine if an email address is valid
	 * @return The regular expression used to determine if an email address is valid
	 */
	public static String getEmailRegex() {
		return ApplicationProperty.getInstance().getProperty("regex.email");
	}
	
	/**
	 * Returns the regular expression used to determine if a zip/postal code is valid
	 * @return The regular expression used to determine if a zip/postal code is valid
	 */
	public static String getZipRegex() {
		return ApplicationProperty.getInstance().getProperty("regex.zip");
	}
	
	
	/**
	 * Returns the regular expression used to determine if a phone number valid
	 * @return The regular expression used to determine if a phone number is valid
	 */
	public static String getPhoneRegex() {    
		return ApplicationProperty.getInstance().getProperty("regex.phone");
	}
}
