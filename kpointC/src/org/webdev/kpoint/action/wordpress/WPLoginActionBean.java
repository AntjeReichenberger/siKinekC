package org.webdev.kpoint.action.wordpress;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.action.AuthenticationActionBean;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.managers.MessageManager;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/WPLogin.action")
public class WPLoginActionBean extends AuthenticationActionBean{

	private String key;
    private String username;
    private String password;
		
	private User user = null;
		
 
	private boolean isUserNameEmpty = false;
	private boolean isPasswdEmpty = false;
	    
	/**
	 * Handles the Kinek login event.
	 * @return
	 * @throws IOException 
	 */
	 public Resolution login() throws Exception {
		 
		 if(username == null || username.isEmpty()){
			 isUserNameEmpty = true;
		 }
		 if(password == null || password.isEmpty()){
			 isPasswdEmpty = true;
		 }
		 
		 if(!isUserNameEmpty && !isPasswdEmpty){
			 user = new UserDao().authenticateConsumer(username, password);
		 }
	     	     
	     if (user == null) {	    	   	 
			return new Resolution(){
				@Override
				public void execute(HttpServletRequest arg0,HttpServletResponse arg1) throws Exception {
					// TODO Auto-generated method stub
					if(username == null){
						username = "";
					}
					String url = UrlManager.getWordPressBaseUrl()+"/sign-in?error=formvalidation&email="+username;
					if(isUserNameEmpty){
						url += "&userNameRequired=true";
					}
					
					if(isPasswdEmpty){
						url += "&pwdRequired=true";
					}
					
					if(!isUserNameEmpty && !isPasswdEmpty){
						url += "&unautherized="+arg1.SC_UNAUTHORIZED;
					}
						
					arg1.sendRedirect(url);					
				}
			};
							
	   	 } 
	   	 return loginSetup(user);
	    
	 }
	 
	 @DontValidate @DefaultHandler
		public Resolution view() {
		  	return new Resolution(){
				@Override
				public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
					// TODO Auto-generated method stub
					res.sendRedirect(ExternalSettingsManager.getConsumerPortalUrl());
				}	  		
		  	}; 
		}
		
	 @DontValidate
	public Resolution logout() {
	 	getContext().getRequest().getSession().invalidate();
	  	setSuccessMessage(MessageManager.getLoginLogout());
	   	
	  	return new Resolution(){
			@Override
			public void execute(HttpServletRequest arg0,HttpServletResponse arg1) throws Exception {
				String url = UrlManager.getWordPressBaseUrl()+"/sign-in";
				arg1.sendRedirect(url);					
			}
		};
	}
		
		public void setKey(String key) {
			this.key = key;
		}


		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
	
}
