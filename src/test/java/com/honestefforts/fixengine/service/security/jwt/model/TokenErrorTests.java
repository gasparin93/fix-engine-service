package com.honestefforts.fixengine.service.security.jwt.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TokenErrorTests {
	
	   private TokenError tokenError;

	   @Test
	   public void testTokenError() throws Exception {
	      this.tokenError = new TokenError();
	      this.tokenError.setError_description("testErrMsg");
	      this.tokenError.setError("internal_server_error");
	      Assertions.assertNotNull(this.tokenError.getError_description());
	      Assertions.assertNotNull(this.tokenError.getError());
	      Assertions.assertNotNull(this.tokenError.toString());
	   }
}
