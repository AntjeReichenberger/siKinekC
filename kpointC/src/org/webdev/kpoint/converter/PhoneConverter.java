package org.webdev.kpoint.converter;

import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.webdev.kpoint.bl.manager.RegexManager;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

/**
 * Responsible for ensuring strings are valid phone numbers in the format defined in the Phone Regex
 *
 */
public class PhoneConverter implements TypeConverter<String>{

	@SuppressWarnings("unused")
	private Locale locale;
	private static Pattern pattern = Pattern.compile(RegexManager.getPhoneRegex()); 
	
	@Override
	public String convert(String input, Class<? extends String> arg1, Collection<ValidationError> errors) {
		Matcher matcher = pattern.matcher(input);
		if (matcher.matches()) {
			return input;
		}
		else {
            errors.add(new ScopedLocalizableError("converter.phone", "invalidPhone") );
            return null;
		}
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
