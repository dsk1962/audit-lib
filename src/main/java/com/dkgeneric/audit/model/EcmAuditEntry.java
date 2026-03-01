package com.dkgeneric.audit.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EcmAuditEntry {
	private String appEvent;
	private EcmEventType ecmEvent;
	private Map<String, Object> ecmEventDetailsMap = new HashMap<>(10);
	private String appUser;
	private String appServiceAccount;
	private Date eventTime = new Date();
	private String objectId;
	private RequestStatus requestStatus;
	private String errorDetails;
	private String errorId;
	private int dbRecordId;

	public void setObjectId(String id) {
		if (id != null && id.length() > 64)
			id = id.substring(0, 64);
		objectId = id;
	}
	
	public void addErrorDetails(String errorText) {
		if( errorDetails == null )
			errorDetails = errorText;
		else
			errorDetails += "\n\n=======================\n" + errorText;
	}

	public void addEventDetail(String name, Object value) {
		if (ecmEventDetailsMap == null)
			ecmEventDetailsMap = new HashMap<>(10);
		ecmEventDetailsMap.put(name, value);
	}
}
