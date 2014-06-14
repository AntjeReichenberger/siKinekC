package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.LandingPage;


public class LandingPageDao extends BaseDao implements GenericDao<LandingPage, Integer>{

	private static final KinekLogger logger = new KinekLogger(LandingPageDao.class);
	
	@Override
	public Integer create(LandingPage newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(LandingPage persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LandingPage> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(LandingPageDao.class);
			List<LandingPage> borderLandingPageList = criteria.list();
			tx.commit();
	
			return borderLandingPageList;	
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not retrieve landing pages.", ex);
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
	public LandingPage read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object borderLandingPages = session.get(LandingPageDao.class,id);
			tx.commit();
			
			return (LandingPage)borderLandingPages;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("LandingPageID", id.toString());
			ApplicationException aex = new ApplicationException("Could not retrieve landing pages.", ex);
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
	public LandingPage read(String kpIdentifier) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(LandingPage.class)
				.add(Restrictions.eq("kpIdentifier", kpIdentifier.toLowerCase()))
				.setMaxResults(1);
			List<LandingPage> borderLandingPageList = criteria.list();
			tx.commit();
			
			LandingPage landingPage = borderLandingPageList.size() > 0 ? borderLandingPageList.get(0) : null;
			
			return landingPage;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("KPIdentifier", kpIdentifier);
			ApplicationException aex = new ApplicationException("Could not retrieve landing pages.", ex);
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
	public void update(LandingPage transientObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
