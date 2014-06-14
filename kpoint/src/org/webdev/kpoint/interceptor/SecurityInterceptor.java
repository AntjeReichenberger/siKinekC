package org.webdev.kpoint.interceptor;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

import org.webdev.kpoint.action.AgreeToTermsActionBean;
import org.webdev.kpoint.action.BaseActionBean;
import org.webdev.kpoint.action.LoginActionBean;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.Role;

@Intercepts(LifecycleStage.HandlerResolution)
public class SecurityInterceptor implements Interceptor {
	
    /** Intercepts execution and checks that the user has appropriate permissions. */
    public Resolution intercept(ExecutionContext context) throws Exception {
        Resolution resolution = context.proceed();
        
        BaseActionBean bean = (BaseActionBean)context.getActionBean();
        
        if (isPermitted(bean)) return resolution;
        
        if (mustAgreeToTOS(bean)) return new RedirectResolution(AgreeToTermsActionBean.class, "view");

        if (isLoggedIn(bean)) {
            return resolution;
        }
        else {
            return new RedirectResolution(LoginActionBean.class, "sessionExpired");
        }
    }
    
    /**
     * Determines if the current action bean is a secure action bean or not
     * @param bean
     * @return
     */
    protected boolean isPermitted(BaseActionBean bean) {
    	return !bean.isSecure();
    }

    /**
     * Determines if the user is logged in
     * @param bean
     * @return True if the user is logged in; otherwise false
     */
    protected boolean isLoggedIn(BaseActionBean bean) {
   		return bean.getActiveUser() != null;
    }
    
    /**
     * Determines if the current user must agree to the site TOS before continuing
     * @param bean The current action bean to test against. Only secure pages require agreeing to TOS.
     * @param user The user to test
     * @return True if the user must agree to the TOS; otherwise false
     */
    protected boolean mustAgreeToTOS(BaseActionBean bean) {
    	if (bean.getClass() == AgreeToTermsActionBean.class) return false;
    	
    	User user = bean.getActiveUser();
    	
    	if (user == null) return false;
    	
    	if(user.getRoleId() == Role.DepotAdmin 
    			&& !user.getAgreedToTOS())
    		return true;
    	
    	return false;
    }
}
