package org.webdev.kpoint.action.depot;

import java.util.List;

import org.webdev.kpoint.action.BaseActionBean;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.persistence.NewsDao;
import org.webdev.kpoint.bl.pojo.News;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/DepotNews.action")
public class NewsListActionBean extends BaseActionBean {

	private List<News> pressItems;
	private List<News> newsItems;

	@DefaultHandler
	public Resolution view() throws Exception {		
		pressItems = new NewsDao().fetch(ExternalSettingsManager.getNewsTypeIdPress(), true);
		newsItems = new NewsDao().fetch(ExternalSettingsManager.getNewsTypeIdNews(), true);
		
		return UrlManager.getDepotNewsList();
	}

	public List<News> getPressItems() {
		return pressItems;
	}

	public void setPressItems(List<News> pressItems) {
		this.pressItems = pressItems;
	}

	public List<News> getNewsItems() {
		return newsItems;
	}

	public void setNewsItems(List<News> newsItems) {
		this.newsItems = newsItems;
	}
}
