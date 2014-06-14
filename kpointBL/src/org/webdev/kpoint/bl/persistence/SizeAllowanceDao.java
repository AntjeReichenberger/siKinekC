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
import org.webdev.kpoint.bl.pojo.SizeAllowance;

public class SizeAllowanceDao extends BaseDao implements GenericDao<SizeAllowance, Integer> {

	private static final KinekLogger logger = new KinekLogger(SizeAllowanceDao.class);
	
	@Override
	public Integer create(SizeAllowance newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(SizeAllowance persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Retrieves the Size Allowance specified by the provided unique Size Allowance id
	 * @return The Size Allowance specified by the provided Id if it exists; null otherwise 
	 */
	@Override
	public SizeAllowance read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object sizeAllowance = session.get(SizeAllowance.class, id);
			tx.commit();
				
			return (SizeAllowance)sizeAllowance;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("SizeAllowanceID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read SizeAllowance.", ex);
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
	public void update(SizeAllowance transientObject) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Retrieves a list of all package Size Allowances
	 */
	@Override @SuppressWarnings("unchecked")
	public List<SizeAllowance> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(SizeAllowance.class);
			List<SizeAllowance> sizeAllowances = criteria.list();
			tx.commit();
				
			return sizeAllowances;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch SizeAllowances.", ex);
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
	 * Retrieves a Map of all available Size Allowances
	 * @return A Map of all available Size Allowances
	 */
	public Map<Integer, SizeAllowance> fetchMap() throws Exception {
		List<SizeAllowance> list = fetch();
		
		Map<Integer, SizeAllowance> sizeAllowances = new HashMap<Integer, SizeAllowance>();
		for (SizeAllowance sizeAllowance : list) {
			sizeAllowances.put(sizeAllowance.getSizeAllowanceId(), sizeAllowance);
		}
		return sizeAllowances;
	}
}
