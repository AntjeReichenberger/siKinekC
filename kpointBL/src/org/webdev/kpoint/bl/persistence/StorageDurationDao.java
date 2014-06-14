package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.StorageDuration;

public class StorageDurationDao extends BaseDao implements GenericDao<StorageDuration, Integer> {
	
	private static final KinekLogger logger = new KinekLogger(StorageDurationDao.class);
	@Override
	public Integer create(StorageDuration newInstance) throws Exception {
		// TODO Auto-generated method stub	
		return null;
	}

	@Override
	public void delete(StorageDuration persistentObject) throws Exception {
		// TODO Auto-generated method stub	
	}

	
	@Override @SuppressWarnings("unchecked")
	public List<StorageDuration> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(StorageDuration.class)
				.addOrder(Order.asc("name"));
			List<StorageDuration> depots = criteria.list();
			tx.commit();
			
			return depots;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of storage durations.", ex);
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
	
	@SuppressWarnings("unchecked")
	public List<StorageDuration> fetch(int days) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(StorageDuration.class)
				.addOrder(Order.asc("id"))
				.add(Restrictions.le("minDays",days));
			List<StorageDuration> storageDurations = criteria.list();
			tx.commit();
			
			return storageDurations;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of storage durations.", ex);
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

	@Override
	public StorageDuration read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object depot = session.get(StorageDuration.class, id);
			tx.commit();
			
			return (StorageDuration)depot;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested storage duration.", ex);
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
	public void update(StorageDuration transientObject) throws Exception {
		// TODO Auto-generated method stub	
	}
	
}
