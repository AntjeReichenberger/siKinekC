package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/Contact.action")
public class ContactUsActionBean extends AccountDashboardActionBean {

	@DefaultHandler @DontValidate
	public Resolution view() {
		return UrlManager.getContactUsForm();
	}

}
