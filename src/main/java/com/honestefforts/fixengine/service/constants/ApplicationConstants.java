package com.honestefforts.fixengine.service.constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationConstants {
	
	public static final String INVALID_TOKEN = "invalid_token";
	public static final String INVALID_TOKEN_DESC = "The access token is invalid or has expired";
	public static final String INVALID_REQUEST = "invalid_request";
	public static final String INVALID_REQUEST_MISSING_TOKEN = "The access token is missing";
	private static final Map<String, String> ERROR_MAP;
	static {
		ERROR_MAP = new ConcurrentHashMap<>();
		ERROR_MAP.put(INVALID_TOKEN, INVALID_TOKEN_DESC);
        ERROR_MAP.put(INVALID_REQUEST, INVALID_REQUEST_MISSING_TOKEN);
	}
	public static Map<String, String> getERROR_MAP() {
		return ERROR_MAP;
	}
	
	public static final String HS256_SECRET_KEY = (System.getenv("HS256_SECRET_KEY") != null) ? 
            System.getenv("HS256_SECRET") : "unknown";
	
	public static final String X_CONSUMERS_IDS = System.getenv("X_CONSUMERS_IDS");
	public static final String X_CONSUMERS_USERNAMES = System.getenv("X_CONSUMERS_USERNAMES");
	
	private static final Map<String, String> AUTHORIZED_CLIENTS_MAP;
	static {
		AUTHORIZED_CLIENTS_MAP = new ConcurrentHashMap<>();
		String[] consumers = X_CONSUMERS_IDS.split(",");
		String[] usernames = X_CONSUMERS_USERNAMES.split(",");
		for (int i = 0; i < consumers.length; i++) {
			AUTHORIZED_CLIENTS_MAP.put(consumers[i], usernames[i]);
		}
		// etc. 
	}

	public static Map<String, String> getAUTHORIZED_CLIENTS_MAP() {
		return AUTHORIZED_CLIENTS_MAP;
	}
}
