-- noinspection SpellCheckingInspectionForFile

-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- auth

CREATE
DATABASE auth CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
auth;

CREATE TABLE `resource`
(
    `id`                      bigint       NOT NULL COMMENT 'id',
    `request_method`          varchar(16)  NOT NULL COMMENT 'request method/upper',
    `module`                  varchar(256) NOT NULL COMMENT 'module/service',
    `uri`                     varchar(256) NOT NULL COMMENT 'resource uri/lower',
    `authenticate`            bit          NOT NULL COMMENT 'need auth? 1-yes 0-no',
    `request_un_decryption`   bit          NOT NULL COMMENT 'do not decrypt request body? 1-not 0-yes',
    `response_un_encryption`  bit          NOT NULL COMMENT 'do not encrypt response body? 1-not 0-yes',
    `existence_request_body`  bit          NOT NULL COMMENT 'has request body? 1-yes 0-no',
    `existence_response_body` bit          NOT NULL COMMENT 'has response body? 1-yes 0-no',
    `type`                    tinyint      NOT NULL COMMENT 'resource type, 1-api 2-manage api 3-open api',
    `name`                    varchar(128) NOT NULL COMMENT 'resource name',
    `description`             varchar(256) DEFAULT '' COMMENT 'resource disc',
    `create_time`             bigint       NOT NULL COMMENT 'data create time',
    `update_time`             bigint       NOT NULL COMMENT 'data update time',
    `creator`                 bigint       NOT NULL COMMENT 'creator id',
    `updater`                 bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_method_module_uri`(`request_method`,`module`,`uri`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of resource';

CREATE TABLE `role`
(
    `id`          bigint      NOT NULL COMMENT 'id',
    `name`        varchar(64) NOT NULL COMMENT 'role name',
    `description` varchar(128) DEFAULT '' COMMENT 'role disc',
    `level`       int         NOT NULL COMMENT 'roles level',
    `is_default`  bit         NOT NULL COMMENT 'is default role? 1-yes 0-no',
    `create_time` bigint      NOT NULL COMMENT 'data create time',
    `update_time` bigint      NOT NULL COMMENT 'data update time',
    `creator`     bigint      NOT NULL COMMENT 'creator id',
    `updater`     bigint      NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_level`(`level`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of role';

CREATE TABLE `role_res_relation`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `role_id`     bigint NOT NULL COMMENT 'role id',
    `res_id`      bigint NOT NULL COMMENT 'resource id',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    `creator`     bigint NOT NULL COMMENT 'creator id',
    `updater`     bigint NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_role_res`(`role_id`,`res_id`) USING BTREE,
    UNIQUE KEY `idx_res_role`(`res_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of role and resource relation';

-- auth0

CREATE
DATABASE auth_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
auth_0;

CREATE TABLE `member_role_relation_0`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `role_id`     bigint NOT NULL COMMENT 'role id',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    `creator`     bigint NOT NULL COMMENT 'creator id',
    `updater`     bigint NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_role`(`member_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member and role relation 0';

CREATE TABLE `member_role_relation_1`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `role_id`     bigint NOT NULL COMMENT 'role id',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    `creator`     bigint NOT NULL COMMENT 'creator id',
    `updater`     bigint NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_role`(`member_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member and role relation 1';

CREATE TABLE `credential_0`
(
    `id`          bigint      NOT NULL COMMENT 'id',
    `credential`  varchar(128) DEFAULT '' COMMENT 'credential',
    `type`        varchar(32) NOT NULL COMMENT 'credential type: PVAR-PHONE_VERIFY_AUTO_REGISTER, PP-PHONE_PWD, EVAR-EMAIL_VERIFY_AUTO_REGISTER, EP-EMAIL_PWD, WEAR-WECHAT_AUTO_REGISTER, MPAR-MINI_PRO_AUTO_REGISTER, LPAR-LOCAL_PHONE_AUTO_REGISTER, NLI-NOT_LOGGED_IN',
    `access`      varchar(255) DEFAULT '' COMMENT 'encrypted password(str)/info(json)',
    `member_id`   bigint      NOT NULL COMMENT 'member id',
    `extra`       varchar(255) DEFAULT '' COMMENT 'extra info',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint      NOT NULL COMMENT 'data create time',
    `update_time` bigint      NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_credential_type_access_member`(`credential`,`type`,`access`,`member_id`) USING BTREE,
    UNIQUE KEY `idx_credential_type`(`credential`,`type`) USING BTREE,
    UNIQUE KEY `idx_member_type`(`member_id`,`type` ) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='credential 0';

CREATE TABLE `credential_1`
(
    `id`          bigint      NOT NULL COMMENT 'id',
    `credential`  varchar(128) DEFAULT '' COMMENT 'credential',
    `type`        varchar(32) NOT NULL COMMENT 'credential type: PVAR-PHONE_VERIFY_AUTO_REGISTER, PP-PHONE_PWD, EVAR-EMAIL_VERIFY_AUTO_REGISTER, EP-EMAIL_PWD, WEAR-WECHAT_AUTO_REGISTER, MPAR-MINI_PRO_AUTO_REGISTER, LPAR-LOCAL_PHONE_AUTO_REGISTER, NLI-NOT_LOGGED_IN',
    `access`      varchar(255) DEFAULT '' COMMENT 'encrypted password(str)/info(json)',
    `member_id`   bigint      NOT NULL COMMENT 'member id',
    `extra`       varchar(255) DEFAULT '' COMMENT 'extra info',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint      NOT NULL COMMENT 'data create time',
    `update_time` bigint      NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_credential_type_access_member`(`credential`,`type`,`access`,`member_id`) USING BTREE,
    UNIQUE KEY `idx_credential_type`(`credential`,`type`) USING BTREE,
    UNIQUE KEY `idx_member_type`(`member_id`,`type` ) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='credential 1';

CREATE TABLE `security_question_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `member_id`   bigint       NOT NULL COMMENT 'member id',
    `question`    varchar(512) NOT NULL COMMENT 'question',
    `answer`      varchar(512) NOT NULL COMMENT 'answer',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_question`(`member_id`,`question`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='security question 0';

CREATE TABLE `security_question_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `member_id`   bigint       NOT NULL COMMENT 'member id',
    `question`    varchar(512) NOT NULL COMMENT 'question',
    `answer`      varchar(512) NOT NULL COMMENT 'answer',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_question`(`member_id`,`question`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='security question 1';

CREATE TABLE `undo_log_0`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 0';

CREATE TABLE `undo_log_1`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 1';



-- seata undo log

-- auth1

CREATE
DATABASE auth_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
auth_1;

CREATE TABLE `member_role_relation_0`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `role_id`     bigint NOT NULL COMMENT 'role id',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    `creator`     bigint NOT NULL COMMENT 'creator id',
    `updater`     bigint NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_role`(`member_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member and role relation 0';

CREATE TABLE `member_role_relation_1`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `role_id`     bigint NOT NULL COMMENT 'role id',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    `creator`     bigint NOT NULL COMMENT 'creator id',
    `updater`     bigint NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_role`(`member_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member and role relation 1';

CREATE TABLE `credential_0`
(
    `id`          bigint      NOT NULL COMMENT 'id',
    `credential`  varchar(128) DEFAULT '' COMMENT 'credential',
    `type`        varchar(32) NOT NULL COMMENT 'credential type: PVAR-PHONE_VERIFY_AUTO_REGISTER, PP-PHONE_PWD, EVAR-EMAIL_VERIFY_AUTO_REGISTER, EP-EMAIL_PWD, WEAR-WECHAT_AUTO_REGISTER, MPAR-MINI_PRO_AUTO_REGISTER, LPAR-LOCAL_PHONE_AUTO_REGISTER, NLI-NOT_LOGGED_IN',
    `access`      varchar(255) DEFAULT '' COMMENT 'encrypted password(str)/info(json)',
    `member_id`   bigint      NOT NULL COMMENT 'member id',
    `extra`       varchar(255) DEFAULT '' COMMENT 'extra info',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint      NOT NULL COMMENT 'data create time',
    `update_time` bigint      NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_credential_type_access_member`(`credential`,`type`,`access`,`member_id`) USING BTREE,
    UNIQUE KEY `idx_credential_type`(`credential`,`type`) USING BTREE,
    UNIQUE KEY `idx_member_type`(`member_id`,`type` ) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='credential 0';

CREATE TABLE `credential_1`
(
    `id`          bigint      NOT NULL COMMENT 'id',
    `credential`  varchar(128) DEFAULT '' COMMENT 'credential',
    `type`        varchar(32) NOT NULL COMMENT 'credential type: PVAR-PHONE_VERIFY_AUTO_REGISTER, PP-PHONE_PWD, EVAR-EMAIL_VERIFY_AUTO_REGISTER, EP-EMAIL_PWD, WEAR-WECHAT_AUTO_REGISTER, MPAR-MINI_PRO_AUTO_REGISTER, LPAR-LOCAL_PHONE_AUTO_REGISTER, NLI-NOT_LOGGED_IN',
    `access`      varchar(255) DEFAULT '' COMMENT 'encrypted password(str)/info(json)',
    `member_id`   bigint      NOT NULL COMMENT 'member id',
    `extra`       varchar(255) DEFAULT '' COMMENT 'extra info',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint      NOT NULL COMMENT 'data create time',
    `update_time` bigint      NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_credential_type_access_member`(`credential`,`type`,`access`,`member_id`) USING BTREE,
    UNIQUE KEY `idx_credential_type`(`credential`,`type`) USING BTREE,
    UNIQUE KEY `idx_member_type`(`member_id`,`type` ) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='credential 1';

CREATE TABLE `security_question_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `member_id`   bigint       NOT NULL COMMENT 'member id',
    `question`    varchar(512) NOT NULL COMMENT 'question',
    `answer`      varchar(512) NOT NULL COMMENT 'answer',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_question`(`member_id`,`question`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='security question 0';

CREATE TABLE `security_question_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `member_id`   bigint       NOT NULL COMMENT 'member id',
    `question`    varchar(512) NOT NULL COMMENT 'question',
    `answer`      varchar(512) NOT NULL COMMENT 'answer',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_question`(`member_id`,`question`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='security question 1';

CREATE TABLE `undo_log_0`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 0';

CREATE TABLE `undo_log_1`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 1';

USE
auth;

INSERT INTO `auth`.`resource`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                              response_un_encryption, `existence_request_body`, `existence_response_body`,
                              `type`, `name`, `description`, `create_time`, `update_time`,
                              `creator`, `updater`)


-- base api

VALUES (100001, 'GET', 'blue-base', '/countries', b'0', b'1', b'1', b'0', b'1', 1,
        'countries', 'countries', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100002, 'GET', 'blue-base', '/states/{pid}', b'0', b'1', b'1', b'0', b'1', 1,
        'states', 'states', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100003, 'GET', 'blue-base', '/cities/{pid}', b'0', b'1', b'1', b'0', b'1', 1,
        'cities', 'cities', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100004, 'GET', 'blue-base', '/areas/{pid}', b'0', b'1', b'1', b'0', b'1', 1,
        'areas', 'areas', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100005, 'GET', 'blue-base', '/languages', b'0', b'1', b'1', b'0', b'1', 1,
        'support languages', 'support languages', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100006, 'GET', 'blue-base', '/language', b'0', b'1', b'1', b'0', b'1', 1,
        'get default language', 'get default language', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100007, 'GET', 'blue-base', '/element', b'0', b'1', b'1', b'0', b'1', 1,
        'select all element', 'select all element', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100008, 'POST', 'blue-base', '/element', b'0', b'1', b'1', b'0', b'1', 1,
        'select target element', 'select target element', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100009, 'GET', 'blue-base', '/dictType', b'0', b'1', b'1', b'0', b'1', 1,
        'query dict types', 'query dict types', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100010, 'GET', 'blue-base', '/bulletin/{type}', b'0', b'1', b'1', b'0', b'1', 1,
        'test get endpoint', 'test get endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100011, 'GET', 'blue-base', '/style/{type}', b'0', b'1', b'1', b'0', b'1', 1,
        'active style of api', 'active style of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- base manage

       (110001, 'POST', 'blue-base', '/manager/countries', b'1', b'1', b'1', b'1', b'1', 2,
        'country list', 'country list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110002, 'POST', 'blue-base', '/manager/country', b'1', b'1', b'1', b'1', b'1', 2,
        'insert country', 'insert country', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110003, 'PUT', 'blue-base', '/manager/country', b'1', b'1', b'1', b'1', b'1', 2,
        'update country', 'update country', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110004, 'DELETE', 'blue-base', '/manager/country/{id}', b'1', b'1', b'1', b'1', b'1', 2,
        'delete country', 'delete country', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (110005, 'POST', 'blue-base', '/manager/states', b'1', b'1', b'1', b'1', b'1', 2,
        'state list', 'state list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110006, 'POST', 'blue-base', '/manager/state', b'1', b'1', b'1', b'1', b'1', 2,
        'insert state', 'insert state', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110007, 'PUT', 'blue-base', '/manager/state', b'1', b'1', b'1', b'1', b'1', 2,
        'update state', 'update state', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110008, 'DELETE', 'blue-base', '/manager/state/{id}', b'1', b'1', b'1', b'1', b'1', 2,
        'delete state', 'delete state', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (110009, 'POST', 'blue-base', '/manager/cities', b'1', b'1', b'1', b'1', b'1', 2,
        'city list', 'city list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110010, 'POST', 'blue-base', '/manager/city', b'1', b'1', b'1', b'1', b'1', 2,
        'insert city', 'insert city', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110011, 'PUT', 'blue-base', '/manager/city', b'1', b'1', b'1', b'1', b'1', 2,
        'update city', 'update city', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110012, 'DELETE', 'blue-base', '/manager/city/{id}', b'1', b'1', b'1', b'1', b'1', 2,
        'delete city', 'delete city', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (110013, 'POST', 'blue-base', '/manager/areas', b'1', b'1', b'1', b'1', b'1', 2,
        'area list', 'area list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110014, 'POST', 'blue-base', '/manager/area', b'1', b'1', b'1', b'1', b'1', 2,
        'insert area', 'insert area', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110015, 'PUT', 'blue-base', '/manager/area', b'1', b'1', b'1', b'1', b'1', 2,
        'update area', 'update area', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110016, 'DELETE', 'blue-base', '/manager/area/{id}', b'1', b'1', b'1', b'1', b'1', 2,
        'delete area', 'delete area', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (110017, 'PUT', 'blue-base', '/manager/languages', b'1', b'1', b'1', b'1', b'1', 2,
        'refresh languages and elements', 'refresh languages and elements', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110018, 'POST', 'blue-base', '/manager/styles', b'1', b'1', b'1', b'1', b'1', 2,
        'style list of manager', 'style list of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110019, 'POST', 'blue-base', '/manager/style', b'1', b'1', b'1', b'1', b'1', 2,
        'insert style', 'insert style', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110020, 'PUT', 'blue-base', '/manager/style', b'1', b'1', b'1', b'1', b'1', 2,
        'update style', 'update style', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110021, 'DELETE', 'blue-base', '/manager/style/{id}', b'1', b'1', b'1', b'1', b'1', 2,
        'delete style', 'delete style', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110022, 'PUT', 'blue-base', '/manager/style/active', b'1', b'1', b'1', b'1', b'1', 2,
        'update active style', 'update active style', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- verify api

       (120001, 'POST', 'blue-verify', '/verify/generate', b'0', b'1', b'1', b'0', b'1', 1,
        'generate verify with param', 'generate verify with param', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- verify manage


-- auth api

       (160001, 'POST', 'blue-auth', '/auth/session', b'0', b'1', b'1', b'1', b'1', 1,
        'login', 'login', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160002, 'DELETE', 'blue-auth', '/auth/session', b'1', b'1', b'1', b'0', b'1', 1,
        'logout', 'logout', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160003, 'DELETE', 'blue-auth', '/auth/sessions', b'1', b'1', b'1', b'0', b'1', 1,
        'logout everywhere', 'logout everywhere', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160004, 'PATCH', 'blue-auth', '/auth/access', b'0', b'1', b'1', b'1', b'1', 1,
        'refresh access', 'refresh access', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160005, 'PATCH', 'blue-auth', '/auth/secret', b'1', b'1', b'1', b'0', b'1', 1,
        'refresh private key', 'refresh private key', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160006, 'POST', 'blue-auth', '/auth/credential', b'1', b'1', b'1', b'0', b'1', 1,
        'credential setting up', 'credential setting up', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160007, 'PUT', 'blue-auth', '/auth/credential', b'1', b'1', b'1', b'0', b'1', 1,
        'credential update', 'credential update', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160008, 'PUT', 'blue-auth', '/auth/access', b'1', b'1', b'1', b'0', b'1', 1,
        'update access', 'update access', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160009, 'POST', 'blue-auth', '/auth/access', b'0', b'1', b'1', b'0', b'1', 1,
        'reset access', 'reset access', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160010, 'POST', 'blue-auth', '/auth/question', b'1', b'1', b'1', b'0', b'1', 1,
        'insert security question', 'insert security question', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160011, 'POST', 'blue-auth', '/auth/questions', b'1', b'1', b'1', b'0', b'1', 1,
        'insert security questions', 'insert security questions', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160012, 'GET', 'blue-auth', '/auth/authority', b'1', b'1', b'1', b'0', b'1', 1,
        'query authority', 'query authority', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- auth manage

       (170001, 'DELETE', 'blue-auth', '/manager/auth/{mid}', b'1', b'1', b'1', b'0', b'1', 2,
        'invalidate member auth', 'invalidate member auth', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (170002, 'POST', 'blue-auth', '/manager/resources', b'1', b'1', b'1', b'1', b'1', 2,
        'resource list', 'resource list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170003, 'POST', 'blue-auth', '/manager/resource', b'1', b'1', b'1', b'1', b'1', 2,
        'insert resource', 'insert resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170004, 'PUT', 'blue-auth', '/manager/resource', b'1', b'1', b'1', b'1', b'1', 2,
        'update resource', 'update resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170005, 'DELETE', 'blue-auth', '/manager/resource/{id}', b'1', b'1', b'1', b'1', b'1', 2,
        'delete resource', 'delete resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170006, 'POST', 'blue-auth', '/manager/resource/auth', b'1', b'1', b'1', b'1', b'1', 2,
        'resource auth', 'resource auth', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (170007, 'POST', 'blue-auth', '/manager/roles', b'1', b'1', b'1', b'1', b'1', 2,
        'role list', 'role list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170008, 'POST', 'blue-auth', '/manager/role', b'1', b'1', b'1', b'1', b'1', 2,
        'insert role', 'insert role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170009, 'PUT', 'blue-auth', '/manager/role', b'1', b'1', b'1', b'1', b'1', 2,
        'update role', 'update role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170010, 'DELETE', 'blue-auth', '/manager/role/{id}', b'1', b'1', b'1', b'1', b'1', 2,
        'delete role', 'delete role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170011, 'PUT', 'blue-auth', '/manager/role/default', b'1', b'1', b'1', b'1', b'1', 2,
        'update default role', 'update default role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170012, 'POST', 'blue-auth', '/manager/role/auth', b'1', b'1', b'1', b'1', b'1', 2,
        'role auth', 'role auth', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (170013, 'PUT', 'blue-auth', '/manager/relation/role-res-update', b'1', b'1', b'1', b'1', b'1', 2,
        'update role-resources-relation', 'update role-resources-relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170014, 'PATCH', 'blue-auth', '/manager/relation/mem-role-insert', b'1', b'1', b'1', b'1', b'1', 2,
        'insert member-role-relation', 'insert member-role-relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170015, 'PUT', 'blue-auth', '/manager/relation/mem-role-update', b'1', b'1', b'1', b'1', b'1', 2,
        'update member-role-relation', 'update member-role-relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170016, 'PATCH', 'blue-auth', '/manager/relation/mem-role-delete', b'1', b'1', b'1', b'1', b'1', 2,
        'delete member-role-relation', 'delete member-role-relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (170017, 'GET', 'blue-auth', '/manager/auth/security/{mid}', b'1', b'1', b'1', b'0', b'1', 2,
        'select members security info', 'select members security info', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- member api

       (180001, 'POST', 'blue-member', '/registry', b'0', b'1', b'1', b'1', b'1', 1,
        'member registry', 'member registry', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180002, 'GET', 'blue-member', '/basic', b'1', b'1', b'1', b'0', b'1', 1,
        'member basic info', 'member basic info', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180003, 'PATCH', 'blue-member', '/basic/icon', b'1', b'1', b'1', b'0', b'1', 1,
        'update member icon', 'update member icon', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180004, 'PATCH', 'blue-member', '/basic/qrCode', b'1', b'1', b'1', b'0', b'1', 1,
        'update member qrCode', 'update member qrCode', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180005, 'PATCH', 'blue-member', '/basic/profile', b'1', b'1', b'1', b'0', b'1', 1,
        'update member profile', 'update member profile', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180007, 'PUT', 'blue-member', '/detail', b'1', b'1', b'1', b'0', b'1', 1,
        'update member detail', 'update member detail', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180008, 'GET', 'blue-member', '/detail', b'1', b'1', b'1', b'0', b'1', 1,
        'member detail info', 'member detail info', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180009, 'PUT', 'blue-member', '/realname', b'1', b'1', b'1', b'0', b'1', 1,
        'update member realname', 'update member realname', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180010, 'GET', 'blue-member', '/realname', b'1', b'1', b'1', b'0', b'1', 1,
        'member realname info', 'member realname info', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180011, 'POST', 'blue-member', '/address', b'1', b'1', b'1', b'1', b'1', 1,
        'add address', 'add address', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180012, 'PUT', 'blue-member', '/address', b'1', b'1', b'1', b'0', b'1', 1,
        'update address', 'update address', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180013, 'DELETE', 'blue-member', '/address/{id}', b'1', b'1', b'1', b'0', b'1', 1,
        'delete address', 'delete address', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180014, 'GET', 'blue-member', '/address', b'1', b'1', b'1', b'0', b'1', 1,
        'select address for api', 'select address for api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180015, 'POST', 'blue-member', '/card', b'1', b'1', b'1', b'1', b'1', 1,
        'add card', 'add card', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180016, 'PUT', 'blue-member', '/card', b'1', b'1', b'1', b'0', b'1', 1,
        'update card', 'update card', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180017, 'DELETE', 'blue-member', '/card/{id}', b'1', b'1', b'1', b'0', b'1', 1,
        'delete card', 'delete card', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180018, 'GET', 'blue-member', '/card', b'1', b'1', b'1', b'0', b'1', 1,
        'select card for api', 'select card for api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- member manage

       (190001, 'POST', 'blue-member', '/manager/basics', b'1', b'1', b'1', b'1', b'1', 2,
        'member basic list', 'member basic list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (190002, 'POST', 'blue-member', '/manager/details', b'1', b'1', b'1', b'1', b'1', 2,
        'member detail list', 'member detail list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (190003, 'POST', 'blue-member', '/manager/realnames', b'1', b'1', b'1', b'1', b'1', 2,
        'member realname list', 'member realname list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (190004, 'POST', 'blue-member', '/manager/authorities', b'1', b'1', b'1', b'1', b'1', 2,
        'authority list', 'authority list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (190005, 'POST', 'blue-member', '/manager/addresses', b'1', b'1', b'1', b'1', b'1', 2,
        'select address for manager', 'select address for manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (190006, 'POST', 'blue-member', '/manager/cards', b'1', b'1', b'1', b'1', b'1', 2,
        'select card for manager', 'select card for manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- finance api
       (200001, 'GET', 'blue-finance', '/balance', b'1', b'1', b'1', b'0', b'1', 1,
        'query balance', 'query balance', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200002, 'POST', 'blue-finance', '/withdraw', b'1', b'0', b'0', b'1', b'1', 1,
        'withdraw/test encrypt in finance', 'withdraw/test encrypt in finance', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1,
        1),

-- finance open

       (200003, 'GET', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3, 'GET dynamic endpoint', 'GET dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200004, 'HEAD', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3, 'HEAD dynamic endpoint', 'HEAD dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200005, 'POST', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3, 'POST dynamic endpoint', 'POST dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200006, 'PUT', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3, 'PUT dynamic endpoint', 'PUT dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200007, 'PATCH', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3, 'PATCH dynamic endpoint', 'PATCH dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200008, 'DELETE', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1', 3,
        'DELETE dynamic endpoint', 'DELETE dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200009, 'OPTIONS', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1', 3,
        'OPTIONS dynamic endpoint', 'OPTIONS dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- finance manage


-- media api

       (220001, 'POST', 'blue-media', '/file/upload', b'1', b'1', b'1', b'1', b'1', 1,
        'media upload of api', 'media upload of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220002, 'POST', 'blue-media', '/file/download', b'1', b'1', b'1', b'1', b'0', 1,
        'file download of api', 'file download of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220003, 'POST', 'blue-media', '/attachments', b'1', b'1', b'1', b'1', b'1', 1,
        'attachment list of api', 'attachment list of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220004, 'POST', 'blue-media', '/downloadHistories', b'1', b'1', b'1', b'1', b'1', 1,
        'download history list of api', 'download history list of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220005, 'POST', 'blue-media', '/qrCode', b'1', b'1', b'1', b'1', b'1', 1,
        'generate qr code for api', 'generate qr code for api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (220006, 'POST', 'blue-media', '/withdraw', b'1', b'0', b'0', b'1', b'1', 1,
        'withdraw test encrypt in media', 'withdraw test encrypt in media', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220007, 'POST', 'blue-media', '/trd', b'1', b'1', b'1', b'1', b'1', 1,
        'test temp redirect in media', 'test temp redirect in medi', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220008, 'POST', 'blue-media', '/rd', b'1', b'1', b'1', b'1', b'1', 1,
        'test redirect in media', 'test redirect in media', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220009, 'POST', 'blue-media', '/fd', b'1', b'1', b'1', b'1', b'1', 1,
        'test forward in media', 'test forward in media', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (220010, 'GET', 'blue-media', '/mail/send', b'0', b'1', b'1', b'0', b'1', 2,
        'test send', 'test send', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220011, 'GET', 'blue-media', '/mail/read', b'0', b'1', b'1', b'0', b'1', 2,
        'test read', 'test read', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- media manage

       (230006, 'POST', 'blue-media', '/manager/attachments', b'1', b'1', b'1', b'0', b'1', 2,
        'attachment list of manager', 'attachment list of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (230007, 'POST', 'blue-media', '/manager/downloadHistories', b'1', b'1', b'1', b'0', b'1', 2,
        'download history list of manager', 'download history list of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1,
        1),
       (230008, 'POST', 'blue-media', '/manager/qrCodeConfigs', b'1', b'1', b'1', b'1', b'1', 2,
        ' qr code config list of manager', ' qr code config list of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (230009, 'POST', 'blue-media', '/manager/qrCodeConfig', b'1', b'1', b'1', b'1', b'1', 2,
        'insert qr code config', 'insert qr code config', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (230010, 'PUT', 'blue-media', '/manager/qrCodeConfig', b'1', b'1', b'1', b'1', b'1', 2,
        'update qr code config', 'update qr code config', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (230011, 'DELETE', 'blue-media', '/manager/qrCodeConfig/{id}', b'1', b'1', b'1', b'1', b'1', 2,
        'delete qr code config', 'delete qr code config', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),


-- portal api

       (250001, 'GET', 'blue-portal', '/bulletins/{type}', b'0', b'1', b'1', b'0', b'1', 1,
        'bulletin list of api', 'bulletin list of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (250003, 'GET', 'blue-portal', '/formatter/{formatter}.html', b'1', b'1', b'1', b'0', b'1', 1,
        'formatter test', 'formatter test', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (250004, 'GET', 'blue-portal', '/fallBack', b'0', b'1', b'1', b'0', b'1', 1,
        'GET fallback', 'GET fallback', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (250005, 'POST', 'blue-portal', '/fallBack', b'0', b'1', b'1', b'1', b'1', 1,
        'POST fallback', 'POST fallback', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- portal manage

       (260001, 'POST', 'blue-portal', '/manager/bulletins', b'1', b'1', b'1', b'0', b'1', 2,
        'bulletin list of manager', 'bulletin list of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (260002, 'POST', 'blue-portal', '/manager/bulletin', b'1', b'1', b'1', b'1', b'1', 2,
        'insert portal', 'insert portal', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (260003, 'PUT', 'blue-portal', '/manager/bulletin', b'1', b'1', b'1', b'1', b'1', 2,
        'update portal', 'update portal', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (260004, 'DELETE', 'blue-portal', '/manager/bulletin/{id}', b'1', b'1', b'1', b'1', b'1', 2,
        'delete portal', 'delete portal', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- marketing api

       (270001, 'POST', 'blue-marketing', '/signIn', b'1', b'1', b'1', b'0', b'1', 1,
        'sign in', 'sign in', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (270002, 'GET', 'blue-marketing', '/signIn', b'1', b'1', b'1', b'0', b'1', 1,
        'query sign in record by month', 'query sign in record by month', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (270003, 'POST', 'blue-marketing', '/eventRecords', b'1', b'1', b'1', b'0', b'1', 1,
        'event record list of api', 'event record list of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- marketing manage

       (280001, 'POST', 'blue-marketing', '/manager/eventRecords', b'1', b'1', b'1', b'0', b'1', 2,
        'event record list of manager', 'event record list of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1,
        1),

-- shine api

       (290001, 'GET', 'blue-shine', '/shine', b'0', b'1', b'1', b'0', b'1', 1,
        'commonweal information', 'commonweal information', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- shine manage


-- event api

       (300001, 'POST', 'blue-event', '/event/report', b'0', b'1', b'1', b'1', b'1', 1,
        'event report', 'event report', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- event manage


-- data api

-- data manage

       (320001, 'POST', 'blue-lake', '/events', b'1', b'1', b'1', b'1', b'1', 2,
        'test lake eventRecord', 'test lake eventRecord', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (320002, 'POST', 'blue-analyze', '/statistics/active/simple', b'1', b'1', b'1', b'1', b'1', 2,
        'statistics active simple', 'statistics active simple', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (320003, 'POST', 'blue-analyze', '/statistics/active/merge', b'1', b'1', b'1', b'1', b'1', 2,
        'statistics merge active', 'statistics merge active', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (320004, 'POST', 'blue-analyze', '/statistics/active/summary', b'1', b'1', b'1', b'0', b'1', 2,
        'statistics summary', 'statistics summary', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

-- role

INSERT INTO `auth`.`role`(`id`, `name`, `description`, `level`, `is_default`, `create_time`, `update_time`, `creator`,
                          `updater`)
VALUES (1, 'blue', 'blue', 0, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (2, 'admin', 'admin', 1, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (3, 'manager', 'manager', 2, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (4, 'tester', 'tester', 3, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (5, 'customer', 'customer', 4, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (6, 'member', 'member', 5, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);


-- blue admin res
INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                       `updater`)
SELECT id + 1000000, 1, id, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1
FROM `auth`.`resource`;


-- admin res
INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                       `updater`)
SELECT id + 2000000, 2, id, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1
FROM `auth`.`resource`
WHERE `type` = 2;


-- manager res
INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                       `updater`)
SELECT id + 3000000, 3, id, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1
FROM `auth`.`resource`
WHERE `type` = 2;


-- tester res
INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                       `updater`)
SELECT id + 4000000, 4, id, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1
FROM `auth`.`resource`
WHERE `type` = 2;


-- customer res
INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                       `updater`)
SELECT id + 5000000, 5, id, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1
FROM `auth`.`resource`
WHERE `type` = 2;


-- member res
INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                       `updater`)
SELECT id + 6000000, 6, id, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1
FROM `auth`.`resource`
WHERE `type` = 1;

-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- finance0

CREATE
DATABASE finance_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
finance_0;

CREATE TABLE `finance_account_0`
(
    `id`          bigint  NOT NULL COMMENT 'id',
    `member_id`   bigint  NOT NULL COMMENT 'member id',
    `balance`     bigint DEFAULT '0' COMMENT 'active balance/fen',
    `frozen`      bigint DEFAULT '0' COMMENT 'frozen balance/fen',
    `income`      bigint DEFAULT '0' COMMENT 'total income/fen',
    `outlay`      bigint DEFAULT '0' COMMENT 'total outlay/fen',
    `status`      tinyint NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint  NOT NULL COMMENT 'data create time',
    `update_time` bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of finance account 0';

CREATE TABLE `finance_account_1`
(
    `id`          bigint  NOT NULL COMMENT 'id',
    `member_id`   bigint  NOT NULL COMMENT 'member id',
    `balance`     bigint DEFAULT '0' COMMENT 'active balance/fen',
    `frozen`      bigint DEFAULT '0' COMMENT 'frozen balance/fen',
    `income`      bigint DEFAULT '0' COMMENT 'total income/fen',
    `outlay`      bigint DEFAULT '0' COMMENT 'total outlay/fen',
    `status`      tinyint NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint  NOT NULL COMMENT 'data create time',
    `update_time` bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of finance account 1';

CREATE TABLE `finance_flow_0`
(
    `id`                    bigint  NOT NULL COMMENT 'id',
    `member_id`             bigint  NOT NULL COMMENT 'member id',
    `order_id`              bigint  NOT NULL COMMENT 'order id',
    `type`                  tinyint NOT NULL COMMENT 'flow type',
    `change_type`           tinyint NOT NULL COMMENT 'change type',
    `amount_changed`        bigint DEFAULT '0' COMMENT 'amount changed/fen',
    `amount_before_changed` bigint DEFAULT '0' COMMENT 'amount before changed/fen',
    `amount_after_changed`  bigint DEFAULT '0' COMMENT 'amount after changed/fen',
    `status`                tinyint NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`           bigint  NOT NULL COMMENT 'data create time',
    `update_time`           bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY                     `idx_member`(`member_id`) USING BTREE,
    KEY                     `idx_order`(`order_id`) USING BTREE,
    KEY                     `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of finance flow 0';

CREATE TABLE `finance_flow_1`
(
    `id`                    bigint  NOT NULL COMMENT 'id',
    `member_id`             bigint  NOT NULL COMMENT 'member id',
    `order_id`              bigint  NOT NULL COMMENT 'order id',
    `type`                  tinyint NOT NULL COMMENT 'flow type',
    `change_type`           tinyint NOT NULL COMMENT 'change type',
    `amount_changed`        bigint DEFAULT '0' COMMENT 'amount changed/fen',
    `amount_before_changed` bigint DEFAULT '0' COMMENT 'amount before changed/fen',
    `amount_after_changed`  bigint DEFAULT '0' COMMENT 'amount after changed/fen',
    `status`                tinyint NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`           bigint  NOT NULL COMMENT 'data create time',
    `update_time`           bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY                     `idx_member`(`member_id`) USING BTREE,
    KEY                     `idx_order`(`order_id`) USING BTREE,
    KEY                     `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of finance flow 1';

CREATE TABLE `organization_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `phone`       varchar(16)  NOT NULL COMMENT 'organization phone format: 8613131693996',
    `email`       varchar(256) NOT NULL COMMENT 'organization email',
    `name`        varchar(128) DEFAULT '0' COMMENT 'organization name',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    `updater`     bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    KEY           `idx_phone`(`phone`) USING BTREE,
    KEY           `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of organization 0';

CREATE TABLE `organization_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `phone`       varchar(16)  NOT NULL COMMENT 'organization phone format: 8613131693996',
    `email`       varchar(256) NOT NULL COMMENT 'organization email',
    `name`        varchar(128) DEFAULT '0' COMMENT 'organization name',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    `updater`     bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    KEY           `idx_phone`(`phone`) USING BTREE,
    KEY           `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of organization 1';

CREATE TABLE `dynamic_handler_0`
(
    `id`           bigint       NOT NULL COMMENT 'id',
    `name`         varchar(256) DEFAULT NULL COMMENT 'dynamic handler name',
    `description`  varchar(512) DEFAULT NULL COMMENT 'dynamic handler disc',
    `handler_bean` varchar(256) NOT NULL COMMENT 'dynamic handler bean',
    `create_time`  bigint       NOT NULL COMMENT 'data create time',
    `update_time`  bigint       NOT NULL COMMENT 'data update time',
    `creator`      bigint       NOT NULL COMMENT 'creator id',
    `updater`      bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name`(`name`) USING BTREE,
    UNIQUE KEY `idx_bean`(`handler_bean`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of dynamic handler 0';

CREATE TABLE `dynamic_handler_1`
(
    `id`           bigint       NOT NULL COMMENT 'id',
    `name`         varchar(256) DEFAULT NULL COMMENT 'dynamic handler name',
    `description`  varchar(512) DEFAULT NULL COMMENT 'dynamic handler disc',
    `handler_bean` varchar(256) NOT NULL COMMENT 'dynamic handler bean',
    `create_time`  bigint       NOT NULL COMMENT 'data create time',
    `update_time`  bigint       NOT NULL COMMENT 'data update time',
    `creator`      bigint       NOT NULL COMMENT 'creator id',
    `updater`      bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name`(`name`) USING BTREE,
    UNIQUE KEY `idx_bean`(`handler_bean`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of dynamic handler 1';

CREATE TABLE `dynamic_resource_0`
(
    `id`              bigint      NOT NULL COMMENT 'id',
    `organization_id` bigint      NOT NULL COMMENT 'organization id',
    `handler_id`      bigint      NOT NULL COMMENT 'dynamic handler id',
    `request_method`  varchar(16) NOT NULL COMMENT 'request method/upper',
    `uri_placeholder` bigint      NOT NULL COMMENT 'dynamic uri placeholder/lower',
    `content_type`    varchar(64) NOT NULL COMMENT 'content type',
    `name`            varchar(128) DEFAULT NULL COMMENT 'dynamic resource name',
    `description`     varchar(256) DEFAULT NULL COMMENT 'dynamic resource disc',
    `create_time`     bigint      NOT NULL COMMENT 'data create time',
    `update_time`     bigint      NOT NULL COMMENT 'data update time',
    `creator`         bigint      NOT NULL COMMENT 'creator id',
    `updater`         bigint      NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    KEY               `idx_organization_handler`(`organization_id`,`handler_id`) USING BTREE,
    UNIQUE KEY `idx_method_placeholder_content`(`request_method`,`uri_placeholder`,`content_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of dynamic resource 0';

CREATE TABLE `dynamic_resource_1`
(
    `id`              bigint      NOT NULL COMMENT 'id',
    `organization_id` bigint      NOT NULL COMMENT 'organization id',
    `handler_id`      bigint      NOT NULL COMMENT 'dynamic handler id',
    `request_method`  varchar(16) NOT NULL COMMENT 'request method/upper',
    `uri_placeholder` bigint      NOT NULL COMMENT 'dynamic uri placeholder/lower',
    `content_type`    varchar(64) NOT NULL COMMENT 'content type',
    `name`            varchar(128) DEFAULT NULL COMMENT 'dynamic resource name',
    `description`     varchar(256) DEFAULT NULL COMMENT 'dynamic resource disc',
    `create_time`     bigint      NOT NULL COMMENT 'data create time',
    `update_time`     bigint      NOT NULL COMMENT 'data update time',
    `creator`         bigint      NOT NULL COMMENT 'creator id',
    `updater`         bigint      NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    KEY               `idx_organization_handler`(`organization_id`,`handler_id`) USING BTREE,
    UNIQUE KEY `idx_method_placeholder_content`(`request_method`,`uri_placeholder`,`content_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of dynamic resource 1';

CREATE TABLE `undo_log_0`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 0';

CREATE TABLE `undo_log_1`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 1';

-- finance1

CREATE
DATABASE finance_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
finance_1;

CREATE TABLE `finance_account_0`
(
    `id`          bigint  NOT NULL COMMENT 'id',
    `member_id`   bigint  NOT NULL COMMENT 'member id',
    `balance`     bigint DEFAULT '0' COMMENT 'active balance/fen',
    `frozen`      bigint DEFAULT '0' COMMENT 'frozen balance/fen',
    `income`      bigint DEFAULT '0' COMMENT 'total income/fen',
    `outlay`      bigint DEFAULT '0' COMMENT 'total outlay/fen',
    `status`      tinyint NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint  NOT NULL COMMENT 'data create time',
    `update_time` bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of finance account 0';

CREATE TABLE `finance_account_1`
(
    `id`          bigint  NOT NULL COMMENT 'id',
    `member_id`   bigint  NOT NULL COMMENT 'member id',
    `balance`     bigint DEFAULT '0' COMMENT 'active balance/fen',
    `frozen`      bigint DEFAULT '0' COMMENT 'frozen balance/fen',
    `income`      bigint DEFAULT '0' COMMENT 'total income/fen',
    `outlay`      bigint DEFAULT '0' COMMENT 'total outlay/fen',
    `status`      tinyint NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint  NOT NULL COMMENT 'data create time',
    `update_time` bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of finance account 1';

CREATE TABLE `finance_flow_0`
(
    `id`                    bigint  NOT NULL COMMENT 'id',
    `member_id`             bigint  NOT NULL COMMENT 'member id',
    `order_id`              bigint  NOT NULL COMMENT 'order id',
    `type`                  tinyint NOT NULL COMMENT 'flow type',
    `change_type`           tinyint NOT NULL COMMENT 'change type',
    `amount_changed`        bigint DEFAULT '0' COMMENT 'amount changed/fen',
    `amount_before_changed` bigint DEFAULT '0' COMMENT 'amount before changed/fen',
    `amount_after_changed`  bigint DEFAULT '0' COMMENT 'amount after changed/fen',
    `status`                tinyint NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`           bigint  NOT NULL COMMENT 'data create time',
    `update_time`           bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY                     `idx_member`(`member_id`) USING BTREE,
    KEY                     `idx_order`(`order_id`) USING BTREE,
    KEY                     `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of finance flow 0';

CREATE TABLE `finance_flow_1`
(
    `id`                    bigint  NOT NULL COMMENT 'id',
    `member_id`             bigint  NOT NULL COMMENT 'member id',
    `order_id`              bigint  NOT NULL COMMENT 'order id',
    `type`                  tinyint NOT NULL COMMENT 'flow type',
    `change_type`           tinyint NOT NULL COMMENT 'change type',
    `amount_changed`        bigint DEFAULT '0' COMMENT 'amount changed/fen',
    `amount_before_changed` bigint DEFAULT '0' COMMENT 'amount before changed/fen',
    `amount_after_changed`  bigint DEFAULT '0' COMMENT 'amount after changed/fen',
    `status`                tinyint NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`           bigint  NOT NULL COMMENT 'data create time',
    `update_time`           bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY                     `idx_member`(`member_id`) USING BTREE,
    KEY                     `idx_order`(`order_id`) USING BTREE,
    KEY                     `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of finance flow 1';

CREATE TABLE `organization_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `phone`       varchar(16)  NOT NULL COMMENT 'organization phone format: 8613131693996',
    `email`       varchar(256) NOT NULL COMMENT 'organization email',
    `name`        varchar(128) DEFAULT '0' COMMENT 'organization name',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    `updater`     bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    KEY           `idx_phone`(`phone`) USING BTREE,
    KEY           `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of organization 0';

CREATE TABLE `organization_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `phone`       varchar(16)  NOT NULL COMMENT 'organization phone format: 8613131693996',
    `email`       varchar(256) NOT NULL COMMENT 'organization email',
    `name`        varchar(128) DEFAULT '0' COMMENT 'organization name',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    `updater`     bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    KEY           `idx_phone`(`phone`) USING BTREE,
    KEY           `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of organization 1';

CREATE TABLE `dynamic_handler_0`
(
    `id`           bigint       NOT NULL COMMENT 'id',
    `name`         varchar(256) DEFAULT NULL COMMENT 'dynamic handler name',
    `description`  varchar(512) DEFAULT NULL COMMENT 'dynamic handler disc',
    `handler_bean` varchar(256) NOT NULL COMMENT 'dynamic handler bean',
    `create_time`  bigint       NOT NULL COMMENT 'data create time',
    `update_time`  bigint       NOT NULL COMMENT 'data update time',
    `creator`      bigint       NOT NULL COMMENT 'creator id',
    `updater`      bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name`(`name`) USING BTREE,
    UNIQUE KEY `idx_bean`(`handler_bean`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of dynamic handler 0';

CREATE TABLE `dynamic_handler_1`
(
    `id`           bigint       NOT NULL COMMENT 'id',
    `name`         varchar(256) DEFAULT NULL COMMENT 'dynamic handler name',
    `description`  varchar(512) DEFAULT NULL COMMENT 'dynamic handler disc',
    `handler_bean` varchar(256) NOT NULL COMMENT 'dynamic handler bean',
    `create_time`  bigint       NOT NULL COMMENT 'data create time',
    `update_time`  bigint       NOT NULL COMMENT 'data update time',
    `creator`      bigint       NOT NULL COMMENT 'creator id',
    `updater`      bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name`(`name`) USING BTREE,
    UNIQUE KEY `idx_bean`(`handler_bean`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of dynamic handler 1';

CREATE TABLE `dynamic_resource_0`
(
    `id`              bigint      NOT NULL COMMENT 'id',
    `organization_id` bigint      NOT NULL COMMENT 'organization id',
    `handler_id`      bigint      NOT NULL COMMENT 'dynamic handler id',
    `request_method`  varchar(16) NOT NULL COMMENT 'request method/upper',
    `uri_placeholder` bigint      NOT NULL COMMENT 'dynamic uri placeholder/lower',
    `content_type`    varchar(64) NOT NULL COMMENT 'content type',
    `name`            varchar(128) DEFAULT NULL COMMENT 'dynamic resource name',
    `description`     varchar(256) DEFAULT NULL COMMENT 'dynamic resource disc',
    `create_time`     bigint      NOT NULL COMMENT 'data create time',
    `update_time`     bigint      NOT NULL COMMENT 'data update time',
    `creator`         bigint      NOT NULL COMMENT 'creator id',
    `updater`         bigint      NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    KEY               `idx_organization_handler`(`organization_id`,`handler_id`) USING BTREE,
    UNIQUE KEY `idx_method_placeholder_content`(`request_method`,`uri_placeholder`,`content_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of dynamic resource 0';

CREATE TABLE `dynamic_resource_1`
(
    `id`              bigint      NOT NULL COMMENT 'id',
    `organization_id` bigint      NOT NULL COMMENT 'organization id',
    `handler_id`      bigint      NOT NULL COMMENT 'dynamic handler id',
    `request_method`  varchar(16) NOT NULL COMMENT 'request method/upper',
    `uri_placeholder` bigint      NOT NULL COMMENT 'dynamic uri placeholder/lower',
    `content_type`    varchar(64) NOT NULL COMMENT 'content type',
    `name`            varchar(128) DEFAULT NULL COMMENT 'dynamic resource name',
    `description`     varchar(256) DEFAULT NULL COMMENT 'dynamic resource disc',
    `create_time`     bigint      NOT NULL COMMENT 'data create time',
    `update_time`     bigint      NOT NULL COMMENT 'data update time',
    `creator`         bigint      NOT NULL COMMENT 'creator id',
    `updater`         bigint      NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    KEY               `idx_organization_handler`(`organization_id`,`handler_id`) USING BTREE,
    UNIQUE KEY `idx_method_placeholder_content`(`request_method`,`uri_placeholder`,`content_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of dynamic resource 1';

CREATE TABLE `undo_log_0`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 0';

CREATE TABLE `undo_log_1`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 1';



INSERT INTO `finance_1`.`dynamic_handler_1`(`id`, `name`, `description`, `handler_bean`, `create_time`, `update_time`,
                                            `creator`, `updater`)
VALUES (1, 'blue_get dynamic endpoint handler', 'blue_get dynamic endpoint handler',
        'com.blue.finance.component.dynamic.impl.BlueGetDynamicEndPointHandlerImpl', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (2, 'blue_post dynamic endpoint handler', 'blue_post dynamic endpoint handler',
        'com.blue.finance.component.dynamic.impl.BluePostDynamicEndPointHandlerImpl', UNIX_TIMESTAMP(),
        UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `finance_1`.`dynamic_resource_1`(`id`, `organization_id`, `handler_id`, `request_method`, `uri_placeholder`,
                                             `content_type`, `name`,
                                             description, `create_time`, `update_time`, `creator`, `updater`)
VALUES (1, 30, 1, 'GET', 1, 'application/json',
        'blue_get dynamic resource',
        'blue_get dynamic resource',
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (2, 31, 2, 'POST', 1, 'application/json',
        'blue_post dynamic resource',
        'blue_post dynamic resource',
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);


-- marketing

CREATE
DATABASE marketing CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
marketing;

CREATE TABLE `reward`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `name`        varchar(128) NOT NULL COMMENT 'reward name',
    `detail`      varchar(256) DEFAULT NULL COMMENT 'reward disc',
    `link`        varchar(256) NOT NULL COMMENT 'reward link',
    `type`        tinyint      NOT NULL COMMENT 'reward type',
    `data`        varchar(512) NOT NULL COMMENT 'reward data/json',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    `updater`     bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of marketing reward';

CREATE TABLE `sign_reward_today_relation`
(
    `id`          bigint  NOT NULL COMMENT 'id',
    `reward_id`   bigint  NOT NULL COMMENT 'reward id',
    `year`        smallint(6) NOT NULL COMMENT 'year',
    `month`       tinyint NOT NULL COMMENT 'month',
    `day`         tinyint NOT NULL COMMENT 'day of month',
    `create_time` bigint  NOT NULL COMMENT 'data create time',
    `update_time` bigint  NOT NULL COMMENT 'data update time',
    `creator`     bigint  NOT NULL COMMENT 'creator id',
    `updater`     bigint  NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_date_reward`(`year`,`month`,`day`,`reward_id`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE,
    KEY           `idx_updater`(`updater`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reward and date relation';


-- marketing0

CREATE
DATABASE marketing_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
marketing_0;

CREATE TABLE `event_record_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `type`        tinyint      NOT NULL COMMENT 'event record type',
    `data`        varchar(512) NOT NULL COMMENT 'event record data/json',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-handled 0-un handled',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY           `idx_create_time`(`create_time`) USING BTREE,
    KEY           `idx_creator_type_create_time`(`creator`,`type`,`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of marketing event record 0';

CREATE TABLE `event_record_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `type`        tinyint      NOT NULL COMMENT 'event record type',
    `data`        varchar(512) NOT NULL COMMENT 'event record data/json',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-handled 0-un handled',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY           `idx_create_time`(`create_time`) USING BTREE,
    KEY           `idx_creator_type_create_time`(`creator`,`type`,`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of marketing event record 1';

CREATE TABLE `undo_log_0`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 0';

CREATE TABLE `undo_log_1`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 1';


-- marketing1

CREATE
DATABASE marketing_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
marketing_1;

CREATE TABLE `event_record_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `type`        tinyint      NOT NULL COMMENT 'event record type',
    `data`        varchar(512) NOT NULL COMMENT 'event record data/json',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-handled 0-un handled',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY           `idx_create_time`(`create_time`) USING BTREE,
    KEY           `idx_creator_type_create_time`(`creator`,`type`,`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of marketing event record 0';

CREATE TABLE `event_record_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `type`        tinyint      NOT NULL COMMENT 'event record type',
    `data`        varchar(512) NOT NULL COMMENT 'event record data/json',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-handled 0-un handled',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY           `idx_create_time`(`create_time`) USING BTREE,
    KEY           `idx_creator_type_create_time`(`creator`,`type`,`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of marketing event record 1';

CREATE TABLE `undo_log_0`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 0';

CREATE TABLE `undo_log_1`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 1';

-- init

INSERT INTO `marketing`.`reward`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                 `update_time`, `creator`, `updater`)
VALUES (1, '1th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (2, '2th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (3, '3th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (4, '4th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (5, '5th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (6, '6th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (7, '7th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (8, '8th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (9, '9th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (10, '10th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (11, '11th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (12, '12th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (13, '13th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (14, '14th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (15, '15th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (16, '16th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (17, '17th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (18, '18th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (19, '19th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (20, '20th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (21, '21th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (22, '22th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (23, '23th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (24, '24th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (25, '25th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (26, '26th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (27, '27th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (28, '28th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (29, '29th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (30, '30th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (31, '31th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);


INSERT INTO `marketing`.`sign_reward_today_relation`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                     `update_time`, `creator`, `updater`)
VALUES (1, 1, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 1,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (2, 2, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 2,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (3, 3, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 3,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (4, 4, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 4,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (5, 5, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 5,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (6, 6, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 6,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (7, 7, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 7,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (8, 8, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 8,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (9, 9, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 9,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (10, 10, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 10,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (11, 11, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 11,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (12, 12, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 12,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (13, 13, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 13,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (14, 14, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 14,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (15, 15, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 15,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (16, 16, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 16,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (17, 17, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 17,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (18, 18, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 18,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (19, 19, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 19,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (20, 20, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 20,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (21, 21, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 21,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (22, 22, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 22,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (23, 23, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 23,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (24, 24, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 24,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (25, 25, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 25,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (26, 26, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 26,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (27, 27, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 27,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (28, 28, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 28,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (29, 29, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 29,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (30, 30, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 30,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (31, 31, DATE_FORMAT(NOW(), '%Y'), DATE_FORMAT(NOW(), '%m'), 31,
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

-- member0

CREATE
DATABASE member_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
member_0;

CREATE TABLE `member_basic_0`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `phone`       varchar(256) DEFAULT '' COMMENT 'phone format: 8613131693996',
    `email`       varchar(256) DEFAULT '' COMMENT 'email',
    `name`        varchar(256) DEFAULT '' COMMENT 'name',
    `icon`        varchar(256) DEFAULT '' COMMENT 'icon link',
    `qr_code`     varchar(256) DEFAULT '' COMMENT 'qrcode link',
    `gender`      tinyint      DEFAULT '3' COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
    `profile`     varchar(128) DEFAULT '' COMMENT 'profile',
    `source`      varchar(16)  DEFAULT 'APP' COMMENT 'source',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY           `idx_phone`(`phone`) USING BTREE,
    KEY           `idx_email`(`email`) USING BTREE,
    KEY           `idx_name`(`name`) USING BTREE,
    KEY           `idx_create_source`(`create_time`,`source`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member basic 0';

CREATE TABLE `member_basic_1`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `phone`       varchar(256) DEFAULT '' COMMENT 'phone format: 8613131693996',
    `email`       varchar(256) DEFAULT '' COMMENT 'email',
    `name`        varchar(256) DEFAULT '' COMMENT 'name',
    `icon`        varchar(255) DEFAULT '' COMMENT 'icon link',
    `qr_code`     varchar(256) DEFAULT '' COMMENT 'qrcode link',
    `gender`      tinyint      DEFAULT '3' COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
    `profile`     varchar(128) DEFAULT '' COMMENT 'profile',
    `source`      varchar(16)  DEFAULT 'APP' COMMENT 'source',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY           `idx_phone`(`phone`) USING BTREE,
    KEY           `idx_email`(`email`) USING BTREE,
    KEY           `idx_name`(`name`) USING BTREE,
    KEY           `idx_create_source`(`create_time`,`source`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member basic 1';

CREATE TABLE `real_name_0`
(
    `id`                bigint NOT NULL COMMENT 'id',
    `member_id`         bigint NOT NULL COMMENT 'member id',
    `real_name`         varchar(256) DEFAULT '' COMMENT 'read name',
    `gender`            tinyint      DEFAULT "3" COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
    `birthday`          varchar(32)  DEFAULT '' COMMENT 'birthday, format: yyyy-MM-dd',
    `nationality`       varchar(128) DEFAULT '' COMMENT 'nationality/country',
    `ethnic`            varchar(64)  DEFAULT '' COMMENT 'ethnic',
    `id_card_no`        varchar(128) DEFAULT '' COMMENT 'id card number',
    `residence_address` varchar(512) DEFAULT '' COMMENT 'residence address',
    `issuing_authority` varchar(512) DEFAULT '' COMMENT 'issuing authority',
    `since_date`        varchar(32)  DEFAULT '' COMMENT 'card since date, format: yyyy-MM-dd',
    `expire_date`       varchar(32)  DEFAULT '' COMMENT 'card expire date, format: yyyy-MM-dd',
    `extra`             varchar(255) DEFAULT '' COMMENT 'extra info',
    `status`            tinyint      DEFAULT '0' COMMENT 'data status: 1-valid 0-invalid',
    `create_time`       bigint NOT NULL COMMENT 'data create time',
    `update_time`       bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY                 `idx_real_name`(`real_name`) USING BTREE,
    KEY                 `idx_id_card`(`id_card_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of real name 0';

CREATE TABLE `real_name_1`
(
    `id`                bigint NOT NULL COMMENT 'id',
    `member_id`         bigint NOT NULL COMMENT 'member id',
    `real_name`         varchar(256) DEFAULT '' COMMENT 'read name',
    `gender`            tinyint      DEFAULT "3" COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
    `birthday`          varchar(32)  DEFAULT '' COMMENT 'birthday, format: yyyy-MM-dd',
    `nationality`       varchar(128) DEFAULT '' COMMENT 'nationality/country',
    `ethnic`            varchar(64)  DEFAULT '' COMMENT 'ethnic',
    `id_card_no`        varchar(128) DEFAULT '' COMMENT 'id card number',
    `residence_address` varchar(512) DEFAULT '' COMMENT 'residence address',
    `issuing_authority` varchar(512) DEFAULT '' COMMENT 'issuing authority',
    `since_date`        varchar(32)  DEFAULT '' COMMENT 'card since date, format: yyyy-MM-dd',
    `expire_date`       varchar(32)  DEFAULT '' COMMENT 'card expire date, format: yyyy-MM-dd',
    `extra`             varchar(255) DEFAULT '' COMMENT 'extra info',
    `status`            tinyint      DEFAULT '0' COMMENT 'data status: 1-valid 0-invalid',
    `create_time`       bigint NOT NULL COMMENT 'data create time',
    `update_time`       bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY                 `idx_real_name`(`real_name`) USING BTREE,
    KEY                 `idx_id_card`(`id_card_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of real name 1';

CREATE TABLE `member_detail_0`
(
    `id`             bigint NOT NULL COMMENT 'id',
    `member_id`      bigint NOT NULL COMMENT 'member id',
    `name`           varchar(256) DEFAULT '' COMMENT 'name',
    `gender`         tinyint      DEFAULT '3' COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
    `phone`          varchar(256) DEFAULT '' COMMENT 'phone format: 8613131693996',
    `email`          varchar(256) DEFAULT '' COMMENT 'email',
    `year_of_birth`  smallint     DEFAULT '0' COMMENT 'year of birth',
    `month_of_birth` tinyint      DEFAULT '0' COMMENT 'month of birth',
    `day_of_birth`   tinyint      DEFAULT '0' COMMENT 'day of birth',
    `chinese_zodiac` tinyint      DEFAULT '0' COMMENT 'chinese zodiac',
    `zodiac_sign`    tinyint      DEFAULT '0' COMMENT 'zodiac sign',
    `height`         tinyint      DEFAULT '0' COMMENT 'height/cm',
    `weight`         tinyint      DEFAULT '0' COMMENT 'weight/kg',
    `country_id`     bigint       DEFAULT '0' COMMENT 'country id',
    `country`        varchar(256) DEFAULT '' COMMENT 'country name',
    `state_id`       bigint       DEFAULT '0' COMMENT 'state id',
    `state`          varchar(256) DEFAULT '' COMMENT 'state name',
    `city_id`        bigint       DEFAULT '0' COMMENT 'city id',
    `city`           varchar(256) DEFAULT '' COMMENT 'city name',
    `address`        varchar(512) DEFAULT '' COMMENT 'address',
    `profile`        varchar(512) DEFAULT '' COMMENT 'profile',
    `hobby`          varchar(512) DEFAULT '' COMMENT 'hobby',
    `homepage`       varchar(255) DEFAULT '' COMMENT 'personal home page',
    `extra`          varchar(255) DEFAULT '' COMMENT 'extra info',
    `status`         tinyint      DEFAULT '0' COMMENT 'data status: 1-valid 0-invalid',
    `create_time`    bigint NOT NULL COMMENT 'data create time',
    `update_time`    bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY              `idx_name`(`name`) USING BTREE,
    KEY              `idx_phone`(`phone`) USING BTREE,
    KEY              `idx_email`(`email`) USING BTREE,
    KEY              `idx_country_state_city`(`country_id`,`state_id`,`city_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member detail 0';

CREATE TABLE `member_detail_1`
(
    `id`             bigint NOT NULL COMMENT 'id',
    `member_id`      bigint NOT NULL COMMENT 'member id',
    `name`           varchar(256) DEFAULT '' COMMENT 'name',
    `gender`         tinyint      DEFAULT '3' COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
    `phone`          varchar(256) DEFAULT '' COMMENT 'phone format: 8613131693996',
    `email`          varchar(256) DEFAULT '' COMMENT 'email',
    `year_of_birth`  smallint     DEFAULT '0' COMMENT 'year of birth',
    `month_of_birth` tinyint      DEFAULT '0' COMMENT 'month of birth',
    `day_of_birth`   tinyint      DEFAULT '0' COMMENT 'day of birth',
    `chinese_zodiac` tinyint      DEFAULT '0' COMMENT 'chinese zodiac',
    `zodiac_sign`    tinyint      DEFAULT '0' COMMENT 'zodiac sign',
    `height`         tinyint      DEFAULT '0' COMMENT 'height/cm',
    `weight`         tinyint      DEFAULT '0' COMMENT 'weight/kg',
    `country_id`     bigint       DEFAULT '0' COMMENT 'country id',
    `country`        varchar(256) DEFAULT '' COMMENT 'country name',
    `state_id`       bigint       DEFAULT '0' COMMENT 'state id',
    `state`          varchar(256) DEFAULT '' COMMENT 'state name',
    `city_id`        bigint       DEFAULT '0' COMMENT 'city id',
    `city`           varchar(256) DEFAULT '' COMMENT 'city name',
    `address`        varchar(512) DEFAULT '' COMMENT 'address',
    `profile`        varchar(512) DEFAULT '' COMMENT 'profile',
    `hobby`          varchar(512) DEFAULT '' COMMENT 'hobby',
    `homepage`       varchar(255) DEFAULT '' COMMENT 'personal home page',
    `extra`          varchar(255) DEFAULT '' COMMENT 'extra info',
    `status`         tinyint      DEFAULT '0' COMMENT 'data status: 1-valid 0-invalid',
    `create_time`    bigint NOT NULL COMMENT 'data create time',
    `update_time`    bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY              `idx_name`(`name`) USING BTREE,
    KEY              `idx_phone`(`phone`) USING BTREE,
    KEY              `idx_email`(`email`) USING BTREE,
    KEY              `idx_country_state_city`(`country_id`,`state_id`,`city_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member detail 1';

CREATE TABLE `undo_log_0`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 0';

CREATE TABLE `undo_log_1`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 1';


-- member1

CREATE
DATABASE member_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
member_1;

CREATE TABLE `member_basic_0`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `phone`       varchar(256) DEFAULT '' COMMENT 'phone format: 8613131693996',
    `email`       varchar(256) DEFAULT '' COMMENT 'email',
    `name`        varchar(256) DEFAULT '' COMMENT 'name',
    `icon`        varchar(256) DEFAULT '' COMMENT 'icon link',
    `qr_code`     varchar(256) DEFAULT '' COMMENT 'qrcode link',
    `gender`      tinyint      DEFAULT '3' COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
    `profile`     varchar(128) DEFAULT '' COMMENT 'profile',
    `source`      varchar(16)  DEFAULT 'APP' COMMENT 'source',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY           `idx_phone`(`phone`) USING BTREE,
    KEY           `idx_email`(`email`) USING BTREE,
    KEY           `idx_name`(`name`) USING BTREE,
    KEY           `idx_create_source`(`create_time`,`source`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member basic 0';

CREATE TABLE `member_basic_1`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `phone`       varchar(256) DEFAULT '' COMMENT 'phone format: 8613131693996',
    `email`       varchar(256) DEFAULT '' COMMENT 'email',
    `name`        varchar(256) DEFAULT '' COMMENT 'name',
    `icon`        varchar(255) DEFAULT '' COMMENT 'icon link',
    `qr_code`     varchar(256) DEFAULT '' COMMENT 'qrcode link',
    `gender`      tinyint      DEFAULT '3' COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
    `profile`     varchar(128) DEFAULT '' COMMENT 'profile',
    `source`      varchar(16)  DEFAULT 'APP' COMMENT 'source',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY           `idx_phone`(`phone`) USING BTREE,
    KEY           `idx_email`(`email`) USING BTREE,
    KEY           `idx_name`(`name`) USING BTREE,
    KEY           `idx_create_source`(`create_time`,`source`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member basic 1';

CREATE TABLE `real_name_0`
(
    `id`                bigint NOT NULL COMMENT 'id',
    `member_id`         bigint NOT NULL COMMENT 'member id',
    `real_name`         varchar(256) DEFAULT '' COMMENT 'read name',
    `gender`            tinyint      DEFAULT "3" COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
    `birthday`          varchar(32)  DEFAULT '' COMMENT 'birthday, format: yyyy-MM-dd',
    `nationality`       varchar(128) DEFAULT '' COMMENT 'nationality/country',
    `ethnic`            varchar(64)  DEFAULT '' COMMENT 'ethnic',
    `id_card_no`        varchar(128) DEFAULT '' COMMENT 'id card number',
    `residence_address` varchar(512) DEFAULT '' COMMENT 'residence address',
    `issuing_authority` varchar(512) DEFAULT '' COMMENT 'issuing authority',
    `since_date`        varchar(32)  DEFAULT '' COMMENT 'card since date, format: yyyy-MM-dd',
    `expire_date`       varchar(32)  DEFAULT '' COMMENT 'card expire date, format: yyyy-MM-dd',
    `extra`             varchar(255) DEFAULT '' COMMENT 'extra info',
    `status`            tinyint      DEFAULT '0' COMMENT 'data status: 1-valid 0-invalid',
    `create_time`       bigint NOT NULL COMMENT 'data create time',
    `update_time`       bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY                 `idx_real_name`(`real_name`) USING BTREE,
    KEY                 `idx_id_card`(`id_card_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of real name 0';

CREATE TABLE `real_name_1`
(
    `id`                bigint NOT NULL COMMENT 'id',
    `member_id`         bigint NOT NULL COMMENT 'member id',
    `real_name`         varchar(256) DEFAULT '' COMMENT 'read name',
    `gender`            tinyint      DEFAULT "3" COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
    `birthday`          varchar(32)  DEFAULT '' COMMENT 'birthday, format: yyyy-MM-dd',
    `nationality`       varchar(128) DEFAULT '' COMMENT 'nationality/country',
    `ethnic`            varchar(64)  DEFAULT '' COMMENT 'ethnic',
    `id_card_no`        varchar(128) DEFAULT '' COMMENT 'id card number',
    `residence_address` varchar(512) DEFAULT '' COMMENT 'residence address',
    `issuing_authority` varchar(512) DEFAULT '' COMMENT 'issuing authority',
    `since_date`        varchar(32)  DEFAULT '' COMMENT 'card since date, format: yyyy-MM-dd',
    `expire_date`       varchar(32)  DEFAULT '' COMMENT 'card expire date, format: yyyy-MM-dd',
    `extra`             varchar(255) DEFAULT '' COMMENT 'extra info',
    `status`            tinyint      DEFAULT '0' COMMENT 'data status: 1-valid 0-invalid',
    `create_time`       bigint NOT NULL COMMENT 'data create time',
    `update_time`       bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY                 `idx_real_name`(`real_name`) USING BTREE,
    KEY                 `idx_id_card`(`id_card_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of real name 1';

CREATE TABLE `member_detail_0`
(
    `id`             bigint NOT NULL COMMENT 'id',
    `member_id`      bigint NOT NULL COMMENT 'member id',
    `name`           varchar(256) DEFAULT '' COMMENT 'name',
    `gender`         tinyint      DEFAULT '3' COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
    `phone`          varchar(256) DEFAULT '' COMMENT 'phone format: 8613131693996',
    `email`          varchar(256) DEFAULT '' COMMENT 'email',
    `year_of_birth`  smallint     DEFAULT '0' COMMENT 'year of birth',
    `month_of_birth` tinyint      DEFAULT '0' COMMENT 'month of birth',
    `day_of_birth`   tinyint      DEFAULT '0' COMMENT 'day of birth',
    `chinese_zodiac` tinyint      DEFAULT '0' COMMENT 'chinese zodiac',
    `zodiac_sign`    tinyint      DEFAULT '0' COMMENT 'zodiac sign',
    `height`         tinyint      DEFAULT '0' COMMENT 'height/cm',
    `weight`         tinyint      DEFAULT '0' COMMENT 'weight/kg',
    `country_id`     bigint       DEFAULT '0' COMMENT 'country id',
    `country`        varchar(256) DEFAULT '' COMMENT 'country name',
    `state_id`       bigint       DEFAULT '0' COMMENT 'state id',
    `state`          varchar(256) DEFAULT '' COMMENT 'state name',
    `city_id`        bigint       DEFAULT '0' COMMENT 'city id',
    `city`           varchar(256) DEFAULT '' COMMENT 'city name',
    `address`        varchar(512) DEFAULT '' COMMENT 'address',
    `profile`        varchar(512) DEFAULT '' COMMENT 'profile',
    `hobby`          varchar(512) DEFAULT '' COMMENT 'hobby',
    `homepage`       varchar(255) DEFAULT '' COMMENT 'personal home page',
    `extra`          varchar(255) DEFAULT '' COMMENT 'extra info',
    `status`         tinyint      DEFAULT '0' COMMENT 'data status: 1-valid 0-invalid',
    `create_time`    bigint NOT NULL COMMENT 'data create time',
    `update_time`    bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY              `idx_name`(`name`) USING BTREE,
    KEY              `idx_phone`(`phone`) USING BTREE,
    KEY              `idx_email`(`email`) USING BTREE,
    KEY              `idx_country_state_city`(`country_id`,`state_id`,`city_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member detail 0';

CREATE TABLE `member_detail_1`
(
    `id`             bigint NOT NULL COMMENT 'id',
    `member_id`      bigint NOT NULL COMMENT 'member id',
    `name`           varchar(256) DEFAULT '' COMMENT 'name',
    `gender`         tinyint      DEFAULT '3' COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
    `phone`          varchar(256) DEFAULT '' COMMENT 'phone format: 8613131693996',
    `email`          varchar(256) DEFAULT '' COMMENT 'email',
    `year_of_birth`  smallint     DEFAULT '0' COMMENT 'year of birth',
    `month_of_birth` tinyint      DEFAULT '0' COMMENT 'month of birth',
    `day_of_birth`   tinyint      DEFAULT '0' COMMENT 'day of birth',
    `chinese_zodiac` tinyint      DEFAULT '0' COMMENT 'chinese zodiac',
    `zodiac_sign`    tinyint      DEFAULT '0' COMMENT 'zodiac sign',
    `height`         tinyint      DEFAULT '0' COMMENT 'height/cm',
    `weight`         tinyint      DEFAULT '0' COMMENT 'weight/kg',
    `country_id`     bigint       DEFAULT '0' COMMENT 'country id',
    `country`        varchar(256) DEFAULT '' COMMENT 'country name',
    `state_id`       bigint       DEFAULT '0' COMMENT 'state id',
    `state`          varchar(256) DEFAULT '' COMMENT 'state name',
    `city_id`        bigint       DEFAULT '0' COMMENT 'city id',
    `city`           varchar(256) DEFAULT '' COMMENT 'city name',
    `address`        varchar(512) DEFAULT '' COMMENT 'address',
    `profile`        varchar(512) DEFAULT '' COMMENT 'profile',
    `hobby`          varchar(512) DEFAULT '' COMMENT 'hobby',
    `homepage`       varchar(255) DEFAULT '' COMMENT 'personal home page',
    `extra`          varchar(255) DEFAULT '' COMMENT 'extra info',
    `status`         tinyint      DEFAULT '0' COMMENT 'data status: 1-valid 0-invalid',
    `create_time`    bigint NOT NULL COMMENT 'data create time',
    `update_time`    bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY              `idx_name`(`name`) USING BTREE,
    KEY              `idx_phone`(`phone`) USING BTREE,
    KEY              `idx_email`(`email`) USING BTREE,
    KEY              `idx_country_state_city`(`country_id`,`state_id`,`city_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member detail 1';

CREATE TABLE `undo_log_0`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 0';

CREATE TABLE `undo_log_1`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 1';


-- portal

CREATE
DATABASE portal CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
portal;


CREATE TABLE `bulletin`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `title`       varchar(128) NOT NULL COMMENT 'bulletin title',
    `content`     varchar(256) DEFAULT '' COMMENT 'bulletin content',
    `link`        varchar(256) DEFAULT '' COMMENT 'bulletin link',
    `type`        tinyint      NOT NULL COMMENT 'bulletin type: 1-popular 2-newest 3-recommend',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `priority`    int          NOT NULL COMMENT 'bulletin priority',
    `active_time` bigint       NOT NULL COMMENT 'data active time',
    `expire_time` bigint       NOT NULL COMMENT 'data expire time',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    `updater`     bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_title`(`title`) USING BTREE,
    KEY           `idx_active_expire_type_stat_pri`(`active_time`,`expire_time`,`type`,`status`,`priority`) USING BTREE,
    KEY           `idx_pri_stat`(`priority`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of bulletin';

-- portal0

CREATE
DATABASE portal_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
portal_0;

CREATE TABLE `undo_log_0`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 0';

CREATE TABLE `undo_log_1`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 1';

-- portal1

CREATE
DATABASE portal_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
portal_1;

CREATE TABLE `undo_log_0`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 0';

CREATE TABLE `undo_log_1`
(
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11) NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6) NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6) NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`branch_id`),
    UNIQUE KEY `ux_undo_log`(`xid`,`branch_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table 1';



INSERT INTO `portal`.`bulletin`(`id`, `title`, `content`, `link`, `type`, `status`, `priority`,
                                `active_time`, `expire_time`, `create_time`, `update_time`, `creator`, `updater`)
VALUES (1, 'popular bulletin 2', 'test data', 'www.baidu.com', 1, 1, 2, UNIX_TIMESTAMP() - 2678400,
        UNIX_TIMESTAMP() + 2678400, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (2, 'popular bulletin 1', 'test data', 'cn.bing.com', 1, 1, 1, UNIX_TIMESTAMP() - 2678400,
        UNIX_TIMESTAMP() + 2678400, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (3, 'newest bulletin 2', 'test data', 'www.baidu.com', 2, 1, 2, UNIX_TIMESTAMP() - 2678400,
        UNIX_TIMESTAMP() + 2678400, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (4, 'newest bulletin 1', 'test data', 'cn.bing.com', 2, 1, 1, UNIX_TIMESTAMP() - 2678400,
        UNIX_TIMESTAMP() + 2678400, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (5, 'recommend bulletin 2', 'test data', 'www.baidu.com', 3, 1, 2, UNIX_TIMESTAMP() - 2678400,
        UNIX_TIMESTAMP() + 2678400, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (6, 'recommend bulletin 1', 'test data', 'cn.bing.com', 3, 1, 1, UNIX_TIMESTAMP() - 2678400,
        UNIX_TIMESTAMP() + 2678400, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

-- article0

CREATE
DATABASE article_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
article_0;

CREATE TABLE `article_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `author_id`   bigint       NOT NULL COMMENT 'member/author id',
    `type`        tinyint      NOT NULL COMMENT 'article type: 1-tricked 2-recommendation 3-shits 4-grumble 5-share 6-water',
    `title`       varchar(256) NOT NULL COMMENT 'article title',
    `author`      varchar(256) NOT NULL COMMENT 'author id',
    `content`     mediumtext   NOT NULL COMMENT 'article content',
    `favorites`   bigint DEFAULT '0' COMMENT 'favorites count',
    `readings`    bigint DEFAULT '0' COMMENT 'readings count',
    `comments`    bigint DEFAULT '0' COMMENT 'comments count',
    `likes`       bigint DEFAULT '0' COMMENT 'likes count',
    `boring`      bigint DEFAULT '0' COMMENT 'boring count',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY           `idx_au_type_status_create`(`author_id`,`type`,`status`,`create_time`) USING BTREE,
    KEY           `idx_favorites`(`favorites`) USING BTREE,
    KEY           `idx_readings`(`readings`) USING BTREE,
    KEY           `idx_comments`(`comments`) USING BTREE,
    KEY           `idx_likes`(`likes`) USING BTREE,
    KEY           `idx_boring`(`boring`) USING BTREE,
    KEY           `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of article 0';


CREATE TABLE `article_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `author_id`   bigint       NOT NULL COMMENT 'member/author id',
    `type`        tinyint      NOT NULL COMMENT 'article type: 1-tricked 2-recommendation 3-shits 4-grumble 5-share 6-water',
    `title`       varchar(256) NOT NULL COMMENT 'article title',
    `author`      varchar(256) NOT NULL COMMENT 'author id',
    `content`     mediumtext   NOT NULL COMMENT 'article content',
    `favorites`   bigint DEFAULT '0' COMMENT 'favorites count',
    `readings`    bigint DEFAULT '0' COMMENT 'readings count',
    `comments`    bigint DEFAULT '0' COMMENT 'comments count',
    `likes`       bigint DEFAULT '0' COMMENT 'likes count',
    `boring`      bigint DEFAULT '0' COMMENT 'boring count',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY           `idx_au_type_status_create`(`author_id`,`type`,`status`,`create_time`) USING BTREE,
    KEY           `idx_favorites`(`favorites`) USING BTREE,
    KEY           `idx_readings`(`readings`) USING BTREE,
    KEY           `idx_comments`(`comments`) USING BTREE,
    KEY           `idx_likes`(`likes`) USING BTREE,
    KEY           `idx_boring`(`boring`) USING BTREE,
    KEY           `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of article 1';



CREATE TABLE `link_0`
(
    `id`            bigint        NOT NULL COMMENT 'id',
    `sub_id`        bigint        NOT NULL COMMENT 'subject(article/comment/link/reply) id',
    `sub_type`      tinyint       NOT NULL COMMENT 'subject type: 1-article 2-comment 3-link 4-reply',
    `sub_author_id` bigint        NOT NULL COMMENT 'subject author id',
    `link_url`      varchar(1024) NOT NULL COMMENT 'link url',
    `content`       varchar(1024) NOT NULL COMMENT 'link content',
    `priority`      int           NOT NULL COMMENT 'link priority',
    `favorites`     bigint DEFAULT '0' COMMENT 'favorites count',
    `readings`      bigint DEFAULT '0' COMMENT 'readings count',
    `comments`      bigint DEFAULT '0' COMMENT 'comments count',
    `likes`         bigint DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint DEFAULT '0' COMMENT 'boring count',
    `status`        tinyint       NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`   bigint        NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype_status`(`sub_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_readings`(`readings`) USING BTREE,
    KEY             `idx_comments`(`comments`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of link 0';


CREATE TABLE `link_1`
(
    `id`            bigint        NOT NULL COMMENT 'id',
    `sub_id`        bigint        NOT NULL COMMENT 'subject(article/comment/link/reply) id',
    `sub_type`      tinyint       NOT NULL COMMENT 'subject type: 1-article 2-comment 3-link 4-reply',
    `sub_author_id` bigint        NOT NULL COMMENT 'subject author id',
    `link_url`      varchar(1024) NOT NULL COMMENT 'link url',
    `content`       varchar(1024) NOT NULL COMMENT 'link content',
    `priority`      int           NOT NULL COMMENT 'link priority',
    `favorites`     bigint DEFAULT '0' COMMENT 'favorites count',
    `readings`      bigint DEFAULT '0' COMMENT 'readings count',
    `comments`      bigint DEFAULT '0' COMMENT 'comments count',
    `likes`         bigint DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint DEFAULT '0' COMMENT 'boring count',
    `status`        tinyint       NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`   bigint        NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype_status`(`sub_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_readings`(`readings`) USING BTREE,
    KEY             `idx_comments`(`comments`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of link 1';



CREATE TABLE `comment_0`
(
    `id`            bigint     NOT NULL COMMENT 'id',
    `sub_id`        bigint     NOT NULL COMMENT 'subject(article/comment/link/reply) id',
    `sub_type`      tinyint    NOT NULL COMMENT 'subject type: 1-article 2-comment 3-link 4-reply',
    `sub_author_id` bigint     NOT NULL COMMENT 'subject author id',
    `from_id`       bigint     NOT NULL COMMENT 'commenter id',
    `content`       mediumtext NOT NULL COMMENT 'comment content',
    `favorites`     bigint DEFAULT '0' COMMENT 'favorites count',
    `replies`       bigint DEFAULT '0' COMMENT 'replies count',
    `likes`         bigint DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint DEFAULT '0' COMMENT 'boring count',
    `status`        tinyint    NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`   bigint     NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype`(`sub_id`,`sub_type`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_replies`(`replies`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE,
    KEY             `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reply 0';

CREATE TABLE `comment_1`
(
    `id`            bigint     NOT NULL COMMENT 'id',
    `sub_id`        bigint     NOT NULL COMMENT 'subject(article/comment/link/reply) id',
    `sub_type`      tinyint    NOT NULL COMMENT 'subject type: 1-article 2-comment 3-link 4-reply',
    `sub_author_id` bigint     NOT NULL COMMENT 'subject author id',
    `from_id`       bigint     NOT NULL COMMENT 'commenter id',
    `content`       mediumtext NOT NULL COMMENT 'comment content',
    `favorites`     bigint DEFAULT '0' COMMENT 'favorites count',
    `replies`       bigint DEFAULT '0' COMMENT 'replies count',
    `likes`         bigint DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint DEFAULT '0' COMMENT 'boring count',
    `status`        tinyint    NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`   bigint     NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype`(`sub_id`,`sub_type`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_replies`(`replies`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE,
    KEY             `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reply 1';



CREATE TABLE `reply_0`
(
    `id`            bigint     NOT NULL COMMENT 'id',
    `sub_id`        bigint     NOT NULL COMMENT 'subject(article/comment/link/reply) id',
    `sub_type`      tinyint    NOT NULL COMMENT 'subject type: 1-article 2-comment 3-link 4-reply',
    `sub_author_id` bigint     NOT NULL COMMENT 'subject author id',
    `comment_id`    bigint     NOT NULL COMMENT 'comment id',
    `from_id`       bigint     NOT NULL COMMENT 'replier id',
    `to_id`         bigint     NOT NULL COMMENT 'reply of comment/reply id',
    `type`          tinyint    NOT NULL COMMENT 'reply type: 1-reply to comment 2-reply to other reply',
    `content`       mediumtext NOT NULL COMMENT 'reply content',
    `favorites`     bigint DEFAULT '0' COMMENT 'favorites count',
    `likes`         bigint DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint DEFAULT '0' COMMENT 'boring count',
    `status`        tinyint    NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`   bigint     NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype_status`(`sub_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_comment_status`(`comment_id`,`status`) USING BTREE,
    KEY             `idx_from_to_type_status`(`from_id`,`to_id`,`type`,`status`) USING BTREE,
    KEY             `idx_to_from_typt_status`(`to_id`,`from_id`,`type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE,
    KEY             `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reply 0';

CREATE TABLE `reply_1`
(
    `id`            bigint     NOT NULL COMMENT 'id',
    `sub_id`        bigint     NOT NULL COMMENT 'subject(article/comment/link/reply) id',
    `sub_type`      tinyint    NOT NULL COMMENT 'subject type: 1-article 2-comment 3-link 4-reply',
    `sub_author_id` bigint     NOT NULL COMMENT 'subject author id',
    `comment_id`    bigint     NOT NULL COMMENT 'comment id',
    `from_id`       bigint     NOT NULL COMMENT 'replier id',
    `to_id`         bigint     NOT NULL COMMENT 'reply of comment/reply id',
    `type`          tinyint    NOT NULL COMMENT 'reply type: 1-reply to comment 2-reply to other reply',
    `content`       mediumtext NOT NULL COMMENT 'reply content',
    `favorites`     bigint DEFAULT '0' COMMENT 'favorites count',
    `likes`         bigint DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint DEFAULT '0' COMMENT 'boring count',
    `status`        tinyint    NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`   bigint     NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype_status`(`sub_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_comment_status`(`comment_id`,`status`) USING BTREE,
    KEY             `idx_from_to_type_status`(`from_id`,`to_id`,`type`,`status`) USING BTREE,
    KEY             `idx_to_from_typt_status`(`to_id`,`from_id`,`type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE,
    KEY             `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reply 1';


-- article1

CREATE
DATABASE article_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
article_1;

CREATE TABLE `article_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `author_id`   bigint       NOT NULL COMMENT 'member/author id',
    `type`        tinyint      NOT NULL COMMENT 'article type: 1-tricked 2-recommendation 3-shits 4-grumble 5-share 6-water',
    `title`       varchar(256) NOT NULL COMMENT 'article title',
    `author`      varchar(256) NOT NULL COMMENT 'author id',
    `content`     mediumtext   NOT NULL COMMENT 'article content',
    `favorites`   bigint DEFAULT '0' COMMENT 'favorites count',
    `readings`    bigint DEFAULT '0' COMMENT 'readings count',
    `comments`    bigint DEFAULT '0' COMMENT 'comments count',
    `likes`       bigint DEFAULT '0' COMMENT 'likes count',
    `boring`      bigint DEFAULT '0' COMMENT 'boring count',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY           `idx_au_type_status_create`(`author_id`,`type`,`status`,`create_time`) USING BTREE,
    KEY           `idx_favorites`(`favorites`) USING BTREE,
    KEY           `idx_readings`(`readings`) USING BTREE,
    KEY           `idx_comments`(`comments`) USING BTREE,
    KEY           `idx_likes`(`likes`) USING BTREE,
    KEY           `idx_boring`(`boring`) USING BTREE,
    KEY           `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of article 0';

CREATE TABLE `article_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `author_id`   bigint       NOT NULL COMMENT 'member/author id',
    `type`        tinyint      NOT NULL COMMENT 'article type: 1-tricked 2-recommendation 3-shits 4-grumble 5-share 6-water',
    `title`       varchar(256) NOT NULL COMMENT 'article title',
    `author`      varchar(256) NOT NULL COMMENT 'author id',
    `content`     mediumtext   NOT NULL COMMENT 'article content',
    `favorites`   bigint DEFAULT '0' COMMENT 'favorites count',
    `readings`    bigint DEFAULT '0' COMMENT 'readings count',
    `comments`    bigint DEFAULT '0' COMMENT 'comments count',
    `likes`       bigint DEFAULT '0' COMMENT 'likes count',
    `boring`      bigint DEFAULT '0' COMMENT 'boring count',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY           `idx_au_type_status_create`(`author_id`,`type`,`status`,`create_time`) USING BTREE,
    KEY           `idx_favorites`(`favorites`) USING BTREE,
    KEY           `idx_readings`(`readings`) USING BTREE,
    KEY           `idx_comments`(`comments`) USING BTREE,
    KEY           `idx_likes`(`likes`) USING BTREE,
    KEY           `idx_boring`(`boring`) USING BTREE,
    KEY           `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of article 1';

CREATE TABLE `link_0`
(
    `id`            bigint        NOT NULL COMMENT 'id',
    `sub_id`        bigint        NOT NULL COMMENT 'subject(article/comment/link/reply) id',
    `sub_type`      tinyint       NOT NULL COMMENT 'subject type: 1-article 2-comment 3-link 4-reply',
    `sub_author_id` bigint        NOT NULL COMMENT 'subject author id',
    `link_url`      varchar(1024) NOT NULL COMMENT 'link url',
    `content`       varchar(1024) NOT NULL COMMENT 'link content',
    `priority`      int           NOT NULL COMMENT 'link priority',
    `favorites`     bigint DEFAULT '0' COMMENT 'favorites count',
    `readings`      bigint DEFAULT '0' COMMENT 'readings count',
    `comments`      bigint DEFAULT '0' COMMENT 'comments count',
    `likes`         bigint DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint DEFAULT '0' COMMENT 'boring count',
    `status`        tinyint       NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`   bigint        NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype_status`(`sub_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_readings`(`readings`) USING BTREE,
    KEY             `idx_comments`(`comments`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of link 0';

CREATE TABLE `link_1`
(
    `id`            bigint        NOT NULL COMMENT 'id',
    `sub_id`        bigint        NOT NULL COMMENT 'subject(article/comment/link/reply) id',
    `sub_type`      tinyint       NOT NULL COMMENT 'subject type: 1-article 2-comment 3-link 4-reply',
    `sub_author_id` bigint        NOT NULL COMMENT 'subject author id',
    `link_url`      varchar(1024) NOT NULL COMMENT 'link url',
    `content`       varchar(1024) NOT NULL COMMENT 'link content',
    `priority`      int           NOT NULL COMMENT 'link priority',
    `favorites`     bigint DEFAULT '0' COMMENT 'favorites count',
    `readings`      bigint DEFAULT '0' COMMENT 'readings count',
    `comments`      bigint DEFAULT '0' COMMENT 'comments count',
    `likes`         bigint DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint DEFAULT '0' COMMENT 'boring count',
    `status`        tinyint       NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`   bigint        NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype_status`(`sub_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_readings`(`readings`) USING BTREE,
    KEY             `idx_comments`(`comments`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of link 1';

CREATE TABLE `comment_0`
(
    `id`            bigint     NOT NULL COMMENT 'id',
    `sub_id`        bigint     NOT NULL COMMENT 'subject(article/comment/link/reply) id',
    `sub_type`      tinyint    NOT NULL COMMENT 'subject type: 1-article 2-comment 3-link 4-reply',
    `sub_author_id` bigint     NOT NULL COMMENT 'subject author id',
    `from_id`       bigint     NOT NULL COMMENT 'commenter id',
    `content`       mediumtext NOT NULL COMMENT 'comment content',
    `favorites`     bigint DEFAULT '0' COMMENT 'favorites count',
    `replies`       bigint DEFAULT '0' COMMENT 'replies count',
    `likes`         bigint DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint DEFAULT '0' COMMENT 'boring count',
    `status`        tinyint    NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`   bigint     NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype`(`sub_id`,`sub_type`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_replies`(`replies`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE,
    KEY             `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reply 0';

CREATE TABLE `comment_1`
(
    `id`            bigint     NOT NULL COMMENT 'id',
    `sub_id`        bigint     NOT NULL COMMENT 'subject(article/comment/link/reply) id',
    `sub_type`      tinyint    NOT NULL COMMENT 'subject type: 1-article 2-comment 3-link 4-reply',
    `sub_author_id` bigint     NOT NULL COMMENT 'subject author id',
    `from_id`       bigint     NOT NULL COMMENT 'commenter id',
    `content`       mediumtext NOT NULL COMMENT 'comment content',
    `favorites`     bigint DEFAULT '0' COMMENT 'favorites count',
    `replies`       bigint DEFAULT '0' COMMENT 'replies count',
    `likes`         bigint DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint DEFAULT '0' COMMENT 'boring count',
    `status`        tinyint    NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`   bigint     NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype`(`sub_id`,`sub_type`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_replies`(`replies`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE,
    KEY             `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reply 1';

CREATE TABLE `reply_0`
(
    `id`            bigint     NOT NULL COMMENT 'id',
    `sub_id`        bigint     NOT NULL COMMENT 'subject(article/comment/link/reply) id',
    `sub_type`      tinyint    NOT NULL COMMENT 'subject type: 1-article 2-comment 3-link 4-reply',
    `sub_author_id` bigint     NOT NULL COMMENT 'subject author id',
    `comment_id`    bigint     NOT NULL COMMENT 'comment id',
    `from_id`       bigint     NOT NULL COMMENT 'replier id',
    `to_id`         bigint     NOT NULL COMMENT 'reply of comment/reply id',
    `type`          tinyint    NOT NULL COMMENT 'reply type: 1-reply to comment 2-reply to other reply',
    `content`       mediumtext NOT NULL COMMENT 'reply content',
    `favorites`     bigint DEFAULT '0' COMMENT 'favorites count',
    `likes`         bigint DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint DEFAULT '0' COMMENT 'boring count',
    `status`        tinyint    NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`   bigint     NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype_status`(`sub_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_comment_status`(`comment_id`,`status`) USING BTREE,
    KEY             `idx_from_to_type_status`(`from_id`,`to_id`,`type`,`status`) USING BTREE,
    KEY             `idx_to_from_typt_status`(`to_id`,`from_id`,`type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE,
    KEY             `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reply 0';

CREATE TABLE `reply_1`
(
    `id`            bigint     NOT NULL COMMENT 'id',
    `sub_id`        bigint     NOT NULL COMMENT 'subject(article/comment/link/reply) id',
    `sub_type`      tinyint    NOT NULL COMMENT 'subject type: 1-article 2-comment 3-link 4-reply',
    `sub_author_id` bigint     NOT NULL COMMENT 'subject author id',
    `comment_id`    bigint     NOT NULL COMMENT 'comment id',
    `from_id`       bigint     NOT NULL COMMENT 'replier id',
    `to_id`         bigint     NOT NULL COMMENT 'reply of comment/reply id',
    `type`          tinyint    NOT NULL COMMENT 'reply type: 1-reply to comment 2-reply to other reply',
    `content`       mediumtext NOT NULL COMMENT 'reply content',
    `favorites`     bigint DEFAULT '0' COMMENT 'favorites count',
    `likes`         bigint DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint DEFAULT '0' COMMENT 'boring count',
    `status`        tinyint    NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time`   bigint     NOT NULL COMMENT 'data create time',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype_status`(`sub_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_comment_status`(`comment_id`,`status`) USING BTREE,
    KEY             `idx_from_to_type_status`(`from_id`,`to_id`,`type`,`status`) USING BTREE,
    KEY             `idx_to_from_typt_status`(`to_id`,`from_id`,`type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE,
    KEY             `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reply 1';

-- base

CREATE
DATABASE base CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
base;

CREATE TABLE `style`
(
    `id`          bigint        NOT NULL COMMENT 'id',
    `name`        varchar(128)  NOT NULL COMMENT 'style name',
    `attributes`  varchar(8192) NOT NULL COMMENT 'style attrtbutes',
    `type`        tinyint       NOT NULL COMMENT 'style type: 1-a 2-b 3-c',
    `is_active`   bit           NOT NULL COMMENT 'is active style? 1-yes 0-no',
    `status`      tinyint       NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint        NOT NULL COMMENT 'data create time',
    `update_time` bigint        NOT NULL COMMENT 'data update time',
    `creator`     bigint        NOT NULL COMMENT 'creator id',
    `updater`     bigint        NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name`(`name`) USING BTREE,
    KEY           `idx_type_active_create`(`type`,`is_active`,`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of style';

INSERT INTO `base`.`style`(`id`, `name`, `attributes`, `type`, `is_active`, `status`, `create_time`, `update_time`,
                           `creator`, `updater`)
VALUES (1, 'blue1', '{}', 1, b'1', 1, 1, 1, 1, 1),
       (2, 'blue2', '{}', 2, b'1', 1, 1, 1, 1, 1),
       (3, 'blue3', '{}', 3, b'1', 1, 1, 1, 1, 1),

       (4, 'blue4', '{}', 1, b'0', 1, 1, 1, 1, 1),
       (5, 'blue5', '{}', 2, b'0', 1, 1, 1, 1, 1),
       (6, 'blue6', '{}', 3, b'0', 1, 1, 1, 1, 1);




