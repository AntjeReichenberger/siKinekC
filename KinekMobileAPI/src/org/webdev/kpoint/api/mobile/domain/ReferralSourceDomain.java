package org.webdev.kpoint.api.mobile.domain;

import java.util.List;

import org.webdev.kpoint.bl.persistence.ReferralSourceDao;
import org.webdev.kpoint.bl.pojo.ReferralSource;

public class ReferralSourceDomain extends Domain{
	
	public static List<ReferralSource> getReferralSources() throws Exception{
		ReferralSourceDao dao = new ReferralSourceDao();
		List<ReferralSource> sources = dao.fetchForConsumer(); 

		return sources;
	}
	
}
