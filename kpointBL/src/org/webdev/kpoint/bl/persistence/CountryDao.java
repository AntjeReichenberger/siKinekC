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
import org.webdev.kpoint.bl.pojo.Country;
import org.webdev.kpoint.bl.pojo.State;

public class CountryDao extends BaseDao implements GenericDao<Country, Integer> {

	private static final KinekLogger logger = new KinekLogger(CountryDao.class);
	
	@Override
	public Integer create(Country newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Country persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}


    /**
     * Retrieve a Country that was previously persisted to the database using
     * the indicated id as primary key
     * @param id The id of the object to retrieve
     * @return The object matching the supplied id; null if none exist
     */
	public Country read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object country = session.get(Country.class, id);
			tx.commit();
			
			return (Country)country;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CountryID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested country.", ex);
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

	@SuppressWarnings("unchecked")
	public Country readFromCountryCode(String countryCode) throws Exception {
		List<Country> countries = new ArrayList<Country>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Country.class)
				.add(Restrictions.eq("countryCode", countryCode));
			countries = (List<Country>)criteria.list();
			tx.commit();
			
			Country country = null;
			if (!countries.isEmpty()) {
				//there will only be one country in this list
				country = countries.get(0);
			}
			
			return country;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CountryCode", countryCode);
			ApplicationException aex = new ApplicationException("Could not read Country.", ex);
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
	public void update(Country transientObject) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Retrieves a list of all supported countries
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Country> fetch() throws Exception {
		List<Country> countries = new ArrayList<Country>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Country.class);
			countries = criteria.list();
			tx.commit();
			
			return countries;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of countries.", ex);
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
}
