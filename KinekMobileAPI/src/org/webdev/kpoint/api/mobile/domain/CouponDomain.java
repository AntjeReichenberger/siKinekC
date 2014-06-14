package org.webdev.kpoint.api.mobile.domain;


import java.util.List;
import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.mobile.util.WSApplicationError;
import org.webdev.kpoint.api.mobile.util.WSApplicationException;
import org.webdev.kpoint.bl.persistence.CouponDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.pojo.Coupon;
import org.webdev.kpoint.bl.pojo.KinekPoint;

public class CouponDomain extends Domain{
	
	private final static String KINEKPOINT_NOT_FOUND = "INVALID_KINEKPOINT_ID"; 
			
	public static List<Coupon> getCoupons(int depotId) throws Exception{
		KinekPointDao kinekPointDao = new KinekPointDao();
		
		KinekPoint kinekPoint = kinekPointDao.read(depotId);
		
		if(kinekPoint == null){
			WSApplicationError err = new WSApplicationError(KINEKPOINT_NOT_FOUND, "No coupons found, KinekPoint ID is invalid: " + depotId,Response.Status.BAD_REQUEST);
			throw new WSApplicationException(err, err.getResponse());
		}
		
		CouponDao couponDao = new CouponDao();
		List<Coupon> coupons = couponDao.fetchKinekPointCoupons(kinekPoint);
		//coupons returned from the backend contain the KinekPoint that the coupon is mapped to
		//for the API, we do not need this information since we already know which KP we are requesting coupons for
		//look through the list of coupons and set the kinekpoint to null so it will not be included in the response
		for(int i=0; i<coupons.size(); i++){
			coupons.get(i).setKinekPoint(null);
		}
		return coupons;
	}
}
