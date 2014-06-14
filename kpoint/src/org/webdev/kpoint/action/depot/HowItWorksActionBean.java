package org.webdev.kpoint.action.depot;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.action.BaseActionBean;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/HowItWorks.action")
public class HowItWorksActionBean extends BaseActionBean {
	@DefaultHandler
	public Resolution view() {
		return UrlManager.getHowItWorks();
	}
}
