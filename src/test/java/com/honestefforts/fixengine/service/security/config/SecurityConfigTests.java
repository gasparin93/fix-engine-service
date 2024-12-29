package com.honestefforts.fixengine.service.security.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.honestefforts.fixengine.service.security.jwt.AccessTokenAuthenticationFailureHandler;

import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

@ExtendWith(SystemStubsExtension.class)
public class SecurityConfigTests {
	
	   @SystemStub
	   private EnvironmentVariables envs = new EnvironmentVariables("X_CONSUMERS_IDS","asdf",
			   "X_CONSUMERS_USERNAMES","engine",
			   "HS256_SECRET_KEY","sdf89s0df89ds8f90ds89fds89f8dsf903nvhcjdf89d0=dsfd");
	   
	   private SecurityConfig securityConfig;
	   
	   @Test
	   public void testSecurityConfiAauthenticationFailureHandler() throws Exception {
          this.securityConfig = new SecurityConfig();
	      AccessTokenAuthenticationFailureHandler obj = (AccessTokenAuthenticationFailureHandler)this.securityConfig.authenticationFailureHandler();
	      Assertions.assertNotNull(obj);
	   }

}
