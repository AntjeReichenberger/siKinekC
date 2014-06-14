package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.DefaultKinekPointHistory;
import org.webdev.kpoint.bl.pojo.Tracking;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.UserTracking;
import org.webdev.kpoint.bl.tracking.TrackedPackage;

public class UserTrackingDao extends BaseDao implements GenericDao<UserTracking, String> {

	private static final KinekLogger logger = new KinekLogger(UserTrackingDao.class);
	
	@Override
	public String create(UserTracking newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		
		//check to see if Tracking object exists, if not, create.	
		Tracking checkTrack = newInstance.getTracking();
		TrackingDao trackingDao = new TrackingDao();
		Tracking existing = trackingDao.read(checkTrack.getTrackingNumber(), checkTrack.getCourier().getCourierId());
		
		if(existing == null){
			trackingDao.create(checkTrack);
		}
		else{
			checkTrack.setId(existing.getId());
			trackingDao.update(checkTrack);
		}
		
		try{			
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();		
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create new user tracking entry.", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}	
		return newInstance.getPackageNickname();
	}

	@Override
	public void delete(UserTracking persistentObject) throws Exception {
		// Delete is implemented by a soft-delete using the Update method (isActive property)
	}
	
	@Override
	public List<UserTracking> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		List<UserTracking> trackings = null;
		
		try{
			
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(UserTracking.class)
										.add(Restrictions.eq("isActive", true));
			
			trackings = criteria.list();
			
			tx.commit();
			
			if(trackings == null){
				return new ArrayList<UserTracking>();
			}	
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve user tracking items", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
		
		return trackings;
	}
	
	public List<UserTracking> fetch(String trackingId) throws Exception {	
		return null;
	}
	
	public List<UserTracking> fetch(Tracking tracking) throws Exception {
		Session session = null;
		Transaction tx = null;
		List<UserTracking> trackings = null;
		
		try{		
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(UserTracking.class)
										.add(Restrictions.eq("tracking",tracking));
			
			trackings = criteria.list();	
			
			if(trackings == null){
				return new ArrayList<UserTracking>();
			}
			
			tx.commit();
			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve user tracking items", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
		
		return trackings;
	}
	
	
	public List<UserTracking> fetch(User user) throws Exception {
		Session session = null;
		Transaction tx = null;
		List<UserTracking> trackings = null;
		
		try{		
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(UserTracking.class)
				.add(Restrictions.eq("user",user))
				.add(Restrictions.eq("isActive", true));
			
						
			trackings = criteria.list();	
			
			if(trackings == null){
				return new ArrayList<UserTracking>();
			}
			
			tx.commit();
			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve user tracking items", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
		
		return trackings;
	}
	
	public UserTracking read(int trackingId) throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		UserTracking userTracking = null;		
		
		try{			
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(UserTracking.class)
									   .createAlias("tracking", "t")
									   .add(Restrictions.eq("t.id", trackingId));
			
			List data = criteria.list();
			if(data != null && data.size() > 0)
				userTracking = (UserTracking) data.get(0);
						
			tx.commit();
			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve user tracking item. Tracking ID: " + trackingId, ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
		
		return userTracking;
	}
	
	public UserTracking read(Tracking tracking, User user) throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		UserTracking userTracking = null;		
		
		try{			
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(UserTracking.class)
									   .add(Restrictions.eq("tracking", tracking))
									   .add(Restrictions.eq("user", user))
									   .add(Restrictions.eq("isActive", true));
			
			List data = criteria.list();
			if(data != null && data.size() > 0)
				userTracking = (UserTracking) data.get(0);
						
			tx.commit();
			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve user tracking item", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
		
		return userTracking;
	}
	
	@Override
	public void update(UserTracking transientObject) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			session.update(transientObject);
			tx.commit();
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			ApplicationException aex = new ApplicationException("Could not update the user tracking entry.", ex);
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

	public String createByEmail(UserTracking newInstance) throws Exception {
			// TODO Auto-generated method stub
			
			Session session = null;
			Transaction tx = null;
			boolean successful = false;
			
			//check to see if Tracking object exists, if not, create.	
			Tracking checkTrack = newInstance.getTracking();
			TrackingDao trackingDao = new TrackingDao();
			Tracking existing = trackingDao.read(checkTrack.getTrackingNumber(), checkTrack.getCourier().getCourierId());
			
			if(existing == null){
				trackingDao.create(checkTrack);
			}
			else{
				checkTrack.setId(existing.getId());
				trackingDao.update(checkTrack);
			}
			
			try{			
				session = HibernateSessionUtil.getCurrentSession();
				tx = session.beginTransaction();
				session.save(newInstance);
				tx.commit();
				successful = true;
			}catch(Exception ex){
				ApplicationException aex = new ApplicationException("Unable to create new user tracking entry.", ex);
				logger.error(aex);		
				if(tx != null && tx.isActive()){
					tx.rollback();
				}
				throw aex;
			}finally{
				verifyDBState(logger, session, tx);
			}
			if(successful)
				return newInstance.getPackageNickname();
			else{
				return null;
			}
	}

	public UserTracking read(TrackedPackage trackedPackage, User user) throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		UserTracking userTracking = null;
		
		Tracking track = new TrackingDao().read(trackedPackage.getTrackingNumber(), trackedPackage.getCourier().getCourierId());
		
		try{			
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(UserTracking.class)
									   .add(Restrictions.eq("user", user))
									   .add(Restrictions.eq("tracking",track));
			
			List data = criteria.list();
			if(data != null && data.size() > 0)
				userTracking = (UserTracking) data.get(0);
						
			tx.commit();
			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve user tracking item", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
		
		return userTracking;
	}

	@Override
	public UserTracking read(String trackingNumber) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
