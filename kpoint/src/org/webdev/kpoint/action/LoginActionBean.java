package org.webdev.kpoint.action;

import java.util.Date;
import java.util.List;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.LoginHistoryDao;
import org.webdev.kpoint.bl.persistence.NewsDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.LoginHistory;
import org.webdev.kpoint.bl.pojo.News;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.managers.MessageManager;


@UrlBinding("/Login.action")
public class LoginActionBean extends BaseActionBean implements ValidationErrorHandler{

	private static final KinekLogger logger = new KinekLogger(LoginActionBean.class);

	//private String key;
	
	@Validate(required=true)
	private String username;

	@Validate(required=true)
	private String passwd;

	@Before
	public void setup() {
		this.showHelpButton = false;		
	}

	@DontValidate @DefaultHandler
	public Resolution view() throws Exception {
		if (getActiveUser() != null) return logout();

		return new ForwardResolution("/WEB-INF/jsp/login.jsp");
	}

	@ValidationMethod(on="login")
	public void formValidate(ValidationErrors errors) throws Exception {
		User user = new UserDao().authenticateAdmin(username, passwd);
		if (user != null) {
			user.setApp(User.App.ADMIN_PORTAL);
			this.setSessionAttribute("activeUser", user);
		}
		else {
			user = new UserDao().authenticateConsumer(username, passwd);
			if(user != null)
			{
				String url = this.getBaseConsumerPortalUrl() + "/Login.action";
				String msg = "You have attemped to login to the admin portal with a consumer user account. <a href=" + url+ ">Go to the consumer portal.</a>";
				errors.add("username", new SimpleError(msg));
			}
			else
			{
				errors.add("username", new SimpleError("Invalid username/password combination"));
			}
		}
	}

	public Resolution login() throws Exception {
		User currentUser = getActiveUser();
		if (currentUser != null) {        	
			//get the active users last login date to determine if they should see the news
			LoginHistoryDao loginDao = new LoginHistoryDao();
			LoginHistory loginHistory = loginDao.fetchLastLogin(currentUser.getUserId());
			Date lastLoginDate = null;
			if(loginHistory != null)
			{
				lastLoginDate = loginHistory.getLoginDate().getTime();
			}
			//add a new record to the loginhistory table
			LoginHistory loginRecord = new LoginHistory();
			loginRecord.setUserId(currentUser.getUserId());
			loginRecord.setApplication(currentUser.getApp().toString());
			loginDao.create(loginRecord);
		
			//get the news articles that have been published since the last login date
			NewsDao newsDao = new NewsDao();
			List<News> newsArticles = newsDao.fetch(lastLoginDate);
			if(newsArticles != null && newsArticles.size() > 0){
				//if there are news articles available, then redirect user to view them
				RedirectResolution res = new RedirectResolution(NewsListActionBean.class);
				res.addParameter("unreadNews", "true");
				return res;
			}
						
			if (getActiveUser().getAdminAccessCheck()) {
				return new RedirectResolution(ViewKinekPointActionBean.class);
			}
			else {
				return new RedirectResolution(DeliveryActionBean.class);
			}
		}	
		return new RedirectResolution(LoginActionBean.class);
	}
	
	@DontValidate
	public Resolution logout() {
		getContext().getRequest().getSession().invalidate();
		setSuccessMessage(MessageManager.getLoginLogout());
		return new RedirectResolution(LoginActionBean.class);
	}
	
	@DontValidate
	public Resolution sessionExpired() {
		getContext().getRequest().getSession().invalidate();
		setSuccessMessage(MessageManager.getLoginSessionExpired());
		return new RedirectResolution(LoginActionBean.class);
	}	
	
	
	/**
	 * If there are any validation errors on the Login action bean, we always redirect to the Login page
	 */
	public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
		return new ForwardResolution("/WEB-INF/jsp/login.jsp");
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
