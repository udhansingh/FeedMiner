package org.onesun.feedminer.core.transform;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.onesun.feedminer.app.PropertiesFileTypes;
import org.onesun.feedminer.app.PropertyFilesFactory;
import org.onesun.feedminer.core.Geo;
import org.onesun.feedminer.core.PublisherInfo;
import org.onesun.feedminer.dbhelper.ContentHelper;
import org.onesun.feedminer.dbhelper.ContentResultSet;
import org.onesun.feedminer.pojo.Mapping;
import org.onesun.feedminer.pojo.MappingArgs;
import org.onesun.feedminer.tags.MappingSymantics;
import org.onesun.feedminer.tags.TransformMappingTechnique;
import org.onesun.feedminer.util.Constants;
import org.onesun.feedminer.util.ExecutionTimer;
import org.onesun.feedminer.util.Utility;


public class TransformsWorker implements Runnable {
	private static final Logger logger = Logger.getLogger(TransformsWorker.class.getName());

	private static Properties properties = PropertyFilesFactory.getProperties(PropertiesFileTypes.APPLICATION);

	private String input = null;
	private String output = null;

	private TransformMappingTechnique mapping = null;
	private ArrayList<MappingArgs> geoMappingArgs = null;
	
	private String id = null;
	private FileOutputStream fos = null;
	
	public TransformsWorker(String id, String input, String output){
		this.id = id;
		this.input = input;
		this.output = output;
		
		this.mapping = new TransformMappingTechnique(Constants.MDF_DIR + File.separatorChar + this.input);
		this.geoMappingArgs = mapping.getAdditionalArgs(MappingSymantics.GEO);
		
		try{
			File f = new File( properties.getProperty(Constants.OUTPUT_FILE_STORE) +
					File.separatorChar + output );
			
			logger.debug(id + " File opened for saving: " + f.getPath());
			fos = new FileOutputStream(f);
		}
		catch(Exception e){
			logger.error(id + " Exception Occured while opening file");
			e.printStackTrace();
		}
	}

	protected void finalize() throws Throwable
	{
		try{
			fos.close();
		}
		catch(Exception e){
			logger.error(id + " Exception Occured while closing file");
			e.printStackTrace();
		}
	}
	
	private StringBuffer buffer = new StringBuffer();
	
	synchronized private void save(){
		try{
			fos.write(buffer.toString().getBytes());
		}
		catch(Exception e){
			logger.error(id + " Exception occured while writing to file");
			e.printStackTrace();
		}
	}

	
	private void transform(){
		Mapping m = null;

		logger.debug(id + " Processing Transform for: " + output);
		buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
				
		m = mapping.get(MappingSymantics.ROOT);
		buffer.append(Utility.openXMLTag(m.mapto));

		m = mapping.get(MappingSymantics.PUBLISHER);
		if(m.enabled == true) buffer.append(Utility.makeXMLTag(m.is_cdata, m.mapto, PublisherInfo.getPublisherName()));
		
		m = mapping.get(MappingSymantics.PUBLISHER_URL);
		if(m.enabled == true) buffer.append(Utility.makeXMLTag(m.is_cdata, m.mapto, PublisherInfo.getPublisherURL()));

		ContentResultSet crs = new ContentResultSet(
				ContentHelper.getInstance().getTableName(),
				ContentHelper.getInstance().getColumns());
		
		// for(int idx = 0; idx < em.getEntities().size(); idx++) {
		while(crs.hasNext()){
			m = mapping.get(MappingSymantics.ENTRY);
			if(m.enabled == true) buffer.append(Utility.openXMLTag(m.mapto));

			UUID uuid = UUID.randomUUID();
			m = mapping.get(MappingSymantics.UUID);
			if(m.enabled == true) buffer.append(Utility.makeXMLTag(m.is_cdata, m.mapto, uuid.toString()));

			// HashMap<String, String> input = em.getEntities().get(idx);
			HashMap<String, String> input = (crs.next()).getContent();
			
			if(input == null) return;
			
			for(String key: mapping.keySet()) {
				m = mapping.get(key);
				
				if(key.compareTo(MappingSymantics.LOCALE) == 0){
					if(m != null && m.enabled == true){
						buffer.append(Utility.openXMLTag(m.mapto));

						// TODO: Escape special characters
						String escValue = StringEscapeUtils.escapeXml(m.value);
						buffer.append(escValue);
						
						buffer.append(Utility.closeXMLTag(m.mapto));
					}
				}
				else if(key.compareTo(MappingSymantics.GEO) == 0){
					// Handle GEO differently
					// Need to translate it differently if additional arguments are found!

					// Has additional Args
					if(geoMappingArgs != null && geoMappingArgs.size() > 0){
						if(m.enabled == true) buffer.append(Utility.openXMLTag(m.mapto));
						
						for(int aidx = 0; aidx < geoMappingArgs.size(); aidx++){
							MappingArgs ma = geoMappingArgs.get(aidx);
							
							if(ma.enabled == true){	
								buffer.append(Utility.openXMLTag(ma.tag));
							
								if(ma.link != null && ma.link.length() > 0){
									String tvalue = input.get(key);
									String escValue = StringEscapeUtils.escapeXml(tvalue);
									
									logger.debug(id + " transforming GEO Args: tag = " + ma.tag +" key = " + key + " value = " + escValue);
									
									buffer.append(Geo.getInstance().getLocationName(ma.link, ma.findBy, escValue));
								}
								
								buffer.append(Utility.closeXMLTag(ma.tag));
							}
						}
						
						if(m.enabled == true) buffer.append(Utility.closeXMLTag(m.mapto));
					}
					// No additional Args
					else {
						if(m.enabled == true)
						{
							String tag = m.mapto;
							String tvalue = input.get(key);
							
							String escValue = StringEscapeUtils.escapeXml(tvalue);
							
							logger.debug(id + " transforming GEO: tag = " + tag +" key = " + key + " value = " + escValue);
							buffer.append(Utility.openXMLTag(tag));
							
							if(m.link != null && m.link.length() > 0){
								buffer.append(Geo.getInstance().getLocationName(m.mapto, m.link, escValue));
							}
							
							buffer.append(Utility.closeXMLTag(tag));
						}
					}
				}
				else {
					if(m.enabled == true)
					{
						String tag = m.mapto;
						String tvalue = input.get(key);
						
						String escValue = StringEscapeUtils.escapeXml(tvalue);
		
						if(tvalue != null && tvalue.length() > 0){
							logger.debug(id + " transforming: tag = " + tag +" key = " + key + " value = " + escValue);
							
							if(m.enabled == true) buffer.append(Utility.makeXMLTag(m.is_cdata, tag, escValue));
						}
					}
				}
			}

			m = mapping.get(MappingSymantics.ENTRY);
			if(m.enabled == true) buffer.append(Utility.closeXMLTag(m.mapto));
			
		}

		m = mapping.get(MappingSymantics.ROOT);
		if(m.enabled == true) buffer.append(Utility.closeXMLTag(m.mapto));
	}

	@Override
	public void run(){
		ExecutionTimer timer = new ExecutionTimer();
		timer.start();
		logger.info("Worker " + id + " started @ " + (timer.getStartTime()));
		
		transform();
		save();
		
		timer.stop();
		logger.info("Worker " + id + " stopped @ " + (timer.getStartTime()));
		
		
		logger.info("Worker " + id + " time taken is " + Utility.millisToString(timer.getElapsedTime()));
	}
}