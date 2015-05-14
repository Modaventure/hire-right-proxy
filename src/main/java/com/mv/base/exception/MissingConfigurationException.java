package com.mv.base.exception;

public class MissingConfigurationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public MissingConfigurationException() {
		super();
	}
	
	public MissingConfigurationException(String message) {
		super(message);
	}
}