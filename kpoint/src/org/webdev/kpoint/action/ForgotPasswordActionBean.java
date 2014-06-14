package org.webdev.kpoint.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.managers.ErrorManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.managers.MessageManager;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.util.Encryption;

@UrlBinding("/ForgotPassword.action")
public class ForgotPasswordActionBean extends BaseActionBean {
	
	@Validate(required=true)
	private String username;
	private User user;
	
	@Before
	public void setup() {
		this.showHelpButton = false;
	}

	/**
	 * Default view shown when the user navigates to the forgotPassword.action
	 * @return
	 */
	@DefaultHandler @DontValidate
	public Resolution view(){
		return UrlManager.getForgotPasswordForm();
	}
	
	@ValidationMethod(on="send")
	public void validateSend(ValidationErrors errors) throws Exception  {
		user = new UserDao().read(username);
		if(user == null) {
			errors.add("userName", ErrorManager.getForgotPasswordNoUserFound());
		}		
	}
	
	/**
	 * Method that sends an email to the user that was entered
	 * in the username box, if that user exists. 
	 * 
	 * @return Forward resolution containing the page displaying the message
	 * 	letting the user know to check their email.
	 * @throws UnsupportedEncodingException 
	 */
	public Resolution send() throws Exception{
		Encryption e = new Encryption(ConfigurationManager.getEncryptionKey());		
		String encryptedUserId = e.encrypt(String.valueOf(user.getUserId()));
		
		String baseUrl = user.isConsumer() ? ExternalSettingsManager.getConsumerPortalBaseUrl() : ExternalSettingsManager.getAdminPortalBaseUrl();		
		String encoding = ConfigurationManager.getUrlEncoding();
		String url = baseUrl + "/ResetPassword.action?"	+ "key=" + URLEncoder.encode(encryptedUserId, encoding);
		
		new EmailManager().sendPasswordResetEmail(user, url);
		
		setSuccessMessage(MessageManager.getForgotPasswordEmailSent());
		
		return UrlManager.getForgotPasswordForm();
	}
	
	@After(on="send")
	public void sendAfter() {
		this.username = null;
	}
	
	/**************Getters and Setters**************/	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
