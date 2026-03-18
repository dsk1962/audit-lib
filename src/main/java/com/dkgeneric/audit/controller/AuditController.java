package com.dkgeneric.audit.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dkgeneric.audit.service.EcmAuditService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuditController {

	@Getter
	@Setter
	@AllArgsConstructor
	private class FilterOption {
		private String value;
		private String name;
		private boolean selected;
	}

	private final List<EcmAuditService> ecmAuditServices;
	private final ObjectMapper objectMapper;

	@GetMapping(path = "/auditLog", produces = MediaType.TEXT_HTML_VALUE)
	@Operation(description = "Get latest entries from audit log table", method = "auditLog")
	public ModelAndView auditLog(ModelMap model,
			@Parameter(description = "Offset", required = false) @RequestParam(defaultValue = "100") int offset,
			@Parameter(description = "Filter Type", required = false) @RequestParam(defaultValue = EcmAuditService.ERROR_FILTER_TYPE) String filterType,
			@Parameter(description = "Filter Value", required = false) @RequestParam(defaultValue = "") String filterValue) {

		EcmAuditService ecmAuditService = ecmAuditServices.stream().filter(EcmAuditService::isAuditTableConfigured)
				.findFirst().orElse(null);
		List<Map<String, Object>> results = Collections.emptyList();
		try {
			results = ecmAuditService != null ? ecmAuditService.getLastAuditRecords(offset, filterType, filterValue)
					: Collections.emptyList();
		} catch (Exception e) {
			model.addAttribute("errorText", e.getMessage());
		}

		results.forEach(entry -> preprocess(ecmAuditService, entry));
		log.info("GET /auditLog.");
		model.addAttribute("logEntries", results);
		model.addAttribute("filterValue", filterValue);
		model.addAttribute("offset", offset);
		model.addAttribute("auditTable", ecmAuditService.getAuditConfiguration().getAuditTableName());
		List<FilterOption> filterOptions = new ArrayList<>(10);
		model.addAttribute("filterOptions", filterOptions);
		filterOptions.add(new FilterOption(EcmAuditService.ERROR_FILTER_TYPE, "Failed Requests",
				filterType.equals(EcmAuditService.ERROR_FILTER_TYPE)));
		filterOptions.add(new FilterOption(EcmAuditService.ALL_FILTER_TYPE, "All requests",
				filterType.equals(EcmAuditService.ALL_FILTER_TYPE)));
		filterOptions.add(new FilterOption(EcmAuditService.REQUEST_DETAILS_FILTER_TYPE, "Request Data contains",
				filterType.equals(EcmAuditService.REQUEST_DETAILS_FILTER_TYPE)));
		filterOptions.add(new FilterOption(EcmAuditService.ERROR_DETAILS_FILTER_TYPE, "Error Data contains",
				filterType.equals(EcmAuditService.ERROR_DETAILS_FILTER_TYPE)));
		filterOptions.add(new FilterOption(EcmAuditService.OBJECTID_FILTER_TYPE, "Object Id equals",
				filterType.equals(EcmAuditService.OBJECTID_FILTER_TYPE)));
		filterOptions.add(new FilterOption(EcmAuditService.ERRORID_FILTER_TYPE, "Error Id equals",
				filterType.equals(EcmAuditService.ERRORID_FILTER_TYPE)));
		filterOptions.add(new FilterOption(EcmAuditService.REQUEST_DATE_FILTER_TYPE, "Request Date",
				filterType.equals(EcmAuditService.REQUEST_DATE_FILTER_TYPE)));

		return new ModelAndView("serviceAuditLog", model);
	}

	private void preprocess(EcmAuditService ecmAuditService, Map<String, Object> entry) {
		if (entry == null)
			return;
		String txt = (String) entry.get(EcmAuditService.ECM_EVENT_DETAILS_COLUMN);
		if (StringUtils.hasText(txt)) {
			try {
				entry.put(EcmAuditService.RESPONSE_TIME_KEY, objectMapper.readTree(txt)
						.path(EcmAuditService.REQUEST_DATA_KEY).path(EcmAuditService.RESPONSE_TIME_KEY).asText());
			} catch (Exception e) {
				// ignore
			}
			if (!ecmAuditService.getAuditConfiguration().getDetailsEventFilter().contains("*")
					&& !ecmAuditService.getAuditConfiguration().getDetailsEventFilter()
							.contains(entry.get(EcmAuditService.APP_EVENT_COLUMN)))
				entry.remove(EcmAuditService.ECM_EVENT_DETAILS_COLUMN);
		}
	}
}
