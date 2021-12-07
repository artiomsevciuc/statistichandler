package com.virtualminds.context;

import java.util.Properties;

public enum StatisticsHandlerProperties {
	NUMBER_OF_SECONDS_TO_WAIT_BEFORE_FLUSH("numberOfSecondsToWaitBeforeFlush", "30"),
	DELAY_BETWEEN_DATABASE_FLUSH("delayMinutesBetweenDatabaseFlush", "5"),
	NUMBER_OF_PROCESSING_THREADS("numberOfProcessingThreads", "3"), 
	MAXIMUM_POOL_SIZE("maximumThreadPoolSize", "10"),
	KEEP_ALIVE_TIME("keepAliveThreadPoolMinutes", "10");
	
	private String propertyName;
	private String defaultValue;
	
	private StatisticsHandlerProperties(String propertyName, String defaultValue) {
		this.propertyName=propertyName;
		this.defaultValue = defaultValue;
	}
	
	public String getPropertyName() {
		return propertyName;
	}

	public int getValue(Properties properties) {
		return Integer.valueOf(properties.getProperty(propertyName, defaultValue));
	}
}
