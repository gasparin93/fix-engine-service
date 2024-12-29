package com.honestefforts.fixengine.service.security.jwt.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JwtAuthenticationTests {
	
	   private JwtAuthentication jwtAuth;

	   @Test
	   public void testJwtAuthenticationConstructor() throws Exception {
	      TokenResponsePostAuth obj = new TokenResponsePostAuth("", 100L, "", "");
	      this.jwtAuth = new JwtAuthentication(obj);
	      Assertions.assertNotNull(this.jwtAuth);
	   }

	   @Test
	   public void testJwtAuthenticationGetCredentials() throws Exception {
	      TokenResponsePostAuth obj = new TokenResponsePostAuth("", 100L, "", "");
	      this.jwtAuth = new JwtAuthentication(obj);
	      Assertions.assertNull(this.jwtAuth.getCredentials());
	   }

	   @Test
	   public void testJwtAuthenticationGetPrincipal() throws Exception {
	      TokenResponsePostAuth obj = new TokenResponsePostAuth("", 100L, "", "");
	      this.jwtAuth = new JwtAuthentication(obj);
	      Assertions.assertNotNull(this.jwtAuth.getPrincipal());
	   }

	   @Test
	   public void testJwtAuthenticationGetDetails() throws Exception {
	      TokenResponsePostAuth obj = new TokenResponsePostAuth("", 100L, "", "");
	      this.jwtAuth = new JwtAuthentication(obj);
	      Assertions.assertNotNull(this.jwtAuth.getDetails());
	   }
}
