package org.webdev.kpoint.api.mobile.domain;

import java.util.List;

import org.webdev.kpoint.bl.persistence.CourierDao;
import org.webdev.kpoint.bl.pojo.Courier;

public class CourierDomain extends Domain{
	
	public static List<Courier> getCouriers() throws Exception{
		CourierDao dao = new CourierDao();
		List<Courier> couriers = dao.fetch(); 
		return couriers;
	}
	
	public static List<Courier> getTrackingCouriers() throws Exception{
		CourierDao dao = new CourierDao();
		List<Courier> couriers = dao.fetchTrackableCouriers(); 
		return couriers;
	}
}
