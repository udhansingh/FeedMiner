package org.onesun.feedminer.cfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.onesun.feedminer.util.Constants;
import org.onesun.feedminer.util.Utility;


public abstract class PropertiesFileLoader {
	private static final Logger logger = Logger.getLogger(PropertiesFileLoader.class.getName());

	protected boolean justCreated = false;
	protected boolean initialized = false;
	protected Properties properties = new Properties();
	protected String fileName = "";

	public PropertiesFileLoader(String fileName){
		this.fileName = fileName;
		
		preinit();
	}

	// Mandatory: Client must override;
	abstract public void setup();

	// hook: client may not override unless needed
	public void postinit() {}
	
	public void preinit() {
		// Create Configdir
		File f = new File(Constants.CONFIG_DIR);
		if(f.exists() == false) {
			f.mkdir();
		}
	}

	// Can't override
	public final void init(){
		if(Utility.exists(fileName) == true){
			load();
		}
		else {
			justCreated = true;
			setup();

			save();

			load();
		}
	}
	synchronized final public void save(){
		File f = new File(fileName);
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(f);
			if(properties.size() > 0){
				properties.store(fos, "");
			}
			else {
				logger.debug("Properites not setup, not writing to file!");
			}
		} catch (Exception e) {
			logger.error("Error while loading configuration: " + fileName);
			e.printStackTrace();
		} finally {
			try {
				if(fos != null) fos.close();

				if(justCreated == true){
					System.err.println("Config file " + fileName + " was just created, please fill it up before proceeding!");
					System.exit(1);
				}
			} catch (IOException e) {
				logger.error("Error while closing configuration: " + fileName);
				e.printStackTrace();
			}
		}
	}		

	public final void load(){
		FileInputStream fis = null;

		try {
			File f = new File(fileName);
			fis = new FileInputStream(f);
			properties.load(fis);
			
			initialized = true;
		} catch (Exception e) {
			logger.error("Error while loading configuration: " + fileName);
			e.printStackTrace();
		} finally {
			try {
				if(fis != null) fis.close();
			} catch (IOException e) {
				logger.error("Error while closing configuration: " + fileName);
				e.printStackTrace();
			}
		}
	}
	
	public final Properties getProperties() {
		return properties;
	}
	
	public final boolean isInitialized(){
		return initialized;
	}

	public final boolean wasJustCreated() {
		return justCreated;
	}
}
