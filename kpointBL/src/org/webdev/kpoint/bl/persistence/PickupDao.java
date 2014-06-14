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
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.Pickup;


public class PickupDao extends BaseDao implements GenericDao<Pickup, Integer> {

	private static final KinekLogger logger = new KinekLogger(PickupDao.class);
	
	@Override
	public Integer create(Pickup newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("TransactionID", newInstance.getTransactionId());
			ApplicationException aex = new ApplicationException("Could not create pickup.", ex);
			logger.error(aex, logProps);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}

		return newInstance.getPickupId();
	}

	@Override
	public Pickup read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object association = session.get(Pickup.class, id);
			tx.commit();
				
			return (Pickup)association;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("PickupID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read pickup.", ex);
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
	public void update(Pickup transientObject) throws Exception {
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
			logProps.put("PickupID", transientObject.getPickupId().toString());
			ApplicationException aex = new ApplicationException("Could not update pickup.", ex);
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
	public void delete(Pickup persistentObject) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pickup> fetch() throws Exception {
		List<Pickup> associations = new ArrayList<Pickup>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Pickup.class);
			associations = criteria.list();
			tx.commit();
			
			return associations;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch all pickups.", ex);
			logger.error(aex);
		
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
	 * Find all pickups exactly matching the supplied Kinek number. 
	 * @param kinekNumber The Kinek number to search by.
	 * @return A list of pickups exactly matching the provided Kinek number
	 */
	@SuppressWarnings("unchecked")
	public List<Pickup> fetch(String kinekNumber) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
		
			Criteria criteria = session.createCriteria(Pickup.class)
				.createAlias("consumer", "c")
				.add(Restrictions.eq("c.kinekNumber", kinekNumber));
			
			List<Pickup> pickups = criteria.list();
	
			tx.commit();
			
			return pickups;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KinekNumber", kinekNumber);
			ApplicationException aex = new ApplicationException("Could not read pickups for user.", ex);
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
	 * Find all pickups exactly matching the supplied KinekPoint. 
	 * @param kpId The id of the KinekPoint to search by.
	 * @return A list of pickups exactly matching the provided KinekPoint
	 */
	@SuppressWarnings("unchecked")
	public List<Pickup> fetch(int kpId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Pickup.class)
				.createAlias("kinekPoint", "kp")
				.add(Restrictions.eq("kp.depotId", kpId));
			
			List<Pickup> pickups = criteria.list();
	
			tx.commit();
			
			return pickups;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KinekPointID", String.valueOf(kpId));
			ApplicationException aex = new ApplicationException("Could not read pickups for KinekPoint.", ex);
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
	 * Find all pickups exactly matching the supplied KinekPoint. 
	 * @param kpId The id of the KinekPoint to search by.
	 * @return A list of pickups exactly matching the provided KinekPoint
	 */
	@SuppressWarnings("unchecked")
	public List<Pickup> fetch(int kpId, Calendar startDate, Calendar endDate) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
		
			Criteria criteria = session.createCriteria(Pickup.class)
				.createAlias("kinekPoint", "kp")
				.add(Restrictions.eq("kp.depotId", kpId))
				.add(Restrictions.between("pickupDate", startDate.getTime(), endDate.getTime()));
			
			List<Pickup> pickups = criteria.list();
	
			tx.commit();
			
			return pickups;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KinekPointID", String.valueOf(kpId));
			logProps.put("StartDate", startDate.toString());
			logProps.put("EndDate", endDate.toString());
			ApplicationException aex = new ApplicationException("Could not read pickups for KinekPoint.", ex);
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
	 * Retrieves the pickup that contains the provided package 
	 * @param kinekNumber The Kinek number to search by.
	 * @return A list of pickups exactly matching the provided Kinek number
	 */
	@SuppressWarnings("unchecked")
	public Pickup read(Package packageObj) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
		
			Criteria criteria = session.createCriteria(Pickup.class)
				.createAlias("packages", "p")
				.add(Restrictions.eq("p.packageId", packageObj.getPackageId()))
				.setMaxResults(1);
			
			List<Pickup> pickups = criteria.list();
	
			tx.commit();
			Pickup pickup = pickups.size() > 0 ? pickups.get(0) : null;
			
			return pickup;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("PackageID", String.valueOf(packageObj.getPackageId()));
			ApplicationException aex = new ApplicationException("Could not read pickups for package.", ex);
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
