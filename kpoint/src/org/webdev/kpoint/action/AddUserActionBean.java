package org.webdev.kpoint.action;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.bl.manager.SessionManager.SessionKey;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.RoleDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Role;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.converter.EmailConverter;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/User.action")
public class AddUserActionBean extends SecureActionBean {
    
	private int userId;
	@ValidateNestedProperties({
		@Validate(field="firstName", required=true, on={"editUser", "createUser"}),
		@Validate(field="lastName", required=true, on={"editUser", "createUser"}),
		@Validate(field="username", required=true, on={"editUser", "createUser"}),
		@Validate(field="email", required=true, on={"editUser", "createUser"}, converter=EmailConverter.class),
		@Validate(field="password", minlength=7, maxlength=40, required=true, on={"editUser", "createUser"}),
		@Validate(field="roleId", required=true, on={"editUser", "createUser"}) // TODO: Add role converter ?
	})
	private User user;
	@Validate(required=true, minlength=7, maxlength=40, on={"editUser", "createUser"})
	private String confirmPassword;
    
		
	private List<KinekPoint> depots;
	private String selectedDepotIds;
	private List<KinekPoint> selectedDepots;
	private int selectedRoleId;
	private int depot;

	
	public int getDepot() {
		return depot;
	}

	public void setDepot(int depot) {
		this.depot = depot;
	}

	@Before(on="view")
	public void viewBefore() {
    	setSessionAttribute(SessionKey.EDIT_USER, null);
	}
	
    @HandlesEvent("getDepotsAjax")
    public Resolution getDepotsAjax() throws Exception {
    	List<KinekPoint> availableDepots;
    	User activeUser = this.getActiveUser();
    
    	
    	if(user != null){
	    	if(user.getRoleId() == Role.DepotStaff  && activeUser.getDepotAdminAccessCheck()){
	    		availableDepots = new UserDao().fetchUserKinekPoints(activeUser.getUserId());
	    	}
	    	else if(user.getRoleId() == Role.DepotStaff  && activeUser.getAdminAccessCheck()){
	    		availableDepots = new KinekPointDao().fetchLight();
	    		User depotStaff = new UserDao().read(userId);
	    		if(depotStaff.getKinekPoint() != null){
	    			user.setKinekPoint(depotStaff.getKinekPoint());
	    		}
	    	}
	    	else{
	    		availableDepots = new KinekPointDao().fetchLight();
	    	}
	    	
	    	StringBuilder b = new StringBuilder();
	    	if((user.getDepot() != null)){
	    		b.append("<option value='");
	    		b.append(user.getDepot().getDepotId());
	    		b.append("'>");
	    		b.append(user.getDepot().getNameAddress1City());
	    		b.append("</option>");
	    	}
	    	else{
	    		b.append("<option value='0'>Please select a depot</option>");
	    	}
	    	
	    	for(KinekPoint depot : availableDepots) {
	    		if(!(user.getDepot() != null && depot.getDepotId() == user.getDepot().getDepotId())){
		    		b.append("<option value='");
		    		b.append(depot.getDepotId());
		    		b.append("'>");
		    		b.append(depot.getNameAddress1City());
		    		b.append("</option>");
	    		}
	    	}
	    	StringReader reader = new StringReader(b.toString());
	    	return new StreamingResolution("text/html", reader);
	    	}
    	
    	StringBuilder b = new StringBuilder();
    	b.append("<option value='0'>Please select a depot</option>");
    	StringReader reader = new StringReader(b.toString());
    	return new StreamingResolution("text/html", reader);
    }
	

    @DefaultHandler
    public Resolution view() throws Exception {
    	User activeUser = this.getActiveUser();

    	if (userId > 0) {    		
    		user = new UserDao().read(userId);
    		setConfirmPassword(user.getPassword());
    		// Don't allow unauthorized edits
    		if (!canEditUser(activeUser, user)) {
    			return new RedirectResolution(ManageUsersActionBean.class);
			}
    		setSessionAttribute(SessionKey.EDIT_USER, user);
    	}
    	
    	if (user!=null)
    		setConfirmPassword(user.getPassword());
    	
    	if(user == null)
    		user = new User();
    	
    	selectedDepotIds = "";
    	List<KinekPoint> selectedDepots = getSelectedDepotListBox();
		for(KinekPoint selectedDepot : selectedDepots){
			selectedDepotIds += selectedDepot.getDepotId() + ",";	
		}
    	
    	ForwardResolution resolution = (ForwardResolution) UrlManager.getAddUserForm();
    	resolution.addParameter("userId", userId);
        return resolution;
    }
    
