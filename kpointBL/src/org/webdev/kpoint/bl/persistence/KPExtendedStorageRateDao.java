package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.KPExtendedStorageRate;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.PackageWeightGroup;
import org.webdev.kpoint.bl.pojo.StorageDuration;
import org.webdev.kpoint.bl.pojo.UnifiedExtendedStorageRate;

public class KPExtendedStorageRateDao extends BaseDao implements GenericDao<KPExtendedStorageRate, Integer> {
	
	private static final KinekLogger logger = new KinekLogger(KPExtendedStorageRateDao.class);
	
	@Override
	public Integer create(KPExtendedStorageRate newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to create a new KPExtendedStorage entry.", ex);
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
	public void delete(KPExtendedStorageRate persistentObject) throws Exception {
		Session session = null;
		Transaction tx = null;

		try{
			session = HibernateSessionUtil.getCurrentSession();
			tx = session.beginTransaction();
			session.delete(persistentObject);
			tx.commit();	
		}catch(Exception ex){	
			ApplicationException aex = new ApplicationException("Unable to delete selected extended storage rate", ex);
			logger.error(aex);
			//TODO	logger.error("selected package rate for delete operation. Rate ID: "+ persistentObject.getUnifiedExtendedStorageRate().getId() 
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
	public List<KPExtendedStorageRate> fetch() throws Exception {
		// TODO Auto-generated method stub	
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<KPExtendedStorageRate> fetch(KinekPoint kp) throws Exception{
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(KPExtendedStorageRate.class)
				.addOrder(Order.asc("id"))
				.add(Restrictions.eq("kinekPointId", kp));
			List<KPExtendedStorageRate> kpExtendedStorageRates = criteria.list();
		
			tx.commit();
			
			return kpExtendedStorageRates;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of KPExtendedStorageRates", ex);
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
	
	@SuppressWarnings("unchecked")
	public List<KPExtendedStorageRate> fetchFiltered(KinekPoint kp) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(KPExtendedStorageRate.class)
				.addOrder(Order.asc("id"))
				.add(Restrictions.eq("kinekPointId", kp));
			List<KPExtendedStorageRate> kpExtendedStorageRates = criteria.list();
		
			tx.commit();
			
			return filterExtendedRates(kpExtendedStorageRates);
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of KPExtendedStorageRates", ex);
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
	
	//Fetch the relevant KPExtendedStorageRates for the number of days
	@SuppressWarnings("unchecked")
	public List<KPExtendedStorageRate> fetch(KinekPoint kp, Date dateInEffect, PackageWeightGroup packageWeightGroup, int days) throws Exception {
		Session session = null;
		Transaction tx = null;
		
		List<StorageDuration> durations = new StorageDurationDao().fetch(days);	
		List<UnifiedExtendedStorageRate> unifiedExtendedStorageRates = new UnifiedExtendedStorageRateDao().fetch(durations,packageWeightGroup.getSingleStorageWeightGroup());
		
		try{
			session = HibernateSessionUtil.getCurrentSession();
			//TODO createdDate filter
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateInEffect);
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(KPExtendedStorageRate.class)
				.add(Restrictions.le("createdDate",cal))
				.addOrder(Order.asc("id"))
				.add(Restrictions.eq("kinekPointId", kp.getDepotId()))
				.add(Restrictions.in("unifiedExtendedStorageRate", unifiedExtendedStorageRates));
				
			List<KPExtendedStorageRate> kpExtendedStorageRates = criteria.list();
			tx.commit();	
			
			for(int i = 0; i < kpExtendedStorageRates.size();i++){
				if(i < kpExtendedStorageRates.size()-1){
					if(kpExtendedStorageRates.get(i).getUnifiedExtendedStorageRate().getId() == kpExtendedStorageRates.get(i+1).getUnifiedExtendedStorageRate().getId()){
						if(kpExtendedStorageRates.get(i).getCreatedDate().compareTo(kpExtendedStorageRates.get(i+1).getCreatedDate()) < 0){
							kpExtendedStorageRates.remove(i);
							i--;
						}
						else{
							kpExtendedStorageRates.remove(i+1);
						}
					}			
				}
			}
			
			return kpExtendedStorageRates;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of KPExtendedStorageRates", ex);
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
	public KPExtendedStorageRate read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object depot = session.get(KPExtendedStorageRate.class, id);
			tx.commit();
			
			return (KPExtendedStorageRate)depot;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested KPExtendedStorageRate.", ex);
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
	public void update(KPExtendedStorageRate transientObject) throws Exception {
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
		//TODO	logProps.put("ID", String.valueOf(transientObject.getKinekPoint().getDepotId()));
			ApplicationException aex = new ApplicationException("Unable to update the KPExtendedStorageRate entry.", ex);
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
	
	
	public List<KPExtendedStorageRate> filterExtendedRates(List<KPExtendedStorageRate> kpExtended) throws Exception {
		
		//newer dates appear before older dates. The second, third, fourth, etc entries for the same id will not be added back to the set
		Collections.sort(kpExtended);
		
		List<KPExtendedStorageRate> filteredRates = new ArrayList<KPExtendedStorageRate>();
		for(int i = 0; i < kpExtended.size();i++){
			if(i > 0){
				//The second, third, fourth, etc entries for the same id will not be added back to the set
				if(kpExtended.get(i).getUnifiedExtendedStorageRate().getId() != kpExtended.get(i-1).getUnifiedExtendedStorageRate().getId()){					
					filteredRates.add(kpExtended.get(i));
				}
			}
			else{
				//Always add the first list element
				filteredRates.add(kpExtended.get(i));
			}	
		}
		return filteredRates;
	}
	
}

