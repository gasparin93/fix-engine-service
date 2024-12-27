package com.honestefforts.fixengine.service.security.jwt.model;

public class TokenResponse {

	private String token_type;
	private String access_token;
	private long expires_at;
	private String expires_at_tz = "UTC";
	
	public TokenResponse(String token_type, String access_token, long expires_at) {
		this.token_type = token_type;
		this.access_token = access_token;
		this.expires_at = expires_at;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public long getExpires_at() {
		return expires_at;
	}

	public void setExpires_at(long expires_at) {
		this.expires_at = expires_at;
	}

	public String getExpires_at_tz() {
		return expires_at_tz;
	}

	public void setExpires_at_tz(String expires_at_tz) {
		this.expires_at_tz = expires_at_tz;
	}

	@Override
	public String toString() {
		return "TokenResponse [token_type=" + token_type + ", access_token=" + access_token + ", expires_at="
				+ expires_at + ", expires_at_tz=" + expires_at_tz + "]";
	}
	
}
