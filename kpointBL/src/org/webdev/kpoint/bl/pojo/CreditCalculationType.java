package org.webdev.kpoint.bl.pojo;

public class CreditCalculationType {
	private int id;
	private String name;
	
	public CreditCalculationType(int id) {
		this.id = id;
	}
	
	public CreditCalculationType() {

	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
}

