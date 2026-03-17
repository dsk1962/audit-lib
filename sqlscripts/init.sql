CREATE TABLE filenet_audit_log
(
   id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 20 ),
   object_id varchar(64),
   app_id varchar(32),
   app_event varchar(64),
   ecm_event varchar(64),
   ecm_event_details text,
   app_user varchar(256),
   event_time timestamp NOT NULL,
   external_id varchar(255),
   CONSTRAINT filenet_audit_log_pk PRIMARY KEY (id)
)

CREATE TABLE "commons_audit"."test_audit"
(
   id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   app_event varchar(256),
   filenet_event varchar(64),
   request_details text,
   app_user varchar(256),
   app_svc_account varchar(256),
   event_time timestamp NOT NULL,
   object_id varchar(64),
   request_status varchar(16),
   error_id varchar(64),
   error_details text
);
