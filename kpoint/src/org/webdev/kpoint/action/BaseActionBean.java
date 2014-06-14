package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Message;

import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.manager.SessionManager.SessionKey;
import org.webdev.kpoint.bl.pojo.User;

public class BaseActionBean implements ActionBean {
	private boolean isSecureBean = false;
	/**	If this is true a 'Help' link will be displayed on the page header; otherwise it will not */
	protected boolean showHelpButton = true;
	private String debugOutput;
	private ActionBeanContext context;
	
	public ActionBeanContext getContext() {
		return context;
	}
	
	public void setContext(ActionBeanContext context) {
		this.context = context;
	}
	
	/**
	 * If an exception is caught by the KinekExceptionHandler and the 
	 * application is in debug mode, we need a method of setting the exception 
	 * details so that they can be displayed to the user. 
	 * @param debugOutput The debug output that will be displayed on the error page.
	 */
	public void setDebugOutput(String debugOutput) {
		this.debugOutput = debugOutput;
	}

	/**
	 * If an exception is caught by the KinekExceptionHandler and the 
	 * application is in debug mode, we need a method of getting the exception 
	 * details so that they can be displayed to the user. 
	 * @return The debug output that will be displayed on the error page.
	 */
	public String getDebugOutput() {
		if (ExternalSettingsManager.getIsInDebugMode()) {
			return debugOutput;
		}
		return "";
	}

	public User getActiveUser() {
		return (User)getContext().getRequest().getSession().getAttribute("activeUser");
	}

	protected void clearSessionAttribute(SessionKey key) {
		clearSessionAttribute(key.name());
	}
	
	protected void clearSessionAttribute(String key) {
		this.getContext().getRequest().getSession().removeAttribute(key);
	}
	
	/**
	 * Sets the supplied object in session data. It can be later retrieved using the key.
	 * @param key The key indicating which session object to store 
	 * @param value The object to store
	 */
	protected void setSessionAttribute(SessionKey key, Object value) {
		setSessionAttribute(key.name(), value);
	}

	/**
	 * Sets the supplied object in session data. It can be later retrieved using the key.
	 * @param key The key indicating which session object to store 
	 * @param value The object to store
	 */
	protected void setSessionAttribute(String key, Object value) {
		this.getContext().getRequest().getSession().setAttribute(key, value);
	}
	
	/**
	 * Retrieves the object associated with the supplied key
	 * @param key Used to identify the object to retrieve
	 * @return The object referenced by the supplied key 
	 */
	protected Object getSessionAttribute(SessionKey key) {
		return getSessionAttribute(key.name());
	}

	/**
	 * Retrieves the object associated with the supplied key
	 * @param key Used to identify the object to retrieve
	 * @return The object referenced by the supplied key 
	 */
	protected Object getSessionAttribute(String key) {
		return this.getContext().getRequest().getSession().getAttribute(key);
	}
	
	protected void setSuccessMessage(Message m) {
		getContext().getMessages().add(m);
	}
	
	/**
	 * Used in determining if the page is secure
	 * @return
	 */
	public boolean isSecure() {
		return isSecureBean;
	}

	public boolean isShowHelpButton() {
		return showHelpButton;
	}

	public void setShowHelpButton(boolean showHelpButton) {
		this.showHelpButton = showHelpButton;
	}
	
	/**
	 * Returns the base URL of the admin portal. This is mainly for use on front-end JSP pages.
	 * It does not include a trailing "/"
	 * @return the base URL of the admin portal
	 */
	public String getBaseAdminPortalUrl() {
		return ExternalSettingsManager.getAdminPortalBaseUrl();
	}
	
	/**
	 * Returns the base URL of the consumer portal. This is mainly for use on front-end JSP pages.
	 * It does not include a trailing "/"
	 * @return the base URL of the consumer portal
	 */
	public String getBaseConsumerPortalUrl() {
		return ExternalSettingsManager.getConsumerPortalBaseUrl();
	}
	
	/**
	 * Returns the base URL of the Depot portal. This is mainly for use on front-end JSP pages.
	 * It does not include a trailing "/"
	 * @return the base URL of the Depot portal
	 */
	public String getBaseDepotPortalUrl() {
		return ExternalSettingsManager.getDepotPortalBaseUrl();
	}

}
