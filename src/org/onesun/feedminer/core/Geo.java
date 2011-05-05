package org.onesun.feedminer.core;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.onesun.feedminer.app.PropertiesFileTypes;
import org.onesun.feedminer.app.PropertyFilesFactory;
import org.onesun.feedminer.util.Constants;
import org.onesun.feedminer.util.XMLFileParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class Geo {
	private static final Logger logger = Logger.getLogger(Geo.class.getName());
	private static Properties properties = PropertyFilesFactory.getProperties(PropertiesFileTypes.APPLICATION);

	private static Geo instance = null;
	private NodeList nl = null;
	
	public static Geo getInstance(){
		if(instance == null){
			instance = new Geo();
		}
		
		return instance;
	}
	
	private Geo(){
		loadGeoDataFromXMLFile();
	}
	
	synchronized private void loadGeoDataFromXMLFile(){
		File f = new File(properties.getProperty(Constants.GEO_DATA));
		if(f.exists() == false){
			logger.error("File " + f.getName() + " does not exist! - won't proceed further!");
			return;
		}
		
		XMLFileParser p = new XMLFileParser(f);
		
		nl = p.getNodeListForTag("loc");
	}
	
/*	synchronized private void loadGeoDataFromTextFile(){
		File f = new File(properties.getProperty(Constants.GEO_DATA));
		if(f.exists() == false){
			logger.error("File " + f.getName() + " does not exist! - won't proceed further!");
			return;
		}
		
		try {
			BufferedReader input = new BufferedReader(new FileReader(f));
			
			String line;
			
			while((line = input.readLine()) != null){
				if(line == null || line.trim().length() == 0) continue;

				StringTokenizer st = new StringTokenizer(line, properties.getProperty(Constants.IFS));
				
				if(st != null){
					if(st.countTokens() == 2){
						// Get the geo coding
						// City:Country
						
						String city = st.nextToken();
						String country = st.nextToken();
						
						geo.put(city.trim().toLowerCase(), country.trim());
					}
				}
			}
		} catch (Exception e) {
			logger.debug("Exception occured while loading GEO mapping");
			e.printStackTrace();
		}
	}*/
	
	private String find(String queryResultType, String queryType, String queryValue){
		String returnValue = null;

		if(queryValue != null){
			for (int i=0; i < nl.getLength(); i++) 
			{
				Element element = (Element)nl.item(i);
				
				String rv = XMLFileParser.getAttributeValue(element, queryType);
				if(rv.toLowerCase().contains(queryValue.toLowerCase())){
					returnValue = XMLFileParser.getAttributeValue(element, queryResultType);
					
					logger.debug("GEO Return: " + queryResultType + " Value = " + returnValue + "\t\t\t Query = " + queryType + " Value = " + queryValue);
					
					return returnValue;
				}
			
//			String findValue = element.getAttribute(queryType);
//			if(findValue != null) {
//				returnValue = element.getAttribute(queryResultType);
//				break;
//			}
        	}
		}
		else {
			logger.debug("Cannot query for null value: " + queryResultType + " unknown for " + "\t\t\t Query = " + queryType + " Value = " + queryValue);
		}
		
		return returnValue;
	}
	
	public String getLocationName(String queryResultType, String queryType, String queryValue){
		return(find(
				queryResultType.toLowerCase(),
				queryType.toLowerCase(),
				queryValue)
			);
	}
}
