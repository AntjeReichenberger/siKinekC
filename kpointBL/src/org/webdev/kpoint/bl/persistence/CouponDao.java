package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.pojo.Coupon;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.util.ApplicationProperty;

public class CouponDao extends BaseDao implements GenericDao<Coupon, Integer> {

	private static final KinekLogger logger = new KinekLogger(CouponDao.class);

	@Override
	public Integer create(Coupon newInstance) throws Exception {
		// TODO Auto-generated method stub

		Session session = null;
		Transaction tx = null;

		try {

			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();

		} catch (Exception ex) {
			ApplicationException aex = new ApplicationException(
					"Unable to create a new coupon.", ex);
			logger.error(aex);
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw aex;
		} finally {
			verifyDBState(logger, session, tx);
		}

		return newInstance.getCouponId();
	}

	/***
	 * Flow of logic: Retrieve coupon where EndDate>current date AND mapped
	 * against the coupon_emails table. coupon_emails table contains couponId
	 * and msg trigger id. If welcome email then join with region, organization
	 * and kinepoint table and (depotId=? or regionId=? or ogranizationId=?) and
	 * msgTrg.id=welcome.id If delivery or delivery reminder email then join
	 * only with kinekpoint table and (depotId=?) and msgTrg=delivery.id or
	 * deliveryReminder.id
	 * 
	 * @param depot
	 *            depot object which contains depotId
	 * @param messageTriggerId
	 *            messageTriggerId is the trigger id of the MessageTrigger
	 * @return List of filtered coupons
	 * 
	 *         Note: Coupon table contains three foreign keys: RegionId,
	 *         OrganizationId and KinekPointId Foreign keys might be available
	 *         or not. Therefore, we cannot do INNER JOIN (Default) with tables
	 *         of the foreign key because foreign key might be null. For this
	 *         reason, we have to do LEFT OUTER JOIN to create join with Region
	 *         or Organization or KinekPoint tables. The LEFT JOIN keyword
	 *         returns all rows from the left table (Coupon), even if there are
	 *         no matches in the right table (Region and Organization and
	 *         KinekPoint tables)
	 */
	public List<Coupon> fetchKinekPointCoupons(KinekPoint depot,
			int messageTriggerId) throws Exception {

		Session session = null;
		Transaction tx = null;

		List<Coupon> coupons = null;
		Calendar cal = Calendar.getInstance();

		try {
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Coupon.class)
								.add(Restrictions.gt("distributionEndDate", cal))
								.add(Restrictions.le("distributionStartDate", cal));
			
			Disjunction disjunction = Restrictions.disjunction();
			if (depot != null) {
				criteria.createAlias("kinekPoint", "kp", Criteria.LEFT_JOIN);
				disjunction.add(Restrictions.eq("kp.depotId",
						depot.getDepotId()));
			}

			if (depot != null && depot.getRegion() != null) {
				criteria.createAlias("region", "rg", Criteria.LEFT_JOIN);
				disjunction.add(Restrictions.eq("rg.regionId", depot
						.getRegion().getRegionId()));
			}

			if (depot != null && depot.getOrganization() != null) {
				criteria.createAlias("organization", "org", Criteria.LEFT_JOIN);
				disjunction.add(Restrictions.eq("org.organizationId", depot
						.getOrganization().getOrganizationId()));
			}

			// if depot is null then there is no region and organization object
			// so build query for Register reminder email
			if (depot == null) {
				criteria.add(Restrictions.isNull("kp.depotId"));
				criteria.add(Restrictions.isNull("rg.regionId"));
				criteria.add(Restrictions.isNull("org.organizationId"));
				criteria.addOrder(Order.asc("expiryDate"));
			} else {
				criteria.add(disjunction).addOrder(Order.asc("expiryDate"));
			}

			criteria.createAlias("messageTriggers", "msgTrgs").add(
					Restrictions.eq("msgTrgs.Id", messageTriggerId));

			coupons = criteria.list();

			if (coupons == null) {
				coupons = new ArrayList<Coupon>();
			}

			tx.commit();

		} catch (Exception ex) {
			ApplicationException aex = new ApplicationException(
					"Unable to fetch coupon by endDate.", ex);
			logger.error(aex);
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw aex;
		} finally {
			verifyDBState(logger, session, tx);
		}

