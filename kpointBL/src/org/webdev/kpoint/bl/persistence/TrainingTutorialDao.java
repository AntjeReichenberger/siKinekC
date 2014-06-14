package org.webdev.kpoint.bl.persistence;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.webdev.kpoint.bl.pojo.TrainingTutorial;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;


public class TrainingTutorialDao extends BaseDao implements GenericDao<TrainingTutorial, Integer> {

	private static final KinekLogger logger = new KinekLogger(TrainingTutorialDao.class);
	
	@Override
	public Integer create(TrainingTutorial newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(TrainingTutorial persistentObject) throws Exception {
		// TODO Auto-generated method stub
	}


	/**
	 * Retrieves a list of all TrainingTutorials
	 * @return A list of all TrainingTutorials
	 */
	@Override @SuppressWarnings("unchecked")
	public List<TrainingTutorial> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(TrainingTutorial.class);
			List<TrainingTutorial> tutorials = criteria.list();
			tx.commit();
			
			return tutorials;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of TrainingTutorials.", ex);
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
	public TrainingTutorial read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object tutorial = session.get(TrainingTutorial.class, id);
			tx.commit();
				
			return (TrainingTutorial)tutorial;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("TrainingTutorialID", id.toString());
			ApplicationException aex = new ApplicationException("Unable to retrieve the requested TrainingTutorial.", ex);
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
	public void update(TrainingTutorial transientObject) throws Exception {
		// TODO Auto-generated method stub
	}

}
