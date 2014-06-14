package org.webdev.kpoint.action;

import java.io.UnsupportedEncodingException;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.JanrainAuthenticationManager;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;

@UrlBinding("/LinkOpenIDAccount.action")
public class LinkOpenIDAccountActionBean extends AuthenticationActionBean {
    
	@Validate(required=true)
    private String username;
	
	@Validate(required=true)
    private String passwd;
	
	private User user;
	private String provider;
	
	private static final KinekLogger logger = new KinekLogger(LinkOpenIDAccountActionBean.class);
	
    @DontValidate @DefaultHandler
    public Resolution view() {
    	return new ForwardResolution("/WEB-INF/jsp/linkopenidaccount.jsp");
    }

    /**
     * Handles validation for the link event. This ensures that the user credentials
     * the user entered are valid before linking an OpenID account.
     * @throws UnsupportedEncodingException 
     */
    @ValidationMethod(on="link")
    public void validateLogin() throws Exception {
        user = new UserDao().authenticateConsumer(username, passwd);
        
        if (user == null) {
   			getContext().getValidationErrors().add("username", new SimpleError("Invalid username/password combination"));
        }    	
    }
    
    /**
     * Handles the link event.
     * @return
     */
    public Resolution link() throws Exception {
    	//call Janrain to associate user with openId account
    	JanrainAuthenticationManager manager = new JanrainAuthenticationManager();
    	manager.mapOpenIDToUser(getActiveUser().getOpenID(), String.valueOf(user.getUserId()));
    			
	   	//initiate login process
    	return loginSetup(user);
    }
        
    /**
     * Skip linking, create a new account
     * @return Login resolution
     * @throws ApplicationException 
     */
    @DontValidate
    public Resolution skiplink() throws Exception {
    	//Create a new Kinek account and map to Janrain account
    	JanrainAuthenticationManager manager = new JanrainAuthenticationManager();
    	User user = manager.createNewUserAccount(getActiveUser());
    	getActiveUser().setUserId(user.getUserId());
    	
    	return loginSetup(getActiveUser());
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

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
}
