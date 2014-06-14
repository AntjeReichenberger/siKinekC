package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.bl.pojo.User;

@UrlBinding("/Dashboard.action")
public class DashboardActionBean extends AuthenticationActionBean {
	private User user;
	
	@DefaultHandler
	public Resolution view() {
		user = getActiveUser();
		return determineDashboardPage(user);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