    @ValidationMethod(on={"editUser"})
    public void editValidate(ValidationErrors errors) throws Exception {
    	if(Role.DepotAdmin != user.getRoleId() && depot != 0){
    		user.setDepot(new KinekPointDao().read(depot));
    	}
    	else if(Role.DepotAdmin != user.getRoleId() && depot == 0){
    		errors.add("user.depot", new SimpleError("No depot selected."));
    	}
    	
    	if(getActiveUser().getAdminAccessCheck()){
	    	if (user.getDepot() == null && Role.DepotAdmin == user.getRoleId()) {
				if(selectedDepotIds == null){
					errors.add("user.depot", new SimpleError("No depots selected."));
				}		
	   		}
    	}
    	 
    	if (!isUsernameAvailable(user.getUsername())) {
    		errors.add("user.username", new SimpleError("Sorry, this username is taken. Please select another."));
    	}
    	if (!user.getPassword().equals(user.getConfirmPassword())) {
    		errors.add("confirmPassword", new SimpleError("Passwords do not match."));
   		}
    	
    }
    
    @ValidationMethod(on={"createUser"})
    public void createValidate(ValidationErrors errors) throws Exception  {
    	if(Role.DepotAdmin != user.getRoleId() && depot != 0){
    		user.setDepot(new KinekPointDao().read(depot));
    	}
    	else if(Role.DepotAdmin != user.getRoleId() && depot == 0){
    		errors.add("user.depot", new SimpleError("No depot selected."));
    	}
    	
    	if (user.getDepot() == null && Role.DepotAdmin == user.getRoleId()) {
			if(selectedDepotIds == null){
				errors.add("user.depot", new SimpleError("No depots selected."));
			}		
   		}	
    	 
    	if (!isUsernameAvailable(user.getUsername())) {
    		errors.add("user.username", new SimpleError("Sorry, this username is taken. Please select another."));
    	}
    	if (!user.getPassword().equals(user.getConfirmPassword())) {
    		errors.add("confirmPassword", new SimpleError("Passwords do not match."));
   		}
    }

