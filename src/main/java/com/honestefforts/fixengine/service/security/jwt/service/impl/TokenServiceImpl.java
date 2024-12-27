package com.honestefforts.fixengine.service.security.jwt.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKey;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.honestefforts.fixengine.service.security.jwt.exception.TokenServiceException;
import com.honestefforts.fixengine.service.security.jwt.model.TokenResponse;
import com.honestefforts.fixengine.service.security.jwt.service.TokenService;
import com.honestefforts.fixengine.service.security.jwt.utility.InMemoryCacheJWTToken;
import com.honestefforts.fixengine.service.security.jwt.utility.ValidationUtil;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.PostConstruct;

@Service
//@Transactional
// above annotation is only valid once spring boot starter jpa is added... if ever
// used/needed though... First we will focus on storing in memory.. basic LRU cache
public class TokenServiceImpl implements TokenService {
	
	private static final Logger logger = Logger.getLogger(TokenServiceImpl.class.getName());

	/**
	 * 1 hour aka 60 minutes time to live for each token. Cleanup interval is every 30 seconds.
	* Cache size 1000: Key = API Client ID, Value = TokenResponse Object */
	static final InMemoryCacheJWTToken<String, TokenResponse> tokensCache = 
			new InMemoryCacheJWTToken<String, TokenResponse>(3600, 30, 1000);
	
	private MacAlgorithm macAlgorithm;
	private SecretKey key;
	
	@Value("${hs256.secretkey}")
	private String secretKey;
	
	@Value("${jwt.expiration.window.milliseconds}")
	private long expirationMillisecondsWindow;
	
	@Autowired
	private ValidationUtil validationUtil;
	
	/* Future spot for when/if Database is actually configured and wired up */
	
	// Ness Logging and Splunk Logging in future? Sure, if we ever get that far hah.
	
	@PostConstruct
	public void postConstruct() {
		logger.log(Level.INFO, "TokenServiceImpl postConstruct called.");
		logger.log(Level.INFO, "Initializing MacAlgorithm and SecretKey for JWT Token Service.");
		macAlgorithm = Jwts.SIG.HS256;
		key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}
	
	@Override
	public TokenResponse performJWTTokenService(String xConsumerID, String xConsumerUsername, String uri)
			throws Exception, TokenServiceException {
		logger.log(Level.INFO, "TokenServiceImpl performJWTTokenService called.");
		
		try {
			// validate consumer ID and username
			if (StringUtils.isEmpty(xConsumerID) || 
					StringUtils.isEmpty(xConsumerUsername)) {
				throw new Exception("Invalid API Consumer");
			}
				
			// validate uri
			if (StringUtils.isEmpty(uri)) {
				throw new Exception("Invalid URI");
			}
			
			// Check cache if a VALID token still exists first.
			TokenResponse tokenObj = tokensCache.get(xConsumerID);
			if(tokenObj == null) {
				logger.log(Level.INFO, "TokenServiceImpl performJWTTokenService cache miss.");
				// As there's no Database configuration/details yet.. just a basic in memory cache will suffice for now..
				// Generate a new token
				tokenObj = generateNewJWTTokenAndPersistToCache(xConsumerID, xConsumerUsername);
				return tokenObj;
			} else {
				logger.log(Level.INFO, "TokenServiceImpl performJWTTokenService cache hit.");
				// check and validate expiration timestamp.
				if(expiresWithinDeclaredMinutes(tokenObj)) {
					// generate a new token then persist to cache.
					logger.log(Level.INFO, "Token expired. Generating new one.");
					tokenObj = generateNewJWTTokenAndPersistToCache(xConsumerID, xConsumerUsername);
				} else {
					// within time limit so return current token object.
					logger.log(Level.INFO, "TokenServiceImpl performJWTTokenService token still valid.");
				}
				return tokenObj;
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"TokenServiceImpl performJWTTokenService encountered an exception: " + e.getMessage());
			e.printStackTrace();
			if(e.getMessage().equals("Invalid API Consumer")) {
				throw new TokenServiceException(
                        e.getMessage(), HttpStatus.UNAUTHORIZED.value());
			}
			throw new TokenServiceException(
					e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
		} finally {
			logger.log(Level.INFO, "TokenServiceImpl performJWTTokenService completed.");
			
		}
	}

	public TokenResponse generateNewJWTTokenAndPersistToCache(String xConsumerID, String xConsumerUsername) throws ParseException {
		TokenResponse response = new TokenResponse("","",0L);
		Date now = validationUtil.generateCurrentTimeDateWithCentralTimeZone();

		// Generate Token
		JwtBuilder builder = Jwts.builder().id(xConsumerID)
				.issuedAt(now).subject(xConsumerUsername)
				.issuer("fixitengine").signWith(key, macAlgorithm);
		
		long ttlMillis = 1800000L; // 30 minutes
		long expMillis = now.getTime() + ttlMillis;
		Date exp = new Date(expMillis);
		builder.expiration(exp);
		
		String token = builder.compact();
		response.setAccess_token(token);
		response.setExpires_at(expMillis);
		response.setToken_type("Bearer");
		
		// Persist to cache
		tokensCache.put(xConsumerID, response); // new token
		
		// Future spot for when/if Database is actually configured and wired up
		// Then we can persist to the Datastore as well...
		
		return response;
	}

	public boolean expiresWithinDeclaredMinutes(TokenResponse tokenObj) throws ParseException {
		long tokenExpirationTime = tokenObj.getExpires_at();
		Date now = validationUtil.generateCurrentTimeDateWithCentralTimeZone();

		if (now.getTime() < (tokenExpirationTime - expirationMillisecondsWindow)) {
			// means current token is still valid and within time limit.
			return false;
		}
		// regardless of whether it's inside the time limit or not, we will return true.
		return true;
	}
	
	
}
