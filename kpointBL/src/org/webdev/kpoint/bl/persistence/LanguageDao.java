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
import org.webdev.kpoint.bl.pojo.Language;


public class LanguageDao extends BaseDao implements GenericDao<Language, Integer> {

	private static final KinekLogger logger = new KinekLogger(LanguageDao.class);
	
	@Override
	public Integer create(Language newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Language persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Language read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object language = session.get(Language.class, id);
			tx.commit();
			
			return (Language)language;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("LanguageID", id.toString());
			ApplicationException aex = new ApplicationException("Could not retrieve language.", ex);
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
	public void update(Language transientObject) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * Retrieves a list of all supported languages
	 * @return A list of all supported languages
	 */
	@Override @SuppressWarnings("unchecked")
	public List<Language> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Language.class);
			List<Language> languages = criteria.list();
			tx.commit();
			
			return languages;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not retrieve languages.", ex);
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
	 * Retrieves a Map of all available Languages
	 * @return A Map of all available Languages
	 */
	public Map<Integer, Language> fetchMap() throws Exception {
		List<Language> languageList = fetch();
		Map<Integer, Language> languages = new HashMap<Integer, Language>();
		for (Language language : languageList) {
			languages.put(language.getLanguageId(), language);
		}
		
		return languages;
	}
}
