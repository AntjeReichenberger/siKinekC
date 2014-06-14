package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.HibernateSessionUtil;
import org.webdev.kpoint.bl.pojo.Payment;

public class PaymentDao extends BaseDao implements GenericDao<Payment, String> {

	private static final KinekLogger logger = new KinekLogger(PaymentDao.class);
	
	@Override
	/**
	 * Creates a new Payment record
	 */
	public String create(Payment newInstance) throws Exception {
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
			logProps.put("TransactionID", newInstance.getTransactionId());
			ApplicationException aex = new ApplicationException("Could not create payment.", ex);
			logger.error(aex, logProps);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}

		return newInstance.getTransactionId();
	}

	@Override
	public void delete(Payment persistentObject) throws Exception {		
	}

    /**
     * Retrieve the payment corresponding to the given transaction number 
     * @param transactionNumber The id of the object to retrieve
     * @return The object matching the supplied transactionNumber; null if none exist
     */
	public Payment read(String transactionNumber) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object payment = session.get(Payment.class, transactionNumber);
			tx.commit();
			
			return (Payment)payment;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("TransactionNumber", transactionNumber);
			ApplicationException aex = new ApplicationException("Could not read payment.", ex);
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
	public void update(Payment transientObject) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Retrieves a list of all payments
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Payment> fetch() throws Exception {
		List<Payment> payments = new ArrayList<Payment>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Payment.class);
			payments = criteria.list();
			tx.commit();
			
			return payments;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch payments.", ex);
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
	 * Retrieves a list of payments for the given invoice number
	 * @param invoiceNumber The invoice number to look for
	 * @return The list of payments
	 */
	@SuppressWarnings("unchecked")
	public List<Payment> fetch(String invoiceNumber) throws Exception {
		List<Payment> payments = new ArrayList<Payment>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Payment.class)
				.createAlias("invoice", "i")
				.add(Restrictions.eq("i.invoiceNumber", invoiceNumber));
			payments = criteria.list();
			tx.commit();
			
			return payments;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("InvoiceNumber", invoiceNumber);
			ApplicationException aex = new ApplicationException("Could not read payments for invoice.", ex);
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
