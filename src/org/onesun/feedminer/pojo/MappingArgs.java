package org.onesun.feedminer.pojo;

public class MappingArgs {
	public String tag;
	public String link;
	public String findBy;
	
	public boolean enabled;
	public boolean is_cdata;
	
	public MappingArgs(String tag, String link, String findBy, boolean enabled, boolean is_cdata){
		this.tag = tag;
		this.link = link;
		this.findBy = findBy;
		this.enabled = enabled;
		this.is_cdata = is_cdata;
	}
}