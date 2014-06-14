package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/Why.action")
public class WhyActionBean extends BaseActionBean {
    @DefaultHandler
    public Resolution view() {
    	if (getActiveUser() != null) {
			setHideSearch(true);
		}
    	return UrlManager.getWordPressUrl();
    }
}
