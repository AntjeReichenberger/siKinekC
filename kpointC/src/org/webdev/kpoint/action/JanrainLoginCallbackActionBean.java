package org.webdev.kpoint.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;

@UrlBinding("/JanrainLoginCallback.action")
public class JanrainLoginCallbackActionBean extends AuthenticationActionBean {
	
	private static final KinekLogger logger = new KinekLogger(JanrainLoginCallbackActionBean.class);
	
	/**
	 * Callback handler for processing authentication responses from JanRain. 
	 * Finishes authentication, and then continues with the Kinek login / registration process.
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
    @DefaultHandler
    public Resolution receive() throws ServletException, IOException {
    	//Get current request
    	logger.info("Start of Janrain callback");
    	HttpServletRequest request = this.getContext().getRequest();
    	String token = request.getParameter("token");
    	
    	// Get the EngageService
        try {
        	return processJanrainResponse(token);
        }
        catch (Exception e) {
        	//open id not successful, show login page with error
        	ApplicationException aex = new ApplicationException("Janrain authentication failed.", e);
    		logger.error(aex);
        	return new RedirectResolution(LoginActionBean.class, "failure");
        }
    }
}