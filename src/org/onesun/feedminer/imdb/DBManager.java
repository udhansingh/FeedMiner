package org.onesun.feedminer.imdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.onesun.feedminer.app.PropertiesFileTypes;
import org.onesun.feedminer.app.PropertyFilesFactory;
import org.onesun.feedminer.util.Constants;


public class DBManager {
	// Only one shutdown hook needed
	private static boolean shutdownHookAdded = false;
	private static Properties properties = PropertyFilesFactory.getProperties(PropertiesFileTypes.IMDB);
	
	private static final int MAX_CONNECTIONS = 2;
	private static final Logger logger = Logger.getLogger(DBManager.class.getName());

	private int checkedOut = 0;
	private int maxConn = MAX_CONNECTIONS;

	private Vector<Connection> freeConnections = new Vector<Connection>();

	private void init(){
		if(shutdownHookAdded == false) {
			Runtime.getRuntime().addShutdownHook(new DBShutdownHook());
			shutdownHookAdded = true;
		}
	}
	
	public DBManager(int mc) {
		if(mc > 0) {
			maxConn = mc;
		}
		else {
			String value = properties.getProperty(Constants.DB_MAX_CONNECTIONS);

			if(value != null){
				maxConn = Integer.parseInt(value);
			}
		}

		init();
	}

	public synchronized Connection getConnection() {
		Connection conn = null;
		if (freeConnections.size() > 0) {
			// Pick the first Connection in the Vector
			// to get round-robin usage
			conn = (Connection) freeConnections.firstElement();
			freeConnections.removeElementAt(0);
			try {
				if (conn.isClosed()) {
					// Try again recursively
					conn = getConnection();
				}
			}
			catch (SQLException e) {
				logger.error("Removed bad connection");
				// Try again recursively
				conn = getConnection();
			}
		}
		else if (maxConn == 0 || checkedOut < maxConn) {
			conn = newConnection();
		}
		if (conn != null) {
			checkedOut++;
		}
		return conn;
	}

	private Connection newConnection() {
		Connection conn = null;

		try {
			try {
				Class.forName(properties.getProperty(Constants.DB_DRIVER_CLASS));
			} catch (ClassNotFoundException e) {
				logger.error("Class.forName failed for HSQLDB");
				e.printStackTrace();
			}
			conn = DriverManager.getConnection(
					properties.getProperty(Constants.DB_URL), 
					properties.getProperty(Constants.DB_USER), 
					properties.getProperty(Constants.DB_PASSWORD)
				);
		} 
		catch(SQLException e) {
			logger.error("SQLException during newConnection\n" + e.getMessage());
			e.printStackTrace();

			return null;
		} 
		catch(Exception e) {
			logger.error("Exception during newConnection\n" + e.getMessage());
		}

		return conn;
	}

	public synchronized Connection getConnection(long timeout) {
		long startTime = System.currentTimeMillis();
		Connection conn;
		while ((conn = getConnection()) == null) {
			try {
				wait(timeout);
			}
			catch (InterruptedException e) {}
			if ((System.currentTimeMillis() - startTime) >= timeout) {
				// Timeout has expired
				return null;
			}
		}
		return conn;
	}

	public synchronized void freeConnection(Connection conn) {
		// Put the connection at the end of the Vector
		freeConnections.addElement(conn);
		checkedOut--;
		notifyAll();
	}

	public synchronized void release() {
		Enumeration<Connection> allConnections = freeConnections.elements();
		while (allConnections.hasMoreElements()) {
			Connection conn = (Connection) allConnections.nextElement();
			try {
				conn.close();
			}
			catch (SQLException e) {
				logger.error("Can't close connection for pool");
			}
		}
		freeConnections.removeAllElements();
	}


}