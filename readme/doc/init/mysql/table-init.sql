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
    `relation_view`           varchar(512) DEFAULT '' COMMENT 'resource relation view',
    `authenticate`            bit          NOT NULL COMMENT 'need auth: 1-yes, 0-no',
    `request_un_decryption`   bit          NOT NULL COMMENT 'do not decrypt request body: 1-not, 0-yes',
    `response_un_encryption`  bit          NOT NULL COMMENT 'do not encrypt response body: 1-not, 0-yes',
    `existence_request_body`  bit          NOT NULL COMMENT 'has request body: 1-yes, 0-no',
    `existence_response_body` bit          NOT NULL COMMENT 'has response body: 1-yes, 0-no',
    `type`                    tinyint      NOT NULL COMMENT 'resource type: 1-rest for client, 2-rest for manager, 3-rest for open api',
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
    `type`        tinyint     NOT NULL COMMENT 'role type: 1-role for client, 2-role for manager, 3-role for open api',
    `name`        varchar(64) NOT NULL COMMENT 'role name',
    `description` varchar(128) DEFAULT '' COMMENT 'role disc',
    `level`       int         NOT NULL COMMENT 'roles level',
    `is_default`  bit         NOT NULL COMMENT 'is default role: 1-yes, 0-no',
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
    `creator`     bigint NOT NULL COMMENT 'creator id',
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
    `creator`     bigint NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_role`(`member_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member and role relation 0';

CREATE TABLE `member_role_relation_1`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `role_id`     bigint NOT NULL COMMENT 'role id',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `creator`     bigint NOT NULL COMMENT 'creator id',
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
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid, 0-invalid',
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
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid, 0-invalid',
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
    `creator`     bigint NOT NULL COMMENT 'creator id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_role`(`member_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member and role relation 0';

CREATE TABLE `member_role_relation_1`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `role_id`     bigint NOT NULL COMMENT 'role id',
    `create_time` bigint NOT NULL COMMENT 'data create time',
    `creator`     bigint NOT NULL COMMENT 'creator id',
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
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid, 0-invalid',
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
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid, 0-invalid',
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


-- media0

CREATE
DATABASE media_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
media_0;

-- media1

CREATE
DATABASE media_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
media_1;

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
    `status`      tinyint NOT NULL COMMENT 'data status: 1-valid, 0-invalid',
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
    `status`      tinyint NOT NULL COMMENT 'data status: 1-valid, 0-invalid',
    `create_time` bigint  NOT NULL COMMENT 'data create time',
    `update_time` bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of finance account 1';

