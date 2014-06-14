package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/HowItWorksVideo.action")
public class HowItWorksVideoActionBean extends BaseActionBean {
    @DefaultHandler
    public Resolution view() {
    	if (getActiveUser() != null) {
			setHideSearch(true);
		}
        return new ForwardResolution("/WEB-INF/jsp/howitworksVideo.jsp");
    }
}
