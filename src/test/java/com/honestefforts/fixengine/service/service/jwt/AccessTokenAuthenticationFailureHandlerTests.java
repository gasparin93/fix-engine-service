package com.honestefforts.fixengine.service.service.jwt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;

import com.honestefforts.fixengine.service.security.jwt.AccessTokenAuthenticationFailureHandler;
import com.honestefforts.fixengine.service.security.jwt.exception.InvalidTokenException;

import jakarta.servlet.http.HttpServletRequest;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

@ExtendWith(SystemStubsExtension.class)
public class AccessTokenAuthenticationFailureHandlerTests {

   private AccessTokenAuthenticationFailureHandler accessTokenAuthenticationFailureHandler;
   
   @SystemStub
   private EnvironmentVariables envs = new EnvironmentVariables("X_CONSUMERS_IDS","asdf","X_CONSUMERS_USERNAMES","engine");
   
   @Test
   public void testEnvironmentVariables() {
	   Assertions.assertEquals("asdf", System.getenv("X_CONSUMERS_IDS"));
	   Assertions.assertEquals("engine", System.getenv("X_CONSUMERS_USERNAMES"));
	}
   
   @Test
   public void testAccessTokenAuthenticationFailureHandlerOnAuthenticationFailure() throws Exception {
	  accessTokenAuthenticationFailureHandler = new AccessTokenAuthenticationFailureHandler();
      InvalidTokenException invalidTokenException = new InvalidTokenException("ErrorMsg");
      HttpServletRequest req = (HttpServletRequest)Mockito.mock(HttpServletRequest.class);
      MockHttpServletResponse resp = new MockHttpServletResponse();
      this.accessTokenAuthenticationFailureHandler.onAuthenticationFailure(req, resp, invalidTokenException);
      Assertions.assertNotNull(this.accessTokenAuthenticationFailureHandler);
   }

   @Test
   public void testAccessTokenAuthenticationFailureHandlerCreateErrorBody() throws Exception {
	  accessTokenAuthenticationFailureHandler = new AccessTokenAuthenticationFailureHandler();
      InvalidTokenException invalidTokenException = new InvalidTokenException("ErrorMsg");
      String res = this.accessTokenAuthenticationFailureHandler.createErrorBody(invalidTokenException);
      Assertions.assertNotNull(res);
   }
}
