package com.virtualminds.requestprocessor;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.virtualminds.exceptions.InvalidRequestException;

public class RequestProcessorWorker implements Runnable {

	private static final String NOT_A_NUMBER_ERROR = "Not a number";
	private static final String TIME_STAMP_ATTRIBUTE = "timestamp";
	private static final String REMOTE_IP_ATTRIBUTE = "remoteIP";
	private static final String USER_ID_ATTRIBUTE = "userID";
	private static final String TAG_ID_ATTRIBUTE = "tagID";
	private static final String CUSTOMER_ID_ATTRIBUTE = "customerID";

	private static final String PARSE_EXCEPTION = "It is not possible to determine customer id from the parsed json: %s";
	private static final String NUMBER_FORMAT_EXCEPTION = "Number format exception durig determination of the customer on the invalid message: %s";
	private static final String CUSTOMER_DETERMINATION_EXCEPTION = "Customer determination in the invalid message was failed: %s";
	private ConcurrentMap<Long, StatisticsPOJO> statisticsMap;
	private ConcurrentSkipListSet<Long> activeCustomerIDs;
	private ConcurrentSkipListSet<Long> blackListIPs;
	private ConcurrentSkipListSet<String> blackListUserAgents;
	private Logger logger = Logger.getLogger(RequestProcessorWorker.class.getName());
	private String message;
	private String userAgent;

	public RequestProcessorWorker(ConcurrentMap<Long, StatisticsPOJO> statisticsMap, String message,
			ConcurrentSkipListSet<Long> activeCustomerIDs, ConcurrentSkipListSet<Long> blackListIPs,
			ConcurrentSkipListSet<String> blackListUserAgents, String userAgent) {
		super();
		this.statisticsMap = statisticsMap;
		this.message = message;
		this.activeCustomerIDs = activeCustomerIDs;
		this.blackListIPs = blackListIPs;
		this.blackListUserAgents = blackListUserAgents;
		this.userAgent = userAgent;
	}

	@Override
	public void run() {
		try {
			JsonObject jsonObject = JsonParser.parseString(getMessage()).getAsJsonObject();
			checkValidJson(jsonObject);
		} catch (JsonSyntaxException e) {
			tryToParseFailedParsing();
		}
	}

	private void checkValidJson(JsonObject jsonObject) {
		JsonPrimitive customerId = null;
		try {
			customerId = (JsonPrimitive) getObjectValue(CUSTOMER_ID_ATTRIBUTE, jsonObject);
			validateCustomerIdIsNumber(customerId);
			getObjectValue(TAG_ID_ATTRIBUTE, jsonObject);
			getObjectValue(USER_ID_ATTRIBUTE, jsonObject);
			JsonPrimitive remoteIpString = (JsonPrimitive) getObjectValue(REMOTE_IP_ATTRIBUTE, jsonObject);
			Long remoteIp = validateRemoteIp(remoteIpString);
			getObjectValue(TIME_STAMP_ATTRIBUTE, jsonObject);
			computeValidJson(customerId.getAsLong(), remoteIp);
		} catch (InvalidRequestException e) {
			processInvalidException(customerId, e);
		}
	}

	private Long validateRemoteIp(JsonPrimitive remoteIp) throws InvalidRequestException {
		String remoteIpString = remoteIp.getAsString();
		if (remoteIpString == null) {
			throw new InvalidRequestException(REMOTE_IP_ATTRIBUTE);
		}
		try {
			return Long.parseLong(remoteIpString.replaceAll("\\.", ""));
		} catch (NumberFormatException e) {
			throw new InvalidRequestException(REMOTE_IP_ATTRIBUTE, e);
		}
	}

	private void processInvalidException(JsonPrimitive customerId, InvalidRequestException e) {
		if (e.isJustLogException()) {
			getLogger().log(Level.INFO, String.format(PARSE_EXCEPTION, getMessage()), e);
		} else if (customerId != null) {
			computeInvalidJson(customerId.getAsLong());
		}
	}

	private void validateCustomerIdIsNumber(JsonPrimitive customerId) throws InvalidRequestException {
		if (!customerId.isNumber()) {
			throw new InvalidRequestException(CUSTOMER_ID_ATTRIBUTE, true, NOT_A_NUMBER_ERROR);
		}
	}

	private void computeValidJson(Long customerId, final long remoteIp) {
		if (customerId != null) {
			statisticsMap.compute(customerId, (key, pojo) -> {
				if (pojo == null) {
					pojo = new StatisticsPOJO(isCustomerActive(customerId));
				}
				if (pojo.isValidCustomer() && isValidUserAgent(userAgent) && isRemoteIpValid(remoteIp)) {
					pojo.incrementValidRequests();
				} else {
					pojo.incrementInvalidRequests();
				}
				return pojo;
			});
		}
	}

	// I just assume that set of Remote IPs wont be very huge, otherwise it will
	// require a select in the database
	private boolean isRemoteIpValid(long remoteIp) {
		return !blackListIPs.contains(remoteIp);
	}

	// I just assume that set of user agents wont be very huge, otherwise it will
	// require a select in the database
	private boolean isValidUserAgent(String userAgent) {
		return !blackListUserAgents.contains(userAgent);
	}

	// I just assume that set of customer IDs wont be very huge, otherwise it will
	// require a select in the database or to do a inverse select
	private boolean isCustomerActive(Long customerId) {
		return activeCustomerIDs.contains(customerId);
	}

	private Object getObjectValue(String keyName, JsonObject jsonObject) throws InvalidRequestException {
		if (jsonObject.has(keyName) && !jsonObject.get(keyName).isJsonNull()) {
			return jsonObject.get(keyName);
		}
		throw new InvalidRequestException(keyName);
	}

	protected void tryToParseFailedParsing() {
		String[] split = getMessage().split(",");
		for (String messageElement : split) {
			if (messageElement.contains(CUSTOMER_ID_ATTRIBUTE)) {
				String[] customerInfo = messageElement.split(":");
				if (customerInfo.length == 2) {
					parseCustomerIdInfo(customerInfo[1].trim());
					return;
				}
			}
		}
		getLogger().log(Level.WARNING, String.format(CUSTOMER_DETERMINATION_EXCEPTION, getMessage()));
	}

	private void parseCustomerIdInfo(String customerIdString) {
		try {
			computeInvalidJson(Long.parseLong(customerIdString));
		} catch (NumberFormatException e) {
			getLogger().log(Level.WARNING, String.format(NUMBER_FORMAT_EXCEPTION, getMessage()));
		}
	}

	private void computeInvalidJson(Long customerId) {
		statisticsMap.compute(customerId, (key, value) -> {
			if (value == null) {
				value = new StatisticsPOJO(isCustomerActive(customerId));
			}
			value.incrementInvalidRequests();
			return value;
		});
	}

	protected String getMessage() {
		return message;
	}

	protected Logger getLogger() {
		return logger;
	}
}
