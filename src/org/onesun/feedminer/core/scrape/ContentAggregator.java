package org.onesun.feedminer.core.scrape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;
import org.onesun.feedminer.dbhelper.ContentHelper;
import org.onesun.feedminer.pojo.ExtractedContent;
import org.onesun.feedminer.tags.MappingSymantics;

import com.sun.syndication.feed.synd.SyndEntry;


public class ContentAggregator {
	private static final Logger logger = Logger.getLogger(ContentAggregator.class.getName());

	private FeedSourceReader feedSource = new FeedSourceReader();
	private FeedAggregator feedProcessor = new FeedAggregator(feedSource);
	
	private ContentHelper helper = ContentHelper.getInstance();
	
	public ContentAggregator(){
		logger.debug("Creating EntityExtractor");
	}
	
	public void extract()
	{
		logger.debug("Starting Extraction ...");
		ArrayList<SyndEntry> entries = feedProcessor.getEntries();
		
		for(int idx = 0; idx < entries.size(); idx++){
			String url = entries.get(idx).getLink();
			logger.debug("Processing URL: " + url);
			
			SyndEntry se = entries.get(idx);
			ContentScrapper ee = new ContentScrapper(url);
			HashMap<String, String> content = ee.getContents();
			
			content.put(MappingSymantics.TITLE, se.getTitle());
			if(idx == 0){
				Set<String> keys = content.keySet();
			
				logger.debug("Entyty #" + (idx+1) + "--------------- contents worked on: " + content.size());
				
				logger.debug("========================================================");
				logger.debug("================= COLUMNS TO PROCESS ===================");
				for(String key : keys){
					logger.debug("Column Name: " + key);
				}
				
				
				helper.createTable(keys);
				logger.debug("========================================================");
			}
			
			ExtractedContent contentObject = new ExtractedContent(content);
			helper.insert(contentObject);
		}
		logger.debug("Finished Extraction ...");
	}
}
