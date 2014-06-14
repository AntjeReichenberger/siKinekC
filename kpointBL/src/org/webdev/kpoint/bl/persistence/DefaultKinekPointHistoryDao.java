package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.DefaultKinekPointHistory;


public class DefaultKinekPointHistoryDao extends BaseDao implements GenericDao<DefaultKinekPointHistory, Integer> {
	
	private static final KinekLogger logger = new KinekLogger(DefaultKinekPointHistoryDao.class);
	
	@Override
	public Integer create(DefaultKinekPointHistory newInstance) throws Exception {
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
			logProps.put("KinekPointHistory UserID", String.valueOf(newInstance.getUserId()));
			ApplicationException aex = new ApplicationException("Unable to create a new Default KinekPoint History record.", ex);
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
	public void delete(DefaultKinekPointHistory persistentObject) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override @SuppressWarnings("unchecked")
	public List<DefaultKinekPointHistory> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(DefaultKinekPointHistory.class);
			List<DefaultKinekPointHistory> defaultKinekPointHistories = criteria.list();
			tx.commit();
			
			return defaultKinekPointHistories;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of KinekPoint History records.", ex);
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
	public DefaultKinekPointHistory read(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(DefaultKinekPointHistory transientObject) throws Exception {
		// TODO Auto-generated method stub
	}
}
