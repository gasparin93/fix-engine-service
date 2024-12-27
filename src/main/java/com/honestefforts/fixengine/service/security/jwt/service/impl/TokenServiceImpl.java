package com.honestefforts.fixengine.service.security.jwt.service.impl;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.honestefforts.fixengine.service.security.jwt.exception.TokenServiceException;
import com.honestefforts.fixengine.service.security.jwt.model.TokenResponse;
import com.honestefforts.fixengine.service.security.jwt.service.TokenService;

@Service
//@Transactional
// above annotation is only valid once spring boot starter jpa is added... if ever
// used/needed though... First we will focus on storing in memory.. basic LRU cache
public class TokenServiceImpl implements TokenService {
	
	private static final Logger logger = Logger.getLogger(TokenServiceImpl.class.getName());

	@Override
	public TokenResponse performJWTTokenService(String xConsumerID, String xConsumerUsername, String uri)
			throws Exception, TokenServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
