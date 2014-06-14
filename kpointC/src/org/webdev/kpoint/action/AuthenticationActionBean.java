package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.RedirectResolution;

import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.JanrainAuthenticationManager;
import org.webdev.kpoint.bl.manager.SessionManager.SessionKey;
import org.webdev.kpoint.bl.persistence.LoginHistoryDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.LoginHistory;
import org.webdev.kpoint.bl.pojo.User;

import com.googlecode.janrain4j.api.engage.response.profile.Profile;

public class AuthenticationActionBean extends BaseActionBean {
	
	private static final KinekLogger logger = new KinekLogger(AuthenticationActionBean.class);
	
	public RedirectResolution processJanrainResponse(String token) throws Exception {
		JanrainAuthenticationManager manager = new JanrainAuthenticationManager();
		Profile profile = manager.getUserProfileFromJanrain(token);
		User openIdUser = manager.populateUser(profile);
		User mappedUser = manager.mapAccounts(openIdUser);
		if(isTemporaryUser(mappedUser)){
			//display page to link accounts
			setActiveUser(mappedUser);
			setShowAllTabs(false);
			RedirectResolution resolution = new RedirectResolution(LinkOpenIDAccountActionBean.class);
			resolution.addParameter("provider", profile.getProviderName());
			return resolution;
		}
		
		return loginSetup(mappedUser);
			
	}
	
	//checks to see if user has open id assigned which would only be true if they are a new user
	private boolean isTemporaryUser(User mappedUser){
		return mappedUser.getOpenID() != null;	
	}
	
	public RedirectResolution loginSetup(User user) throws Exception {
		updateSessionId(user);
        setActiveUser(user);
        setShowAllTabs(false);
        
        //add a new record to the loginhistory table
		LoginHistory loginRecord = new LoginHistory();
		loginRecord.setUserId(user.getUserId());
		loginRecord.setApplication(user.getApp().toString());
		LoginHistoryDao loginDao = new LoginHistoryDao();
		loginDao.create(loginRecord);
	
		return determineDashboardPage(user);
	}
	
	public RedirectResolution determineDashboardPage(User user) {
	    RedirectResolution resolution = null;
        if(user == null){
            return new RedirectResolution(LoginActionBean.class);
        }
        // If a depot was previously searched for and the user does not have a default depot,
        // go to choose default page with searched depot selected
    	if (user.getDepot() != null && user.getDepot().getDepotId() == 1 && getSessionAttribute(SessionKey.LAST_VIEWED_DEPOT) != null) {
      		resolution = new RedirectResolution(ChooseAKinekPointActionBean.class);
    		resolution.addParameter("depotId", getSessionAttribute(SessionKey.LAST_VIEWED_DEPOT));
    	}
    	// ...otherwise go the the depot search
    	else {
            if (user.getDepot().getDepotId() != 1 && isProfileCompleted(user)) {
                resolution = new RedirectResolution(MyParcelsActionBean.class);
    		} else if (user.getDepot().getDepotId() == 1 && !isProfileCompleted(user)) {
    	        resolution = new RedirectResolution(CompleteYourProfileActionBean.class);
    		} else if (user.getDepot().getDepotId() == 1 && isProfileCompleted(user)) { //no kinekpoint is selected
    	        resolution = new RedirectResolution(ChooseAKinekPointActionBean.class);
    		}
    		else{
    		    //if none of the above, go to the profile page
    			resolution = new RedirectResolution(CompleteYourProfileActionBean.class);
        	}
    	}
        
		return resolution;
	}
	

	/**
     * Updates the provided user with their assigned session id.
     * This also updates persistent storage so that the session id can be retrieved later.
     * @param currentUser The user whose session id is to be updated.
     */
    private void updateSessionId(User currentUser) throws Exception {
    	String sessionId = getContext().getRequest().getSession().getId();
    	currentUser.setSessionId(sessionId);
    	new UserDao().update(currentUser);
    }
    
    /**
     * Return true if all required fields of MyProfile are completed
     * @param u User object  
     * */
    //check USER has all required profile data
    private boolean isProfileCompleted(User u){
    	
    	boolean isComplete=false;
    	
    	if((u.getAddress1()!=null && !u.getAddress1().trim().equals(""))
    			&& (u.getZip()!=null && !u.getZip().trim().equals(""))
    			&& (u.getEmail()!=null && !u.getEmail().trim().equals(""))){
    		
    			isComplete=true;
    	} 
    	
    	return isComplete;
    }
}
