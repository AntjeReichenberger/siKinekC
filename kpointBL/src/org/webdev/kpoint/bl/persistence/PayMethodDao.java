package org.webdev.kpoint.bl.persistence;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.PayMethod;


public class PayMethodDao extends BaseDao implements GenericDao<PayMethod, Integer> {

	private static final KinekLogger logger = new KinekLogger(PayMethodDao.class);
	
	@Override
	public Integer create(PayMethod newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(PayMethod persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PayMethod read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object payMethod = session.get(PayMethod.class, id);
			tx.commit();
			
			return (PayMethod)payMethod;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("PayMethodID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read pay method.", ex);
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
	public void update(PayMethod transientObject) throws Exception {
		// TODO Auto-generated method stub
	}
	
	
	/**
	 * Retrieves a list of all supported payment methods
	 * @return A list of all supported payment methods
	 */
	@Override @SuppressWarnings("unchecked")
	public List<PayMethod> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(PayMethod.class);
			List<PayMethod> methods = criteria.list();
			tx.commit();
			
			return methods;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch pay methods.", ex);
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
	
	/**
	 * Retrieves a Map of all available Payment Methods
	 * @return A Map of all available Payment Methods
	 */
	public Map<Integer, PayMethod> fetchMap() throws Exception {
		List<PayMethod> list = fetch();
		
		Map<Integer, PayMethod> payMethods = new HashMap<Integer, PayMethod>();
		for (PayMethod method : list) {
			payMethods.put(method.getPayMethodId(), method);
		}
		return payMethods;
	}
}
