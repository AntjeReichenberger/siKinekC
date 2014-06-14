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
import org.webdev.kpoint.bl.pojo.MessageMedia;


public class MessageMediaDao extends BaseDao implements GenericDao<MessageMedia, Integer> {

	private static final KinekLogger logger = new KinekLogger(MessageMediaDao.class);

	@Override
	public Integer create(MessageMedia newInstance) throws Exception {
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
			logProps.put("MessageMedia Name", newInstance.getName());
			ApplicationException aex = new ApplicationException("Could not create message media.", ex);
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

	


	public MessageMedia read(String transactionNumber) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object messageMedia = session.get(MessageMedia.class, transactionNumber);
			tx.commit();
			
			return (MessageMedia)messageMedia;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("TransactionNumber", transactionNumber);
			ApplicationException aex = new ApplicationException("Could not read message media.", ex);
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
	public MessageMedia read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object messageMedia = session.get(MessageMedia.class, id);
			tx.commit();
				
			return (MessageMedia)messageMedia;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("MessageMediaID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read message media.", ex);
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
	 * Retrieves a list of all MessageMedia
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MessageMedia> fetch() throws Exception {
		List<MessageMedia> messageMedias = new ArrayList<MessageMedia>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(MessageMedia.class);
			messageMedias = criteria.list();
			tx.commit();
			
			return messageMedias;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch message medias.", ex);
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
	public void update(MessageMedia transientObject) throws Exception {
		// TODO Complete the update() method on MessageMediaDAO	
	}
	@Override
	public void delete(MessageMedia persistentObject) throws Exception {
		// TODO Complete the delete() method on MessageMediaDAO
	}

}
