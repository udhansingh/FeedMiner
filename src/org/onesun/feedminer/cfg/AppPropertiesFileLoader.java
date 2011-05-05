package org.onesun.feedminer.cfg;

import java.io.File;

import org.onesun.feedminer.util.Constants;


public class AppPropertiesFileLoader extends PropertiesFileLoader {
	private static final String appConfigFile = Constants.CONFIG_DIR + File.separatorChar + "app-config.properties";

	public AppPropertiesFileLoader(){
		super(appConfigFile);
	}
	
	@Override
	public void setup(){
		properties.setProperty(Constants.PUBLISHER_NAME, "YOUR_COMPANY_NAME_HERE");
		properties.setProperty(Constants.PUBLISHER_URL, "YOUR_COMPANY_URL_HERE");
		properties.setProperty(Constants.OUTPUT_FILE_STORE, "output.fs");
		properties.setProperty(Constants.FEEDERS_DATA, "feeders.xml");
		properties.setProperty(Constants.GEO_DATA, "geo.xml");
		properties.setProperty(Constants.HTTP_TIMEOUT, "5");
	}
	
	@Override
	public void postinit(){
		// User Output dir: Only required for AppConfiguration
		File f = new File(properties.getProperty(Constants.OUTPUT_FILE_STORE));
		if(f.exists() == false){
			f.mkdir();
		}
	}
}
