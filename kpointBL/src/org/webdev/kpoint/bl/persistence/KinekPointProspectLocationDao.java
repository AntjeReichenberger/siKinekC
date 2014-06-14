package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.HibernateSessionUtil;
import org.webdev.kpoint.bl.pojo.KinekPointProspectLocation;

public class KinekPointProspectLocationDao extends BaseDao implements GenericDao<KinekPointProspectLocation, Integer> {

	private static final KinekLogger logger = new KinekLogger(KinekPointProspectLocationDao.class);

	@Override
	public Integer create(KinekPointProspectLocation newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create a new Kinek Point Prospect Location.", ex);
			logger.error(aex);
		
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
	public void delete(KinekPointProspectLocation persistentObject) throws Exception {
		// TODO Auto-generated method stub		
	}

	/**
	 * Retrieves all prospect locations
	 * @return All consumer credits
	 */
	@SuppressWarnings("unchecked")
	public List<KinekPointProspectLocation> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(KinekPointProspectLocation.class);
			List<KinekPointProspectLocation> prospects = criteria.list();
			tx.commit();
	
			return prospects;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Kinek Point Prospect Locations.", ex);
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
	public KinekPointProspectLocation read(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(KinekPointProspectLocation transientObject) throws Exception {
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
			logProps.put("KPProspectLocationID", String.valueOf(transientObject.getId()));
			ApplicationException aex = new ApplicationException("Unable to update a Kinek Point Prospect Location.", ex);
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
