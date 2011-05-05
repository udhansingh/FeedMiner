package org.onesun.feedminer.app;

import java.util.HashMap;
import java.util.Properties;

import org.onesun.feedminer.cfg.AppPropertiesFileLoader;
import org.onesun.feedminer.cfg.IMDBPropertiesFileLoader;
import org.onesun.feedminer.cfg.LoggerPropertiesFileLoader;
import org.onesun.feedminer.cfg.PropertiesFileLoader;


public class PropertyFilesFactory {
	private static HashMap<PropertiesFileTypes, PropertiesFileLoader> props = new HashMap<PropertiesFileTypes, PropertiesFileLoader>();
	
	public static void init(){
		if(props.size() == 0){
			// Create Configuration
			props.put(PropertiesFileTypes.APPLICATION, new AppPropertiesFileLoader());
			props.put(PropertiesFileTypes.LOGGER, new LoggerPropertiesFileLoader());
			props.put(PropertiesFileTypes.IMDB, new IMDBPropertiesFileLoader());
			// props.put(PropertiesFile.MAPPER, new MapperPropertiesFileLoader());

			// initialize configuration
			for(PropertiesFileTypes key : props.keySet()){
				props.get(key).init();
			}
			
			// post initialization
			for(PropertiesFileTypes key : props.keySet()){
				props.get(key).postinit();
			}
		}
	}
	
	private static PropertiesFileLoader getConfig(PropertiesFileTypes ct){
		return props.get(ct);
	}
	
	public static Properties getProperties(PropertiesFileTypes ct){
		if(ct == null) return null;
		else return getConfig(ct).getProperties();
	}
}
