package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Organization;

public class OrganizationDao extends BaseDao implements GenericDao<Organization, Integer> {

	private static final KinekLogger logger = new KinekLogger(Organization.class);
	
	@Override
	public Integer create(Organization newInstance) throws Exception {
		// TODO Auto-generated method stub
		
		Session session = null;
		Transaction tx = null;
		
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			
			session.save(newInstance);
			
			tx.commit();
			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create a new Organization.", ex);
			logger.error(aex);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
		
		return newInstance.getOrganizationId();
	}

	@Override
	public void delete(Organization persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Retrieves a list of all supported organizations
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Organization> fetch() throws Exception {
		// TODO Auto-generated method stub
		
		Session session = null;
		Transaction tx = null;
		List<Organization> orgs = null;
		
		try{
		
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			
				Criteria criteria = session.createCriteria(Organization.class)
									.addOrder(Order.asc("name"));
				orgs = criteria.list();
				
				if(orgs == null){
					orgs = new ArrayList<Organization>();
				}
				
			tx.commit();
			
			return orgs;
			
		}catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of organizations.", ex);
			logger.error(aex);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
	}

	@Override
	public Organization read(Integer id) throws Exception {
		// TODO Auto-generated method stub
		
		Session session = null;
		Transaction tx = null;
		
		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			
				Organization org = (Organization)session.get(Organization.class, id);
			
			tx.commit();
			
			return org;
			
		}catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("OrganizationID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested organization.", ex);			
			logger.error(aex, logProps);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public Organization read(String name) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Organization.class)
				.add(Restrictions.eq("name", name))			
				.setMaxResults(1);
			List<Organization> orgList = criteria.list();
			tx.commit();
			
			Organization org = orgList.size() > 0 ? orgList.get(0) : null;
			
			return org;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Organization Name", name);
			ApplicationException aex = new ApplicationException("Unable to retrieve the organization at the given name.", ex);
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
	public void update(Organization transientObject) throws Exception {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		
		try{
			
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();		
			session.update(transientObject);			
			tx.commit();
			
		}catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Organization Id", String.valueOf(transientObject.getOrganizationId()));
			ApplicationException aex = new ApplicationException("Could not update the Organization.", ex);
			logger.error(aex, logProps);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}		
		
	}

	
	
}
