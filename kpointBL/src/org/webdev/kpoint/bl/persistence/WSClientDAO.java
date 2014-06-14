package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.WSClient;

public class WSClientDAO extends BaseDao implements GenericDao<WSClient, Integer> {

	private static final KinekLogger logger = new KinekLogger(WSClientDAO.class);

	@Override
	public Integer create(WSClient newInstance) throws Exception {
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
			logProps.put("Username", newInstance.getUsername());
			ApplicationException aex = new ApplicationException("Could not create WS Client.", ex);
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
	public void delete(WSClient persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WSClient read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object client = session.get(WSClient.class, id);
			tx.commit();
				
			return (WSClient)client;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("UserID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read WS Client.", ex);
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
	 * Retrieves a web services client based matching the provided user name, password and host.
	 * Returns null if none exist.
	 * @param username The user name to match against
	 * @param password The password to match against
	 * @return The client matching the provided user name and password; null if none exist
	 */
	@SuppressWarnings("unchecked")
	public WSClient read(String username, String password, String hostname) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(WSClient.class)
				.add(Restrictions.eq("username", username))
				.add(Restrictions.eq("password", password))
				.add(Restrictions.eq("host", hostname))
				.setMaxResults(1);
			List<WSClient> clients = criteria.list();
			tx.commit();
			
			WSClient client = clients.size() > 0 ? clients.get(0) : null;  
			
			return client;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Username", username);
			logProps.put("Hostname", hostname);
			ApplicationException aex = new ApplicationException("Could not read WS Client.", ex);
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
	 * Retrieves a web services client based matching the provided user name and password.
	 * Returns null if none exist.
	 * @param username The user name to match against
	 * @param password The password to match against
	 * @return The client matching the provided user name and password; null if none exist
	 */
	@SuppressWarnings("unchecked")
	public WSClient read(String username, String password) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(WSClient.class)
				.add(Restrictions.eq("username", username))
				.add(Restrictions.eq("password", password))
				.setMaxResults(1);
			List<WSClient> clients = criteria.list();
			tx.commit();
			
			WSClient client = clients.size() > 0 ? clients.get(0) : null;  
			
			return client;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Username", username);
			ApplicationException aex = new ApplicationException("Could not read WS Client.", ex);
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
	public void update(WSClient transientObject) throws Exception {
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
			logProps.put("Username", transientObject.getUsername());
			ApplicationException aex = new ApplicationException("Could not update WS Client.", ex);
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
	public List<WSClient> fetch() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
