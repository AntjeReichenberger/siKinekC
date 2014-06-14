package org.webdev.kpoint.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.bl.pojo.KinekPoint;


@UrlBinding("/AccountDashboard.action")
public class AccountDashboardActionBean extends SecureActionBean {
	
	@DefaultHandler @DontValidate
	public Resolution defaultResolution() {
		return new RedirectResolution(MyParcelsActionBean.class);
	}
	
	
	public List<String> getSearchDefault() {
		List<String> searchDefault = new ArrayList<String>();
		searchDefault.add("Test");
		return searchDefault;
	}
	
	public String getKinekNumber() {
		return getActiveUser().getKinekNumber();
	}
	
	public String getConsumerFullName() {
		return getActiveUser().getFullName();
	}
	
	public String getConsumerCell() {
		return getActiveUser().getCellPhone();
	}
	
	public String getConsumerEmail() {
		return getActiveUser().getEmail();
	}
	
	public KinekPoint getKinekPoint() {
		return getActiveUser().getDepot();
	}
	
	public Set<KinekPoint> getMyKinekPoints() {
		return getActiveUser().getKinekPoints();
	}
	
	public Boolean getDefaultDepotSelected() {
		return getActiveUser().getDepot().getDepotId() != 1;
	}
	
	/*public Boolean getAddressInfoComplete() {
		return getActiveUser().getAddress1() != null && getActiveUser().getAddress1() != "";
	}*/
	
	@Override
	public boolean getHideSearch() {
		return true;
	}
}
