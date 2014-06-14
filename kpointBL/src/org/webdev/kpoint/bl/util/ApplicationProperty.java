package org.webdev.kpoint.bl.util;

import java.util.Properties;


public class ApplicationProperty {
	private static ApplicationProperty ref;
	private Properties properties = new Properties();
	

	private ApplicationProperty(){
	
	}

	public static synchronized ApplicationProperty getInstance()
    {
      if (ref == null)
          // it's ok, we can call this constructor
          ref = new ApplicationProperty();
      return ref;
    }

	public Properties getProperties() {
		return properties;
	}

	public String getProperty(String propertyKey) {
		Object propertyValue = properties.get(propertyKey);
		if (propertyValue == null) {
			throw new NullPointerException("Property '" + propertyKey + "' not found.");
		}
		return (String) propertyValue;
	}
	
	public void addProperty(String key, String val) {
		properties.put(key, val);
	}
}