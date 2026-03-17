package com.dkgeneric.audit;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.dkgeneric.audit.common.FilenetContentLibConverter;
import com.dkgeneric.audit.model.EcmAuditEntry;
import com.dkgeneric.audit.model.EcmEventType;
import com.dkgeneric.audit.model.RequestStatus;
import com.dkgeneric.audit.service.EcmAuditService;
import com.dkgeneric.audit.service.FilenetOperationsAuditService;
import com.dkgeneric.commons.config.GitInformations;
import com.dkgeneric.filenet.content.common.ServiceException;
import com.dkgeneric.filenet.content.model.P8ContentObjectAuditInfo;
import com.dkgeneric.filenet.content.model.P8ObjectAuditInfo;
import com.dkgeneric.filenet.content.model.PropertyAuditInfo;
import com.dkgeneric.filenet.content.resources.P8ContentResource;
import com.dkgeneric.filenet.content.response.CreateDocumentResponse;
import com.dkgeneric.filenet.content.response.GetContentResponse;
import com.dkgeneric.filenet.content.response.UpdateDocumentMetadataResponse;
import com.dkgeneric.jpa.audit.repository.model.FilenetOperationAuditLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import ch.qos.logback.classic.Level;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@EnableEncryptableProperties
@Slf4j
class AuditLibApplicationTests {
	static final Level LIB_LOG_LEVEL = Level.DEBUG;
	@Autowired
	private FilenetOperationsAuditService auditService;
	@Autowired
	@Qualifier("auditlibAuditService")
	private EcmAuditService baseAuditService;
	@Autowired
	private FilenetContentLibConverter contentLibConverter;
	@Autowired
	private GitInformations gitInformations;

	@Test
	@Order(5)
	void baseAuditTest() throws JsonProcessingException {
		log.info("Base audit started");
		Map<String, Object> eventDetails = new HashMap<>();
		eventDetails.put("requestparameter1", "param1TestValue");
		eventDetails.put("jsonparameter1", new FilenetOperationAuditLog("sample id", "sample event", "data", null));
		EcmAuditEntry auditEntry = new EcmAuditEntry("test_app_event", EcmEventType.NOTSET, eventDetails, "test_user",
				"test_service_account", new Date(), "{test_object_id}", RequestStatus.COMPLETED, "",
				UUID.randomUUID().toString(), 0);
		baseAuditService.insertAuditRecord(auditEntry);
		log.info("Base audit created a record with id {}", auditEntry.getDbRecordId());
		assertTrue(auditEntry.getDbRecordId() > 0, "Create base audit record failed. record id <= 0.");
	}

	P8ContentResource createContentResource() {
		P8ContentResource result = new P8ContentResource();
		result.setContentType("application/pdf");
		result.setFileName("Test Document.pdf");
		result.setSize(12345);
		return result;
	}

	@BeforeAll
	void initialize() throws ServiceException {
		((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.davita.ecm.p8.content")).setLevel(LIB_LOG_LEVEL);
		log.info("Test started.");
		log.info("Initialization code started.");
		log.info(gitInformations.printAllGitInfo());
	}

	@Test
	@Order(1)
	void p8CreateDocumenAudit() {
		log.info("Create document audit started");
		CreateDocumentResponse response = new CreateDocumentResponse();
		response.setP8DocumentId("{80B5AA6F-0000-CE16-8583-101C09C2ECAE}");
		P8ContentObjectAuditInfo auditInfo = new P8ContentObjectAuditInfo();
		response.setAuditInfo(auditInfo);
		auditInfo.setObjectClass("Document");
		auditInfo.setObjectId("{80B5AA6F-0000-CE16-8583-101C09C2ECAE}");
		auditInfo.addPropertyAuditInfo(new PropertyAuditInfo("DocumentTitle", null, "Document Title"));
		auditInfo.addPropertyAuditInfo(
				new PropertyAuditInfo("DateLastModified", null, new Date(new Date().getTime() - 20 * 60 * 1000)));
		auditInfo.setContentResource(createContentResource());
		FilenetOperationAuditLog entry = contentLibConverter.convert(response);
		auditService.addP8OperationAuditRecord(setAppData(entry));
		assertTrue(entry.getId() > 0, "Create audit record failed. record id <= 0.");
	}

	@Test
	@Order(4)
	void p8GetContentAudit() {
		log.info("Get content audit started");
		GetContentResponse response = new GetContentResponse();
		P8ContentObjectAuditInfo auditInfo = new P8ContentObjectAuditInfo();
		response.setAuditInfo(auditInfo);
		auditInfo.setObjectClass("Document");
		auditInfo.setObjectId("{80B5AA6F-0000-CE16-8583-101C09C2ECAE}");
		auditInfo.setContentResource(createContentResource());
		FilenetOperationAuditLog entry = contentLibConverter.convert(response);
		auditService.addP8OperationAuditRecord(setAppData(entry));
		assertTrue(entry.getId() > 0, "Create audit record failed. record id <= 0.");
	}

	@Test
	@Order(2)
	void p8UpdateDocumentAudit() {
		log.info("Update document audit started");
		UpdateDocumentMetadataResponse response = new UpdateDocumentMetadataResponse();
		P8ObjectAuditInfo auditInfo = new P8ObjectAuditInfo();
		List<P8ObjectAuditInfo> auditInfos = new ArrayList<>();
		auditInfos.add(auditInfo);
		response.setAuditInfos(auditInfos);
		auditInfo.setObjectClass("Document");
		auditInfo.setObjectId("{80B5AA6F-0000-CE16-8583-101C09C2ECAE}");
		auditInfo.addPropertyAuditInfo(new PropertyAuditInfo("DocumentTitle", "Document Title", "New Document Title"));
		auditInfo.addPropertyAuditInfo(
				new PropertyAuditInfo("DateLastModified", new Date(new Date().getTime() - 20 * 60 * 1000), new Date()));
		List<FilenetOperationAuditLog> entry = contentLibConverter.convert(response);
		auditService.addP8OperationAuditRecord(setAppData(entry.get(0)));
		assertTrue(entry.get(0).getId() > 0, "Create audit record failed. record id <= 0.");
	}

	@Test
	@Order(3)
	void p8UpdateDocumentAuditNoPropertyChanged() {
		log.info("Update document audit started");
		UpdateDocumentMetadataResponse response = new UpdateDocumentMetadataResponse();
		P8ObjectAuditInfo auditInfo = new P8ObjectAuditInfo();
		List<P8ObjectAuditInfo> auditInfos = new ArrayList<>();
		auditInfos.add(auditInfo);
		response.setAuditInfos(auditInfos);
		auditInfo.setObjectClass("Document");
		auditInfo.setObjectId("{80B5AA6F-0000-CE16-8583-101C09C2ECAE}");
		List<FilenetOperationAuditLog> entry = contentLibConverter.convert(response);
		auditService.addP8OperationAuditRecord(setAppData(entry.get(0)));
		assertTrue(entry.get(0).getId() > 0, "Create audit record failed. record id <= 0.");
	}

	private FilenetOperationAuditLog setAppData(FilenetOperationAuditLog auditInfo) {
		auditInfo.setAppEvent("Audit Lib unit test");
		auditInfo.setAppId("Audit Lib Project");
		auditInfo.setAppUser("Unit Test");
		return auditInfo;
	}
}
