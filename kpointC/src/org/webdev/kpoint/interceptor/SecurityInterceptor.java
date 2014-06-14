package org.webdev.kpoint.interceptor;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

import org.webdev.kpoint.action.BaseActionBean;
import org.webdev.kpoint.action.LoginActionBean;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;

@Intercepts(LifecycleStage.HandlerResolution)
public class SecurityInterceptor implements Interceptor {
	
    /** Intercepts execution and checks that the user has appropriate permissions. */
    public Resolution intercept(ExecutionContext context) throws Exception {
        Resolution resolution = context.proceed();
        
        BaseActionBean bean = (BaseActionBean)context.getActionBean();
        
        ActionBeanContext actionBeanContext = bean.getContext();
        if (!actionBeanContext.getRequest().isSecure()) {
      		//return new ErrorResolution(HttpServletResponse.SC_FORBIDDEN);
        }
        
        if (isPermitted(bean)) {
        	return resolution;
        }
        
        if (mustInvalidateSession(bean)) {
        	return new RedirectResolution(LoginActionBean.class, "signedInElsewhere");
        }
        
        if (isLoggedIn(bean)) {
            return resolution;
        }
        else {
            return new RedirectResolution(LoginActionBean.class, "sessionExpired");
        }
    }
    
    /**
     * Determines if a user's session should be invalidated because their 
     * current session Id does not match the session Id stored in persistent storage
     * @param bean The current BaseActionBean
     * @return True if the session should be invalidated; otherwise false
     */
    private boolean mustInvalidateSession(BaseActionBean bean) throws Exception {
        User activeUser = bean.getActiveUser();
        if (activeUser == null) return false;
        
        // This should never be null. A user cannot be signed in without having an account in
        // Persistent storage.
        User savedSessionUser = new UserDao().read(activeUser.getUserId());
        
        // SessionId should never be null either, but a bug on sign up caused a null value exception
        // That bug has been fixed, but check here just in case another arises due to new code.
        // A user will get kicked to the login page rather than have the application blow up
        if ((activeUser.getSessionId() == null)) return true;
        
        if (activeUser.getSessionId().equals(savedSessionUser.getSessionId()))
        	return false;
        
        return true;
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
}
