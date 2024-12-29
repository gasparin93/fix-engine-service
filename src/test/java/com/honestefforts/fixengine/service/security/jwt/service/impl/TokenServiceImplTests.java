package com.honestefforts.fixengine.service.security.jwt.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import com.honestefforts.fixengine.service.constants.ApplicationConstants;
import com.honestefforts.fixengine.service.security.jwt.exception.TokenServiceException;
import com.honestefforts.fixengine.service.security.jwt.model.TokenResponse;
import com.honestefforts.fixengine.service.security.jwt.utility.ValidationUtil;

import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

@ExtendWith(SystemStubsExtension.class)
public class TokenServiceImplTests {
	
	   private TokenServiceImpl tokenServiceImpl;
	
	   @SystemStub
	   private EnvironmentVariables envs = new EnvironmentVariables("X_CONSUMERS_IDS","asdf",
			   "X_CONSUMERS_USERNAMES","engine",
			   "HS256_SECRET_KEY","sdf89s0df89ds8f90ds89fds89f8dsf903nvhcjdf89d0=dsfd");
	
	   private ValidationUtil validationUtil;
	
	   @Test
	   public void testTokenServicePostConstruct() throws Exception {
          this.tokenServiceImpl = new TokenServiceImpl();
	      ReflectionTestUtils.setField(this.tokenServiceImpl, "secretKey", ApplicationConstants.HS256_SECRET_KEY);
	      this.tokenServiceImpl.postConstruct();
	      Assertions.assertNotNull(this.tokenServiceImpl);
	   }

	   @Test
	   public void testTokenServiceGenerateNewJWTTokenAndPersistToCache() throws Exception {
		  this.tokenServiceImpl = new TokenServiceImpl();
		  validationUtil = new ValidationUtil();
	      ReflectionTestUtils.setField(this.tokenServiceImpl, "validationUtil", validationUtil);
	      ReflectionTestUtils.setField(this.tokenServiceImpl, "macAlgorithm", SIG.HS256);
	      ReflectionTestUtils.setField(this.tokenServiceImpl, "key", Keys.hmacShaKeyFor((byte[])Decoders.BASE64.decode(ApplicationConstants.HS256_SECRET_KEY)));
	      TokenResponse resp = this.tokenServiceImpl.generateNewJWTTokenAndPersistToCache("testId", "testConsumerName");
	      Assertions.assertNotNull(resp);
	   }

	   @Test
	   public void testTokenServiceExpiresWithinFiveMinutes1() throws Exception {
		  this.tokenServiceImpl = new TokenServiceImpl();
		  validationUtil = new ValidationUtil();
	      ReflectionTestUtils.setField(this.tokenServiceImpl, "validationUtil", this.validationUtil);
	      long expire = this.validationUtil.generateCurrentTimeDateWithCentralTimeZone().getTime() + 600000L;
	      TokenResponse obj = new TokenResponse("", "", expire);
	      Assertions.assertFalse(this.tokenServiceImpl.expiresWithinDeclaredMinutes(obj));
	   }

	   @Test
	   public void testTokenServiceExpiresWithinFiveMinutes2() throws Exception {
		  this.tokenServiceImpl = new TokenServiceImpl();
		  validationUtil = new ValidationUtil();
	      ReflectionTestUtils.setField(this.tokenServiceImpl, "validationUtil", this.validationUtil);
	      long expire = this.validationUtil.generateCurrentTimeDateWithCentralTimeZone().getTime();
	      TokenResponse obj = new TokenResponse("", "", expire);
	      Assertions.assertTrue(this.tokenServiceImpl.expiresWithinDeclaredMinutes(obj));
	   }

	   @Test
	   public void testTokenServiceCheckConsumerIdAndConsumerUserNameAgainstAuthorizedClientsMap1() {
		   this.tokenServiceImpl = new TokenServiceImpl();
		   Assertions.assertFalse(this.tokenServiceImpl.checkConsumerIdAndConsumerUserNameAgainstAuthorizedClientsMap("asdf", "engine"));
	   }
	   
	   @Test
	   public void testTokenServiceCheckConsumerIdAndConsumerUserNameAgainstAuthorizedClientsMap2() {
		   this.tokenServiceImpl = new TokenServiceImpl();
		   Assertions.assertTrue(this.tokenServiceImpl.checkConsumerIdAndConsumerUserNameAgainstAuthorizedClientsMap("cccc", "ffff"));
	   }
	   
	   @Test
	   public void testTokenServicePerformJWTTokenService1() throws Exception {
          this.tokenServiceImpl = new TokenServiceImpl();
		  validationUtil = new ValidationUtil();
	      ReflectionTestUtils.setField(this.tokenServiceImpl, "validationUtil", this.validationUtil);
	      ReflectionTestUtils.setField(this.tokenServiceImpl, "macAlgorithm", SIG.HS256);
	      ReflectionTestUtils.setField(this.tokenServiceImpl, "key", Keys.hmacShaKeyFor((byte[])Decoders.BASE64.decode(ApplicationConstants.HS256_SECRET_KEY)));
	      TokenResponse resp = this.tokenServiceImpl.performJWTTokenService("asdf", "engine", "/v1/health");
	      Assertions.assertNotNull(resp);
	   }

	   @Test
	   public void testTokenServicePerformJWTTokenService2() throws Exception {
		   this.tokenServiceImpl = new TokenServiceImpl();
		   TokenServiceException e = (TokenServiceException)Assertions.assertThrows(TokenServiceException.class, () -> {
	         this.tokenServiceImpl.performJWTTokenService(null, "engine", "/v1/health");
	      });
	      Assertions.assertEquals(401, e.getHttpStatusCode());
	   }

	   @Test
	   public void testTokenServicePerformJWTTokenService3() throws Exception {
		   this.tokenServiceImpl = new TokenServiceImpl();
		   TokenServiceException e = (TokenServiceException)Assertions.assertThrows(TokenServiceException.class, () -> {
	         this.tokenServiceImpl.performJWTTokenService("asdf", null, "/v1/health");
	      });
	      Assertions.assertEquals(401, e.getHttpStatusCode());
	   }

	   @Test
	   public void testTokenServicePerformJWTTokenService4() throws Exception {
		   this.tokenServiceImpl = new TokenServiceImpl();
		   TokenServiceException e = (TokenServiceException)Assertions.assertThrows(TokenServiceException.class, () -> {
	         this.tokenServiceImpl.performJWTTokenService("asdf", "engine", null);
	      });
	      Assertions.assertEquals(500, e.getHttpStatusCode());
	   }

	   @Test
	   public void testTokenServicePerformJWTTokenService5() throws Exception {
		   this.tokenServiceImpl = new TokenServiceImpl();
		   TokenServiceException e = (TokenServiceException)Assertions.assertThrows(TokenServiceException.class, () -> {
	         this.tokenServiceImpl.performJWTTokenService("testId", "testUserName", "/auth/v1.0/enroll/delta");
	      });
	      Assertions.assertEquals(401, e.getHttpStatusCode());
	   }
	   
}
