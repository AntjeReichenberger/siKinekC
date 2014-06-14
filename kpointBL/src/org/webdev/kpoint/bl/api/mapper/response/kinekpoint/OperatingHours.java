package org.webdev.kpoint.bl.api.mapper.response.kinekpoint;

import java.io.Serializable;
import java.sql.Time;

public class OperatingHours implements Serializable {
	private static final long serialVersionUID = -2096063663287679897L;
	
	private final String defaultStartTime = "9:00:00";
	private final String defaultEndTime = "17:00:00";
	
	private Time mondayStart;
	private Time mondayEnd;
	private boolean closedMonday;
	
	private Time tuesdayStart;
	private Time tuesdayEnd;
	private boolean closedTuesday;
	
	private Time wednesdayStart;
	private Time wednesdayEnd;
	private boolean closedWednesday;
	
	private Time thursdayStart;
	private Time thursdayEnd;
	private boolean closedThursday;

	private Time fridayStart;
	private Time fridayEnd;
	private boolean closedFriday;

	private Time saturdayStart;
	private Time saturdayEnd;
	private boolean closedSaturday;
	
	private Time sundayStart;
	private Time sundayEnd;
	private boolean closedSunday;
	
	private String hoursInfo;

	public OperatingHours(
			org.webdev.kpoint.bl.pojo.OperatingHours blOperatingHours) {
		mondayStart = blOperatingHours.getMondayStart();
		mondayEnd = blOperatingHours.getMondayEnd();
		closedMonday = blOperatingHours.getClosedMonday();
		tuesdayStart = blOperatingHours.getTuesdayStart();
		tuesdayEnd = blOperatingHours.getTuesdayEnd();
		closedTuesday = blOperatingHours.getClosedTuesday();
		wednesdayStart = blOperatingHours.getWednesdayStart();
		wednesdayEnd = blOperatingHours.getWednesdayEnd();
		closedWednesday = blOperatingHours.getClosedWednesday();
		thursdayStart = blOperatingHours.getThursdayStart();
		thursdayEnd = blOperatingHours.getThursdayEnd();
		closedThursday = blOperatingHours.getClosedThursday();
		fridayStart = blOperatingHours.getFridayStart();
		fridayEnd = blOperatingHours.getFridayEnd();
		closedFriday = blOperatingHours.getClosedFriday();
		saturdayStart = blOperatingHours.getSaturdayStart();
		saturdayEnd = blOperatingHours.getSaturdayEnd();
		closedSaturday = blOperatingHours.getClosedSaturday();
		sundayStart = blOperatingHours.getSundayStart();
		sundayEnd = blOperatingHours.getSundayEnd();
		closedSunday = blOperatingHours.getClosedSunday();
		hoursInfo = blOperatingHours.getHoursInfo();
	}

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

	public boolean getClosedMonday() {
		return closedMonday;
	}

	public void setClosedMonday(boolean closedMonday) {
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

	public boolean getClosedTuesday() {
		return closedTuesday;
	}

	public void setClosedTuesday(boolean closedTuesday) {
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

	public boolean getClosedWednesday() {
		return closedWednesday;
	}

	public void setClosedWednesday(boolean closedWednesday) {
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

	public boolean getClosedThursday() {
		return closedThursday;
	}

	public void setClosedThursday(boolean closedThursday) {
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

	public boolean getClosedFriday() {
		return closedFriday;
	}

	public void setClosedFriday(boolean closedFriday) {
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

	public boolean getClosedSaturday() {
		return closedSaturday;
	}

	public void setClosedSaturday(boolean closedSaturday) {
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

	public boolean getClosedSunday() {
		return closedSunday;
	}

	public void setClosedSunday(boolean closedSunday) {
		this.closedSunday = closedSunday;
	}

	public String getDefaultStartTime() {
		return defaultStartTime;
	}

	public String getDefaultEndTime() {
		return defaultEndTime;
	}
	
	public void setDefaultHours() {
		mondayStart = Time.valueOf(defaultStartTime);
		tuesdayStart = Time.valueOf(defaultStartTime);
		wednesdayStart = Time.valueOf(defaultStartTime);
		thursdayStart = Time.valueOf(defaultStartTime);
		fridayStart = Time.valueOf(defaultStartTime);
		saturdayStart = Time.valueOf(defaultStartTime);
		sundayStart = Time.valueOf(defaultStartTime);
		
		mondayEnd = Time.valueOf(defaultEndTime);
		tuesdayEnd = Time.valueOf(defaultEndTime);
		wednesdayEnd = Time.valueOf(defaultEndTime);
		thursdayEnd = Time.valueOf(defaultEndTime);
		fridayEnd = Time.valueOf(defaultEndTime);
		saturdayEnd = Time.valueOf(defaultEndTime);
		sundayEnd = Time.valueOf(defaultEndTime);
	}

	public String getHoursInfo() {
		return hoursInfo;
	}

	public void setHoursInfo(String hoursInfo) {
		this.hoursInfo = hoursInfo;
	}
	
	
}
