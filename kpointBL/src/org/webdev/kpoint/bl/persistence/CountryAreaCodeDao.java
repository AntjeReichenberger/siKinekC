package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.CountryAreaCode;

public class CountryAreaCodeDao extends BaseDao implements GenericDao<CountryAreaCode, Integer> {

	private static final KinekLogger logger = new KinekLogger(CountryAreaCodeDao.class);
	
	@Override
	public Integer create(CountryAreaCode newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(CountryAreaCode persistentObject) throws Exception {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	public String read(String areaCode) throws Exception {
		String countryCode = null;
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(CountryAreaCode.class)
				.add(Restrictions.eq("areaCode", areaCode));
			List<CountryAreaCode> countryAreaCodes = (List<CountryAreaCode>)criteria.list();
			tx.commit();
						
			if (!countryAreaCodes.isEmpty()) {
				//there will only be one country in this list
				CountryAreaCode countryAreaCode = countryAreaCodes.get(0);
				countryCode = countryAreaCode.getCountryCode();
			}
			
			return countryCode;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CountryCode", countryCode);
			ApplicationException aex = new ApplicationException("Could not find Country Area Code.", ex);
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
	public void update(CountryAreaCode transientObject) throws Exception {
		// TODO Auto-generated method stub	
	}

	@Override
	public CountryAreaCode read(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CountryAreaCode> fetch() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
