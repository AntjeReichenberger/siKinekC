package org.webdev.kpoint.bl.persistence;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;

public class BaseDao {

	protected void verifyDBState(KinekLogger logger, Session session, Transaction tx) throws Exception{
		if(session != null && session.isOpen()){
			session.close();
		}

		if(tx != null && tx.isActive()){
			//rollback error
			ApplicationException aex = new ApplicationException("Database transaction was not closed properly.");
			logger.error(aex);
			throw aex;
		}
		if(session != null && session.isOpen()){
			//session error
			ApplicationException aex = new ApplicationException("Database session was not closed properly.");
			logger.error(aex);
			throw aex;
		}
	}

}
