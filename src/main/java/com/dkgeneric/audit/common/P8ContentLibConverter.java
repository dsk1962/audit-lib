package com.dkgeneric.audit.common;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.dkgeneric.commons.config.LibConfig;
import com.dkgeneric.filenet.content.model.P8ContentObjectAuditInfo;
import com.dkgeneric.filenet.content.model.P8ObjectAuditInfo;
import com.dkgeneric.filenet.content.model.PropertyAuditInfo;
import com.dkgeneric.filenet.content.response.CreateDocumentResponse;
import com.dkgeneric.filenet.content.response.CreateDocumentVersionResponse;
import com.dkgeneric.filenet.content.response.GetContentResponse;
import com.dkgeneric.filenet.content.response.UpdateDocumentMetadataResponse;
import com.dkgeneric.jpa.taxonomy.audit.model.P8OperationAuditLog;

import lombok.NoArgsConstructor;

/**
 * The Class P8ContentLibConverter. Use this class to convert p8-content-lib specific audit information to auditable data
 */
@Component("P8ContentLibConverter")
@NoArgsConstructor
@Lazy
public class P8ContentLibConverter {

	/** The date format. */
	private DateTimeFormatter dateFormat = DateTimeFormatter.ISO_INSTANT;

	/**
	 * Appends document resource information to existing string
	 *
	 * @param sb the string builder to whic resource information will be added
	 * @param contentResource the content resource
	 */
	private void appendResourceInfo(StringBuilder sb,
			com.dkgeneric.filenet.content.resources.P8ContentResource contentResource) {
		if (sb != null && contentResource != null) {
			sb.append(AuditConstants.RESOURCE_FILE_TXT).append(contentResource.getFileName())
					.append(AuditConstants.CRLF_TXT).append(AuditConstants.RESOURCE_SIZE_TXT)
					.append(contentResource.getSize()).append(AuditConstants.CRLF_TXT)
					.append(AuditConstants.RESOURCE_TYPE_EVENT).append(contentResource.getContentType());
		}
	}

	/**
	 * Converts results of document create operation.
	 *
	 * @param response the create document response
	 * @return the p8 operation audit log instance
	 */
	public P8OperationAuditLog convert(CreateDocumentResponse response) {
		if (response.getAuditInfo() == null)
			return null;
		P8ObjectAuditInfo auditInfo = response.getAuditInfo();
		P8OperationAuditLog result = new P8OperationAuditLog();
		result.setDocumentId(auditInfo.getObjectId());
		if (response instanceof CreateDocumentVersionResponse)
			result.setEcmEvent(AuditConstants.DOCUMENT_VERSION_CREATE_EVENT);
		else
			result.setEcmEvent(AuditConstants.DOCUMENT_CREATE_EVENT);
		StringBuilder sb = getEventDetailsBuilder(auditInfo);
		if (!CollectionUtils.isEmpty(auditInfo.getModifiedProperties())) {
			sb.append(AuditConstants.PROPERTIES_TXT).append(AuditConstants.CRLF_TXT);
			if (response instanceof CreateDocumentVersionResponse)
				addModifiedProperties(auditInfo, sb);
			else
				for (PropertyAuditInfo propertyAuditInfo : auditInfo.getModifiedProperties())
					sb.append(AuditConstants.PROPERTY_TXT).append(propertyAuditInfo.getPropertyName())
							.append(AuditConstants.CRLF_TXT).append(AuditConstants.VALUE_TXT)
							.append(getStringValue(propertyAuditInfo.getNewValue())).append(AuditConstants.CRLF_TXT);
		} else
			sb.append(AuditConstants.NO_UPDATED_PROPERTIES_TXT).append(AuditConstants.CRLF_TXT);
		if (auditInfo instanceof P8ContentObjectAuditInfo p8ContentObjectAuditInfo)
			appendResourceInfo(sb, p8ContentObjectAuditInfo.getContentResource());
		result.setEcmEventDetails(sb.toString());
		result.setEventTime(auditInfo.getEventTime());
		return result;
	}

