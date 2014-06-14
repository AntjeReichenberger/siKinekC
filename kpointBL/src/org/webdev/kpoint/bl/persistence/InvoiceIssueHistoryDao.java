package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.Invoice;
import org.webdev.kpoint.bl.pojo.InvoiceIssueHistory;


public class InvoiceIssueHistoryDao extends BaseDao implements GenericDao<InvoiceIssueHistory, Integer> {

	private static final KinekLogger logger = new KinekLogger(InvoiceIssueHistoryDao.class);
	
	@Override
	public Integer create(InvoiceIssueHistory newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create a new Invoice Issue History record.", ex);
			logger.error(aex);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		return newInstance.getInvoiceIssueHistoryId();
	}

	@Override
	public void delete(InvoiceIssueHistory persistentObject) throws Exception {		
	}


    /**
     * Retrieve the invoiceissuehistory record corresponding to the given invoice number 
     * @param invoiceNumber The id of the object to retrieve
     * @return The object matching the supplied invoiceNumber; null if none exist
     */
	public InvoiceIssueHistory read(Integer invoiceIssueHistoryId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object history = session.get(InvoiceIssueHistory.class, invoiceIssueHistoryId);
			tx.commit();
			
			return (InvoiceIssueHistory)history;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ID", invoiceIssueHistoryId.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Invoice Issue History record.", ex);
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
	public void update(InvoiceIssueHistory transientObject) throws Exception {
	}
	
	/**
	 * Retrieves a list of all supported countries
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InvoiceIssueHistory> fetch() throws Exception{
		List<InvoiceIssueHistory> invoices = new ArrayList<InvoiceIssueHistory>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(InvoiceIssueHistory.class);
			invoices = criteria.list();
			tx.commit();
			
			return invoices;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Invoice Issue History records.", ex);
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
     * Retrieve the invoiceissuehistory records corresponding to the month 
     * starting with the given startDate
     * @param startDate The first day of the month to retrieve records for
     * @return The object matching the supplied invoiceNumber; null if none exist
     */
	@SuppressWarnings("unchecked")
	public List<InvoiceIssueHistory> fetchByMonth(Date startDate) throws Exception {
		GregorianCalendar endgc = new GregorianCalendar();
		endgc.setTime(startDate);
		endgc.add(Calendar.MONTH, 1);
		Date endDate = endgc.getTime();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(InvoiceIssueHistory.class)
				.createAlias("invoice", "i")
				.add(Restrictions.ge("i.startDate", startDate))
				.add(Restrictions.lt("i.endDate", endDate));
			List<InvoiceIssueHistory> records = criteria.list();
			tx.commit();
			
			return records;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("StartDate", startDate.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Invoice Issue History records.", ex);
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
     * Retrieve the invoiceissuehistory records corresponding to the month 
     * starting with the given startDate
     * @param startDate The first day of the month to retrieve records for
     * @param depotId The id of the depot to look for
     * @return The object matching the supplied invoiceNumber; null if none exist
     */
	@SuppressWarnings("unchecked")
	public List<InvoiceIssueHistory> fetchByMonth(Date startDate, int depotId) throws Exception {
		GregorianCalendar endgc = new GregorianCalendar();
		endgc.setTime(startDate);
		endgc.add(Calendar.MONTH, 1);
		Date endDate = endgc.getTime();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(InvoiceIssueHistory.class)
				.createAlias("invoice", "i")
				.add(Restrictions.ge("i.startDate", startDate))
				.add(Restrictions.lt("i.endDate", endDate))
				.add(Restrictions.eq("i.depotId", depotId));
			List<InvoiceIssueHistory> records = criteria.list();
			tx.commit();
			
			return records;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("StartDate", startDate.toString());
			logProps.put("DepotID", String.valueOf(depotId));
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Invoice Issue History records for the given Depot.", ex);
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
	 * Fetches the invoice numbers of all invoice issue history records
	 * @return The array of numbers
	 */
	@SuppressWarnings("unchecked")
	public List fetchInvoiceNumbers() throws Exception {
		String hql = "select history.invoice.invoiceNumber from InvoiceIssueHistory history";	
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();		Query query = session.createQuery(hql);
			List numbers = query.list();		
			tx.commit();
	
			return numbers;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Invoice Issue History numbers.", ex);
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
	public Date getLastIssuedDate(Invoice inv) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(InvoiceIssueHistory.class)
				.createAlias("invoice", "i")
				.add(Restrictions.eq("i.invoiceNumber", inv.getInvoiceNumber()))
				.addOrder(Order.desc("issueDate"));
			List<InvoiceIssueHistory> records = criteria.list();
			
			tx.commit();
			
			Date last = null;
			if(records != null && records.size() > 0)
				last = records.get(0).getIssueDate();
			
			return last;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("InvoiceNumber", inv.getInvoiceNumber());
			ApplicationException aex = new ApplicationException("Unable to retrieve the last issued date for the given invoice.", ex);
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
