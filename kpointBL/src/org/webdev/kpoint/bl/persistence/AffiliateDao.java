package org.webdev.kpoint.bl.persistence;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.Affiliate;

public class AffiliateDao extends BaseDao implements GenericDao<Affiliate, Integer> {

	private static final KinekLogger logger = new KinekLogger(AffiliateDao.class);
	
	@Override
	public Integer create(Affiliate newInstance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Affiliate persistentObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Affiliate> fetch() throws Exception {
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateSessionUtil.getCurrentSession();
			
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Affiliate.class)				 	        
					 	        .add(Restrictions.eq("display", true));
			
			List<Affiliate> homePartners = criteria.list();
			tx.commit();
			return homePartners;
		}
		catch(Exception ex){
			ApplicationException aex = new ApplicationException("Unable to retrieve a list of affiliates.", ex);
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
	public Affiliate read(Integer id) throws Exception{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Affiliate transientObject) throws Exception{
		// TODO Auto-generated method stub
		
	}

}