	/**
	 * Converts results of get document content operation.
	 *
	 * @param response the get document content response
	 * @return the p8 operation audit log instance
	 */
	public P8OperationAuditLog convert(GetContentResponse response) {
		if (response.getAuditInfo() != null) {
			P8ObjectAuditInfo auditInfo = response.getAuditInfo();
			P8OperationAuditLog result = new P8OperationAuditLog();
			result.setEcmEvent(AuditConstants.DOCUMENT_DOWNLOAD_EVENT);
			result.setDocumentId(auditInfo.getObjectId());
			StringBuilder sb = getEventDetailsBuilder(auditInfo);
			if (auditInfo instanceof P8ContentObjectAuditInfo p8ContentObjectAuditInfo)
				appendResourceInfo(sb, p8ContentObjectAuditInfo.getContentResource());
			result.setEcmEventDetails(sb.toString());
			result.setEventTime(auditInfo.getEventTime());
			return result;
		}
		return null;
	}

	private void addModifiedProperties(P8ObjectAuditInfo auditInfo, StringBuilder sb) {
		for (PropertyAuditInfo propertyAuditInfo : auditInfo.getModifiedProperties())
			sb.append(AuditConstants.PROPERTY_TXT).append(propertyAuditInfo.getPropertyName())
					.append(AuditConstants.CRLF_TXT).append(AuditConstants.BEFORE_TXT)
					.append(getStringValue(propertyAuditInfo.getOldValue())).append(AuditConstants.CRLF_TXT)
					.append(AuditConstants.AFTER_TXT).append(getStringValue(propertyAuditInfo.getNewValue()))
					.append(AuditConstants.CRLF_TXT);

	}

	/**
	 * Converts results of document update operation.
	 *
	 * @param response the document update response
	 * @return the list of p8 operation audit log instances
	 */
	public List<P8OperationAuditLog> convert(UpdateDocumentMetadataResponse response) {
		ArrayList<P8OperationAuditLog> results = new ArrayList<>();
		if (!CollectionUtils.isEmpty(response.getAuditInfos())) {
			for (P8ObjectAuditInfo auditInfo : response.getAuditInfos()) {
				P8OperationAuditLog result = new P8OperationAuditLog();
				result.setDocumentId(auditInfo.getObjectId());
				result.setEcmEvent(AuditConstants.DOCUMENT_UPDATE_EVENT);
				StringBuilder sb = getEventDetailsBuilder(auditInfo);
				if (!CollectionUtils.isEmpty(auditInfo.getModifiedProperties())) {
					sb.append(AuditConstants.PROPERTIES_TXT).append(AuditConstants.CRLF_TXT);
					addModifiedProperties(auditInfo, sb);
				} else
					sb.append(AuditConstants.NO_UPDATED_PROPERTIES_TXT).append(AuditConstants.CRLF_TXT);
				result.setEcmEventDetails(sb.toString());
				result.setEventTime(auditInfo.getEventTime());
				results.add(result);
			}
		}
		return results;
	}

	/**
	 * Populates audit log details string with initial information
	 *
	 * @param auditInfo the new audit info
	 * @return the event details 
	 */
	private StringBuilder getEventDetailsBuilder(P8ObjectAuditInfo auditInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append(AuditConstants.DOCUMENT_CLASS_TXT).append(auditInfo.getObjectClass()).append(AuditConstants.CRLF_TXT);
		return sb;
	}

	/**
	 * Converts object value to string. Null values will be returned as empty string. Date values will be formatted based on date format provided in application 
	 * configuration(dva.ecm.audit.dateformat property). See {@link LibConfig#getAuditDateFormat()}
	 * Default value :"yyyy-MM-dd HH:mm:ss". 
	 *
	 * @param value the value
	 * @return the string value
	 */
	private String getStringValue(Object value) {
		if (value == null)
			return "";
		return value instanceof Date date ? dateFormat.format(date.toInstant().atOffset(ZoneOffset.UTC))
				: value.toString();
	}

}
