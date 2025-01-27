package com.honestefforts.fixengine.service.service.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.honestefforts.fixengine.service.constants.ApplicationConstants;
import com.honestefforts.fixengine.service.security.jwt.AccessTokenAuthenticationFailureHandler;
import com.honestefforts.fixengine.service.security.jwt.AccessTokenFilter;
import com.honestefforts.fixengine.service.security.jwt.exception.InvalidTokenException;
import com.honestefforts.fixengine.service.security.jwt.model.JwtAuthentication;
import com.honestefforts.fixengine.service.security.jwt.model.TokenResponsePostAuth;
import com.honestefforts.fixengine.service.security.jwt.utility.JwtTokenValidator;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

@ExtendWith(SystemStubsExtension.class)
public class AccessTokenFilterTests {
	
   private AccessTokenFilter accessTokenFilter;
   
   private MacAlgorithm macAlgorithm;
   private SecretKey key;	
   
   @SystemStub
   private EnvironmentVariables envs = new EnvironmentVariables("X_CONSUMERS_IDS","asdf",
		   "X_CONSUMERS_USERNAMES","engine",
		   "HS256_SECRET_KEY","sdf89s0df89ds8f90ds89fds89f8dsf903nvhcjdf89d0=dsfd");
   
   @Test
   public void testEnvironmentVariables() {
	   Assertions.assertEquals("asdf", System.getenv("X_CONSUMERS_IDS"));
	   Assertions.assertEquals("engine", System.getenv("X_CONSUMERS_USERNAMES"));
	}
	
   @Test
   public void testAccessTokenFilterConstructor() throws Exception {
      this.accessTokenFilter = new AccessTokenFilter(new JwtTokenValidator(), new AccessTokenAuthenticationFailureHandler());
      Assertions.assertNotNull(this.accessTokenFilter);
   }

   @Test
   public void testAccessTokenFilterAttemptAuthentication1() throws Exception {
      this.accessTokenFilter = new AccessTokenFilter(new JwtTokenValidator(), new AccessTokenAuthenticationFailureHandler());
      MockHttpServletRequest req = new MockHttpServletRequest();
      HttpServletResponse resp = (HttpServletResponse)Mockito.mock(HttpServletResponse.class);
      InvalidTokenException e = (InvalidTokenException)Assertions.assertThrows(InvalidTokenException.class, () -> {
         this.accessTokenFilter.attemptAuthentication(req, resp);
      });
      Assertions.assertNotNull(e);
   }

   @Test
   public void testAccessTokenFilterAttemptAuthentication2() throws Exception {
      this.macAlgorithm = SIG.HS256;
      this.key = Keys.hmacShaKeyFor((byte[])Decoders.BASE64.decode(ApplicationConstants.HS256_SECRET_KEY));
	      
      Date now = new Date();
      JwtBuilder builder = Jwts.builder().id("testId").issuedAt(now).subject("testConsumerUsername").issuer("fixitengine").signWith(this.key, this.macAlgorithm);
      long ttlMillis = 25200000L;
      long expMillis = now.getTime() + ttlMillis;
      Date exp = new Date(expMillis);
      builder.expiration(exp);
      String token = builder.compact();
      accessTokenFilter = new AccessTokenFilter(new JwtTokenValidator(), new AccessTokenAuthenticationFailureHandler());
      MockHttpServletRequest req = new MockHttpServletRequest();
      req.addHeader("Authorization", "Bearer " + token);
      HttpServletResponse resp = (HttpServletResponse)Mockito.mock(HttpServletResponse.class);
      JwtAuthentication auth = (JwtAuthentication)this.accessTokenFilter.attemptAuthentication(req, resp);
      Assertions.assertNotNull(auth);
   }

   @Test
   public void testAccessTokenFilterSuccessfulAuthentication() throws Exception {
      accessTokenFilter = 
    		  new AccessTokenFilter(new JwtTokenValidator(), 
    				  new AccessTokenAuthenticationFailureHandler());
      TokenResponsePostAuth tokenObj = new TokenResponsePostAuth("fakeId", 100000L, "fakeToken", "fakeUserName");
      JwtAuthentication authMock = new JwtAuthentication(tokenObj);
      MockHttpServletRequest req = new MockHttpServletRequest();
      MockHttpServletResponse resp = new MockHttpServletResponse();
      MockFilterChain filterChain = new MockFilterChain();

      accessTokenFilter.successfulAuthentication(req, resp, filterChain, authMock);
      Assertions.assertNotNull(accessTokenFilter);
   }

   @Test
   public void testAccessTokenFilterExtractTokenAsString1() throws Exception {
      this.accessTokenFilter = new AccessTokenFilter(new JwtTokenValidator(), new AccessTokenAuthenticationFailureHandler());
      MockHttpServletRequest req = new MockHttpServletRequest();
      req.addHeader("Authorization", "Bearer fakeToken");
      String token = this.accessTokenFilter.extractTokenAsString(req);
      Assertions.assertEquals("fakeToken", token);
   }

   @Test
   public void testAccessTokenFilterExtractTokenAsString2() throws Exception {
      this.accessTokenFilter = new AccessTokenFilter(new JwtTokenValidator(), new AccessTokenAuthenticationFailureHandler());
      MockHttpServletRequest req = new MockHttpServletRequest();
      req.addHeader("Authorization", "fakeToken");
      InvalidTokenException e = (InvalidTokenException)Assertions.assertThrows(InvalidTokenException.class, () -> {
         this.accessTokenFilter.extractTokenAsString(req);
      });
      Assertions.assertNotNull(e);
   }

}
