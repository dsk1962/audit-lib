package com.dkgeneric.audit.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.dkgeneric.audit.model.RequestParameter;
import com.dkgeneric.audit.model.WebServiceRequestAuditEntry;
import com.dkgeneric.commons.controller.BaseWebService;

import jakarta.servlet.http.HttpServletRequest;

@Component("p8auditlibWebServiceServletRequestAuditService")
@DependsOn("appJsonConfigurationService")
@Lazy
public class WebServiceServletRequestAuditService extends EcmAuditService {

	private static final String AUDIT_LOG_ENTRY_KEY = "p8.audit.LogEntry";

	public WebServiceRequestAuditEntry createWebServiceRequestAuditEntry(String appEvent,
			Map<String, Object> requestData, HttpServletRequest request) {
		WebServiceRequestAuditEntry result = new WebServiceRequestAuditEntry(request);
		result.setEcmEventDetailsMap(requestData);
		result.setAppEvent(appEvent);
		result.setAppUser(BaseWebService.getUserName());
		request.setAttribute(AUDIT_LOG_ENTRY_KEY, result);
		return result;
	}

	public WebServiceRequestAuditEntry createWebServiceRequestAuditEntry(String appEvent, HttpServletRequest request,
			RequestParameter... parameters) {
		HashMap<String, Object> requestData = new HashMap<>();
		if (parameters.length > 0)
			for (RequestParameter parameter : parameters)
				requestData.put(parameter.getName(), parameter.getValue());
		return createWebServiceRequestAuditEntry(appEvent, requestData, request);
	}

	public WebServiceRequestAuditEntry getWebServiceRequestAuditEntry(HttpServletRequest request) {
		return (WebServiceRequestAuditEntry) request.getAttribute(AUDIT_LOG_ENTRY_KEY);

	}
}
