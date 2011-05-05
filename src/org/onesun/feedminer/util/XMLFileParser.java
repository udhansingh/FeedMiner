package org.onesun.feedminer.util;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLFileParser {
	private static final Logger logger = Logger.getLogger(XMLFileParser.class.getName());
	private Document doc = null;
	public FileInputStream fis = null;
	
	public XMLFileParser(File f){
		if(f != null){
			try {
				fis = new FileInputStream(f);
				doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fis);
			} catch (Exception e) {
				logger.debug("Error while opening file " + f.getName());
				e.printStackTrace();
			}
		}
	}
	
	public NodeList getNodeListForTag(String tagname)
	{
		try 
		{
			NodeList list = doc.getElementsByTagName(tagname);
			
			return list;
		}
		catch (Exception e) 
		{
			logger.debug("Error while getting nodelist from XML document");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getNodeValue(Element item, String tag){
		String textValue = null;

		NodeList nodes = item.getElementsByTagName(tag);
		if(nodes != null && nodes.getLength() > 0){
			Element element = (Element)nodes.item(0);
			textValue = element.getFirstChild().getNodeValue();
		}

		return textValue;
	}
	
	public static String getAttributeValue(Element item, String attr){
		String textValue = null;

		textValue = item.getAttribute(attr);

		return textValue;
	}
}
