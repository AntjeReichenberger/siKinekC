package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;
import java.util.Calendar;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.Promotion;
import org.webdev.kpoint.bl.util.CalendarUtil;


public class PromotionDao extends BaseDao implements GenericDao<Promotion, Integer> {
	
	private static final KinekLogger logger = new KinekLogger(PromotionDao.class);
	
	@Override
	public Integer create(Promotion newInstance) throws Exception {
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
			logProps.put("PromoCode", newInstance.getCode());
			ApplicationException aex = new ApplicationException("Could not create new Promotion.", ex);
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
	public void delete(Promotion persistentObject) throws Exception {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void update(Promotion transientObject) throws Exception {
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
			logProps.put("PromoCode", String.valueOf(transientObject.getId()));
			ApplicationException aex = new ApplicationException("Could not update the Promotion.", ex);
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
	
	@Override @SuppressWarnings("unchecked")
	public List<Promotion> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Promotion.class);
			List<Promotion> promotions = criteria.list();
			tx.commit();
			
			return promotions;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch Promotions.", ex);
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
	 * Retrieves all promotions due to expire between the two provided dates (inclusive).
	 * Times are ignored.
	 * @param expiryStart
	 * @param expiryEnd
	 * @return List of all promotions due to expire between the two provided dates (inclusive).
	 */
	@SuppressWarnings("unchecked")
	public List<Promotion> fetch(Calendar expiryStart, Calendar expiryEnd) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Promotion.class)
				.add(Restrictions.ge("endDate", new CalendarUtil(expiryStart).getStartOfDay()))
				.add(Restrictions.le("endDate", new CalendarUtil(expiryEnd).getEndOfDay()));
			List<Promotion> promotions = criteria.list();
			tx.commit();
	
			return promotions;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ExpiryStartDate", expiryStart.toString());
			logProps.put("ExpiryEndDate", expiryEnd.toString());
			ApplicationException aex = new ApplicationException("Could not retrieve Promotions.", ex);
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
	public Promotion read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object promotion = session.get(Promotion.class, id);
			tx.commit();
			
			return (Promotion)promotion;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("PromoID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read Promotion.", ex);
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
	
	@SuppressWarnings("unchecked")
	public Promotion read(String promotionCode) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Promotion.class)
				.add(Restrictions.eq("code", promotionCode))
				.setMaxResults(1);
			List<Promotion> promotions = criteria.list();
			tx.commit();
			Promotion promotion = promotions.size() > 0 ? promotions.get(0) : null;
			
			return promotion;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("PromoCode", promotionCode);
			ApplicationException aex = new ApplicationException("Could not read Promotion.", ex);
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
