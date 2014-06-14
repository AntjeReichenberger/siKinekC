package org.webdev.kpoint.api.partner.util;

import org.webdev.kpoint.bl.util.ApplicationProperty;

public class PropertyReader {

	public static String getAPIContext(){
		return ApplicationProperty.getInstance().getProperty("url.context");
	}						
}
