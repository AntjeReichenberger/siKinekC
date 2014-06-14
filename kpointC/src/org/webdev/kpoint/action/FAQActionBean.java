package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/FAQ.action")
public class FAQActionBean extends BaseActionBean {
	@DefaultHandler
    public Resolution view() {
		return UrlManager.getWordPressUrl();
    }
}
