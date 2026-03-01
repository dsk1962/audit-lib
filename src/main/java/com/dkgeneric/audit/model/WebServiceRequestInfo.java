package com.dkgeneric.audit.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WebServiceRequestInfo {

	private static HashSet<String> excludeHeaderNames = new HashSet<>(Arrays.asList("authorization"));

	private String url;

	private String method;
	private String contentType;
	private int responseStatus;
	private Map<String, String> headers = new HashMap<>(20);
	public WebServiceRequestInfo(HttpServletRequest request) {
		if (request != null) {
			url = request.getRequestURL().toString();
			method = request.getMethod();
			contentType = request.getContentType();
			request.getHeaderNames().asIterator().forEachRemaining(name -> {
				if (!excludeHeaderNames.contains(name))
					headers.put(name.toLowerCase(), request.getHeader(name));
			});
		}
	}
}
