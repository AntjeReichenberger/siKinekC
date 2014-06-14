package org.webdev.kpoint.managers;

import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationError;

import org.webdev.kpoint.bl.util.ApplicationProperty;

public class ErrorManager {

	// ForgotPasswordActionBean
	
	public static ValidationError getForgotPasswordNoUserFound() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.forgotPassword.noUserFound"));
	}
	
	// ResetpasswordActionBean
	
	public static ValidationError getResetPasswordInvalidUserId() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.resetPassword.invalidUserId"));
	}
	
	public static ValidationError getResetPasswordPasswordMismatch() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.resetPassword.passwordMismatch"));
	}

}
