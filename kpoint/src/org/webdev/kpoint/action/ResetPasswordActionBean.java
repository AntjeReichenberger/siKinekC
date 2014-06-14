package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.managers.ErrorManager;
import org.webdev.kpoint.managers.MessageManager;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.util.Encryption;

@UrlBinding("/ResetPassword.action")
public class ResetPasswordActionBean extends BaseActionBean {
	
	private String key;
	@Validate(required=true, minlength=7, maxlength=40)
	private String passwd;
	@Validate(required=true, minlength=7, maxlength=40)
	private String confPasswd;
	
	private User user;
	
	@Before
	public void setup() {
		this.showHelpButton = false;
	}

	/**
	 * Default method called when the reset.action page is navigated to.
	 * 
	 * It decrypt's the key provided in the userId query string, if its an invalid users
	 * the error page is displayed, if no query string is provided, the error page is displayed.
	 * If all works out, the resetPassword page is displayed.
	 * 
	 * @return the reset password, or the error page, depending on the encryption key 
	 * 	provided in the query string.
	 */
	@DefaultHandler @DontValidate
	public Resolution view() {
		return UrlManager.getResetPasswordForm();
	}
	
	/**
	 * Validates the form when the reset button is clicked.
	 * 
	 * Validation includes. Passwords cannot be null or 
	 * empty, passwords must match.
	 * 
	 * @param errors the list of validation errors to display on the page.
	 */
	@ValidationMethod(on="resetPassword")
    public void resetPasswordValidate(ValidationErrors errors) throws Exception {
		Encryption e = new Encryption(ConfigurationManager.getEncryptionKey());
		String decryptedUserId = e.decrypt(key);	
		
		user = new UserDao().read(Integer.parseInt(decryptedUserId));
		if (user == null) {
			errors.add("key", ErrorManager.getResetPasswordInvalidUserId());
		}
		
		if(!passwd.equals(confPasswd)) {
			errors.add("confPasswd", ErrorManager.getResetPasswordPasswordMismatch());
		}
	}
	
	/**
	 * Method called when the reset button is clicked, and validation passes.
	 * 
	 *  Resets the users password to the new one provided, Then redirects to the
	 *  success page.
	 *  
	 * @return Forward Resolution of the Success Page
	 */
	public Resolution resetPassword() throws Exception {
		user.setPassword(passwd);
		new UserDao().update(user);
		
		setSuccessMessage(MessageManager.getResetPasswordSuccess());
		
		return UrlManager.getLoginForm();
	}
	
	/**Getters and Setters*/	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getPasswd() {
		return passwd;
	}
	
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	public String getConfPasswd() {
		return confPasswd;
	}
	
	public void setConfPasswd(String confPasswd) {
		this.confPasswd = confPasswd;
	}
}