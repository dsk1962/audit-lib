package com.dkgeneric.audit.service;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.dkgeneric.jpa.audit.repository.FilenetAuditRepository;
import com.dkgeneric.jpa.audit.repository.model.FilenetOperationAuditLog;

import lombok.RequiredArgsConstructor;

/**
 * The Class P8OperationsAuditService. Should be used for p8 operations logging
 */
@Component("p8auditlibP8OperationsAuditService")
@DependsOn("appJsonConfigurationService")
@RequiredArgsConstructor
public class FilenetOperationsAuditService {

	/** The p8 audit repository. */
	private final FilenetAuditRepository p8AuditRepository;

	/**
	 * Adds the P8 operation audit record.
	 *
	 * @param logEntry the log entry
	 */
	public void addP8OperationAuditRecord(FilenetOperationAuditLog logEntry) {
		p8AuditRepository.saveAndFlush(logEntry);
	}

}
