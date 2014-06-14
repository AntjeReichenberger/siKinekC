package org.webdev.kpoint.bl.manager;

import java.util.Calendar;
import java.util.Hashtable;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.Role;
import org.webdev.kpoint.bl.pojo.User;

import com.googlecode.janrain4j.api.engage.EngageFailureException;
import com.googlecode.janrain4j.api.engage.EngageService;
import com.googlecode.janrain4j.api.engage.EngageServiceFactory;
import com.googlecode.janrain4j.api.engage.ErrorResponeException;
import com.googlecode.janrain4j.api.engage.response.UserDataResponse;
import com.googlecode.janrain4j.api.engage.response.profile.Profile;

public class JanrainAuthenticationManager {
	
	private static final KinekLogger logger = new KinekLogger(JanrainAuthenticationManager.class);
	
	public Profile getUserProfileFromJanrain(String token) throws EngageFailureException, ErrorResponeException{
		// Retrieve the Janrain user data
        EngageService engageService = EngageServiceFactory.getEngageService();
        UserDataResponse userDataResponse = engageService.authInfo(token);
        Profile profile = userDataResponse.getProfile();
        
        return profile;
    }
	
	public User populateUser(Profile profile) throws Exception{
	    // Setup user data on local object 
        User openIDUser = new User();
		openIDUser.setOpenID(profile.getIdentifier());
		String primaryKey = profile.getPrimaryKey(); 
		if(primaryKey != null && primaryKey != ""){
			openIDUser.setUserId(Integer.parseInt(primaryKey));
		}
		openIDUser.setEmail(profile.getEmail());
		if(profile.getName() != null){
			openIDUser.setFirstName(profile.getName().getGivenName());
			openIDUser.setLastName(profile.getName().getFamilyName());
		}
		if(profile.getAddress() != null)
		{
			String formattedAddress = profile.getAddress().getFormatted();
			if(formattedAddress != null) {
				String[] formattedAddressSplit = formattedAddress.split(",");
				String city = "";
				String state = "";
				if(formattedAddressSplit.length >= 2){
					//we can get city and state
					//we will determine country based on the state
					city = formattedAddressSplit[0].trim();
					state = formattedAddressSplit[1].trim();
				}
				openIDUser.setCity(city);
				openIDUser.setState(new StateDao().read(state));
			}
		}
		
		return openIDUser;
	}
	
	/**
	 * Returns a fully populated User object if the account mapping is successful or if user already exists.  If this method
	 * determines that the user does not exist and no auto-mapping can be applied via email address,
	 * it returns a partially populated User object which represents the available user data. 
	 * @param openIDUser
	 * @return
	 * @throws Exception
	 */
	public User mapAccounts(User openIDUser) throws Exception{
		//If user successfully set, perform logic to determine if
    	//1 - this is a new user
    	//2 - this is an open id that is associated with an existing user via matching emails
		UserDao userDao = new UserDao();
		User user = userDao.read(openIDUser.getUserId());
		if(user == null){
			//check to see if we have an email match
			//this means the user had an existing kinek account with the same email address as their openid account
			user = userDao.readByEmail(openIDUser.getEmail());
			if(user != null){
				//automatically map openid to user with that email address
				mapOpenIDToUser(openIDUser.getOpenID(), String.valueOf(user.getUserId()));
			}
			else {
				User newUser = openIDUser;
				newUser.setUserId(-1); //this should be cleared since no match was found
				newUser.setUsername(openIDUser.getEmail());
				newUser.setEnabled(true);
				newUser.setRoleId(Role.Consumer);
				newUser.setCreatedDate(Calendar.getInstance());
				newUser.setEnabledDate(Calendar.getInstance());
				int baseDepotId = 1;
				newUser.setKinekPoint(new KinekPointDao().read(baseDepotId));
				user = newUser;
			}
		}
		
		return user;
	}
	
	public User createNewUserAccount(User user) throws Exception{
		//create a new Kinek user account
    	Integer userId = new UserDao().create(user);
    	user.setUserId(userId.intValue());
    	
    	//call Janrain to associate user with openId account
    	mapOpenIDToUser(user.getOpenID(), userId.toString());
    	
    	return user;
	}
	
	
	public void mapOpenIDToUser(String openID, String userId){
		//call Janrain to associate user with openId account
		try{
			EngageService engageService = EngageServiceFactory.getEngageService();
			engageService.map(openID, userId);
		}
		catch (Exception e) {
        	//open id not successful, show login page with error
    		Hashtable<String,String> logData = new Hashtable<String,String>();
    		logData.put("UserId", userId);
    		logData.put("OpenID", openID);

            logger.error(new ApplicationException("Unable to map OpenID to User", e), logData);
        }
	}

}
