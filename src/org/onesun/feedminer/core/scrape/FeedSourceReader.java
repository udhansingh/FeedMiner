package org.onesun.feedminer.core.scrape;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.onesun.feedminer.app.PropertiesFileTypes;
import org.onesun.feedminer.app.PropertyFilesFactory;
import org.onesun.feedminer.util.Constants;
import org.onesun.feedminer.util.XMLFileParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class FeedSourceReader {
	private static final Logger logger = Logger.getLogger(FeedSourceReader.class.getName());
	
	private static Properties properties = PropertyFilesFactory.getProperties(PropertiesFileTypes.APPLICATION);

	private HashMap<String, String> sources = new HashMap<String, String>();
	
	private void put(String key, String val){
		sources.put(key.trim().toLowerCase(), val.trim());
	}
	
	synchronized private void init(){
		File f = new File(properties.getProperty(Constants.FEEDERS_DATA));
		if(f.exists() == false){
			logger.error("FATAL ERROR: File " + f.getName() + " does not exist! - won't proceed further!");
			
			System.exit(1);
		}
		
		String fileName = f.getName();
		String ext = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
		
		if(ext.compareToIgnoreCase("xml") == 0){
			loadXMLFile(f);
		}
		else {
			logger.error("FATAL ERROR: File extensions xml are only supported formats!");
			System.exit(1);
		}
	}
	
	private void loadXMLFile(File f){
		XMLFileParser p = new XMLFileParser(f);
		
		NodeList nl = p.getNodeListForTag("feeder");
		
		for (int i=0; i<nl.getLength(); i++) 
		{
			Element element = (Element)nl.item(i);
			
			String name = element.getAttribute("name");
			String escUrl = element.getAttribute("url");
			
			// Unescape XML characters: The input xml file has escaped URL
			String url = StringEscapeUtils.unescapeXml(escUrl);
			
			logger.debug("Extracted: name = " + name + " << ---- >> url = " + url);
			put(name, url);
		}
	}
	
	public FeedSourceReader()
	{
		logger.debug("Started Loading FeedSources");
		init();
		logger.debug("FeedSourced loading completed");
	}
	
	public HashMap<String, String> getFeedSources()
	{
		return sources;
	}
}
