package org.webdev.kpoint.action;

import java.util.Hashtable;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;

import com.googlecode.janrain4j.api.engage.EngageService;
import com.googlecode.janrain4j.api.engage.EngageServiceFactory;

@UrlBinding("/UnmapOpenID.action")
public class UnmapOpenIDActionBean extends AuthenticationActionBean {
    
	private static final KinekLogger logger = new KinekLogger(AuthenticationActionBean.class);
	
	@Validate(required=true)
    private String username;
	
	@Validate(required=true)
    private String passwd;
	
    @DontValidate @DefaultHandler
    public Resolution view() {
    	return new ForwardResolution("/WEB-INF/jsp/unmapOpenID.jsp");
    }

    /**
     * Handles the Kinek unmap event.
     * @return
     */
    public Resolution unmap() throws Exception {
        User user = new UserDao().authenticateConsumer(username, passwd);
        if(user != null){
        	unmapOpenID(String.valueOf(user.getUserId()));
        }
      
    	/** Temporarily used to reset all users in the database
    	 * int maxUserId = 27193;
    	for(int i=0; i<maxUserId; i++){
        	unmapOpenID(String.valueOf(i));
    	}
    	 */
    	
    	return new ForwardResolution("/WEB-INF/jsp/unmapOpenID.jsp");

    }
    
    protected void unmapOpenID(String userId){
		//call Janrain to associate user with openId account
		try{
			EngageService engageService = EngageServiceFactory.getEngageService();
			engageService.unmap(userId, true);
		}
		catch (Exception e) {
        	//open id not successful, show login page with error
    		Hashtable<String,String> logData = new Hashtable<String,String>();
    		logData.put("UserId", userId);
    		
            logger.error(new ApplicationException("Unable to map OpenID to User", e), logData);
        }
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
