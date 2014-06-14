package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.CreditIssueReason;


public class CreditIssueReasonDao extends BaseDao implements GenericDao<CreditIssueReason, Integer> {
	
	private static final KinekLogger logger = new KinekLogger(CreditIssueReasonDao.class);
	
	@Override
	public Integer create(CreditIssueReason newInstance) throws Exception {
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
			logProps.put("CreditIssueReason Desc", newInstance.getTitle());
			ApplicationException aex = new ApplicationException("Unable to create a new Credit Issue Reason.", ex);
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
	public void delete(CreditIssueReason persistentObject) throws Exception {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void update(CreditIssueReason transientObject) throws Exception {
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
			logProps.put("CreditIssueReasonID", String.valueOf(transientObject.getId()));
			ApplicationException aex = new ApplicationException("Unable to update a Credit Issue Reason.", ex);
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
	public List<CreditIssueReason> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(CreditIssueReason.class);
			List<CreditIssueReason> creditIssueReasons = criteria.list();
			tx.commit();
			
			return creditIssueReasons;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Credit Issue Reasons.", ex);
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
	public CreditIssueReason read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object creditIssueReason = session.get(CreditIssueReason.class, id);
			tx.commit();
			
			return (CreditIssueReason)creditIssueReason;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CreditIssueReasonID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Credit Issue Reason.", ex);
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
	public CreditIssueReason read(String title) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(CreditIssueReason.class)
				.add(Restrictions.eq("title", title))
				.setMaxResults(1);
			List<CreditIssueReason> creditIssueReasons = criteria.list();
			tx.commit();
			CreditIssueReason creditIssueReason = creditIssueReasons.size() > 0 ? creditIssueReasons.get(0) : null;
			
			return creditIssueReason;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CreditIssueReason Title", title);
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Credit Issue Reason.", ex);
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
