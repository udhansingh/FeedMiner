package org.onesun.feedminer.tags;

import java.util.HashMap;
import java.util.Set;


public class ScrapeMappingTechnique {
	private HashMap<String, String> mapping = new HashMap<String, String>();
	
	public ScrapeMappingTechnique(){
		init();
	}
	
	// TODO: 
	// This file must be replaced with user tags
	// When that is done; FM can be used by anyone else
	// until then, this is only for Ananth Bashyam
	private void init(){
		mapping.put("source", MappingSymantics.ROOT);
		mapping.put("job", MappingSymantics.ENTRY);
		mapping.put(MappingSymantics.TITLE, MappingSymantics.TITLE);
		mapping.put("publisher", MappingSymantics.PUBLISHER);
		mapping.put("publisherurl", MappingSymantics.PUBLISHER_URL);
		mapping.put("referencenumber", MappingSymantics.UUID);
		mapping.put("Posted On:", MappingSymantics.DATE);
		mapping.put("Location", MappingSymantics.GEO);
		mapping.put("Job Description:", MappingSymantics.DESCRIPTION);
		mapping.put(MappingSymantics.URL, MappingSymantics.URL);
	}
	
	public Set<String> keySet(){
		return mapping.keySet();
	}
	public String get(String key){
		return mapping.get(key);
	}
}
