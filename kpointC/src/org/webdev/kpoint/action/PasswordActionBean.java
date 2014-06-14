package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.managers.MessageManager;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/Password.action")
public class PasswordActionBean  extends AccountDashboardActionBean {
	@Validate(minlength=7, maxlength=40, required=true, on="savePassword")
	private String newPassword;
	@Validate(minlength=7, maxlength=40, required=true, on="savePassword")
	private String confirmPassword;

	@DefaultHandler
	public Resolution view() {
		return UrlManager.getChangePassword();
	}

	@ValidationMethod(on="savePassword")
	public void formValidate(ValidationErrors errors) {
		if (!newPassword.equals(confirmPassword)) {
			errors.add("confirmPassword", new SimpleError("Passwords do not match."));
		}
	}

	public Resolution savePassword() throws Exception {
		User user = getActiveUser();
		user.setPassword(newPassword);
		new UserDao().update(user);
		
		setSuccessMessage(MessageManager.getChangePasswordSuccess());
		return UrlManager.getChangePassword();
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}   

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public boolean getDisplayMessage() {
		String password = getActiveUser().getPassword();
		return (password == null || password.equals(""));
	}
}