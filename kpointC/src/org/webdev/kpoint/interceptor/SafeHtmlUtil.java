package org.webdev.kpoint.interceptor;

import java.util.Hashtable;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.util.ConfigurationReader;

public class SafeHtmlUtil
{
	private Policy policy;
	private static final KinekLogger logger = new KinekLogger(SafeHtmlUtil.class);
	
	public SafeHtmlUtil()
	{
		initilizePolicyFile();
	}
	
	private void initilizePolicyFile() {
		try
		{
			policy = Policy.getInstance(ConfigurationReader.getInputStream("antisamy-1.3.xml"));
		}
		catch (PolicyException ex)
		{
			logger.error(new ApplicationException("Could not load Antisamy policy file.  Input will not be sanitized.", ex));
		}
	}

	public String sanitize(String raw)
	{
		if (raw==null || raw.length()==0)
			return raw;
		
		String sanitized;
		try
		{
			sanitized = htmlEntityEncode(raw); 
		}
		catch (Exception ex)
		{
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("RawData", raw);
            logger.error(new ApplicationException("Data could not be sanitized, empty string will be used.", ex), logProps);
			sanitized = "";
		}
		return sanitized;
	}


	public String htmlEntityEncode(String input) throws ScanException, PolicyException
	{
		AntiSamy as = new AntiSamy();
		CleanResults cr = as.scan(input, policy);
		return cr.getCleanHTML();
	}
}
