package org.onesun.feedminer.dbhelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.onesun.feedminer.imdb.DBManager;
import org.onesun.feedminer.pojo.ExtractedContent;


public class ContentResultSet implements Iterator<ExtractedContent> {
	private static final Logger logger = Logger.getLogger(ContentResultSet.class.getName());
	
	private static final int MAX_CONNECTIONS = 1;
	private DBManager dbMgr = new DBManager(MAX_CONNECTIONS);
	
	private ResultSet rs = null;
	private Connection conn = null;
	private PreparedStatement stmt = null;
	private String tableName = null;
	private Set<String> columns = null;
	
	public ContentResultSet(String tableName, Set<String> columns){
		this.tableName = tableName;
		this.columns = columns;
		this.conn = dbMgr.getConnection();
	}
	
	private void runQuery(){
		try{
			String sql = "SELECT * FROM " + tableName;
			
			logger.debug("Executing SQL: " + sql);
			
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
	
//			System.out.println("Listing ...");
//			while (rs.next()) {
//				System.out.println(
//					rs.getInt("id") +
//					"\t" +
//					rs.getInt("example_id") +
//					"\t" +
//					rs.getInt("col_x") +
//					"\t" +
//					rs.getInt("col_y") +
//					"\t" +
//					rs.getBoolean("has_score")
//					);
//			}
		}catch(Exception e){
			logger.debug("Exception occured while querying " + tableName);
			e.printStackTrace();
		}
		finally {
		}
	}

	protected void finalize() throws Throwable
	{
		super.finalize();
		try {
			rs.close();
			stmt.close();
			dbMgr.freeConnection(conn);
		}
		catch(Exception e) {
			logger.debug("Exception occured while closing " + tableName);
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean hasNext() {
		try {
			if(rs == null){
				runQuery();
			}
			
			return !rs.isLast();
		}
		catch(Exception e){
			logger.debug("Exception occured determining last row " + tableName);
			e.printStackTrace();
		}
		finally {
		}
		
		return false;
	}

	@Override
	public ExtractedContent next() {
		try {
			if(rs == null){
				runQuery();
			}
			
			if(rs.next()){
				HashMap<String, String> content = new HashMap<String, String>();
				
				for(String column : columns){
//					if(column.compareToIgnoreCase(helper.DESCRIPTION_COLUMN) == 0){
//						InputStream is = rs.getBinaryStream(column);
//						StringBuffer buffer = new StringBuffer();
//						buffer.append(is.read());
//						
//						content.put(column, buffer.toString());
//					}
//					else 
					{
						content.put(column, rs.getString(column));
					}
				}
				
				ExtractedContent contentObject = new ExtractedContent(content);
				return contentObject;
			}
		}
		catch(Exception e){
			logger.debug("Exception occured determining last row " + columns);
			e.printStackTrace();
		}
		finally {
		}
		
		return null;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		// nothing to do here!!!
	}
}
