package com.honestefforts.fixengine.service.security.jwt.exception;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TokenServiceExceptionTest {
	
	private TokenServiceException tokenServiceException;
	
	@Test
	public void testTokenServiceException1() {
		tokenServiceException = new TokenServiceException("Test Err Msg", 401);
		assertNotNull(tokenServiceException);
	}
	
	@Test
	public void testTokenServiceException2() {
		tokenServiceException = new TokenServiceException("Test Err Msg", 
				new Exception("Test Exception Obj"), 401);
		assertNotNull(tokenServiceException);
	}
	
   @Test
   public void testTokenServiceException3() throws Exception {
      this.tokenServiceException = new TokenServiceException("Test Err Msg", 401);
      this.tokenServiceException.setHttpStatusCode(503);
      Assertions.assertEquals(this.tokenServiceException.getHttpStatusCode(), 503);
   }
   
   
}
