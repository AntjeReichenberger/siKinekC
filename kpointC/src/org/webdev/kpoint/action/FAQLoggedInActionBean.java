package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/FAQLoggedIn.action")
public class FAQLoggedInActionBean extends AccountDashboardActionBean {
	@DefaultHandler
    public Resolution view() {
		if (!getDefaultDepotSelected() || !getAddressInfoComplete())
			return UrlManager.getFAQ();
		return UrlManager.getDashboardFAQ();
    }
}
