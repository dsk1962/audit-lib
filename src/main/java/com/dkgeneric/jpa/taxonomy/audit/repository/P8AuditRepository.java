package com.dkgeneric.jpa.taxonomy.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dkgeneric.jpa.taxonomy.audit.model.P8OperationAuditLog;

/**
 * The Interface P8AuditRepository. To be used in JPA operations
 */
@Repository("p8auditlibP8AuditRepository")
public interface P8AuditRepository extends JpaRepository<P8OperationAuditLog, Long> {
}
