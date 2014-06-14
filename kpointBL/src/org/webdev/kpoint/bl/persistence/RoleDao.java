package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.Role;

public class RoleDao extends BaseDao implements GenericDao<Role, Integer> {

	private static final KinekLogger logger = new KinekLogger(RoleDao.class);
	
	@Override
	public Integer create(Role newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Role persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	@Override @SuppressWarnings("unchecked")
	public List<Role> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Role.class);
			List<Role> roles = criteria.list();
			tx.commit();
				
			return roles;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch Roles.", ex);
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
	 * Retrieves all the administrator roles with equal or lesser authorization 
	 * privileges to the provided role id.
	 * This prevents DepotAdmins from having access to KinekAdmin roles, etc  
	 * @param roleId Represents the maximum privileges of the roles retrieved. 
	 * @return A list of roles with equal or lesser privileges
	 */
	@SuppressWarnings("unchecked")
	public List<Role> fetch(int roleId) throws Exception {
		
		//depot admin can only create depot staff. If roleId is depotAdmin then allowedRoles will only contain depotStaff.
		//if(roleId == Role.DepotAdmin){
		//	roleId = Role.DepotStaff;
		//}
		
		Collection<Integer> allowedRoles = new ArrayList<Integer>();
		switch (roleId) {
			case Role.KinekAdmin:
				allowedRoles.add(Role.KinekAdmin);
			case Role.ReportAdmin:
				allowedRoles.add(Role.ReportAdmin);
			case Role.DepotAdmin:
				allowedRoles.add(Role.DepotAdmin);
			case Role.DepotStaff:
				allowedRoles.add(Role.DepotStaff);
		}
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Role.class)
				.add(Restrictions.in("roleId", allowedRoles));
	
			List<Role> roles = criteria.list();
			tx.commit();
			
			return roles;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("RoleID", String.valueOf(roleId));
			ApplicationException aex = new ApplicationException("Could not read Role.", ex);
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
	public Role read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object role = session.get(Role.class, id);
			tx.commit();
				
			return (Role)role;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("RoleID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read Role.", ex);
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
	public void update(Role transientObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
