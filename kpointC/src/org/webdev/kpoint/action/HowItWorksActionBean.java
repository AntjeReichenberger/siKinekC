package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/HowItWorks.action")
public class HowItWorksActionBean extends BaseActionBean {
    @DefaultHandler
    public Resolution view() {
    	if (getActiveUser() != null) {
			setHideSearch(true);
		}
        //return new ForwardResolution("/WEB-INF/jsp/howitworksOverview.jsp");
    	return UrlManager.getWordPressUrl();
    }
}
