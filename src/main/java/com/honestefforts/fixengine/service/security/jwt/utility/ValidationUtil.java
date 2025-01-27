package com.honestefforts.fixengine.service.security.jwt.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.honestefforts.fixengine.service.security.jwt.model.MutableHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ValidationUtil {
		
	public Date generateCurrentTimeDateWithCentralTimeZone() throws ParseException {
		SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		
		long nowMillis = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(nowMillis));
		
		String dateStr = isoFormat.format(calendar.getTime());
		
		/* Second SimpleDateFormat object is required here.. otherwise using 
		 * isoFormat object again will NOT retain the converted timezone date 
		 * format to UTC but rather.. the local machine which is incorrect. */
		SimpleDateFormat sdfConverted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = sdfConverted.parse(dateStr);
		
		return now;
	}
	
	public StringBuffer constructRequestLogsAfterJWTTokenFilterRequest(
			HttpServletRequest request) {
		StringBuffer requestLogs = new StringBuffer();
		requestLogs.append("Request URI: " + request.getRequestURI());
		requestLogs.append("\nRequest Method: " + request.getMethod());
		requestLogs.append("\nRequest Headers: " + request.getHeaderNames());
		requestLogs.append("\nRequest Prevalidation of EncodedUserId: ");
		requestLogs.append(String.valueOf(request.getAttribute("encodedUserId")));
		requestLogs.append("\nRequest ConsumerUsername: ");
		requestLogs.append(String.valueOf(request
				.getAttribute("consumerUsername")));

		return requestLogs;
	}
	
	public MutableHttpServletRequest constructMutableHttpServletRequest(
			HttpServletRequest request) {
		MutableHttpServletRequest mutableRequest = 
				new MutableHttpServletRequest(request);
		mutableRequest.putHeader("X-Consumer-ID", request.getAttribute("encodedUserId").toString());
		mutableRequest.putHeader("X-Consumer-Username", request.getAttribute("consumerUsername").toString());
		
		return mutableRequest;
	}
}
