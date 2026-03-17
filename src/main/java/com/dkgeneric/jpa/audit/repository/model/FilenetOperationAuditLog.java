package com.dkgeneric.jpa.audit.repository.model;

import java.util.Date;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class P8OperationAuditLog. JPA model fro P8 operations audit
 */
@Entity
@Table(name = "filenet_audit_log")
@Getter
@Setter
public class FilenetOperationAuditLog {

	/** The id. */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	/** The document id. */
	@Column(name = "object_id")
	private String documentId;

	/** The app id. */
	@Column(name = "app_id")
	private String appId;

	/** The app event. */
	@Column(name = "app_event")
	private String appEvent;

	/** The ecm event. */
	@Column(name = "ecm_event")
	private String ecmEvent;

	/** The ecm event details. */
	@Lob
	@Column(name = "ecm_event_details")
	private String ecmEventDetails;

	/** The app user. */
	@Column(name = "app_user")
	private String appUser;

	/** The external id. */
	@Column(name = "external_id")
	private String externalId;

	/** The event time. */
	@Column(name = "event_time")
	private Date eventTime = new Date();

	/**
	 * Instantiates a new log entry
	 */
	public FilenetOperationAuditLog() {
	}

	/**
	 * Instantiates a new log entry
	 *
	 * @param documentId the P8 document id
	 * @param ecmEvent the ecm event
	 * @param ecmEventDetails the ecm event details
	 * @param eventTime the event time
	 * @param auditData the audit data. The following application specific keys will be processed "appId","appEvent","appUser"
	 */
	public FilenetOperationAuditLog(String documentId, String ecmEvent, String ecmEventDetails, Date eventTime,
			Map<String, Object> auditData) {
		this.documentId = documentId;
		this.appId = auditData == null ? null : (String) auditData.get("appId");
		this.appEvent = auditData == null ? null : (String) auditData.get("appEvent");
		this.ecmEvent = ecmEvent;
		this.ecmEventDetails = ecmEventDetails;
		this.appUser = auditData == null ? null : (String) auditData.get("appUser");
		this.eventTime = eventTime;
	}

	/**
	 * Instantiates a new log entry. eventTime will be set to current date
	 *
	 * @param documentId the document id
	 * @param ecmEvent the ecm event
	 * @param ecmEventDetails the ecm event details
	 * @param auditData the audit data. The following application specific keys will be processed "appId","appEvent","appUser"
	 */
	public FilenetOperationAuditLog(String documentId, String ecmEvent, String ecmEventDetails,
			Map<String, Object> auditData) {
		this(documentId, ecmEvent, ecmEventDetails, new Date(), auditData);
	}

}
