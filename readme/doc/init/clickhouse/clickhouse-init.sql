-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- data

-- lake

CREATE DATABASE ldap_lake_0 ENGINE = Ordinary;
CREATE DATABASE ldap_lake_1 ENGINE = Ordinary;

use ldap_lake_0;

CREATE TABLE `opt_event_0`(`id` UInt64,`data_event_type` String,`data_event_op_type` String,`stamp` UInt64,`create_date` date,`method` String,`uri` String,`real_uri` String,`request_body` String,`request_extra` String,`response_status` UInt16,`response_body` String,`response_extra` String,`request_id` String,`metadata` String,`jwt` String,`member_id` UInt64,`role_ids` Array(UInt64),`credential_type` String,`device_type` String,`login_time` UInt64,`client_ip` String,`user_agent` String,`sec_key` String,`request_un_decryption` UInt8,`response_un_encryption` UInt8,`existence_request_body` UInt8,`existence_response_body` UInt8,`duration_seconds` UInt8,`resource_id` UInt64,`module` String,`relative_uri` String,`absolute_uri` String,`relation_view` String,`authenticate` UInt8,`type` UInt8,`name` String) ENGINE = MergeTree(create_date, (id), 8192);
CREATE TABLE `opt_event_1`(`id` UInt64,`data_event_type` String,`data_event_op_type` String,`stamp` UInt64,`create_date` date,`method` String,`uri` String,`real_uri` String,`request_body` String,`request_extra` String,`response_status` UInt16,`response_body` String,`response_extra` String,`request_id` String,`metadata` String,`jwt` String,`member_id` UInt64,`role_ids` Array(UInt64),`credential_type` String,`device_type` String,`login_time` UInt64,`client_ip` String,`user_agent` String,`sec_key` String,`request_un_decryption` UInt8,`response_un_encryption` UInt8,`existence_request_body` UInt8,`existence_response_body` UInt8,`duration_seconds` UInt8,`resource_id` UInt64,`module` String,`relative_uri` String,`absolute_uri` String,`relation_view` String,`authenticate` UInt8,`type` UInt8,`name` String) ENGINE = MergeTree(create_date, (id), 8192);

use ldap_lake_1;

CREATE TABLE `opt_event_0`(`id` UInt64,`data_event_type` String,`data_event_op_type` String,`stamp` UInt64,`create_date` date,`method` String,`uri` String,`real_uri` String,`request_body` String,`request_extra` String,`response_status` UInt16,`response_body` String,`response_extra` String,`request_id` String,`metadata` String,`jwt` String,`member_id` UInt64,`role_ids` Array(UInt64),`credential_type` String,`device_type` String,`login_time` UInt64,`client_ip` String,`user_agent` String,`sec_key` String,`request_un_decryption` UInt8,`response_un_encryption` UInt8,`existence_request_body` UInt8,`existence_response_body` UInt8,`duration_seconds` UInt8,`resource_id` UInt64,`module` String,`relative_uri` String,`absolute_uri` String,`relation_view` String,`authenticate` UInt8,`type` UInt8,`name` String) ENGINE = MergeTree(create_date, (id), 8192);
CREATE TABLE `opt_event_1`(`id` UInt64,`data_event_type` String,`data_event_op_type` String,`stamp` UInt64,`create_date` date,`method` String,`uri` String,`real_uri` String,`request_body` String,`request_extra` String,`response_status` UInt16,`response_body` String,`response_extra` String,`request_id` String,`metadata` String,`jwt` String,`member_id` UInt64,`role_ids` Array(UInt64),`credential_type` String,`device_type` String,`login_time` UInt64,`client_ip` String,`user_agent` String,`sec_key` String,`request_un_decryption` UInt8,`response_un_encryption` UInt8,`existence_request_body` UInt8,`existence_response_body` UInt8,`duration_seconds` UInt8,`resource_id` UInt64,`module` String,`relative_uri` String,`absolute_uri` String,`relation_view` String,`authenticate` UInt8,`type` UInt8,`name` String) ENGINE = MergeTree(create_date, (id), 8192);

