package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.bl.persistence.LandingPageDao;
import org.webdev.kpoint.bl.pojo.LandingPage;

@UrlBinding("/Landing.action")
public class LandingPageActionBean extends BaseActionBean {
	
	private String page;
	LandingPage landingPage = new LandingPage();
	
	@DefaultHandler
	public Resolution view() throws Exception{
		if (getPage() != null && !getPage().trim().equals("") && getPage().equalsIgnoreCase("kinekPoint")) {
			return new ForwardResolution("/WEB-INF/jsp/landing/urban/default.jsp");
		}
		else if (getPage() != null && !getPage().trim().equals("") && getPage().equalsIgnoreCase("border")) {
			return new ForwardResolution("/WEB-INF/jsp/landing/border/default.jsp");
		}
		else if (getPage() != null && !getPage().trim().equals("") && getPage().equalsIgnoreCase("stp")) {
			return new ForwardResolution("/WEB-INF/jsp/landing/border/defaultstp.jsp");
		}
		else if (getPage() != null && !getPage().trim().equals("") && getPage().equalsIgnoreCase("military")) {
			return new ForwardResolution("/WEB-INF/jsp/landing/military/default.jsp");
		}
		else if (getPage() != null && !getPage().trim().equals("")) {
			landingPage = retrieveLandingPage(getPage());
			
			if (landingPage != null) {
				String templateName = landingPage.getTemplateName().trim().toLowerCase();
		
				if (templateName.equals("bordertemplate")) {
					return new ForwardResolution("/WEB-INF/jsp/landing/border/template.jsp");
				}
				else if (templateName.equals("urbantemplate")) {
					return new ForwardResolution("/WEB-INF/jsp/landing/urban/template.jsp");
				}
				else if (templateName.equals("pharmasaveurbantemplate")) {
					return new ForwardResolution("/WEB-INF/jsp/landing/urban/pharmasavetemplate.jsp");
				}
				else if (templateName.equals("militarytemplate")) {
					return new ForwardResolution("/WEB-INF/jsp/landing/military/template.jsp");
				}
			}
		}
		
		return new RedirectResolution(HomeActionBean.class);
	}
	
	private LandingPage retrieveLandingPage(String pageName) throws Exception {
		LandingPageDao landingPageDao = new LandingPageDao();
		LandingPage landingPage = landingPageDao.read(pageName);
		
		return landingPage;
	}

	public LandingPage getLandingPage() {
		return landingPage;
	}
			
	public void setPage(String page) {
		this.page = page;
	}
	
	public String getPage() {
		return page;
	}
}
