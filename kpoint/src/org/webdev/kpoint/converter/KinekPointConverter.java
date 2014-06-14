package org.webdev.kpoint.converter;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Locale;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;

public class KinekPointConverter implements TypeConverter<KinekPoint>{
	
	private static final KinekLogger logger = new KinekLogger(KinekPointConverter.class);

	@SuppressWarnings("unused")
	private Locale locale;

	@Override
	public KinekPoint convert(String input, Class<? extends KinekPoint> arg1, Collection<ValidationError> errors) {
		int depotId;
		try {
			depotId = Integer.parseInt(input);
		}
		catch (NumberFormatException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KinekPointID", input);
            logger.error(new ApplicationException("Invalid KinekPoint ID provided", ex), logProps);

            errors.add(new ScopedLocalizableError("converter.depot", "invalidId") );
			return null;
		}
		
		KinekPoint depot = null;
		try {
			depot = new KinekPointDao().read(depotId);
		} catch (Exception e) {
			logger.error(new ApplicationException("An error occured while reading a KinekPoint ID", e));
		}
		
		if (depot == null) {
            errors.add(new ScopedLocalizableError("converter.depot", "invalidDepot") );
			return null;			
		}
		
		return depot;
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
