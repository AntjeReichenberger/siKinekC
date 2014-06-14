package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;
import java.util.Calendar;

public class KinekPointHistory implements Serializable {
	private static final long serialVersionUID = -2096063663287679894L;
	private int historyId = -1;
	private int depotId;
	private String name;
	private Calendar changedDate;
	private String typeOfChange;
	private String changesMade;
	
	public int getHistoryId() {
		return historyId;
	}
	
	public void setHistoryId(int historyId) {
		this.historyId = historyId;
	}
	
	public int getDepotId() {
		return depotId;
	}
	
	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Calendar getChangedDate() {
		return changedDate;
	}
	
	public void setChangedDate(Calendar changedDate) {
		this.changedDate = changedDate;
	}

	public String getTypeOfChange() {
		return typeOfChange;
	}
	
	public void setTypeOfChange(String typeOfChange) {
		this.typeOfChange = typeOfChange;
	}
	
	public String getChangesMade() {
		return changesMade;
	}
	
	public void setChangesMade(String changesMade) {
		this.changesMade = changesMade;
	}
}
