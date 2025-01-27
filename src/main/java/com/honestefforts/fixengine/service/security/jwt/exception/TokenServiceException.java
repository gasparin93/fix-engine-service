package com.honestefforts.fixengine.service.security.jwt.exception;

public class TokenServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3213230724899736134L;

	private int httpStatusCode = 401;
	
	public TokenServiceException(String message, int httpStatusCode) {
		super(message);
		this.httpStatusCode = httpStatusCode;
	}
	
	public TokenServiceException(String message, Throwable e, int httpStatusCode) {
		super(message, e);
		this.httpStatusCode = httpStatusCode;
	}
	
	public int getHttpStatusCode() {
		return httpStatusCode;
	}
	
	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}
}
