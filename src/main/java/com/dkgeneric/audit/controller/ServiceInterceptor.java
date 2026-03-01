package com.dkgeneric.audit.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.dkgeneric.audit.common.AuditUtils;
import com.dkgeneric.audit.model.RequestStatus;
import com.dkgeneric.audit.model.WebServiceRequestAuditEntry;
import com.dkgeneric.audit.service.WebServiceRequestAuditService;
import com.dkgeneric.commons.controller.CommonsControllerExceptionHandler;
import com.dkgeneric.commons.model.ErrorLogEntry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ServiceInterceptor implements HandlerInterceptor {

	protected final WebServiceRequestAuditService auditService;
	protected final CommonsControllerExceptionHandler p8CommonsControllerExceptionHandler;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) throws Exception {
		WebServiceRequestAuditEntry auditEntry = auditService.getWebServiceRequestAuditEntry();
		if (auditEntry != null) {
			ErrorLogEntry errorLogEntry = p8CommonsControllerExceptionHandler.getErrorLogEntry();
			AuditUtils.processErrors(auditEntry, errorLogEntry);
			if (auditEntry.getRequestStatus() == null
					|| RequestStatus.COMPLETED.equals(auditEntry.getRequestStatus())) {
				if (response.getStatus() >= 200 && response.getStatus() < 300 && errorLogEntry == null)
					auditEntry.setRequestStatus(RequestStatus.COMPLETED);
				else
					auditEntry.setRequestStatus(RequestStatus.FAILED);
			}
			auditEntry.getWebServiceRequestInfo().setResponseStatus(response.getStatus());
			if (auditEntry.isSaveInInterceptor())
				auditService.insertAuditRecord(auditEntry);
		}
	}

}
