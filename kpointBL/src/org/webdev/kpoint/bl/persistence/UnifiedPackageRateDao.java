package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.UnifiedPackageRate;

public class UnifiedPackageRateDao extends BaseDao implements GenericDao<UnifiedPackageRate, Integer> {
	
	private static final KinekLogger logger = new KinekLogger(UnifiedPackageRateDao.class);
	@Override
	public Integer create(UnifiedPackageRate newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create a new UnifiedPackageRate entry.", ex);
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
	public void delete(UnifiedPackageRate persistentObject) throws Exception {
		// TODO Auto-generated method stub	
	}

	
	@Override @SuppressWarnings("unchecked")
	public List<UnifiedPackageRate> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(UnifiedPackageRate.class)
				.addOrder(Order.asc("id"));
			List<UnifiedPackageRate> depots = criteria.list();
			tx.commit();
			
			return depots;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of unified package rates.", ex);
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
	public UnifiedPackageRate read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object depot = session.get(UnifiedPackageRate.class, id);
			tx.commit();
			
			return (UnifiedPackageRate)depot;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KPID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested unified package rate.", ex);
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
	public void update(UnifiedPackageRate transientObject) throws Exception {
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
			logProps.put("KinekPointID", String.valueOf(transientObject.getId()));
			ApplicationException aex = new ApplicationException("Unable to update a Kinek Point.", ex);
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

