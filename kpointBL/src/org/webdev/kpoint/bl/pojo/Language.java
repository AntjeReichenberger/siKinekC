package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;

public class Language implements Serializable {

	private static final long serialVersionUID = -7230527446115673890L;
	
	private int languageId;
	private String name;
	
	public int getLanguageId() {
		return languageId;
	}
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
