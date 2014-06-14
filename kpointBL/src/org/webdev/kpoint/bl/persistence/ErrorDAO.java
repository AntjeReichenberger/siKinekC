package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.Error;

public class ErrorDAO extends BaseDao implements GenericDao<Error, Integer> {

	private static final KinekLogger logger = new KinekLogger(ErrorDAO.class);

	@Override
	public Integer create(Error newInstance) throws Exception {
		return null;
	}

	@Override
	public void delete(Error persistentObject) throws Exception {
		// TODO Auto-generated method stub		
	}

	@Override
	public Error read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object error = session.get(Error.class, id);
			tx.commit();
				
			return (Error)error;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ErrorID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Error.", ex);
			logger.error(aex, logProps);
		
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
	
		
	/**
	 * Retrieves a row from error table by errorCode.
	 * Returns null if none exist.
	 * @param errorCode The user name to match against
	 * @return Return a row of the error table; null if none exist
	 */
	@SuppressWarnings("unchecked")
	public Error read(String errorCode) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Error.class)
				.add(Restrictions.eq("errorCode", errorCode))
				.setMaxResults(1);
			List<Error> errorList = criteria.list();
			tx.commit();
			
			Error error = errorList.size() > 0 ? errorList.get(0) : null;  
			
			return error;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ErrorCode", errorCode);
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested error.", ex);
			logger.error(aex, logProps);
		
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
	public void update(Error transientObject) throws Exception {
	}

	@Override
	public List<Error> fetch() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
