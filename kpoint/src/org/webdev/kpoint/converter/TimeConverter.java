package org.webdev.kpoint.converter;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ConfigurationManager;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

public class TimeConverter implements TypeConverter<Time> {
	
	private static final KinekLogger logger = new KinekLogger(TimeConverter.class);

	private Locale locale;
	
	@Override
	public Time convert(String input, Class<? extends Time> arg1, Collection<ValidationError> errors) {
		try {
			DateFormat format = new SimpleDateFormat(ConfigurationManager.getTimeFormat(), locale);
			Date date = format.parse(input);
			return new Time(date.getTime()); 
		}
		catch (ParseException e) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Date", input);
            logger.error(new ApplicationException("Invalid Date provided", e), logProps);

            errors.add(new ScopedLocalizableError("converter.time", "invalidTime") );
			return null;
		}
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
