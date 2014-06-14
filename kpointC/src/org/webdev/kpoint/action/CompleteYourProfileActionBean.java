package org.webdev.kpoint.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.manager.NotificationManager;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.Notification;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.util.AddressInfoRetriever;

@UrlBinding("/CompleteYourProfile.action")
public class CompleteYourProfileActionBean extends SecureActionBean {
	@ValidateNestedProperties({
		@Validate(field="firstName", required=true, minlength=2, on="storeUser"),
		@Validate(field="lastName", required=true, minlength=2, on="storeUser"),
		@Validate(field="address1", required=true, on="storeUser"),
		@Validate(field="zip", required=true, on="storeUser"),
		@Validate(field="agreedToTOS", required=true, on="storeUser")
	})
	private User user;
	
	@DontValidate @DefaultHandler
    public Resolution view() {
		user = getActiveUser();
		return UrlManager.getCompleteYourProfile();
	}

	@ValidationMethod(on="storeUser")
    public void formValidate(ValidationErrors errors) throws Exception {
	
	}

	public Resolution storeUser() throws Exception {
		User activeUser = getActiveUser();
		activeUser.setFirstName(user.getFirstName());
		activeUser.setLastName(user.getLastName());
		activeUser.setAddress1(user.getAddress1());
		activeUser.setZip(user.getZip());
		
		AddressInfoRetriever retriever = new AddressInfoRetriever(user.getZip());
		activeUser.setCity(retriever.getCity());
		activeUser.setState(new StateDao().readFromStateProvCode(retriever.getState()));
		
		List<Notification> currentNotifications = new ArrayList<Notification>(activeUser.getNotifications());
		
		//user also receive tracking emails by default
		currentNotifications = NotificationManager.mergeNotification(currentNotifications, true, ExternalSettingsManager.getTrackingEmailNotification());
		activeUser.setNotifications(new HashSet<Notification>(currentNotifications));	
		activeUser.setAgreedToTOS(user.getAgreedToTOS());
		
        new UserDao().update(activeUser);
        // Update the user in session
        setActiveUser(activeUser);

        return new RedirectResolution(ChooseAKinekPointActionBean.class);
    }
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}