CREATE TABLE `order_0`
(
    `id`            bigint       NOT NULL COMMENT 'id',
    `member_id`     bigint       NOT NULL COMMENT 'member id',
    `order_no`      varchar(255) NOT NULL COMMENT 'order no',
    `flow_no`       varchar(255) NOT NULL COMMENT 'flow no',
    `type`          tinyint      NOT NULL COMMENT 'order type',
    `payment_type`  tinyint      NOT NULL COMMENT 'payment type',
    `amount`        bigint       NOT NULL COMMENT 'order amount/fen',
    `pay_amount`    bigint       NOT NULL COMMENT 'pay amount/fen',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'order extra',
    `payment_extra` varchar(256) DEFAULT NULL COMMENT 'payment extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'order detail',
    `status`        tinyint      NOT NULL COMMENT 'order status: 1-valid, 0-invalid',
    `version`       bigint       DEFAULT '1' COMMENT 'version',
    `create_time`   bigint       NOT NULL COMMENT 'order create time',
    `update_time`   bigint       NOT NULL COMMENT 'order update time',
    `payment_time`  bigint       DEFAULT NULL COMMENT 'order payment time',
    PRIMARY KEY (`id`),
    KEY             `idx_member`(`member_id`) USING BTREE,
    UNIQUE KEY `idx_order_no`(`order_no`) USING BTREE,
    UNIQUE KEY `idx_flow_no`(`flow_no`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of order 0';

CREATE TABLE `order_1`
(
    `id`            bigint       NOT NULL COMMENT 'id',
    `member_id`     bigint       NOT NULL COMMENT 'member id',
    `order_no`      varchar(255) NOT NULL COMMENT 'order no',
    `flow_no`       varchar(255) NOT NULL COMMENT 'flow no',
    `type`          tinyint      NOT NULL COMMENT 'order type',
    `payment_type`  tinyint      NOT NULL COMMENT 'payment type',
    `amount`        bigint       NOT NULL COMMENT 'order amount/fen',
    `pay_amount`    bigint       NOT NULL COMMENT 'pay amount/fen',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'order extra',
    `payment_extra` varchar(256) DEFAULT NULL COMMENT 'payment extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'order detail',
    `status`        tinyint      NOT NULL COMMENT 'order status: 1-valid, 0-invalid',
    `version`       bigint       DEFAULT '1' COMMENT 'version',
    `create_time`   bigint       NOT NULL COMMENT 'order create time',
    `update_time`   bigint       NOT NULL COMMENT 'order update time',
    `payment_time`  bigint       DEFAULT NULL COMMENT 'order payment time',
    PRIMARY KEY (`id`),
    KEY             `idx_member`(`member_id`) USING BTREE,
    UNIQUE KEY `idx_order_no`(`order_no`) USING BTREE,
    UNIQUE KEY `idx_flow_no`(`flow_no`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of order 1';

CREATE TABLE `order_article_0`
(
    `id`            bigint  NOT NULL COMMENT 'id',
    `order_id`      bigint  NOT NULL COMMENT 'order id',
    `article_id`    bigint  NOT NULL COMMENT 'article id',
    `member_id`     bigint  NOT NULL COMMENT 'member id',
    `amount`        bigint  NOT NULL COMMENT 'article amount/fen',
    `quantity`      bigint  NOT NULL COMMENT 'article quantity',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'article extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'article detail',
    `status`        tinyint NOT NULL COMMENT 'order article status: 1-valid, 0-invalid',
    `order_version` bigint       DEFAULT '1' COMMENT 'order version',
    `create_time`   bigint  NOT NULL COMMENT 'data create time',
    `update_time`   bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY             `idx_order`(`order_id`) USING BTREE,
    KEY             `idx_member`(`member_id`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of order article 0';

CREATE TABLE `order_article_1`
(
    `id`            bigint  NOT NULL COMMENT 'id',
    `order_id`      bigint  NOT NULL COMMENT 'order id',
    `article_id`    bigint  NOT NULL COMMENT 'article id',
    `member_id`     bigint  NOT NULL COMMENT 'member id',
    `amount`        bigint  NOT NULL COMMENT 'article amount/fen',
    `quantity`      bigint  NOT NULL COMMENT 'article quantity',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'article extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'article detail',
    `status`        tinyint NOT NULL COMMENT 'order article status: 1-valid, 0-invalid',
    `order_version` bigint       DEFAULT '1' COMMENT 'order version',
    `create_time`   bigint  NOT NULL COMMENT 'data create time',
    `update_time`   bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY             `idx_order`(`order_id`) USING BTREE,
    KEY             `idx_member`(`member_id`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of order article 1';

CREATE TABLE `reference_amount_0`
(
    `id`            bigint  NOT NULL COMMENT 'id',
    `order_id`      bigint  NOT NULL COMMENT 'order id',
    `member_id`     bigint  NOT NULL COMMENT 'member id',
    `type`          tinyint NOT NULL COMMENT 'amount type',
    `reference_id`  bigint       DEFAULT '0' COMMENT 'reference id',
    `amount`        bigint  NOT NULL COMMENT 'reference amount/fen',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'reference extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'amount detail',
    `status`        tinyint NOT NULL COMMENT 'reference amount status: 1-valid, 0-invalid',
    `order_version` bigint       DEFAULT '1' COMMENT 'order version',
    `create_time`   bigint  NOT NULL COMMENT 'data create time',
    `update_time`   bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY             `idx_order`(`order_id`) USING BTREE,
    KEY             `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reference amount 0';

CREATE TABLE `reference_amount_1`
(
    `id`            bigint  NOT NULL COMMENT 'id',
    `order_id`      bigint  NOT NULL COMMENT 'order id',
    `member_id`     bigint  NOT NULL COMMENT 'member id',
    `type`          tinyint NOT NULL COMMENT 'amount type',
    `reference_id`  bigint       DEFAULT '0' COMMENT 'reference id',
    `amount`        bigint  NOT NULL COMMENT 'reference amount/fen',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'reference extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'amount detail',
    `status`        tinyint NOT NULL COMMENT 'reference amount status: 1-valid, 0-invalid',
    `order_version` bigint       DEFAULT '1' COMMENT 'order version',
    `create_time`   bigint  NOT NULL COMMENT 'data create time',
    `update_time`   bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY             `idx_order`(`order_id`) USING BTREE,
    KEY             `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reference amount 1';

CREATE TABLE `refund_0`
(
    `id`            bigint       NOT NULL COMMENT 'id',
    `order_id`      bigint       NOT NULL COMMENT 'order id',
    `article_id`    bigint       NOT NULL COMMENT 'order article id',
    `reference_id`  bigint       NOT NULL COMMENT 'reference id',
    `member_id`     bigint       NOT NULL COMMENT 'member id',
    `order_no`      varchar(255) NOT NULL COMMENT 'refund order no',
    `flow_no`       varchar(255) NOT NULL COMMENT 'refund flow no',
    `type`          tinyint      NOT NULL COMMENT 'amount type',
    `amount`        bigint       NOT NULL COMMENT 'refund amount/fen',
    `reason`        varchar(256) DEFAULT NULL COMMENT 'amount detail',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'refund extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'amount detail',
    `status`        tinyint      NOT NULL COMMENT 'refund status: 1-valid, 0-invalid',
    `create_time`   bigint       NOT NULL COMMENT 'refund create time',
    `update_time`   bigint       NOT NULL COMMENT 'refund update time',
    `complete_time` bigint       DEFAULT NULL COMMENT 'refund complete time',
    PRIMARY KEY (`id`),
    KEY             `idx_order`(`order_id`) USING BTREE,
    KEY             `idx_member`(`member_id`) USING BTREE,
    UNIQUE KEY `idx_order_no`(`order_no`) USING BTREE,
    UNIQUE KEY `idx_flow_no`(`flow_no`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of refund 0';

CREATE TABLE `refund_1`
(
    `id`            bigint       NOT NULL COMMENT 'id',
    `order_id`      bigint       NOT NULL COMMENT 'order id',
    `article_id`    bigint       NOT NULL COMMENT 'order article id',
    `reference_id`  bigint       NOT NULL COMMENT 'reference id',
    `member_id`     bigint       NOT NULL COMMENT 'member id',
    `order_no`      varchar(255) NOT NULL COMMENT 'refund order no',
    `flow_no`       varchar(255) NOT NULL COMMENT 'refund flow no',
    `type`          tinyint      NOT NULL COMMENT 'amount type',
    `amount`        bigint       NOT NULL COMMENT 'refund amount/fen',
    `reason`        varchar(256) DEFAULT NULL COMMENT 'amount detail',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'refund extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'amount detail',
    `status`        tinyint      NOT NULL COMMENT 'refund status: 1-valid, 0-invalid',
    `create_time`   bigint       NOT NULL COMMENT 'refund create time',
    `update_time`   bigint       NOT NULL COMMENT 'refund update time',
    `complete_time` bigint       DEFAULT NULL COMMENT 'refund complete time',
    PRIMARY KEY (`id`),
    KEY             `idx_order`(`order_id`) USING BTREE,
    KEY             `idx_member`(`member_id`) USING BTREE,
    UNIQUE KEY `idx_order_no`(`order_no`) USING BTREE,
    UNIQUE KEY `idx_flow_no`(`flow_no`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of refund 1';

CREATE TABLE `organization_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `phone`       varchar(16)  NOT NULL COMMENT 'organization phone format: 8613131693996',
    `email`       varchar(256) NOT NULL COMMENT 'organization email',
    `name`        varchar(128) DEFAULT '0' COMMENT 'organization name',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid, 0-invalid',
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
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid, 0-invalid',
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
    `status`      tinyint NOT NULL COMMENT 'data status: 1-valid, 0-invalid',
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
    `status`      tinyint NOT NULL COMMENT 'data status: 1-valid, 0-invalid',
    `create_time` bigint  NOT NULL COMMENT 'data create time',
    `update_time` bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of finance account 1';

CREATE TABLE `order_0`
(
    `id`            bigint       NOT NULL COMMENT 'id',
    `member_id`     bigint       NOT NULL COMMENT 'member id',
    `order_no`      varchar(255) NOT NULL COMMENT 'order no',
    `flow_no`       varchar(255) NOT NULL COMMENT 'flow no',
    `type`          tinyint      NOT NULL COMMENT 'order type',
    `payment_type`  tinyint      NOT NULL COMMENT 'payment type',
    `amount`        bigint       NOT NULL COMMENT 'order amount/fen',
    `pay_amount`    bigint       NOT NULL COMMENT 'pay amount/fen',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'order extra',
    `payment_extra` varchar(256) DEFAULT NULL COMMENT 'payment extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'order detail',
    `status`        tinyint      NOT NULL COMMENT 'order status: 1-valid, 0-invalid',
    `version`       bigint       DEFAULT '1' COMMENT 'version',
    `create_time`   bigint       NOT NULL COMMENT 'order create time',
    `update_time`   bigint       NOT NULL COMMENT 'order update time',
    `payment_time`  bigint       DEFAULT NULL COMMENT 'order payment time',
    PRIMARY KEY (`id`),
    KEY             `idx_member`(`member_id`) USING BTREE,
    UNIQUE KEY `idx_order_no`(`order_no`) USING BTREE,
    UNIQUE KEY `idx_flow_no`(`flow_no`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of order 0';

CREATE TABLE `order_1`
(
    `id`            bigint       NOT NULL COMMENT 'id',
    `member_id`     bigint       NOT NULL COMMENT 'member id',
    `order_no`      varchar(255) NOT NULL COMMENT 'order no',
    `flow_no`       varchar(255) NOT NULL COMMENT 'flow no',
    `type`          tinyint      NOT NULL COMMENT 'order type',
    `payment_type`  tinyint      NOT NULL COMMENT 'payment type',
    `amount`        bigint       NOT NULL COMMENT 'order amount/fen',
    `pay_amount`    bigint       NOT NULL COMMENT 'pay amount/fen',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'order extra',
    `payment_extra` varchar(256) DEFAULT NULL COMMENT 'payment extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'order detail',
    `status`        tinyint      NOT NULL COMMENT 'order status: 1-valid, 0-invalid',
    `version`       bigint       DEFAULT '1' COMMENT 'version',
    `create_time`   bigint       NOT NULL COMMENT 'order create time',
    `update_time`   bigint       NOT NULL COMMENT 'order update time',
    `payment_time`  bigint       DEFAULT NULL COMMENT 'order payment time',
    PRIMARY KEY (`id`),
    KEY             `idx_member`(`member_id`) USING BTREE,
    UNIQUE KEY `idx_order_no`(`order_no`) USING BTREE,
    UNIQUE KEY `idx_flow_no`(`flow_no`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of order 1';

CREATE TABLE `order_article_0`
(
    `id`            bigint  NOT NULL COMMENT 'id',
    `order_id`      bigint  NOT NULL COMMENT 'order id',
    `article_id`    bigint  NOT NULL COMMENT 'article id',
    `member_id`     bigint  NOT NULL COMMENT 'member id',
    `amount`        bigint  NOT NULL COMMENT 'article amount/fen',
    `quantity`      bigint  NOT NULL COMMENT 'article quantity',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'article extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'article detail',
    `status`        tinyint NOT NULL COMMENT 'order article status: 1-valid, 0-invalid',
    `order_version` bigint       DEFAULT '1' COMMENT 'order version',
    `create_time`   bigint  NOT NULL COMMENT 'data create time',
    `update_time`   bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY             `idx_order`(`order_id`) USING BTREE,
    KEY             `idx_member`(`member_id`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of order article 0';

CREATE TABLE `order_article_1`
(
    `id`            bigint  NOT NULL COMMENT 'id',
    `order_id`      bigint  NOT NULL COMMENT 'order id',
    `article_id`    bigint  NOT NULL COMMENT 'article id',
    `member_id`     bigint  NOT NULL COMMENT 'member id',
    `amount`        bigint  NOT NULL COMMENT 'article amount/fen',
    `quantity`      bigint  NOT NULL COMMENT 'article quantity',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'article extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'article detail',
    `status`        tinyint NOT NULL COMMENT 'order article status: 1-valid, 0-invalid',
    `order_version` bigint       DEFAULT '1' COMMENT 'order version',
    `create_time`   bigint  NOT NULL COMMENT 'data create time',
    `update_time`   bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY             `idx_order`(`order_id`) USING BTREE,
    KEY             `idx_member`(`member_id`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of order article 1';

CREATE TABLE `reference_amount_0`
(
    `id`            bigint  NOT NULL COMMENT 'id',
    `order_id`      bigint  NOT NULL COMMENT 'order id',
    `member_id`     bigint  NOT NULL COMMENT 'member id',
    `type`          tinyint NOT NULL COMMENT 'amount type',
    `reference_id`  bigint       DEFAULT '0' COMMENT 'reference id',
    `amount`        bigint  NOT NULL COMMENT 'reference amount/fen',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'reference extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'amount detail',
    `status`        tinyint NOT NULL COMMENT 'reference amount status: 1-valid, 0-invalid',
    `order_version` bigint       DEFAULT '1' COMMENT 'order version',
    `create_time`   bigint  NOT NULL COMMENT 'data create time',
    `update_time`   bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY             `idx_order`(`order_id`) USING BTREE,
    KEY             `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reference amount 0';

CREATE TABLE `reference_amount_1`
(
    `id`            bigint  NOT NULL COMMENT 'id',
    `order_id`      bigint  NOT NULL COMMENT 'order id',
    `member_id`     bigint  NOT NULL COMMENT 'member id',
    `type`          tinyint NOT NULL COMMENT 'amount type',
    `reference_id`  bigint       DEFAULT '0' COMMENT 'reference id',
    `amount`        bigint  NOT NULL COMMENT 'reference amount/fen',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'reference extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'amount detail',
    `status`        tinyint NOT NULL COMMENT 'reference amount status: 1-valid, 0-invalid',
    `order_version` bigint       DEFAULT '1' COMMENT 'order version',
    `create_time`   bigint  NOT NULL COMMENT 'data create time',
    `update_time`   bigint  NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY             `idx_order`(`order_id`) USING BTREE,
    KEY             `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of reference amount 1';

CREATE TABLE `refund_0`
(
    `id`            bigint       NOT NULL COMMENT 'id',
    `order_id`      bigint       NOT NULL COMMENT 'order id',
    `article_id`    bigint       NOT NULL COMMENT 'order article id',
    `reference_id`  bigint       NOT NULL COMMENT 'reference id',
    `member_id`     bigint       NOT NULL COMMENT 'member id',
    `order_no`      varchar(255) NOT NULL COMMENT 'refund order no',
    `flow_no`       varchar(255) NOT NULL COMMENT 'refund flow no',
    `type`          tinyint      NOT NULL COMMENT 'amount type',
    `amount`        bigint       NOT NULL COMMENT 'refund amount/fen',
    `reason`        varchar(256) DEFAULT NULL COMMENT 'amount detail',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'refund extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'amount detail',
    `status`        tinyint      NOT NULL COMMENT 'refund status: 1-valid, 0-invalid',
    `create_time`   bigint       NOT NULL COMMENT 'refund create time',
    `update_time`   bigint       NOT NULL COMMENT 'refund update time',
    `complete_time` bigint       DEFAULT NULL COMMENT 'refund complete time',
    PRIMARY KEY (`id`),
    KEY             `idx_order`(`order_id`) USING BTREE,
    KEY             `idx_member`(`member_id`) USING BTREE,
    UNIQUE KEY `idx_order_no`(`order_no`) USING BTREE,
    UNIQUE KEY `idx_flow_no`(`flow_no`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of refund 0';

CREATE TABLE `refund_1`
(
    `id`            bigint       NOT NULL COMMENT 'id',
    `order_id`      bigint       NOT NULL COMMENT 'order id',
    `article_id`    bigint       NOT NULL COMMENT 'order article id',
    `reference_id`  bigint       NOT NULL COMMENT 'reference id',
    `member_id`     bigint       NOT NULL COMMENT 'member id',
    `order_no`      varchar(255) NOT NULL COMMENT 'refund order no',
    `flow_no`       varchar(255) NOT NULL COMMENT 'refund flow no',
    `type`          tinyint      NOT NULL COMMENT 'amount type',
    `amount`        bigint       NOT NULL COMMENT 'refund amount/fen',
    `reason`        varchar(256) DEFAULT NULL COMMENT 'amount detail',
    `extra`         varchar(256) DEFAULT NULL COMMENT 'refund extra',
    `detail`        varchar(256) DEFAULT NULL COMMENT 'amount detail',
    `status`        tinyint      NOT NULL COMMENT 'refund status: 1-valid, 0-invalid',
    `create_time`   bigint       NOT NULL COMMENT 'refund create time',
    `update_time`   bigint       NOT NULL COMMENT 'refund update time',
    `complete_time` bigint       DEFAULT NULL COMMENT 'refund complete time',
    PRIMARY KEY (`id`),
    KEY             `idx_order`(`order_id`) USING BTREE,
    KEY             `idx_member`(`member_id`) USING BTREE,
    UNIQUE KEY `idx_order_no`(`order_no`) USING BTREE,
    UNIQUE KEY `idx_flow_no`(`flow_no`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of refund 1';

CREATE TABLE `organization_0`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `phone`       varchar(16)  NOT NULL COMMENT 'organization phone format: 8613131693996',
    `email`       varchar(256) NOT NULL COMMENT 'organization email',
    `name`        varchar(128) DEFAULT '0' COMMENT 'organization name',
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid, 0-invalid',
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
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid, 0-invalid',
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
    `status`      tinyint      DEFAULT '1' COMMENT 'data status: 1-valid, 0-invalid',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    `updater`     bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name_type`(`name`,`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of marketing reward';

CREATE TABLE `reward_date_relation`
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
    UNIQUE KEY `idx_date`(`year`,`month`,`day`) USING BTREE,
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
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-handled, 0-un handled',
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
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-handled, 0-un handled',
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
    `real_name`         varchar(256) DEFAULT '' COMMENT 'real name',
    `gender`            tinyint      DEFAULT '3' COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of real name detail 0';

CREATE TABLE `real_name_1`
(
    `id`                bigint NOT NULL COMMENT 'id',
    `member_id`         bigint NOT NULL COMMENT 'member id',
    `real_name`         varchar(256) DEFAULT '' COMMENT 'real name',
    `gender`            tinyint      DEFAULT '3' COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of real name detail 1';

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
    `height`         smallint      DEFAULT '0' COMMENT 'height/cm',
    `weight`         smallint      DEFAULT '0' COMMENT 'weight/kg',
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
    `height`         smallint      DEFAULT '0' COMMENT 'height/cm',
    `weight`         smallint      DEFAULT '0' COMMENT 'weight/kg',
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
    `gender`            tinyint      DEFAULT '3' COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
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
    `gender`            tinyint      DEFAULT '3' COMMENT 'gender: 1-male 0-female 2-other 3-unknown',
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

CREATE TABLE `notice`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `title`       varchar(128) NOT NULL COMMENT 'notice title',
    `content`     text         DEFAULT NULL COMMENT 'notice content',
    `link`        varchar(256) DEFAULT '' COMMENT 'notice link',
    `type`        tinyint      NOT NULL COMMENT 'notice type: 1-privacy policy 2-faq 3-about us 4-terms and conditions',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    `updater`     bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_type`(`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of notice';

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

-- article

CREATE
DATABASE article CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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

CREATE
DATABASE base_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
base_0;

CREATE
DATABASE base_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
base_1;

CREATE
DATABASE agreement CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
agreement;

CREATE TABLE `agreement`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `title`       varchar(128) NOT NULL COMMENT 'agreement title',
    `content`     varchar(256) DEFAULT '' COMMENT 'agreement content',
    `link`        varchar(256) DEFAULT '' COMMENT 'agreement link',
    `type`        tinyint      NOT NULL COMMENT 'agreement type 1.PLATFORM 2.SERVICE 3.PRIVACY 4.MEMBER 5.BUSINESS 6.EXCLUSION_CLAUSE',
    `status`      tinyint      NOT NULL COMMENT 'data status: 1-valid 0-invalid',
    `priority`    int          NOT NULL COMMENT 'agreement priority',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    `creator`     bigint       NOT NULL COMMENT 'creator id',
    `updater`     bigint       NOT NULL COMMENT 'updater id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_title`(`title`) USING BTREE,
    KEY           `idx_type_stat_pri`(`type`,`status`,`priority`) USING BTREE,
    KEY           `idx_pri_stat`(`priority`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of agreement';


CREATE
DATABASE agreement_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
agreement_0;

CREATE TABLE `member_agreement_record_0`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `agreement_id`     bigint NOT NULL COMMENT 'agreement id',
    `agree`      tinyint      NOT NULL COMMENT 'agree: 1-yes 0-no',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_agreement`(`member_id`,`agreement_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member agreement record 0';

CREATE TABLE `member_agreement_record_1`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `agreement_id`     bigint NOT NULL COMMENT 'agreement id',
    `agree`      tinyint      NOT NULL COMMENT 'agree: 1-yes 0-no',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_agreement`(`member_id`,`agreement_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member agreement record 1';




CREATE
DATABASE agreement_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
agreement_1;

CREATE TABLE `member_agreement_record_0`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `agreement_id`     bigint NOT NULL COMMENT 'agreement id',
    `agree`      tinyint      NOT NULL COMMENT 'agree: 1-yes 0-no',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_agreement`(`member_id`,`agreement_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member agreement record 0';

CREATE TABLE `member_agreement_record_1`
(
    `id`          bigint NOT NULL COMMENT 'id',
    `member_id`   bigint NOT NULL COMMENT 'member id',
    `agreement_id`     bigint NOT NULL COMMENT 'agreement id',
    `agree`      tinyint      NOT NULL COMMENT 'agree: 1-yes 0-no',
    `create_time` bigint       NOT NULL COMMENT 'data create time',
    `update_time` bigint       NOT NULL COMMENT 'data update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_agreement`(`member_id`,`agreement_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of member agreement record 1';

