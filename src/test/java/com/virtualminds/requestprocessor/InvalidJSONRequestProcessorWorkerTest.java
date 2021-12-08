package com.virtualminds.requestprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InvalidJSONRequestProcessorWorkerTest {

	private static final long CUSTOMER_ID = 51;
	private RequestProcessorWorker testable;
	private ConcurrentHashMap<Long, StatisticsPOJO> statisticsMap;
	private ConcurrentSkipListSet<Long> customerIDs;
	private ConcurrentSkipListSet<Long> validIPs;
	private ConcurrentSkipListSet<String> validUserAgents;
	@Mock
	private Logger logger;

	@Before
	public void before() {
		statisticsMap = new ConcurrentHashMap<>();
		customerIDs = new ConcurrentSkipListSet<>();
		validIPs = new ConcurrentSkipListSet<>();
		validUserAgents = new ConcurrentSkipListSet<>();
		testable = Mockito
				.spy(new RequestProcessorWorker(statisticsMap, "", customerIDs, validIPs, validUserAgents, ""));
		doReturn(logger).when(testable).getLogger();
	}

	@Test
	public void testTryToParseFailedMessage() {
		doReturn(
				"{\"customerID\": 51,\"tagID\": 2,\"userID\": \"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\": \"123.234")
						.when(testable).getMessage();

		testable.tryToParseFailedParsing();

		StatisticsPOJO statisticsPOJO = statisticsMap.get(CUSTOMER_ID);
		assertNotNull(statisticsPOJO);
		assertEquals(0, statisticsPOJO.getNumberOfValidRequests());
		assertEquals(1, statisticsPOJO.getNumberOfInvalidRequests());
		verifyZeroInteractions(logger);
	}

	@Test
	public void testTryToParseFailedMessage_ParseException() {
		String message = "{\"customerID\": 51a,\"tagID\": 2,\"userID\": \"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\": \"123.234";
		doReturn(message).when(testable).getMessage();

		testable.tryToParseFailedParsing();

		StatisticsPOJO statisticsPOJO = statisticsMap.get(CUSTOMER_ID);
		assertNull(statisticsPOJO);
		verify(logger).log(Level.WARNING,
				"Number format exception durig determination of the customer on the invalid message: " + message);
	}

	@Test
	public void testTryToParseFailedMessage_CustomerNotPresent() {
		String message = "{tagID\": 2,\"userID\": \"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\": \"123.234";
		doReturn(message).when(testable).getMessage();

		testable.tryToParseFailedParsing();

		StatisticsPOJO statisticsPOJO = statisticsMap.get(CUSTOMER_ID);
		assertNull(statisticsPOJO);
		verify(logger).log(Level.WARNING, "Customer determination in the invalid message was failed: " + message);
	}

	@Test
	public void testTryToParseFailedMessage_CustomerNoComa() {
		String message = "{tagID\": 2,\"customerID\",\"userID\" \"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\": \"123.234";
		doReturn(message).when(testable).getMessage();

		testable.tryToParseFailedParsing();

		StatisticsPOJO statisticsPOJO = statisticsMap.get(CUSTOMER_ID);
		assertNull(statisticsPOJO);
		verify(logger).log(Level.WARNING, "Customer determination in the invalid message was failed: " + message);
	}

}
