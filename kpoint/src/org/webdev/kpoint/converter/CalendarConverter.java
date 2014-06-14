package org.webdev.kpoint.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.managers.ErrorManager;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

public class CalendarConverter implements TypeConverter<Calendar>{
	
	private static final KinekLogger logger = new KinekLogger(CalendarConverter.class);

	@SuppressWarnings("unused")
	private Locale locale;

	@Override
	public Calendar convert(String input, Class<? extends Calendar> arg1, Collection<ValidationError> errors) {
		Calendar cal = null;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			Date date = format.parse(input);
			cal = Calendar.getInstance();
			cal.setTime(date);
		}
		catch (ParseException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Date", input);
            logger.error(new ApplicationException("Invalid date provided", ex), logProps);
            
            errors.add(ErrorManager.getCalendarConverterInvalidFormat());
		}
		
		return cal;
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
