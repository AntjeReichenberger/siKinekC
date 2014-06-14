package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.HibernateSessionUtil;
import org.webdev.kpoint.bl.pojo.MessageTrigger;


public class MessageTriggerDao extends BaseDao implements GenericDao<MessageTrigger, Integer> {

	private static final KinekLogger logger = new KinekLogger(MessageTriggerDao.class);

	@Override
	public Integer create(MessageTrigger newInstance) throws Exception {
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
			logProps.put("MessageTrigger Name", newInstance.getName());
			ApplicationException aex = new ApplicationException("Could not create message trigger.", ex);
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
	

	public MessageTrigger read(String transactionNumber) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object messageTrigger = session.get(MessageTrigger.class, transactionNumber);
			tx.commit();
			
			return (MessageTrigger)messageTrigger;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("TransactionNumber", transactionNumber);
			ApplicationException aex = new ApplicationException("Could not read message trigger.", ex);
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
	public MessageTrigger read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object messageTrigger = session.get(MessageTrigger.class, id);
			tx.commit();
				
			return (MessageTrigger)messageTrigger;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("MessageTriggerID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read message trigger.", ex);
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
	 * Retrieves a list of all MessageTriggers
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MessageTrigger> fetch() throws Exception {
		List<MessageTrigger> messageTriggers = new ArrayList<MessageTrigger>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(MessageTrigger.class);
			messageTriggers = criteria.list();
			tx.commit();
			
			return messageTriggers;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch message triggers.", ex);
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
	public void delete(MessageTrigger persistentObject) throws Exception {
		// TODO Complete the delete() method on MessageTriggerDAO 
	}
	
	
	@Override
	public void update(MessageTrigger transientObject) throws Exception {
		// TODO Complete the update() method on MessageTriggerDAO
	}
}
