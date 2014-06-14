package org.webdev.kpoint.interceptor;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.controller.StripesRequestWrapper;

@Intercepts(LifecycleStage.BindingAndValidation)
public class XssInterceptor implements Interceptor
{
	private static ThreadLocal<ExecutionContext> currentContext = new ThreadLocal<ExecutionContext>();
	
	public Resolution intercept(ExecutionContext context) throws Exception 
	{
        StripesRequestWrapper stripesWrapper = null;
        HttpServletRequest originalRequest = null;
        try {
            currentContext.set(context);
            stripesWrapper = StripesRequestWrapper.findStripesWrapper(context
                    .getActionBeanContext().getRequest());
            originalRequest = (HttpServletRequest) stripesWrapper.getRequest();
            stripesWrapper.setRequest(new XssRequestWrapper(originalRequest));
            return context.proceed();
        } finally {
            currentContext.remove();
            if (stripesWrapper != null && originalRequest != null)
                stripesWrapper.setRequest(originalRequest);
        }
    }
}