package org.webdev.kpoint.action.depot;

import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.action.BaseActionBean;
import org.webdev.kpoint.bl.persistence.NewsDao;
import org.webdev.kpoint.bl.pojo.News;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/DepotNewsItem.action")
public class NewsItemActionBean extends BaseActionBean {
	private String id;
	private News item;
	
	@DefaultHandler
	public Resolution view() throws Exception {	
		item = new NewsDao().read(Integer.parseInt(id));
		return UrlManager.getDepotNewsItem();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public News getItem() {
		return item;
	}

	public void setItem(News item) {
		this.item = item;
	}
}
