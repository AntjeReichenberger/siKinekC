package org.webdev.kpoint.bl.persistence;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.Country;
import org.webdev.kpoint.bl.pojo.State;

public class StateDao extends BaseDao implements GenericDao<State, Integer> {

	private static final KinekLogger logger = new KinekLogger(StateDao.class);

	@Override
	public Integer create(State newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(State persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public State read(Integer id) throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Object state = session.get(State.class, id);
			tx.commit();
				
			return (State)state;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("StateID", id.toString());
			ApplicationException aex = new ApplicationException("Could not read State.", ex);
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
	public State read(String stateName) throws Exception {
		List<State> states = new ArrayList<State>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(State.class)
				.add(Restrictions.eq("name", stateName));
			states = (List<State>)criteria.list();
			tx.commit();
			
			State state = null;
			if (!states.isEmpty()) {
				state = states.get(0);
			}
			
			return state;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("StateName", stateName);
			ApplicationException aex = new ApplicationException("Could not read State.", ex);
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
	public State readFromStateProvCode(String stateProvCode) throws Exception {
		List<State> states = new ArrayList<State>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(State.class)
				.add(Restrictions.eq("stateProvCode", stateProvCode));
			states = (List<State>)criteria.list();
			tx.commit();
			
			State state = null;
			if (!states.isEmpty()) {
				state = states.get(0);
			}
			
			return state;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("StateProvCode", stateProvCode);
			ApplicationException aex = new ApplicationException("Could not read State.", ex);
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
	public void update(State transientObject) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override @SuppressWarnings("unchecked")
	public List<State> fetch() throws Exception {
		List<State> states = new ArrayList<State>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(State.class);
			states = (List<State>)criteria.list();
			tx.commit();
				
			return states;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not fetch States.", ex);
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
	 * Retrieves a list of states or provinces that belong to the provided country
	 * @param c The country to retrieve the states or provinces for
	 * @return A list of states or provinces mapped to the provided country
	 */
	@SuppressWarnings("unchecked")
	public List<State> fetch(Country c) throws Exception {

		List<State> states = new ArrayList<State>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();		
			Criteria criteria = session.createCriteria(State.class)
				.add(Restrictions.eq("country", c))
				.addOrder(Order.asc("name"));
			states = (List<State>)criteria.list();
			tx.commit();
			
			return states;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CountryID", String.valueOf(c.getCountryId()));
			ApplicationException aex = new ApplicationException("Could not read States based on Country.", ex);
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
	 * Retrieves a list of states or provinces that belong to the provided country
	 * @param c The country to retrieve the states or provinces for
	 * @return A list of states or provinces mapped to the provided country
	 */
	@SuppressWarnings("unchecked")
	public List<State> fetchBorderStates(Country c) throws Exception {
		
		List<State> states = new ArrayList<State>();
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(State.class)
				.add(Restrictions.eq("country", c))
				.add(Restrictions.eq("isBorderState", true))
				.addOrder(Order.asc("name"));
			states = (List<State>)criteria.list();
			tx.commit();
			
			return states;
		}
		catch(Exception ex){
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("CountryID", String.valueOf(c.getCountryId()));
			ApplicationException aex = new ApplicationException("Could not read Border States based on Country.", ex);
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
	 * Retrieves a list of states that have a depot mapped to them
	 * TODO: Figure out the "Hibernate" method of doing this
	 * @return A list of states that have a depot mapped to them
	 */
	@SuppressWarnings("unchecked")
	public List<State> fetchActive() throws Exception {
		String sql = "select * from state s where s.stateId in (select distinct(stateId) from KPDepot)";
		
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			List<State> states = session.createSQLQuery(sql).addEntity(State.class).list();
			tx.commit();
				
			return states;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Could not read active States.", ex);
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
}
