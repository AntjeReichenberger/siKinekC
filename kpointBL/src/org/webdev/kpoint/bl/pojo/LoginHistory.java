package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;
import java.util.Calendar;

public class LoginHistory implements Serializable {
	private static final long serialVersionUID = -2096063663287679894L;
	private int id; //internal id used by hibernate
	private int userId;
	private String application;
	private Calendar loginDate;
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getUserId() {
		return userId;
	}
	
	public void setApplication(String application) {
		this.application = application;
	}
	public String getApplication() {
		return application;
	}
	
	public Calendar getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Calendar loginDate) {
		this.loginDate = loginDate;
	}
}
