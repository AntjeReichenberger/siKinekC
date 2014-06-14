package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.pojo.DefaultKinekPointHistory;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Role;
import org.webdev.kpoint.bl.pojo.User;


public class UserDao extends BaseDao implements GenericDao<User, Integer> {

	private static final KinekLogger logger = new KinekLogger(UserDao.class);
	
	@Override
	public Integer create(User newInstance) throws Exception {
		//get auto generated unique KinekNumber
		String kinekNumber = generateKinekNumber();
		//set the kinekNumber to user object
		newInstance.setKinekNumber(kinekNumber);
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
			
			if(!newInstance.getDepotAdminAccessCheck())
			{
				if (newInstance.getKinekPoint() != null && newInstance.getKinekPoint().getDepotId() > 1) {
					DefaultKinekPointHistory defaultKinekPointHistory = new DefaultKinekPointHistory();
					defaultKinekPointHistory.setUserId(newInstance.getUserId());
					defaultKinekPointHistory.setKinekPointId(newInstance.getKinekPoint().getDepotId());
					DefaultKinekPointHistoryDao defaultKinekPointHistoryDao = new DefaultKinekPointHistoryDao();
					defaultKinekPointHistoryDao.create(defaultKinekPointHistory);
				}
			}

		} 
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KinekNumber", kinekNumber);
			ApplicationException aex = new ApplicationException("Could not create User.", ex);
			logger.error(aex, logProps);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
				
