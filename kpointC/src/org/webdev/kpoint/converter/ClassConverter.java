package org.webdev.kpoint.converter;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Locale;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.webdev.kpoint.action.AccountDashboardActionBean;
import org.webdev.kpoint.action.MyProfileActionBean;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;

@SuppressWarnings("unchecked")
public class ClassConverter implements TypeConverter<Class>
{
	@SuppressWarnings("unused")
	private Locale locale;
	private static final KinekLogger logger = new KinekLogger(ClassConverter.class);
	private Class<? extends AccountDashboardActionBean> defaultActionBean = MyProfileActionBean.class;

	public Class<? extends AccountDashboardActionBean> convert(
			String input, 
			Class<? extends Class> arg1, 
			Collection<ValidationError> errors )
	{
		Class<? extends AccountDashboardActionBean> c;
		try
		{
			c = (Class<? extends AccountDashboardActionBean>) Class.forName(input);
		}
		catch (Exception ex)
		{
			errors.add(new ScopedLocalizableError("converter.class", "invalidReferrer"));
			c = defaultActionBean;
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Class Name", input);
            logger.error(new ApplicationException("Unable to create class", ex), logProps);
   		}
		return c;
	}

	@Override
	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}
}