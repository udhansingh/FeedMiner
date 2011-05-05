package org.onesun.feedminer.util;

import java.io.File;

public interface Constants {
	public static final String CONFIG_DIR = "etc/config";
	public static final String DATA_DIR = "etc/data";
	public static final String MDF_DIR = CONFIG_DIR + File.separatorChar + "mdf";
	
	public static final String HTTP_TIMEOUT = "HTTP_TIMEOUT";
	public static final String OUTPUT_FILE_STORE = "OutputFileStore";
	
	public static final String FEEDERS_DATA = "FeedersDataFile";
	public static final String GEO_DATA = "GeoDataFile";
	
	public static final String PUBLISHER_NAME = "Publisher";
	public static final String PUBLISHER_URL = "PublisherURL";
	
	public static final String DB_DRIVER_CLASS = "JDBC.DRIVER";
	public static final String DB_USER = "USER";
	public static final String DB_PASSWORD = "PASSWORD";
	public static final String DB_MAX_CONNECTIONS = "CONNECTIONS";
	public static final String DB_URL = "JDBC.URL";
}
