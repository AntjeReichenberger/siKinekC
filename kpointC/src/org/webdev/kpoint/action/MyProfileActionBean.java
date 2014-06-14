package org.webdev.kpoint.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.persistence.CountryDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.Country;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.State;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.converter.EmailConverter;
import org.webdev.kpoint.managers.MessageManager;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.util.AddressInfoRetriever;

@UrlBinding("/MyProfile.action")
public class MyProfileActionBean extends AccountDashboardActionBean {
	@ValidateNestedProperties({
		@Validate(field="email", required=true, on="storeUser", converter=EmailConverter.class),
		@Validate(field="address1", required=true, on="storeUser"),
		@Validate(field="zip", required=true, on="storeUser")
	})
	private User user;
	
	@DefaultHandler @DontValidate
    public Resolution view() {
		user = getActiveUser();
		
		return UrlManager.getMyProfile();
    }
	
	@ValidationMethod(on="storeUser")
    public void formValidate(ValidationErrors errors) throws Exception {
		UserDao userDao = new UserDao();
		
		if (userDao.readByEmail(user.getEmail()) != null && userDao.readByEmail(user.getEmail()).getUserId() != getActiveUser().getUserId()) {
			errors.add("emailConfirm", new SimpleError("Email address already in use, please select another email"));
		}
		else if (userDao.read(user.getEmail()) != null && userDao.read(user.getEmail()).getUserId() != getActiveUser().getUserId()) {
			errors.add("emailConfirm", new SimpleError("Email address already in use, please select another email"));
		}
	}

	public Resolution storeUser() throws Exception {
		User activeUser = getActiveUser();
		activeUser.setPhone(user.getPhone());
		activeUser.setEmail(user.getEmail());
		activeUser.setUsername(user.getEmail());
		
		activeUser.setAddress1(user.getAddress1()) ;
		activeUser.setZip(user.getZip());
		
		// disregard user city.
		AddressInfoRetriever retriever = new AddressInfoRetriever(user.getZip());
		activeUser.setCity(retriever.getCity());
		activeUser.setState(new StateDao().readFromStateProvCode(retriever.getState()));

        new UserDao().update(activeUser);
        
        // Update the user in session
        setActiveUser(activeUser);
        
        //return SUCCESS_PAGE;
        this.setSuccessMessage(MessageManager.getMyAccount());
        return new RedirectResolution(MyProfileActionBean.class);
    }
	
	public Resolution returnToMyParcels() {
		return new RedirectResolution(MyParcelsActionBean.class);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public List<Country> getCountries() throws Exception {
		return new CountryDao().fetch();
	}
	
	public List<State> getStates() throws Exception {
		return new StateDao().fetch();
	}
	
	public List<KinekPoint> getDepots() throws Exception {
		return new KinekPointDao().fetch();
	}

	/**
	 * Used to determine where to return to if an action is taken that leads you from this page
	 * @return This class name
	 */
	public String getReferrer() {
		return this.getClass().getName();
	}
}
