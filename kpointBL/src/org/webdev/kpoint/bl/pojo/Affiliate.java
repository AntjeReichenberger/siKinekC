package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;

public class Affiliate implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4588702268681194287L;
	
	private int id;
	private String name;
	private String url;
	private boolean display;
	
	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean getDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	
}
