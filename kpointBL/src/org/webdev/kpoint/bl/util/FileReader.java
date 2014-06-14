package org.webdev.kpoint.bl.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;


public class FileReader {
	private static final long serialVersionUID = 3080451775621008059L;
	private static final KinekLogger logger = new KinekLogger(FileReader.class);
	
	public static InputStream getInputStream(String fileName) {
        Class<?> clazz = FileReader.class;
        return clazz.getResourceAsStream(fileName);
    } 
	
	public static String readFile(String filename) {
		Class<FileReader> c = FileReader.class;
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
			logProps.put("Filename", filename);
            logger.error(new ApplicationException("Could not read file", e), logProps);
        }

		return sb.toString();
	}
}