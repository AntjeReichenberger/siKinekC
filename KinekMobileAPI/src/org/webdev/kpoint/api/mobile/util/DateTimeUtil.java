package org.webdev.kpoint.api.mobile.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

	//return as "yyyy-MM-dd HH:mm:ss"
	public static String convertCalendarToString(Calendar calendar){
		if(calendar != null) {
			 long milisecond=calendar.getTimeInMillis();
			 return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(milisecond));
		}
		return "";
	}
	
	//return as "yyyy-MM-dd HH:mm:ss"
	public static String convertDateToString(Date date){
		 long milisecond=date.getTime();
		 return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(milisecond));
	}	
	
	//return as "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
	public static Calendar convertStringToCalendar(String calendarStr){
		DateFormat formatter=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Calendar cal=null;
		try {
			Date date=(Date)formatter.parse(calendarStr);
			cal=Calendar.getInstance();		
			cal.setTimeInMillis(date.getTime());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cal;		
	}
	
	//return as "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
	public static Date convertStringToDate(String dateStr){
		DateFormat formatter=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date date=null;
		try {
			date=(Date)formatter.parse(dateStr);			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;		
	}
	
}
