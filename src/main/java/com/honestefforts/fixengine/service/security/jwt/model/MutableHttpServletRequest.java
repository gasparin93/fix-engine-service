package com.honestefforts.fixengine.service.security.jwt.model;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class MutableHttpServletRequest extends HttpServletRequestWrapper {

	private final Map<String, String> customHeaders;
	
	public MutableHttpServletRequest(HttpServletRequest request) {
		super(request);
		this.customHeaders = new HashMap<>();
	}

	public void putHeader(String name, String value) {
		this.customHeaders.put(name, value);
	}
	
	public String getHeader(String name) {
		String headerValue = customHeaders.get(name);
		
		if (headerValue != null) {
			return headerValue;
		}
		return ((HttpServletRequest) getRequest()).getHeader(name);
	}
}
