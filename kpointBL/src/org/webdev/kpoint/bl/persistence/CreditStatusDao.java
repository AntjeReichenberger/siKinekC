package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.CreditStatus;

public class CreditStatusDao extends BaseDao implements GenericDao<CreditStatus, Integer> {
	
	private static final KinekLogger logger = new KinekLogger(CreditStatusDao.class);
	
	@Override
	public Integer create(CreditStatus newInstance) throws Exception {
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
			logProps.put("CreditStatus", newInstance.getStatus());
			ApplicationException aex = new ApplicationException("Unable to create a new Credit Status.", ex);
			logger.error(aex, logProps);
		
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
	public void delete(CreditStatus persistentObject) throws Exception {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void update(CreditStatus transientObject) throws Exception {
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
			logProps.put("CreditStatusID", String.valueOf(transientObject.getId()));
			ApplicationException aex = new ApplicationException("Unable to update a Credit Status.", ex);
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
	
	@Override @SuppressWarnings("unchecked")
	public List<CreditStatus> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(CreditStatus.class);
			List<CreditStatus> creditStatuses = criteria.list();
			tx.commit();
			
			return creditStatuses;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Credit Statuses.", ex);
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
	
	@Override
	public CreditStatus read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object creditStatus = session.get(CreditStatus.class, id);
			tx.commit();
			
			return (CreditStatus)creditStatus;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CreditStatusID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Credit Status.", ex);
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
	
	@SuppressWarnings("unchecked")
	public CreditStatus read(String status) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(CreditStatus.class)
				.add(Restrictions.eq("status", status))
				.setMaxResults(1);
			List<CreditStatus> creditStatuses = criteria.list();
			tx.commit();
			CreditStatus creditStatus = creditStatuses.size() > 0 ? creditStatuses.get(0) : null;
			
			return creditStatus;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CreditStatus", status);
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Credit Stutus.", ex);
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
}
