package org.webdev.kpoint.bl.api.mapper.request.kinekpoint;

import java.io.Serializable;
import java.sql.Time;

public class OperatingHours implements Serializable {
	private static final long serialVersionUID = -2096063663287679897L;
	
	private final String defaultStartTime = "9:00:00";
	private final String defaultEndTime = "17:00:00";
	
	private Time mondayStart;
	private Time mondayEnd;
	private String closedMonday;
	
	private Time tuesdayStart;
	private Time tuesdayEnd;
	private String closedTuesday;
	
	private Time wednesdayStart;
	private Time wednesdayEnd;
	private String closedWednesday;
	
	private Time thursdayStart;
	private Time thursdayEnd;
	private String closedThursday;

	private Time fridayStart;
	private Time fridayEnd;
	private String closedFriday;

	private Time saturdayStart;
	private Time saturdayEnd;
	private String closedSaturday;
	
	private Time sundayStart;
	private Time sundayEnd;
	private String closedSunday;
	
	private String hoursInfo;

	public Time getMondayStart() {
		return mondayStart;
	}

	public void setMondayStart(Time mondayStart) {
		this.mondayStart = mondayStart;
	}

	public Time getMondayEnd() {
		return mondayEnd;
	}

	public void setMondayEnd(Time mondayEnd) {
		this.mondayEnd = mondayEnd;
	}

	public String getClosedMonday() {
		return closedMonday;
	}

	public void setClosedMonday(String closedMonday) {
		this.closedMonday = closedMonday;
	}

	public Time getTuesdayStart() {
		return tuesdayStart;
	}

	public void setTuesdayStart(Time tuesdayStart) {
		this.tuesdayStart = tuesdayStart;
	}

	public Time getTuesdayEnd() {
		return tuesdayEnd;
	}

	public void setTuesdayEnd(Time tuesdayEnd) {
		this.tuesdayEnd = tuesdayEnd;
	}

	public String getClosedTuesday() {
		return closedTuesday;
	}

	public void setClosedTuesday(String closedTuesday) {
		this.closedTuesday = closedTuesday;
	}

	public Time getWednesdayStart() {
		return wednesdayStart;
	}

	public void setWednesdayStart(Time wednesdayStart) {
		this.wednesdayStart = wednesdayStart;
	}

	public Time getWednesdayEnd() {
		return wednesdayEnd;
	}

	public void setWednesdayEnd(Time wednesdayEnd) {
		this.wednesdayEnd = wednesdayEnd;
	}

	public String getClosedWednesday() {
		return closedWednesday;
	}

	public void setClosedWednesday(String closedWednesday) {
		this.closedWednesday = closedWednesday;
	}

	public Time getThursdayStart() {
		return thursdayStart;
	}

	public void setThursdayStart(Time thursdayStart) {
		this.thursdayStart = thursdayStart;
	}

	public Time getThursdayEnd() {
		return thursdayEnd;
	}

	public void setThursdayEnd(Time thursdayEnd) {
		this.thursdayEnd = thursdayEnd;
	}

	public String getClosedThursday() {
		return closedThursday;
	}

	public void setClosedThursday(String closedThursday) {
		this.closedThursday = closedThursday;
	}

	public Time getFridayStart() {
		return fridayStart;
	}

	public void setFridayStart(Time fridayStart) {
		this.fridayStart = fridayStart;
	}

	public Time getFridayEnd() {
		return fridayEnd;
	}

	public void setFridayEnd(Time fridayEnd) {
		this.fridayEnd = fridayEnd;
	}

	public String getClosedFriday() {
		return closedFriday;
	}

	public void setClosedFriday(String closedFriday) {
		this.closedFriday = closedFriday;
	}

	public Time getSaturdayStart() {
		return saturdayStart;
	}

	public void setSaturdayStart(Time saturdayStart) {
		this.saturdayStart = saturdayStart;
	}

	public Time getSaturdayEnd() {
		return saturdayEnd;
	}

	public void setSaturdayEnd(Time saturdayEnd) {
		this.saturdayEnd = saturdayEnd;
	}

	public String getClosedSaturday() {
		return closedSaturday;
	}

	public void setClosedSaturday(String closedSaturday) {
		this.closedSaturday = closedSaturday;
	}

	public Time getSundayStart() {
		return sundayStart;
	}

	public void setSundayStart(Time sundayStart) {
		this.sundayStart = sundayStart;
	}

	public Time getSundayEnd() {
		return sundayEnd;
	}

	public void setSundayEnd(Time sundayEnd) {
		this.sundayEnd = sundayEnd;
	}

	public String getClosedSunday() {
		return closedSunday;
	}

	public void setClosedSunday(String closedSunday) {
		this.closedSunday = closedSunday;
	}

	public String getDefaultStartTime() {
		return defaultStartTime;
	}

	public String getDefaultEndTime() {
		return defaultEndTime;
	}
	
	public String getHoursInfo() {
		return hoursInfo;
	}

	public void setHoursInfo(String hoursInfo) {
		this.hoursInfo = hoursInfo;
	}
	
	
}
