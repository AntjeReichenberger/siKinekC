package org.webdev.kpoint.action.wordpress;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.action.BaseActionBean;
import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.util.Encryption;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/WPForgotPassword.action")
public class WPForgotPasswordActionBean extends BaseActionBean {
	
	private String username;
	private User user;

	private boolean isUserNameEmpty = false;
	private boolean isInvalidUserName = false;
	
	public Resolution SendErrorsToWP(){
		return new Resolution(){

			@Override
			public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
				// TODO Auto-generated method stub
				String url = UrlManager.getWordPressBaseUrl()+"/forgot-password?error=formvalidation";
				
				if(isUserNameEmpty){
					url += "&usernameRequired=true";
				}
				
				if(isInvalidUserName){
					url += "&invalidUserName=true";
				}
				
				res.sendRedirect(url);
			}
			
		};
	}
	
	/**
	 * Method that sends an email to the user that was entered
	 * in the username box, if that user exists. 
	 * 
	 * @return Forward resolution containing the page displaying the message
	 * 	letting the user know to check their email.
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public Resolution send() throws Exception{
		
		if(username == null || username.isEmpty()){
			isUserNameEmpty = true;
			return SendErrorsToWP();
		}
				
		user = new UserDao().read(username);
		if(user == null) {
			isInvalidUserName = true;
			return SendErrorsToWP();
		}	
		
		Encryption e = new Encryption(ConfigurationManager.getEncryptionKey());
		String encryptedUserId = e.encrypt(String.valueOf(user.getUserId()));
		
		String url = "";
		String encoding = ConfigurationManager.getUrlEncoding();
		if(user.isConsumer()){
			url = ExternalSettingsManager.getConsumerPasswordResetUrl() + "?key=" + URLEncoder.encode(encryptedUserId, encoding);;
		}
		else{
			url = ExternalSettingsManager.getAdminPasswordResetUrl() + "?key=" + URLEncoder.encode(encryptedUserId, encoding);;
		}
		
		new EmailManager().sendPasswordResetEmail(user, url);
		
		//Success! You have been emailed a link to the reset password page
				
		return new Resolution(){
			@Override
			public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
				// TODO Auto-generated method stub
				res.sendRedirect(UrlManager.getWordPressBaseUrl() + "/forgot-password?successMsg=true");
			}
			
		};
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
