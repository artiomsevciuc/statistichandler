package com.virtualminds.requestprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ValidJSONRequestProcessorWorkerTest {

	private static final String FIREFOX_USER_AGENT = "Firefox";
	private static final long CUSTOMER_ID = 51;
	private static final String IP_ADDRESS = "127.0.0.1";
	private RequestProcessorWorker testable;
	private ConcurrentHashMap<Long, StatisticsPOJO> statisticsMap;
	private ConcurrentSkipListSet<Long> customerIDs;
	private ConcurrentSkipListSet<Long> blackListedIPS;
	private ConcurrentSkipListSet<String> invalidUserAgents;
	private static final long VALID_CUSTOMER_ID = 51;
	private static final String VALID_MESSAGE = "{\"customerID\": " + VALID_CUSTOMER_ID
			+ ",\"tagID\": 2,\"userID\": \"aaaaaaaa-bbbb-cccc-1111-222222222222\",\"remoteIP\": \"%s\",\"timestamp\": 1500000000}";
	@Mock
	private Logger logger;
	private String requestMessage;

	@Before
	public void before() {
		this.requestMessage = String.format(VALID_MESSAGE, IP_ADDRESS);
		statisticsMap = new ConcurrentHashMap<>();
		customerIDs = new ConcurrentSkipListSet<>();
		blackListedIPS = new ConcurrentSkipListSet<>();
		invalidUserAgents = new ConcurrentSkipListSet<>();
		testable = Mockito.spy(new RequestProcessorWorker(statisticsMap, "", customerIDs, blackListedIPS,
				invalidUserAgents, FIREFOX_USER_AGENT));
	}

	@Test
	public void testRun_RequestIsValid() {
		customerIDs.add(VALID_CUSTOMER_ID);

		doReturn(requestMessage).when(testable).getMessage();

		testable.run();

		StatisticsPOJO statisticsPOJO = statisticsMap.get(CUSTOMER_ID);
		assertNotNull(statisticsPOJO);
		assertEquals(1, statisticsPOJO.getNumberOfValidRequests());
		assertEquals(0, statisticsPOJO.getNumberOfInvalidRequests());
		verifyZeroInteractions(logger);
	}

	@Test
	public void testRun_CustomerIsNotActive() {
		customerIDs.add(12L);
		doReturn(requestMessage).when(testable).getMessage();

		testable.run();

		StatisticsPOJO statisticsPOJO = statisticsMap.get(CUSTOMER_ID);
		assertNotNull(statisticsPOJO);
		assertEquals(0, statisticsPOJO.getNumberOfValidRequests());
		assertEquals(1, statisticsPOJO.getNumberOfInvalidRequests());
		verifyZeroInteractions(logger);
	}

	@Test
	public void testRun_CustomerIsNotANumber() {
		requestMessage = String.format(requestMessage, "NoAtNumber", IP_ADDRESS);
		doReturn(requestMessage).when(testable).getMessage();

		testable.run();

		StatisticsPOJO statisticsPOJO = statisticsMap.get(CUSTOMER_ID);
		assertNotNull(statisticsPOJO);
		assertEquals(0, statisticsPOJO.getNumberOfValidRequests());
		assertEquals(1, statisticsPOJO.getNumberOfInvalidRequests());
		verifyZeroInteractions(logger);
	}

	@Test
	public void testRun_UserAgentIsNotValid() {
		customerIDs.add(VALID_CUSTOMER_ID);
		invalidUserAgents.add(FIREFOX_USER_AGENT);
		doReturn(requestMessage).when(testable).getMessage();

		testable.run();

		StatisticsPOJO statisticsPOJO = statisticsMap.get(CUSTOMER_ID);
		assertNotNull(statisticsPOJO);
		assertEquals(0, statisticsPOJO.getNumberOfValidRequests());
		assertEquals(1, statisticsPOJO.getNumberOfInvalidRequests());
		verifyZeroInteractions(logger);
	}

	@Test
	public void testRun_IPInBlackList() {
		customerIDs.add(VALID_CUSTOMER_ID);
		blackListedIPS.add(127001L);
		doReturn(requestMessage).when(testable).getMessage();

		testable.run();

		StatisticsPOJO statisticsPOJO = statisticsMap.get(CUSTOMER_ID);
		assertNotNull(statisticsPOJO);
		assertEquals(0, statisticsPOJO.getNumberOfValidRequests());
		assertEquals(1, statisticsPOJO.getNumberOfInvalidRequests());
		verifyZeroInteractions(logger);
	}

	@Test
	public void testRun_WrongFormatIP() {
		customerIDs.add(VALID_CUSTOMER_ID);
		requestMessage = String.format(VALID_MESSAGE, "456.L123.12.2");
		doReturn(requestMessage).when(testable).getMessage();

		testable.run();

		StatisticsPOJO statisticsPOJO = statisticsMap.get(CUSTOMER_ID);
		assertNotNull(statisticsPOJO);
		assertEquals(0, statisticsPOJO.getNumberOfValidRequests());
		assertEquals(1, statisticsPOJO.getNumberOfInvalidRequests());
	}

}
