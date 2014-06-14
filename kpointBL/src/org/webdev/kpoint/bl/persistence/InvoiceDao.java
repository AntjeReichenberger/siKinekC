package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.Invoice;


public class InvoiceDao extends BaseDao implements GenericDao<Invoice, String> {

	private static final KinekLogger logger = new KinekLogger(InvoiceDao.class);
	
	@Override
	public String create(Invoice newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create a new Invoice.", ex);
			logger.error(aex);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		return newInstance.getInvoiceNumber();
	}

	@Override
	public void delete(Invoice persistentObject) throws Exception {		
	}


    /**
     * Retrieve the invoice corresponding to the given invoice number 
     * @param invoiceNumber The id of the object to retrieve
     * @return The object matching the supplied invoiceNumber; null if none exist
     */
	public Invoice read(String invoiceNumber) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object invoice = session.get(Invoice.class, invoiceNumber);
			tx.commit();
			
			return (Invoice)invoice;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Invoice Number", invoiceNumber);
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Invoice.", ex);
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
	 * Retrieves the next invoice number as an integer.
	 * @return the next invoice number or -1 if transaction fails
	 */
	@SuppressWarnings("unchecked")
	public int getNextInvoiceNumber() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Invoice.class)
				.addOrder(Order.desc("invoiceNumber"));
			List<Invoice> records = criteria.list();
			tx.commit();
			
			int next = 1000;
			if(records != null && records.size() != 0)
			{
				next = Integer.parseInt(records.get(0).getInvoiceNumber()) + 1;
			}
			
			return next;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			ApplicationException aex = new ApplicationException("Unable to retrieve the next Invoice number.", ex);
			logger.error(aex, logProps);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
				logger.info("Transaction rolled back");
				if(tx != null && tx.isActive()){
					logger.info("Transaction still open");
				}
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		//return -1;
	}

	@Override
	public void update(Invoice transientObject) throws Exception {		
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
			logProps.put("InvoiceID", String.valueOf(transientObject.getInvoiceNumber()));
			ApplicationException aex = new ApplicationException("Unable to update an Invoice.", ex);
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
	 * Retrieves a list of all supported countries
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Invoice> fetch() throws Exception {
		List<Invoice> invoices = new ArrayList<Invoice>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Invoice.class);
			invoices = criteria.list();
			tx.commit();
			
			return invoices;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Invoices.", ex);
			logger.error(aex);
		
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
	 * Retrieves the list of invoices corresponding to the given year, month
	 * and depot 
	 * @param year The year to look for. Set to 0 to find all.
	 * @param month The month to look for. Set to 0 to find all.
	 * @param depotId The depot to look for. Set to 0 to find all.
	 * @return The list of invoices
	 */
	@SuppressWarnings("unchecked")
	public List<Invoice> fetch(int year, int month, int depotId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Invoice.class)
				.createAlias("depot", "d")
				.addOrder(Order.asc("d.name"))
				.addOrder(Order.asc("startDate"));
			
			if (year != 0)
				criteria.add(Restrictions.sqlRestriction("startDate like ?", year+"-%-%", Hibernate.STRING));
			if (month != 0) {
				String monthStr = month < 10 ? ("0"+month) : String.valueOf(month);
				criteria.add(Restrictions.sqlRestriction("startDate like ?", "%-"+monthStr+"-%", Hibernate.STRING));
			}
			if (depotId != 0)
				criteria.add(Restrictions.eq("d.depotId", depotId));
			List<Invoice> records = criteria.list();
			tx.commit();
			
			return records;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Year", String.valueOf(year));
			logProps.put("Month", String.valueOf(month));
			logProps.put("Depot ID", String.valueOf(depotId));
			ApplicationException aex = new ApplicationException("Unable to retrieve the Invoice for the given Depot for the given year and month.", ex);
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
	 * Retrieves a list of issued invoices based on several different criteria for the
	 * invoice report page.
	 * @param depotId The id of the depot
	 * @param stateId The id of the state
	 * @param city The city
	 * @param startMonth The starting invoice period month
	 * @param startYear The starting invoice period year
	 * @param endMonth The ending invoice period month
	 * @param endYear The ending invoice period year
	 * @return The list of invoices
	 */
	@SuppressWarnings("unchecked")
	public List<Invoice> fetch(int depotId, int stateId, String city, int startMonth, int startYear, int endMonth, int endYear) throws Exception {	
		
		List issuedInvoiceNumbers = new InvoiceIssueHistoryDao().fetchInvoiceNumbers();
		if (issuedInvoiceNumbers.size() == 0) {
			return new ArrayList<Invoice>();
		}
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Invoice.class)
				.createAlias("depot", "d")
				.add(Restrictions.in("invoiceNumber", issuedInvoiceNumbers));
			
			if (depotId != 0)
				criteria.add(Restrictions.eq("d.depotId", depotId));
			
			if (stateId != 0) {
				criteria.createCriteria("d.state")
					.add(Restrictions.eq("stateId", stateId));
			}
			
			if (city != null)
				criteria.add(Restrictions.eq("d.city", city));
			
			if (startMonth != 0 && startYear != 0) {
				GregorianCalendar gc = new GregorianCalendar(startYear, startMonth-1, 1);
				criteria.add(Restrictions.ge("startDate", gc.getTime()));
			}
			
			if (endMonth != 0 && endYear != 0) {
				GregorianCalendar gc = new GregorianCalendar(endYear, endMonth-1, 1);
				criteria.add(Restrictions.le("startDate", gc.getTime()));
			}
			
			List<Invoice> records = criteria.list();
			tx.commit();
			
			return records;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Depot ID", String.valueOf(depotId));
			logProps.put("State ID", String.valueOf(stateId));
			logProps.put("City", city);
			logProps.put("Start Month", String.valueOf(startMonth));
			logProps.put("Start Year", String.valueOf(startYear));
			logProps.put("End Month", String.valueOf(endMonth));
			logProps.put("End Year", String.valueOf(endYear));
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested list of Invoices.", ex);
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
}
