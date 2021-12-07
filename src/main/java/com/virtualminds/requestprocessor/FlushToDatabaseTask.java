package com.virtualminds.requestprocessor;

import java.util.Properties;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.virtualminds.context.StatisticsHandlerProperties;
import com.virtualminds.dal.StatementsExecutor;

public class FlushToDatabaseTask extends TimerTask {

	private Logger logger = Logger.getLogger(FlushToDatabaseTask.class.getName());
	private Properties properties;

	public FlushToDatabaseTask(Properties properties) {
		this.properties = properties;
	}

	@Override
	public void run() {
		ConcurrentMap<Long, StatisticsPOJO> statisticsMap = StatisticsContextHolder.getInstance().getStatisticsMap();
		StatisticsContextHolder.getInstance().setStatisticsMap(new ConcurrentHashMap<>());
		waitForFinishingProcessingOldMessages();
		StatementsExecutor executor = new StatementsExecutor();
		statisticsMap.forEach((key, value) -> {
			if (value.shouldFlush()) {
				executor.insertOrUpdateStatistics(key, value);
			}
		});
	}

	private void waitForFinishingProcessingOldMessages() {
		try {
			Thread.sleep(
					StatisticsHandlerProperties.NUMBER_OF_SECONDS_TO_WAIT_BEFORE_FLUSH.getValue(properties)
					* 1000L);
		} catch (InterruptedException e) {
			logger.log(Level.WARNING, "Thread sleep failed", e);
		}
	}

}
