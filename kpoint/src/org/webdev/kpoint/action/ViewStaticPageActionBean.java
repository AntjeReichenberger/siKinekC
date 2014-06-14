package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/ShowPage.action")
public class ViewStaticPageActionBean extends BaseActionBean {
    
   
	private String action;
	
    /* Getters and setters for user and confirmPassword */

    @DefaultHandler
    public Resolution view() {
    	if (action.equalsIgnoreCase("marketing"))
			return new ForwardResolution("/WEB-INF/jsp/marketingmaterials.jsp");

    	if (action.equalsIgnoreCase("agreement"))
			return new ForwardResolution("/WEB-INF/jsp/agreement.jsp");
    	
    	if (action.equalsIgnoreCase("contact"))
			return new ForwardResolution("/WEB-INF/jsp/contactus.jsp");
    	
    	if (action.equalsIgnoreCase("privacy"))
			return new ForwardResolution("/WEB-INF/jsp/privacy.jsp");
    	
    	if (action.equalsIgnoreCase("marketing"))
    		return new ForwardResolution("/WEB-INF/jsp/marketingmaterials.jsp");
    	
    	if (action.equalsIgnoreCase("training"))
    		return new ForwardResolution("/WEB-INF/jsp/trainingmaterials.jsp");
    	
    	if (action.equalsIgnoreCase("help"))
    		return new ForwardResolution("/WEB-INF/jsp/help.jsp");
    	
    	if (action.equalsIgnoreCase("terms"))
			return new ForwardResolution("/WEB-INF/jsp/terms.jsp");
    	
    	if(action.equalsIgnoreCase("faq"))
    		return new ForwardResolution("/WEB-INF/jsp/depot/faq.jsp");
    	
    	if(action.equalsIgnoreCase("success"))
    		return new ForwardResolution("/WEB-INF/jsp/depot/becomeakinekpoint_success.jsp");
    	
    	if(action.equalsIgnoreCase("ampc"))
    		return new ForwardResolution("/WEB-INF/jsp/depot/landing_ampc.jsp");
    	
    	if(action.equalsIgnoreCase("rsa"))
    		return new ForwardResolution("/WEB-INF/jsp/depot/landing_rsa.jsp");
    	
    	if(action.equalsIgnoreCase("annex"))
    		return new ForwardResolution("/WEB-INF/jsp/depot/landing_annex.jsp");
    	
    	return new ForwardResolution("/WEB-INF/jsp/comingsoon.jsp");
    }

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
