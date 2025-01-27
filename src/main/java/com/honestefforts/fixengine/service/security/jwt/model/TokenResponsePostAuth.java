package com.honestefforts.fixengine.service.security.jwt.model;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class TokenResponsePostAuth {

	private String encodedUserId;
	private long expires;
	private String access_token;
	private String consumerUsername;
	
	public TokenResponsePostAuth(String encodedUserId, long expires, String access_token, String consumerUsername) {
		this.encodedUserId = encodedUserId;
		this.expires = expires;
		this.access_token = access_token;
		this.consumerUsername = consumerUsername;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}

	public String getEncodedUserId() {
		return encodedUserId;
	}

	public void setEncodedUserId(String encodedUserId) {
		this.encodedUserId = encodedUserId;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getConsumerUsername() {
		return consumerUsername;
	}

	public void setConsumerUsername(String consumerUsername) {
		this.consumerUsername = consumerUsername;
	}

	@Override
	public String toString() {
		return "TokenResponsePostAuth [encodedUserId=" + encodedUserId + ", expires=" + expires + ", access_token="
				+ access_token + ", consumerUsername=" + consumerUsername + "]";
	}
}
