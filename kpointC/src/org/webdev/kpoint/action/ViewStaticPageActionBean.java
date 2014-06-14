package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/ShowPage.action")
public class ViewStaticPageActionBean extends AccountDashboardActionBean {
	private String action;
	
	@DefaultHandler
	public Resolution view() {
		if (getActiveUser() != null) {
			setHideSearch(true);
		}
	
		if (action == null || action.trim().length() == 0) {
			return new RedirectResolution(HomeActionBean.class);
		}
		
		if (action.equalsIgnoreCase("Terms"))
			return new ForwardResolution("/WEB-INF/jsp/terms.jsp");

		if (action.equalsIgnoreCase("Marketing"))
			return new ForwardResolution("/WEB-INF/jsp/marketingmaterials.jsp");

		if (action.equalsIgnoreCase("Training"))
			return new ForwardResolution("/WEB-INF/jsp/trainingmaterials.jsp");

		if (action.equalsIgnoreCase("Privacy"))
			return new ForwardResolution("/WEB-INF/jsp/privacy.jsp");
		
		if (action.equalsIgnoreCase("GreeningYourDeliveries"))
			return new ForwardResolution("/WEB-INF/jsp/greeningcorporateresponsibility.jsp");
			
		if (action.equalsIgnoreCase("Money"))
			return new ForwardResolution("/WEB-INF/jsp/landing/money.jsp");
		
		if(action.equalsIgnoreCase("MissedDelivery"))
			return new ForwardResolution("/WEB-INF/jsp/landing/misseddelivery.jsp");
		
		if(action.equalsIgnoreCase("Calais"))
			return new ForwardResolution("/WEB-INF/jsp/landing/border/calais.jsp");
		
		if(action.equalsIgnoreCase("Thompson"))
			return new ForwardResolution("/WEB-INF/jsp/landing/thompson.jsp");

		if(action.equalsIgnoreCase("ShoppingOnline"))
			return new ForwardResolution("/WEB-INF/jsp/landing/shoppingonline.jsp");
		
		if(action.equalsIgnoreCase("SafeShopping"))
			return new ForwardResolution("/WEB-INF/jsp/landing/safeshopping.jsp");		
		
		if(action.equalsIgnoreCase("ShipToStore"))
			return new ForwardResolution("/WEB-INF/jsp/landing/shiptostore.jsp");
		
		return new RedirectResolution(HomeActionBean.class);
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
