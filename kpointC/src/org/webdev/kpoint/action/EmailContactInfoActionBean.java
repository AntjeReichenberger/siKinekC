package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

import org.webdev.kpoint.converter.ClassConverter;
import org.webdev.kpoint.converter.EmailConverter;
import org.webdev.kpoint.bl.manager.EmailManager;

@UrlBinding("/EmailContactInfo.action")
public class EmailContactInfoActionBean extends AccountDashboardActionBean {
	private static final Resolution FORM_PAGE = new ForwardResolution("/WEB-INF/jsp/adb_emailcontactinfo_form.jsp");
	private static final Resolution PREVIEW_PAGE = new ForwardResolution("/WEB-INF/jsp/adb_emailcontactinfo_preview.jsp");
	
	@Validate(required=true, on="send", converter=EmailConverter.class)
	private String emailAddress;
	private String customMessage;
	private String emailPreview;
	
	
	@Validate(converter=ClassConverter.class)
	private Class<? extends AccountDashboardActionBean> referrer;
	
	@DefaultHandler @DontValidate
	public Resolution view() {
		if (getActiveUser().getDepot().getDepotId() == 1) {
			if (referrer != null)
				return new RedirectResolution(referrer);
			return defaultResolution();
		}
		
		return FORM_PAGE;
	}
	
	public Resolution preview() {
		EmailManager manager = new EmailManager();
		if (customMessage == null)
			customMessage = "";
		emailPreview = manager.getContactInfoEmailBody(getActiveUser(), customMessage);
		return PREVIEW_PAGE;
	}
	
	public Resolution send() throws Exception {
		sendEmail();
		if (emailAddress != null) setSuccessMessage(new SimpleMessage("Successfully sent email to " + emailAddress));
		else setSuccessMessage(new SimpleMessage("Successfully sent email to " + getActiveUser().getEmail()));
		return FORM_PAGE; 
	}
	
	private void sendEmail() throws Exception {
		EmailManager emailManager = new EmailManager();
		if (customMessage == null)
			customMessage = "";
		
		if (emailAddress != null && !emailAddress.isEmpty())
			emailManager.sendContactInfoEmail(emailAddress, getActiveUser(), customMessage);
	}
	
	@DontValidate
	public Resolution cancel() {
		if (referrer != null)
			return new RedirectResolution(referrer);
		return defaultResolution();
	}
	
	@DontValidate
	public Resolution returnToMyParcels() {
		return new RedirectResolution(MyParcelsActionBean.class);
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

	public String getEmailPreview() {
		return emailPreview;
	}

	public void setEmailPreview(String emailPreview) {
		this.emailPreview = emailPreview;
	}

	public Class<? extends AccountDashboardActionBean> getReferrer() {
		return referrer;
	}

	public void setReferrer(Class<? extends AccountDashboardActionBean> referrer) {
		this.referrer = referrer;
	}
}
