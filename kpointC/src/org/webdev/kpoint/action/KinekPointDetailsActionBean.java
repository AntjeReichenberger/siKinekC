package org.webdev.kpoint.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KPExtendedStorageRate;
import org.webdev.kpoint.bl.pojo.KPSkidRate;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/KinekPointDetails.action")
public class KinekPointDetailsActionBean extends AccountDashboardActionBean {
		
	private int depotId;
	private KinekPoint depot;
	private String userAddress;
	private User user;
	private boolean hasExtendedRates = false;
	private boolean hasPackageRates = false;
	private KPSkidRate kpSkidRate = null;
	
	@ValidationMethod(on="view")
	public void validateUserHasKinekPoint(ValidationErrors errors){
		UserDao userDao = new UserDao();
		List<KinekPoint> myKps = new ArrayList<KinekPoint>(getActiveUser().getKinekPoints());				
		boolean foundKp = false;
		for(KinekPoint myKp : myKps){
			if(myKp.getDepotId() == depotId){
				depot = myKp;
				foundKp = true;
				break;
			}
		}	
		if(!foundKp){
			errors.add("depotId", new SimpleError("Invalid KinekPoint"));
		}	
	}	
	
	@DefaultHandler
	public Resolution view() {	
		depot.filterExtendedRates();
		for(KPExtendedStorageRate storage: depot.getKpExtendedStorageRates()){
			if (storage.getActualFee().compareTo(BigDecimal.ZERO) != 0){
				setHasExtendedRates(true);
				break;
			}
		}	
		if (depot.getKpPackageRates().size() > 0){
			setHasPackageRates(true);
		}
		
		user = getActiveUser();
		if (user.getState() != null) {
			userAddress = user.getAddress1() + ", " + user.getCity() + ", " + user.getState().getName() + ", " + user.getState().getCountry().getName();
		}
		else {
			userAddress = user.getAddress1() + ", " + user.getCity();
		}
				
		List<KPSkidRate> kpSkidRates = new ArrayList<KPSkidRate>(depot.getKpSkidRate());
		if(kpSkidRates.size() > 0)
			setKpSkidRate(kpSkidRates.get(0));
		
		return UrlManager.getKinekPointDetails();
	}

	public void setDepotId(int depotId){
		this.depotId = depotId;
	}
	public int getDepotId(){
		return depotId;
	}

	public void setDepot(KinekPoint depot) {
		this.depot = depot;
	}

	public KinekPoint getDepot() {
		return depot;
	}

	public void setHasExtendedRates(boolean hasExtendedRates) {
		this.hasExtendedRates = hasExtendedRates;
	}

	public boolean isHasExtendedRates() {
		return hasExtendedRates;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
	
	public boolean getHasHours(){
		if(depot.getOperatingHours().getHoursInfo() == null || depot.getOperatingHours().getHoursInfo().equals("")){
			return false;
		}
		return true;
	}

	public void setHasPackageRates(boolean hasPackageRates) {
		this.hasPackageRates = hasPackageRates;
	}

	public boolean getHasPackageRates() {
		return hasPackageRates;
	}

	public void setKpSkidRate(KPSkidRate kpSkidRate) {
		this.kpSkidRate = kpSkidRate;
	}

	public KPSkidRate getKpSkidRate() {
		return kpSkidRate;
	}
}
