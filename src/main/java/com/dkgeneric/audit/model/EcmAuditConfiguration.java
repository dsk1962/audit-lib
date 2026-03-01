package com.dkgeneric.audit.model;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
@Component
public class EcmAuditConfiguration {

	@Value("${com.davita.audit.audittablename:#{\"\"}}")
	private String auditTableName;
	private List<String> detailsEventFilter = Collections.emptyList();

	public void copyNonNullValues(EcmAuditConfiguration src) {
		if (StringUtils.hasText(src.auditTableName))
			auditTableName = src.auditTableName;
		if (!CollectionUtils.isEmpty(src.detailsEventFilter))
			detailsEventFilter = src.detailsEventFilter;
	}
}
