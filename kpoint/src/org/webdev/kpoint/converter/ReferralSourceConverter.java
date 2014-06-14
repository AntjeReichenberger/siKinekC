package org.webdev.kpoint.converter;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Locale;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.ReferralSourceDao;
import org.webdev.kpoint.bl.pojo.ReferralSource;

public class ReferralSourceConverter implements TypeConverter<ReferralSource>{
	private static final KinekLogger logger = new KinekLogger(ReferralSourceConverter.class);
	
	@SuppressWarnings("unused")
	private Locale locale;

	@Override
	public ReferralSource convert(String input, Class<? extends ReferralSource> arg1, Collection<ValidationError> errors) {
		int id;
		try {
			id = Integer.parseInt(input);
		}
		catch (NumberFormatException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ReferralSource", input);
            logger.error(new ApplicationException("Invalid Referral Source ID provided", ex), logProps);

            errors.add(new ScopedLocalizableError("converter.ReferralSource", "invalidId") );
			return null;
		}
		
		ReferralSource referralSource = null;
		try {
			referralSource = new ReferralSourceDao().read(id);
		} catch (Exception e) {
			logger.error(new ApplicationException("Invalid Referral Source ID provided", e));
		}
		
		if (referralSource == null) {
            errors.add(new ScopedLocalizableError("converter.ReferralSource", "invalidReferralSource") );
			return null;
		}
		
		return referralSource;
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
