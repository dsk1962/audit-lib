package com.dkgeneric.jpa.taxonomy.audit.model;

import java.util.Date;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * The Class P8OperationAuditLog. JPA model fro P8 operations audit
 */
@Entity
@Table(name = "ECMRMT_AUDIT_LOG")
public class P8OperationAuditLog {

	/** The id. */
	private long id;

	/** The document id. */
	private String documentId;

	/** The app id. */
	private String appId;

	/** The app event. */
	private String appEvent;

	/** The ecm event. */
	private String ecmEvent;

	/** The ecm event details. */
	private String ecmEventDetails;

	/** The app user. */
	private String appUser;

	/** The external id. */
	private String externalId;

	/** The event time. */
	private Date eventTime;

	/**
	 * Instantiates a new log entry
	 */
	public P8OperationAuditLog() {
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
	public P8OperationAuditLog(String documentId, String ecmEvent, String ecmEventDetails, Date eventTime,
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
	public P8OperationAuditLog(String documentId, String ecmEvent, String ecmEventDetails,
			Map<String, Object> auditData) {
		this(documentId, ecmEvent, ecmEventDetails, new Date(), auditData);
	}

	/**
	 * Gets the app event.<br>
	 * JPA: @Column(name = "APP_EVENT")
	 * @return the app event
	 */
	@Column(name = "APP_EVENT")
	public String getAppEvent() {
		return appEvent;
	}

	/**
	 * Gets the app id.<br>
	 * JPA: @Column(name = "APP_ID")
	 * @return the app id
	 */
	@Column(name = "APP_ID")
	public String getAppId() {
		return appId;
	}

	/**
	 * Gets the app user.<br>
	 * JPA: @Column(name = "APP_USER")
	 * @return the app user
	 */
	@Column(name = "APP_USER")
	public String getAppUser() {
		return appUser;
	}

	/**
	 * Gets the document id.<br>
	 * JPA: @Column(name = "OBJECT_ID")
	 * @return the document id
	 */
	@Column(name = "OBJECT_ID")
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * Gets the ecm event.<br>
	 * JPA: @Column(name = "ECM_EVENT")
	 * @return the ecm event
	 */
	@Column(name = "ECM_EVENT")
	public String getEcmEvent() {
		return ecmEvent;
	}

	/**
	 * Gets the ecm event details.<br>
	 * JPA: @Column(name = "ECM_EVENT_DETAILS")
	 * @return the ecm event details
	 */
	@Lob
	@Column(name = "ECM_EVENT_DETAILS")
	public String getEcmEventDetails() {
		return ecmEventDetails;
	}

	/**
	 * Gets the event time.<br>
	 * JPA: @Column(name = "EVENT_TIME")
	 * @return the event time
	 */
	@Column(name = "EVENT_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getEventTime() {
		return eventTime;
	}

	/**
	 * Gets the external id.<br>
	 * JPA: @Column(name = "EXTERNAL_ID")
	 * @return the external id
	 */
	@Column(name = "EXTERNAL_ID")
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Gets the id.<br>
	 * JPA: @Column(name = "ID")<br>    @Id<br>    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ecmrmt_audit_log_id_seq")<br>    @SequenceGenerator(name = "ecmrmt_audit_log_id_seq", sequenceName = "ECMRMT_AUDIT_LOG_SEQ", allocationSize = 1)
	 * @return the id
	 */
	@Column(name = "ID")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ecmrmt_audit_log_id_seq")
	@SequenceGenerator(name = "ecmrmt_audit_log_id_seq", sequenceName = "ECMRMT_AUDIT_LOG_SEQ", allocationSize = 1)
	public long getId() {
		return id;
	}

	/**
	 * Sets the app event.
	 *
	 * @param appEvent the new app event
	 */
	public void setAppEvent(String appEvent) {
		this.appEvent = appEvent;
	}

	/**
	 * Sets the app id.
	 *
	 * @param appId the new app id
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * Sets the app user.
	 *
	 * @param appUser the new app user
	 */
	public void setAppUser(String appUser) {
		this.appUser = appUser;
	}

	/**
	 * Sets the document id.
	 *
	 * @param documentId the new document id
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * Sets the ecm event.
	 *
	 * @param ecmEvent the new ecm event
	 */
	public void setEcmEvent(String ecmEvent) {
		this.ecmEvent = ecmEvent;
	}

	/**
	 * Sets the ecm event details.
	 *
	 * @param ecmEventDetails the new ecm event details
	 */
	public void setEcmEventDetails(String ecmEventDetails) {
		this.ecmEventDetails = ecmEventDetails;
	}

	/**
	 * Sets the event time.
	 *
	 * @param eventTime the new event time
	 */
	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	/**
	 * Sets the external id.
	 *
	 * @param exyternalId the new external id
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}
}
