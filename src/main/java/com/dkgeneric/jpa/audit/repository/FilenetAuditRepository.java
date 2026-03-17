package com.dkgeneric.jpa.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dkgeneric.jpa.audit.repository.model.FilenetOperationAuditLog;

/**
 * The Interface P8AuditRepository. To be used in JPA operations
 */
@Repository("auditlibFilenetAuditRepository")
public interface FilenetAuditRepository extends JpaRepository<FilenetOperationAuditLog, Long> {
}
