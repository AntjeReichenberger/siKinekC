package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.HibernateSessionUtil;
import org.webdev.kpoint.bl.pojo.Message;

public class MessageDao extends BaseDao implements GenericDao<Message, Integer> {

	private static final KinekLogger logger = new KinekLogger(MessageDao.class);

	@Override
	public Integer create(Message newInstance) throws Exception {
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
			logProps.put("MessageContents", newInstance.getContents());
			ApplicationException aex = new ApplicationException("Could not create message.", ex);
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


	public Message read(String transactionNumber) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object message = session.get(Message.class, transactionNumber);
			tx.commit();
			
			return (Message)message;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("TransactionNumber", transactionNumber);
			ApplicationException aex = new ApplicationException("Could not read message.", ex);
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
	public Message read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object message = session.get(Message.class, id);
			tx.commit();
				
			return (Message)message;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("MessageID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read message.", ex);
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
	 * Retrieves a list of all Messages
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Message> fetch(int mediumId, int triggerId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Message.class)
				.add(Restrictions.isNotNull("id"))
				.createAlias("medium", "m")
				.createAlias("trigger", "t");
				
				if (mediumId != 0) {
					criteria.add(Restrictions.eq("m.id", mediumId));
				}
				
				if (triggerId != 0) {
					criteria.add(Restrictions.eq("t.id", triggerId));
				}
				
			List<Message> messages = criteria.list();
			tx.commit();
	
			return messages;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("MediumID", String.valueOf(mediumId));
			logProps.put("TriggerID", String.valueOf(triggerId));
			ApplicationException aex = new ApplicationException("Could not fetch messages.", ex);
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
	 * Retrieves a list of Messages that match the provided Medium and Trigger 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Message> fetch() throws Exception {
		List<Message> messages = new ArrayList<Message>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Message.class);
			messages = criteria.list();
			tx.commit();
			
			return messages;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch messages.", ex);
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
	public void update(Message transientObject) throws Exception {
		// TODO Complete the update() method on MessageDAO	
	}

	@Override
	public void delete(Message persistentObject) throws Exception {
		// TODO Complete the delete() method on MessageDAO
	}
}
