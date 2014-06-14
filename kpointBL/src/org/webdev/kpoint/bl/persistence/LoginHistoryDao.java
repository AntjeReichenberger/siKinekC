package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.KinekPointHistory;
import org.webdev.kpoint.bl.pojo.LoginHistory;


public class LoginHistoryDao extends BaseDao implements GenericDao<LoginHistory, Integer>{

	private static final KinekLogger logger = new KinekLogger(LoginHistoryDao.class);

	@Override
	public Integer create(LoginHistory newInstance) throws Exception {
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
			logProps.put("User ID", String.valueOf(newInstance.getUserId()));
			logProps.put("Application", newInstance.getApplication());
			ApplicationException aex = new ApplicationException("Unable to create login history record.", ex);
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
	public void delete(LoginHistory persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override @SuppressWarnings("unchecked")
	public List<LoginHistory> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(LoginHistory.class)
				.addOrder(Order.asc("loginDate"));
			List<LoginHistory> loginHistory = criteria.list();
			tx.commit();
			
			return loginHistory;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Login records.", ex);
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
	public LoginHistory fetchLastLogin(int userId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(LoginHistory.class)
				.add(Restrictions.eq("userId", userId))
				.addOrder(Order.asc("loginDate"));
			List<LoginHistory> loginHistory = criteria.list();
			tx.commit();
			
			if(loginHistory != null && loginHistory.size() > 0)
				return loginHistory.get(loginHistory.size()-1);

			return null;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve last Login record.", ex);
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
	public LoginHistory read(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(LoginHistory transientObject) throws Exception {
		// TODO Auto-generated method stub
	}
}
