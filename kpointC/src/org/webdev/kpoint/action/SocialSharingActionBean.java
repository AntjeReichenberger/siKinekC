package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;


@UrlBinding("/SocialSharing.action")
public class SocialSharingActionBean extends BaseActionBean {
    
		
    @DontValidate @DefaultHandler
    public Resolution view() {
    	    	
        return new ForwardResolution("/WEB-INF/jsp/rpx_xdcomm.html");
    }

  
}
