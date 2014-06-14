package org.webdev.kpoint.converter;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Locale;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.AssociationDao;
import org.webdev.kpoint.bl.pojo.Association;

public class AssociationConverter implements TypeConverter<Association>{
	
	private static final KinekLogger logger = new KinekLogger(AssociationConverter.class);

	@SuppressWarnings("unused")
	private Locale locale;

	@Override
	public Association convert(String input, Class<? extends Association> arg1, Collection<ValidationError> errors) {
		int id;
		try {
			id = Integer.parseInt(input);
		}
		catch (NumberFormatException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Input", input);
            logger.error(new ApplicationException("Association ID is not valid", ex), logProps);
        
            errors.add(new ScopedLocalizableError("converter.Association", "invalidId") );
			return null;
		}
		
		Association size = null;
		try {
			size = new AssociationDao().read(id);
		} catch (Exception e) {
			 logger.error(new ApplicationException("Association size is not valid", e));
		}
		
		if (size == null) {
            errors.add(new ScopedLocalizableError("converter.Association", "invalidAssociation") );
			return null;			
		}
		
		return size;
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