		return newInstance.getUserId();
	}

	@Override
	public void delete(User persistentObject) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public User read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			User user = (User)session.get(User.class, id);
			tx.commit();
			
			if(user != null)
			{
				user.setNotificationSummary();
				for(KinekPoint kp : user.getKinekPoints()){
					kp.populateFees();
				}
			}
			
			return (User)user;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("UserID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read User.", ex);
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
	public User read(String username) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.eq("username", username))
				.setMaxResults(1);
			List<User> users = criteria.list();
			tx.commit();
			
			User user = users.size() > 0 ? users.get(0) : null;
		
			if(user != null)
				user.setNotificationSummary();
			
			return user;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("UserName", username);
			ApplicationException aex = new ApplicationException("Could not read User.", ex);
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
	 * Retrieves a single user matching the supplied email address
	 * @param email The email address of the user being retrieved 
	 * @return A user matching the supplied email address if one is found; otherwise null
	 */
	@SuppressWarnings("unchecked")
	public User readByEmail(String email) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.eq("email", email))
				.setMaxResults(1);
			List<User> users = criteria.list();
			tx.commit();
			
			User user = users.size() > 0 ? users.get(0) : null;
			if(user != null)
				user.setNotificationSummary();
			
			return user;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Email", email);
			ApplicationException aex = new ApplicationException("Could not read User.", ex);
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
	 * Retrieves a single user matching the supplied openID
	 * @param openID The openID identifier of the user being retrieved 
	 * @return A user matching the supplied openID if one is found; otherwise null
	 */
	@SuppressWarnings("unchecked")
	public User readByOpenID(String openID) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.eq("openID", openID))
				.setMaxResults(1);
			List<User> users = criteria.list();
			tx.commit();
			
			User user = users.size() > 0 ? users.get(0) : null;
			if(user != null)
				user.setNotificationSummary();
			
			return user;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("OpenID", openID);
			ApplicationException aex = new ApplicationException("Could not read User.", ex);
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
	 * Retrieves a single user matching the supplied kinek number
	 * @param kinekNumber The kinekNumber of the user being retrieved 
	 * @return A user matching the supplied kinek number if one is found; otherwise null
	 */
	@SuppressWarnings("unchecked")
	public User readConsumer(String kinekNumber) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.like("kinekNumber", "%"+kinekNumber))
				.setMaxResults(1);
			List<User> users = criteria.list();
			tx.commit();
			
			User user = users.size() > 0 ? users.get(0) : null;
			if(user != null)
				user.setNotificationSummary();

			return user;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("kinekNumber", kinekNumber);
			ApplicationException aex = new ApplicationException("Could not read User.", ex);
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
	 * Retrieves an enabled user matching the provided user name and password; null if none exist
	 * @param username The user name to match against
	 * @param password The password to match against
	 * @return The user matching the provided user name and password; null if none exist
	 */
	@SuppressWarnings("unchecked")
	public User read(String username, String password) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.eq("username", username))
				.add(Restrictions.eq("password", password))
				.add(Restrictions.eq("enabled", true))
				.setMaxResults(1);
			
			List<User> users = criteria.list();
			tx.commit();
			
			User user = users.size() > 0 ? users.get(0) : null;  
			if(user != null){
				user.setNotificationSummary();
				for(KinekPoint kp: user.getKinekPoints())
					kp.populateFees();
			}
			
			return user;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Username", username);
			ApplicationException aex = new ApplicationException("Could not read user.", ex);
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
	public void update(User transientObject) throws Exception {
		UserDao userDao = new UserDao();
		User oldUser = userDao.read(transientObject.getUserId());
		
		if(!transientObject.getDepotAdminAccessCheck()){
			if(transientObject.getKinekPoint() != null && oldUser.getKinekPoint() != null)
			{
				if (transientObject.getKinekPoint().getDepotId() != oldUser.getKinekPoint().getDepotId()) {
					DefaultKinekPointHistory defaultKinekPointHistory = new DefaultKinekPointHistory();
					defaultKinekPointHistory.setUserId(transientObject.getUserId());
					defaultKinekPointHistory.setKinekPointId(transientObject.getKinekPoint().getDepotId());
					DefaultKinekPointHistoryDao defaultKinekPointHistoryDao = new DefaultKinekPointHistoryDao();
					defaultKinekPointHistoryDao.create(defaultKinekPointHistory);
				}
			}
		}
		
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
			logProps.put("UserID", String.valueOf(transientObject.getUserId()));
			ApplicationException aex = new ApplicationException("Could not update the User.", ex);
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
	 * Retrieves all users
	 * @return The user which matches the supplied
	 */
	@SuppressWarnings("unchecked")
	public List<User> fetchConsumers() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.isNotNull("kinekNumber"))
				.add(Restrictions.ne("kinekNumber", ""));
			List<User> users = criteria.list();
			tx.commit();
				
			return users;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch Consumers.", ex);
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
	 * Retrieves all users
	 * @return All consumers except those that have a training Kinek#
	 */
	@SuppressWarnings("unchecked")
	public List<User> fetchConsumersForUserReport() throws Exception {
		List<String> trainingKinekNumbers = ExternalSettingsManager.getTrainingKinekNumbers();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.isNotNull("kinekNumber"))
				.add(Restrictions.ne("kinekNumber", ""));
			for (String trainingKinekNumber : trainingKinekNumbers)
				criteria.add(Restrictions.ne("kinekNumber", trainingKinekNumber));
			List<User> users = criteria.list();
			tx.commit();
	
			return users;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch Consumers for User Report.", ex);
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
	 * Retrieves the users matching the supplied Kinek#
	 * @param kinekNumber The kinek number to search by
	 * @return The user which matches the supplied
	 */
	@SuppressWarnings("unchecked")
	public List<User> fetchConsumers(String kinekNumber) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.like("kinekNumber", kinekNumber + "%"))
				.add(Restrictions.isNotNull("kinekNumber"))
				.add(Restrictions.ne("kinekNumber", ""));
			List<User> users = criteria.list();
			tx.commit();
				
			return users;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KinekNumber", kinekNumber);
			ApplicationException aex = new ApplicationException("Could not read User.", ex);
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
	 * Retrieves the users matching the supplied contact information
	 */
	@SuppressWarnings("unchecked")
	public List<User> fetchConsumers(String firstName, String lastName, String phone) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.conjunction()
					.add(Restrictions.ilike("firstName", firstName, MatchMode.START))
					.add(Restrictions.ilike("lastName", lastName, MatchMode.START))
					.add(Restrictions.ilike("phone", phone, MatchMode.START)))
				.add(Restrictions.isNotNull("kinekNumber"))
				.add(Restrictions.ne("kinekNumber", ""));
			List<User> users = criteria.list();
			tx.commit();
			
			return users;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("FirstName", firstName);
			logProps.put("LastName", lastName);
			logProps.put("Phone", phone);
			ApplicationException aex = new ApplicationException("Could not read User.", ex);
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
	 * Retrieves the users matching the supplied contact information
	 */
	@SuppressWarnings("unchecked")
	public List<User> fetchConsumers(String firstName, String lastName, int stateId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.createAlias("state", "s")
				.add(Restrictions.isNotNull("kinekNumber"))
				.add(Restrictions.ne("kinekNumber", ""));
	
			if (firstName != null && firstName != "")
				criteria.add(Restrictions.ilike("firstName", firstName, MatchMode.START));
			if (lastName != null && lastName != "")
				criteria.add(Restrictions.ilike("lastName", lastName, MatchMode.START));
			if (stateId != -1)
				criteria.add(Restrictions.eq("s.stateId", stateId));		
			
			List<User> users = criteria.list();
			tx.commit();
			
			return users;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("FirstName", firstName);
			logProps.put("LastName", lastName);
			logProps.put("StateID", String.valueOf(stateId));
			ApplicationException aex = new ApplicationException("Could not read Users.", ex);
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
	 * Retrieves the users matching the supplied contact information
	 */
	@SuppressWarnings("unchecked")
	public List<User> fetchConsumersByNamesOrPhone(String firstName, String lastName, String phone) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.isNotNull("kinekNumber"))
				.add(Restrictions.ne("kinekNumber", ""));
			
			if (firstName != null && !firstName.equals(""))
				criteria.add(Restrictions.ilike("firstName", firstName, MatchMode.START));
			if (lastName != null && !lastName.equals(""))
				criteria.add(Restrictions.ilike("lastName", lastName, MatchMode.START));
			if (phone != null && !phone.equals(""))
				criteria.add(Restrictions.ilike("phone", phone, MatchMode.START));
			
			List<User> users = criteria.list();
			tx.commit();
			
			return users;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("FirstName", firstName);
			logProps.put("LastName", lastName);
			logProps.put("Phone", phone);
			ApplicationException aex = new ApplicationException("Could not read Users.", ex);
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
	 * Retrieves all consumers created during the specified year.  This also
	 * excludes any consumers who are set up with a training kinek#.
	 * This could be changed to use Calendar if another 'int' method is required 
	 * @param createdYear The year to search on.
	 * @return All consumers created during the specified year
	 */
	@SuppressWarnings("unchecked")
	public List<User> fetchConsumers(int createdYear) throws Exception {
		List<String> trainingKinekNumbers = ExternalSettingsManager.getTrainingKinekNumbers();
		
		Calendar startDate = Calendar.getInstance();
		startDate.set(createdYear, Calendar.JANUARY, 1);
		
		Calendar endDate = Calendar.getInstance();
		endDate.set(createdYear, Calendar.DECEMBER, 31);
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.between("createdDate", startDate, endDate))
				.add(Restrictions.isNotNull("kinekNumber"))
				.add(Restrictions.ne("kinekNumber", ""));
			for (String trainingKinekNumber : trainingKinekNumbers)
				criteria.add(Restrictions.ne("kinekNumber", trainingKinekNumber));
			List<User> users = criteria.list();
			tx.commit();
			
			return users;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CreatedYear", String.valueOf(createdYear));
			ApplicationException aex = new ApplicationException("Could not read Users.", ex);
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
	 * Retrieves the users that will be sent registration reminder emails.
	 * Only retrieves uses that were created before the cut-off date,
	 * have not specified a depot id, and also have not received a previous
	 * registration reminder email.
	 */
	@SuppressWarnings("unchecked")
	public List<User> fetchConsumerForRegistrationReminder(Calendar cutOffDate) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.createAlias("depot", "d")
				.add(Restrictions.eq("d.depotId", 1))
				.add(Restrictions.isNull("regReminderEmailDate"))
				.add(Restrictions.le("createdDate", cutOffDate))			
				.add(Restrictions.eq("enabled", true))			
				.add(Restrictions.eq("roleId", Role.Consumer));							
			
			List<User> users = criteria.list();
			tx.commit();
			
			return users;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CutOffDate", cutOffDate.toString());
			ApplicationException aex = new ApplicationException("Could not read Users.", ex);
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
	 * Retrieves all admins (non-consumers) mapped to the specified deport that have authorization 
	 * equal to or less than the provided role
	 * This ensures DepotAdmins cannot edit KinekAdmins, etc 
	 * IE: KinekAdmin can retrieve all users, DepotAdmin can retrieve DepotAdmin, DeportStaff
	 * @param depotId The id of the depot to retrieve users from
	 * @param roleId The id of the role that represents the maximum 
	 * @return A list of users mapped to the provided depot with a role equal to or less than the provided role
	 */
	@SuppressWarnings("unchecked")
	public List<User> fetch(KinekPoint depot, int roleId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.createAlias("kinekPoints", "fkp")
				.add(Restrictions.eq("fkp.depotId",depot.getDepotId()))
				.add(Restrictions.ne("roleId", Role.Consumer));
	
			if (roleId == Role.DepotAdmin) {
				criteria.add(Restrictions.ne("roleId", Role.KinekAdmin))
					.add(Restrictions.ne("roleId", Role.ReportAdmin));
			}
			
			List<User> users = criteria.list();
			tx.commit();
			
			return users;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KinekPointID", String.valueOf(depot.getDepotId()));
			ApplicationException aex = new ApplicationException("Could not read Users.", ex);
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
	 * Retrieves an active user matching the provided user name and password; null if none exist.
	 * @param username The username to match against
	 * @param password The password to match against
	 * @return The user matching the provided username and password; null if none exist
	 */
	public User authenticateAdmin(String username, String password) throws Exception {
		User user = read(username, password);
		
		if (user == null) return null;
		
		user.setNotificationSummary();

		// Only return users with null or blank Kinek numbers. Only consumers, not admins, have Kinek numbers
		if (user.getKinekNumber() == null || user.getKinekNumber().equals("")) return user;
		
		return null; 
	}
	
	/**
	 * Retrieves an active consumer matching the provided user name and password; null if none exist.
	 * @param username The username to match against
	 * @param password The password to match against
	 * @return The user matching the provided username and password; null if none exist
	 */
	public User authenticateConsumer(String username, String password) throws Exception {
		User user = read(username, password);
		
		if (user == null) return null;
		
		user.setNotificationSummary();

		// Only return users with kinek numbers. All consumers must have kinek numbers
		if (user.getKinekNumber() != null && user.getKinekNumber().length() > 0) return user;
		
		return null;
	}
	
	/**
	 * Retrieves an active consumer with the specified token; null if none exist.
	 * @param token The active authentication token for the user.  This is used for some authentication mechanisms, such as Janrain OpenID.
	 * @return The user with the provided token; null if none exist
	 */
	@SuppressWarnings("unchecked")
	public User authenticateConsumer(String token) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.eq("authenticationToken", token))
				.add(Restrictions.eq("enabled", true))
				.setMaxResults(1);
			
			List<User> users = criteria.list();
			tx.commit();
			
			User user = users.size() > 0 ? users.get(0) : null;  
			if(user != null)
			{
				user.setNotificationSummary();

				// Only return users with kinek numbers. All consumers must have kinek numbers
				if (user.getKinekNumber() != null && user.getKinekNumber().length() > 0) return user;
			}
			
			return null;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Token", token);
			ApplicationException aex = new ApplicationException("Could not read user.", ex);
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
	public List<User> fetch() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	/**
	 * Generate a kinekNumber. Ensure it is unique
	 * @return A unique Kinek number
	 */
	private String generateKinekNumber() throws Exception {
		String kinekNumber;
		do {
			String num = String.valueOf(System.currentTimeMillis());
			kinekNumber = num.substring(num.length()-8, num.length()-1);
		} while (new UserDao().readConsumer(kinekNumber) != null);
		return kinekNumber;
	}		
	
	
	/**
	 * Retrieves all UserKinekPoints for the given userid 
	 * @param userId The user to look for
	 * @return The list of depots
	 */
	@SuppressWarnings("unchecked")
	public List<KinekPoint> fetchUserKinekPoints(int userId) throws Exception {
		Session session = null;
		Transaction tx = null;
		List<KinekPoint> favKPs = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.eq("userId", userId));				
			
			List<User> users = criteria.list();			
			
			if(users != null){				
				favKPs = new ArrayList<KinekPoint>(users.get(0).getKinekPoints()); 
			}
			
			tx.commit();
			
			if(favKPs == null){
				return new ArrayList<KinekPoint>();
			}
			else{
				new KinekPointDao().populateFees(favKPs);
			}
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("UserID", String.valueOf(userId));
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Points with the given userId.", ex);
			logger.error(aex, logProps);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		return favKPs;
	}
	
	/**
	 * Retrieves a list of users that do not have a city specified and have a createdDate between start date and end date
	 * @return A list of users
	 */
	@SuppressWarnings("unchecked")
	public List<User> fetchUsersForAddressUpdate(GregorianCalendar startDate, GregorianCalendar endDate) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class)
				.add(Restrictions.isNull("city"))
				.add(Restrictions.between("createdDate", startDate, endDate))
				.add(Restrictions.eq("roleId", Role.Consumer));	
			
			List<User> users = criteria.list();
			tx.commit();
			
			for(User user : users)
			{
				user.setNotificationSummary();
			}

			return users;
		}
		catch(Exception ex){
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw ex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
	}
}
