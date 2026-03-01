package com.dkgeneric.audit.config;

import org.springframework.stereotype.Component;

import com.dkgeneric.commons.config.CommonsLibGitInformation;

/**
 * Provides access to git commit used to create this library. Maven
 * git-commit-id-plugin is used to populate git data.
 */
@Component("p8auditlibGitInformation")
public class AuditLibGitInformation extends CommonsLibGitInformation {
	@Override
	public String getProjectName() {
		return "p8-audit-lib";
	}
}
