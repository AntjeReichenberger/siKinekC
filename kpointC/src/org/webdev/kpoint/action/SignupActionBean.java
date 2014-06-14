package org.webdev.kpoint.action;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

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

@UrlBinding("/Register.action")
public class SignupActionBean extends BaseActionBean {
	
	@ValidateNestedProperties({
		@Validate(field="password", required=true, on="createUser", minlength=7, maxlength=40),
		@Validate(field="email", required=true, on="createUser", converter=EmailConverter.class)
	})
	private User user;
	
	@Validate(required=true, on="createUser")
	private String confirmPassword;
	
	private boolean prospectRead = false;
	private Prospect _prospect;
	
	@DefaultHandler
	public Resolution view() {
		setSessionAttribute(SessionKey.REGISTER_USER, null);
		
		return new Resolution(){
			@Override
			public void execute(HttpServletRequest arg0,HttpServletResponse arg1) throws Exception {
				String url = UrlManager.getWordPressBaseUrl()+"/sign-up";
				arg1.sendRedirect(url);					
			}
		};
	}

	@ValidationMethod(on="createUser")
	public void setLoginInformationValidation(ValidationErrors errors) throws Exception {
		if (!isEmailAvailable(user.getEmail()) || !isUsernameAvailable(user.getEmail())) {
			errors.add("user.email", new SimpleError("A Kinek account already exists for this email address. Forgot your password? Click "+
					                                       "<a href='ForgotPassword.action'>here</a> to reset"));
		}
		if (!user.getPassword().equals(confirmPassword)) {
			errors.add("confirmPassword", new SimpleError("Passwords must match"));
		}
		
		if(ExternalSettingsManager.isRecaptchaEnabled())
		{
			String remoteAddr = getContext().getRequest().getRemoteAddr();
	        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
	        reCaptcha.setPrivateKey(ExternalSettingsManager.getCaptchaPrivateKey());
	        
	        String challenge = getContext().getRequest().getParameter("recaptcha_challenge_field");
	        String uresponse = getContext().getRequest().getParameter("recaptcha_response_field");
	        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

	        if (!reCaptchaResponse.isValid()) {
	        	errors.add("recaptcha", new SimpleError("The Captcha value you provided was not correct"));
	        }
		}
    }

	public Resolution createUser() throws Exception {
		user.setUsername(user.getEmail());
		user.setEnabled(true);
		user.setRoleId(Role.Consumer);
		user.setCreatedDate(Calendar.getInstance());
		user.setEnabledDate(Calendar.getInstance());
		
		int baseDepotId = 1;
		user.setKinekPoint(new KinekPointDao().read(baseDepotId));
		
		String sessionId = getContext().getRequest().getSession().getId();
		user.setSessionId(sessionId);
		
		new UserDao().create(user);
		if (userIsReferral())
		{
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

	public String getJanrainTokenUrl() {
		return ExternalSettingsManager.getJanrainSignupTokenUrl();
	}

	@Override
	public boolean getHideSearch() {
		return true;
	}
}
