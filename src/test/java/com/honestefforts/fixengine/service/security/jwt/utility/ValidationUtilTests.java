package com.honestefforts.fixengine.service.security.jwt.utility;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.honestefforts.fixengine.service.security.jwt.model.MutableHttpServletRequest;

public class ValidationUtilTests {
	
	   private ValidationUtil validationUtil;

	   @Test
	   public void testJwtTokenValidatorGenerateCurrentTimeDateWithCentralTimeZone() throws Exception {
		   validationUtil = new ValidationUtil();
		   Date obj = this.validationUtil.generateCurrentTimeDateWithCentralTimeZone();
	       Assertions.assertNotNull(obj);
	   }
	   
	   @Test
	   public void testMutableHttpServletRequestOverall() throws Exception {
	      MockHttpServletRequest mockReq = new MockHttpServletRequest();
	      MutableHttpServletRequest mutableHttpServletRequest = new MutableHttpServletRequest(mockReq);
	      Assertions.assertNull(mutableHttpServletRequest.getHeader("testHeader"));
	      mutableHttpServletRequest.putHeader("testHeader", "testValue");
	      Assertions.assertNotNull(mutableHttpServletRequest.getHeader("testHeader"));
	   }

	   @Test
	   public void testValidationUtilConstructRequestLogsAfterJWTTokenFilterRequest() throws Exception {
	      MockHttpServletRequest mockReq = new MockHttpServletRequest();
	      mockReq.setAttribute("encodedUserId", "testId");
	      mockReq.setAttribute("consumerUsername", "testUserName");
	      
	      validationUtil = new ValidationUtil();
	      StringBuffer strB = this.validationUtil.constructRequestLogsAfterJWTTokenFilterRequest(mockReq);
	      Assertions.assertNotNull(strB);
	   }

	   @Test
	   public void testValidationUtilConstructMutableHttpServletRequest() throws Exception {
	      MockHttpServletRequest mockReq = new MockHttpServletRequest();
	      mockReq.setAttribute("encodedUserId", "testId");
	      mockReq.setAttribute("consumerUsername", "testUserName");
	      
	      validationUtil = new ValidationUtil();
	      MutableHttpServletRequest obj = this.validationUtil.constructMutableHttpServletRequest(mockReq);
	      Assertions.assertNotNull(obj);
	   }
}
