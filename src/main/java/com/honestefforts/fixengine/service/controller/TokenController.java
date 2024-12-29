package com.honestefforts.fixengine.service.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.honestefforts.fixengine.service.constants.ApplicationConstants;
import com.honestefforts.fixengine.service.security.jwt.exception.TokenServiceException;
import com.honestefforts.fixengine.service.security.jwt.model.TokenError;
import com.honestefforts.fixengine.service.security.jwt.model.TokenResponse;
import com.honestefforts.fixengine.service.security.jwt.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping(value = {"/v1"})
public class TokenController {
	
	private Logger logger = Logger.getLogger(TokenController.class.getName());
	
	@Autowired
	private TokenService tokenService;

	@PostMapping(value = "/oauth2.0/cached/token")
	public @ResponseBody ResponseEntity<?> postToken(
			@RequestHeader(name = "X-Consumer-ID") @NotBlank(message="Verify consumerID!") String consumerId,
			@RequestHeader(name = "X-Consumer-Username") @NotBlank(message="Verify consumerUsername!") String consumerUsername,
			HttpServletRequest request) {
		TokenResponse tokenResponse = new TokenResponse("", "", 0L);
		try {
			tokenResponse = tokenService.performJWTTokenService(consumerId, consumerUsername, request.getRequestURI());
		} catch (TokenServiceException e) {
			// handles 401 for invalid API Client or super rare http 500 scenarios.
			logger.log(Level.SEVERE, e.getMessage());
			TokenError t = new TokenError();
			t.setError_description(e.getMessage());
			t.setError(ApplicationConstants.getStatusCodeToErrorTypeMap()
					.getOrDefault(e.getHttpStatusCode(), ApplicationConstants.INTERNAL_SERVER_ERROR));
			return ResponseEntity.status(e.getHttpStatusCode()).body(t);
		} catch (Exception e) {
			// at this point, an error happened in the "finally" block of TokenService. 
			// So return an HTTP 500 status code response.
			logger.log(Level.SEVERE, e.getMessage());
			TokenError t = new TokenError();
			t.setError_description(e.getMessage());
			t.setError(ApplicationConstants.INTERNAL_SERVER_ERROR);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(t);
		}
		return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
	}
}
