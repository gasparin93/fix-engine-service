package com.honestefforts.fixengine.service.security.jwt.utility;

import java.util.Date;

import javax.crypto.SecretKey;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.honestefforts.fixengine.service.constants.ApplicationConstants;
import com.honestefforts.fixengine.service.security.jwt.exception.InvalidTokenException;
import com.honestefforts.fixengine.service.security.jwt.model.TokenResponsePostAuth;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

@ExtendWith(SystemStubsExtension.class)
public class JwtTokenValidatorTests {
	
	   private JwtTokenValidator jwtTokenValidator;
	
	   private MacAlgorithm macAlgorithm;
	   private SecretKey key;	
	   
	   @SystemStub
	   private EnvironmentVariables envs = new EnvironmentVariables("X_CONSUMERS_IDS","asdf",
			   "X_CONSUMERS_USERNAMES","fixitengine",
			   "HS256_SECRET_KEY","sdf89s0df89ds8f90ds89fds89f8dsf903nvhcjdf89d0=dsfd");

	   @Test
	   public void testJwtTokenValidatorValidateAccessTokenRequest1() throws Exception {
	      this.macAlgorithm = SIG.HS256;
	      this.key = Keys.hmacShaKeyFor((byte[])Decoders.BASE64.decode(ApplicationConstants.HS256_SECRET_KEY));
		  
	      jwtTokenValidator = new JwtTokenValidator();
	      Date now = new Date();
	      JwtBuilder builder = Jwts.builder().id("testId").issuedAt(now).subject("asdf").issuer("fixitengine").signWith(this.key, this.macAlgorithm);
	      long ttlMillis = 25200000L;
	      long expMillis = now.getTime() + ttlMillis;
	      Date exp = new Date(expMillis);
	      builder.expiration(exp);
	      String token = builder.compact();
	      TokenResponsePostAuth obj = this.jwtTokenValidator.validateAccessTokenRequest(token);
	      Assertions.assertTrue(!StringUtils.isEmpty(obj.getAccess_token()));
	   }

	   @Test
	   public void testJwtTokenValidatorValidateAccessTokenRequest2() throws Exception {
	      this.macAlgorithm = SIG.HS256;
	      this.key = Keys.hmacShaKeyFor((byte[])Decoders.BASE64.decode(ApplicationConstants.HS256_SECRET_KEY));
	      Date now = new Date();
	      
	      jwtTokenValidator = new JwtTokenValidator();
	      JwtBuilder builder = Jwts.builder().id("testId").issuedAt(now).subject("asdf").issuer("fixitengine").signWith(this.key, this.macAlgorithm);
	      Date exp = new Date(now.getTime());
	      builder.expiration(exp);
	      String token = builder.compact();
	      InvalidTokenException e = (InvalidTokenException)Assertions.assertThrows(InvalidTokenException.class, () -> {
	         this.jwtTokenValidator.validateAccessTokenRequest(token);
	      });
	      Assertions.assertNotNull(e);
	   }

	   @Test
	   public void testJwtTokenValidatorValidateAccessTokenRequest3() throws Exception {
	      this.macAlgorithm = SIG.HS256;
	      this.key = Keys.hmacShaKeyFor((byte[])Decoders.BASE64.decode(ApplicationConstants.HS256_SECRET_KEY));
	      ValidationUtil validationUtil = new ValidationUtil();
	      jwtTokenValidator = new JwtTokenValidator();
	      
	      Date now = validationUtil.generateCurrentTimeDateWithCentralTimeZone();
	      JwtBuilder builder = Jwts.builder().id("testId").issuedAt(now).subject("asdf").issuer("fixitengine").signWith(this.key, this.macAlgorithm);
	      Date exp = new Date(now.getTime());
	      builder.expiration(exp);
	      String token = builder.compact();
	      InvalidTokenException e = (InvalidTokenException)Assertions.assertThrows(InvalidTokenException.class, () -> {
	         this.jwtTokenValidator.validateAccessTokenRequest(token);
	      });
	      Assertions.assertNotNull(e);
	   }

	   @Test
	   public void testJwtTokenValidatorValidateAccessTokenRequest4() throws Exception {
	      this.macAlgorithm = SIG.HS256;
	      this.key = Keys.hmacShaKeyFor((byte[])Decoders.BASE64.decode(ApplicationConstants.HS256_SECRET_KEY));
	      Date now = new Date();
	      
	      jwtTokenValidator = new JwtTokenValidator();
	      JwtBuilder builder = Jwts.builder().id("").issuedAt(now).subject("asdf").issuer("fixitengine").signWith(this.key, this.macAlgorithm);
	      long ttlMillis = 25200000L;
	      long expMillis = now.getTime() + ttlMillis;
	      Date exp = new Date(expMillis);
	      builder.expiration(exp);
	      String token = builder.compact();
	      InvalidTokenException e = (InvalidTokenException)Assertions.assertThrows(InvalidTokenException.class, () -> {
	         this.jwtTokenValidator.validateAccessTokenRequest(token);
	      });
	      Assertions.assertNotNull(e);
	   }
}
