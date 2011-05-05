package org.onesun.feedminer.core.scrape;

import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.onesun.feedminer.app.PropertiesFileTypes;
import org.onesun.feedminer.app.PropertyFilesFactory;
import org.onesun.feedminer.tags.MappingSymantics;
import org.onesun.feedminer.tags.ScrapeMappingTechnique;
import org.onesun.feedminer.util.Constants;


public class ContentScrapper {
	private static final String MAIN_DIV = "table";
	private static final String SUB_DIV_IT = "tr";
	private static final String SUB_DIV_EX = "td";
	
	private static final Logger logger = Logger.getLogger(ContentScrapper.class.getName());

	private static final ScrapeMappingTechnique mapping = new ScrapeMappingTechnique();
	
	private HashMap<String, String> contents = new HashMap<String, String>();

	private void initHttpTimeout(){
		if(timeoutInitialized == false) {
			Properties properties = PropertyFilesFactory.getProperties(PropertiesFileTypes.APPLICATION);
			
			String sTimeout = properties.getProperty(Constants.HTTP_TIMEOUT);
			int timeout = 5;
			
			if(sTimeout != null){
				timeout = Integer.parseInt(sTimeout);
				
				if(timeout <= 0){
					timeout = 5;
				}
			}
			
			TIMEOUT = (timeout * 1000);
			logger.debug("HTTP Timeout is set to " + TIMEOUT);
			
			timeoutInitialized = true;
		}
	}
	public ContentScrapper(String urlString){
		initHttpTimeout();
		
		// Start the actual task
		extract(urlString);
	}

	private static boolean timeoutInitialized = false;
	private static int TIMEOUT = 0;
	
	private void extract(String urlString) {
		try {
			URL url = new URL(urlString);
			contents.put(MappingSymantics.URL, urlString);

			Document doc = Jsoup.parse(url, TIMEOUT);
			Elements tables = doc.getElementsByTag(MAIN_DIV);
			Element rtable = tables.get(tables.size() - 1);
			Elements rows = rtable.getElementsByTag(SUB_DIV_IT);

			for (int rowIdx = 0; rowIdx < rows.size(); rowIdx++) {
				Elements cols = rows.get(rowIdx).getElementsByTag(SUB_DIV_EX);

				Element k = cols.get(0);
				Element v = cols.get(1);

				String key = k.text().trim();
				String val = v.text().trim();
				String mkey = mapping.get(key);
				

				if(mkey != null && mkey.length() > 0){
					key = mkey;
					
					logger.debug("Extracted Mapping ---> " + mkey + " <<---->> " + val);
				}
				
				contents.put(key, val);
			}
		} catch (Exception e) {
			logger.error("Exception occured while extracting page: " + urlString);	
			e.printStackTrace();
		}
	}

	public HashMap<String, String> getContents() {
		return contents;
	}
}
