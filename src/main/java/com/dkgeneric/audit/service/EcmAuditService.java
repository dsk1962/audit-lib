package com.dkgeneric.audit.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.ConfigurationException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.dkgeneric.commons.common.CommonsUtil;
import com.dkgeneric.commons.config.LibConfig;
import com.dkgeneric.commons.service.AppJsonConfigurationService;
import com.dkgeneric.audit.model.EcmAuditConfiguration;
import com.dkgeneric.audit.model.EcmAuditEntry;
import com.dkgeneric.audit.model.EcmEventType;
import com.dkgeneric.audit.model.RequestStatus;
import com.dkgeneric.audit.model.WebServiceRequestAuditEntry;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component("p8auditlibEcmAuditService")
@DependsOn("AppJsonConfigurationService")
@Lazy
@Slf4j
public class EcmAuditService {

	public static final String AUDIT_CONFIGURATION_ENTRY = "auditCfg";
	private static final String JSON_KEY = "ecmAuditConfiguration";
	public static final String APP_EVENT_COLUMN = "APP_EVENT";
	public static final String APP_USER_COLUMN = "APP_USER";
	public static final String APP_SVC_ACCOUNT_COLUMN = "APP_SVC_ACCOUNT";
	public static final String OBJECT_ID_COLUMN = "OBJECT_ID";
	public static final String ECM_EVENT_DETAILS_COLUMN = "ECM_EVENT_DETAILS";
	public static final String EVENT_TIME_COLUMN = "EVENT_TIME";
	public static final String ECM_EVENT_COLUMN = "ECM_EVENT";
	public static final String REQUEST_STATUS_COLUMN = "REQUEST_STATUS";
	public static final String ERROR_ID_COLUMN = "ERROR_ID";
	public static final String ERROR_DETAILS_COLUMN = "ERROR_DETAILS";
	public static final String RESPONSE_TIME_KEY = "responseTime";
	public static final String REQUEST_INFO_KEY = "requestInfo";
	public static final String REQUEST_DATA_KEY = "requestData";

	public static final String ERROR_FILTER_TYPE = "error";
	public static final String ALL_FILTER_TYPE = "all";
	public static final String REQUEST_DETAILS_FILTER_TYPE = "requestDetails";
	public static final String ERROR_DETAILS_FILTER_TYPE = "errorDetails";
	public static final String OBJECTID_FILTER_TYPE = "objectId";
	public static final String ERRORID_FILTER_TYPE = "errorId";
	public static final String REQUEST_DATE_FILTER_TYPE = "requestDate";

	@Autowired
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private AppJsonConfigurationService jsonConfigurationService;
	@Autowired
	private LibConfig commonsLibConfig;

