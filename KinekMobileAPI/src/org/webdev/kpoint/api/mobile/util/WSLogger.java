package org.webdev.kpoint.api.mobile.util;

import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.webdev.kpoint.bl.logging.KinekLogger;

public class WSLogger extends KinekLogger{
	
	Logger logger = null;
		
	public WSLogger() {
    }
	
	@SuppressWarnings("rawtypes")
	public WSLogger(Class clas){
		super(clas);
		logger = LoggerFactory.getLogger(clas);
	}
	
	public void error(WSApplicationError error){
		MDC.put("errorcode", error.getInternalError().getErrorCode());
		logger.error(getLogMessage(error));
		MDC.remove("errorcode");
		
	}
	
	public void error(WSApplicationError error, Hashtable<String,String> additionalData){
		MDC.put("errorcode", error.getInternalError().getErrorCode());
		addMDCKeys(additionalData);
		
		logger.error(getLogMessage(error));
		
		MDC.remove("errorcode");
		removeMDCKeys(additionalData);
	}
	
	private String getLogMessage(WSApplicationError error){
		String logMessage = "";
		if(error.getException() != null)
			logMessage = error.convertPrintStackToString();
		else
			logMessage = error.getDetailedMessage();
		
		return logMessage;
	}

}