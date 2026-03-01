package com.dkgeneric.audit.model;

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
public class RequestParameter {

	public static final String BODY_PARAMETER_NAME = "ECM_AUDIT_REQUEST_BODY";

	private String name;
	private Object value;
}
