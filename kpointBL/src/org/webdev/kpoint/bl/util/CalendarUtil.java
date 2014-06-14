package org.webdev.kpoint.bl.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarUtil {
	public enum AM_PM {AM, PM};
	
	private Calendar calendar;
	
	public CalendarUtil(Calendar calendar)
	{
		this.calendar = calendar;
	}
	
	public Calendar getStartOfDay() {
		Calendar inclusiveStartDate = (Calendar)calendar.clone();
		inclusiveStartDate.set(Calendar.HOUR, 0);
		inclusiveStartDate.set(Calendar.MINUTE, 0);
		inclusiveStartDate.set(Calendar.SECOND, 0);
		inclusiveStartDate.set(Calendar.AM_PM, Calendar.AM);
		return inclusiveStartDate;
	}
	
	public Calendar getEndOfDay() {
		Calendar inclusiveEndDate = (Calendar)calendar.clone();
		inclusiveEndDate.set(Calendar.HOUR, 11);
		inclusiveEndDate.set(Calendar.MINUTE, 59);
		inclusiveEndDate.set(Calendar.SECOND, 59);
		inclusiveEndDate.set(Calendar.AM_PM, Calendar.PM);
		return inclusiveEndDate;
	}
	
	public Calendar setTime(int hour, int minute, int second, AM_PM am_pm) {
		Calendar newCalendar = (Calendar)calendar.clone();
		newCalendar.set(Calendar.HOUR, hour);
		newCalendar.set(Calendar.MINUTE, minute);
		newCalendar.set(Calendar.SECOND, second);
		if (am_pm == AM_PM.AM)
			newCalendar.set(Calendar.AM_PM, Calendar.AM);
		else
			newCalendar.set(Calendar.AM_PM, Calendar.PM);
		return newCalendar;
	}
}
