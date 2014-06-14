package org.webdev.kpoint.converter;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Locale;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.KinekPointStatusDao;
import org.webdev.kpoint.bl.pojo.KinekPointStatus;

public class KinekPointStatusConverter implements TypeConverter<KinekPointStatus>{
	
	private static final KinekLogger logger = new KinekLogger(KinekPointStatusConverter.class);

	@SuppressWarnings("unused")
	private Locale locale;

	@Override
	public KinekPointStatus convert(String input, Class<? extends KinekPointStatus> arg1, Collection<ValidationError> errors) {
		int id;
		try {
			id = Integer.parseInt(input);
		}
		catch (NumberFormatException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KPStatusID", input);
            logger.error(new ApplicationException("Invalid KinekPoint Status ID provided", ex), logProps);

            errors.add(new ScopedLocalizableError("converter.depotStatus", "invalidId") );
			return null;
		}
		
		KinekPointStatus status = null;
		try {
			status = new KinekPointStatusDao().read(id);
		} catch (Exception e) {
			logger.error(new ApplicationException("Invalid KinekPoint Status ID provided", e));
		}
		
		if (status == null) {
            errors.add(new ScopedLocalizableError("converter.depotStatus", "invalidDepotStatus") );
			return null;			
		}
		
		return status;
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
