package org.webdev.kpoint.converter;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Locale;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.SizeAllowanceDao;
import org.webdev.kpoint.bl.pojo.SizeAllowance;

public class SizeAllowanceConverter implements TypeConverter<SizeAllowance>{
	
	private static final KinekLogger logger = new KinekLogger(SizeAllowanceConverter.class);

	@SuppressWarnings("unused")
	private Locale locale;

	@Override
	public SizeAllowance convert(String input, Class<? extends SizeAllowance> arg1, Collection<ValidationError> errors) {
		int id;
		try {
			id = Integer.parseInt(input);
		}
		catch (NumberFormatException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("SizeAllowance", input);
            logger.error(new ApplicationException("Invalid Size Allowance ID provided", ex), logProps);

            errors.add(new ScopedLocalizableError("converter.sizeAllowance", "invalidId") );
			return null;
		}
		
		SizeAllowance size = null;
		try {
			size = new SizeAllowanceDao().read(id);
		} catch (Exception e) {
			logger.error(new ApplicationException("Invalid Size Allowance ID provided", e));
		}
		
		if (size == null) {
            errors.add(new ScopedLocalizableError("converter.sizeAllowance", "invalidSizeAllowance") );
			return null;			
		}
		
		return size;
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
