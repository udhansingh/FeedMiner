package org.onesun.feedminer.core.transform;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.onesun.feedminer.util.Constants;
import org.onesun.feedminer.util.XMLFileParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class ContentTransformer {
	private static final String MAPPERS_CONFIG_FILE = Constants.CONFIG_DIR + File.separatorChar + "mappers-config.xml";
	private static final Logger logger = Logger.getLogger(ContentTransformer.class.getName());

	private ArrayList<TransformsWorker> transformers = new ArrayList<TransformsWorker>();
	
	public ContentTransformer(){
		logger.debug("Starting Content Transformation ...");
		init();
		logger.debug("Completed Content Transformation ...");
	}

	public void execute(){
		ArrayList<Thread> myThreads = new ArrayList<Thread>();
		
		logger.debug("Creating threads");
		for(int cc = 0; cc < transformers.size(); cc++){
			Thread t = new Thread(transformers.get(cc));
			myThreads.add(t);
			t.start();
		}
		
		for(int cc = 0; cc < transformers.size(); cc++){
			try{
				myThreads.get(cc).join();
			}
			catch(Exception e){
				logger.debug("Exception occured while joning threads");
				e.printStackTrace();
			}
		}
		
		logger.debug("completed creating and starting threads");
	}

	public void init() {
		logger.debug("Loading File: " + MAPPERS_CONFIG_FILE);
		
		File f = new File(MAPPERS_CONFIG_FILE);
		if(f.exists() == false){
			logger.error("File " + f.getName() + " does not exist! - won't proceed further!");
			return;
		}
		XMLFileParser p = new XMLFileParser(f);
		
		NodeList nl = p.getNodeListForTag("mapper");
		for (int i=0; i<nl.getLength(); i++) 
		{
			Element element = (Element)nl.item(i);
			
			String name = element.getAttribute("name");
			String input = element.getAttribute("input");
			String output = element.getAttribute("output");
			Boolean enabled = Boolean.parseBoolean(element.getAttribute("enabled"));
			
			if(enabled == true){
				TransformsWorker conn = new TransformsWorker(name, input, output);
				transformers.add(conn);
			}
			
			
			logger.debug("Mapper Def: " + name + " <<< " + input + " <<< " + output + " <<< " + enabled);
        }
	}
}
