package org.webdev.kpoint.bl.pojo;

public class ReferralSource {
	private int id;
	private String displayName;
	private boolean displayInAdmin;
	private boolean displayInConsumer;
	private int displayIndex;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public boolean getDisplayInAdmin() {
		return displayInAdmin;
	}
	
	public void setDisplayInAdmin(boolean displayInAdmin) {
		this.displayInAdmin = displayInAdmin;
	}
	
	public boolean getDisplayInConsumer() {
		return displayInConsumer;
	}
	
	public void setDisplayInConsumer(boolean displayInConsumer) {
		this.displayInConsumer = displayInConsumer;
	}
	
	public int getDisplayIndex() {
		return displayIndex;
	}
	
	public void setDisplayIndex(int displayIndex) {
		this.displayIndex = displayIndex;
	}
}
