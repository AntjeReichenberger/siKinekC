package org.webdev.kpoint.bl.pojo;

public class RedirectReason {
	private int reasonId;
	private String name;
	
	public RedirectReason() {
	
	}

	public RedirectReason(int reasonId, String name) {
		this.reasonId = reasonId;
		this.name = name;
	}

	public int getReasonId() {
		return reasonId;
	}

	public void setReasonId(int i) {
		this.reasonId = i;
	}

	public String getName() {
		return name;
	}

	public void setName(String s) {
		this.name = s;
	}
}