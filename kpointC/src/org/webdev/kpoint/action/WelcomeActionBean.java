package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/Welcome.action")
public class WelcomeActionBean extends BaseActionBean {

    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution("/WEB-INF/jsp/welcome.jsp");
    }
}
