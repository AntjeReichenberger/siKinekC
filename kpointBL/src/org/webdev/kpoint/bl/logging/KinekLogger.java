package org.webdev.kpoint.bl.logging;

import java.util.Enumeration;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class KinekLogger {

	Logger logger = null;

	public KinekLogger() {
	}

	public KinekLogger(Class<?> clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}

	public void debug(String message) {
		logger.debug(message);
	}

	public void debug(String message, Hashtable<String, String> additionalData) {
		addMDCKeys(additionalData);
		logger.debug(message);
		removeMDCKeys(additionalData);
	}

	public void info(String message) {
		logger.info(message);
	}

	public void info(String message, Hashtable<String, String> additionalData) {
		addMDCKeys(additionalData);
		logger.info(message);
		removeMDCKeys(additionalData);
	}

	public void error(ApplicationException ex) {
		logger.error(ex.getMessage(), ex);
	}

	public void error(String msg) {
		logger.error(msg);
	}

	public void error(ApplicationException ex,
		Hashtable<String, String> additionalData) {
		addMDCKeys(additionalData);
		logger.error(ex.getMessage(), ex);
		removeMDCKeys(additionalData);
	}

	public void error(String msg, Hashtable<String, String> additionalData) {
		addMDCKeys(additionalData);
		logger.error(msg);
		removeMDCKeys(additionalData);
	}

	public void warn(ApplicationException ex) {
		logger.warn(ex.getMessage(), ex);
	}

	public void warn(String msg) {
		logger.warn(msg);
	}

	public void warn(ApplicationException ex, Hashtable<String, String> additionalData) {
		addMDCKeys(additionalData);
		logger.warn(ex.getMessage(), ex);
		removeMDCKeys(additionalData);
	}

	public void warn(String msg, Hashtable<String, String> additionalData) {
		addMDCKeys(additionalData);
		logger.warn(msg);
		removeMDCKeys(additionalData);
	}

	public void addMDCKeys(Hashtable<String, String> additionalData) {
		Enumeration<String> e = additionalData.keys();
		while (e.hasMoreElements()) {
			String key = (String) (e.nextElement());
			String value = (String) additionalData.get(key);
			MDC.put(key, value);
		}
	}

	public void removeMDCKeys(Hashtable<String, String> additionalData) {
		Enumeration<String> e = additionalData.keys();
		while (e.hasMoreElements()) {
			String key = (String) (e.nextElement());
			MDC.remove(key);
		}
	}

}