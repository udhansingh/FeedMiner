package org.onesun.feedminer.cfg;

import java.io.File;

import org.onesun.feedminer.util.Constants;


public class IMDBPropertiesFileLoader extends PropertiesFileLoader {
	private static final String IMDB_CONFIG_FILE = Constants.CONFIG_DIR + File.separatorChar + "db.properties";

	public IMDBPropertiesFileLoader(){
		super(IMDB_CONFIG_FILE);
	}
	
	@Override
	public void setup(){
		properties.setProperty(Constants.DB_DRIVER_CLASS, "org.hsqldb.jdbcDriver");
		properties.setProperty(Constants.DB_URL, "jdbc:hsqldb:mem:feed_content_db");
		properties.setProperty(Constants.DB_USER, "sa");
		properties.setProperty(Constants.DB_PASSWORD, "");
		properties.setProperty(Constants.DB_MAX_CONNECTIONS, "2");
	}
}
