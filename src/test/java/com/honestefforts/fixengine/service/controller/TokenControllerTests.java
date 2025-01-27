package com.honestefforts.fixengine.service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

@SpringBootTest
@ContextConfiguration
@TestPropertySource(
		   locations = {"classpath:application.yml"}
		)
@ExtendWith(SystemStubsExtension.class)
public class TokenControllerTests {
		
	   @Autowired
	   private WebApplicationContext webApplicationContext;
	   
	   private MockMvc mockMvc;
	   
	   @SystemStub
	   private EnvironmentVariables envs = new EnvironmentVariables("X_CONSUMERS_IDS","asdf",
			   "X_CONSUMERS_USERNAMES","fixitengine",
			   "HS256_SECRET_KEY","sdf89s0df89ds8f90ds89fds89f8dsf903nvhcjdf89d0=dsfd");
	   
	   @BeforeEach
	   public void setUp() {
		   this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	   }
	   
	   @Test
	   public void testTokenControllerPostToken1() throws Exception {
	      MockHttpSession session = new MockHttpSession();
	      this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/oauth2.0/cached/token").contentType(
	    		  MediaType.APPLICATION_JSON).header("X-Consumer-ID", new Object[]{"asdf"}).header("X-Consumer-Username", new Object[]{"fixitengine"})
	    		  .session(session)).andExpect(MockMvcResultMatchers.status().isOk());
	   }

	   @Test
	   public void testTokenControllerPostToken2() throws Exception {
	      MockHttpSession session = new MockHttpSession();
	      this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/oauth2.0/cached/token").contentType(MediaType.APPLICATION_JSON)
	    		  .header("X-Consumer-ID", new Object[]{"testId"}).header("X-Consumer-Username", new Object[]{"testUserName"}).session(session)).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	   }

}
