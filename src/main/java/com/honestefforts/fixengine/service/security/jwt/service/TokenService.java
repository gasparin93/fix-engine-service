package com.honestefforts.fixengine.service.security.jwt.service;

import com.honestefforts.fixengine.service.security.jwt.exception.TokenServiceException;
import com.honestefforts.fixengine.service.security.jwt.model.TokenResponse;

public interface TokenService {

	TokenResponse performJWTTokenService(String xConsumerID, String xConsumerUsername, String uri) throws Exception, TokenServiceException;
}
