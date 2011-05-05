package org.onesun.feedminer.core;

import java.util.Properties;

import org.onesun.feedminer.app.PropertiesFileTypes;
import org.onesun.feedminer.app.PropertyFilesFactory;
import org.onesun.feedminer.util.Constants;


public class PublisherInfo {
	private static Properties properties = PropertyFilesFactory.getProperties(PropertiesFileTypes.APPLICATION);

	private static boolean initialized = false;
	
	public static boolean isInitialized(){
		return initialized;
	}
	
	private static String publisherURL;
	private static String publisherName;
	
	public static void init(){
		publisherName = properties.getProperty(Constants.PUBLISHER_NAME);
		publisherURL = properties.getProperty(Constants.PUBLISHER_URL);
		
		initialized = true;
	}
	
	public static String getPublisherName(){
		return publisherName;
	}
	
	public static String getPublisherURL(){
		return publisherURL;
	}
}
