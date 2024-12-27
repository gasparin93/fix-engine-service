package com.honestefforts.fixengine.service.security.jwt;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import com.honestefforts.fixengine.service.constants.ApplicationConstants;
import com.honestefforts.fixengine.service.security.jwt.exception.InvalidTokenException;
import com.honestefforts.fixengine.service.security.jwt.model.JwtAuthentication;
import com.honestefforts.fixengine.service.security.jwt.model.TokenResponsePostAuth;
import com.honestefforts.fixengine.service.security.jwt.utility.JwtTokenValidator;

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
		logger.log(Level.INFO, "Attempting to authenticate for a request {0}", 
				request.getRequestURI());
		JwtAuthentication jwtAuthentication = null;
		try {
			String token = extractTokenAsString(request);
			TokenResponsePostAuth tokenResponse = tokenVerifier
					.validateAccessTokenRequest(token);
			jwtAuthentication = new JwtAuthentication(tokenResponse);
			jwtAuthentication.setAuthenticated(true);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			jwtAuthentication = new JwtAuthentication(
					new TokenResponsePostAuth("",0L,"","")); 
			// null/empty token and response.
			jwtAuthentication.setAuthenticated(false);
			throw e; 
			/* will be InvalidTokenException which maps to Http Status Code 401 
			* Unauthorized */
		}
		
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(jwtAuthentication);
		SecurityContextHolder.setContext(context);
		
		return jwtAuthentication;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
			HttpServletResponse response, FilterChain chain, Authentication authResult) 
					throws IOException, ServletException {
		logger.log(Level.INFO, "Successful Authentication for the request {0}", 
				request.getRequestURI());
		
		request.setAttribute("encodedUserId", 
				String.valueOf(((TokenResponsePostAuth) authResult.getDetails())
						.getEncodedUserId()));
		request.setAttribute("consumerUsername",
				String.valueOf(((TokenResponsePostAuth) authResult.getDetails())
						.getConsumerUsername()));
		chain.doFilter(request, response);
	}
	
	public String extractTokenAsString(HttpServletRequest request) {
		try {
			String parseStr = request.getHeader("Authorization");
			if(!parseStr.startsWith("Bearer ")) {
                throw new InvalidTokenException("Invalid or Malformed Value");
            }
			parseStr = parseStr.substring(7); // has to advance beyond "Bearer "
			return parseStr;
		} catch (Exception e) {
            throw new InvalidTokenException(
            		ApplicationConstants.INVALID_REQUEST, e);
		}
	}
}
