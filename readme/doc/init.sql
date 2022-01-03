-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- secure0

CREATE
DATABASE secure_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
secure_0;

CREATE TABLE `resource_0`
(
    `id`                      bigint       NOT NULL COMMENT 'id',
    `request_method`          varchar(16)  NOT NULL COMMENT 'request method/upper',
    `module`                  varchar(256) NOT NULL COMMENT 'module/service',
    `uri`                     varchar(256) NOT NULL COMMENT 'resource uri/lower',
    `authenticate`            bit          NOT NULL COMMENT 'need auth? 1-yes 2-no',
    `request_un_decryption`   bit          NOT NULL COMMENT 'do not decrypt request body? 1-not 0-yes',
    `response_un_encryption`  bit          NOT NULL COMMENT 'do not encrypt response body? 1-not 0-yes',
    `existence_request_body`  bit          NOT NULL COMMENT 'has request body? 1-yes 2-no',
    `existence_response_body` bit          NOT NULL COMMENT 'has response body? 1-yes 2-no',
    `type`                    tinyint      NOT NULL COMMENT 'resource type, 1-api 2-manage api 3-open api',
    `name`                    varchar(128) DEFAULT NULL COMMENT 'resource name',
    `description`             varchar(256) DEFAULT NULL COMMENT 'resource disc',
    `create_time`             bigint       NOT NULL COMMENT 'data create time',
    `update_time`             bigint       NOT NULL COMMENT 'data update time',
    `creator`                 bigint       NOT NULL COMMENT 'creator id',
    `updater`                 bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_method_module_uri`(`request_method`,`module`,`uri`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of resource 0';

CREATE TABLE `resource_1`
(
    `id`                      bigint       NOT NULL COMMENT 'id',
    `request_method`          varchar(16)  NOT NULL COMMENT 'request method/upper',
    `module`                  varchar(256) NOT NULL COMMENT 'module/service',
    `uri`                     varchar(256) NOT NULL COMMENT 'resource uri/lower',
    `authenticate`            bit          NOT NULL COMMENT 'need auth? 1-yes 2-no',
    `request_un_decryption`   bit          NOT NULL COMMENT 'do not decrypt request body? 1-not 0-yes',
    `response_un_encryption`  bit          NOT NULL COMMENT 'do not encrypt response body? 1-not 0-yes',
    `existence_request_body`  bit          NOT NULL COMMENT 'has request body? 1-yes 2-no',
    `existence_response_body` bit          NOT NULL COMMENT 'has response body? 1-yes 2-no',
    `type`                    tinyint      NOT NULL COMMENT 'resource type, 1-api 2-manage api 3-open api',
    `name`                    varchar(128) DEFAULT NULL COMMENT 'resource name',
    `description`             varchar(256) DEFAULT NULL COMMENT 'resource disc',
    `create_time`             bigint       NOT NULL COMMENT 'data create time',
    `update_time`             bigint       NOT NULL COMMENT 'data update time',
    `creator`                 bigint       NOT NULL COMMENT 'creator id',
    `updater`                 bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_method_module_uri`(`request_method`,`module`,`uri`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of resource 1';

CREATE TABLE `role_0`
(
    `id`          bigint      NOT NULL COMMENT 'id',
    `name`        varchar(64) NOT NULL COMMENT 'role name',
    `description` varchar(128) DEFAULT NULL COMMENT 'role disc',
    `level`       int         NOT NULL COMMENT 'roles level',
    `is_default`  bit         NOT NULL COMMENT 'is default role? 1-yes 0-no',
    `create_time` bigint      NOT NULL COMMENT 'data create time',
    `update_time` bigint      NOT NULL COMMENT 'data update time',
    `creator`     bigint      NOT NULL COMMENT 'creator id',
    `updater`     bigint      NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_level`(`level`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of role 0';

CREATE TABLE `role_1`
(
    `id`          bigint      NOT NULL COMMENT 'id',
    `name`        varchar(64) NOT NULL COMMENT 'role name',
    `description` varchar(128) DEFAULT NULL COMMENT 'role disc',
    `level`       int         NOT NULL COMMENT 'roles level',
    `is_default`  bit         NOT NULL COMMENT 'is default role? 1-yes 0-no',
    `create_time` bigint      NOT NULL COMMENT 'data create time',
    `update_time` bigint      NOT NULL COMMENT 'data update time',
    `creator`     bigint      NOT NULL COMMENT 'creator id',
    `updater`     bigint      NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_level`(`level`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of role 1';

CREATE TABLE `role_res_relation_0`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of role and resource relation 0';

CREATE TABLE `role_res_relation_1`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of role and resource relation 1';

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

-- seata undo log

CREATE TABLE `undo_log`
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';

-- secure1

CREATE
DATABASE secure_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
secure_1;

CREATE TABLE `resource_0`
(
    `id`                      bigint       NOT NULL COMMENT 'id',
    `request_method`          varchar(16)  NOT NULL COMMENT 'request method/upper',
    `module`                  varchar(256) NOT NULL COMMENT 'module/service',
    `uri`                     varchar(256) NOT NULL COMMENT 'resource uri/lower',
    `authenticate`            bit          NOT NULL COMMENT 'need auth? 1-yes 2-no',
    `request_un_decryption`   bit          NOT NULL COMMENT 'do not decrypt request body? 1-not 0-yes',
    `response_un_encryption`  bit          NOT NULL COMMENT 'do not encrypt response body? 1-not 0-yes',
    `existence_request_body`  bit          NOT NULL COMMENT 'has request body? 1-yes 2-no',
    `existence_response_body` bit          NOT NULL COMMENT 'has response body? 1-yes 2-no',
    `type`                    tinyint      NOT NULL COMMENT 'resource type, 1-api 2-manage api 3-open api',
    `name`                    varchar(128) DEFAULT NULL COMMENT 'resource name',
    `description`             varchar(256) DEFAULT NULL COMMENT 'resource disc',
    `create_time`             bigint       NOT NULL COMMENT 'data create time',
    `update_time`             bigint       NOT NULL COMMENT 'data update time',
    `creator`                 bigint       NOT NULL COMMENT 'creator id',
    `updater`                 bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_method_module_uri`(`request_method`,`module`,`uri`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of resource 0';

CREATE TABLE `resource_1`
(
    `id`                      bigint       NOT NULL COMMENT 'id',
    `request_method`          varchar(16)  NOT NULL COMMENT 'request method/upper',
    `module`                  varchar(256) NOT NULL COMMENT 'module/service',
    `uri`                     varchar(256) NOT NULL COMMENT 'resource uri/lower',
    `authenticate`            bit          NOT NULL COMMENT 'need auth? 1-yes 2-no',
    `request_un_decryption`   bit          NOT NULL COMMENT 'do not decrypt request body? 1-not 0-yes',
    `response_un_encryption`  bit          NOT NULL COMMENT 'do not encrypt response body? 1-not 0-yes',
    `existence_request_body`  bit          NOT NULL COMMENT 'has request body? 1-yes 2-no',
    `existence_response_body` bit          NOT NULL COMMENT 'has response body? 1-yes 2-no',
    `type`                    tinyint      NOT NULL COMMENT 'resource type, 1-api 2-manage api 3-open api',
    `name`                    varchar(128) DEFAULT NULL COMMENT 'resource name',
    `description`             varchar(256) DEFAULT NULL COMMENT 'resource disc',
    `create_time`             bigint       NOT NULL COMMENT 'data create time',
    `update_time`             bigint       NOT NULL COMMENT 'data update time',
    `creator`                 bigint       NOT NULL COMMENT 'creator id',
    `updater`                 bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_method_module_uri`(`request_method`,`module`,`uri`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of resource 1';

CREATE TABLE `role_0`
(
    `id`          bigint      NOT NULL COMMENT 'id',
    `name`        varchar(64) NOT NULL COMMENT 'role name',
    `description` varchar(128) DEFAULT NULL COMMENT 'role disc',
    `level`       int         NOT NULL COMMENT 'roles level',
    `is_default`  bit         NOT NULL COMMENT 'is default role? 1-yes 0-no',
    `create_time` bigint      NOT NULL COMMENT 'data create time',
    `update_time` bigint      NOT NULL COMMENT 'data update time',
    `creator`     bigint      NOT NULL COMMENT 'creator id',
    `updater`     bigint      NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_level`(`level`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of role 0';

CREATE TABLE `role_1`
(
    `id`          bigint      NOT NULL COMMENT 'id',
    `name`        varchar(64) NOT NULL COMMENT 'role name',
    `description` varchar(128) DEFAULT NULL COMMENT 'role disc',
    `level`       int         NOT NULL COMMENT 'roles level',
    `is_default`  bit         NOT NULL COMMENT 'is default role? 1-yes 0-no',
    `create_time` bigint      NOT NULL COMMENT 'data create time',
    `update_time` bigint      NOT NULL COMMENT 'data update time',
    `creator`     bigint      NOT NULL COMMENT 'creator id',
    `updater`     bigint      NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_level`(`level`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of role 1';

CREATE TABLE `role_res_relation_0`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of role and resource relation 0';

CREATE TABLE `role_res_relation_1`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of role and resource relation 1';

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

-- seata undo log

CREATE TABLE `undo_log`
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';

-- init
-- resources

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9505726846205953, 'POST', 'blue-secure', '/auth/loginByAcctAndPwd', b'0', b'1', b'1', b'1', b'1', 1,
        'login by account and pwd', 'login by account and pwd', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506121983229953, 'PUT', 'blue-secure', '/auth/updateSecret', b'1', b'1', b'1', b'0', b'1', 1,
        'refresh private key', 'refresh private key', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506477400129537, 'GET', 'blue-secure', '/auth/authority', b'1', b'1', b'1', b'0', b'1', 1,
        'query authority', 'query authority', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506557930799110, 'GET', 'blue-member', '/member', b'1', b'1', b'1', b'0', b'1', 1,
        'member info', 'member info', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9505726846205957, 'DELETE', 'blue-secure', '/auth/logout', b'1', b'1', b'1', b'0', b'1', 1,
        'logout', 'logout', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506121983229954, 'GET', 'blue-portal', '/fallBack', b'0', b'1', b'1', b'0', b'1', 1,
        'GET fallback', 'GET fallback', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506477400129538, 'POST', 'blue-portal', '/fallBack', b'0', b'1', b'1', b'1', b'1', 1,
        'POST fallback', 'POST fallback', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506557930799111, 'GET', 'blue-portal', '/bulletin/{type}', b'0', b'1', b'1', b'0', b'1', 1,
        'bulletin list of api', 'bulletin list of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);


INSERT INTO `secure_0`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506121983229955, 'POST', 'blue-file', '/file/upload', b'1', b'1', b'1', b'1', b'1', 1,
        'file upload of api', 'file upload of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506477400129539, 'POST', 'blue-file', '/file/download', b'1', b'1', b'1', b'1', b'0', 1,
        'file download of api', 'file download of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506557930799112, 'POST', 'blue-file', '/attachment/list', b'1', b'1', b'1', b'1', b'1', 1,
        'attachment list of api', 'attachment list of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9505726846205965, 'POST', 'blue-marketing', '/signIn', b'1', b'1', b'1', b'0', b'1', 1,
        'sign in', 'sign in', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506121983229956, 'GET', 'blue-marketing', '/signIn', b'1', b'1', b'1', b'0', b'1', 1,
        'query sign in record by month', 'query sign in record by month', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506477400129540, 'GET', 'blue-file', '/file/downloadTest/{id}', b'1', b'1', b'1', b'1', b'0', 1,
        'download test', 'download test', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506557930799113, 'GET', 'blue-finance', '/finance/balance', b'1', b'1', b'1', b'0', b'1', 1,
        'query balance', 'query balance', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9505726846205969, 'POST', 'blue-member', '/member/registry', b'0', b'1', b'1', b'1', b'1', 1,
        'member registry', 'member registry', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    `response_un_encryption`, `existence_request_body`, `existence_response_body`,
                                    `type`,
                                    `name`, `description`, `create_time`, `update_time`, `creator`, `updater`)
VALUES (9505726846205970, 'GET', 'blue-portal', '/formatter/{formatter}.html', b'1', b'1', b'1', b'0', b'1', 1,
        'formatter test', 'formatter test', 1629253160, 1629253160, 1, 1);

INSERT INTO `secure_0`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506121983229957, 'POST', 'blue-finance', '/withdraw', b'1', b'0', b'0', 1, b'1', b'1',
        'withdraw/test encrypt', 'withdraw/test encrypt', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506477400129541, 'POST', 'blue-file', '/attachment/withdraw', b'1', b'0', b'0', b'1', b'1', 1,
        'withdraw/test encrypt', 'withdraw/test encrypt', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506557930799114, 'GET', 'blue-shine', '/shine', b'0', b'1', b'1', b'0', b'1', 1,
        'commonweal information', 'commonweal information', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (14978349487128577, 'GET', 'blue-base', '/dictType', b'0', b'1', b'1', b'0',
        b'1', 1,
        'query dict types', 'query dict types', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (19029083459420161, 'GET', 'blue-base', '/bulletin/{type}', b'0', b'1', b'1', b'0', b'1', 1,
        'test endpoint', 'test endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (19028988970172419, 'POST', 'blue-base', '/bulletin/{type}', b'0', b'1', b'1', b'0', b'1', 1,
        'test endpoint', 'test endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

-- dynamic resources

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151489, 'GET', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3,
        'GET dynamic endpoint', 'GET dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151490, 'HEAD', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3,
        'HEAD dynamic endpoint', 'HEAD dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151491, 'POST', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3,
        'POST dynamic endpoint', 'POST dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151492, 'PUT', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3,
        'PUT dynamic endpoint', 'PUT dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151493, 'PATCH', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3,
        'PATCH dynamic endpoint', 'PATCH dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151495, 'DELETE', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1',
        b'1', 3,
        'DELETE dynamic endpoint', 'DELETE dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151496, 'OPTIONS', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1',
        b'1', 3,
        'OPTIONS dynamic endpoint', 'OPTIONS dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

-- manage resources

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (17727588109647875, 'POST', 'blue-secure', '/manager/resource/list', b'1', b'1', b'1', b'1', b'1', 2,
        'resource list', 'resource list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (17727698705088515, 'POST', 'blue-secure', '/manager/role/list', b'1', b'1', b'1', b'1', b'1', 2,
        'role list', 'role list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (17727756695502851, 'POST', 'blue-member', '/manager/member/list', b'1', b'1', b'1', b'1', b'1', 2,
        'member list', 'member list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (17727776022888453, 'POST', 'blue-member', '/manager/authority/list', b'1', b'1', b'1', b'1', b'1', 2,
        'authority list', 'authority list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (18497721916719105, 'POST', 'blue-secure', '/manager/role', b'1', b'1', b'1', b'1', b'1', 2,
        'insert role', 'insert role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (18497740170362881, 'PUT', 'blue-secure', '/manager/role', b'1', b'1', b'1', b'1', b'1', 2,
        'update role', 'update role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (19026881223327750, 'DELETE', 'blue-secure', '/manager/role/{id}', b'1', b'1', b'1', b'0', b'1', 2,
        'delete role', 'delete role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (18497811045679105, 'POST', 'blue-secure', '/manager/resource', b'1', b'1', b'1', b'1', b'1', 2,
        'insert resource', 'insert resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (18497870101512195, 'PUT', 'blue-secure', '/manager/resource', b'1', b'1', b'1', b'1', b'1', 2,
        'update resource', 'update resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (19027366546243585, 'DELETE', 'blue-secure', '/manager/resource/{id}', b'1', b'1', b'1', b'0', b'1', 2,
        'delete resource', 'delete resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (18500844357943298, 'POST', 'blue-secure', '/manager/role/auth', b'1', b'1', b'1', b'1', b'1', 2,
        'role auth', 'role auth', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (18501349016633350, 'POST', 'blue-secure', '/manager/resource/auth', b'1', b'1', b'1', b'1', b'1', 2,
        'resource auth', 'resource auth', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (19028252391636995, 'PUT', 'blue-secure', '/manager/relation/role-res', b'1', b'1', b'1', b'1', b'1', 2,
        'update role-resources-relation', 'update role-resources-relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                                    response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (19028471435001859, 'PUT', 'blue-secure', '/manager/relation/mem-role', b'1', b'1', b'1', b'1', b'1', 2,
        'update member-role-relation', 'update member-role-relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

-- role

INSERT INTO `secure_1`.`role_1`(`id`, `name`, `description`, `level`, `is_default`, `create_time`, `update_time`,
                                `creator`,
                                `updater`)
VALUES (9507591944175638, 'normal', 'normal', 999999999, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_0`(`id`, `name`, `description`, `level`, `is_default`, `create_time`, `update_time`,
                                `creator`,
                                `updater`)
VALUES (17558421159018501, 'summer admin', 'summer admin', 0, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

-- role resource relations

-- admin auth

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282820, 17558421159018501, 9505726846205953, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349900, 17558421159018501, 9506121983229953, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120540, 17558421159018501, 9506477400129537, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175640, 17558421159018501, 9506557930799110, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282821, 17558421159018501, 9505726846205957, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349901, 17558421159018501, 9506121983229954, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120541, 17558421159018501, 9506477400129538, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175641, 17558421159018501, 9506557930799111, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282822, 17558421159018501, 9505726846205961, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349902, 17558421159018501, 9506121983229955, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120542, 17558421159018501, 9506477400129539, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175642, 17558421159018501, 9506557930799112, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282825, 17558421159018501, 9505726846205965, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349903, 17558421159018501, 9506121983229956, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120543, 17558421159018501, 9506477400129540, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175643, 17558421159018501, 9506557930799113, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282829, 17558421159018501, 9505726846205969, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349904, 17558421159018501, 9506121983229957, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120544, 17558421159018501, 9506477400129541, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175644, 17558421159018501, 9506557930799114, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282827, 17558421159018501, 9505726846205973, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282828, 17558421159018501, 9505726846205970, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (12445829528846376, 17558421159018501, 9507591944175638, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (17727873733361666, 17558421159018501, 17727588109647875, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (17727823267528708, 17558421159018501, 17727698705088515, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (17727870503747586, 17558421159018501, 17727756695502851, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (17727973582995466, 17558421159018501, 17727776022888453, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (18497811045679106, 17558421159018501, 18497721916719105, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (18497870101512196, 17558421159018501, 18497740170362881, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (18497721916719106, 17558421159018501, 18497811045679105, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (18497740170362882, 17558421159018501, 18497870101512195, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (18501196553650180, 17558421159018501, 18500844357943298, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (18501308222832650, 17558421159018501, 18501349016633350, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (19027040128729089, 17558421159018501, 19026881223327750, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (19027459961815050, 17558421159018501, 19027366546243585, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (19028462836678660, 17558421159018501, 19028252391636995, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (19028617463857155, 17558421159018501, 19028471435001859, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

-- normal auth

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282830, 9507591944175638, 9505726846205953, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349921, 9507591944175638, 9506121983229953, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120551, 9507591944175638, 9506477400129537, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175651, 9507591944175638, 9506557930799110, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282831, 9507591944175638, 9505726846205957, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349922, 9507591944175638, 9506121983229954, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120552, 9507591944175638, 9506477400129538, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175652, 9507591944175638, 9506557930799111, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282832, 9507591944175638, 9505726846205961, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349923, 9507591944175638, 9506121983229955, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120553, 9507591944175638, 9506477400129539, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175653, 9507591944175638, 9506557930799112, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282833, 9507591944175638, 9505726846205965, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349925, 9507591944175638, 9506121983229956, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120554, 9507591944175638, 9506477400129540, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175654, 9507591944175638, 9506557930799113, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282834, 9507591944175638, 9505726846205969, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349926, 9507591944175638, 9506121983229957, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120556, 9507591944175638, 9506477400129541, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175656, 9507591944175638, 9506557930799114, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- file0

CREATE
DATABASE file_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
file_0;

CREATE TABLE `attachment_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `link`        varchar(256) NOT NULL COMMENT 'attachment link',
    `name`        varchar(256) NOT NULL COMMENT 'resource name',
    `file_type`   varchar(64)  NOT NULL COMMENT 'attachment type/suffix',
    `size`        bigint       NOT NULL COMMENT 'attachment length',
    `status`      tinyint      NOT NULL COMMENT 'attachment status: 1-active 2-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY           `idx_name`(`name`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of attachment 0';

CREATE TABLE `attachment_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `link`        varchar(256) NOT NULL COMMENT 'attachment link',
    `name`        varchar(256) NOT NULL COMMENT 'resource name',
    `file_type`   varchar(64)  NOT NULL COMMENT 'attachment type/suffix',
    `size`        bigint       NOT NULL COMMENT 'attachment length',
    `status`      tinyint      NOT NULL COMMENT 'attachment status: 1-active 2-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY           `idx_name`(`name`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of attachment 1';

CREATE TABLE `download_history_0`
(
    `id`            bigint NOT NULL COMMENT 'id',
    `attachment_id` bigint NOT NULL COMMENT 'attachment id',
    `create_time`   bigint NOT NULL COMMENT 'data create time',
    `creator`       bigint NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY             `idx_attachment_id`(`attachment_id`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE,
    KEY             `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of download history 0';

CREATE TABLE `download_history_1`
(
    `id`            bigint NOT NULL COMMENT 'id',
    `attachment_id` bigint NOT NULL COMMENT 'attachment id',
    `create_time`   bigint NOT NULL COMMENT 'data create time',
    `creator`       bigint NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY             `idx_attachment_id`(`attachment_id`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE,
    KEY             `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of download history 1';


-- seata

CREATE TABLE `undo_log`
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';

-- file1

CREATE
DATABASE file_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
file_1;

CREATE TABLE `attachment_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `link`        varchar(256) NOT NULL COMMENT 'attachment link',
    `name`        varchar(256) NOT NULL COMMENT 'resource name',
    `file_type`   varchar(64)  NOT NULL COMMENT 'attachment type/suffix',
    `size`        bigint       NOT NULL COMMENT 'attachment length',
    `status`      tinyint      NOT NULL COMMENT 'attachment status: 1-active 2-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY           `idx_name`(`name`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of attachment 0';

CREATE TABLE `attachment_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `link`        varchar(256) NOT NULL COMMENT 'attachment link',
    `name`        varchar(256) NOT NULL COMMENT 'resource name',
    `file_type`   varchar(64)  NOT NULL COMMENT 'attachment type/suffix',
    `size`        bigint       NOT NULL COMMENT 'attachment length',
    `status`      tinyint      NOT NULL COMMENT 'attachment status: 1-active 2-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY           `idx_name`(`name`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of attachment 1';

CREATE TABLE `download_history_0`
(
    `id`            bigint NOT NULL COMMENT 'id',
    `attachment_id` bigint NOT NULL COMMENT 'attachment id',
    `create_time`   bigint NOT NULL COMMENT 'data create time',
    `creator`       bigint NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY             `idx_attachment_id`(`attachment_id`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE,
    KEY             `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of download history 0';

CREATE TABLE `download_history_1`
(
    `id`            bigint NOT NULL COMMENT 'id',
    `attachment_id` bigint NOT NULL COMMENT 'attachment id',
    `create_time`   bigint NOT NULL COMMENT 'data create time',
    `creator`       bigint NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY             `idx_attachment_id`(`attachment_id`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE,
    KEY             `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of download history 1';


-- seata

CREATE TABLE `undo_log`
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';


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

CREATE TABLE `organization_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `phone`       varchar(16)  NOT NULL COMMENT 'organization phone',
    `email`       varchar(256) NOT NULL COMMENT 'organization email',
    `name`        varchar(128) DEFAULT NULL COMMENT 'organization name',
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
    `phone`       varchar(16)  NOT NULL COMMENT 'organization phone',
    `email`       varchar(256) NOT NULL COMMENT 'organization email',
    `name`        varchar(128) DEFAULT NULL COMMENT 'organization name',
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


-- seata undo log

CREATE TABLE `undo_log`
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';

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

CREATE TABLE `organization_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `phone`       varchar(16)  NOT NULL COMMENT 'organization phone',
    `email`       varchar(256) NOT NULL COMMENT 'organization email',
    `name`        varchar(128) DEFAULT NULL COMMENT 'organization name',
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
    `phone`       varchar(16)  NOT NULL COMMENT 'organization phone',
    `email`       varchar(256) NOT NULL COMMENT 'organization email',
    `name`        varchar(128) DEFAULT NULL COMMENT 'organization name',
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

-- seata undo log

CREATE TABLE `undo_log`
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';


INSERT INTO `finance_1`.`dynamic_handler_1`(`id`, `name`, `description`, `handler_bean`, `create_time`, `update_time`,
                                            `creator`, `updater`)
VALUES (13739721721151489, 'blue_get dynamic endpoint handler', 'blue_get dynamic endpoint handler',
        'com.blue.finance.component.dynamic.impl.BlueGetDynamicEndPointHandlerImpl', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `finance_1`.`dynamic_handler_1`(`id`, `name`, `description`, `handler_bean`, `create_time`, `update_time`,
                                            `creator`, `updater`)
VALUES (13739721721151490, 'blue_post dynamic endpoint handler', 'blue_post dynamic endpoint handler',
        'com.blue.finance.component.dynamic.impl.BluePostDynamicEndPointHandlerImpl', UNIX_TIMESTAMP(),
        UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `finance_1`.`dynamic_resource_1`(`id`, `organization_id`, `handler_id`, `request_method`, `uri_placeholder`,
                                             `content_type`, `name`,
                                             description, `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151489, 13739721721151489, 13739721721151489, 'GET', 1, 'application/json',
        'blue_get dynamic resource',
        'blue_get dynamic resource',
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `finance_1`.`dynamic_resource_1`(`id`, `organization_id`, `handler_id`, `request_method`, `uri_placeholder`,
                                             `content_type`, `name`,
                                             description, `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151490, 13739721721151490, 13739721721151490, 'POST', 1, 'application/json',
        'blue_post dynamic resource',
        'blue_post dynamic resource',
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);


-- marketing0

CREATE
DATABASE marketing_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
marketing_0;

CREATE TABLE `event_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `type`        tinyint      NOT NULL COMMENT 'event type',
    `data`        varchar(512) NOT NULL COMMENT 'event data/json',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-handled 0-un handled',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY           `idx_create_time`(`create_time`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of marketing event 0';

CREATE TABLE `event_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `type`        tinyint      NOT NULL COMMENT 'event type',
    `data`        varchar(512) NOT NULL COMMENT 'event data/json',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-handled 0-un handled',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY           `idx_create_time`(`create_time`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of marketing event 1';

CREATE TABLE `reward_0`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of marketing reward 0';

CREATE TABLE `reward_1`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of marketing reward 1';

CREATE TABLE `sign_reward_today_relation_0`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reward and date relation 0';

CREATE TABLE `sign_reward_today_relation_1`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reward and date relation 1';

-- seata undo log

CREATE TABLE `undo_log`
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';


-- marketing1

CREATE
DATABASE marketing_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
marketing_1;

CREATE TABLE `event_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `type`        tinyint      NOT NULL COMMENT 'event type 1-签到奖励, 2-活动奖励',
    `data`        varchar(512) NOT NULL COMMENT 'event data/json',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-handled 0-un handled',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY           `idx_create_time`(`create_time`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of marketing event 0';

CREATE TABLE `event_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `type`        tinyint      NOT NULL COMMENT 'event type 1-签到奖励, 2-活动奖励',
    `data`        varchar(512) NOT NULL COMMENT 'event data/json',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-handled 0-un handled',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    KEY           `idx_create_time`(`create_time`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of marketing event 1';

CREATE TABLE `reward_0`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of marketing reward 0';

CREATE TABLE `reward_1`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of marketing reward 1';

CREATE TABLE `sign_reward_today_relation_0`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reward and date relation 0';

CREATE TABLE `sign_reward_today_relation_1`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reward and date relation 1';

-- seata undo log

CREATE TABLE `undo_log`
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';

-- init

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335558, '1th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629954, '2th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (235157848067, '3th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (127783698434, '4th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335559, '5th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629959, '6th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (235157848072, '7th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (127783698440, '8th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335554, '9th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629957, '10th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (235157848065, '11th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (127783698439, '12th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335553, '13th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629953, '14th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (235157848068, '15th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (127783698438, '16th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335561, '17th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629955, '18th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (235157848066, '19th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (127783698437, '20th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335556, '21th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629960, '22th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (235157848069, '23th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (127783698436, '24th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335560, '25th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629956, '26th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (235157848071, '27th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (127783698442, '28th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335555, '29th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629958, '30th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629962, '31th reward', 'Im a reward...', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);



INSERT INTO `marketing_0`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (166429982722, 185757335558, 2022, 1, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (202937237505, 151397629954, 2022, 1, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (127783665667, 235157848067, 2022, 1, 3, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (157848469505, 185757335559, 2022, 1, 5, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (166429982728, 151397629959, 2022, 1, 6, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (202937237507, 235157848072, 2022, 1, 7, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (127783665672, 127783698440, 2022, 1, 8, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (157848469511, 185757335554, 2022, 1, 9, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (166429982721, 151397629957, 2022, 1, 10, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (202937237512, 235157848065, 2022, 1, 11, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (127783665671, 127783698439, 2022, 1, 12, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (157848469506, 185757335553, 2022, 1, 13, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (166429982725, 235157848068, 2022, 1, 15, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (202937237511, 127783698438, 2022, 1, 16, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (127783665668, 185757335561, 2022, 1, 17, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (157848469507, 151397629955, 2022, 1, 18, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (166429982726, 235157848066, 2022, 1, 19, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (202937237510, 127783698437, 2022, 1, 20, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (127783665670, 185757335556, 2022, 1, 21, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (157848469508, 151397629960, 2022, 1, 22, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (166429982729, 235157848069, 2022, 1, 23, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (202937237506, 185757335560, 2022, 1, 25, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (127783665669, 151397629956, 2022, 1, 26, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (157848469512, 235157848071, 2022, 1, 27, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (166429982724, 127783698442, 2022, 1, 28, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (202937237508, 185757335555, 2022, 1, 29, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (127783665665, 151397629958, 2022, 1, 30, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (157848469510, 151397629962, 2022, 1, 31, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);


-- member0

CREATE
DATABASE member_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
member_0;

CREATE TABLE `member_basic_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `phone`       varchar(256) NOT NULL COMMENT 'phone number',
    `email`       varchar(256) NOT NULL COMMENT 'email',
    `password`    varchar(256) DEFAULT NULL COMMENT 'password',
    `name`        varchar(256) DEFAULT NULL COMMENT 'name',
    `icon`        varchar(255) DEFAULT NULL COMMENT 'icon link',
    `gender`      tinyint      DEFAULT '1' COMMENT 'gender: 1-male 2-female 3-other',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_phone`(`phone`) USING BTREE,
    UNIQUE KEY `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member basic 0';

CREATE TABLE `member_basic_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `phone`       varchar(256) NOT NULL COMMENT 'phone number',
    `email`       varchar(256) NOT NULL COMMENT 'email',
    `password`    varchar(256) DEFAULT NULL COMMENT 'password',
    `name`        varchar(256) DEFAULT NULL COMMENT 'name',
    `icon`        varchar(255) DEFAULT NULL COMMENT 'icon link',
    `gender`      tinyint      DEFAULT '1' COMMENT 'gender: 1-male 2-female 3-other',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_phone`(`phone`) USING BTREE,
    UNIQUE KEY `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member basic 1';

CREATE TABLE `member_detail_0`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `real_name`   varchar(50)  DEFAULT NULL COMMENT 'read name',
    `id_card_no`  varchar(50)  DEFAULT NULL COMMENT 'id card number',
    `address`     varchar(256) DEFAULT NULL COMMENT 'address',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY           `idx_name`(`real_name`) USING BTREE,
    UNIQUE KEY `idx_id_card`(`id_card_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member detail 0';

CREATE TABLE `member_detail_1`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `real_name`   varchar(50)  DEFAULT NULL COMMENT 'read name',
    `id_card_no`  varchar(50)  DEFAULT NULL COMMENT 'id card number',
    `address`     varchar(256) DEFAULT NULL COMMENT 'address',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY           `idx_name`(`real_name`) USING BTREE,
    UNIQUE KEY `idx_id_card`(`id_card_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member detail 1';

CREATE TABLE `member_business_0`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `qr_code`     varchar(512) DEFAULT NULL COMMENT 'qrcode link',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member business 0';

CREATE TABLE `member_business_1`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `qr_code`     varchar(512) DEFAULT NULL COMMENT 'qrcode link',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member business 1';


-- seata undo log

CREATE TABLE `undo_log`
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';


-- member1

CREATE
DATABASE member_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
member_1;

CREATE TABLE `member_basic_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `phone`       varchar(256) NOT NULL COMMENT 'phone number',
    `email`       varchar(256) NOT NULL COMMENT 'email',
    `password`    varchar(256) DEFAULT NULL COMMENT 'password',
    `name`        varchar(256) DEFAULT NULL COMMENT 'name',
    `icon`        varchar(255) DEFAULT NULL COMMENT 'icon link',
    `gender`      tinyint      DEFAULT '1' COMMENT 'gender: 1-male 2-female 3-other',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_phone`(`phone`) USING BTREE,
    UNIQUE KEY `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member basic 0';

CREATE TABLE `member_basic_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `phone`       varchar(256) NOT NULL COMMENT 'phone number',
    `email`       varchar(256) NOT NULL COMMENT 'email',
    `password`    varchar(256) DEFAULT NULL COMMENT 'password',
    `name`        varchar(256) DEFAULT NULL COMMENT 'name',
    `icon`        varchar(255) DEFAULT NULL COMMENT 'icon link',
    `gender`      tinyint      DEFAULT '1' COMMENT 'gender: 1-male 2-female 3-other',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_phone`(`phone`) USING BTREE,
    UNIQUE KEY `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member basic 1';

CREATE TABLE `member_detail_0`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `real_name`   varchar(50)  DEFAULT NULL COMMENT 'read name',
    `id_card_no`  varchar(50)  DEFAULT NULL COMMENT 'id card number',
    `address`     varchar(256) DEFAULT NULL COMMENT 'address',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY           `idx_name`(`real_name`) USING BTREE,
    UNIQUE KEY `idx_id_card`(`id_card_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member detail 0';

CREATE TABLE `member_detail_1`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `real_name`   varchar(50)  DEFAULT NULL COMMENT 'read name',
    `id_card_no`  varchar(50)  DEFAULT NULL COMMENT 'id card number',
    `address`     varchar(256) DEFAULT NULL COMMENT 'address',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY           `idx_name`(`real_name`) USING BTREE,
    UNIQUE KEY `idx_id_card`(`id_card_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member detail 1';

CREATE TABLE `member_business_0`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `qr_code`     varchar(512) DEFAULT NULL COMMENT 'qrcode link',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member business 0';

CREATE TABLE `member_business_1`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `qr_code`     varchar(512) DEFAULT NULL COMMENT 'qrcode link',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `update_time` bigint NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member business 1';


-- seata undo log

CREATE TABLE `undo_log`
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';


-- portal0

CREATE
DATABASE portal_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
portal_0;

CREATE TABLE `bulletin_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `title`       varchar(128) NOT NULL COMMENT 'bulletin title',
    `content`     varchar(256) DEFAULT NULL COMMENT 'bulletin content',
    `link`        varchar(256) NOT NULL COMMENT 'bulletin link',
    `type`        tinyint      NOT NULL COMMENT 'bulletin type: 1-popular 2-newest 3-recommend',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `priority`    int          NOT NULL COMMENT 'bulletin priority',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    `updater`     bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_title`(`title`) USING BTREE,
    KEY           `idx_stat_pri`(`status`,`priority`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of bulletin 0';

CREATE TABLE `bulletin_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `title`       varchar(128) NOT NULL COMMENT 'bulletin title',
    `content`     varchar(256) DEFAULT NULL COMMENT 'bulletin content',
    `link`        varchar(256) NOT NULL COMMENT 'bulletin link',
    `type`        tinyint      NOT NULL COMMENT 'bulletin type: 1-popular 2-newest 3-recommend',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `priority`    int          NOT NULL COMMENT 'bulletin priority',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    `updater`     bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_title`(`title`) USING BTREE,
    KEY           `idx_stat_pri`(`status`,`priority`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of bulletin 1';

-- seata undo log

CREATE TABLE `undo_log`
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';


-- portal1

CREATE
DATABASE portal_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
portal_1;

CREATE TABLE `bulletin_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `title`       varchar(128) NOT NULL COMMENT 'bulletin title',
    `content`     varchar(256) DEFAULT NULL COMMENT 'bulletin content',
    `link`        varchar(256) NOT NULL COMMENT 'bulletin link',
    `type`        tinyint      NOT NULL COMMENT 'bulletin type: 1-popular 2-newest 3-recommend',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `priority`    int          NOT NULL COMMENT 'bulletin priority',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    `updater`     bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_title`(`title`) USING BTREE,
    KEY           `idx_stat_pri`(`status`,`priority`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of bulletin 0';

CREATE TABLE `bulletin_1`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `title`       varchar(128) NOT NULL COMMENT 'bulletin title',
    `content`     varchar(256) DEFAULT NULL COMMENT 'bulletin content',
    `link`        varchar(256) NOT NULL COMMENT 'bulletin link',
    `type`        tinyint      NOT NULL COMMENT 'bulletin type: 1-popular 2-newest 3-recommend',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `priority`    int          NOT NULL COMMENT 'bulletin priority',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    `updater`     bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_title`(`title`) USING BTREE,
    KEY           `idx_stat_pri`(`status`,`priority`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of bulletin 1';

-- seata undo log

CREATE TABLE `undo_log`
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';



INSERT INTO `portal_0`.`bulletin_0`(`id`, `title`, `content`, `link`, `type`, `status`, `priority`, `create_time`,
                                    `update_time`, `creator`, `updater`)
VALUES (159987531779, 'popular bulletin 2', 'test data', 'www.baidu.com', 1, 1, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `portal_0`.`bulletin_1`(`id`, `title`, `content`, `link`, `type`, `status`, `priority`, `create_time`,
                                    `update_time`, `creator`, `updater`)
VALUES (172872466433, 'popular bulletin 1', 'test data', 'cn.bing.com', 1, 1, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1,
        1);

INSERT INTO `portal_1`.`bulletin_0`(`id`, `title`, `content`, `link`, `type`, `status`, `priority`, `create_time`,
                                    `update_time`, `creator`, `updater`)
VALUES (127783665667, 'newest bulletin 2', 'test data', 'www.baidu.com', 2, 1, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1,
        1);

INSERT INTO `portal_1`.`bulletin_1`(`id`, `title`, `content`, `link`, `type`, `status`, `priority`, `create_time`,
                                    `update_time`, `creator`, `updater`)
VALUES (162143436801, 'newest bulletin 1', 'test data', 'cn.bing.com', 2, 1, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1,
        1);

INSERT INTO `portal_0`.`bulletin_0`(`id`, `title`, `content`, `link`, `type`, `status`, `priority`, `create_time`,
                                    `update_time`, `creator`, `updater`)
VALUES (159987531778, 'recommend bulletin 2', 'test data', 'www.baidu.com', 3, 1, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `portal_1`.`bulletin_1`(`id`, `title`, `content`, `link`, `type`, `status`, `priority`, `create_time`,
                                    `update_time`, `creator`, `updater`)
VALUES (162143436802, 'recommend bulletin 1', 'test data', 'cn.bing.com', 3, 1, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);


-- business0

CREATE
DATABASE business_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
business_0;

CREATE TABLE `article_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `author_id`   bigint       NOT NULL COMMENT 'member/author id',
    `type`        tinyint      NOT NULL COMMENT 'article type: 1-tricked 2-recommendation 3-shits 4-grumble 5-share 6-water',
    `title`       varchar(256) NOT NULL COMMENT 'article title',
    `author`      varchar(256) NOT NULL COMMENT 'author id',
    `content`     mediumtext   NOT NULL COMMENT 'article content',
    `favorites`   bigint       NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `readings`    bigint       NOT NULL DEFAULT '0' COMMENT 'readings count',
    `comments`    bigint       NOT NULL DEFAULT '0' COMMENT 'comments count',
    `likes`       bigint       NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`      bigint       NOT NULL DEFAULT '0' COMMENT 'boring count',
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
    `favorites`   bigint       NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `readings`    bigint       NOT NULL DEFAULT '0' COMMENT 'readings count',
    `comments`    bigint       NOT NULL DEFAULT '0' COMMENT 'comments count',
    `likes`       bigint       NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`      bigint       NOT NULL DEFAULT '0' COMMENT 'boring count',
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
    `favorites`     bigint        NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `readings`      bigint        NOT NULL DEFAULT '0' COMMENT 'readings count',
    `comments`      bigint        NOT NULL DEFAULT '0' COMMENT 'comments count',
    `likes`         bigint        NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint        NOT NULL DEFAULT '0' COMMENT 'boring count',
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
    `favorites`     bigint        NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `readings`      bigint        NOT NULL DEFAULT '0' COMMENT 'readings count',
    `comments`      bigint        NOT NULL DEFAULT '0' COMMENT 'comments count',
    `likes`         bigint        NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint        NOT NULL DEFAULT '0' COMMENT 'boring count',
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
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `replies`       bigint     NOT NULL DEFAULT '0' COMMENT 'replies count',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT 'boring count',
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
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `replies`       bigint     NOT NULL DEFAULT '0' COMMENT 'replies count',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT 'boring count',
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
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT 'boring count',
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
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT 'boring count',
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


-- business1

CREATE
DATABASE business_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
business_1;

CREATE TABLE `article_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `author_id`   bigint       NOT NULL COMMENT 'member/author id',
    `type`        tinyint      NOT NULL COMMENT 'article type: 1-tricked 2-recommendation 3-shits 4-grumble 5-share 6-water',
    `title`       varchar(256) NOT NULL COMMENT 'article title',
    `author`      varchar(256) NOT NULL COMMENT 'author id',
    `content`     mediumtext   NOT NULL COMMENT 'article content',
    `favorites`   bigint       NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `readings`    bigint       NOT NULL DEFAULT '0' COMMENT 'readings count',
    `comments`    bigint       NOT NULL DEFAULT '0' COMMENT 'comments count',
    `likes`       bigint       NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`      bigint       NOT NULL DEFAULT '0' COMMENT 'boring count',
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
    `favorites`   bigint       NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `readings`    bigint       NOT NULL DEFAULT '0' COMMENT 'readings count',
    `comments`    bigint       NOT NULL DEFAULT '0' COMMENT 'comments count',
    `likes`       bigint       NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`      bigint       NOT NULL DEFAULT '0' COMMENT 'boring count',
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
    `favorites`     bigint        NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `readings`      bigint        NOT NULL DEFAULT '0' COMMENT 'readings count',
    `comments`      bigint        NOT NULL DEFAULT '0' COMMENT 'comments count',
    `likes`         bigint        NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint        NOT NULL DEFAULT '0' COMMENT 'boring count',
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
    `favorites`     bigint        NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `readings`      bigint        NOT NULL DEFAULT '0' COMMENT 'readings count',
    `comments`      bigint        NOT NULL DEFAULT '0' COMMENT 'comments count',
    `likes`         bigint        NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint        NOT NULL DEFAULT '0' COMMENT 'boring count',
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
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `replies`       bigint     NOT NULL DEFAULT '0' COMMENT 'replies count',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT 'boring count',
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
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `replies`       bigint     NOT NULL DEFAULT '0' COMMENT 'replies count',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT 'boring count',
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
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT 'boring count',
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
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT 'favorites count',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT 'likes count',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT 'boring count',
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


-- the_data测试独立库表

CREATE
DATABASE dict CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
dict;

CREATE TABLE `dict_type`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `code`        varchar(128) NOT NULL COMMENT 'dict type code',
    `name`        varchar(256) DEFAULT NULL COMMENT 'dict type name',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    `updater`     bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_code`(`code`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of dict type';

CREATE TABLE `dict`
(
    `id`           bigint       NOT NULL COMMENT 'id',
    `dict_type_id` bigint       NOT NULL COMMENT 'dict type id',
    `name`         varchar(256) DEFAULT NULL COMMENT 'dict name',
    `value`        varchar(128) NOT NULL COMMENT 'dict value',
    `create_time`  bigint       NOT NULL COMMENT 'data create time',
    `update_time`  bigint       NOT NULL COMMENT 'data update time',
    `creator`      bigint       NOT NULL COMMENT 'creator id',
    `updater`      bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_type_name_value`(`dict_type_id`,`name`,`value` ) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of dict';