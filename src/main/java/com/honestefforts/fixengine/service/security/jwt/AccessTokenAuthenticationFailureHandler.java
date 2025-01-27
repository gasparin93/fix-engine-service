package com.honestefforts.fixengine.service.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.google.gson.Gson;
import com.honestefforts.fixengine.service.constants.ApplicationConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AccessTokenAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(createErrorBody(exception));
	}

	public String createErrorBody(AuthenticationException exception ) {
		Map<String, Object> details = new HashMap<>();
		details.put("error_description", ApplicationConstants.getERROR_MAP()
				.getOrDefault(exception.getMessage(), 
						ApplicationConstants.INVALID_TOKEN_DESC));
		details.put("error", exception.getMessage());
		
		JSONObject exceptionObj = new JSONObject(details);
		return new Gson().toJson(exceptionObj);
	}
}
