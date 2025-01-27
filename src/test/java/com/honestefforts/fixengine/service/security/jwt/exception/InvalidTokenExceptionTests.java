package com.honestefforts.fixengine.service.security.jwt.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InvalidTokenExceptionTests {

   private InvalidTokenException invalidTokenException;
	
   @Test
   public void testInvalidTokenException1() throws Exception {
      this.invalidTokenException = new InvalidTokenException("Test Err Msg");
      Assertions.assertNotNull(this.invalidTokenException);
   }

   @Test
   public void testInvalidTokenException2() throws Exception {
      this.invalidTokenException = new InvalidTokenException("Test Err Msg", new Exception("Test Exception Obj"));
      Assertions.assertNotNull(this.invalidTokenException);
   }
}
