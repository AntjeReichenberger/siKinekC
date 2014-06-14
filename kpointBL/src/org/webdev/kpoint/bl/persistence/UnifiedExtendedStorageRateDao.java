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
import org.webdev.kpoint.bl.pojo.StorageDuration;
import org.webdev.kpoint.bl.pojo.StorageWeightGroup;
import org.webdev.kpoint.bl.pojo.UnifiedExtendedStorageRate;

public class UnifiedExtendedStorageRateDao extends BaseDao implements GenericDao<UnifiedExtendedStorageRate, Integer> {
	
	private static final KinekLogger logger = new KinekLogger(UnifiedExtendedStorageRateDao.class);
	@Override
	public Integer create(UnifiedExtendedStorageRate newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create a new unified extended storage entry.", ex);
			logger.error(aex);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		return newInstance.getId();
	}

	@Override
	public void delete(UnifiedExtendedStorageRate persistentObject) throws Exception {
		// TODO Auto-generated method stub	
	}

	
	@Override @SuppressWarnings("unchecked")
	public List<UnifiedExtendedStorageRate> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(UnifiedExtendedStorageRate.class)
				.addOrder(Order.asc("id"));
			List<UnifiedExtendedStorageRate> depots = criteria.list();
			tx.commit();
			
			return depots;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of unified extended storage rates.", ex);
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
	public UnifiedExtendedStorageRate read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object depot = session.get(UnifiedExtendedStorageRate.class, id);
			tx.commit();
			
			return (UnifiedExtendedStorageRate)depot;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Id", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested unified storage rate.", ex);
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
	
	@SuppressWarnings("unchecked")
	public List<UnifiedExtendedStorageRate> fetch(List<StorageDuration> durations,StorageWeightGroup storageWeightGroup) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(UnifiedExtendedStorageRate.class)
				.addOrder(Order.asc("id"))
				.add(Restrictions.eq("storageWeightGroup",storageWeightGroup));
			
			List<UnifiedExtendedStorageRate> unifiedExtendedStorageRates = criteria.list();
			tx.commit();
			
			List<UnifiedExtendedStorageRate> unifiedExtendedStorageRatesResult = new ArrayList<UnifiedExtendedStorageRate>();
			for(UnifiedExtendedStorageRate unifiedExtendedStorageRate: unifiedExtendedStorageRates){
				for(StorageDuration duration : durations){
					if(unifiedExtendedStorageRate.getStorageDuration().getId() == duration.getId()){
						unifiedExtendedStorageRatesResult.add(unifiedExtendedStorageRate);
					}
				}
			}
			
			return unifiedExtendedStorageRatesResult;
			
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of unified extended storage rates.", ex);
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
	public void update(UnifiedExtendedStorageRate transientObject) throws Exception {
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
			logProps.put("ID", String.valueOf(transientObject.getId()));
			ApplicationException aex = new ApplicationException("Unable to update the requested unified storage rate.", ex);
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

