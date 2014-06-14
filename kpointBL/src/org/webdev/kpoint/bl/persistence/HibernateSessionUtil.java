package org.webdev.kpoint.bl.persistence;

import java.io.File;
import java.net.URL;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.LoadEvent;
import org.hibernate.event.LoadEventListener;
import org.hibernate.event.def.DefaultLoadEventListener;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.repackage.cglib.proxy.Enhancer;
import org.hibernate.stat.Statistics;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;


/**
 * This class guarantees that only one single SessionFactory is instantiated and
 * that the configuration is done thread safe as singleton. Actually it only
 * wraps the Hibernate SessionFactory. When a JNDI name is configured the
 * session is bound to to JNDI, else it is only saved locally. You are free to
 * use any kind of JTA or Thread transactionFactories.
 */
public class HibernateSessionUtil {

	/** The single instance of hibernate configuration */
	private static Configuration cfg = new Configuration();

	/** The single instance of hibernate SessionFactory */
	private static org.hibernate.SessionFactory sessionFactory;

	private static Statistics stats;
	

	/**
	 * Default constructor.
	 */
	private HibernateSessionUtil() {

	}
	
	/**
	 * Initializes the configuration if not yet done and returns the current
	 * instance
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static SessionFactory getInstance() {
		if (sessionFactory == null)
			initSessionFactory();
		return sessionFactory;
	}

	/**
	 * The behavior of this method depends on the session context you have
	 * configured. This factory is intended to be used with a hibernate.cfg.xml
	 * including the following property <property
	 * name="current_session_context_class">thread</property> This would return
	 * the current open session or if this does not exist, will create a new
	 * session
	 * 
	 * @return
	 */
	public static Session getCurrentSession() {
		// Number of connection requests. Note that this number represents 
		// the number of times Hibernate asked for a connection, and 
		// NOT the number of connections (which is determined by your 
		// pooling mechanism).
		//System.out.println("STATS-ConnectCount:" + stats.getConnectCount());
		// Number of flushes done on the session (either by client code or 
		// by hibernate).
		//System.out.println("STATS-FlushCount:" + stats.getFlushCount());
		// The number of completed transactions (failed and successful).
		//System.out.println("STATS-TransactionCount:" + stats.getTransactionCount());
		// The number of transactions completed without failure
		//System.out.println("STATS-SuccessfulTransactionCount:" + stats.getSuccessfulTransactionCount());
		// The number of sessions your code has opened.
		//System.out.println("STATS-SessionOpenCount:" + stats.getSessionOpenCount());
		// The number of sessions your code has closed.
		//System.out.println("STATS-SessionCloseCount:" + stats.getSessionCloseCount());

		//return sessionFactory.getCurrentSession();
		return getInstance().getCurrentSession();
	}

	/**
	 * Initializes the sessionFactory in a safe way even if more than one thread
	 * tries to build a sessionFactory
	 */
	private static synchronized void initSessionFactory() {
		if (sessionFactory == null) {
			try {
				URL url = HibernateSessionUtil.class.getResource("/hibernate.cfg.xml");
				File f = new File(url.getFile());
				System.out.println("HIB CONF LOCATION:" + f.getAbsolutePath());

				cfg = new Configuration().configure();
				cfg = cfg.setProperty("hibernate.connection.url", ExternalSettingsManager.getDatabaseConnectionString());
				cfg = cfg.setProperty("hibernate.connection.username", ExternalSettingsManager.getDatabaseUsername());
				cfg = cfg.setProperty("hibernate.connection.password", ExternalSettingsManager.getDatabasePassword());

				System.out.println(cfg.getProperties().toString());
				 //assumes an instantiated Configuration variable called cfg

				// https://hibernate.atlassian.net/browse/HHH-2481
				cfg.setListeners("load", new LoadEventListener [] {new DefaultLoadEventListener(), new LoadEventListener() {
		            public void onLoad(LoadEvent event, LoadType loadType) throws HibernateException {
		                Object obj = event.getResult();
		                if (obj instanceof HibernateProxy) {
		                    Enhancer.registerCallbacks(obj.getClass(),null);
		                }
		            }
		        }});
				
				sessionFactory = cfg.buildSessionFactory();

				//stats = sessionFactory.getStatistics();
				//stats.setStatisticsEnabled(true);
			} catch (Exception e) {
				System.err.println("%%%% Error Creating HibernateSessionFactory %%%%");
				System.err.println(e.getMessage());
				throw new HibernateException(e.getMessage());
			}
		}
	}

	public static void close() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
		sessionFactory = null;
	}
}