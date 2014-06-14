package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.State;


public class KinekPointDao extends BaseDao implements GenericDao<KinekPoint, Integer> {
	
	private static final KinekLogger logger = new KinekLogger(KinekPointDao.class);
	
	@Override
	public Integer create(KinekPoint newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create a new Kinek Point.", ex);
			logger.error(aex);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		return newInstance.getDepotId();
	}

	@Override
	public void delete(KinekPoint persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	public void populateFees(List<KinekPoint> kps) throws Exception {
		for(KinekPoint kp : kps){
			kp.populateFees();
		}
	}
	
	@Override @SuppressWarnings("unchecked")
	public List<KinekPoint> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			// TODO: This will remove the base depot from searches, but it shouldn't be here in the first place.
			// It is a hack until something better can be implemented.
			Criteria criteria = session.createCriteria(KinekPoint.class)
				.add(Restrictions.ne("depotId", 1))
				.addOrder(Order.asc("name"));
			List<KinekPoint> depots = criteria.list();
			tx.commit();
			
			return depots;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Points.", ex);
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
	
	public List<KinekPoint> fetchByState(int stateId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			// TODO: This will remove the base depot from searches, but it shouldn't be here in the first place.
			// It is a hack until something better can be implemented.
			Criteria criteria = session.createCriteria(KinekPoint.class)
				.add(Restrictions.ne("depotId", 1))
				.add(Restrictions.eq("state.stateId", stateId))
				.addOrder(Order.asc("name"));
			List<KinekPoint> depots = criteria.list();
			tx.commit();
			
			return depots;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Points.", ex);
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
	

	public List<KinekPoint> fetchLight() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			// TODO: This will remove the base depot from searches, but it shouldn't be here in the first place.
			// It is a hack until something better can be implemented.
			Criteria criteria = session.createCriteria(KinekPoint.class)
				.add(Restrictions.ne("depotId", 1))
				.addOrder(Order.asc("name"));
			
			// only retrieve the following fields: id, state, viewCount, user.username
			ProjectionList properties = Projections.projectionList();
			properties.add(Projections.property("depotId"));
			properties.add(Projections.property("name"));
			properties.add(Projections.property("city"));
			properties.add(Projections.property("phone"));
			properties.add(Projections.property("createdDate"));
			properties.add(Projections.property("address1"));
			criteria.setProjection(properties);
			

			List<KinekPoint> depots = new ArrayList<KinekPoint>();
			Iterator it = criteria.list().iterator();
		      if(!it.hasNext()){
		        System.out.println("No any data!");
		      }
		      else{
		        while(it.hasNext()){
		          Object[] row = (Object[])it.next();
		          KinekPoint point = new KinekPoint();
		          for(int i = 0; i < row.length;i++){		        	
		        	if(i == 0){
		        		point.setDepotId(new Integer(row[i].toString()));
		        	}
		        	else if(i == 1){
		        		point.setName(row[i].toString());
		        	}
		        	else if(i == 2){
		        		point.setCity(row[i].toString());
		        	}
		        	else if(i == 3){
		        		point.setPhone(row[i].toString());
		        	}
		        	else if(i == 4){
		        		point.setCreatedDate((Calendar) row[i]);
		        	}
		        	else if(i == 5){
		        		point.setAddress1(row[i].toString());
		        	}
		          }
		          depots.add(point);
		        }
		      }
			tx.commit();
			
			return depots;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Points.", ex);
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
	
	public List<KinekPoint> fetchLight(int statusId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			// TODO: This will remove the base depot from searches, but it shouldn't be here in the first place.
			// It is a hack until something better can be implemented.
			Criteria criteria = session.createCriteria(KinekPoint.class)
				.add(Restrictions.ne("depotId", 1))
				.add(Restrictions.eq("status.id", statusId))
				.addOrder(Order.asc("name"));
			
			// only retrieve the following fields: id, state, viewCount, user.username
			ProjectionList properties = Projections.projectionList();
			properties.add(Projections.property("depotId"));
			properties.add(Projections.property("name"));
			properties.add(Projections.property("city"));
			properties.add(Projections.property("phone"));
			properties.add(Projections.property("createdDate"));
			criteria.setProjection(properties);
			

			List<KinekPoint> depots = new ArrayList<KinekPoint>();
			Iterator it = criteria.list().iterator();
		      if(!it.hasNext()){
		        System.out.println("No any data!");
		      }
		      else{
		        while(it.hasNext()){
		          Object[] row = (Object[])it.next();
		          KinekPoint point = new KinekPoint();
		          for(int i = 0; i < row.length;i++){		        	
		        	if(i == 0){
		        		point.setDepotId(new Integer(row[i].toString()));
		        	}
		        	else if(i == 1){
		        		point.setName(row[i].toString());
		        	}
		        	else if(i == 2){
		        		point.setCity(row[i].toString());
		        	}
		        	else if(i == 3){
		        		point.setPhone(row[i].toString());
		        	}
		        	else if(i == 4){
		        		point.setCreatedDate((Calendar) row[i]);
		        	}
		          }
		          depots.add(point);
		        }
		      }
			tx.commit();
			
			return depots;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Points.", ex);
			logger.error(aex);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		return null;
	}
	
	public List<KinekPoint> fetchLight(boolean isActive) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			// TODO: This will remove the base depot from searches, but it shouldn't be here in the first place.
			// It is a hack until something better can be implemented.
			Criteria criteria = session.createCriteria(KinekPoint.class)
				.add(Restrictions.ne("depotId", 1))
				.addOrder(Order.asc("name"));
			
			if (isActive)
				criteria = criteria
					.createAlias("status", "s")
					.add(Restrictions.eq("s.id", ExternalSettingsManager.getDepotStatusActive()));
			
			// only retrieve the following fields: id, state, viewCount, user.username
			ProjectionList properties = Projections.projectionList();
			properties.add(Projections.property("depotId"));
			properties.add(Projections.property("name"));
			properties.add(Projections.property("city"));
			properties.add(Projections.property("phone"));
			properties.add(Projections.property("createdDate"));
			properties.add(Projections.property("state"));	
			criteria.setProjection(properties);		

			List<KinekPoint> depots = new ArrayList<KinekPoint>();
			Iterator it = criteria.list().iterator();
		      if(!it.hasNext()){
		        System.out.println("No any data!");
		      }
		      else{
		        while(it.hasNext()){
		          Object[] row = (Object[])it.next();
		          KinekPoint point = new KinekPoint();
		          for(int i = 0; i < row.length;i++){		        	
		        	if(i == 0){
		        		point.setDepotId(new Integer(row[i].toString()));
		        	}
		        	else if(i == 1){
		        		point.setName(row[i].toString());
		        	}
		        	else if(i == 2){
		        		point.setCity(row[i].toString());
		        	}
		        	else if(i == 3){
		        		point.setPhone(row[i].toString());
		        	}
		        	else if(i == 4){
		        		point.setCreatedDate((Calendar) row[i]);
		        	}
		        	else if(i == 5){
		        		point.setState((State) row[i]);
		        	}
		          }
		          depots.add(point);
		        }
		      }
			tx.commit();
			
			return depots;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Points.", ex);
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
	 * Retrieves all depots of either active or inactive status, depending on the value of isActive 
	 * @param isActive Whether retrieve active or inactive depots
	 * @return A list of either active or inactive depots
	 */
	@SuppressWarnings("unchecked")
	public List<KinekPoint> fetch(boolean isActive) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			// TODO: This will remove the base depot from searches, but it shouldn't be here in the first place.
			// It is a hack until something better can be implemented.
			Criteria criteria = session.createCriteria(KinekPoint.class)			
				.add(Restrictions.ne("depotId", 1))
				.add(Restrictions.isNotNull("createdDate"));
			
			if (isActive)
				criteria = criteria
					.createAlias("status", "s")
					.add(Restrictions.eq("s.id", ExternalSettingsManager.getDepotStatusActive()));
				
			List<KinekPoint> depots = criteria.list();
			tx.commit();
			return depots;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("IsActive", Boolean.toString(isActive));
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Points.", ex);
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
	 * Retrieves all depots with the given status
	 * @param statusId The status to look for
	 * @return The list of depots
	 */
	@SuppressWarnings("unchecked")
	public List<KinekPoint> fetch(int statusId) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			// TODO: This will remove the base depot from searches, but it shouldn't be here in the first place.
			// It is a hack until something better can be implemented.
			Criteria criteria = session.createCriteria(KinekPoint.class)			
				.add(Restrictions.ne("depotId", 1))
				.createAlias("status", "s")
				.add(Restrictions.eq("s.id", statusId))
				.addOrder(Order.asc("createdDate"));
	
			List<KinekPoint> depots = criteria.list();
			tx.commit();
			populateFees(depots);
			return depots;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("StatusID", String.valueOf(statusId));
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Points with the given status.", ex);
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
	public List<KinekPoint> fetch(String city) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			// TODO: This will remove the base depot from searches, but it shouldn't be here in the first place.
			// It is a hack until something better can be implemented.
			Criteria criteria = session.createCriteria(KinekPoint.class)
				.createAlias("status", "s")
				.add(Restrictions.eq("s.id", ExternalSettingsManager.getDepotStatusActive()))
				.add(Restrictions.ne("depotId", 1))
				.add(Restrictions.ilike("city", city, MatchMode.ANYWHERE));
			List<KinekPoint> depots = criteria.list();
			tx.commit();
			populateFees(depots);
			return depots;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("City", city);
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Points in the given city.", ex);
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
	 * Retrieves all (active) depots whose coordinates are located in the box defined by the coordinate range provided.
	 * @param maxLatitude The maximum latitude
	 * @param minLatitude The minimum latitude
	 * @param maxLongitude The maximum Longitude
	 * @param minLongitude The minimum Longitude
	 * @return A list of all depots whose coordinates are located in the box defined by the coordinate range provided.
	 */
	@SuppressWarnings("unchecked")
	public List<KinekPoint> fetch(double maxLatitude, double minLatitude, double maxLongitude, double minLongitude, boolean isBorderLocation) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(KinekPoint.class);
			criteria.createAlias("status", "s");
			criteria.add(Restrictions.between("geolat", minLatitude, maxLatitude));
			criteria.add(Restrictions.between("geolong", minLongitude, maxLongitude));
			criteria.add(Restrictions.eq("s.id", ExternalSettingsManager.getDepotStatusActive()));
			criteria.add(Restrictions.ne("depotId", 1));
						
			List<KinekPoint> depots = criteria.list();
			
			
			if(isBorderLocation){
				for(int i=0; i<depots.size(); i++) {
					if(depots.get(i).getBorderStates().size()== 0) {
						//drop current from list
						depots.remove(i);
						i--;
					}
				}
			}
			
			tx.commit();
			populateFees(depots);
			return depots;		
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Max Latitude", String.valueOf(maxLatitude));
			logProps.put("Min Latitude", String.valueOf(minLatitude));
			logProps.put("Max Longitude", String.valueOf(maxLongitude));
			logProps.put("Min Longitude", String.valueOf(minLongitude));
			logProps.put("IsBorderLocation", Boolean.toString(isBorderLocation));
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Points for the given restrictions.", ex);
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
	 * Retrieves all (active) depots whose coordinates are located in the box defined by the coordinate range provided.
	 * @param maxLatitude The maximum latitude
	 * @param minLatitude The minimum latitude
	 * @param maxLongitude The maximum Longitude
	 * @param minLongitude The minimum Longitude
	 * @return A list of all depots whose coordinates are located in the box defined by the coordinate range provided.
	 */
	@SuppressWarnings("unchecked")
	public List<KinekPoint> fetchBorderKinekPoints(String stateProvCode) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			// TODO: This will remove the base depot from searches, but it shouldn't be here in the first place.
			// It is a hack until something better can be implemented.
			Criteria criteria = session.createCriteria(KinekPoint.class)		
						.createAlias("status", "s")
						.createAlias("borderStates", "brdStates")
						.add(Restrictions.eq("brdStates.stateProvCode", stateProvCode))
						.add(Restrictions.eq("s.id", ExternalSettingsManager.getDepotStatusActive()))
						.add(Restrictions.ne("depotId", 1));
						
			List<KinekPoint> depots = criteria.list();
			tx.commit();
			populateFees(depots);
			return depots;		
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("StateProvince Code", stateProvCode);
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Points in the given state/province.", ex);
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
	public KinekPoint read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object depot = session.get(KinekPoint.class, id);
			tx.commit();
			KinekPoint kp = (KinekPoint)depot;
			kp.populateFees();
			return kp;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KPID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Kinek Point.", ex);
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
	public KinekPoint read(String address1, String city, String stateProvCode, String zip) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(KinekPoint.class)
				.createAlias("state", "st")
				.add(Restrictions.eq("address1", address1))
				.add(Restrictions.eq("city", city))
				.add(Restrictions.eq("st.stateProvCode", stateProvCode))
				.add(Restrictions.eq("zip", zip))			
				.setMaxResults(1);
			List<KinekPoint> kpointList = criteria.list();
			tx.commit();
			
			KinekPoint kpoint = kpointList.size() > 0 ? kpointList.get(0) : null;
			kpoint.populateFees();
			return kpoint;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Address1", address1);
			logProps.put("City", city);
			logProps.put("StateProvince Code", stateProvCode);
			logProps.put("Zip", zip);
			ApplicationException aex = new ApplicationException("Unable to retrieve the Kinek Point at the given address.", ex);
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
	 * Retrieves the closest depot to the given coordinates
	 * @param latitude The latitude
	 * @param longitude The Longitude
	 * @return The closest depot to the given coordinates
	 */
	public KinekPoint readNearest(double latitude, double longitude, boolean isBorderLocation) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
		
			Query query = session.createSQLQuery(
		    	"SELECT depotId, " +
		    	"( SQRT( POW( ABS(? - geolat), 2) + POW( ABS(? - geolong), 2) ) ) AS dif " +
		    	"FROM KPDepot ORDER BY dif ASC LIMIT 1;")
		    	.setString(0, Double.toString(latitude))
		    	.setString(1, Double.toString(longitude));
			
			Object[] res = (Object[]) query.uniqueResult();
			int id = (Integer) res[0];
			tx.commit();
			
			return read(id);	
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Latitude", String.valueOf(latitude));
			logProps.put("Longitude", String.valueOf(longitude));
			logProps.put("IsBorderLocation", String.valueOf(isBorderLocation));
			ApplicationException aex = new ApplicationException("Unable to retrieve the Kinek Point at the given Latitude and Longitude.", ex);
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
	public void update(KinekPoint transientObject) throws Exception {
		// TODO: This will remove the base depot from searches, but it shouldn't be here in the first place.
		// It is a hack until something better can be implemented.
		if (transientObject.getDepotId() == 1) {
			transientObject.setGeolat(0);
			transientObject.setGeolong(0);
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
			logProps.put("KinekPointID", String.valueOf(transientObject.getDepotId()));
			ApplicationException aex = new ApplicationException("Unable to update a Kinek Point.", ex);
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
