package org.onesun.feedminer.tags;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;
import org.onesun.feedminer.pojo.Mapping;
import org.onesun.feedminer.pojo.MappingArgs;
import org.onesun.feedminer.util.XMLFileParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class TransformMappingTechnique {
	private static final Logger logger = Logger.getLogger(TransformMappingTechnique.class.getName());

	private HashMap<String, Mapping> mapping = new HashMap<String, Mapping>();
	private NodeList nodes = null;
	private XMLFileParser parser = null;
	
	public TransformMappingTechnique(String fileName){
		init(fileName);
	}
	
	public ArrayList<MappingArgs> getAdditionalArgs(String whichType){
		NodeList nl = parser.getNodeListForTag("child");
		
		ArrayList<MappingArgs> margs = null;
		if(nl.getLength() > 0) margs = new ArrayList<MappingArgs>();
		
		logger.debug("Number of args to process is: " + nl.getLength());
		for (int i=0; i < nl.getLength(); i++){
			Element e = (Element)nl.item(i);
			
			String type = e.getAttribute("type");
			// Process only the type that is asked for?
			if(type.compareTo(whichType) == 0){
				String tag = e.getAttribute("tag");
				String link = e.getAttribute("link");
				String findBy = e.getAttribute("findBy");
				String enabled = e.getAttribute("enabled");
				String is_cdata = e.getAttribute("is_cdata");
				
				boolean isEnabled = Boolean.parseBoolean(enabled);
				boolean isCdata = Boolean.parseBoolean(is_cdata);
				logger.debug("Additional Args: isEnabled=" + isEnabled + " || isCdata=" +
						is_cdata + " || tag=" + tag + " || link=" + link);
				
				MappingArgs ma = new MappingArgs(tag, link, findBy, isEnabled, isCdata);
				margs.add(ma);
			}
		}
		
		return margs;
	}
	
	private void init(String fileName){
		parser = new XMLFileParser(new File(fileName));
		
		logger.debug("Loading Mapper File: " + fileName);
		nodes = parser.getNodeListForTag("mapping");
		
		for (int i=0; i < nodes.getLength(); i++) 
		{
			Element element = (Element)nodes.item(i);
			
			String key = element.getAttribute("tag");
			String mapto = element.getAttribute("mapto");
			String enabled = element.getAttribute("enabled");
			String is_cdata = element.getAttribute("is_cdata");
			String link = element.getAttribute("link");
			String findBy = element.getAttribute("findBy");
			String user_value = element.getAttribute("value");
			
			Mapping value = new Mapping(mapto, Boolean.parseBoolean(enabled), Boolean.parseBoolean(is_cdata), link);
			if(user_value != null){
				value.value = user_value;
			}
			
			mapping.put(key, value);

			logger.debug("Processed Entry: Key=" + key + " || findBy=" + findBy +  " || mapto=" + mapto + " || link=" + link + " || enabled=" + enabled + " || is_cdata=" + is_cdata);
        }
	}
	
	public Mapping get(String key){
		return mapping.get(key);
	}
	
	public Set<String> keySet(){
		return mapping.keySet();
	}
}
