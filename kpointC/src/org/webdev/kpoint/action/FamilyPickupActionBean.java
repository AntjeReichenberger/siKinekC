package org.webdev.kpoint.action;

import org.webdev.kpoint.managers.UrlManager;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/FamilyPickup.action")
public class FamilyPickupActionBean extends AccountDashboardActionBean {

	private int surrogateId;
	
	@DefaultHandler
	public Resolution view(){
		return UrlManager.getFamilyPickUp();
	}	
	
	public int getSurrogateId() {
		return surrogateId;
	}

	public void setSurrogateId(int surrogateId) {
		this.surrogateId = surrogateId;
	}


	
	public Resolution editSurrogatePage(){
		RedirectResolution resolution = new RedirectResolution(EditSurrogateActionBean.class);
		resolution.addParameter("surrogateId", surrogateId);
		
		return resolution;
	}
	
	public Resolution deleteSurrogate(){
		return new RedirectResolution(FamilyPickupActionBean.class);
	}
}
