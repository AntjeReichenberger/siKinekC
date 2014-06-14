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
import org.webdev.kpoint.bl.pojo.ReferralSource;

public class ReferralSourceDao extends BaseDao implements GenericDao<ReferralSource, Integer> {
	
	private static final KinekLogger logger = new KinekLogger(ReferralSourceDao.class);
	
	public Integer create(ReferralSource newInstance) throws Exception {
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
			logProps.put("ReferralSourceDisplayName", newInstance.getDisplayName());
			ApplicationException aex = new ApplicationException("Could not create ReferralSource.", ex);
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

	public void delete(ReferralSource persistentObject) throws Exception {
		// TODO Auto-generated method stub
	}
	
	public void update(ReferralSource transientObject) throws Exception {
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
			logProps.put("ReferralSourceID", String.valueOf(transientObject.getId()));
			ApplicationException aex = new ApplicationException("Could not update ReferralSource.", ex);
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
	 * Retrieves the package specified by the supplied package id
	 * @param id The id of the package to retrieve.
	 * @return The package identified by the id if one is found; otherwise false
	 */
	public ReferralSource read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object referralSource = session.get(ReferralSource.class, id);
			tx.commit();
				
			return (ReferralSource)referralSource;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ReferralSourceID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read ReferralSource.", ex);
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
	public List<ReferralSource> fetch() throws Exception {
		List<ReferralSource> referralSources = new ArrayList<ReferralSource>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ReferralSource.class);
			referralSources = criteria.list();
			tx.commit();
			
			return referralSources;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch ReferralSources.", ex);
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
	public List<ReferralSource> fetchForConsumer() throws Exception {
		List<ReferralSource> referralSources = new ArrayList<ReferralSource>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ReferralSource.class)
				.add(Restrictions.eq("displayInConsumer", true))
				.addOrder(Order.asc("displayIndex"));
			referralSources = criteria.list();
			tx.commit();
			
			return referralSources;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch Consumer ReferralSources.", ex);
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
	public List<ReferralSource> fetchForAdmin() throws Exception {
		List<ReferralSource> referralSources = new ArrayList<ReferralSource>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ReferralSource.class)
				.add(Restrictions.eq("displayInAdmin", true));
			referralSources = criteria.list();
			tx.commit();
			
			return referralSources;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch Admin ReferralSources.", ex);
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
}
