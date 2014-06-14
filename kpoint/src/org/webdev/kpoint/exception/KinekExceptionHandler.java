package org.webdev.kpoint.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.exception.DefaultExceptionHandler;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.managers.UrlManager;

public class KinekExceptionHandler extends DefaultExceptionHandler {
	
	private static final KinekLogger logger = new KinekLogger(KinekExceptionHandler.class);

	public Resolution handleGeneric(Exception e, HttpServletRequest request, HttpServletResponse response) {
		//only need to log runtime exceptions that are not applicationexceptions.  app exceptions are already logged by the backend
		if(!(e instanceof ApplicationException))
		{
			ApplicationException aex = new ApplicationException("A runtime exception has occurred.", e);
			logger.error(aex);
		}
		return UrlManager.getError();
	}
}
