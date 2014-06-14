package org.webdev.kpoint.managers;

import net.sourceforge.stripes.action.Message;
import net.sourceforge.stripes.action.SimpleMessage;

import org.webdev.kpoint.bl.util.ApplicationProperty;

public class MessageManager {
	// PickUpActionBean
	
	/**
	 * Success message displayed when a package has been picked up
	 * @return Success Message
	 */
	public static Message getPickedUpPackage() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.pickeduppackage"));
	}
	
	/**
	 * Success message displayed when multiple packages have been picked up
	 * @return Success Message
	 */
	public static Message getPickedUpPackages() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.pickeduppackages"));
	}
	
	// ManageDepotActionBean
	
	public static Message getSavedDepot() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.saveddepot"));
	}
	
	// ForgotPasswordActionBean
	
	/**
	 * Retrieves the message displayed to the user when they 
	 * successfully have a forgot password email sent to them
	 */
	public static Message getForgotPasswordEmailSent() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.forgotPassword.emailSent"));
	}
	
	// ResetPasswordActionBean
	
	public static Message getResetPasswordSuccess() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.resetPassword.success"));
	}
	
	// ContactActionBean
	
	public static Message getContactSuccess() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.contact"));
	}
	
	// LoginActionBean
	
	public static Message getLoginLogout() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.login.logout"));
	}
	
	public static Message getLoginSessionExpired() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.login.sessionExpired"));
	}
	
	//DeliveryActionBean
	
	public static Message getAcceptDeliveryAcceptedEmailsSent() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.acceptDelivery.acceptedEmailsSent"));
	}

	public static Message getAcceptDelivery() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.acceptDelivery"));
	}

	/**
	 * Retrieves the message displayed when Reminder Emails are successfully sent.
	 * @param numberOfMessagesSent The number of email messages successfully sent
	 * @return The formatted message to display when Reminder Emails are successfully sent
	 */
	public static Message getReminderEmailSuccess(Integer numberOfMessagesSent) {
		String displayMessage;
		if (numberOfMessagesSent == 1)
			displayMessage = ApplicationProperty.getInstance().getProperty("message.reminderEmail.successOne");
		else
			displayMessage = ApplicationProperty.getInstance().getProperty("message.reminderEmail.success");
		
		displayMessage = displayMessage.replaceAll("\\$numberOfMessagesSent", numberOfMessagesSent.toString());
		
		return new SimpleMessage(displayMessage);
	}
	
	public static Message getUnreadNews() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.news.unread"));
	}
	
	public static Message getOrganizationCreateSuccess(){
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.organization.create"));
	}
	
	public static Message getOrganizationEditSuccess(){
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.organization.edit"));
	}	

	public static Message getCouponCreateSuccess(){
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.coupon.create"));
	}
	
}
