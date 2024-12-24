package com.honestefforts.fixengine.service.security.jwt;

import java.io.IOException;

import org.jboss.logging.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import com.honestefforts.fixengine.service.security.jwt.validation.JwtTokenValidator;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AccessTokenFilter extends AbstractAuthenticationProcessingFilter {

	private Logger logger = Logger.getLogger(AccessTokenFilter.class.getName());
	
	private final JwtTokenValidator tokenVerifier; 
	
	public AccessTokenFilter(JwtTokenValidator jwtTokenValidator, 
			AuthenticationFailureHandler authenticationFailureHandler) {
		super(AnyRequestMatcher.INSTANCE);
		setAuthenticationFailureHandler(authenticationFailureHandler);
		this.tokenVerifier = jwtTokenValidator;
	}


	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
			HttpServletResponse response, FilterChain chainn, Authentication authResult) 
					throws IOException, ServletException {
		
	}
	
}