    public Resolution editUser() throws Exception  {
    	
    	int currentRoleId = 0;
    	
    	// this is the logged in user (e.g. the Admin)
    	User activeUser = this.getActiveUser();
        
    	// grab the initial user profile out of session (populated with all values BEFORE the edit) 
        User editUser = (User)getSessionAttribute(SessionKey.EDIT_USER);

        currentRoleId = editUser.getRoleId();
        
		// Don't allow unauthorized edits
		if (!canEditUser(activeUser, editUser)) {
			return new RedirectResolution(ManageUsersActionBean.class);
		}

		// update the our initial user with the values submitted from the form (aka the "user object)
        editUser.setEnabled(user.getEnabled());
        editUser.setFirstName(user.getFirstName());
        editUser.setLastName(user.getLastName());
        editUser.setUsername(user.getUsername());
        editUser.setEmail(user.getEmail());
        editUser.setPassword(user.getPassword());
        editUser.setCellPhone(user.getCellPhone());        
        editUser.setRoleId(user.getRoleId());
        
		// If the user is a KinekAdmin, they are allowed to change the depot ... so use the value from the form
        if (activeUser.getAdminAccessCheck()) {
        	editUser.setKinekPoint(user.getKinekPoint());
        	
            //Handle multiple kinekpoints for Depot ADMIN ONLY. The multiple kps will be inserted into the user_kinepoint table
            List<KinekPoint> selectedDepots = new ArrayList<KinekPoint>();
            List<KinekPoint> dbDepots = new ArrayList<KinekPoint>(new UserDao().fetchUserKinekPoints(editUser.getUserId()));
            
            if(selectedDepotIds != null && (Role.DepotAdmin == user.getRoleId())){
            	String [] selectedDepotIdArray = selectedDepotIds.split(",");
            	KinekPointDao depotDao = new KinekPointDao();            		
            	
            	//Remove if depot is available in the db but not in the selectedDepotIdArray
            	for(int i=0; i<dbDepots.size(); i++){
            		boolean isFound = false;            		
            		int dbDepotId = dbDepots.get(i).getDepotId();
            		
            		for(int j=0; j<selectedDepotIdArray.length; j++){
            			if(dbDepotId == Integer.parseInt(selectedDepotIdArray[j])){
            				isFound = true;            				
            				break;
            			}
            		}
            		if(!isFound){
            			dbDepots.remove(i);
            			i = 0;
            		}
            	}
            	
            	//Add if depot is available in the selectedDepotIdArray but not in the DB
            	for(int i=0; i<selectedDepotIdArray.length; i++){
            		boolean isFound = false;                        		
            		for(int j=0; j<dbDepots.size(); j++){
            			int dbDepotId = dbDepots.get(j).getDepotId();
            			if(Integer.parseInt(selectedDepotIdArray[i]) == dbDepotId){
            				isFound = true;
            				break;
            			}
            		}
            		if(!isFound){
            			KinekPoint newDepot = new KinekPointDao().read(Integer.parseInt(selectedDepotIdArray[i]));
            			dbDepots.add(newDepot);
            		}
            	}
            	
            	editUser.setKinekPoints(new HashSet<KinekPoint>(dbDepots));
            }else{
            	if(user.getDepot() != null){
            		Set<KinekPoint> singleDepotSet = new HashSet<KinekPoint>();
    	        	singleDepotSet.add(user.getKinekPoint());
    	        	editUser.setKinekPoints(singleDepotSet);
            	}
            	else{
            		editUser.setKinekPoints(null);
            	}
            }
        }
        else 
        {
        	// otherwise ... just use the logged in user's Depot
        	//editUser.setKinekPoint(activeUser.getKinekPoint());
        	if(user.getKinekPoint() != null){
	        	editUser.setKinekPoint(user.getKinekPoint());
	        	//depot staff will always have one single kinekpoint
	        	Set<KinekPoint> singleDepotSet = new HashSet<KinekPoint>();
	        	singleDepotSet.add(user.getKinekPoint());
	        	editUser.setKinekPoints(singleDepotSet);
	        }
        }
        
        //set the Province / State of this user to match the Depot that was just assigned
        //We no longer set the state of the user based on depot editUser.setState(editUser.getKinekPoint().getState());
        
        new UserDao().update(editUser);
        
        setSuccessMessage(new SimpleMessage("Successfully edited user"));
        return new RedirectResolution(ManageUsersActionBean.class);
    }
    
    public Resolution createUser() throws Exception {
    	// this is the logged in user (e.g. the Admin)
    	User activeUser = this.getActiveUser();
        
   		user.setCreatedDate(Calendar.getInstance());
    	
   		//DAWSON said it is useless to set state where we don't need to insert address info. But we are not 
   		//100% sure that it will not break the code internally........For this reason, it is commented out.
        //user.setState(user.getKinekPoint().getState());
        
        // If the user is NOT a KinekAdmin or DepotAdmin, they cannot define the depot ... so we'll just use logged in user's Depot
        if (!activeUser.getAdminAccessCheck() && !activeUser.getDepotAdminAccessCheck()) {
        	user.setKinekPoint(activeUser.getKinekPoint());
        }
        
        //Handle multiple kinekpoints for Depot ADMIN ONLY. The multiple kps will be inserted into the user_kinepoint table
        List<KinekPoint> selectedDepots = new ArrayList<KinekPoint>();
        if(selectedDepotIds != null && (user.getRoleId() == Role.DepotAdmin)){
        	String [] selectedDepotIdArray = selectedDepotIds.split(",");
        	KinekPointDao depotDao = new KinekPointDao();        	 
        	for(String selectedDepotId:selectedDepotIdArray){
        		KinekPoint selectedDepot = depotDao.read(Integer.parseInt(selectedDepotId));        	
        		selectedDepots.add(selectedDepot);        		
        	}          
        	user.setKinekPoints(new HashSet<KinekPoint>(selectedDepots));
        }
        else if(user.getRoleId() == Role.DepotStaff){
        	Set<KinekPoint> singleFav = new HashSet<KinekPoint>();
        	singleFav.add(user.getKinekPoint());
        	user.setKinekPoints(singleFav);
        }
    	
    	new UserDao().create(user);
    	setSuccessMessage(new SimpleMessage("Successfully created user"));
    	
    	if(user.getRoleId() == Role.DepotAdmin){
    		new EmailManager().sendAdminCreationEmail(user);
    	}
    	else if(user.getRoleId() == Role.DepotStaff){
    		new EmailManager().sendNewKinekPointStaffEmail(user);
    	}
    	
        return new RedirectResolution(ManageUsersActionBean.class);
    }
    
