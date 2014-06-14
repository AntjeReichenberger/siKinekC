package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.Coupon;
import org.webdev.kpoint.bl.pojo.CouponNotification;
import org.webdev.kpoint.bl.pojo.Notification;

public class NotificationDao extends BaseDao {

	private static final KinekLogger logger = new KinekLogger(NotificationDao.class);
	
	public Notification read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object notification = session.get(Notification.class, id);
			tx.commit();
			
			return (Notification)notification;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("UserID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read Notification.", ex);
			logger.error(aex, logProps);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
	}
	
	/**
	 * Retrieves the list of notifications that the user has subscribed to.  In order to provide a simple interface for all
	 * calling systems to determine which notifications a user has subscribed to, concrete variables have been created for 
	 * each notification.  If a new notification becomes available for subscription, it will require code changes to be exposed
	 * and used by calling systems.
	 * @return The list of notifications
	 */
	public List<Notification> fetchNotifications(int userId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			List<Notification> userNotifications = null;
			
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			Query query = session.createSQLQuery(
		    	"SELECT NotificationId " +
		    	"FROM user_notification WHERE UserId = ?;")
		    	.setString(0, String.valueOf(userId));
			
			List data = query.list();
			if(data == null){
				return new ArrayList<Notification>();
			}
			else
			{
				userNotifications = new ArrayList<Notification>();
				for(int i=0; i<data.size(); i++)
				{
					int notificationId = ((Integer)data.get(i)).intValue();
					Notification n = new Notification();
					n.setId(notificationId);
					
					userNotifications.add(n);
				}
			}
			
			tx.commit();
			
			return userNotifications;
			
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			ApplicationException aex = new ApplicationException("Unable to retrieve users notification subscriptions.", ex);
			logger.error(aex, logProps);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
	}

}
