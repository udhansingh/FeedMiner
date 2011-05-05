package org.onesun.feedminer.util;

public class ExecutionTimer {
	private long start = 0;
	private long end = 0;
	
	public void start(){
		start = System.currentTimeMillis();
	}
	
	public void stop(){
		end = System.currentTimeMillis();
	}
	
	public long getElapsedTime(){
		return end - start;
	}
	
	public long getStopTime(){
		return end;
	}
	
	public long getStartTime(){
		return start;
	}
}