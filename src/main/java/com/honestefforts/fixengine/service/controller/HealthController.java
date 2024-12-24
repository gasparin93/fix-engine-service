package com.honestefforts.fixengine.service.controller;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1"})
public class HealthController {

	private Logger logger = Logger.getLogger(HealthController.class.getName());
	
	@GetMapping("/health")
	public @ResponseBody ResponseEntity<?> healthCheck() {
		logger.log(Level.INFO, "Inside Healthcheck endpoint");
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}
}
