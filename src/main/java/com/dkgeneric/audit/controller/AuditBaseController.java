package com.dkgeneric.audit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.dkgeneric.commons.controller.CommonsBaseController;
import com.dkgeneric.audit.service.WebServiceRequestAuditService;

public class AuditBaseController extends CommonsBaseController{

	@Autowired
	@Qualifier("p8auditlibWebServiceRequestAuditService")
	protected WebServiceRequestAuditService auditService;
}
