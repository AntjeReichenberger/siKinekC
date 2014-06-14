package org.webdev.kpoint.api.partner.util;

import static com.googlecode.janrain4j.conf.Config.Builder.build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.LoggerFactory;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.util.ApplicationProperty;

import com.googlecode.janrain4j.conf.Config;
import com.googlecode.janrain4j.conf.ConfigHolder;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class ConfigurationLoader extends HttpServlet {
	private static final long serialVersionUID = 3080451775621008059L;
	public static boolean isLoaded = false;
	private static final KinekLogger logger = new KinekLogger(ConfigurationLoader.class);
	
	public static InputStream getInputStream(String fileName) {
        Class<?> clazz = ConfigurationLoader.class;
        return clazz.getResourceAsStream(fileName);
    } 
	
	public static void load(Class<ConfigurationLoader> c, String filename) throws IOException {
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
		Class<ConfigurationLoader> c = ConfigurationLoader.class;
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
			logger.error(new ApplicationException("Error occurred parsing the properties file.", e));
		}

		return sb.toString();
	}

	public void init() throws ServletException {
		String path = this.getInitParameter("path");
		String realPath = this.getServletContext().getRealPath(path);
		
		//Configure LogBack
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
					logger.error(new ApplicationException("External properties file could not be read and will not be used.", ex));
				}
			}
		}
		
		 //Configure Janrain (OpenID authentication)
	    Config config = build()
        .apiKey(ApplicationProperty.getInstance().getProperty("janrain4j.api.key"))
        .applicationID(ApplicationProperty.getInstance().getProperty("janrain4j.application.id"))
        .applicationDomain(ApplicationProperty.getInstance().getProperty("janrain4j.application.domain"))
        .tokenUrl(ApplicationProperty.getInstance().getProperty("janrain4j.token.url"));

	    ConfigHolder.setConfig(config);
		
	    //load other app settings
		try
		{
			load(ConfigurationLoader.class, "application.properties");
			load(ConfigurationLoader.class, "emails.properties");
		}
		catch (IOException e) {
            logger.error(new ApplicationException("Properties file could not be read and will not be used.", e));
		}
		
		isLoaded = true;
	}
}