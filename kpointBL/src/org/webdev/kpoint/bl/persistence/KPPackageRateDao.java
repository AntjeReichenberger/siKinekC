package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.KPPackageRate;
import org.webdev.kpoint.bl.pojo.KinekPoint;

public class KPPackageRateDao extends BaseDao implements GenericDao<KPPackageRate, Integer> {
	
	private static final KinekLogger logger = new KinekLogger(KPPackageRateDao.class);
	@Override
	public Integer create(KPPackageRate newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create a new KPPackageRate entry.", ex);
			logger.error(aex);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		//TODO
		return 0;
	}

	@Override
	public void delete(KPPackageRate persistentObject) throws Exception {
		Session session = null;
		Transaction tx = null;

		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			session.delete(persistentObject);
			tx.commit();		
		}catch(Exception ex){	
			ApplicationException aex = new ApplicationException("Unable to delete selected package rate", ex);
			logger.error(aex);
			//TODO		logger.error("selected package rate for delete operation. Rate ID: "+ persistentObject.getUnifiedPackageRate().getId() 
			//TODO		+ "KinekPointID: "+ persistentObject.getKinekPoint().getDepotId());
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}finally{
			verifyDBState(logger, session, tx);
		}		
	}

	
	@Override
	public List<KPPackageRate> fetch() throws Exception {
		// TODO Auto-generated method stub	
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<KPPackageRate> fetch(KinekPoint kp) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(KPPackageRate.class)
				.addOrder(Order.asc("id"))
				.add(Restrictions.eq("kinekPointId", kp.getDepotId()));
			List<KPPackageRate> kpPackageRates = criteria.list();
			tx.commit();
						
			return kpPackageRates;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of KPPackageRates.", ex);
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
	public KPPackageRate read(Integer id) throws Exception {
		// TODO Auto-generated method stub	
		return null;
	}
	
	@Override
	public void update(KPPackageRate transientObject) throws Exception {
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
			//TODO	logProps.put("KinekPointID", String.valueOf(transientObject.getKinekPoint().getDepotId()));
			ApplicationException aex = new ApplicationException("Unable to update a KPPackageRate.", ex);
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