		return coupons;
	}

	/***
	 * Fetch all active coupons that are mapped to the specified KinekPoint.
	 * 
	 * @param depot  Coupons will be retrieved for this Depot
	 * @return List of filtered coupons
	 * 
	 */
	public List<Coupon> fetchKinekPointCoupons(KinekPoint depot) throws Exception {

		Session session = null;
		Transaction tx = null;

		List<Coupon> coupons = null;
		Calendar cal = Calendar.getInstance();

		try {
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Coupon.class)
								.add(Restrictions.gt("distributionEndDate", cal))
								.add(Restrictions.le("distributionStartDate", cal));
			
			Disjunction disjunction = Restrictions.disjunction();
			if (depot != null) {
				criteria.createAlias("kinekPoint", "kp", Criteria.LEFT_JOIN);
				disjunction.add(Restrictions.eq("kp.depotId",
						depot.getDepotId()));
			}

			if (depot != null && depot.getRegion() != null) {
				criteria.createAlias("region", "rg", Criteria.LEFT_JOIN);
				disjunction.add(Restrictions.eq("rg.regionId", depot
						.getRegion().getRegionId()));
			}

			if (depot != null && depot.getOrganization() != null) {
				criteria.createAlias("organization", "org", Criteria.LEFT_JOIN);
				disjunction.add(Restrictions.eq("org.organizationId", depot
						.getOrganization().getOrganizationId()));
			}

			// if depot is null then there is no region and organization object
			if (depot == null) {
				criteria.add(Restrictions.isNull("kp.depotId"));
				criteria.add(Restrictions.isNull("rg.regionId"));
				criteria.add(Restrictions.isNull("org.organizationId"));
				criteria.addOrder(Order.asc("expiryDate"));
			} else {
				criteria.add(disjunction).addOrder(Order.asc("expiryDate"));
			}

			coupons = criteria.list();

			if (coupons == null) {
				coupons = new ArrayList<Coupon>();
			}

			tx.commit();

		} catch (Exception ex) {
			ApplicationException aex = new ApplicationException("Unable to fetch coupons.", ex);
			logger.error(aex);
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw aex;
		} finally {
			verifyDBState(logger, session, tx);
		}

		return coupons;
	}
	
	/***
	 * This method is used to retrieve a list of generic coupons - those that
	 * are not associated with a specific KinekPoint. Generic coupons are used
	 * on the registration reminder emails.
	 */
	public List<Coupon> fetchGenericCoupons(int messageTriggerId) throws Exception {

		Session session = null;
		Transaction tx = null;
		Calendar cal = Calendar.getInstance();

		List<Coupon> coupons = null;
		try {
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Coupon.class)
					.add(Restrictions.gt("distributionEndDate", cal))
					.add(Restrictions.le("distributionStartDate", cal))
					.createAlias("kinekPoint", "kp", Criteria.LEFT_JOIN)
					.add(Restrictions.isNull("kp.depotId"))
					.createAlias("region", "rg", Criteria.LEFT_JOIN)
					.add(Restrictions.isNull("rg.regionId"))
					.createAlias("organization", "org", Criteria.LEFT_JOIN)
					.add(Restrictions.isNull("org.organizationId"))
					.addOrder(Order.asc("expiryDate"));

			criteria.createAlias("messageTriggers", "msgTrgs").add(
					Restrictions.eq("msgTrgs.Id", messageTriggerId));

			coupons = criteria.list();

			if (coupons == null) {
				coupons = new ArrayList<Coupon>();
			}

			tx.commit();

		} catch (Exception ex) {
			ApplicationException aex = new ApplicationException(
					"Unable to fetch generic coupons.", ex);
			logger.error(aex);
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw aex;
		} finally {
			verifyDBState(logger, session, tx);
		}

		return coupons;
	}

	public boolean isAlwaysShowCouponConflictsByKP(int kinekPointId) throws Exception {

		Session session = null;
		Transaction tx = null;

		boolean isConflict = false;

		try {

			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Coupon.class)
					.createAlias("kinekPoint", "kp")
					.add(Restrictions.eq("kp.depotId", kinekPointId))
					.add(Restrictions.eq("alwaysShowCoupon", true));

			List<Coupon> coupons = criteria.list();

			if (coupons.size() > 0) {
				isConflict = true;
			}

			tx.commit();

		} catch (Exception ex) {

			ApplicationException aex = new ApplicationException(
					"Unable to fetch coupon by kinekpoint Id.", ex);
			logger.error(aex);
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw aex;

		} finally {
			verifyDBState(logger, session, tx);
		}

		return isConflict;
	}

	@Override
	public void delete(Coupon persistentObject) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Coupon> fetch() throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;

		List<Coupon> coupons = null;

		Calendar cal = Calendar.getInstance();

		try {
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Coupon.class);

			coupons = criteria.list();

			if (coupons == null) {
				coupons = new ArrayList<Coupon>();
			}

			tx.commit();

		} catch (Exception ex) {
			ApplicationException aex = new ApplicationException(
					"Unable to fetch coupons.", ex);
			logger.error(aex);
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw aex;
		} finally {
			verifyDBState(logger, session, tx);
		}

		return coupons;
	}

	@Override
	public Coupon read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateSessionUtil.getCurrentSession();

			tx = session.beginTransaction();
			Object coupon = session.get(Coupon.class, id);
			tx.commit();

			return (Coupon) coupon;
		} catch (Exception ex) {
			Hashtable<String, String> logProps = new Hashtable<String, String>();
			logProps.put("CouponID", id.toString());
			ApplicationException aex = new ApplicationException(
					"Could not read Coupon.", ex);
			logger.error(aex, logProps);

			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw aex;
		} finally {
			verifyDBState(logger, session, tx);
		}
	}

	@Override
	public void update(Coupon transientObject) throws Exception {
		// TODO Auto-generated method stub

	}

}
