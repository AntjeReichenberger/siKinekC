package org.webdev.kpoint.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/JanrainSignupCallback.action")
public class JanrainSignupCallbackActionBean extends AuthenticationActionBean {
	
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
    	HttpServletRequest request = this.getContext().getRequest();
    	String token = request.getParameter("token");

        try {
            return processJanrainResponse(token);
        }
        catch (Exception e) {
        	//open id not successful, show login page with error
    		return new RedirectResolution(SignupActionBean.class, "failure");
        }
    }
}
