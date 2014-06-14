package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.Coupon;
import org.webdev.kpoint.bl.pojo.CouponNotification;

public class CouponNotificationDao extends BaseDao implements GenericDao<CouponNotification, Integer> {

	private static final KinekLogger logger = new KinekLogger(CouponNotificationDao.class);
	
	@Override
	public Integer create(CouponNotification newInstance) throws Exception {
		// TODO Auto-generated method stub
		
		Session session = null;
		Transaction tx = null;
		
		try{
			
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();			
			session.save(newInstance);			
			tx.commit();
			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create a new coupon notification.", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
		
		return newInstance.getId();
	}

	@Override
	public void delete(CouponNotification persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<CouponNotification> fetch() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<CouponNotification> fetchCouponNotificationsByUserIdAndCoupons(int userId, List<Coupon> coupons) throws Exception {
		// TODO Auto-generated method stub
		
		Session session = null;
		Transaction tx = null;
		
		List<Integer> couponIds = new ArrayList<Integer>(); 
		for(Coupon coupon:coupons){
			couponIds.add(coupon.getCouponId());
		}
		
		List<CouponNotification> couponNotifications = null;
						
		try{
			
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			String hql = "select cn.coupon.couponId from CouponNotification cn " +
							"WHERE cn.user.userId = " + userId + " " + 
							"GROUP BY cn.coupon.couponId " +
							"ORDER BY max(cn.receivedDate) ASC";
 
			
			Query query = session.createQuery(hql);
			List results = query.list();		
			if(results == null){
				return new ArrayList<CouponNotification>();
			}
			else
			{
				couponNotifications = new ArrayList<CouponNotification>();
				for(int i=0; i<results.size(); i++)
				{
					int couponId = ((Integer)results.get(i)).intValue();
					CouponNotification cn = new CouponNotification();
					Coupon coupon = new Coupon();
					coupon.setCouponId(couponId);
					cn.setCoupon(coupon);
					
					couponNotifications.add(cn);
				}
			}
			
			
			tx.commit();
			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to fetch list of coupons by user Id.", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
		
		return couponNotifications;
	}	
	
	@Override
	public void update(CouponNotification transientObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CouponNotification read(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
