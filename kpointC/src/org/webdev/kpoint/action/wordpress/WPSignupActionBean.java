package org.webdev.kpoint.action.wordpress;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.webdev.kpoint.action.BaseActionBean;
import org.webdev.kpoint.action.CompleteYourProfileActionBean;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.manager.SessionManager.SessionKey;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.ProspectDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.Prospect;
import org.webdev.kpoint.bl.pojo.Role;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.converter.EmailConverter;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/WPSignup.action")
public class WPSignupActionBean extends BaseActionBean {
	
	private static final int max_password_length = 40;
	private static final int min_password_length = 7;
	
	private User user;
		
	private String confirmPassword;
	private String confirmEmail;
	
	private boolean prospectRead = false;
	private Prospect _prospect;
	
	private boolean isValidEmail = true;
	private boolean isValidPasswordLen = true;
	private boolean isEmailEmpty = true;
	private boolean isPasswordEmpty = true;	
	private boolean isNewEmail = true;
	private boolean isValidConfirmPwd = true;
	private boolean isValidCaptchaResponse = true;
	private boolean isValidConfirmEmail = true;
	
	private String password;
	private String email;
	
	@DefaultHandler
	public Resolution view() {
		setSessionAttribute(SessionKey.REGISTER_USER, null);
		
		return UrlManager.getSignUpForm();
	}
	
	public Resolution SendErrorsToWP(){
		return new Resolution(){

			@Override
			public void execute(HttpServletRequest arg0,HttpServletResponse arg1) throws Exception {
				// TODO Auto-generated method stub
				//create url parameter according to validation
				if(email == null){
					email = "";
				}
				String url = UrlManager.getWordPressBaseUrl() + "/sign-up?error=formvalidation&email=" + email;
				
				if(isEmailEmpty){
					url += "&emailRequired=true";
				}
				if(isPasswordEmpty){
					url += "&pwdRequired=true";
				}				
				if(!isNewEmail){
					url += "&existedEmailAddress=true";					
				}
				if(!isValidConfirmPwd){
					url += "&invalidConfirmPwd=true";
				}
				if(!isValidCaptchaResponse){
					url += "&invalidCaptchaRes=true";
				}			
				if(!isValidPasswordLen){
					url += "&invalidPwdLength=true";
				}
				if(!isValidEmail){
					url += "&invalidEmailAddress=true";
				}
				if(!isValidConfirmEmail){
					url += "&invalidConfirmEmail=true";
				}
					
				arg1.sendRedirect(url);
			}
			
		};
	}
	
	public Resolution createUser() throws Exception {
		
		if (email != null && !email.isEmpty()) {
			isEmailEmpty = false;
		}
		if (password != null && !password.isEmpty()) {
			isPasswordEmpty = false;
		}
		
		//required fields
		if (isEmailEmpty || isPasswordEmpty) {
			return SendErrorsToWP();
		}

		//check valid email address format
		if (!EmailConverter.isValidEmailAddress(email)) {
			isValidEmail = false;
			return SendErrorsToWP();
		}
		
		///check password length
		if (password.length() < min_password_length || password.length() > max_password_length) {
			isValidPasswordLen = false;
			return SendErrorsToWP();
		}
		
		if (!isEmailAvailable(email) || !isUsernameAvailable(email)) {
			isNewEmail = false;
		}
		if (!password.equals(confirmPassword)) {
			isValidConfirmPwd = false;
		}
		if (!email.equals(confirmEmail)) {
			isValidConfirmEmail = false;
		}
			
		String remoteAddr = getContext().getRequest().getRemoteAddr();
	    ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
	    reCaptcha.setPrivateKey(ExternalSettingsManager.getCaptchaPrivateKey());
	        
	    String challenge = getContext().getRequest().getParameter("recaptcha_challenge_field");
	    String uresponse = getContext().getRequest().getParameter("recaptcha_response_field");
	    ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
	
	    if (!reCaptchaResponse.isValid()) {
	      	isValidCaptchaResponse = false;
	    }
	        
		if(!isNewEmail || !isValidConfirmPwd || !isValidConfirmEmail || !isValidCaptchaResponse) {
			return SendErrorsToWP();
		}
		
		//create user 
		user = new User();
		user.setUsername(email);
		user.setEmail(email);
		user.setPassword(password);
		user.setEnabled(true);
		user.setRoleId(Role.Consumer);
		user.setCreatedDate(Calendar.getInstance());
		user.setEnabledDate(Calendar.getInstance());
		
		int baseDepotId = 1;
		user.setKinekPoint(new KinekPointDao().read(baseDepotId));
		
		String sessionId = getContext().getRequest().getSession().getId();
		user.setSessionId(sessionId);
		
		new UserDao().create(user);
		if (userIsReferral()) {
			markProspectAsConverted();
		}
		
		setActiveUser(user);
		setShowAllTabs(false);
		
		return new RedirectResolution(CompleteYourProfileActionBean.class);
	}
		
	/**
	 * Determines if the provided user (the current customer) is a referral
	 * @return true if the user was referred by another customer; false otherwise
	 */
	private boolean userIsReferral() throws Exception 
	{
		return new ProspectDao().read(user.getEmail()) != null;
	}
	
	
	private void markProspectAsConverted() throws Exception 
	{
		Prospect prospect = getProspect();
		prospect.setConversionDate(Calendar.getInstance());
		new ProspectDao().update(prospect);
	}
	
	private Prospect getProspect() throws Exception 
	{
		if (!prospectRead)
		{
			_prospect = new ProspectDao().read(user.getEmail());
			prospectRead = true;
		}
		return _prospect;
	}
	
	/**
	 * Determines if the provided email address is available or not.
	 * All email addresses should be unique
	 * @param email The email address to check
	 * @return True if the email address is available; otherwise false
	 */
	private boolean isEmailAvailable(String email) throws Exception {
		return new UserDao().readByEmail(email) == null;
	}

	private boolean isUsernameAvailable(String username) throws Exception {
		return new UserDao().read(username) == null;
	}

	public String getConfirmPasswd() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	@Override
	public boolean getHideSearch() {
		return true;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getWordpressKpointCUrl(){
		return UrlManager.getWordPressBaseUrl();
	}

	public boolean isValidConfirmEmail() {
		return isValidConfirmEmail;
	}

	public void setValidConfirmEmail(boolean isValidConfirmEmail) {
		this.isValidConfirmEmail = isValidConfirmEmail;
	}

	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}
}
