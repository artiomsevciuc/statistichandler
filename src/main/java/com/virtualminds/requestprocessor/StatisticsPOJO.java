package com.virtualminds.requestprocessor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class StatisticsPOJO {
	private AtomicInteger numberOfValidRequests = new AtomicInteger(0);
	private AtomicInteger numberOfInvalidRequests = new AtomicInteger(0);
	private AtomicBoolean isValidCustomer = new AtomicBoolean(true);

	public StatisticsPOJO(boolean userActive) {
		isValidCustomer = new AtomicBoolean(userActive);
	}

	public int incrementValidRequests() {
		return numberOfValidRequests.incrementAndGet();
	}

	public int incrementInvalidRequests() {
		return numberOfInvalidRequests.incrementAndGet();
	}

	public int getNumberOfValidRequests() {
		return numberOfValidRequests.get();
	}

	public int getNumberOfInvalidRequests() {
		return numberOfInvalidRequests.get();
	}

	public boolean isValidCustomer() {
		return isValidCustomer.get();
	}

	public void nullifyNumberOfValidRequests() {
		numberOfValidRequests.set(0);
	}

	public void nullifyNumberOfInvalidRequests() {
		numberOfInvalidRequests.set(0);
	}

	public boolean shouldFlush() {
		return numberOfValidRequests.get() + numberOfInvalidRequests.get() > 0;
	}
}
