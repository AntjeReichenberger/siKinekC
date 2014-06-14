package org.webdev.kpoint.action;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.State;

@UrlBinding("/ManageUsers.action")
public class ManageUsersActionBean extends SecureActionBean {
	@Validate(on="userSearch")
    private int depotId;
	@Validate(on="userSearch")
    private int stateId;
	private int userId;
    private List<KinekPoint> depots = new ArrayList<KinekPoint>();
    private List<User> activeUsers = new ArrayList<User>();
    private List<User> inactiveUsers = new ArrayList<User>();
    
	@DefaultHandler @DontValidate
    public Resolution view() throws Exception {
		if (this.getContext().getEventName().equalsIgnoreCase("getDepotsAjax"))
			return getDepotsAjax();

		User activeUser = this.getActiveUser();
	
		//TODO See if depot admin has only one KP
		
		if (activeUser.getDepotAdminAccessCheck()) {
			//depotId = 0; //was originally activeUser.getKinekPoint().getDepotId();
			//stateId = 0; //was originally activeUser.getKinekPoint().getState().getStateId();
	    	return userSearch();
		}	
		
        return new ForwardResolution("/WEB-INF/jsp/manageUsers_step1.jsp");
    }

    public Resolution userSearch() throws Exception {
		fetchUsersByRole(getActiveUser().getRoleId());
		
		if(getActiveUser().getDepotAdminAccessCheck()){
			//----Fetch kinekpoints that are mapped to the depotAdmin
			fetchMappedDepotsForDepotAdmin();
			
			//default depot id
			//TODO depotId = getActiveUser().getDepot().getDepotId();			
			//------------------------------------------------------------			
		}
		
		return new ForwardResolution("/WEB-INF/jsp/manageUsers_step2.jsp");
    }
    
    @HandlesEvent("getDepotsAjax")
    public Resolution getDepotsAjax() throws Exception {
    	List<KinekPoint> allDepots = new KinekPointDao().fetchByState(stateId);
    	StringBuilder b = new StringBuilder();
    	for(KinekPoint depot : allDepots) {
    		if (depot.getState().getStateId() != stateId) continue;
    		b.append("<option value='");
    		b.append(depot.getDepotId());
    		b.append("'>");
    		b.append(depot.getName());
    		b.append("</option>");
    	}
    	StringReader reader = new StringReader(b.toString());
    	return new StreamingResolution("text/html", reader);
    }
	
	/**
	 * Fetches all users mapped to the current depot that have authorization equal to 
	 * or less than the authorization of the provided role. 
	 * @param roleId The id of the role that represents the maximum authorization to retrieve
	 */
	private void fetchUsersByRole(int roleId) throws Exception {
		//TODO
		if(depotId > 0){
			KinekPoint depot = new KinekPointDao().read(depotId);
			List <User> users = new UserDao().fetch(depot, roleId);
			for (User user : users) {
				if (user.getEnabled())
					activeUsers.add(user);
				else
					inactiveUsers.add(user);
			}
		}
	}

	
	private void fetchMappedDepotsForDepotAdmin() throws Exception {
		
		UserDao userDao = new UserDao();
		int userId = getActiveUser().getUserId();
		depots = new ArrayList<KinekPoint>(userDao.fetchUserKinekPoints(userId));
		
		if(depots == null){
			depots = new ArrayList<KinekPoint>();
		}
	}
	
	
	public Resolution searchByDepotId() throws Exception {		
		fetchMappedDepotsForDepotAdmin();
		fetchUsersByRole(getActiveUser().getRoleId());		
		return new ForwardResolution("/WEB-INF/jsp/manageUsers_step2.jsp");
	}
	
    public Resolution completeAjax() {
    	return new RedirectResolution(PickupActionBean.class);
    }
    
    public int getActiveUserCount() {
    	return activeUsers.size();
    }
    
    public int getInactiveUserCount() {
    	return inactiveUsers.size();
    }

	public int getDepotId() {
		return depotId;
	}

	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<KinekPoint> getDepots() {
		return depots;
	}

	public void setDepots(List<KinekPoint> depots) {
		this.depots = depots;
	}

	public List<User> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<User> activeUsers) {
		this.activeUsers = activeUsers;
	}

	public List<User> getInactiveUsers() {
		return inactiveUsers;
	}

	public void setInactiveUsers(List<User> inactiveUsers) {
		this.inactiveUsers = inactiveUsers;
	}

	public List<State> getActiveStates() throws Exception {
		return new StateDao().fetchActive();
	}
	
    public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}
	
	
}
