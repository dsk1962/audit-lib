package com.dkgeneric.audit.model;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WebServiceRequestAuditEntry extends EcmAuditEntry {

	private WebServiceRequestInfo webServiceRequestInfo;
	private boolean saveInInterceptor = true;

	public WebServiceRequestAuditEntry(HttpServletRequest request) {
		webServiceRequestInfo = new WebServiceRequestInfo(request);
	}
}
