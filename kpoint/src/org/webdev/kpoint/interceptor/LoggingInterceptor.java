package org.webdev.kpoint.interceptor;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

import org.slf4j.MDC;
import org.webdev.kpoint.action.BaseActionBean;

@Intercepts(LifecycleStage.HandlerResolution)
public class LoggingInterceptor implements Interceptor {
	
    /** Intercepts execution and adds basic information that will always be logged */
    public Resolution intercept(ExecutionContext context) throws Exception {
        Resolution resolution = context.proceed();
        BaseActionBean bean = (BaseActionBean)context.getActionBean();
        MDC.put("RequestURI", context.getActionBeanContext().getRequest().getRequestURI());
        MDC.put("RequestQueryString", context.getActionBeanContext().getRequest().getQueryString());
        if (isLoggedIn(bean)) {
            String sessionId = bean.getActiveUser().getSessionId();
            int userid = bean.getActiveUser().getUserId();
            
            MDC.put("SessionID", sessionId);
            MDC.put("LoggedInUserID", String.valueOf(userid));
        }
        return resolution;
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
