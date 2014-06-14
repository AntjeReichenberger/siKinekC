package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.KinekPointHistory;


public class KinekPointHistoryDao extends BaseDao implements GenericDao<KinekPointHistory, Integer>{

	private static final KinekLogger logger = new KinekLogger(KinekPointHistoryDao.class);

	@Override
	public Integer create(KinekPointHistory newInstance) throws Exception {
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
			logProps.put("KPHistory Name", newInstance.getName());
			ApplicationException aex = new ApplicationException("Unable to create a new Kinek Point History record.", ex);
			logger.error(aex, logProps);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		return newInstance.getDepotId();
	}
	
	@Override
	public void delete(KinekPointHistory persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override @SuppressWarnings("unchecked")
	public List<KinekPointHistory> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(KinekPointHistory.class)
				.addOrder(Order.asc("name"));
			List<KinekPointHistory> depotHistories = criteria.list();
			tx.commit();
			
			return depotHistories;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Point History records.", ex);
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
	public KinekPointHistory read(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(KinekPointHistory transientObject) throws Exception {
		// TODO Auto-generated method stub
	}
}
