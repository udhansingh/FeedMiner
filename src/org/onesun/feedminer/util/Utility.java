package org.onesun.feedminer.util;

import java.io.File;

public class Utility {
	public static String openXMLTag(String tname){
		return "<" + tname + ">";
	}
	
	public static String closeXMLTag(String tname){
		return "</" + tname + ">";
	}
	
	public static String makeXMLTag(boolean cDataFlag, String tname, String tvalue){
		String text = tvalue;
		
		if(cDataFlag == true){
			text = "<![CDATA[" + text + "]]>";
		}
		
		return(openXMLTag(tname) + text + closeXMLTag(tname));
	}
	
	public static boolean exists(String fileName){
		File f = new File(fileName);
		return f.exists();
	}
	
	public static String millisToString(long millis){
		String format = String.format("%%0%dd", 2);
		long elapsedTime = millis / 1000;
		
		String seconds = String.format(format, elapsedTime % 60);  
		String minutes = String.format(format, (elapsedTime % 3600) / 60);  
		String hours = String.format(format, elapsedTime / 3600);  
		String result =  hours + ":" + minutes + ":" + seconds;  
		
		return result;
	}
}
