package org.webdev.kpoint.bl.persistence;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.Region;

public class RegionDao extends BaseDao implements GenericDao<Region, Integer> {

	private static final KinekLogger logger = new KinekLogger(Region.class);
	
	@Override
	public Integer create(Region newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Region persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Retrieves a list of all supported regions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Region> fetch() throws Exception {
		// TODO Auto-generated method stub
		
		Session session = null;
		Transaction tx = null;
		List<Region> regions = null;
		try{
			
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			
				Criteria criteria = session.createCriteria(Region.class).addOrder(Order.asc("name"));
				regions = criteria.list();
				
				if(regions == null){
					regions = new ArrayList<Region>();
				}
				
			tx.commit();
			
			return regions;
			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of regions.", ex);
			logger.error(aex);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
	}

    /**
     * Retrieve a Region that was previously persisted to the database using
     * the indicated id as primary key
     * @param id The id of the object to retrieve
     * @return The object matching the supplied id; null if none exist
     */
	public Region read(Integer id) throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
				Region region = (Region)session.get(Region.class, id);			
			tx.commit();
			
			return region;
			
		}catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("RegionID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested region.", ex);
			logger.error(aex, logProps);		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger,session,tx);
		}
	}

	@Override
	public void update(Region transientObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	
}
