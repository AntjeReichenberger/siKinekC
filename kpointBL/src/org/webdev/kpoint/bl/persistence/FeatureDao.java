package org.webdev.kpoint.bl.persistence;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.Feature;


public class FeatureDao extends BaseDao implements GenericDao<Feature, Integer> {

	private static final KinekLogger logger = new KinekLogger(FeatureDao.class);
	
	@Override
	public Integer create(Feature newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Feature persistentObject) throws Exception {
		// TODO Auto-generated method stub
	}


	/**
	 * Retrieves a list of all possible features that a depot can have
	 * @return A list of all possible features that a depot can have
	 */
	@Override @SuppressWarnings("unchecked")
	public List<Feature> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Feature.class);
			List<Feature> features = criteria.list();
			tx.commit();
			
			return features;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Features.", ex);
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

	@Override
	public Feature read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object feature = session.get(Feature.class, id);
			tx.commit();
				
			return (Feature)feature;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("FeatureID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Feature.", ex);
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

	@Override
	public void update(Feature transientObject) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Retrieves a Map of all available Features
	 * @return A Map of all available Features
	 */
	public Map<Integer, Feature> fetchMap() throws Exception {
		List<Feature> list = fetch();
		Map<Integer, Feature> features = new HashMap<Integer, Feature>();
		for (Feature feature : list) {
			features.put(feature.getFeatureId(), feature);
		}
		
		return features;
	}

}
