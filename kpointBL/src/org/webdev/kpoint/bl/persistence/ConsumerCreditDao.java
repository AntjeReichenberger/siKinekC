package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.ConsumerCredit;
import org.webdev.kpoint.bl.pojo.CreditIssueReason;
import org.webdev.kpoint.bl.pojo.CreditStatus;
import org.webdev.kpoint.bl.pojo.Page;
import org.webdev.kpoint.bl.pojo.Pickup;
import org.webdev.kpoint.bl.pojo.Promotion;
import org.webdev.kpoint.bl.pojo.User;

public class ConsumerCreditDao extends BaseDao implements GenericDao<ConsumerCredit, Integer> {
	
	private static final KinekLogger logger = new KinekLogger(ConsumerCreditDao.class);
	
	@Override
	public Integer create(ConsumerCredit newInstance) throws Exception{
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
			logProps.put("ConsumerCredit User", newInstance.getUser().getUsername());
			ApplicationException aex = new ApplicationException("Unable to create a new consumer credit.", ex);
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
	public void delete(ConsumerCredit persistentObject) throws Exception {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void update(ConsumerCredit transientObject) throws Exception {
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
			logProps.put("ConsumerCredit", String.valueOf(transientObject.getId()));
			ApplicationException aex = new ApplicationException("Unable to update a consumer credit.", ex);
			logger.error(aex, logProps);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally {
			verifyDBState(logger, session, tx);
		}
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<ConsumerCredit> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ConsumerCredit.class);
			List<ConsumerCredit> consumerCredits = criteria.list();
			tx.commit();
			
			return consumerCredits;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of consumer credits.", ex);
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
	public List<ConsumerCredit> fetch(Integer userId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ConsumerCredit.class)
				.addOrder(Order.desc("issueDate"))
				.createCriteria("user")
				.add(Restrictions.eq("userId", userId));
			List<ConsumerCredit> consumerCredit = criteria.list();
			tx.commit();
			
			return consumerCredit;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("UserID", userId.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of consumer credits for the given user.", ex);
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
	 * Retrieves all consumer credits based on status  
	 * @return All consumer credits
	 */
	@SuppressWarnings("unchecked")
	public List<ConsumerCredit> fetch(CreditStatus status) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ConsumerCredit.class)
				.add(Restrictions.eq("creditStatus", status));
			List<ConsumerCredit> consumerCredits = criteria.list();
			tx.commit();
	
			return consumerCredits;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CreditStatusID", String.valueOf(status.getId()));
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of consumer credits with the given status.", ex);
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
	public List<ConsumerCredit> fetch(User consumer, CreditStatus status) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ConsumerCredit.class)
				.add(Restrictions.eq("user", consumer))
				.add(Restrictions.eq("creditStatus", status));
			List<ConsumerCredit> credits = criteria.list();
			tx.commit();
			
			return credits;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Username", consumer.getUsername());
			logProps.put("CreditStatusID", String.valueOf(status.getId()));
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of consumer credits for the given consumer with the given status.", ex);
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
	public List<ConsumerCredit> fetchByIds(List<Integer> ids) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ConsumerCredit.class)
				.add(Restrictions.in("id", ids));
			List<ConsumerCredit> credits = criteria.list();
			tx.commit();
			
			return credits;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested consumer credits.", ex);
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
	 * Retrieves all consumer credits with a given issue-Reason and status
	 * @return A list of consumer credits
	 */
	@SuppressWarnings("unchecked")
	public List<ConsumerCredit> fetch(CreditIssueReason selectedReason, CreditStatus selectedStatus) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ConsumerCredit.class)
				.add(Restrictions.isNotNull("id"));
				
				if (selectedReason != null) {
					criteria.add(Restrictions.eq("issueReason", selectedReason));
				}
				
				if (selectedStatus != null) {
					criteria.add(Restrictions.eq("creditStatus", selectedStatus));
				}
				
			List<ConsumerCredit> consumerCredits = criteria.list();
			tx.commit();
	
			return consumerCredits;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CreditIssueReason", String.valueOf(selectedReason.getId()));
			logProps.put("CreditStatus", String.valueOf(selectedStatus.getId()));
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of consumer credits with the given reason and status.", ex);
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
	 * Retrieves all consumer credits belonging to the provided promotion 
	 * which have not yet been notified that their promotion is about to expire.
	 * @return All consumer credits
	 */
	@SuppressWarnings("unchecked")
	public List<ConsumerCredit> fetchNonNotified(Promotion promotion, CreditStatus status) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ConsumerCredit.class)
				.add(Restrictions.eq("promotion", promotion))
				.add(Restrictions.eq("creditStatus", status))
				.add(Restrictions.isNull("expiryNotificationDate"));
			List<ConsumerCredit> consumerCredits = criteria.list();
			tx.commit();
	
			return consumerCredits;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("PromotionID", String.valueOf(promotion.getId()));
			logProps.put("CreditStatusID", String.valueOf(status.getId()));
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of consumer credits with the given promotion and status.", ex);
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
	public List<ConsumerCredit> fetchByPromotion(Integer promotionId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ConsumerCredit.class)
				.createCriteria("promotion")
				.add(Restrictions.eq("id", promotionId));
			List<ConsumerCredit> consumerCredit = criteria.list();
			tx.commit();
			
			return consumerCredit;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("PromotionID", promotionId.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of consumer credits for the given promotion.", ex);
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
	 * Retrieves consumer credits that have been applied to one of the given Pickups
	 * @param pickups Pickups to check for credits used against
	 * @return The credits used against the given Pickups
	 */
	@SuppressWarnings("unchecked")
	public List<ConsumerCredit> fetchByPickups(List<Pickup> pickups) throws Exception {		
		List<Integer> pickupIds = new ArrayList<Integer>();
		for(Pickup pickup : pickups) {
			pickupIds.add(pickup.getPickupId());
		}
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
		
			Criteria criteria = session.createCriteria(ConsumerCredit.class)
				.createAlias("pickup", "p");
			if (pickupIds.size() > 0)
				criteria.add(Restrictions.in("p.pickupId", pickupIds));
			
			List<ConsumerCredit> consumerCredits = criteria.list();
			tx.commit();
			
			return consumerCredits;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of consumer credits that were used for the given pickups.", ex);
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
	public Page<ConsumerCredit> fetchPage(Integer userId, int pageNumber, int pageSize) throws Exception {
		//build basic query
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ConsumerCredit.class)
				.addOrder(Order.desc("issueDate"))
				.createCriteria("user")
				.add(Restrictions.eq("userId", userId));
			
			//set specific page of data
			criteria.setFirstResult(pageNumber * pageSize);
			criteria.setMaxResults(pageSize);
			
			List<ConsumerCredit> consumerCredits = criteria.list();
			tx.commit();
			
			Page<ConsumerCredit> page = new Page<ConsumerCredit>();
			page.setPageSize(pageSize);
			page.setPage(pageNumber);
			page.setResults(consumerCredits);
			
			return page;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("UserID", userId.toString());
			logProps.put("PageNumber", String.valueOf(pageNumber));
			logProps.put("PageSize", String.valueOf(pageSize));
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested page of consumer credits.", ex);
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
	public ConsumerCredit read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object consumerCredit = session.get(ConsumerCredit.class, id);
			tx.commit();
			
			return (ConsumerCredit)consumerCredit;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ConsumerCreditID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested consumer credit.", ex);
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