	@Autowired
	@Qualifier("ecmAuditDataSource")
	private DataSource ecmDatacapDataSource;
	@Autowired
	@Qualifier("ecmAuditJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Autowired
	@Getter
	protected EcmAuditConfiguration auditConfiguration;
	@Autowired
	protected ObjectMapper mapper;
	private SimpleJdbcInsert simpleJdbcInsert;

	private static final String SELECT_LATEST_AUDIT_RECORDS = "SELECT * FROM {} WHERE ID > (SELECT MAX(t.ID) - {} FROM {} t) {}";
	private static final String AND_JOIN = " AND ";

	public void insertAuditRecord(EcmAuditEntry entry) throws JsonProcessingException {
		Map<String, Object> namedParameterMap = new HashMap<>();
		namedParameterMap.put(APP_EVENT_COLUMN, entry.getAppEvent());
		if (entry.getEcmEvent() == null)
			namedParameterMap.put(ECM_EVENT_COLUMN, EcmEventType.NOTSET);
		else
			namedParameterMap.put(ECM_EVENT_COLUMN, entry.getEcmEvent().getLabel());
		long responseTime = -1;
		if (entry.getEventTime() != null) {
			responseTime = System.currentTimeMillis() - entry.getEventTime().getTime();
			entry.addEventDetail(RESPONSE_TIME_KEY, responseTime);
		}
		if (entry instanceof WebServiceRequestAuditEntry webServiceRequestAuditEntry) {
			HashMap<String, Object> data = new HashMap<>();
			data.put(REQUEST_INFO_KEY, webServiceRequestAuditEntry.getWebServiceRequestInfo());
			data.put(REQUEST_DATA_KEY, entry.getEcmEventDetailsMap());
			namedParameterMap.put(ECM_EVENT_DETAILS_COLUMN, mapper.writeValueAsString(data));
		} else
			namedParameterMap.put(ECM_EVENT_DETAILS_COLUMN, mapper.writeValueAsString(entry.getEcmEventDetailsMap()));
		namedParameterMap.put(APP_USER_COLUMN, entry.getAppUser());
		namedParameterMap.put(APP_SVC_ACCOUNT_COLUMN, entry.getAppServiceAccount());
		namedParameterMap.put(EVENT_TIME_COLUMN, entry.getEventTime());
		namedParameterMap.put(OBJECT_ID_COLUMN, entry.getObjectId());
		if (entry.getErrorId() != null)
			namedParameterMap.put(ERROR_ID_COLUMN, entry.getErrorId());
		if (entry.getRequestStatus() == null)
			namedParameterMap.put(REQUEST_STATUS_COLUMN, RequestStatus.NOTSET.getLabel());
		else
			namedParameterMap.put(REQUEST_STATUS_COLUMN, entry.getRequestStatus().getLabel());
		namedParameterMap.put(ERROR_DETAILS_COLUMN, entry.getErrorDetails());
		entry.setDbRecordId(simpleJdbcInsert.executeAndReturnKey(namedParameterMap).intValue());
		log.info("Audit record ID: {}. response time: {}", entry.getDbRecordId(), responseTime);
	}

	public boolean isAuditTableConfigured() {
		return StringUtils.hasText(auditConfiguration.getAuditTableName());
	}

	public List<Map<String, Object>> getLastAuditRecords(int offset, String filterType, String filterValue) {
		if (offset <= 0)
			offset = 100;
		if (!StringUtils.hasText(auditConfiguration.getAuditTableName()))
			return Collections.emptyList();

		String filter = "";
		if (ERROR_FILTER_TYPE.equals(filterType))
			filter = " AND Lower(" + REQUEST_STATUS_COLUMN + ") <> 'completed'";
		else if (StringUtils.hasText(filterValue)) {
			switch (filterType) {
			case REQUEST_DETAILS_FILTER_TYPE:
				filter = AND_JOIN + ECM_EVENT_DETAILS_COLUMN + " like '%" + filterValue + "%'";
				break;
			case ERROR_DETAILS_FILTER_TYPE:
				filter = AND_JOIN + ERROR_DETAILS_COLUMN + " like '%" + filterValue + "%'";
				break;
			case OBJECTID_FILTER_TYPE:
				filter = AND_JOIN + OBJECT_ID_COLUMN + " = '" + filterValue + "'";
				break;
			case ERRORID_FILTER_TYPE:
				filter = AND_JOIN + ERROR_ID_COLUMN + " = '" + filterValue + "'";
				break;
			case REQUEST_DATE_FILTER_TYPE:
				String[] dates = filterValue.split(",");
				filter = AND_JOIN + EVENT_TIME_COLUMN + " >= " + getToOracleDate(dates[0]);
				if (dates.length > 1)
					filter += AND_JOIN + EVENT_TIME_COLUMN + " < " + getToOracleDate(dates[1]);
				break;
			default:
				break;
			}
		}
		return jdbcTemplate.queryForList(CommonsUtil.format(SELECT_LATEST_AUDIT_RECORDS,
				auditConfiguration.getAuditTableName(), offset, auditConfiguration.getAuditTableName(), filter));
	}

	private String getToOracleDate(String date) {
		String format = date.length() > 10 ? "'YYYY-MM-DD HH24:MI:SS'" : "'YYYY-MM-DD'";
		return " TO_DATE('" + date + "'," + format + ")";
	}

	@PostConstruct
	public void postConstruct() throws ConfigurationException, JsonProcessingException {
		String applicationId = commonsLibConfig.getApplicationId();
		JsonNode cfg = jsonConfigurationService.getAppConfiguration();
		if (cfg == null)
			throw new ConfigurationException("No configurations for appliaction id: " + applicationId + " found.");
		if (!cfg.has(AUDIT_CONFIGURATION_ENTRY))
			throw new ConfigurationException("No configuration with name " + AUDIT_CONFIGURATION_ENTRY
					+ " found in configuration for appliaction id: " + applicationId + " .");
		cfg = cfg.get(AUDIT_CONFIGURATION_ENTRY);
		if (cfg.has(JSON_KEY))
			auditConfiguration.copyNonNullValues(mapper.convertValue(cfg.get(JSON_KEY), EcmAuditConfiguration.class));
		if (StringUtils.hasText(auditConfiguration.getAuditTableName()))
			simpleJdbcInsert = new SimpleJdbcInsert(ecmDatacapDataSource)
					.withTableName(auditConfiguration.getAuditTableName()).usingGeneratedKeyColumns("ID");
	}
}
