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
import org.webdev.kpoint.bl.pojo.Package;

public class PackageDao extends BaseDao implements GenericDao<Package, Integer> {

	private static final KinekLogger logger = new KinekLogger(PackageDao.class);

	@Override
	public Integer create(Package newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not create package.", ex);
			logger.error(aex);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
			
		return newInstance.getPackageId();
	}

	@Override
	public void delete(Package persistentObject) throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * Retrieves the package specified by the supplied package id
	 * @param id The id of the package to retrieve.
	 * @return The package identified by the id if one is found; otherwise false
	 */
	public Package read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object Package = session.get(Package.class, id);
			tx.commit();
				
			return (Package)Package;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("PackageID", id.toString());
			ApplicationException aex = new ApplicationException("Could not retrieve package.", ex);
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
	public void update(Package transientObject) throws Exception  {
		Session session = HibernateSessionUtil.getInstance().getCurrentSession();
		
		Transaction tx = null;		
		try {
			tx = session.beginTransaction();
			session.update(transientObject);
			tx.commit();
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("PackageID", String.valueOf(transientObject.getPackageId()));
			ApplicationException aex = new ApplicationException("Could not update package.", ex);
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
	public List<Package> fetch() throws Exception {
		// TODO Auto-generated method stub
		return new ArrayList<Package>();
	}

	/**
	 * Retrieves all packages that match the provided collection of package ids;
	 * @param packageIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Package> fetch(List<Integer> packageIds) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Package.class)
				.add(Restrictions.in("packageId", packageIds));
			List<Package> packages = criteria.list();
			tx.commit();
		
			return packages;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not retrieve packages.", ex);
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
}
