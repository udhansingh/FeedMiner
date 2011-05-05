package org.onesun.feedminer.imdb;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DBShutdownHook extends Thread {
	private static final Logger logger = Logger.getLogger(DBShutdownHook.class.getName());

    public void run() {
		try {
			DBManager dbm = new DBManager(1);
			
			Connection conn = dbm.getConnection();
			String sql = "SHUTDOWN";

			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
			stmt.close();
			dbm.release();
		}
		catch(Exception e){
			logger.error("Exception while shutting down DB");
			e.printStackTrace();
		}
		finally {
			logger.info("Database Successfully shutdown");
		}
	}
}