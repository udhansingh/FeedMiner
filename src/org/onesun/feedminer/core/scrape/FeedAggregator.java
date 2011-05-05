package org.onesun.feedminer.core.scrape;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class FeedAggregator {
	private static final Logger logger = Logger.getLogger(FeedAggregator.class.getName());

	private FeedSourceReader feedSource = null;
	private ArrayList<SyndEntry> entries = new ArrayList<SyndEntry>();

	public FeedAggregator(FeedSourceReader fs) {
		logger.debug("Initializing Feed Processor ...");
		feedSource = fs;

		logger.debug("Starting feed processor: will extract entities now ...");
		if (feedSource != null) {
			extract();
		}
		logger.debug("FeedProcessor completed extracting entities");
	}

	@SuppressWarnings("unchecked")
	private void extract() {
		HashMap<String, String> sources = feedSource.getFeedSources();
		Set<String> keys = sources.keySet();
		
		for (String key : keys) {
			try {
				String url = sources.get(key);

				URL feedUrl = new URL(url);
				SyndFeedInput input = new SyndFeedInput();
				SyndFeed feed = input.build(new XmlReader(feedUrl));

				List<SyndEntry> feeds = feed.getEntries();
				for (int fidx = 0; fidx < feeds.size(); fidx++) {
					SyndEntry se = (SyndEntry) feeds.get(fidx);

					entries.add(se);
				}
			} catch (Exception e) {
				logger.error("Exception occured while extracting feed source: " + key);
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<SyndEntry> getEntries(){
		return entries;
	}
}
