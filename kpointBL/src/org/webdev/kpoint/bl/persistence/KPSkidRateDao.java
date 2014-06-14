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
import org.webdev.kpoint.bl.pojo.KPSkidRate;
import org.webdev.kpoint.bl.pojo.KinekPoint;

public class KPSkidRateDao extends BaseDao implements GenericDao<KPSkidRate, Integer> {
	
	private static final KinekLogger logger = new KinekLogger(KPSkidRateDao.class);
	
	@Override
	public Integer create(KPSkidRate newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create a new Skid rate.", ex);
			logger.error(aex);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		return newInstance.getUnifiedSkidRate().getId();
	}

	@Override
	public void delete(KPSkidRate persistentObject) throws Exception {
		Session session = null;
		Transaction tx = null;

		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			session.delete(persistentObject);
			tx.commit();
			
		}catch(Exception ex){	
			ApplicationException aex = new ApplicationException("Unable to delete selected skid rate", ex);
			logger.error(aex);
			//TODO logger.error("selected skid rate for delete operation. Rate ID: "+ persistentObject.getUnifiedSkidRate().getId() 
			//TODO		+ "KinekPointID: "+ persistentObject.getKinekPoint().getDepotId());
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}	
		
		
	}

	
	@Override @SuppressWarnings("unchecked")
	public List<KPSkidRate> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(KPSkidRate.class)
				.addOrder(Order.asc("id"));
			List<KPSkidRate> depots = criteria.list();

			tx.commit();
			
			return depots;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Points.", ex);
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
	public List<KPSkidRate> fetch(KinekPoint kp) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();		
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(KPSkidRate.class)
				.addOrder(Order.asc("id"))
				.add(Restrictions.eq("kinekPointId", kp.getDepotId()));
			List<KPSkidRate> kpSkidRates = criteria.list();
					
			tx.commit();
			
			return kpSkidRates;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Points.", ex);
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
	public KPSkidRate read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object depot = session.get(KPSkidRateDao.class, id);
			tx.commit();
			
			return (KPSkidRate)depot;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KPID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Kinek Point.", ex);
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
	public void update(KPSkidRate transientObject) throws Exception {
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
			logProps.put("KinekPointID", String.valueOf(transientObject.getUnifiedSkidRate().getId()));
			ApplicationException aex = new ApplicationException("Unable to create skid rate entry", ex);
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
