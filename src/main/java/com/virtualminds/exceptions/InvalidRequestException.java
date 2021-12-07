package com.virtualminds.exceptions;

public class InvalidRequestException extends Exception {
	private static final long serialVersionUID = 1L;
	final boolean justLogException;

	public InvalidRequestException() {
		justLogException = false;
	}

	public InvalidRequestException(boolean justLogExcetption) {
		this.justLogException = justLogExcetption;
	}

	public boolean isJustLogException() {
		return justLogException;
	}
}
