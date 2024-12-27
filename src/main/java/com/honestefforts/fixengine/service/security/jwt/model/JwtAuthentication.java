package com.honestefforts.fixengine.service.security.jwt.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthentication extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -186222233943939439L;

	private final TokenResponsePostAuth response;

	public JwtAuthentication(TokenResponsePostAuth response) {
		super(response.getAuthorities());
		this.response = response;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return response.toString();
	}

	@Override
	public TokenResponsePostAuth getDetails() {
		return response;
	}
	
}