    @After(on="store")
    public void storeAfter() {
        setSessionAttribute(SessionKey.EDIT_USER, null);
    }
    
    /**
     * Compares the roles and depots of the activeUser with the editUser to determine if the
     * activeUser is permitted to edit the editUser
     * @param activeUser The current user
     * @param editUser The user who is to be edited
     * @return true if the activeUser is permitted to edit the editUser; otherwise, false 
     */
    private boolean canEditUser(User activeUser, User editUser) throws Exception {
    	// Anyone can edit themselves
    	if (activeUser.getUserId() == editUser.getUserId())
    		return true;
    	
    	int roleId = activeUser.getRoleId();
    	// KinekAdmin can edit everyone, as can ReportAdmins
    	if (roleId == Role.KinekAdmin || roleId == Role.ReportAdmin)
    		return true;
    	if (roleId == Role.DepotAdmin) {
    		// DepotAdmin can only edit users for their depot
    		boolean found = false;
    		
    		//TODO
    		List<KinekPoint> usersKps = new UserDao().fetchUserKinekPoints(activeUser.getUserId());
    		List<KinekPoint> editKps = new UserDao().fetchUserKinekPoints(editUser.getUserId());
    		for(KinekPoint userKp: usersKps){
    			for(KinekPoint editKp: editKps){
	    			if(userKp.getDepotId() == editKp.getDepotId()){
	    				found = true;
	    				break;
	    			}	
    			}
    			if(found){
    				break;
    			}
    		}   		
    		if(!found){	
    			return false;
    		}
    		// DepotAdmins cannot edit KinekAdmins or ReportAdmins 
    		if (editUser.getRoleId() == Role.KinekAdmin || editUser.getRoleId() == Role.ReportAdmin)
    			return false;
    		return true;
    	}
    	//Everyone else cannot edit users
    	return false;
    }
    
