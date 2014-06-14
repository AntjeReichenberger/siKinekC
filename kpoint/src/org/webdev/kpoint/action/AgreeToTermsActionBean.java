package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;

@UrlBinding("/Terms.action")
public class AgreeToTermsActionBean extends BaseActionBean {
	private boolean agreedToTOS;
	
	@DontValidate @DefaultHandler 
	public Resolution view() {
		return UrlManager.getTermsOfServiceForm();
	}
	
	@ValidationMethod(on="agree")
	public void agreeValidation(ValidationErrors errors) {
		if (agreedToTOS == false) {
			errors.add("agreeToTOS", new SimpleError("You must agree to the terms of service before continuing"));
		}
	}
	
	/**
	 * Event handler for the Agree action
	 * @return Resolution to DeliveryActionBean
	 */
	public Resolution agree() throws Exception {
		User activeUser = getActiveUser();
		activeUser.setAgreedToTOS(agreedToTOS);
		new UserDao().update(activeUser);
		
		return new RedirectResolution(DeliveryActionBean.class);
	}

	public boolean isAgreedToTOS() {
		return agreedToTOS;
	}

	public void setAgreedToTOS(boolean agreedToTOS) {
		this.agreedToTOS = agreedToTOS;
	}
}
