package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/About.action")
public class AboutActionBean extends BaseActionBean {

	@Before
	public void setup() {
    	this.showHelpButton = false;		
	}
	
	@DefaultHandler @DontValidate
	public Resolution view() {
		return UrlManager.getAboutForm();
	}

}
