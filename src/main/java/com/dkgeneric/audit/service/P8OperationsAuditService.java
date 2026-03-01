package com.dkgeneric.audit.service;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.dkgeneric.jpa.taxonomy.audit.model.P8OperationAuditLog;
import com.dkgeneric.jpa.taxonomy.audit.repository.P8AuditRepository;

import lombok.RequiredArgsConstructor;

/**
 * The Class P8OperationsAuditService. Should be used for p8 operations logging
 */
@Component("p8auditlibP8OperationsAuditService")
@DependsOn("AppJsonConfigurationService")
@RequiredArgsConstructor
public class P8OperationsAuditService {

	/** The p8 audit repository. */
	private final P8AuditRepository p8AuditRepository;

	/**
	 * Adds the P8 operation audit record.
	 *
	 * @param logEntry the log entry
	 */
	public void addP8OperationAuditRecord(P8OperationAuditLog logEntry) {
		p8AuditRepository.saveAndFlush(logEntry);
	}

}
