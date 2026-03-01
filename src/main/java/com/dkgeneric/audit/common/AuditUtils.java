package com.dkgeneric.audit.common;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.util.StringUtils;

import com.dkgeneric.commons.model.ErrorLogEntry;
import com.dkgeneric.audit.model.EcmAuditEntry;

public class AuditUtils {

	public static void processErrors(EcmAuditEntry auditEntry, ErrorLogEntry errorLogEntry) {
		if (errorLogEntry != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("Error Id : ").append(errorLogEntry.getErrorId());
			sb.append("\r\nError Message : ").append(errorLogEntry.getErrorMessage());
			if (StringUtils.hasText(errorLogEntry.getErrorCode()))
				sb.append("\r\nError Code : ").append(errorLogEntry.getErrorCode());
			if (errorLogEntry.getException() != null) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				errorLogEntry.getException().printStackTrace(pw);
				sb.append("\r\nStack trace: ").append(sw.toString());
			}
			auditEntry.addErrorDetails(sb.toString());
			auditEntry.setErrorId(errorLogEntry.getErrorId());
		}
	}

	private AuditUtils() {
	}
}
