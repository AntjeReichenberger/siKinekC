package org.webdev.kpoint.bl.persistence;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.DeviceToken;

public class DeviceTokenDao extends BaseDao implements GenericDao<DeviceToken, Integer> {

	private static final KinekLogger logger = new KinekLogger(DeviceTokenDao.class);

	@Override
	public Integer create(DeviceToken newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create new device token record.", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}	
		return newInstance.getId();
	}

	@Override
	public void delete(DeviceToken persistentObject) throws Exception {
		Session session = null;
		Transaction tx = null;
		
		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			session.delete(persistentObject);
			tx.commit();
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to delete device token", ex);
			logger.error(aex);
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}	
	}

	@Override
	public DeviceToken read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		DeviceToken token = null;		
		
		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(DeviceToken.class)
									   .add(Restrictions.eq("id", id));
			
			List data = criteria.list();
			if(data != null && data.size() > 0)
				token = (DeviceToken)data.get(0);
						
			tx.commit();
			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve device token", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
		
		return token;
	}

	public DeviceToken fetch(String tokenVal) throws Exception {
		Session session = null;
		Transaction tx = null;
		DeviceToken token = null;		
		
		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(DeviceToken.class)
									   .add(Restrictions.eq("token", tokenVal));
			
			List data = criteria.list();
			if(data != null && data.size() > 0)
				token = (DeviceToken)data.get(0);
						
			tx.commit();
			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve device token", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
		
		return token;
	}
	
	public List<DeviceToken> fetch(int userId) throws Exception {
		Session session = null;
		Transaction tx = null;
		List<DeviceToken> data = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(DeviceToken.class)
									   .add(Restrictions.eq("userId", userId));
			
			data = criteria.list();
			tx.commit();
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve device tokens", ex);
			logger.error(aex);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
		
		return data;
	}

	@Override
	public void update(DeviceToken transientObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DeviceToken> fetch() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
