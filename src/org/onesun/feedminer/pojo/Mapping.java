package org.onesun.feedminer.pojo;

public class Mapping {
	public String mapto = null;
	public boolean enabled = false;
	public boolean is_cdata = false;
	public String link = null;
	public String value = null;
	
	public Mapping(String mapto, boolean enabled, boolean is_cdata, String link){
		this.mapto = mapto;
		this.enabled = enabled;
		this.is_cdata = is_cdata;
		this.link = link;
	}
}