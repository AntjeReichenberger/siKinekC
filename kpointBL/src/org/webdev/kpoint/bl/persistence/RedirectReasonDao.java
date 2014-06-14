package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.RedirectReason;


public class RedirectReasonDao extends BaseDao implements GenericDao<RedirectReason, Integer> {

	private static final KinekLogger logger = new KinekLogger(RedirectReasonDao.class);

	@Override
	public Integer create(RedirectReason newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(RedirectReason persistentObject) throws Exception {
		// TODO Auto-generated method stub	
	}

	/**
	 * Fetches a redirect reason matching the supplied id
	 * @param id The id of the redirect reason to find 
	 * @return The redirect reason matching the supplied id
	 */
	public RedirectReason read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object redirectReason = session.get(RedirectReason.class, id);
			tx.commit();
				
			return (RedirectReason)redirectReason;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("RedirectReasonID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read RedirectReason.", ex);
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
	public void update(RedirectReason transientObject) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Fetches all Redirect Reasons
	 * @return a list of all redirect reasons
	 */
	@SuppressWarnings("unchecked")
	public List<RedirectReason> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(RedirectReason.class);
			List<RedirectReason> reasons = criteria.list();
			tx.commit();
			
			return reasons;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch RedirectReasons.", ex);
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
}
