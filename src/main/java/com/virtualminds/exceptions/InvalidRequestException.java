package com.virtualminds.exceptions;

public class InvalidRequestException extends Exception {
	private static final long serialVersionUID = 1L;
	private static final String REQUIRED_PROPERTY_NOT_SET = "The '%s' required property is not set";
	private static final String REQUIRED_PROPERTY_VALIDATION_EXCEPTION = "The '%s' required property is set but validation exception occurs %s";
	private final boolean justLogException;
	private final String message;

	public InvalidRequestException(String requiredKeyName) {
		justLogException = false;
		message = String.format(REQUIRED_PROPERTY_NOT_SET, requiredKeyName);
	}

	public InvalidRequestException(boolean justLogExcetption, String requiredKeyName) {
		this.justLogException = justLogExcetption;
		message = String.format(REQUIRED_PROPERTY_NOT_SET, requiredKeyName);
	}

	public InvalidRequestException(String requiredKeyName, Exception e) {
		justLogException = false;
		message = String.format(REQUIRED_PROPERTY_VALIDATION_EXCEPTION, requiredKeyName, e.toString());
	}

	public InvalidRequestException(String requiredKeyName, boolean justLogExcetption, String customMessage) {
		this.justLogException = justLogExcetption;
		message = String.format(REQUIRED_PROPERTY_VALIDATION_EXCEPTION, requiredKeyName, customMessage);
	}

	public boolean isJustLogException() {
		return justLogException;
	}

	@Override
	public String toString() {
		return message;
	}

}
