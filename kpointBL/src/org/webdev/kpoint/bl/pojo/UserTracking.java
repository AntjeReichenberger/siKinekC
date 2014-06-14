package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;
import java.util.Calendar;

public class UserTracking implements Serializable, Comparable<UserTracking> {

	private static final long serialVersionUID = 7161429916079107066L;
	private transient int id;
	private User user;
	private Tracking tracking;
	private String packageNickname;
	private boolean autoSelected = true;
	private transient boolean isActive = true;
	
	public String getPackageNickname() {
		return packageNickname;
	}
	public void setPackageNickname(String packageNickname) {
		this.packageNickname = packageNickname;
	}
	public void setAutoSelected(boolean autoSelected) {
		this.autoSelected = autoSelected;
	}
	public boolean getAutoSelected() {
		return autoSelected;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	public void setTracking(Tracking tracking) {
		this.tracking = tracking;
	}
	public Tracking getTracking() {
		return tracking;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean getIsActive() {
		return isActive;
	}
	@Override
	public int compareTo(UserTracking o) {
		Calendar d1 = tracking.getCreatedDate();
		Calendar d2 = o.getTracking().getCreatedDate();	
		return d2.compareTo(d1);
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
}
