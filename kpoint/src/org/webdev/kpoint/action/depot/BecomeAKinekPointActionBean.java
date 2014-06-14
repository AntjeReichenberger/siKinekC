package org.webdev.kpoint.action.depot;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.action.BaseActionBean;

import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.managers.UrlManager;


@UrlBinding("/BecomeAKinekPoint.action")
public class BecomeAKinekPointActionBean extends BaseActionBean {
	
	private static final KinekLogger logger = new KinekLogger(BecomeAKinekPointActionBean.class);
	
	@DefaultHandler
	public Resolution view() {
		return UrlManager.getBecomeAKinekPointContact();
	}
}
