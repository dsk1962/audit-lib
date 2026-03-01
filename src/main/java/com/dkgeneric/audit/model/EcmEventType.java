package com.dkgeneric.audit.model;

import static com.dkgeneric.audit.common.AuditConstants.*;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EcmEventType {

	CREATE_DOCUMENT(DOCUMENT_CREATE_EVENT), UPDATE_DOCUMENT(DOCUMENT_UPDATE_EVENT), COPY_DOCUMENT(DOCUMENT_COPY_EVENT),
	DOCUMENT_BATCH_UPDATE(DOCUMENT_BATCH_UPDATE_EVENT), DELETE_DOCUMENT(DOCUMENT_PERMANENT_DELETE_EVENT),
	SOFT_DELETE_DOCUMENT(DOCUMENT_SOFT_DELETE_EVENT), DOCUMENT_BULK_DOWNLOAD(DOCUMENT_BULK_DOWNLOAD_EVENT),
	DOCUMENT_DOWNLOAD(DOCUMENT_DOWNLOAD_EVENT), SEARCH_DOCUMENTS(SERACH_DOCUMENTS_EVENT), NOTSET("Not Set");

	private static final Map<String, EcmEventType> BY_LABEL = new HashMap<>();

	static {
		for (EcmEventType e : values()) {
			BY_LABEL.put(e.label, e);
		}
	}

	public static EcmEventType valueOfLabel(String label) {
		return BY_LABEL.get(label);
	}

	public final String label;

	private EcmEventType(String label) {
		this.label = label;
	}

	@JsonValue
	public String getLabel() {
		return label;
	}

}
