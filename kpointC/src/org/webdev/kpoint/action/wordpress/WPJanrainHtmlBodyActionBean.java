package org.webdev.kpoint.action.wordpress;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.action.AuthenticationActionBean;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;


@UrlBinding("/WPJanrainhtmlbody.action")
public class WPJanrainHtmlBodyActionBean extends AuthenticationActionBean{
	
	public String getJanrainTokenUrl() {
		return ExternalSettingsManager.getJanrainSignupTokenUrl();
	}	
	
	 public Resolution getJanRainHtmlBody(){
		 return new ForwardResolution("WEB-INF/jsp/wordpress/wp_janrain_htmlbody.jsp");
	 }

	 public Resolution getJanRainHtmlBodySignup(){
		 return new ForwardResolution("WEB-INF/jsp/wordpress/wp_janrain_htmlbody_singup.jsp");
	 }
}