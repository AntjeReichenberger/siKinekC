package org.webdev.kpoint.action;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.managers.ErrorManager;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.manager.SessionManager.SessionKey;
import org.webdev.kpoint.bl.persistence.CourierDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.persistence.RedirectReasonDao;
import org.webdev.kpoint.bl.pojo.Courier;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.RedirectReason;

@UrlBinding("/KinekNumberSearch.action")
public class KinekNumberSearchActionBean extends SecureActionBean {
	
	// Local constants
	private final String CONFIRMED_USERS_SESSION_KEY = "PICKACTION_CONFIRMEDUSERS";
	
	// Search Criteria
	private String kinekNumber;
	private String firstName;
	private String lastName;
	private String phone;

	// Attributes from entry page
	private String via;
	private String customInfo;
	private int courierId;
	private int reasonId;
	private String redirectLocation;
	
	private List<String> selectedUserIds = new ArrayList<String>();
	private List<User> users = new ArrayList<User>();
	
	@Before(on="view")
	public void viewBefore() {
		setSessionAttribute(SessionKey.KINEK_NUMBER_SEARCH_RESULTS, null);
		this.setSessionAttribute(CONFIRMED_USERS_SESSION_KEY, new ArrayList<User>());

	}
	
	@DefaultHandler
	@DontValidate
	public Resolution view() {
		return UrlManager.getKinekNumberSearchForm();
	}
	
	/**
	 * Handles validation for the searchByKinekNumber event
	 * @param errors
	 */
	@ValidationMethod(on="searchByKinekNumber")
	public void validateSearchByKinekNumber(ValidationErrors errors) throws Exception {
		// Return everyone if no Kinek number is supplied 
		if (kinekNumber == null || kinekNumber.trim().length() == 0) {
			users = new UserDao().fetchConsumers(); 
		}
		else {
			//format kinek number before sending to backend
			//remove prefixes of KINEK or #
			users = new UserDao().fetchConsumers(formatKinekNumber(kinekNumber));
		}

		if (users.size() == 0) {
			ValidationError error = new SimpleError("There were no users found that match the supplied Kinek#");  
			errors.add("kinekNumber", error);
		}
		
		// Save the results in session in case the validation fires on the next page
		setSessionAttribute(SessionKey.KINEK_NUMBER_SEARCH_RESULTS, users);
	}
	
	private String formatKinekNumber(String kinekNumber){
		String formattedKinekNumber = kinekNumber;
		String precedingText = ConfigurationManager.getKinekNumberPrecedingText();
		if (kinekNumber.toUpperCase().startsWith(precedingText, 0)) {
			formattedKinekNumber = kinekNumber.substring(5);
		}
		else if(kinekNumber.startsWith("#")){
			formattedKinekNumber = kinekNumber.substring(1);
		}
		
		return formattedKinekNumber;
	}
	
	/**
	 * Searches for users by their Kinek number. Partial matches are permitted
	 * @return
	 */
	public Resolution searchByKinekNumber() {
		return UrlManager.getKinekNumberSearchResults();
	}
	
	
	/**
	 * Handles validation for the searchByContact event
	 * @param errors
	 */
	@ValidationMethod(on="searchByContact")
	public void validateSearchByContact(ValidationErrors errors) throws Exception  {
		if (firstName == null) firstName = "";
		if (lastName == null) lastName = "";
		if (phone == null) phone = "";
		
		// Return everyone if no Kinek number is supplied 
		if (firstName.trim().length() == 0 && lastName.trim().length() == 0 && phone.trim().length() == 0) {
			users = new UserDao().fetchConsumers(); 
		}
		else {
			users = new UserDao().fetchConsumers(firstName, lastName, phone);
		}

		if (users.size() == 0) {
			ValidationError error = new SimpleError("There were no users found that match the supplied contact credentials");  
			errors.add("firstName", error);
		}
		
		// Save the results in session in case the validation fires on the next page
		setSessionAttribute(SessionKey.KINEK_NUMBER_SEARCH_RESULTS, users);
	}
	
	/**
	 * Searches for users by their contact details. Partial matches are permitted
	 * @return Resolution
	 */
	public Resolution searchByContact() {
		return UrlManager.getKinekNumberSearchResults();
	}
	

	
	@SuppressWarnings("unchecked")
	@ValidationMethod(on="processResults")
	public void processResultsValidation(ValidationErrors errors) {
		if (selectedUserIds == null || selectedUserIds.size() == 0) {
			errors.add("selectedUserIds", ErrorManager.getKinekNumberSearchNoUserSelected());
		}
		
		// If the validation fails, get the results again so they are displayed on the page...
		if (errors.size() > 0) {
			users = (List<User>)getSessionAttribute(SessionKey.KINEK_NUMBER_SEARCH_RESULTS);
		}
		// Otherwise clear the session variable
		else {
			setSessionAttribute(SessionKey.KINEK_NUMBER_SEARCH_RESULTS, null);
		}
	}
	
	/**
	 * Handles the return event. This allows the user to return to the flow that they entered from. 
	 * @return Resolution
	 */
	public Resolution processResults() {
		this.setSessionAttribute("selectedUserIds", selectedUserIds);
		this.setSessionAttribute("courierId", courierId);
		this.setSessionAttribute("customInfo", customInfo);

		// Redirect
		if (via.equalsIgnoreCase(ConfigurationManager.getViaRedirect())) {
			this.setSessionAttribute("redirectLocation", redirectLocation);
			this.setSessionAttribute("reasonId", reasonId);
			return new RedirectResolution(RedirectActionBean.class, "validateSearchResults");
		}
		// Accept Delivery
		else if (via.equalsIgnoreCase(ConfigurationManager.getViaAccept())) {
			return new RedirectResolution(DeliveryActionBean.class, "validateSearchResults");			
		}
		
		// In case of neither, return to main page
		return new RedirectResolution(DeliveryActionBean.class, "view");
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String s) {
		this.firstName = s;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String s) {
		this.lastName = s;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String s) {
		this.phone = s;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> u) {
		this.users = u;
	}
	
	public String getVia() {
		return via;
	}

	public void setVia(String s) {
		this.via = s;
	}

	public String getCustomInfo() {
		return customInfo;
	}

	public void setCustomInfo(String s) {
		this.customInfo = s;
	}

	public String getKinekNumber() {
		return kinekNumber;
	}

	public void setKinekNumber(String kinekNumber) {
		this.kinekNumber = kinekNumber;
	}

	public List<String> getSelectedUserIds() {
		return selectedUserIds;
	}

	public void setSelectedUserIds(List<String> l) {
		this.selectedUserIds = l;
	}

	public int getCourierId() {
		return courierId;
	}

	public void setCourierId(int value) {
		this.courierId = value;
	}

	public int getReasonId() {
		return reasonId;
	}

	public void setReasonId(int value) {
		this.reasonId = value;
	}
	
	public List<Courier> getCouriers() throws Exception  {
		return new CourierDao().fetch();
	}
	
	public List<RedirectReason> getRedirectReasons() throws Exception {
		return new RedirectReasonDao().fetch();
	}
	
	public String getRedirectLocation() {
		return redirectLocation;
	}
	
	public void setRedirectLocation(String value) {
		this.redirectLocation = value;
	}
}
