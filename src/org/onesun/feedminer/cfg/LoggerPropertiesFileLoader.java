package org.onesun.feedminer.cfg;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.onesun.feedminer.util.Constants;


public class LoggerPropertiesFileLoader extends PropertiesFileLoader {
	private static final String LOGGER_CONFIG_FILE = Constants.CONFIG_DIR + File.separatorChar + "logger-config.properties";

	public LoggerPropertiesFileLoader(){
		super(LOGGER_CONFIG_FILE);
	}
	
	@Override
	public void setup(){
		properties.setProperty("log4j.rootLogger", "debug, FILE");
		properties.setProperty("log4j.appender.FILE", "org.apache.log4j.FileAppender");
		properties.setProperty("log4j.appender.FILE.layout", "org.apache.log4j.PatternLayout");
		properties.setProperty("log4j.appender.FILE.File", "logs/diagnostic.log");
		properties.setProperty("log4j.appender.FILE.layout.ConversionPattern", "%d{ABSOLUTE} %5p %c{1}:%L - %m%n");
	}
	
	@Override
	public void postinit(){
		PropertyConfigurator.configure(properties);
	}
}
