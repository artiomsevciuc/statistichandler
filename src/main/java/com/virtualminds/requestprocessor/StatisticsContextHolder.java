package com.virtualminds.requestprocessor;

import java.time.Instant;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.virtualminds.context.StatisticsHandlerProperties;

public class StatisticsContextHolder {
	private ConcurrentMap<Long, StatisticsPOJO> statisticsMap;
	private ConcurrentSkipListSet<Long> blackListIPs;
	private ConcurrentSkipListSet<Long> activeCustomerIDs;
	private ConcurrentSkipListSet<String> blackListUserAgents;
	private ThreadPoolExecutor threadPoolExecutor;
	private Timer timer;
	private Properties properties;
	private static StatisticsContextHolder instance;

	private StatisticsContextHolder() {
	}

	public static StatisticsContextHolder getInstance() {
		if (instance == null) {
			instance = new StatisticsContextHolder();
		}
		return instance;
	}

	public void initializeThreadPoolExecutor(ConcurrentSkipListSet<Long> blackListIPs,
			ConcurrentSkipListSet<Long> activeCustomerIDs, ConcurrentSkipListSet<String> blackListUserAgents,
			Properties properties) {
		this.blackListIPs = blackListIPs;
		this.activeCustomerIDs = activeCustomerIDs;
		this.blackListUserAgents = blackListUserAgents;
		this.properties = properties;
		statisticsMap = new ConcurrentHashMap<>();
		threadPoolExecutor = new ThreadPoolExecutor(
				StatisticsHandlerProperties.NUMBER_OF_PROCESSING_THREADS.getValue(properties),
				StatisticsHandlerProperties.MAXIMUM_POOL_SIZE.getValue(properties),
				StatisticsHandlerProperties.KEEP_ALIVE_TIME.getValue(properties),
				TimeUnit.MINUTES,
				new LinkedBlockingQueue<Runnable>());
		timer = new Timer();
		long delay = StatisticsHandlerProperties.DELAY_BETWEEN_DATABASE_FLUSH.getValue(properties) * 60 * 1000L;
		timer.scheduleAtFixedRate(new FlushToDatabaseTask(properties), Date.from(Instant.now().plusMillis(delay)),
				delay);
	}

	public void processMessage(String message, String userAgent) {
		threadPoolExecutor.execute(new RequestProcessorWorker(statisticsMap, message, activeCustomerIDs, blackListIPs,
				blackListUserAgents, userAgent));
	}

	public ConcurrentMap<Long, StatisticsPOJO> getStatisticsMap() {
		return statisticsMap;
	}

	public void setStatisticsMap(ConcurrentMap<Long, StatisticsPOJO> statisticsMap) {
		this.statisticsMap = statisticsMap;
	}

	public ThreadPoolExecutor getThreadPoolExecutor() {
		return threadPoolExecutor;
	}

	public Timer getTimer() {
		return timer;
	}

	public void destroyContext() {
		getThreadPoolExecutor().shutdown();
		getTimer().cancel();
		new FlushToDatabaseTask(properties).run();
	}
}
