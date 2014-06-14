package org.webdev.kpoint.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.action.wordpress.WPLoginActionBean;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.managers.MessageManager;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/Login.action")
public class LoginActionBean extends AuthenticationActionBean {

	@Validate(required = true)
	private String username;

	@Validate(required = true)
	private String passwd;

	private User user;

	@DontValidate
	@DefaultHandler
	public Resolution view() throws Exception {
		if(ExternalSettingsManager.getIsInLocalMode()){
			return new ForwardResolution("/WEB-INF/jsp/login.jsp");
		}
		else{
			return new Resolution(){
				@Override
				public void execute(HttpServletRequest arg0,HttpServletResponse arg1) throws Exception {
					String url = UrlManager.getWordPressBaseUrl()+"/sign-in";
					arg1.sendRedirect(url);					
				}
			};
		}
	}

	/**
	 * Handles validation for the login event. The actual authentication occurs
	 * here since we need to ensure a valid user was returned.
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@ValidationMethod(on = "login")
	public void validateLogin() throws Exception {
		user = new UserDao().authenticateConsumer(username, passwd);
		if (user == null) {
			user = new UserDao().authenticateAdmin(username, passwd);
			if (user == null) {
				getContext().getValidationErrors().add("username", new SimpleError("Invalid username/password combination"));
			} else {
				String url = ExternalSettingsManager.getAdminPortalBaseUrl()+ "/Login.action";
				String msg = "You have attempted to login to the consumer portal with an admin user account. <a href=" + url + ">Go to the admin portal.";
				getContext().getValidationErrors().add("username",	new SimpleError(msg));
			}
		}
	}

	/**
	 * Handles the Kinek login event.
	 * 
	 * @return
	 */
	public Resolution login() throws Exception {
		return loginSetup(user);
	}

	/**
	 * Open ID Authentication failure - display page with an error message
	 * 
	 * @return
	 */
	@DontValidate
	public Resolution failure() {
		getContext()
				.getValidationErrors()
				.add("username",
						new SimpleError(
								"An error has occurred authenticating with your OpenID Provider"));
		return new ForwardResolution("/WEB-INF/jsp/login.jsp");
	}

	/**
	 * Log out, resetting all session variables
	 * 
	 * @return Login resolution
	 */
	@DontValidate
	public Resolution logout() {
		getContext().getRequest().getSession().invalidate();
		setSuccessMessage(MessageManager.getLoginLogout());
		return new RedirectResolution(LoginActionBean.class);
	}

	/**
	 * Expires a users session and displays a message indicating that this has
	 * happened.
	 * 
	 * @return Login resolution
	 */
	@DontValidate
	public Resolution sessionExpired() {
		getContext().getRequest().getSession().invalidate();
		setSuccessMessage(MessageManager.getLoginSessionExpired());
		return new RedirectResolution(WPLoginActionBean.class);
	}

	/**
	 * Event called when a user signs in in more than one location.
	 * 
	 * @return Login resolution
	 */
	@DontValidate
	public Resolution signedInElsewhere() {
		getContext().getRequest().getSession().invalidate();
		setSuccessMessage(MessageManager.getLoginSignedInElsewhere());
		return new RedirectResolution(WPLoginActionBean.class);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
}
