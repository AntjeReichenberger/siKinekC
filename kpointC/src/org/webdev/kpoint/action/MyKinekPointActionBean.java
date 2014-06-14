package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 * This is a placeholder action bean.  Stripes is trying to load this actionbean at runtime and throwing an error
 * so we will add the bean to see if that helps.  The application does not directly use this bean.
 * @author dawson
 *
 */
@UrlBinding("/MyKinekPoint.action")
public class MyKinekPointActionBean extends AccountDashboardActionBean {
		
	@DefaultHandler @DontValidate
	public Resolution view() throws Exception {
		return new RedirectResolution(MyKinekPointsActionBean.class); 
	}
	
}
