package com.honestefforts.fixengine.service.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1699850231683913185L;

	public InvalidTokenException(String msg) {
		super(msg);
	}
	
	public InvalidTokenException(String msg, Throwable t) {
		super(msg, t);
	}

}