-- risk

CREATE DATABASE ldap_risk_0 ENGINE = Ordinary;
CREATE DATABASE ldap_risk_1 ENGINE = Ordinary;

use ldap_risk_0;

CREATE TABLE `risk_hit_record_0`(`id` UInt64,`data_event_type` String,`data_event_op_type` String,`stamp` UInt64,`create_date` date,`method` String,`uri` String,`real_uri` String,`request_body` String,`request_extra` String,`response_status` UInt16,`response_body` String,`response_extra` String,`request_id` String,`metadata` String,`jwt` String,`member_id` UInt64,`role_ids` Array(UInt64),`credential_type` String,`device_type` String,`login_time` UInt64,`client_ip` String,`user_agent` String,`sec_key` String,`request_un_decryption` UInt8,`response_un_encryption` UInt8,`existence_request_body` UInt8,`existence_response_body` UInt8,`duration_seconds` UInt8, `hit_type` UInt8, `illegal_expires_second` UInt64) ENGINE = MergeTree(create_date, (id), 8192);
CREATE TABLE `risk_hit_record_1`(`id` UInt64,`data_event_type` String,`data_event_op_type` String,`stamp` UInt64,`create_date` date,`method` String,`uri` String,`real_uri` String,`request_body` String,`request_extra` String,`response_status` UInt16,`response_body` String,`response_extra` String,`request_id` String,`metadata` String,`jwt` String,`member_id` UInt64,`role_ids` Array(UInt64),`credential_type` String,`device_type` String,`login_time` UInt64,`client_ip` String,`user_agent` String,`sec_key` String,`request_un_decryption` UInt8,`response_un_encryption` UInt8,`existence_request_body` UInt8,`existence_response_body` UInt8,`duration_seconds` UInt8, `hit_type` UInt8, `illegal_expires_second` UInt64) ENGINE = MergeTree(create_date, (id), 8192);

use ldap_risk_1;

CREATE TABLE `risk_hit_record_0`(`id` UInt64,`data_event_type` String,`data_event_op_type` String,`stamp` UInt64,`create_date` date,`method` String,`uri` String,`real_uri` String,`request_body` String,`request_extra` String,`response_status` UInt16,`response_body` String,`response_extra` String,`request_id` String,`metadata` String,`jwt` String,`member_id` UInt64,`role_ids` Array(UInt64),`credential_type` String,`device_type` String,`login_time` UInt64,`client_ip` String,`user_agent` String,`sec_key` String,`request_un_decryption` UInt8,`response_un_encryption` UInt8,`existence_request_body` UInt8,`existence_response_body` UInt8,`duration_seconds` UInt8, `hit_type` UInt8, `illegal_expires_second` UInt64) ENGINE = MergeTree(create_date, (id), 8192);
CREATE TABLE `risk_hit_record_1`(`id` UInt64,`data_event_type` String,`data_event_op_type` String,`stamp` UInt64,`create_date` date,`method` String,`uri` String,`real_uri` String,`request_body` String,`request_extra` String,`response_status` UInt16,`response_body` String,`response_extra` String,`request_id` String,`metadata` String,`jwt` String,`member_id` UInt64,`role_ids` Array(UInt64),`credential_type` String,`device_type` String,`login_time` UInt64,`client_ip` String,`user_agent` String,`sec_key` String,`request_un_decryption` UInt8,`response_un_encryption` UInt8,`existence_request_body` UInt8,`existence_response_body` UInt8,`duration_seconds` UInt8, `hit_type` UInt8, `illegal_expires_second` UInt64) ENGINE = MergeTree(create_date, (id), 8192);


-- analyze

CREATE DATABASE ldap_analyze_0 ENGINE = Ordinary;

CREATE DATABASE ldap_analyze_1 ENGINE = Ordinary;











