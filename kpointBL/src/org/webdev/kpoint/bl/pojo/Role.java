package org.webdev.kpoint.bl.pojo;

/**
 * @author jamie
 * This class contains the definitions of the various roles that a user can have
 * This class must be kept in sync with roleId conatined in the KPRole table of the database. 
 */
public class Role {
	public static final int Consumer = 1;
	public static final int DepotAdmin = 2;
	public static final int DepotStaff = 3;
	public static final int KinekAdmin = 4;
	public static final int ReportAdmin = 5;
	
	private int roleId = -1;
	private String name;
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
