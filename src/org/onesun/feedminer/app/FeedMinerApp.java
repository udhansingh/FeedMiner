package org.onesun.feedminer.app;

import org.apache.log4j.Logger;
import org.onesun.feedminer.core.PublisherInfo;
import org.onesun.feedminer.core.scrape.ContentAggregator;
import org.onesun.feedminer.core.transform.ContentTransformer;
import org.onesun.feedminer.util.ExecutionTimer;
import org.onesun.feedminer.util.Utility;


public class FeedMinerApp {
	private static final Logger logger = Logger.getLogger(FeedMinerApp.class.getName());

	private static ContentAggregator extractor = null;
	private static ContentTransformer transformer = null;

	public static void init(){
		// Need to initialize first to be able to perform logging.
		PropertyFilesFactory.init();
		logger.debug("App Configurations Loaded!");
		
		logger.debug("Loading Publisher Information");
		if(PublisherInfo.isInitialized() == false) PublisherInfo.init();
	}
	
	public FeedMinerApp() {
		extractor = new ContentAggregator();
		transformer =  new ContentTransformer();
	}
	
	public void startETL() {
		// Extract Entities: Needs to be done only once
		extractor.extract();
		
		// Run transformers: One thread per transform output
		transformer.execute();
	}

	public static void main(String[] args) {
		FeedMinerApp.init();
		
		ExecutionTimer timer = new ExecutionTimer();
		timer.start();
		logger.info("Timer Started @ " + (timer.getStartTime()));

		final String pleaseWait = "Please Wait!";
		
		logger.info("Initializing ... " + pleaseWait);
		// Create App
		FeedMinerApp app = new FeedMinerApp();
		logger.info("Done!");
		
		// Execute ETL
		logger.info("Performing ETL ... " + pleaseWait);
		app.startETL();

		timer.stop();
		logger.info("Timer Stoppped @ " + (timer.getStopTime()));
		
		logger.info("Done!");
		logger.info("Total Time taken for Extraction and Transformation: " + Utility.millisToString(timer.getElapsedTime()));
	}
}
