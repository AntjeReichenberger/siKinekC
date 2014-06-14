package org.webdev.kpoint.converter;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Locale;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.pojo.State;

public class StateConverter implements TypeConverter<State>{
	
	private static final KinekLogger logger = new KinekLogger(StateConverter.class);

	@SuppressWarnings("unused")
	private Locale locale;

	@Override
	public State convert(String input, Class<? extends State> arg1, Collection<ValidationError> errors) {
		int stateId;
		try {
			stateId = Integer.parseInt(input);
		}
		catch (NumberFormatException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("StateID", input);
            logger.error(new ApplicationException("Invalid State ID provided", ex), logProps);

            errors.add(new ScopedLocalizableError("converter.state", "invalidId") );
			return null;
		}
		
		State state = null;
		try {
			state = new StateDao().read(stateId);
		} catch (Exception e) {
			logger.error(new ApplicationException("Invalid State ID provided", e));
		}
		
		if (state == null) {
            errors.add(new ScopedLocalizableError("converter.state", "invalidState") );
			return null;			
		}
		
		return state;
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
