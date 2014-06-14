package org.webdev.kpoint.action;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.persistence.KinekPointStatusDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KinekPointStatus;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.User;

@UrlBinding("/DepotList.action")
public class FindDepotActionBean extends SecureActionBean {
	private String location;
	private int statusId;
	private int depotId;
	private List<User> activeUsers = new ArrayList<User>();
    private List<User> inactiveUsers = new ArrayList<User>();

	@DefaultHandler
	public Resolution view() throws Exception {
		statusId = 1;

		if (depotId == 0) {
			return UrlManager.getFindDepotResults();
		}

		fetchUsersByRole(getActiveUser().getRoleId());
		return UrlManager.getFindDepotUsers();
	}
	
	/**
	 * Fetches all users mapped to the current depot that have authorization equal to 
	 * or less than the authorization of the provided role. 
	 * @param roleId The id of the role that represents the maximum authorization to retrieve
	 */
	private void fetchUsersByRole(int roleId) throws Exception  {
		KinekPoint depot = new KinekPointDao().read(depotId);
		List <User> users = new UserDao().fetch(depot, roleId);
		for (User user : users) {
			if (user.getEnabled())
				activeUsers.add(user);
			else
				inactiveUsers.add(user);
		}
	}

	public Resolution search() {
		return UrlManager.getFindDepotResults();
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getDepotId() {
		return depotId;
	}

	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public List<KinekPoint> getDepots() throws Exception {
		List<KinekPoint> depots = new KinekPointDao().fetchLight(statusId);
		return depots;
	}

	public List<KinekPointStatus> getStatuses() throws Exception {
		return new KinekPointStatusDao().fetch();
	}
	
	public int getActiveUserCount() {
    	return activeUsers.size();
    }
    
    public int getInactiveUserCount() {
    	return inactiveUsers.size();
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
}
