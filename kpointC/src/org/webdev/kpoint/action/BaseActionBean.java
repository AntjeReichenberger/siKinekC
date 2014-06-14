package org.webdev.kpoint.action;

import java.util.HashMap;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Message;

import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.manager.SessionManager.SessionKey;
import org.webdev.kpoint.bl.pojo.User;

public class BaseActionBean implements ActionBean {
	private ActionBeanContext context;
	private String debugOutput;
	private boolean hideSearch = false;
	
	public ActionBeanContext getContext() {
		return context;
	}
	
	public void setContext(ActionBeanContext context) {
		this.context = context;
	}

	public User getActiveUser() {
		return (User)getSessionAttribute(SessionKey.ACTIVE_USER);
	}
	
	public void setActiveUser(User user) {
		user.setApp(User.App.CONSUMER_PORTAL);
		setSessionAttribute(SessionKey.ACTIVE_USER, user);
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

	public boolean getSignedIn() {
		return getActiveUser() != null;
	}
	
	public boolean getAddressInfoComplete() {
		if (getSignedIn()) {
			return getActiveUser().getAddress1() != null && getActiveUser().getAddress1() != "";
		}
		else {
			return false;
		}
	}

	/**
	 * Used to determine whether the "Find a Depot" search box is displayed
	 * @return true if the search box should be hidden; false otherwise 
	 */
	public boolean getHideSearch() {
		return hideSearch;
	}
	
	public void setHideSearch(boolean hideSearch) {
		this.hideSearch = hideSearch;
	}
	
	/**
	 * If false, base what tabs are shown off of whether or not there is a logged in user, if true always show all tabs
	 * @return true if all tabs should be shown; false otherwise 
	 */
	public boolean getShowAllTabs() {
		boolean showAllTabs = false;
		if (getSessionAttribute("showAllTabs") != null)
			showAllTabs = Boolean.parseBoolean(getSessionAttribute("showAllTabs").toString());
		return showAllTabs;
	}
	
	public void setShowAllTabs(boolean showAllTabs) {
		setSessionAttribute("showAllTabs", showAllTabs);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> getHotLocations() {
		return (HashMap) ConfigurationManager.getHotLocations();
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
	protected void setSessionAttribute(String key, Object value)
	{
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
	protected Object getSessionAttribute(String key)
	{
		return this.getContext().getRequest().getSession().getAttribute(key);
	}
	
	/**
	 * Sets the success message that is displayed on the page following a redirect 
	 * @param message The success message that is displayed on the page following a redirect
	 */
	protected void setSuccessMessage(Message message) {
		getContext().getMessages().add(message);
	}
	
	/**
	 * Used in determining if the page is secure
	 * @return
	 */
	public boolean isSecure() {
		return false;
	}
}
