-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- data

-- lake

CREATE
DATABASE ldap_lake_0 ENGINE = Ordinary;

use
ldap_lake_0;

CREATE TABLE `opt_event_0`
(
    `id`                      UInt64,
    `data_event_type`         String,
    `stamp`                   UInt64,
    `create_date`             date,
    `method`                  String,
    `uri`                     String,
    `real_uri`                String,
    `request_body`            String,
    `response_status`         UInt16,
    `response_body`           String,
    `request_id`              String,
    `metadata`                String,
    `jwt`                     String,
    `access`                  String,
    `client_ip`               String,
    `sec_key`                 String,
    `request_un_decryption`   UInt8,
    `response_un_encryption`  UInt8,
    `existence_request_body`  UInt8,
    `existence_response_body` UInt8
) ENGINE = MergeTree(create_date, (id), 8192);

CREATE TABLE `opt_event_1`
(
    `id`                      UInt64,
    `data_event_type`         String,
    `stamp`                   UInt64,
    `create_date`             date,
    `method`                  String,
    `uri`                     String,
    `real_uri`                String,
    `request_body`            String,
    `response_status`         UInt16,
    `response_body`           String,
    `request_id`              String,
    `metadata`                String,
    `jwt`                     String,
    `access`                  String,
    `client_ip`               String,
    `sec_key`                 String,
    `request_un_decryption`   UInt8,
    `response_un_encryption`  UInt8,
    `existence_request_body`  UInt8,
    `existence_response_body` UInt8
) ENGINE = MergeTree(create_date, (id), 8192);



CREATE
DATABASE ldap_lake_1 ENGINE = Ordinary;

use
ldap_lake_1;

CREATE TABLE `opt_event_0`
(
    `id`                      UInt64,
    `data_event_type`         String,
    `stamp`                   UInt64,
    `create_date`             date,
    `method`                  String,
    `uri`                     String,
    `real_uri`                String,
    `request_body`            String,
    `response_status`         UInt16,
    `response_body`           String,
    `request_id`              String,
    `metadata`                String,
    `jwt`                     String,
    `access`                  String,
    `client_ip`               String,
    `sec_key`                 String,
    `request_un_decryption`   UInt8,
    `response_un_encryption`  UInt8,
    `existence_request_body`  UInt8,
    `existence_response_body` UInt8
) ENGINE = MergeTree(create_date, (id), 8192);

CREATE TABLE `opt_event_1`
(
    `id`                      UInt64,
    `data_event_type`         String,
    `stamp`                   UInt64,
    `create_date`             date,
    `method`                  String,
    `uri`                     String,
    `real_uri`                String,
    `request_body`            String,
    `response_status`         UInt16,
    `response_body`           String,
    `request_id`              String,
    `metadata`                String,
    `jwt`                     String,
    `access`                  String,
    `client_ip`               String,
    `sec_key`                 String,
    `request_un_decryption`   UInt8,
    `response_un_encryption`  UInt8,
    `existence_request_body`  UInt8,
    `existence_response_body` UInt8
) ENGINE = MergeTree(create_date, (id), 8192);



-- risk

CREATE DATABASE ldap_risk_0 ENGINE = Ordinary;

CREATE DATABASE ldap_risk_1 ENGINE = Ordinary;








-- analyze

CREATE DATABASE ldap_analyze_0 ENGINE = Ordinary;

CREATE DATABASE ldap_analyze_1 ENGINE = Ordinary;











