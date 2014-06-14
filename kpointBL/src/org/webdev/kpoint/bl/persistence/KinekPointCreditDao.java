package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;
import java.util.GregorianCalendar;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.util.ApplicationProperty;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.KinekPointCredit;
import org.webdev.kpoint.bl.pojo.CreditStatus;
import org.webdev.kpoint.bl.pojo.CreditIssueReason;

public class KinekPointCreditDao extends BaseDao implements GenericDao<KinekPointCredit, Integer> {
	
	private static final KinekLogger logger = new KinekLogger(KinekPointCreditDao.class);
	
	@Override
	public Integer create(KinekPointCredit newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
		} catch (Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create a new Kinek Point Credit.", ex);
			logger.error(aex);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		} finally {
			verifyDBState(logger, session, tx);
		}
		
		return newInstance.getId();
	}
	
	@Override
	public void delete(KinekPointCredit persistentObject) throws Exception {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void update(KinekPointCredit transientObject) throws Exception {
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
			logProps.put("KPCreditID", String.valueOf(transientObject.getId()));
			ApplicationException aex = new ApplicationException("Unable to update a Kinek Point Credit.", ex);
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
	public List<KinekPointCredit> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(KinekPointCredit.class);
			List<KinekPointCredit> depotCredits = criteria.list();
			tx.commit();
			
			return depotCredits;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Point Credits.", ex);
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
	 * Retrieves all depot credits used on the given invoice number
	 * @return All consumer credits
	 */
	@SuppressWarnings("unchecked")
	public List<KinekPointCredit> fetch(String invoiceNumber) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(KinekPointCredit.class)
				.createAlias("redemptionInvoice", "i")
				.add(Restrictions.eq("i.invoiceNumber", invoiceNumber));
			List<KinekPointCredit> consumerCredits = criteria.list();
			tx.commit();
	
			return consumerCredits;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("InvoiceNumber", invoiceNumber);
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Point Credits for the given invoice.", ex);
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
	 * Retrieves all depot credits with a given issue-Reason and status
	 * @return A list of depot credits
	 */
	@SuppressWarnings("unchecked")
	public List<KinekPointCredit> fetch(CreditIssueReason selectedReason, CreditStatus selectedStatus) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(KinekPointCredit.class)
				.add(Restrictions.isNotNull("id"));
				
				if (selectedReason != null) {
					criteria.add(Restrictions.eq("issueReason", selectedReason));
				}
				
				if (selectedStatus != null) {
					criteria.add(Restrictions.eq("creditStatus", selectedStatus));
				}
				
			List<KinekPointCredit> depotCredits = criteria.list();
			tx.commit();
	
			return depotCredits;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CreditIssueReasonID", String.valueOf(selectedReason.getId()));
			logProps.put("CreditStatusID", String.valueOf(selectedStatus.getId()));
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Point Credits for the selected reason and status.", ex);
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
	 * Retrieves all consumer credits
	 * @return All consumer credits
	 */
	@SuppressWarnings("unchecked")
	public List<KinekPointCredit> fetchUnused(int depotId, Date endDate) throws Exception {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(endDate);
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(KinekPointCredit.class)
				.createAlias("creditStatus", "s")
				.createAlias("depot", "d")
				.add(Restrictions.eq("d.depotId", depotId))
				.add(Restrictions.eq("s.status", ApplicationProperty.getInstance().getProperty("credit.status.available")))
				.add(Restrictions.le("issueDate", endDate))
				.addOrder(Order.asc("issueDate"));
			List<KinekPointCredit> depotCredits = criteria.list();
			tx.commit();
	
			return depotCredits;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("DepotID", String.valueOf(depotId));
			logProps.put("EndDate", endDate.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Point Credits for the given Kinek Point.", ex);
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
	public KinekPointCredit read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object depotCredit = session.get(KinekPointCredit.class, id);
			tx.commit();
			
			return (KinekPointCredit)depotCredit;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KPCreditID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Kinek Point Credit.", ex);
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
