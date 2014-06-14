package org.webdev.kpoint.bl.api.mapper.response.kinekpoint;

import java.util.Date;

public class Organization {

	private String name;
	private String description;
	private Date createdDate;
	
	public Organization(org.webdev.kpoint.bl.pojo.Organization blOrganization) {
		name = blOrganization.getName();
		description = blOrganization.getDescription();
		createdDate = blOrganization.getCreatedDate();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}
}
