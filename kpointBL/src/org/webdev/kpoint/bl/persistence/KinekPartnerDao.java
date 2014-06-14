package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.CountryAreaCode;
import org.webdev.kpoint.bl.pojo.KinekPartner;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.User;

public class KinekPartnerDao extends BaseDao implements GenericDao<KinekPartner, Integer> {

	private static final KinekLogger logger = new KinekLogger(KinekPartnerDao.class);
	
	@Override
	public Integer create(KinekPartner newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(KinekPartner persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void update(KinekPartner transientObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public KinekPartner read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			KinekPartner partner = (KinekPartner)session.get(KinekPartner.class, id);
			tx.commit();
			
			return (KinekPartner)partner;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("id", id.toString());
			ApplicationException aex = new ApplicationException("Could not read KPPartner.", ex);
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
	 * Retrieves an enabled kppartner matching the provided token and password; null if none exist
	 * @param token The token to match against
	 * @param password The password to match against
	 * @return The kppartner matching the provided token and password; null if none exist
	 */
	@SuppressWarnings("unchecked")
	public KinekPartner read(String token, String password) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(KinekPartner.class)
				.add(Restrictions.eq("token", token))
				.add(Restrictions.eq("password", password))
				.add(Restrictions.eq("enabled", true))
				.setMaxResults(1);
			
			List<KinekPartner> kpPartner = criteria.list();
			tx.commit();
			
			KinekPartner foundKpPartner = kpPartner.size() > 0 ? kpPartner.get(0) : null;  
			
			return foundKpPartner;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Token", token);
			ApplicationException aex = new ApplicationException("Could not read KPPartner.", ex);
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
	public List<KinekPartner> fetch() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
