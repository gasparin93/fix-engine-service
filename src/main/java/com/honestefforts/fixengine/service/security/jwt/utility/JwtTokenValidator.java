package com.honestefforts.fixengine.service.security.jwt.utility;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKey;

import org.apache.commons.lang3.StringUtils;

import com.honestefforts.fixengine.service.constants.ApplicationConstants;
import com.honestefforts.fixengine.service.security.jwt.exception.InvalidTokenException;
import com.honestefforts.fixengine.service.security.jwt.model.TokenResponsePostAuth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtTokenValidator {

	private Logger logger = Logger.getLogger(JwtTokenValidator.class.getName());
	
	private SecretKey key;
	
	private ValidationUtil validationUtil = new ValidationUtil();
	
	@SuppressWarnings("unchecked")
	public TokenResponsePostAuth validateAccessTokenRequest(String token) {
		Jwt<Header, Claims> tokenDecoded = null;
		TokenResponsePostAuth tokenResponse = 
				new TokenResponsePostAuth("",0L,"","");
		String errorMsg = "";
		
		try {
			key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(
					ApplicationConstants.HS256_SECRET_KEY));
			
			tokenDecoded = (Jwt<Header, Claims>) Jwts.parser()
					.requireIssuer("fixitengine")
					.verifyWith(key)
					.build()
					.parse(token);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			errorMsg = e.getMessage();
			throw new InvalidTokenException(ApplicationConstants.INVALID_TOKEN);
		}
		
		// custom logic to deal with UTC timezone validation especially when
		// testing on local machine..
		try {
			long currentTime = validationUtil
					.generateCurrentTimeDateWithCentralTimeZone().getTime();
			long expirationTime = tokenDecoded.getPayload().getExpiration().getTime();
			
			if (currentTime >= expirationTime) {
                throw new InvalidTokenException(ApplicationConstants.INVALID_TOKEN);
            }
		} catch (Exception e) {
			logger.log(Level.SEVERE, ApplicationConstants.INVALID_TOKEN);
			throw new InvalidTokenException(ApplicationConstants.INVALID_TOKEN);
		}
		
		if(tokenDecoded != null && !StringUtils.isEmpty(tokenDecoded.getPayload()
				.getId())) {
			logger.log(Level.INFO, tokenDecoded.getPayload().getId());
			tokenResponse.setAccess_token(token);
			tokenResponse.setExpires(tokenDecoded.getPayload()
					.getExpiration().getTime());
			tokenResponse.setEncodedUserId(tokenDecoded.getPayload().getId());
			tokenResponse.setConsumerUsername(tokenDecoded.getPayload()
					.getIssuer());
		} else {
			logger.log(Level.SEVERE, ApplicationConstants.INVALID_TOKEN);
			throw new InvalidTokenException(errorMsg);
		}
		return tokenResponse;
	}
	
	
}
