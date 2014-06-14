package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;


public class PackageReceiptDao extends BaseDao implements GenericDao<PackageReceipt, Integer> {

	private static final KinekLogger logger = new KinekLogger(PackageReceiptDao.class);

	@Override
	public Integer create(PackageReceipt newInstance) throws Exception {
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
			ApplicationException aex = new ApplicationException("Could not create package receipt.", ex);
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
	public void delete(PackageReceipt persistentObject) {
		// TODO Auto-generated method stub
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PackageReceipt> fetch() throws Exception {	
		List<PackageReceipt> associations = new ArrayList<PackageReceipt>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(PackageReceipt.class);
			associations = criteria.list();
			tx.commit();
			
			return associations;	
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch package receipts.", ex);
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
	
	@Override
	public PackageReceipt read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			Package pkg = new Package();
			
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object association = session.get(PackageReceipt.class,id);
			tx.commit();
				
			return (PackageReceipt)association;	
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("PackageReceiptID", id.toString());
			ApplicationException aex = new ApplicationException("Could not retrieve package receipt.", ex);
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
	public void update(PackageReceipt transientObject) throws Exception {
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
			logProps.put("PackageID", String.valueOf(transientObject.getId()));
			ApplicationException aex = new ApplicationException("Could not update package receipt.", ex);
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
	 * Find all package receipts exactly matching the supplied Kinek number. 
	 * @param kinekNumber The Kinek number to search by.
	 * @return A list of package receipts exactly matching the provided Kinek number
	 */
	@SuppressWarnings("unchecked")
	public List<PackageReceipt> fetch(String kinekNumber) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
		
			Criteria criteria = session.createCriteria(PackageReceipt.class)
				.addOrder(Order.desc("receivedDate"))
				.createAlias("packageRecipients", "pr")
				.add(Restrictions.eq("pr.kinekNumber", kinekNumber));
			
			Package pkg = new Package();
			List<PackageReceipt> receipts = criteria.list();
	
			tx.commit();
			
			return receipts;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KinekNumber", kinekNumber);
			ApplicationException aex = new ApplicationException("Could not retrieve package receipts for user.", ex);
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
	 * Find all package receipts exactly matching the supplied User ID. 
	 * @param userId The User ID  to search by.
	 * @return A list of package receipts exactly matching the provided User ID
	 */
	@SuppressWarnings("unchecked")
	public List<PackageReceipt> fetch(int userId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(PackageReceipt.class)
				.addOrder(Order.desc("receivedDate"))
				.createAlias("packageRecipients", "pr")
				.add(Restrictions.eq("pr.userId", userId));
			
			List<PackageReceipt> receipts = criteria.list();
			if(receipts != null){
				for(PackageReceipt pr : receipts){
					KinekPoint kp = pr.getKinekPoint();
					kp.populateFees();
				}
			}
			tx.commit();
			return receipts;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("UserID", String.valueOf(userId));
			ApplicationException aex = new ApplicationException("Could not retrieve package receipts for user.", ex);
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
	 * Find all package receipts matching a particular depot
	 * @param depot The depot to search by
	 * @return A list of package receipts matching the provided search criteria
	 */
	@SuppressWarnings("unchecked")
	public List<PackageReceipt> fetch(KinekPoint depot) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
		
			Criteria criteria = session.createCriteria(PackageReceipt.class)
			.createAlias("kinekPoint", "kp")
			.add(Restrictions.eq("kp.depotId", depot.getDepotId()));
			
			List<PackageReceipt> receipts = criteria.list();
			
			tx.commit();
			return receipts;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KinekPointID", String.valueOf(depot.getDepotId()));
			ApplicationException aex = new ApplicationException("Could not retrieve package receipts for depot.", ex);
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
	 * Find all package receipts matching the given depots
	 * @param depot The depot to search by
	 * @return A list of package receipts matching the provided search criteria
	 */
	@SuppressWarnings("unchecked")
	public List<PackageReceipt> fetchAll(List<KinekPoint> depots) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			
			List<Integer> depotIds = new ArrayList<Integer>();
			for(KinekPoint depot : depots ){
				depotIds.add(depot.getDepotId());
			}

			Criteria criteria = session.createCriteria(PackageReceipt.class)
			.createAlias("kinekPoint", "kp")
			.add(Restrictions.in("kp.depotId", depotIds.toArray()));
			
			List<PackageReceipt> receipts = criteria.list();
			
			tx.commit();
			return receipts;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KinekPointID", String.valueOf(1));
			ApplicationException aex = new ApplicationException("Could not retrieve package receipts for depot.", ex);
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
	 * Find all package receipts matching the supplied Kinek number/depot combination. 
	 * @param kinekNumber The Kinek number to search by.
	 * @return A list of package receipts matching the provided Kinek number/depot combination
	 */
	@SuppressWarnings("unchecked")
	public List<PackageReceipt> fetch(String kinekNumber, KinekPoint depot) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
		
			Criteria criteria = session.createCriteria(PackageReceipt.class)
				.createAlias("packageRecipients", "pr")
				.createAlias("kinekPoint", "kp")
				.add(Restrictions.eq("pr.kinekNumber", kinekNumber))
				.add(Restrictions.eq("kp.depotId", depot.getDepotId()));
			
			List<PackageReceipt> receipts = criteria.list();
	
			tx.commit();
			
			return receipts;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KinekPointID", String.valueOf(depot.getDepotId()));
			logProps.put("KinekNumber", kinekNumber);
			ApplicationException aex = new ApplicationException("Could not retrieve package receipts for depot and kineknumber combo.", ex);
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
	 * Retrieves all receipts where package's pickupdate is null and lastEmailDate at
	 * least as old as the given cut off date
	 * @param cutOffDate The cut off date to check against the last email date
	 * @return A list of receipts 
	 */
	@SuppressWarnings("unchecked")
	public List<PackageReceipt> fetch(Calendar cutOffDate) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
		
			Criteria criteria = session.createCriteria(PackageReceipt.class)
				.createAlias("packages", "p")
				.add(Restrictions.lt("lastEmailDate", cutOffDate.getTime()))
				.add(Restrictions.isNull("p.pickupDate"));
			
			List<PackageReceipt> tempReceipts = criteria.list();
			
			tx.commit();
			
			//The criteria.list() should return a list of all unique receipts, but for some reason
			//it duplicated them by the number of recipients
			List<PackageReceipt> packageReceipts = new ArrayList<PackageReceipt>();
			for (PackageReceipt receipt : tempReceipts) {
				if (!packageReceipts.contains(receipt))
					packageReceipts.add(receipt);
			}
			
			return packageReceipts;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CutOffDate", cutOffDate.toString());
			ApplicationException aex = new ApplicationException("Could not retrieve package receipts based on cut off date.", ex);
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
	 * Retrieves all receipts where package has arrived SpecialDeliveryReminderEmailDays days ago. 
	 *   
	 * @param specialDeliveryReminderEmailDays as int e.g: 27 
	 * 
	 * @return A list of receipts 
	 */
	@SuppressWarnings("unchecked")
	public List<PackageReceipt> fetchOnlySpecialDaysOldPackageReceipt(int specialDeliveryReminderEmailDays) throws Exception {
		Session session = null;
		Transaction tx = null;
		
		Calendar calStart24Hrs=Calendar.getInstance();
		calStart24Hrs.add(Calendar.DATE, -specialDeliveryReminderEmailDays);
		if(calStart24Hrs.get(Calendar.AM_PM)==Calendar.PM){
			calStart24Hrs.add(Calendar.HOUR,-(calStart24Hrs.get(Calendar.HOUR)+12));
		}else{
			calStart24Hrs.add(Calendar.HOUR,-(calStart24Hrs.get(Calendar.HOUR)));
		}
		calStart24Hrs.clear(Calendar.MINUTE);
		calStart24Hrs.clear(Calendar.SECOND);

		Calendar calEnd24Hrs=Calendar.getInstance();
		if(calEnd24Hrs.get(Calendar.AM_PM)==Calendar.PM){
			calEnd24Hrs.add(Calendar.HOUR, -(calEnd24Hrs.get(Calendar.HOUR)+12));
		}else{
			calEnd24Hrs.add(Calendar.HOUR, -(calEnd24Hrs.get(Calendar.HOUR)));
		}
		calEnd24Hrs.add(Calendar.DATE, -(specialDeliveryReminderEmailDays-1));
		calEnd24Hrs.clear(Calendar.MINUTE);
		calEnd24Hrs.clear(Calendar.SECOND);
		
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
				
			Criteria criteria = session.createCriteria(PackageReceipt.class)
				.createAlias("packages", "p")
				.add(Restrictions.between("receivedDate",calStart24Hrs.getTime(),calEnd24Hrs.getTime()))		
				.add(Restrictions.isNull("p.pickupDate"));
			
			List<PackageReceipt> tempReceipts = criteria.list();
			
			tx.commit();
			
			//The criteria.list() should return a list of all unique receipts, but for some reason
			//it duplicated them by the number of recipients, this is for some reason because of
			//the restriction on the depot id
			List<PackageReceipt> packageReceipts = new ArrayList<PackageReceipt>();
			for (PackageReceipt receipt : tempReceipts) {
				if (!packageReceipts.contains(receipt))
					packageReceipts.add(receipt);
			}
			return packageReceipts;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("DaysOld", String.valueOf(specialDeliveryReminderEmailDays));
			ApplicationException aex = new ApplicationException("Could not retrieve package receipts that require special reminder.", ex);
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
	 * Retrieves all package receipts for the specified depot which have 
	 * been received between the two provided dates 
	 * @param depot The depot to search on
	 * @param start The earliest "received date"
	 * @param end The latest "received date'
	 * @return All packages for the specified depot which have
	 * been received between the two provided dates
	 */
	@SuppressWarnings("unchecked")
	public List<PackageReceipt> fetch(int depotId, Calendar start, Calendar end) throws Exception {
		List<String> trainingKinekNumbers = ExternalSettingsManager.getTrainingKinekNumbers();
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(PackageReceipt.class)
				.createAlias("packageRecipients", "pr")
				.createAlias("kinekPoint", "d")
				.add(Restrictions.eq("d.depotId", depotId))
			// TODO: Package receipt should probably be changed to support Calendar
			// for now, convert Calendar to Date using start.getTime() and end.getTime()
				.add(Restrictions.between("receivedDate", start.getTime(), end.getTime()));
			for (String trainingKinekNumber : trainingKinekNumbers)
				criteria.add(Restrictions.ne("pr.kinekNumber", trainingKinekNumber));
			
			List<PackageReceipt> tempReceipts = criteria.list();
			tx.commit();
			
			//The criteria.list() should return a list of all unique receipts, but for some reason
			//it duplicated them by the number of recipients, this is for some reason because of
			//the restriction on the depot id
			List<PackageReceipt> packageReceipts = new ArrayList<PackageReceipt>();
			for (PackageReceipt receipt : tempReceipts) {
				if (!packageReceipts.contains(receipt))
					packageReceipts.add(receipt);
			}
			
			return packageReceipts;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KinekPointID", String.valueOf(depotId));
			logProps.put("StartDate", start.toString());
			logProps.put("EndDate", end.toString());
			ApplicationException aex = new ApplicationException("Could not retrieve package receipts for depot.", ex);
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
	 * Retrieves the package receipt that contains the provided package 
	 * @param packageObj The package to retrieve the receipt for
	 * @return The package receipt that contains the provided package 
	 */
	@SuppressWarnings("unchecked")
	public PackageReceipt read(Package packageObj) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
		
			Criteria criteria = session.createCriteria(PackageReceipt.class)
				.createAlias("packages", "p")
				.add(Restrictions.eq("p.packageId", packageObj.getPackageId()))
				.setMaxResults(1);
			
			List<PackageReceipt> packageReceipts = criteria.list();
	
			tx.commit();
			PackageReceipt pickup = packageReceipts.size() > 0 ? packageReceipts.get(0) : null;
		
			return pickup;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("PackageID", String.valueOf(packageObj.getPackageId()));
			ApplicationException aex = new ApplicationException("Could not retrieve package receipt for package.", ex);
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
