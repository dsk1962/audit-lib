package com.dkgeneric.audit.common;

/**
 * The Class AuditConstants.
 */
public class AuditConstants {

	// Document Events
	/** The Constant DOCUMENT_UPDATE_EVENT. */
	public static final String DOCUMENT_UPDATE_EVENT = "Document Update";

	/** The Constant DOCUMENT_CREATE_EVENT. */
	public static final String DOCUMENT_CREATE_EVENT = "Create Document";

	/** The Constant DOCUMENT_VERSION_CREATE_EVENT. */
	public static final String DOCUMENT_VERSION_CREATE_EVENT = "Create Document Version";

	/** The Constant DOCUMENT_DOWNLOAD_EVENT. */
	public static final String DOCUMENT_DOWNLOAD_EVENT = "Download Document";

	/** The Constant DOCUMENT_COPY_EVENT. */
	public static final String DOCUMENT_COPY_EVENT = "Copy Document";

	/** The Constant DOCUMENT_BATCH_UPDATE_EVENT. */
	public static final String DOCUMENT_BATCH_UPDATE_EVENT = "Batch Update";

	/** The Constant DOCUMENT_PERMANENT_DELETE_EVENT. */
	public static final String DOCUMENT_PERMANENT_DELETE_EVENT = "Permanent Delete";

	/** The Constant DOCUMENT_PERMANENT_DELETE_EVENT. */
	public static final String DOCUMENT_SOFT_DELETE_EVENT = "Soft Delete";

	/** The Constant DOCUMENT_BULK_DOWNLOAD_EVENT. */
	public static final String DOCUMENT_BULK_DOWNLOAD_EVENT = "Bulk Download";

	/** The Constant DOCUMENT_BULK_DOWNLOAD_EVENT. */
	public static final String SERACH_DOCUMENTS_EVENT = "Search Documents";

	// statuses
	/** The Constant STATUS_COMPLETED. */
	public static final String STATUS_COMPLETED = "Completed";
	
	/** The Constant STATUS_FAILED. */
	public static final String STATUS_FAILED = "Failed";
	
	/** The Constant STATUS_PARTIALLY. */
	public static final String STATUS_PARTIALLY = "Partially";
	
	// other events
	public static final String SEND_KAFKA_MESSAGE_EVENT = "Send Kafka Message";
	
	
	
	// audit text

	/** The Constant AFTER_TXT. */
	public static final String AFTER_TXT = "After: ";

	/** The Constant BEFORE_TXT. */
	public static final String BEFORE_TXT = "Before: ";

	/** The Constant CRLF_TXT. */
	public static final String CRLF_TXT = "\r\n";

	/** The Constant DOCUMENT_CLASS_TXT. */
	public static final String DOCUMENT_CLASS_TXT = "Document class: ";

	/** The Constant NO_UPDATED_PROPERTIES_TXT. */
	public static final String NO_UPDATED_PROPERTIES_TXT = "No modified properties";

	/** The Constant PROPERTIES_TXT. */
	public static final String PROPERTIES_TXT = "Properties: ";

	/** The Constant PROPERTY_TXT. */
	public static final String PROPERTY_TXT = "Property: ";

	/** The Constant RESOURCE_FILE_TXT. */
	public static final String RESOURCE_FILE_TXT = "Resource file: ";

	/** The Constant RESOURCE_SIZE_TXT. */
	public static final String RESOURCE_SIZE_TXT = "Resource size: ";

	/** The Constant RESOURCE_TYPE_EVENT. */
	public static final String RESOURCE_TYPE_EVENT = "Resource type: ";

	/** The Constant VALUE_TXT. */
	public static final String VALUE_TXT = "Value: ";

	/**
	 * Hides constructor
	 */
	private AuditConstants() {
	}

}
