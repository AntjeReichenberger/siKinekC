package org.webdev.kpoint.action.wordpress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.action.BaseActionBean;
import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.util.Encryption;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/WPResetPassword.action")
public class WPResetPasswordActionBean extends BaseActionBean {
	
	private String key;
	private String passwd;
	private String confPasswd;
	
	private User user;	
	
	private boolean hasPasswordMismatch = false;
	private boolean hasInvalidPassword = false;
	private boolean hasValidKey = true;
	
	public Resolution SendErrorsToWP(){
		return new Resolution(){

			@Override
			public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
				// TODO Auto-generated method stub
				String url = UrlManager.getWordPressBaseUrl()+"/reset-password?error=formvalidation";
				
				if(hasPasswordMismatch){
					url += "&hasPasswordMismatch=true";
				}
				if(hasInvalidPassword){
					url += "&hasInvalidPassword=true";
				}
				if(!hasValidKey){
					url += "&hasInvalidKey=true";
				}
				url += "&key=" + getKey();
				res.sendRedirect(url);
			}	
		};
	}
	
	
	
	/**
	 * Method called when the reset button is clicked, and validation passes.
	 * 
	 *  Resets the users password to the new one provided, then redirects to the login 
	 *  page and displays a success message.
	 *  
	 * @return Login Page Resolution
	 */
	public Resolution resetPassword() throws Exception  {
		Encryption e = new Encryption(ConfigurationManager.getEncryptionKey());
		String decryptedUserId = e.decrypt(key);
		user = new UserDao().read(Integer.parseInt(decryptedUserId));
		
		if (user == null) {
			hasValidKey = false;
			return SendErrorsToWP();
		}
		if(passwd == null || passwd.equals("") || passwd.length() < 7 || passwd.length() > 40 ) {
			hasInvalidPassword = true;
			return SendErrorsToWP();
		}	
		
		
		if(!passwd.equals(confPasswd)) {
			hasPasswordMismatch = true;
			return SendErrorsToWP();
		}		
		
		user.setPassword(passwd);
		new UserDao().update(user);
		
		return new Resolution(){
			@Override
			public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
				// TODO Auto-generated method stub
				String url = UrlManager.getWordPressBaseUrl()+"/sign-in?resetPassword=true";
				res.sendRedirect(url);
			}	
		};
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