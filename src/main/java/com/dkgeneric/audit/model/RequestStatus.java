package com.dkgeneric.audit.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RequestStatus {
	COMPLETED("Completed"), FAILED("Failed"), NOTSET("Not Set"), PART_COMPLETED("Partially");

	private static final Map<String, RequestStatus> BY_LABEL = new HashMap<>();

	static {
		for (RequestStatus e : values()) {
			BY_LABEL.put(e.label, e);
		}
	}

	public static RequestStatus valueOfLabel(String label) {
		return BY_LABEL.get(label);
	}

	public final String label;

	private RequestStatus(String label) {
		this.label = label;
	}

	@JsonValue
	public String getLabel() {
		return label;
	}

}
