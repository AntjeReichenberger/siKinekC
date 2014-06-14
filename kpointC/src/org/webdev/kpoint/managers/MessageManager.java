package org.webdev.kpoint.managers;

import net.sourceforge.stripes.action.Message;
import net.sourceforge.stripes.action.SimpleMessage;

import org.webdev.kpoint.bl.util.ApplicationProperty;

public class MessageManager {
	public static Message getContactUs() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.contactUs"));
	}
	
	public static Message getMyAccount() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.myAccount"));
	}
	
	public static Message getLoginLogout() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.login.logout"));
	}
	
	public static Message getLoginSessionExpired() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.login.sessionExpired"));
	}
	
	public static Message getLoginSignedInElsewhere() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.login.signedInElsewhere"));
	}
	
	/**
	 * Retrieves the message displayed to the user when they 
	 * successfully have a forgot password email sent to them
	 */
	public static Message getForgotPasswordEmailSent() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.forgotPassword.emailSent"));
	}
	
	/**
	 * Retrieves the message that should be displayed once a user has successfully reset their password 
	 * @return The message that should be displayed once a user has successfully reset their password
	 */
	public static Message getResetPasswordSuccess() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.resetPassword.success"));
	}
	
	public static Message getChangePasswordSuccess() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.changePassword.success"));
	}
	
	public static Message getChangeEmailSuccess() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.changeEmail.success"));
	}
	
	public static Message getSendEmailSuccess() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.sendEmail.success"));
	}

	public static Message getSendSMSSuccess() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.sendSMS.success"));
	}

	public static Message getSetDefaultDepot() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.setDefaulDepot.success"));
	}

	public static Message getKPProspectSuccess() {
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.createKinekPointProspect.success"));
	}
	
	public static Message getAddNewKPSuccess(){
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.addNewKP.success"));	
	}
	
	public static Message getAddNewKPDuplicate(){
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.addNewKP.duplicate"));	
	}
	
	public static Message getRemoveKPSuccess(){
		return new SimpleMessage(ApplicationProperty.getInstance().getProperty("message.removeKP.success"));	
	}

}
