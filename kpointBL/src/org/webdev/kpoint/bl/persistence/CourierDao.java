package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.Courier;

public class CourierDao extends BaseDao implements GenericDao<Courier, Integer> {

	private static final KinekLogger logger = new KinekLogger(CourierDao.class);
	
	@Override
	public Integer create(Courier newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Courier persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Retrieves the courier with the supplied id; null if none exist
	 * @param id The id of the courier to retrieve
	 * @return The courier matching the supplied id; otherwise null
	 */
	public Courier read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object courier = session.get(Courier.class, id);
			tx.commit();
			
			return (Courier)courier;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CourierID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Courier.", ex);
			logger.error(aex, logProps);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		//return null;
	}

	@Override
	public void update(Courier transientObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Retrieves a list of all couriers
	 * @return A list containing all couriers
	 */
	@SuppressWarnings("unchecked")
	public List<Courier> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Courier.class);
			List<Courier> couriers = criteria.list();
			tx.commit();
			
			return couriers;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of couriers.", ex);
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
	 * Retrieves a list of couriers that provide package tracking capabilities
	 * @return The courier matching the supplied id; otherwise null
	 */
	@SuppressWarnings("unchecked")
	public List<Courier> fetchTrackableCouriers() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Courier.class)
				.add(Restrictions.eq("isTrackable", true));
			
			List<Courier> couriers = criteria.list();
			tx.commit();
			
			return couriers;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of couriers.", ex);
			logger.error(aex);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		//return null;
	}
	
	/**
	 * Retrieves the courier with the supplied code; null if none exist
	 * @param id The code of the courier to retrieve
	 * @return The courier matching the supplied id; otherwise null
	 */
	@SuppressWarnings("unchecked")
	public Courier read(String courierCode) throws Exception {
		Session session = null;
		Transaction tx = null;
		Courier courier = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Courier.class)
				.add(Restrictions.eq("courierCode", courierCode));
			
			List<Courier> couriers = criteria.list();
			if(couriers != null)
			{
				courier = couriers.get(0);
			}
			tx.commit();
			
			return courier;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of couriers.", ex);
			logger.error(aex);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		//return null;
	}
}