    /**
     * Determines if a provided user.username is available
     * @param user The user to test
     * @return True if the provided username is available, otherwise false
     */
    public boolean isUsernameAvailable(String username) throws Exception  {
    	// Username doesn't exist
    	User usernameCheck = new UserDao().read(username);
    	if (usernameCheck == null)
    		return true;
    	
    	// Username exists, but it is mapped to the user which is being edited
    	User editUser = (User)getSessionAttribute(SessionKey.EDIT_USER);    	
    	if (editUser != null && usernameCheck.getUserId() == editUser.getUserId())
    		return true;
    	
    	return false;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Resolution displayDepots() throws Exception {
		List<KinekPoint> depotOptions = null;
		StringBuilder builder = new StringBuilder();
		int defaultDepotId=0;
		
		if(getActiveUser().getDepotAdminAccessCheck()){			
	    	//----Fetch kinekpoints that are mapped to the depotAdmin	    	
	    	UserDao userDao = new UserDao();
	    	int userId = getActiveUser().getUserId();
	    	//defaultDepotId = getActiveUser().getDepot().getDepotId();
	    		    		    	
	    	depots = new ArrayList<KinekPoint>(userDao.fetchUserKinekPoints(userId));
	    	
			depotOptions = depots;
		}
		else if(selectedRoleId == Role.DepotAdmin){			
			//suppose user get error msg on the page then we lost the user id because in that time we don't have userId url param. For this reason, in that time we will get the user id from session			
			if(userId <= 0){
				user = (User)getSessionAttribute(SessionKey.EDIT_USER);
				if(user != null){
					userId = user.getUserId();
					//TODO defaultDepotId = user.getDepot().getDepotId();
				}
			}else if(userId > 0){
				user = new UserDao().read(userId);
				//defaultDepotId = user.getDepot().getDepotId();
			}
			depotOptions = getSelectedDepotListBox();
		}
		else{
			//suppose user get error msg on the page then we lost the user id because in that time we don't have userId url param. For this reason, in that time we will get the user id from session			
			if(userId <= 0){
				user = (User)getSessionAttribute(SessionKey.EDIT_USER);
				if(user != null && user.getDepot() != null){
					defaultDepotId = user.getDepot().getDepotId();
				}
			}else if(userId > 0){
				user = new UserDao().read(userId);
				defaultDepotId = user.getDepot().getDepotId();
			}
			depotOptions = new KinekPointDao().fetchLight();
		}
		
		if(depotOptions == null){
			depotOptions = new ArrayList<KinekPoint>();
		}
		
		builder.append("{\"depots\":[");
		for(KinekPoint depotOpt:depotOptions){
			builder.append("{\"id\":\""+depotOpt.getDepotId()+"\",\"name\":\""+depotOpt.getName()+"\"},");
		}		
		builder.append("],\"defaultDepotId\":\""+defaultDepotId+"\"}");
		return new StreamingResolution("text",new StringReader(builder.toString()));
	}
	
	public Collection<Role> getDisplayRoles() throws Exception  {
		return new RoleDao().fetch(getActiveUser().getRoleId());
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public String getSelectedDepotIds(){
		return selectedDepotIds;
	}
	public void setSelectedDepotIds(String selectedDepotIds){
		this.selectedDepotIds = selectedDepotIds;
	}
	
	public void setListDepots(List<KinekPoint> depots){
		this.depots = depots;
	}
	public List<KinekPoint> getListDepots(){
		return depots;
	}
	
	public List<KinekPoint> getSelectedDepotListBox() throws Exception {
		
		if(selectedDepotIds == null || selectedDepotIds.equals("") || selectedDepotIds.equals(" ")  || selectedDepotIds.equals(",")){
			selectedDepots = new ArrayList<KinekPoint>();
			UserDao userDao = new UserDao();
			
			if(userId > 0 && !getActiveUser().getDepotAdminAccessCheck()){
				selectedDepots = new ArrayList<KinekPoint>(userDao.fetchUserKinekPoints(userId));
			}
		}
		else{
			selectedDepots = new ArrayList<KinekPoint>();
			String [] selectedDepotIdArray = selectedDepotIds.split(",");
			KinekPointDao depotDao = new KinekPointDao(); 
			
			for(String selectedDepotId:selectedDepotIdArray){
        		KinekPoint selectedDepot = depotDao.read(Integer.parseInt(selectedDepotId));        	
        		selectedDepots.add(selectedDepot);        		
			}
		}
		
		return selectedDepots;
	}
	
	public List<KinekPoint> getDepotListBox() throws Exception {
		List<KinekPoint> depots = new ArrayList<KinekPoint>();
		if(getActiveUser().getAdminAccessCheck())
			depots = new KinekPointDao().fetchLight();
		else
			depots = new UserDao().fetchUserKinekPoints(getActiveUser().getUserId());
			
		
    	List<KinekPoint> selectedDepots = null;
	
    	if(user.getUserId() != -1){
    		UserDao userDao = new UserDao();
    		//----Fetch kinekpoints that are mapped to the user_kinekpoint table
    		selectedDepots = new ArrayList<KinekPoint>(userDao.fetchUserKinekPoints(user.getUserId()));    	
		
    		//search selectedDepot list box and remove depot from depot list  
    		if(selectedDepots != null && selectedDepots.size() > 0){
    			for(int i=0; i<depots.size(); i++){
    				int depotId = depots.get(i).getDepotId();
    			
    				for(int j=0; j<selectedDepots.size(); j++){
    					int selectedDepotId = selectedDepots.get(j).getDepotId();
    					if(depotId == selectedDepotId){
    						depots.remove(i);
    						i = 0; //if item remove from list then rest of item is moved
    						break;
    					}
    				}    			
    			}
    		}
    	}
    	
		return depots;
	}
	
	//Used on create user list
	public List<KinekPoint> getDepotListCreate() throws Exception {
		if(getActiveUser().getDepotAdminAccessCheck()){
			UserDao userDao = new UserDao(); 
			int userId = getActiveUser().getUserId(); 
			return new ArrayList<KinekPoint>(userDao.fetchUserKinekPoints(userId));			
		}else{
			return new ArrayList<KinekPoint>();
		}		
	}
	
	public int getDepotAdminRoleId(){
		return Role.DepotAdmin;
	}
	public int getSelectedRoleId(){
		return selectedRoleId;
	}
	public void setSelectedRoleId(int selectedRoleId){
		this.selectedRoleId = selectedRoleId;
	}

}
