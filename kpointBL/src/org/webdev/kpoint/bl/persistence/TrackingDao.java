package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.pojo.DefaultKinekPointHistory;
import org.webdev.kpoint.bl.pojo.Tracking;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.UserTracking;

public class TrackingDao extends BaseDao implements GenericDao<Tracking, String> {

	private static final KinekLogger logger = new KinekLogger(TrackingDao.class);
	
	@Override
	public String create(Tracking newInstance) throws Exception {
	
		Session session = null;
		Transaction tx = null;
		
		try{	
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
	
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create new tracking record.", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}	
		return newInstance.getId().toString();
	}
	
	public String createByEmail(Tracking newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		boolean successful = false;
		
		try{	
			session = HibernateSessionUtil.getCurrentSession();		
			tx = session.beginTransaction();	
			session.save(newInstance);
			tx.commit();
			successful = true;
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create new tracking record.", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}	
		if(successful){
			return newInstance.getTrackingNumber();
		}
		else{
			return null;
		}
	}

	@Override
	public void delete(Tracking persistentObject) throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		
		try{
			
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			session.delete(persistentObject);
			tx.commit();

		}catch(Exception ex){
			
			ApplicationException aex = new ApplicationException("Unable to delete selected tracking item", ex);
			logger.error(aex);
			logger.error("selected tracking package for delete operation: "+persistentObject.getTrackingNumber());
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}	
	}
	
	@Override
	public List<Tracking> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		List<Tracking> trackings = null;
		
		try{
			
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Tracking.class);
			
			trackings = criteria.list();
			tx.commit();
		
			if(trackings == null){
				return new ArrayList<Tracking>();
			}
			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve tracking items", ex);
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
	
	public List<Tracking> fetchTrackingRecordsForAutoUpdate() throws Exception {
		Session session = null;
		Transaction tx = null;
		List<Tracking> trackings = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1*ConfigurationManager.getMaxDaysWithoutTrackingUpdateThreshold()); //subtract 5 days from the current date.  we only want to process tracking records that are NOT delivered AND have been successfully updated in the last 5 days
		
		try{
			
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Tracking.class)
										.add(Restrictions.eq("isDelivered", false))
										.add(Restrictions.gt("lastModifiedDate", cal));
			
			trackings = criteria.list();
			tx.commit();
		
			if(trackings == null){
				return new ArrayList<Tracking>();
			}
			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve tracking items", ex);
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
	
	
	public List<Tracking> fetch(String trackingNumber) throws Exception {
		// TODO Auto-generated method stub
		
		return null;
	}

	public Tracking read(int trackingId) throws Exception {
		// TODO Auto-generated method stub 
		Session session = null;
		Transaction tx = null;
		Tracking tracking = null;		
		
		try{
			
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Tracking.class)
									   .add(Restrictions.eq("id", trackingId));
			
			List<Tracking> data = (List<Tracking>) criteria.list();
			if(data != null && data.size() > 0){
				tracking = (Tracking) data.get(0);
			}						
			tx.commit();			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve tracking item", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
		
		return tracking;
	}
	
	public Tracking read(String trackingNumber, int courierId) throws Exception {
		// TODO Auto-generated method stub 
		Session session = null;
		Transaction tx = null;
		Tracking tracking = null;		
		
		try{
			
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Tracking.class)
									   .add(Restrictions.eq("trackingNumber", trackingNumber))
									   .add(Restrictions.eq("courier.courierId", courierId));
			
			List<Tracking> data = (List<Tracking>) criteria.list();
			if(data != null && data.size() > 0){
				tracking = (Tracking) data.get(0);
			}						
			tx.commit();			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve tracking item", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
		
		return tracking;
	}
	
	
	@Override
	public void update(Tracking transientObject) throws Exception {
		TrackingDao trackingDao = new TrackingDao();
		
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
			logProps.put("TrackingNumber", String.valueOf(transientObject.getTrackingNumber()));
			ApplicationException aex = new ApplicationException("Could not update the TrackedPackage.", ex);
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

	@Override
	public Tracking read(String id) {
		// TODO Auto-generated method stub
		return null;
	}


}
