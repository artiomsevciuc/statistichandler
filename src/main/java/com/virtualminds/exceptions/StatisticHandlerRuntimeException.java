package com.virtualminds.exceptions;

public class StatisticHandlerRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public StatisticHandlerRuntimeException(String message, Exception e) {
		super(message, e);
	}

}
