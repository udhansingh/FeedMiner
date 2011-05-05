package org.onesun.feedminer.pojo;

import java.util.HashMap;

public class ExtractedContent {
	private HashMap<String, String> content = null;
	
	public HashMap<String, String> getContent(){
		return content;
	}
	
	public ExtractedContent(HashMap<String, String> content){
		this.content = content;
	}
}
