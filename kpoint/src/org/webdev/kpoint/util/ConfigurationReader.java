package org.webdev.kpoint.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.LoggerFactory;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.util.ApplicationProperty;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class ConfigurationReader extends HttpServlet {
	private static final KinekLogger logger = new KinekLogger(ConfigurationReader.class);

	private static final long serialVersionUID = 3080451775621008059L;
	private static boolean isLoaded = false;
	
	public static InputStream getInputStream(String fileName) {
        Class<?> clazz = ConfigurationReader.class;
        return clazz.getResourceAsStream(fileName);
    } 
	
	public static void load(Class<ConfigurationReader> c, String filename) throws IOException {
		InputStream propsStream = c.getResourceAsStream(filename);
		Properties temp = new Properties();
		temp.load(propsStream);
		Enumeration<Object> enumeration = temp.keys();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			ApplicationProperty.getInstance().addProperty(key, temp.getProperty(key));
		}
	}

	public static String readFile(String filename) {
		Class<ConfigurationReader> c = ConfigurationReader.class;
		InputStream is = c.getResourceAsStream(filename);

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}
		catch (IOException e) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("FileName", filename);
            logger.error(new ApplicationException("Configuration file could not be read", e), logProps);
		}

		return sb.toString();
	}

	public void init() throws ServletException {
		String path = this.getInitParameter("path");
		String realPath = this.getServletContext().getRealPath(path);
		System.out.println("REALPATH:" + realPath);
		
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	    try {
	      JoranConfigurator configurator = new JoranConfigurator();
	      configurator.setContext(lc);
	      // the context was probably already configured by default configuration rules
	      lc.reset(); 
	      configurator.doConfigure(realPath + "/" + this.getInitParameter("logback_filename"));
	    } catch (JoranException je) {
	      // StatusPrinter will handle this
	    }
	    StatusPrinter.printInCaseOfErrorsOrWarnings(lc);

	    File dir = new File(realPath);
		for (File file : dir.listFiles()) {
			System.out.println("FILE:" + file.getName());
			if (file.getName().endsWith(".properties")) {
				try {
					Properties temp = new Properties();
					temp.load(new FileInputStream(file));
					Enumeration<Object> enumeration = temp.keys();
					while (enumeration.hasMoreElements()) {
						String key = (String) enumeration.nextElement();
						ApplicationProperty.getInstance().addProperty(key, temp.getProperty(key));
					}
				}
				catch (IOException ex) {
					Hashtable<String,String> logProps = new Hashtable<String,String>();
					logProps.put("FileName", realPath);
		            logger.error(new ApplicationException("Properties file could not be read and will not be used.", ex), logProps);
				}
			}
		}
		
		//load other app settings
		try
		{
			load(ConfigurationReader.class, "application.properties");
			load(ConfigurationReader.class, "messages.properties");
			load(ConfigurationReader.class, "errors.properties");
			load(ConfigurationReader.class, "urls.properties");
			load(ConfigurationReader.class, "regex.properties");
			load(ConfigurationReader.class, "emails.properties");
		}
		catch (IOException e) {
            logger.error(new ApplicationException("Properties file could not be read and will not be used.", e));
		}
		isLoaded = true;
	}
}