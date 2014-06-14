package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;


@UrlBinding("/Home.action")
public class HomeActionBean extends BaseActionBean {
    @DefaultHandler
    public Resolution view() {
    	
    	if (getActiveUser() == null)
    		return new RedirectResolution(LoginActionBean.class);
    	
    	if (getActiveUser().getAdminAccessCheck())
    		return new RedirectResolution(ViewKinekPointActionBean.class);
    	
    	return new RedirectResolution(DeliveryActionBean.class);
    }

    public String getJavaVersion() {
        return System.getProperty("java.version");
    }

    public String getOsName() {
        return System.getProperty("os.name");
    }
}
