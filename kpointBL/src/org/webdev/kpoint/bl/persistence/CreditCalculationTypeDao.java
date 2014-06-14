package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.CreditCalculationType;

public class CreditCalculationTypeDao extends BaseDao implements GenericDao<CreditCalculationType, Integer> {

	private static final KinekLogger logger = new KinekLogger(CreditCalculationTypeDao.class);
	
	@Override
	public Integer create(CreditCalculationType newInstance) throws Exception {
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
			logProps.put("CreditCalculationType Name", newInstance.getName());
			ApplicationException aex = new ApplicationException("Unable to create a new credit card calculation type.", ex);
			logger.error(aex, logProps);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw(aex);
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		return newInstance.getId();
	}

	@Override
	public void delete(CreditCalculationType persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CreditCalculationType read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object CreditCalculationType = session.get(CreditCalculationType.class, id);
			tx.commit();
			
			return (CreditCalculationType)CreditCalculationType;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CreditCalculationTypeID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Credit Card Calculation Type.", ex);
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
	
	/**
	 * Retrieves a single CreditCalculationType matching the supplied code exactly
	 * @param code The code of the CreditCalculationType being retrieved 
	 * @return A CreditCalculationType matching the supplied code if one is found; otherwise null
	 */
	@SuppressWarnings("unchecked")
	public CreditCalculationType readCode(String code) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(CreditCalculationType.class)
				.add(Restrictions.eq("code", code))
				.setMaxResults(1);
			List<CreditCalculationType> CreditCalculationTypes = criteria.list();
			tx.commit();
			
			CreditCalculationType CreditCalculationType = CreditCalculationTypes.size() > 0 ? CreditCalculationTypes.get(0) : null;
			
			return CreditCalculationType;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CreditCalculationType Code", code);
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Credit Card Calculation Type.", ex);
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
	public void update(CreditCalculationType transientObject) throws Exception {
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
			logProps.put("CreditCalculationTypeID", String.valueOf(transientObject.getId()));
			ApplicationException aex = new ApplicationException("Unable to update a Credit Card Calculation Type.", ex);
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
	public List<CreditCalculationType> fetch() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
