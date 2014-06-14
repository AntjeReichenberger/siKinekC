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
	
	@SuppressWarnings("unused")
	private Locale locale;
	private static final KinekLogger logger = new KinekLogger(StateConverter.class);
	
	@Override
	public State convert(String input, Class<? extends State> arg1, Collection<ValidationError> errors){
		int stateId;
		try {
			stateId = Integer.parseInt(input);
		}
		catch (NumberFormatException ex) {
            errors.add(new ScopedLocalizableError("converter.state", "invalidId") );
			Hashtable<String,String> logValues = new Hashtable<String,String>();
			logValues.put("StateID", input);
            logger.error(new ApplicationException("Invalid state id provided", ex), logValues);
            return null;
		}
		//TODO
		State state = null;
		try{
			state = new StateDao().read(stateId);
		}
		catch(Exception ex){
			errors.add(new ScopedLocalizableError("converter.state", "invalidState") );
			return null;
		}
		
		if (state == null) {
            errors.add(new ScopedLocalizableError("converter.state", "invalidState") );
			Hashtable<String,String> logValues = new Hashtable<String,String>();
			logValues.put("StateID", input);
            logger.error("State does not exist", logValues);
			return null;			
		}
		
		return state;
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
