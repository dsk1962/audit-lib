package com.dkgeneric.audit.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.dkgeneric.commons.controller.BaseWebService;
import com.dkgeneric.audit.model.RequestParameter;
import com.dkgeneric.audit.model.WebServiceRequestAuditEntry;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component("p8auditlibWebServiceRequestAuditService")
@DependsOn("AppJsonConfigurationService")
@RequiredArgsConstructor
@Lazy
public class WebServiceRequestAuditService extends EcmAuditService {

	private static final String AUDIT_LOG_ENTRY_KEY = "p8.audit.LogEntry";

	private final HttpServletRequest request;

	public WebServiceRequestAuditEntry createWebServiceRequestAuditEntry(String appEvent,
			Map<String, Object> requestData) {
		WebServiceRequestAuditEntry result = new WebServiceRequestAuditEntry(request);
		result.setEcmEventDetailsMap(requestData);
		result.setAppEvent(appEvent);
		result.setAppUser(BaseWebService.getUserName());
		RequestContextHolder.currentRequestAttributes().setAttribute(AUDIT_LOG_ENTRY_KEY, result,
				RequestAttributes.SCOPE_REQUEST);
		return result;
	}

	public WebServiceRequestAuditEntry createWebServiceRequestAuditEntry(String appEvent,
			RequestParameter... parameters) {
		HashMap<String, Object> requestData = new HashMap<>();
		if (parameters.length > 0)
			for (RequestParameter parameter : parameters)
				requestData.put(parameter.getName(), parameter.getValue());
		return createWebServiceRequestAuditEntry(appEvent, requestData);
	}

	public WebServiceRequestAuditEntry getWebServiceRequestAuditEntry() {
		return (WebServiceRequestAuditEntry) RequestContextHolder.currentRequestAttributes()
				.getAttribute(AUDIT_LOG_ENTRY_KEY, RequestAttributes.SCOPE_REQUEST);

	}
}
