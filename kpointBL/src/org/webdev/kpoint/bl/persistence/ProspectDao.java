package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;
import java.util.Calendar;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.Prospect;


public class ProspectDao extends BaseDao implements GenericDao<Prospect, Integer> {

	private static final KinekLogger logger = new KinekLogger(ProspectDao.class);
	
	@Override
	public Integer create(Prospect newInstance) throws Exception {
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
			logProps.put("Email", newInstance.getEmail());
			ApplicationException aex = new ApplicationException("Could not create Prospect.", ex);
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
	public void delete(Prospect persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Retrieves all promotions
	 * @return All promotions
	 */
	@SuppressWarnings("unchecked")
	public List<Prospect> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Prospect.class)
				.add(Restrictions.isNotNull("id"));
			List<Prospect> prospects = criteria.list();
			tx.commit();
	
			return prospects;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch Prospects.", ex);
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
	 * Retrieves all promotions with Referral dates that fall within a provided date range
	 * @return A list of promotions
	 */
	@SuppressWarnings("unchecked")
	public List<Prospect> fetchByReferralDate(Calendar minReferralDate, Calendar maxReferralDate) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Prospect.class)
				.add(Restrictions.isNotNull("id"))
				.add(Restrictions.isNotNull("referralDate"))
				.add(Restrictions.ge("referralDate", minReferralDate))
				.add(Restrictions.le("referralDate", maxReferralDate))
				.addOrder(Order.asc("referralDate"));
			List<Prospect> prospects = criteria.list();
			tx.commit();
	
			return prospects;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ReferralStartDate", minReferralDate.toString());
			logProps.put("ReferralEndDate", maxReferralDate.toString());
			ApplicationException aex = new ApplicationException("Could not retrieve Prospects based on referral date.", ex);
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
	 * Retrieves all promotions with Conversion dates that fall within a provided date range
	 * @return A list of promotions
	 */
	@SuppressWarnings("unchecked")
	public List<Prospect> fetchByConversionDate(Calendar minConversionDate, Calendar maxConversionDate) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Prospect.class)
				.add(Restrictions.isNotNull("id"))
				.add(Restrictions.isNotNull("conversionDate"))
				.add(Restrictions.ge("conversionDate", minConversionDate))
				.add(Restrictions.le("conversionDate", maxConversionDate))
				.addOrder(Order.asc("conversionDate"));
			List<Prospect> prospects = criteria.list();
			tx.commit();
	
			return prospects;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ConversionStartDate", minConversionDate.toString());
			logProps.put("ConversionEndDate", maxConversionDate.toString());
			ApplicationException aex = new ApplicationException("Could not retrieve Prospects based on conversion date.", ex);
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
	public Prospect read(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Prospect read(String email) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Prospect.class)
				.add(Restrictions.eq("email", email))
				.addOrder(Order.asc("referralDate"))
				.setMaxResults(1);
			List<Prospect> prospects = criteria.list();
			tx.commit();
			
			Prospect prospect = prospects.size() > 0 ? prospects.get(0) : null;
			
			return prospect;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Email", email);
			ApplicationException aex = new ApplicationException("Could not read Prospect.", ex);
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
	public void update(Prospect transientObject) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.update(transientObject);
			tx.commit();
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ProspectID", String.valueOf(transientObject.getId()));
			ApplicationException aex = new ApplicationException("Could not update Prospect.", ex);
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
}
