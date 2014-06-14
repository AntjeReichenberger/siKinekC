package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.HibernateSessionUtil;
import org.webdev.kpoint.bl.pojo.News;

public class NewsDao extends BaseDao implements GenericDao<News, Integer> {

	private static final KinekLogger logger = new KinekLogger(NewsDao.class);
	
	@Override
	public Integer create(News newInstance) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			session.save(newInstance);
			tx.commit();
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("NewsTitle", newInstance.getTitle());
			ApplicationException aex = new ApplicationException("Could not create news article.", ex);
			logger.error(aex, logProps);
		
			if(tx != null && tx.isActive()){
				tx.rollback();
			}
			throw aex;
		}
		finally{
			verifyDBState(logger, session, tx);
		}
		
		return newInstance.getId();
	}
	
	@Override
	public News read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object news = session.get(News.class, id);
			tx.commit();
				
			return (News)news;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("NewsID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read news article.", ex);
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
	
	
	/**
	 * Retrieves a list of all News
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<News> fetch() throws Exception {
		List<News> news = new ArrayList<News>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(News.class)
				.addOrder(Order.desc("publishDate"));
			news = criteria.list();
			tx.commit();
			
			return news;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch news articles.", ex);
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
	 * Retrieves a list of all News with the given type id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<News> fetch(int typeId, boolean onlyActive) throws Exception {
		List<News> news = new ArrayList<News>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(News.class)
				.createAlias("type", "t")
				.add(Restrictions.eq("t.id", typeId))
				.addOrder(Order.desc("publishDate"));
			
			if(onlyActive) {
				criteria = criteria.add(Restrictions.eq("isActive",true));
			}
	
			news = criteria.list();
			tx.commit();
			
			return news;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("TypeID", String.valueOf(typeId));
			logProps.put("OnlyActive", String.valueOf(onlyActive));
			ApplicationException aex = new ApplicationException("Could not retrieve news articles.", ex);
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
	
	/**
	 * Retrieves a list of all News that have a publish date later than the recentNewsDate
	 * and are active
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<News> fetch(Date recentNewsDate) throws Exception {
		List<News> news = new ArrayList<News>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(News.class)
				.add(Restrictions.ge("publishDate", recentNewsDate))
				.add(Restrictions.eq("isActive",true))
				.addOrder(Order.desc("publishDate"));
			
			news = criteria.list();
			tx.commit();
			
			return news;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CutOffDate", recentNewsDate.toString());
			ApplicationException aex = new ApplicationException("Could not retrieve news articles.", ex);
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
	public void delete(News persistentObject) throws Exception {
		// TODO Complete the delete() method on NewsDao 
	}
	
	
	@Override
	public void update(News transientObject) throws Exception {
		// TODO Complete the update() method on NewsDao
	}
}
