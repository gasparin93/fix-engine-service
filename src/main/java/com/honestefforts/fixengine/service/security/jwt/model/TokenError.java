package com.honestefforts.fixengine.service.security.jwt.model;

public class TokenError {
	
	private String error;
	private String error_description;
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getError_description() {
		return error_description;
	}
	public void setError_description(String error_description) {
		this.error_description = error_description;
	}
	
	@Override
	public String toString() {
		return "TokenError [error=" + error + ", error_description=" + error_description + "]";
	}

}
