package org.webdev.kpoint.bl.persistence;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.CreditCard;


public class CreditCardDao extends BaseDao implements GenericDao<CreditCard, Integer> {

	private static final KinekLogger logger = new KinekLogger(CreditCardDao.class);

	
	@Override
	public Integer create(CreditCard newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(CreditCard persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CreditCard read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object creditCard = session.get(CreditCard.class,id);
			tx.commit();
				
			return (CreditCard)creditCard;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CreditCardID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested Credit Card.", ex);
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
	public void update(CreditCard transientObject) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Retrieves a list of all supported credit card types
	 * @return A list of all supported credit card types
	 */
	@SuppressWarnings("unchecked")
	public List<CreditCard> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(CreditCard.class);
			List<CreditCard> creditCards = criteria.list();
			tx.commit();
			
			return creditCards;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of Credit Cards.", ex);
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
	
	/**
	 * Retrieves a Map of all available Credit Cards
	 * @return A Map of all available Credit Cards
	 */
	public Map<Integer, CreditCard> fetchMap() throws Exception {
		List<CreditCard> creditCardList = fetch();
		Map<Integer, CreditCard> creditCards = new HashMap<Integer, CreditCard>();
		for (CreditCard creditCard : creditCardList) {
			creditCards.put(creditCard.getCardId(), creditCard);
		}
		
		return creditCards;
	}
}
