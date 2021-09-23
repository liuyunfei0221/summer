-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- secure0

CREATE
DATABASE secure_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
secure_0;

CREATE TABLE `resource_0`
(
    `id`                      bigint       NOT NULL COMMENT '主键',
    `request_method`          varchar(16)  NOT NULL COMMENT '请求方式/大写',
    `module`                  varchar(256) NOT NULL COMMENT '服务',
    `uri`                     varchar(256) NOT NULL COMMENT '资源路径',
    `authenticate`            bit          NOT NULL COMMENT '认证标识 1需要认证 0免认证',
    `pre_un_decryption`       bit          NOT NULL COMMENT '请求数据不解密 1不解密 0解密',
    `post_un_encryption`      bit          NOT NULL COMMENT '响应数据不加密 1不加密 0加密',
    `existence_request_body`  bit          NOT NULL COMMENT '是否有请求体 1有 0没有',
    `existence_response_body` bit          NOT NULL COMMENT '是否有响应体 1有 0没有',
    `type`                    tinyint      NOT NULL COMMENT '资源类型 1前台api 2后台api 3对外提供api',
    `name`                    varchar(128) DEFAULT NULL COMMENT '资源名称',
    `description`             varchar(256) DEFAULT NULL COMMENT '资源描述',
    `create_time`             bigint       NOT NULL COMMENT '创建时间',
    `update_time`             bigint       NOT NULL COMMENT '修改时间',
    `creator`                 bigint       NOT NULL COMMENT '创建人',
    `updater`                 bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_method_module_uri`(`request_method`,`module`,`uri`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资源表0';

CREATE TABLE `resource_1`
(
    `id`                      bigint       NOT NULL COMMENT '主键',
    `request_method`          varchar(16)  NOT NULL COMMENT '请求方式/大写',
    `module`                  varchar(256) NOT NULL COMMENT '服务',
    `uri`                     varchar(256) NOT NULL COMMENT '资源路径',
    `authenticate`            bit          NOT NULL COMMENT '认证标识 1需要认证 0免认证',
    `pre_un_decryption`       bit          NOT NULL COMMENT '请求数据不解密 1不解密 0解密',
    `post_un_encryption`      bit          NOT NULL COMMENT '响应数据不加密 1不加密 0加密',
    `existence_request_body`  bit          NOT NULL COMMENT '是否有请求体 1有 0没有',
    `existence_response_body` bit          NOT NULL COMMENT '是否有响应体 1有 0没有',
    `type`                    tinyint      NOT NULL COMMENT '资源类型 1前台api 2后台api 3对外提供api',
    `name`                    varchar(128) DEFAULT NULL COMMENT '资源名称',
    `description`             varchar(256) DEFAULT NULL COMMENT '资源描述',
    `create_time`             bigint       NOT NULL COMMENT '创建时间',
    `update_time`             bigint       NOT NULL COMMENT '修改时间',
    `creator`                 bigint       NOT NULL COMMENT '创建人',
    `updater`                 bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_method_module_uri`(`request_method`,`module`,`uri`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资源表1';

CREATE TABLE `role_0`
(
    `id`          bigint      NOT NULL COMMENT '主键',
    `name`        varchar(64) NOT NULL COMMENT '角色名称',
    `description` varchar(128) DEFAULT NULL COMMENT '角色描述',
    `is_default`  bit         NOT NULL COMMENT '是否默认角色',
    `create_time` bigint      NOT NULL COMMENT '创建时间',
    `update_time` bigint      NOT NULL COMMENT '修改时间',
    `creator`     bigint      NOT NULL COMMENT '创建人',
    `updater`     bigint      NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表0';

CREATE TABLE `role_1`
(
    `id`          bigint      NOT NULL COMMENT '主键',
    `name`        varchar(64) NOT NULL COMMENT '角色名称',
    `description` varchar(128) DEFAULT NULL COMMENT '角色描述',
    `is_default`  bit         NOT NULL COMMENT '是否默认角色',
    `create_time` bigint      NOT NULL COMMENT '创建时间',
    `update_time` bigint      NOT NULL COMMENT '修改时间',
    `creator`     bigint      NOT NULL COMMENT '创建人',
    `updater`     bigint      NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表1';

CREATE TABLE `role_res_relation_0`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `role_id`     bigint NOT NULL COMMENT '角色id',
    `res_id`      bigint NOT NULL COMMENT '资源id',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    `creator`     bigint NOT NULL COMMENT '创建人',
    `updater`     bigint NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_role_res`(`role_id`,`res_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色资源关联表0';

CREATE TABLE `role_res_relation_1`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `role_id`     bigint NOT NULL COMMENT '角色id',
    `res_id`      bigint NOT NULL COMMENT '资源id',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    `creator`     bigint NOT NULL COMMENT '创建人',
    `updater`     bigint NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_role_res`(`role_id`,`res_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色资源关联表1';

CREATE TABLE `member_role_relation_0`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `member_id`   bigint NOT NULL COMMENT '用户id',
    `role_id`     bigint NOT NULL COMMENT '角色id',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    `creator`     bigint NOT NULL COMMENT '创建人',
    `updater`     bigint NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_role`(`member_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员角色关联表0';

CREATE TABLE `member_role_relation_1`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `member_id`   bigint NOT NULL COMMENT '用户id',
    `role_id`     bigint NOT NULL COMMENT '角色id',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    `creator`     bigint NOT NULL COMMENT '创建人',
    `updater`     bigint NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_role`(`member_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员角色关联表1';

-- seata表

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
    `id`                      bigint       NOT NULL COMMENT '主键',
    `request_method`          varchar(16)  NOT NULL COMMENT '请求方式/大写',
    `module`                  varchar(256) NOT NULL COMMENT '服务',
    `uri`                     varchar(256) NOT NULL COMMENT '资源路径',
    `authenticate`            bit          NOT NULL COMMENT '认证标识 1需要认证 0免认证',
    `pre_un_decryption`       bit          NOT NULL COMMENT '请求数据不解密 1不解密 0解密',
    `post_un_encryption`      bit          NOT NULL COMMENT '响应数据不加密 1不加密 0加密',
    `existence_request_body`  bit          NOT NULL COMMENT '是否有请求体 1有 0没有',
    `existence_response_body` bit          NOT NULL COMMENT '是否有响应体 1有 0没有',
    `type`                    tinyint      NOT NULL COMMENT '资源类型 1前台api 2后台api 3对外提供api',
    `name`                    varchar(128) DEFAULT NULL COMMENT '资源名称',
    `description`             varchar(256) DEFAULT NULL COMMENT '资源描述',
    `create_time`             bigint       NOT NULL COMMENT '创建时间',
    `update_time`             bigint       NOT NULL COMMENT '修改时间',
    `creator`                 bigint       NOT NULL COMMENT '创建人',
    `updater`                 bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_method_module_uri`(`request_method`,`module`,`uri`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资源表0';

CREATE TABLE `resource_1`
(
    `id`                      bigint       NOT NULL COMMENT '主键',
    `request_method`          varchar(16)  NOT NULL COMMENT '请求方式/大写',
    `module`                  varchar(256) NOT NULL COMMENT '服务',
    `uri`                     varchar(256) NOT NULL COMMENT '资源路径',
    `authenticate`            bit          NOT NULL COMMENT '认证标识 1需要认证 0免认证',
    `pre_un_decryption`       bit          NOT NULL COMMENT '请求数据不解密 1不解密 0解密',
    `post_un_encryption`      bit          NOT NULL COMMENT '响应数据不加密 1不加密 0加密',
    `existence_request_body`  bit          NOT NULL COMMENT '是否有请求体 1有 0没有',
    `existence_response_body` bit          NOT NULL COMMENT '是否有响应体 1有 0没有',
    `type`                    tinyint      NOT NULL COMMENT '资源类型 1前台api 2后台api 3对外提供api',
    `name`                    varchar(128) DEFAULT NULL COMMENT '资源名称',
    `description`             varchar(256) DEFAULT NULL COMMENT '资源描述',
    `create_time`             bigint       NOT NULL COMMENT '创建时间',
    `update_time`             bigint       NOT NULL COMMENT '修改时间',
    `creator`                 bigint       NOT NULL COMMENT '创建人',
    `updater`                 bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_method_module_uri`(`request_method`,`module`,`uri`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资源表1';

CREATE TABLE `role_0`
(
    `id`          bigint      NOT NULL COMMENT '主键',
    `name`        varchar(64) NOT NULL COMMENT '角色名称',
    `description` varchar(128) DEFAULT NULL COMMENT '角色描述',
    `is_default`  bit         NOT NULL COMMENT '是否默认角色',
    `create_time` bigint      NOT NULL COMMENT '创建时间',
    `update_time` bigint      NOT NULL COMMENT '修改时间',
    `creator`     bigint      NOT NULL COMMENT '创建人',
    `updater`     bigint      NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表0';

CREATE TABLE `role_1`
(
    `id`          bigint      NOT NULL COMMENT '主键',
    `name`        varchar(64) NOT NULL COMMENT '角色名称',
    `description` varchar(128) DEFAULT NULL COMMENT '角色描述',
    `is_default`  bit         NOT NULL COMMENT '是否默认角色',
    `create_time` bigint      NOT NULL COMMENT '创建时间',
    `update_time` bigint      NOT NULL COMMENT '修改时间',
    `creator`     bigint      NOT NULL COMMENT '创建人',
    `updater`     bigint      NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表1';

CREATE TABLE `role_res_relation_0`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `role_id`     bigint NOT NULL COMMENT '角色id',
    `res_id`      bigint NOT NULL COMMENT '资源id',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    `creator`     bigint NOT NULL COMMENT '创建人',
    `updater`     bigint NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_role_res`(`role_id`,`res_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色资源关联表0';

CREATE TABLE `role_res_relation_1`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `role_id`     bigint NOT NULL COMMENT '角色id',
    `res_id`      bigint NOT NULL COMMENT '资源id',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    `creator`     bigint NOT NULL COMMENT '创建人',
    `updater`     bigint NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_role_res`(`role_id`,`res_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色资源关联表1';

CREATE TABLE `member_role_relation_0`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `member_id`   bigint NOT NULL COMMENT '用户id',
    `role_id`     bigint NOT NULL COMMENT '角色id',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    `creator`     bigint NOT NULL COMMENT '创建人',
    `updater`     bigint NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_role`(`member_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员角色关联表0';

CREATE TABLE `member_role_relation_1`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `member_id`   bigint NOT NULL COMMENT '用户id',
    `role_id`     bigint NOT NULL COMMENT '角色id',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    `creator`     bigint NOT NULL COMMENT '创建人',
    `updater`     bigint NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member_role`(`member_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员角色关联表1';

-- seata表

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

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9505726846205953, 'POST', 'blue-secure', '/auth/loginByAcctAndPwd', b'0', b'1', b'1', b'1', b'1', 1,
        '账号密码登录', '账号密码登录', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506121983229953, 'PUT', 'blue-secure', '/auth/updateSecret', b'1', b'1', b'1', b'0', b'1', 1,
        '刷新密钥', '刷新密钥', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506477400129537, 'GET', 'blue-secure', '/auth/authority', b'1', b'1', b'1', b'0', b'1', 1,
        '查询权限信息', '查询权限信息', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506557930799110, 'GET', 'blue-member', '/member', b'1', b'1', b'1', b'0', b'1', 1,
        '用户信息', '用户信息', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9505726846205957, 'DELETE', 'blue-secure', '/auth/logout', b'1', b'1', b'1', b'0', b'1', 1,
        '注销登录', '注销登录', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506121983229954, 'GET', 'blue-portal', '/fallBack', b'0', b'1', b'1', b'0', b'1', 1,
        'GET降级', 'GET降级', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506477400129538, 'POST', 'blue-portal', '/fallBack', b'0', b'1', b'1', b'1', b'1', 1,
        'POST降级', 'POST降级', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506557930799111, 'GET', 'blue-portal', '/bulletin/{type}', b'0', b'1', b'1', b'0', b'1', 1,
        '前台公告列表', '前台公告列表', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9505726846205961, 'POST', 'blue-manager', '/member/list', b'1', b'1', b'1', b'1', b'1', 1,
        '后台用户列表', '后台用户列表', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506121983229955, 'POST', 'blue-file', '/file/upload', b'1', b'1', b'1', b'1', b'1', 1,
        '前台文件上传', '前台文件上传', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506477400129539, 'POST', 'blue-file', '/file/download', b'1', b'1', b'1', b'1', b'0', 1,
        '前台文件下载', '前台文件下载', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506557930799112, 'POST', 'blue-file', '/attachment/list', b'1', b'1', b'1', b'1', b'1', 1,
        '附件列表', '附件列表', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9505726846205965, 'POST', 'blue-marketing', '/signIn', b'1', b'1', b'1', b'0', b'1', 1,
        '签到', '签到', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506121983229956, 'GET', 'blue-marketing', '/signIn', b'1', b'1', b'1', b'0', b'1', 1,
        '获取当月签到记录', '获取当月签到记录', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506477400129540, 'GET', 'blue-file', '/file/downloadTest/{id}', b'1', b'1', b'1', b'1', b'0', 1,
        '测试下载', '测试下载', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506557930799113, 'GET', 'blue-finance', '/finance/balance', b'1', b'1', b'1', b'0', b'1', 1,
        '查询余额', '查询余额', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9505726846205969, 'POST', 'blue-member', '/member/registry', b'0', b'1', b'1', b'1', b'1', 1,
        '成员注册', '成员注册', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    `post_un_encryption`, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`, `description`, `create_time`, `update_time`, `creator`, `updater`)
VALUES (9505726846205970, 'GET', 'blue-portal', '/formatter/{formatter}.html', b'1', b'1', b'1', b'0', b'1', 1,
        '测试模板资源', '测试模板资源', 1629253160, 1629253160, 1, 1);


INSERT INTO `secure_0`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506121983229957, 'POST', 'blue-finance', '/withdraw', b'1', b'0', b'0', 1, b'1', b'1',
        '提现', '提现', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506477400129541, 'POST', 'blue-file', '/attachment/withdraw', b'1', b'0', b'0', b'1', b'1', 1,
        'react项目测试加密', 'react项目测试加密', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (9506557930799114, 'GET', 'blue-shine', '/shine', b'0', b'1', b'1', b'0', b'1', 1,
        '公益信息', '公益信息', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);


INSERT INTO `secure_0`.`resource_0`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (12445673836249089, 'POST', 'blue-member', '/manager/member/list', b'1', b'1', b'1', b'0', b'1', 2,
        '后台测试成员管理', '后台测试成员管理', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

-- 动态端点

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151489, 'GET', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3,
        'GET动态端点path', 'GET动态端点path', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151490, 'HEAD', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3,
        'HEAD动态端点path', 'HEAD动态端点path', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151491, 'POST', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3,
        'POST动态端点path', 'POST动态端点path', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151492, 'PUT', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3,
        'PUT动态端点path', 'PUT动态端点path', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151493, 'PATCH', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3,
        'PATCH动态端点path', 'PATCH动态端点path', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151495, 'DELETE', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1',
        b'1', 3,
        'DELETE动态端点path', 'DELETE动态端点path', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`resource_1`(`id`, `request_method`, `module`, `uri`, `authenticate`, `pre_un_decryption`,
                                    post_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                                    `name`,
                                    `description`,
                                    `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151496, 'OPTIONS', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1',
        b'1', 3,
        'OPTIONS动态端点path', 'OPTIONS动态端点path', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

--

INSERT INTO `secure_1`.`role_1`(`id`, `name`, `description`, `is_default`, `create_time`, `update_time`, `creator`,
                                `updater`)
VALUES (9507591944175638, '普通用户', '普通用户', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);



INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282820, 9507591944175638, 9505726846205953, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349900, 9507591944175638, 9506121983229953, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120540, 9507591944175638, 9506477400129537, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175640, 9507591944175638, 9506557930799110, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282821, 9507591944175638, 9505726846205957, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349901, 9507591944175638, 9506121983229954, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120541, 9507591944175638, 9506477400129538, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175641, 9507591944175638, 9506557930799111, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282822, 9507591944175638, 9505726846205961, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349902, 9507591944175638, 9506121983229955, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120542, 9507591944175638, 9506477400129539, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175642, 9507591944175638, 9506557930799112, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282825, 9507591944175638, 9505726846205965, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349903, 9507591944175638, 9506121983229956, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120543, 9507591944175638, 9506477400129540, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175643, 9507591944175638, 9506557930799113, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282826, 9507591944175638, 9505726846205969, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507452349349904, 9507591944175638, 9506121983229957, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507496381120544, 9507591944175638, 9506477400129541, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507591944175644, 9507591944175638, 9506557930799114, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282827, 9507591944175638, 9505726846205973, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (9507161365282828, 9507591944175638, 9505726846205970, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_0`.`role_res_relation_1`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (12445829528846376, 9507591944175638, 9507591944175638, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `secure_1`.`role_res_relation_0`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                             `updater`)
VALUES (13031107951853608, 9507591944175638, 12445673836249089, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- file0

CREATE
DATABASE file_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
file_0;

CREATE TABLE `attachment_0`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `link`        varchar(256) NOT NULL COMMENT '资源链接',
    `name`        varchar(256) NOT NULL COMMENT '资源名称',
    `file_type`   varchar(64)  NOT NULL COMMENT '资源类型',
    `size`        bigint       NOT NULL COMMENT '资源大小',
    `status`      tinyint      NOT NULL COMMENT '资源状态 0停用 1启用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    KEY           `idx_name`(`name`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='附件表0';

CREATE TABLE `attachment_1`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `link`        varchar(256) NOT NULL COMMENT '资源链接',
    `name`        varchar(256) NOT NULL COMMENT '资源名称',
    `file_type`   varchar(64)  NOT NULL COMMENT '资源类型',
    `size`        bigint       NOT NULL COMMENT '资源大小',
    `status`      tinyint      NOT NULL COMMENT '资源状态 0停用 1启用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    KEY           `idx_name`(`name`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='附件表1';

CREATE TABLE `download_history_0`
(
    `id`            bigint NOT NULL COMMENT '主键',
    `attachment_id` bigint NOT NULL COMMENT '文件id',
    `create_time`   bigint NOT NULL COMMENT '创建时间',
    `creator`       bigint NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    KEY             `idx_attachment_id`(`attachment_id`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE,
    KEY             `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='下载历史表0';

CREATE TABLE `download_history_1`
(
    `id`            bigint NOT NULL COMMENT '主键',
    `attachment_id` bigint NOT NULL COMMENT '文件id',
    `create_time`   bigint NOT NULL COMMENT '创建时间',
    `creator`       bigint NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    KEY             `idx_attachment_id`(`attachment_id`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE,
    KEY             `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='下载历史表1';


-- seata表

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
    `id`          bigint       NOT NULL COMMENT '主键',
    `link`        varchar(256) NOT NULL COMMENT '资源链接',
    `name`        varchar(256) NOT NULL COMMENT '资源名称',
    `file_type`   varchar(64)  NOT NULL COMMENT '资源类型',
    `size`        bigint       NOT NULL COMMENT '资源大小',
    `status`      tinyint      NOT NULL COMMENT '资源状态 0停用 1启用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    KEY           `idx_name`(`name`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='附件表0';

CREATE TABLE `attachment_1`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `link`        varchar(256) NOT NULL COMMENT '资源链接',
    `name`        varchar(256) NOT NULL COMMENT '资源名称',
    `file_type`   varchar(64)  NOT NULL COMMENT '资源类型',
    `size`        bigint       NOT NULL COMMENT '资源大小',
    `status`      tinyint      NOT NULL COMMENT '资源状态 0停用 1启用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    KEY           `idx_name`(`name`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='附件表1';

CREATE TABLE `download_history_0`
(
    `id`            bigint NOT NULL COMMENT '主键',
    `attachment_id` bigint NOT NULL COMMENT '文件id',
    `create_time`   bigint NOT NULL COMMENT '创建时间',
    `creator`       bigint NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    KEY             `idx_attachment_id`(`attachment_id`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE,
    KEY             `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='下载历史表0';

CREATE TABLE `download_history_1`
(
    `id`            bigint NOT NULL COMMENT '主键',
    `attachment_id` bigint NOT NULL COMMENT '文件id',
    `create_time`   bigint NOT NULL COMMENT '创建时间',
    `creator`       bigint NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    KEY             `idx_attachment_id`(`attachment_id`) USING BTREE,
    KEY             `idx_create_time`(`create_time`) USING BTREE,
    KEY             `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='下载历史表1';


-- seata表

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
    `id`          bigint  NOT NULL COMMENT '主键',
    `member_id`   bigint  NOT NULL COMMENT '成员id',
    `balance`     bigint DEFAULT '0' COMMENT '可用余额/分',
    `frozen`      bigint DEFAULT '0' COMMENT '冻结金额/分',
    `income`      bigint DEFAULT '0' COMMENT '历史总收入/分',
    `outlay`      bigint DEFAULT '0' COMMENT '历史总支出/分',
    `status`      tinyint NOT NULL COMMENT '状态 0停用 1启用',
    `create_time` bigint  NOT NULL COMMENT '创建时间',
    `update_time` bigint  NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员资金账户表0';

CREATE TABLE `finance_account_1`
(
    `id`          bigint  NOT NULL COMMENT '主键',
    `member_id`   bigint  NOT NULL COMMENT '成员id',
    `balance`     bigint DEFAULT '0' COMMENT '可用余额/分',
    `frozen`      bigint DEFAULT '0' COMMENT '冻结金额/分',
    `income`      bigint DEFAULT '0' COMMENT '历史总收入/分',
    `outlay`      bigint DEFAULT '0' COMMENT '历史总支出/分',
    `status`      tinyint NOT NULL COMMENT '状态 0停用 1启用',
    `create_time` bigint  NOT NULL COMMENT '创建时间',
    `update_time` bigint  NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员资金账户表1';

CREATE TABLE `organization_0`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `phone`       varchar(16)  NOT NULL COMMENT '组织电话',
    `email`       varchar(256) NOT NULL COMMENT '组织邮箱',
    `name`        varchar(128) DEFAULT NULL COMMENT '组织名称',
    `status`      tinyint      DEFAULT '1' COMMENT '状态，1可用 0禁用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    `updater`     bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    KEY           `idx_phone`(`phone`) USING BTREE,
    KEY           `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织基础信息表0';

CREATE TABLE `organization_1`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `phone`       varchar(16)  NOT NULL COMMENT '组织电话',
    `email`       varchar(256) NOT NULL COMMENT '组织邮箱',
    `name`        varchar(128) DEFAULT NULL COMMENT '组织名称',
    `status`      tinyint      DEFAULT '1' COMMENT '状态，1可用 0禁用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    `updater`     bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    KEY           `idx_phone`(`phone`) USING BTREE,
    KEY           `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织基础信息表1';

CREATE TABLE `dynamic_handler_0`
(
    `id`           bigint       NOT NULL COMMENT '主键',
    `name`         varchar(256) DEFAULT NULL COMMENT '动态资源处理器名称',
    `description`  varchar(512) DEFAULT NULL COMMENT '动态资源处理器描述',
    `handler_bean` varchar(256) NOT NULL COMMENT '动态资源处理器bean',
    `create_time`  bigint       NOT NULL COMMENT '创建时间',
    `update_time`  bigint       NOT NULL COMMENT '修改时间',
    `creator`      bigint       NOT NULL COMMENT '创建人',
    `updater`      bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name`(`name`) USING BTREE,
    UNIQUE KEY `idx_bean`(`handler_bean`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='动态资源处理器表0';

CREATE TABLE `dynamic_handler_1`
(
    `id`           bigint       NOT NULL COMMENT '主键',
    `name`         varchar(256) DEFAULT NULL COMMENT '动态资源处理器名称',
    `description`  varchar(512) DEFAULT NULL COMMENT '动态资源处理器描述',
    `handler_bean` varchar(256) NOT NULL COMMENT '动态资源处理器bean',
    `create_time`  bigint       NOT NULL COMMENT '创建时间',
    `update_time`  bigint       NOT NULL COMMENT '修改时间',
    `creator`      bigint       NOT NULL COMMENT '创建人',
    `updater`      bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name`(`name`) USING BTREE,
    UNIQUE KEY `idx_bean`(`handler_bean`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='动态资源处理器表1';

CREATE TABLE `dynamic_resource_0`
(
    `id`              bigint       NOT NULL COMMENT '主键',
    `organization_id` bigint       NOT NULL COMMENT '组织主键',
    `handler_id`      bigint       NOT NULL COMMENT '动态资源处理器主键',
    `request_method`  varchar(16)  NOT NULL COMMENT '请求方式/大写',
    `uri_placeholder` bigint NOT NULL COMMENT '动态资源路径占位符',
    `content_type`    varchar(64)  NOT NULL COMMENT '媒体类型',
    `name`            varchar(128) DEFAULT NULL COMMENT '动态资源名称',
    `description`     varchar(256) DEFAULT NULL COMMENT '动态资源描述',
    `create_time`     bigint       NOT NULL COMMENT '创建时间',
    `update_time`     bigint       NOT NULL COMMENT '修改时间',
    `creator`         bigint       NOT NULL COMMENT '创建人',
    `updater`         bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    KEY               `idx_organization_handler`(`organization_id`,`handler_id`) USING BTREE,
    UNIQUE KEY `idx_method_placeholder_content`(`request_method`,`uri_placeholder`,`content_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='动态资源表0';

CREATE TABLE `dynamic_resource_1`
(
    `id`              bigint       NOT NULL COMMENT '主键',
    `organization_id` bigint       NOT NULL COMMENT '组织主键',
    `handler_id`      bigint       NOT NULL COMMENT '动态资源处理器主键',
    `request_method`  varchar(16)  NOT NULL COMMENT '请求方式/大写',
    `uri_placeholder` bigint NOT NULL COMMENT '动态资源路径占位符',
    `content_type`    varchar(64)  NOT NULL COMMENT '媒体类型',
    `name`            varchar(128) DEFAULT NULL COMMENT '动态资源名称',
    `description`     varchar(256) DEFAULT NULL COMMENT '动态资源描述',
    `create_time`     bigint       NOT NULL COMMENT '创建时间',
    `update_time`     bigint       NOT NULL COMMENT '修改时间',
    `creator`         bigint       NOT NULL COMMENT '创建人',
    `updater`         bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    KEY               `idx_organization_handler`(`organization_id`,`handler_id`) USING BTREE,
    UNIQUE KEY `idx_method_placeholder_content`(`request_method`,`uri_placeholder`,`content_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='动态资源表1';


-- seata表

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
    `id`          bigint  NOT NULL COMMENT '主键',
    `member_id`   bigint  NOT NULL COMMENT '成员id',
    `balance`     bigint DEFAULT '0' COMMENT '可用余额/分',
    `frozen`      bigint DEFAULT '0' COMMENT '冻结金额/分',
    `income`      bigint DEFAULT '0' COMMENT '历史总收入/分',
    `outlay`      bigint DEFAULT '0' COMMENT '历史总支出/分',
    `status`      tinyint NOT NULL COMMENT '状态 0停用 1启用',
    `create_time` bigint  NOT NULL COMMENT '创建时间',
    `update_time` bigint  NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员资金账户表0';

CREATE TABLE `finance_account_1`
(
    `id`          bigint  NOT NULL COMMENT '主键',
    `member_id`   bigint  NOT NULL COMMENT '成员id',
    `balance`     bigint DEFAULT '0' COMMENT '可用余额/分',
    `frozen`      bigint DEFAULT '0' COMMENT '冻结金额/分',
    `income`      bigint DEFAULT '0' COMMENT '历史总收入/分',
    `outlay`      bigint DEFAULT '0' COMMENT '历史总支出/分',
    `status`      tinyint NOT NULL COMMENT '状态 0停用 1启用',
    `create_time` bigint  NOT NULL COMMENT '创建时间',
    `update_time` bigint  NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员资金账户表1';

CREATE TABLE `organization_0`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `phone`       varchar(16)  NOT NULL COMMENT '组织电话',
    `email`       varchar(256) NOT NULL COMMENT '组织邮箱',
    `name`        varchar(128) DEFAULT NULL COMMENT '组织名称',
    `status`      tinyint      DEFAULT '1' COMMENT '状态，1可用 0禁用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    `updater`     bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    KEY           `idx_phone`(`phone`) USING BTREE,
    KEY           `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织基础信息表0';

CREATE TABLE `organization_1`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `phone`       varchar(16)  NOT NULL COMMENT '组织电话',
    `email`       varchar(256) NOT NULL COMMENT '组织邮箱',
    `name`        varchar(128) DEFAULT NULL COMMENT '组织名称',
    `status`      tinyint      DEFAULT '1' COMMENT '状态，1可用 0禁用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    `updater`     bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    KEY           `idx_phone`(`phone`) USING BTREE,
    KEY           `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织基础信息表1';

CREATE TABLE `dynamic_handler_0`
(
    `id`           bigint       NOT NULL COMMENT '主键',
    `name`         varchar(256) DEFAULT NULL COMMENT '动态资源处理器名称',
    `description`  varchar(512) DEFAULT NULL COMMENT '动态资源处理器描述',
    `handler_bean` varchar(256) NOT NULL COMMENT '动态资源处理器bean',
    `create_time`  bigint       NOT NULL COMMENT '创建时间',
    `update_time`  bigint       NOT NULL COMMENT '修改时间',
    `creator`      bigint       NOT NULL COMMENT '创建人',
    `updater`      bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name`(`name`) USING BTREE,
    UNIQUE KEY `idx_bean`(`handler_bean`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='动态资源处理器表0';

CREATE TABLE `dynamic_handler_1`
(
    `id`           bigint       NOT NULL COMMENT '主键',
    `name`         varchar(256) DEFAULT NULL COMMENT '动态资源处理器名称',
    `description`  varchar(512) DEFAULT NULL COMMENT '动态资源处理器描述',
    `handler_bean` varchar(256) NOT NULL COMMENT '动态资源处理器bean',
    `create_time`  bigint       NOT NULL COMMENT '创建时间',
    `update_time`  bigint       NOT NULL COMMENT '修改时间',
    `creator`      bigint       NOT NULL COMMENT '创建人',
    `updater`      bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name`(`name`) USING BTREE,
    UNIQUE KEY `idx_bean`(`handler_bean`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='动态资源处理器表1';

CREATE TABLE `dynamic_resource_0`
(
    `id`              bigint       NOT NULL COMMENT '主键',
    `organization_id` bigint       NOT NULL COMMENT '组织主键',
    `handler_id`      bigint       NOT NULL COMMENT '动态资源处理器主键',
    `request_method`  varchar(16)  NOT NULL COMMENT '请求方式/大写',
    `uri_placeholder` bigint NOT NULL COMMENT '动态资源路径占位符',
    `content_type`    varchar(64)  NOT NULL COMMENT '媒体类型',
    `name`            varchar(128) DEFAULT NULL COMMENT '动态资源名称',
    `description`     varchar(256) DEFAULT NULL COMMENT '动态资源描述',
    `create_time`     bigint       NOT NULL COMMENT '创建时间',
    `update_time`     bigint       NOT NULL COMMENT '修改时间',
    `creator`         bigint       NOT NULL COMMENT '创建人',
    `updater`         bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    KEY               `idx_organization_handler`(`organization_id`,`handler_id`) USING BTREE,
    UNIQUE KEY `idx_method_placeholder_content`(`request_method`,`uri_placeholder`,`content_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='动态资源表0';

CREATE TABLE `dynamic_resource_1`
(
    `id`              bigint       NOT NULL COMMENT '主键',
    `organization_id` bigint       NOT NULL COMMENT '组织主键',
    `handler_id`      bigint       NOT NULL COMMENT '动态资源处理器主键',
    `request_method`  varchar(16)  NOT NULL COMMENT '请求方式/大写',
    `uri_placeholder` bigint NOT NULL COMMENT '动态资源路径占位符',
    `content_type`    varchar(64)  NOT NULL COMMENT '媒体类型',
    `name`            varchar(128) DEFAULT NULL COMMENT '动态资源名称',
    `description`     varchar(256) DEFAULT NULL COMMENT '动态资源描述',
    `create_time`     bigint       NOT NULL COMMENT '创建时间',
    `update_time`     bigint       NOT NULL COMMENT '修改时间',
    `creator`         bigint       NOT NULL COMMENT '创建人',
    `updater`         bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    KEY               `idx_organization_handler`(`organization_id`,`handler_id`) USING BTREE,
    UNIQUE KEY `idx_method_placeholder_content`(`request_method`,`uri_placeholder`,`content_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='动态资源表1';

-- seata表

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
VALUES (13739721721151489, 'blue_get动态端点处理器', 'blue_get动态端点处理器',
        'com.blue.finance.component.dynamic.impl.BlueGetDynamicEndPointHandlerImpl', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);

INSERT INTO `finance_1`.`dynamic_handler_1`(`id`, `name`, `description`, `handler_bean`, `create_time`, `update_time`,
                                            `creator`, `updater`)
VALUES (13739721721151490, 'blue_post动态端点处理器', 'blue_post动态端点处理器',
        'com.blue.finance.component.dynamic.impl.BluePostDynamicEndPointHandlerImpl', UNIX_TIMESTAMP(),
        UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `finance_1`.`dynamic_resource_1`(`id`, `organization_id`, `handler_id`, `request_method`, `uri_placeholder`,
                                             `content_type`, `name`,
                                             description, `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151489, 13739721721151489, 13739721721151489, 'GET', 1, 'application/json', 'blue_get动态端点',
        'blue_get动态端点',
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `finance_1`.`dynamic_resource_1`(`id`, `organization_id`, `handler_id`, `request_method`, `uri_placeholder`,
                                             `content_type`, `name`,
                                             description, `create_time`, `update_time`, `creator`, `updater`)
VALUES (13739721721151490, 13739721721151490, 13739721721151490, 'POST', 1, 'application/json', 'blue_post动态端点',
        'blue_post动态端点',
        UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);


-- marketing0

CREATE
DATABASE marketing_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
marketing_0;

CREATE TABLE `event_0`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `type`        tinyint      NOT NULL COMMENT '事件类型',
    `data`        varchar(512) NOT NULL COMMENT 'json格式事件数据',
    `status`      tinyint      NOT NULL COMMENT '事件状态 0未处理 1已处理',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    KEY           `idx_create_time`(`create_time`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='营销事件表0';

CREATE TABLE `event_1`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `type`        tinyint      NOT NULL COMMENT '事件类型',
    `data`        varchar(512) NOT NULL COMMENT 'json格式事件数据',
    `status`      tinyint      NOT NULL COMMENT '事件状态 0未处理 1已处理',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    KEY           `idx_create_time`(`create_time`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='营销事件表1';

CREATE TABLE `reward_0`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `name`        varchar(128) NOT NULL COMMENT '奖励名称',
    `detail`      varchar(256) DEFAULT NULL COMMENT '奖励描述',
    `link`        varchar(256) NOT NULL COMMENT '奖励图片链接',
    `type`        tinyint      NOT NULL COMMENT '奖励类型',
    `data`        varchar(512) NOT NULL COMMENT 'json格式奖励信息数据',
    `status`      tinyint      DEFAULT '1' COMMENT '状态，1可用 0禁用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    `updater`     bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='签到奖励表0';

CREATE TABLE `reward_1`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `name`        varchar(128) NOT NULL COMMENT '奖励名称',
    `detail`      varchar(256) DEFAULT NULL COMMENT '奖励描述',
    `link`        varchar(256) NOT NULL COMMENT '奖励图片链接',
    `type`        tinyint      NOT NULL COMMENT '奖励类型',
    `data`        varchar(512) NOT NULL COMMENT 'json格式奖励信息数据',
    `status`      tinyint      DEFAULT '1' COMMENT '状态，1可用 0禁用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    `updater`     bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='签到奖励表1';

CREATE TABLE `sign_reward_today_relation_0`
(
    `id`          bigint  NOT NULL COMMENT '主键',
    `reward_id`   bigint  NOT NULL COMMENT '奖励id',
    `year`        smallint(6) NOT NULL COMMENT '年份',
    `month`       tinyint NOT NULL COMMENT '月份',
    `day`         tinyint NOT NULL COMMENT '日期',
    `create_time` bigint  NOT NULL COMMENT '创建时间',
    `update_time` bigint  NOT NULL COMMENT '修改时间',
    `creator`     bigint  NOT NULL COMMENT '创建人',
    `updater`     bigint  NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_date_reward`(`year`,`month`,`day`,`reward_id`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE,
    KEY           `idx_updater`(`updater`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='当日签到奖励关联表0';

CREATE TABLE `sign_reward_today_relation_1`
(
    `id`          bigint  NOT NULL COMMENT '主键',
    `reward_id`   bigint  NOT NULL COMMENT '奖励id',
    `year`        smallint(6) NOT NULL COMMENT '年份',
    `month`       tinyint NOT NULL COMMENT '月份',
    `day`         tinyint NOT NULL COMMENT '日期',
    `create_time` bigint  NOT NULL COMMENT '创建时间',
    `update_time` bigint  NOT NULL COMMENT '修改时间',
    `creator`     bigint  NOT NULL COMMENT '创建人',
    `updater`     bigint  NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_date_reward`(`year`,`month`,`day`,`reward_id`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE,
    KEY           `idx_updater`(`updater`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='当日签到奖励关联表1';

-- seata表

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
    `id`          bigint       NOT NULL COMMENT '主键',
    `type`        tinyint      NOT NULL COMMENT '事件类型 1-签到奖励, 2-活动奖励',
    `data`        varchar(512) NOT NULL COMMENT 'json格式事件数据',
    `status`      tinyint      NOT NULL COMMENT '事件状态 0未处理 1已处理',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    KEY           `idx_create_time`(`create_time`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='营销事件表0';

CREATE TABLE `event_1`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `type`        tinyint      NOT NULL COMMENT '事件类型 1-签到奖励, 2-活动奖励',
    `data`        varchar(512) NOT NULL COMMENT 'json格式事件数据',
    `status`      tinyint      NOT NULL COMMENT '事件状态 0未处理 1已处理',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    KEY           `idx_create_time`(`create_time`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='营销事件表1';

CREATE TABLE `reward_0`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `name`        varchar(128) NOT NULL COMMENT '奖励名称',
    `detail`      varchar(256) DEFAULT NULL COMMENT '奖励描述',
    `link`        varchar(256) NOT NULL COMMENT '奖励图片链接',
    `type`        tinyint      NOT NULL COMMENT '奖励类型',
    `data`        varchar(512) NOT NULL COMMENT 'json格式奖励信息数据',
    `status`      tinyint      DEFAULT '1' COMMENT '状态，1可用 0禁用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    `updater`     bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='签到奖励表0';

CREATE TABLE `reward_1`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `name`        varchar(128) NOT NULL COMMENT '奖励名称',
    `detail`      varchar(256) DEFAULT NULL COMMENT '奖励描述',
    `link`        varchar(256) NOT NULL COMMENT '奖励图片链接',
    `type`        tinyint      NOT NULL COMMENT '奖励类型',
    `data`        varchar(512) NOT NULL COMMENT 'json格式奖励信息数据',
    `status`      tinyint      DEFAULT '1' COMMENT '状态，1可用 0禁用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    `updater`     bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='签到奖励表1';

CREATE TABLE `sign_reward_today_relation_0`
(
    `id`          bigint  NOT NULL COMMENT '主键',
    `reward_id`   bigint  NOT NULL COMMENT '奖励id',
    `year`        smallint(6) NOT NULL COMMENT '年份',
    `month`       tinyint NOT NULL COMMENT '月份',
    `day`         tinyint NOT NULL COMMENT '日期',
    `create_time` bigint  NOT NULL COMMENT '创建时间',
    `update_time` bigint  NOT NULL COMMENT '修改时间',
    `creator`     bigint  NOT NULL COMMENT '创建人',
    `updater`     bigint  NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_date_reward`(`year`,`month`,`day`,`reward_id`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE,
    KEY           `idx_updater`(`updater`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='当日签到奖励关联表0';

CREATE TABLE `sign_reward_today_relation_1`
(
    `id`          bigint  NOT NULL COMMENT '主键',
    `reward_id`   bigint  NOT NULL COMMENT '奖励id',
    `year`        smallint(6) NOT NULL COMMENT '年份',
    `month`       tinyint NOT NULL COMMENT '月份',
    `day`         tinyint NOT NULL COMMENT '日期',
    `create_time` bigint  NOT NULL COMMENT '创建时间',
    `update_time` bigint  NOT NULL COMMENT '修改时间',
    `creator`     bigint  NOT NULL COMMENT '创建人',
    `updater`     bigint  NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_date_reward`(`year`,`month`,`day`,`reward_id`) USING BTREE,
    KEY           `idx_creator`(`creator`) USING BTREE,
    KEY           `idx_updater`(`updater`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='当日签到奖励关联表1';

-- seata表

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
VALUES (185757335558, '1号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629954, '2号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (235157848067, '3号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (127783698434, '4号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335559, '5号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629959, '6号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (235157848072, '7号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (127783698440, '8号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335554, '9号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629957, '10号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (235157848065, '11号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (127783698439, '12号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335553, '13号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629953, '14号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (235157848068, '15号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (127783698438, '16号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335561, '17号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629955, '18号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (235157848066, '19号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (127783698437, '20号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`reward_0`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335556, '21号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629960, '22号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (235157848069, '23号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (127783698436, '24号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335560, '25号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629956, '26号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (235157848071, '27号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (127783698442, '28号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (185757335555, '29号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629958, '30号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`reward_1`(`id`, `name`, `detail`, `link`, `type`, `data`, `status`, `create_time`,
                                     `update_time`, `creator`, `updater`)
VALUES (151397629962, '31号签到奖励', '奖励。。。。。。', 'www.baidu.com', 1, '{}', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);



INSERT INTO `marketing_0`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (166429982722, 185757335558, 2021, 9, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (202937237505, 151397629954, 2021, 9, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (127783665667, 235157848067, 2021, 9, 3, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (157848469505, 185757335559, 2021, 9, 5, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (166429982728, 151397629959, 2021, 9, 6, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (202937237507, 235157848072, 2021, 9, 7, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (127783665672, 127783698440, 2021, 9, 8, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (157848469511, 185757335554, 2021, 9, 9, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (166429982721, 151397629957, 2021, 9, 10, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (202937237512, 235157848065, 2021, 9, 11, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (127783665671, 127783698439, 2021, 9, 12, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (157848469506, 185757335553, 2021, 9, 13, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (166429982725, 235157848068, 2021, 9, 15, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (202937237511, 127783698438, 2021, 9, 16, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (127783665668, 185757335561, 2021, 9, 17, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (157848469507, 151397629955, 2021, 9, 18, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (166429982726, 235157848066, 2021, 9, 19, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (202937237510, 127783698437, 2021, 9, 20, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (127783665670, 185757335556, 2021, 9, 21, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (157848469508, 151397629960, 2021, 9, 22, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (166429982729, 235157848069, 2021, 9, 23, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (202937237506, 185757335560, 2021, 9, 25, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (127783665669, 151397629956, 2021, 9, 26, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (157848469512, 235157848071, 2021, 9, 27, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (166429982724, 127783698442, 2021, 9, 28, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_0`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (202937237508, 185757335555, 2021, 9, 29, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_0`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (127783665665, 151397629958, 2021, 9, 30, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `marketing_1`.`sign_reward_today_relation_1`(`id`, `reward_id`, `year`, `month`, `day`, `create_time`,
                                                         `update_time`, `creator`, `updater`)
VALUES (157848469510, 151397629962, 2021, 9, 31, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);


-- member0

CREATE
DATABASE member_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
member_0;

CREATE TABLE `member_basic_0`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `phone`       varchar(16)  NOT NULL COMMENT '手机号',
    `email`       varchar(256) NOT NULL COMMENT '邮箱',
    `password`    varchar(256) DEFAULT NULL COMMENT '登录密码',
    `name`        varchar(128) DEFAULT NULL COMMENT '昵称',
    `icon`        varchar(255) DEFAULT NULL COMMENT '图标url',
    `gender`      tinyint      DEFAULT '1' COMMENT '性别 1男 0女 2其他',
    `status`      tinyint      DEFAULT '1' COMMENT '状态，1可用 0禁用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_phone`(`phone`) USING BTREE,
    UNIQUE KEY `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员基础信息表0';

CREATE TABLE `member_basic_1`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `phone`       varchar(16)  NOT NULL COMMENT '手机号',
    `email`       varchar(256) NOT NULL COMMENT '邮箱',
    `password`    varchar(256) DEFAULT NULL COMMENT '登录密码',
    `name`        varchar(128) DEFAULT NULL COMMENT '昵称',
    `icon`        varchar(255) DEFAULT NULL COMMENT '图标url',
    `gender`      tinyint      DEFAULT '1' COMMENT '性别 1男 0女 2其他',
    `status`      tinyint      DEFAULT '1' COMMENT '状态，1可用 0禁用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_phone`(`phone`) USING BTREE,
    UNIQUE KEY `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员基础信息表1';

CREATE TABLE `member_detail_0`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `member_id`   bigint NOT NULL COMMENT '成员id',
    `real_name`   varchar(50)  DEFAULT NULL COMMENT '真实姓名',
    `id_card_no`  varchar(50)  DEFAULT NULL COMMENT '身份证号',
    `address`     varchar(256) DEFAULT NULL COMMENT '联系地址',
    `email`       varchar(128) DEFAULT NULL COMMENT '电子邮箱',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY           `idx_name`(`real_name`) USING BTREE,
    UNIQUE KEY `idx_id_card`(`id_card_no`) USING BTREE,
    UNIQUE KEY `idx_email`(`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员详细信息表0';

CREATE TABLE `member_detail_1`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `member_id`   bigint NOT NULL COMMENT '成员id',
    `real_name`   varchar(50)  DEFAULT NULL COMMENT '真实姓名',
    `id_card_no`  varchar(50)  DEFAULT NULL COMMENT '身份证号',
    `address`     varchar(256) DEFAULT NULL COMMENT '联系地址',
    `email`       varchar(128) DEFAULT NULL COMMENT '电子邮箱',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY           `idx_name`(`real_name`) USING BTREE,
    UNIQUE KEY `idx_id_card`(`id_card_no`) USING BTREE,
    UNIQUE KEY `idx_email`(`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员详细信息表1';

CREATE TABLE `member_business_0`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `member_id`   bigint NOT NULL COMMENT '成员id',
    `qr_code`     varchar(512) DEFAULT NULL COMMENT '成员二维码链接',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员业务信息表0';

CREATE TABLE `member_business_1`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `member_id`   bigint NOT NULL COMMENT '成员id',
    `qr_code`     varchar(512) DEFAULT NULL COMMENT '成员二维码链接',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员业务信息表1';


-- seata表

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
    `id`          bigint       NOT NULL COMMENT '主键',
    `phone`       varchar(16)  NOT NULL COMMENT '手机号',
    `email`       varchar(256) NOT NULL COMMENT '邮箱',
    `password`    varchar(256) DEFAULT NULL COMMENT '登录密码',
    `name`        varchar(128) DEFAULT NULL COMMENT '昵称',
    `icon`        varchar(255) DEFAULT NULL COMMENT '图标url',
    `gender`      tinyint      DEFAULT '1' COMMENT '性别 1男 0女 2其他',
    `status`      tinyint      DEFAULT '1' COMMENT '状态，1可用 0禁用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_phone`(`phone`) USING BTREE,
    UNIQUE KEY `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员基础信息表0';

CREATE TABLE `member_basic_1`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `phone`       varchar(16)  NOT NULL COMMENT '手机号',
    `email`       varchar(256) NOT NULL COMMENT '邮箱',
    `password`    varchar(256) DEFAULT NULL COMMENT '登录密码',
    `name`        varchar(128) DEFAULT NULL COMMENT '昵称',
    `icon`        varchar(255) DEFAULT NULL COMMENT '图标url',
    `gender`      tinyint      DEFAULT '1' COMMENT '性别 1男 0女 2其他',
    `status`      tinyint      DEFAULT '1' COMMENT '状态，1可用 0禁用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_phone`(`phone`) USING BTREE,
    UNIQUE KEY `idx_email`(`email`) USING BTREE,
    UNIQUE KEY `idx_name`(`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员基础信息表1';

CREATE TABLE `member_detail_0`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `member_id`   bigint NOT NULL COMMENT '成员id',
    `real_name`   varchar(50)  DEFAULT NULL COMMENT '真实姓名',
    `id_card_no`  varchar(50)  DEFAULT NULL COMMENT '身份证号',
    `address`     varchar(256) DEFAULT NULL COMMENT '联系地址',
    `email`       varchar(128) DEFAULT NULL COMMENT '电子邮箱',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY           `idx_name`(`real_name`) USING BTREE,
    UNIQUE KEY `idx_id_card`(`id_card_no`) USING BTREE,
    UNIQUE KEY `idx_email`(`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员详细信息表0';

CREATE TABLE `member_detail_1`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `member_id`   bigint NOT NULL COMMENT '成员id',
    `real_name`   varchar(50)  DEFAULT NULL COMMENT '真实姓名',
    `id_card_no`  varchar(50)  DEFAULT NULL COMMENT '身份证号',
    `address`     varchar(256) DEFAULT NULL COMMENT '联系地址',
    `email`       varchar(128) DEFAULT NULL COMMENT '电子邮箱',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE,
    KEY           `idx_name`(`real_name`) USING BTREE,
    UNIQUE KEY `idx_id_card`(`id_card_no`) USING BTREE,
    UNIQUE KEY `idx_email`(`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员详细信息表1';

CREATE TABLE `member_business_0`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `member_id`   bigint NOT NULL COMMENT '成员id',
    `qr_code`     varchar(512) DEFAULT NULL COMMENT '成员二维码链接',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员业务信息表0';

CREATE TABLE `member_business_1`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `member_id`   bigint NOT NULL COMMENT '成员id',
    `qr_code`     varchar(512) DEFAULT NULL COMMENT '成员二维码链接',
    `create_time` bigint NOT NULL COMMENT '创建时间',
    `update_time` bigint NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_member`(`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='成员业务信息表1';


-- seata表

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
    `id`          bigint       NOT NULL COMMENT '主键',
    `title`       varchar(128) NOT NULL COMMENT '公告标题',
    `content`     varchar(256) DEFAULT NULL COMMENT '公告内容',
    `link`        varchar(256) NOT NULL COMMENT '公告图片链接',
    `type`        tinyint      NOT NULL COMMENT '公告类型 1热门 2最新 3推荐',
    `status`      tinyint      NOT NULL COMMENT '公告状态 0停用 1启用',
    `priority`    int          NOT NULL COMMENT '优先级',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    `updater`     bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_title`(`title`) USING BTREE,
    KEY           `idx_stat_pri`(`status`,`priority`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='公告表0';

CREATE TABLE `bulletin_1`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `title`       varchar(128) NOT NULL COMMENT '公告标题',
    `content`     varchar(256) DEFAULT NULL COMMENT '公告内容',
    `link`        varchar(256) NOT NULL COMMENT '公告图片链接',
    `type`        tinyint      NOT NULL COMMENT '公告类型 1热门 2最新 3推荐',
    `status`      tinyint      NOT NULL COMMENT '公告状态 0停用 1启用',
    `priority`    int          NOT NULL COMMENT '优先级',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    `updater`     bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_title`(`title`) USING BTREE,
    KEY           `idx_stat_pri`(`status`,`priority`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='公告表1';

-- seata表

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
    `id`          bigint       NOT NULL COMMENT '主键',
    `title`       varchar(128) NOT NULL COMMENT '公告标题',
    `content`     varchar(256) DEFAULT NULL COMMENT '公告内容',
    `link`        varchar(256) NOT NULL COMMENT '公告图片链接',
    `type`        tinyint      NOT NULL COMMENT '公告类型 1热门 2最新 3推荐',
    `status`      tinyint      NOT NULL COMMENT '公告状态 0停用 1启用',
    `priority`    int          NOT NULL COMMENT '优先级',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    `updater`     bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_title`(`title`) USING BTREE,
    KEY           `idx_stat_pri`(`status`,`priority`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='公告表0';

CREATE TABLE `bulletin_1`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `title`       varchar(128) NOT NULL COMMENT '公告标题',
    `content`     varchar(256) DEFAULT NULL COMMENT '公告内容',
    `link`        varchar(256) NOT NULL COMMENT '公告图片链接',
    `type`        tinyint      NOT NULL COMMENT '公告类型 1热门 2最新 3推荐',
    `status`      tinyint      NOT NULL COMMENT '公告状态 0停用 1启用',
    `priority`    int          NOT NULL COMMENT '优先级',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    `creator`     bigint       NOT NULL COMMENT '创建人',
    `updater`     bigint       NOT NULL COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_title`(`title`) USING BTREE,
    KEY           `idx_stat_pri`(`status`,`priority`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='公告表1';

-- seata表

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
VALUES (159987531779, '热门公告2', '测试数据', 'www.baidu.com', 1, 1, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `portal_0`.`bulletin_1`(`id`, `title`, `content`, `link`, `type`, `status`, `priority`, `create_time`,
                                    `update_time`, `creator`, `updater`)
VALUES (172872466433, '热门公告1', '测试数据', 'cn.bing.com', 1, 1, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `portal_1`.`bulletin_0`(`id`, `title`, `content`, `link`, `type`, `status`, `priority`, `create_time`,
                                    `update_time`, `creator`, `updater`)
VALUES (127783665667, '最新公告2', '测试数据', 'www.baidu.com', 2, 1, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `portal_1`.`bulletin_1`(`id`, `title`, `content`, `link`, `type`, `status`, `priority`, `create_time`,
                                    `update_time`, `creator`, `updater`)
VALUES (162143436801, '最新公告1', '测试数据', 'cn.bing.com', 2, 1, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `portal_0`.`bulletin_0`(`id`, `title`, `content`, `link`, `type`, `status`, `priority`, `create_time`,
                                    `update_time`, `creator`, `updater`)
VALUES (159987531778, '推荐公告2', '测试数据', 'www.baidu.com', 3, 1, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);

INSERT INTO `portal_1`.`bulletin_1`(`id`, `title`, `content`, `link`, `type`, `status`, `priority`, `create_time`,
                                    `update_time`, `creator`, `updater`)
VALUES (162143436802, '推荐公告1', '测试数据', 'cn.bing.com', 3, 1, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);


-- business0

CREATE
DATABASE business_0 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
business_0;

CREATE TABLE `article_0`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `author_id`   bigint       NOT NULL COMMENT '成员/作者id',
    `type`        tinyint      NOT NULL COMMENT '文章类型 1爬坑经验 2推荐 3吐槽 4发牢骚 5晒物 6水文 待添加',
    `title`       varchar(256) NOT NULL COMMENT '文章标题',
    `author`      varchar(256) NOT NULL COMMENT '文章作者',
    `content`     mediumtext   NOT NULL COMMENT '文章内容',
    `favorites`   bigint       NOT NULL DEFAULT '0' COMMENT '收藏量',
    `readings`    bigint       NOT NULL DEFAULT '0' COMMENT '阅读量',
    `comments`    bigint       NOT NULL DEFAULT '0' COMMENT '评论量',
    `likes`       bigint       NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`      bigint       NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`      tinyint      NOT NULL COMMENT '文章状态 0停用 1启用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY           `idx_au_type_status_create`(`author_id`,`type`,`status`,`create_time`) USING BTREE,
    KEY           `idx_favorites`(`favorites`) USING BTREE,
    KEY           `idx_readings`(`readings`) USING BTREE,
    KEY           `idx_comments`(`comments`) USING BTREE,
    KEY           `idx_likes`(`likes`) USING BTREE,
    KEY           `idx_boring`(`boring`) USING BTREE,
    KEY           `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章表0';


CREATE TABLE `article_1`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `author_id`   bigint       NOT NULL COMMENT '成员/作者id',
    `type`        tinyint      NOT NULL COMMENT '文章类型 1爬坑经验 2推荐 3吐槽 4发牢骚 5晒物 6水文 待添加',
    `title`       varchar(256) NOT NULL COMMENT '文章标题',
    `author`      varchar(256) NOT NULL COMMENT '文章作者',
    `content`     mediumtext   NOT NULL COMMENT '文章内容',
    `favorites`   bigint       NOT NULL DEFAULT '0' COMMENT '收藏量',
    `readings`    bigint       NOT NULL DEFAULT '0' COMMENT '阅读量',
    `comments`    bigint       NOT NULL DEFAULT '0' COMMENT '评论量',
    `likes`       bigint       NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`      bigint       NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`      tinyint      NOT NULL COMMENT '文章状态 0停用 1启用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY           `idx_au_type_status_create`(`author_id`,`type`,`status`,`create_time`) USING BTREE,
    KEY           `idx_favorites`(`favorites`) USING BTREE,
    KEY           `idx_readings`(`readings`) USING BTREE,
    KEY           `idx_comments`(`comments`) USING BTREE,
    KEY           `idx_likes`(`likes`) USING BTREE,
    KEY           `idx_boring`(`boring`) USING BTREE,
    KEY           `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章表1';



CREATE TABLE `link_0`
(
    `id`            bigint        NOT NULL COMMENT '主键',
    `sub_id`        bigint        NOT NULL COMMENT '主题id',
    `sub_type`      tinyint       NOT NULL COMMENT '主题类型 1文章 2链接 待添加',
    `sub_author_id` bigint        NOT NULL COMMENT '主题作者id',
    `link_url`      varchar(1024) NOT NULL COMMENT '链接url',
    `content`       varchar(1024) NOT NULL COMMENT '链接相关内容/描述',
    `priority`      int           NOT NULL COMMENT '优先级',
    `favorites`     bigint        NOT NULL DEFAULT '0' COMMENT '收藏量',
    `readings`      bigint        NOT NULL DEFAULT '0' COMMENT '阅读量',
    `comments`      bigint        NOT NULL DEFAULT '0' COMMENT '评论量',
    `likes`         bigint        NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`        bigint        NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`        tinyint       NOT NULL COMMENT '状态 0停用 1启用',
    `create_time`   bigint        NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype_status`(`sub_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_readings`(`readings`) USING BTREE,
    KEY             `idx_comments`(`comments`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='相关链接表0';


CREATE TABLE `link_1`
(
    `id`            bigint        NOT NULL COMMENT '主键',
    `sub_id`        bigint        NOT NULL COMMENT '主题id',
    `sub_type`      tinyint       NOT NULL COMMENT '主题类型 1文章 2链接 待添加',
    `sub_author_id` bigint        NOT NULL COMMENT '主题作者id',
    `link_url`      varchar(1024) NOT NULL COMMENT '链接url',
    `content`       varchar(1024) NOT NULL COMMENT '链接相关内容/描述',
    `priority`      int           NOT NULL COMMENT '优先级',
    `favorites`     bigint        NOT NULL DEFAULT '0' COMMENT '收藏量',
    `readings`      bigint        NOT NULL DEFAULT '0' COMMENT '阅读量',
    `comments`      bigint        NOT NULL DEFAULT '0' COMMENT '评论量',
    `likes`         bigint        NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`        bigint        NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`        tinyint       NOT NULL COMMENT '状态 0停用 1启用',
    `create_time`   bigint        NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype_status`(`sub_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_readings`(`readings`) USING BTREE,
    KEY             `idx_comments`(`comments`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='相关链接表1';



CREATE TABLE `comment_0`
(
    `id`            bigint     NOT NULL COMMENT '主键',
    `sub_id`        bigint     NOT NULL COMMENT '主题id',
    `sub_type`      tinyint    NOT NULL COMMENT '主题类型 1文章 2链接 待添加',
    `sub_author_id` bigint     NOT NULL COMMENT '主题作者id',
    `from_id`       bigint     NOT NULL COMMENT '评论人id',
    `content`       mediumtext NOT NULL COMMENT '评论内容',
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT '收藏量',
    `replies`       bigint     NOT NULL DEFAULT '0' COMMENT '回复量',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`        tinyint    NOT NULL COMMENT '状态 0停用 1启用',
    `create_time`   bigint     NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype`(`sub_id`,`sub_type`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_replies`(`replies`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE,
    KEY             `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='主题评论表0';

CREATE TABLE `comment_1`
(
    `id`            bigint     NOT NULL COMMENT '主键',
    `sub_id`        bigint     NOT NULL COMMENT '主题id',
    `sub_type`      tinyint    NOT NULL COMMENT '主题类型 1文章 2链接 待添加',
    `sub_author_id` bigint     NOT NULL COMMENT '主题作者id',
    `from_id`       bigint     NOT NULL COMMENT '评论人id',
    `content`       mediumtext NOT NULL COMMENT '评论内容',
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT '收藏量',
    `replies`       bigint     NOT NULL DEFAULT '0' COMMENT '回复量',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`        tinyint    NOT NULL COMMENT '状态 0停用 1启用',
    `create_time`   bigint     NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype`(`sub_id`,`sub_type`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_replies`(`replies`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE,
    KEY             `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='主题评论表1';



CREATE TABLE `reply_0`
(
    `id`            bigint     NOT NULL COMMENT '主键',
    `sub_id`        bigint     NOT NULL COMMENT '主题id',
    `sub_type`      tinyint    NOT NULL COMMENT '主题类型 1文章 2链接 待添加',
    `sub_author_id` bigint     NOT NULL COMMENT '主题作者id',
    `comment_id`    bigint     NOT NULL COMMENT '评论id',
    `from_id`       bigint     NOT NULL COMMENT '回复人id',
    `to_id`         bigint     NOT NULL COMMENT '被回复人或被回复评论id',
    `type`          tinyint    NOT NULL COMMENT '回复类型 1回复评论的回复 2回复他人回复的回复',
    `content`       mediumtext NOT NULL COMMENT '回复内容',
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT '收藏量',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`        tinyint    NOT NULL COMMENT '状态 0停用 1启用',
    `create_time`   bigint     NOT NULL COMMENT '创建时间',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='回复表0';

CREATE TABLE `reply_1`
(
    `id`            bigint     NOT NULL COMMENT '主键',
    `sub_id`        bigint     NOT NULL COMMENT '主题id',
    `sub_type`      tinyint    NOT NULL COMMENT '主题类型 1文章 2链接 待添加',
    `sub_author_id` bigint     NOT NULL COMMENT '主题作者id',
    `comment_id`    bigint     NOT NULL COMMENT '评论id',
    `from_id`       bigint     NOT NULL COMMENT '回复人id',
    `to_id`         bigint     NOT NULL COMMENT '被回复人或被回复评论id',
    `type`          tinyint    NOT NULL COMMENT '回复类型 1回复评论的回复 2回复他人回复的回复',
    `content`       mediumtext NOT NULL COMMENT '回复内容',
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT '收藏量',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`        tinyint    NOT NULL COMMENT '状态 0停用 1启用',
    `create_time`   bigint     NOT NULL COMMENT '创建时间',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='回复表1';


-- business1

CREATE
DATABASE business_1 CHARACTER SET utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE
business_1;

CREATE TABLE `article_0`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `author_id`   bigint       NOT NULL COMMENT '成员/作者id',
    `type`        tinyint      NOT NULL COMMENT '文章类型 1爬坑经验 2推荐 3吐槽 4发牢骚 5晒物 6水文 待添加',
    `title`       varchar(256) NOT NULL COMMENT '文章标题',
    `author`      varchar(256) NOT NULL COMMENT '文章作者',
    `content`     mediumtext   NOT NULL COMMENT '文章内容',
    `favorites`   bigint       NOT NULL DEFAULT '0' COMMENT '收藏量',
    `readings`    bigint       NOT NULL DEFAULT '0' COMMENT '阅读量',
    `comments`    bigint       NOT NULL DEFAULT '0' COMMENT '评论量',
    `likes`       bigint       NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`      bigint       NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`      tinyint      NOT NULL COMMENT '文章状态 0停用 1启用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY           `idx_au_type_status_create`(`author_id`,`type`,`status`,`create_time`) USING BTREE,
    KEY           `idx_favorites`(`favorites`) USING BTREE,
    KEY           `idx_readings`(`readings`) USING BTREE,
    KEY           `idx_comments`(`comments`) USING BTREE,
    KEY           `idx_likes`(`likes`) USING BTREE,
    KEY           `idx_boring`(`boring`) USING BTREE,
    KEY           `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章表0';


CREATE TABLE `article_1`
(
    `id`          bigint       NOT NULL COMMENT '主键',
    `author_id`   bigint       NOT NULL COMMENT '成员/作者id',
    `type`        tinyint      NOT NULL COMMENT '文章类型 1爬坑经验 2推荐 3吐槽 4发牢骚 5晒物 6水文 待添加',
    `title`       varchar(256) NOT NULL COMMENT '文章标题',
    `author`      varchar(256) NOT NULL COMMENT '文章作者',
    `content`     mediumtext   NOT NULL COMMENT '文章内容',
    `favorites`   bigint       NOT NULL DEFAULT '0' COMMENT '收藏量',
    `readings`    bigint       NOT NULL DEFAULT '0' COMMENT '阅读量',
    `comments`    bigint       NOT NULL DEFAULT '0' COMMENT '评论量',
    `likes`       bigint       NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`      bigint       NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`      tinyint      NOT NULL COMMENT '文章状态 0停用 1启用',
    `create_time` bigint       NOT NULL COMMENT '创建时间',
    `update_time` bigint       NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY           `idx_au_type_status_create`(`author_id`,`type`,`status`,`create_time`) USING BTREE,
    KEY           `idx_favorites`(`favorites`) USING BTREE,
    KEY           `idx_readings`(`readings`) USING BTREE,
    KEY           `idx_comments`(`comments`) USING BTREE,
    KEY           `idx_likes`(`likes`) USING BTREE,
    KEY           `idx_boring`(`boring`) USING BTREE,
    KEY           `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章表1';



CREATE TABLE `link_0`
(
    `id`            bigint        NOT NULL COMMENT '主键',
    `sub_id`        bigint        NOT NULL COMMENT '主题id',
    `sub_type`      tinyint       NOT NULL COMMENT '主题类型 1文章 2链接 待添加',
    `sub_author_id` bigint        NOT NULL COMMENT '主题作者id',
    `link_url`      varchar(1024) NOT NULL COMMENT '链接url',
    `content`       varchar(1024) NOT NULL COMMENT '链接相关内容/描述',
    `priority`      int           NOT NULL COMMENT '优先级',
    `favorites`     bigint        NOT NULL DEFAULT '0' COMMENT '收藏量',
    `readings`      bigint        NOT NULL DEFAULT '0' COMMENT '阅读量',
    `comments`      bigint        NOT NULL DEFAULT '0' COMMENT '评论量',
    `likes`         bigint        NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`        bigint        NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`        tinyint       NOT NULL COMMENT '状态 0停用 1启用',
    `create_time`   bigint        NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype_status`(`sub_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_readings`(`readings`) USING BTREE,
    KEY             `idx_comments`(`comments`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='相关链接表0';


CREATE TABLE `link_1`
(
    `id`            bigint        NOT NULL COMMENT '主键',
    `sub_id`        bigint        NOT NULL COMMENT '主题id',
    `sub_type`      tinyint       NOT NULL COMMENT '主题类型 1文章 2链接 待添加',
    `sub_author_id` bigint        NOT NULL COMMENT '主题作者id',
    `link_url`      varchar(1024) NOT NULL COMMENT '链接url',
    `content`       varchar(1024) NOT NULL COMMENT '链接相关内容/描述',
    `priority`      int           NOT NULL COMMENT '优先级',
    `favorites`     bigint        NOT NULL DEFAULT '0' COMMENT '收藏量',
    `readings`      bigint        NOT NULL DEFAULT '0' COMMENT '阅读量',
    `comments`      bigint        NOT NULL DEFAULT '0' COMMENT '评论量',
    `likes`         bigint        NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`        bigint        NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`        tinyint       NOT NULL COMMENT '状态 0停用 1启用',
    `create_time`   bigint        NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype_status`(`sub_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_readings`(`readings`) USING BTREE,
    KEY             `idx_comments`(`comments`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='相关链接表1';



CREATE TABLE `comment_0`
(
    `id`            bigint     NOT NULL COMMENT '主键',
    `sub_id`        bigint     NOT NULL COMMENT '主题id',
    `sub_type`      tinyint    NOT NULL COMMENT '主题类型 1文章 2链接 待添加',
    `sub_author_id` bigint     NOT NULL COMMENT '主题作者id',
    `from_id`       bigint     NOT NULL COMMENT '评论人id',
    `content`       mediumtext NOT NULL COMMENT '评论内容',
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT '收藏量',
    `replies`       bigint     NOT NULL DEFAULT '0' COMMENT '回复量',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`        tinyint    NOT NULL COMMENT '状态 0停用 1启用',
    `create_time`   bigint     NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype`(`sub_id`,`sub_type`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_replies`(`replies`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE,
    KEY             `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='主题评论表0';


CREATE TABLE `comment_1`
(
    `id`            bigint     NOT NULL COMMENT '主键',
    `sub_id`        bigint     NOT NULL COMMENT '主题id',
    `sub_type`      tinyint    NOT NULL COMMENT '主题类型 1文章 2链接 待添加',
    `sub_author_id` bigint     NOT NULL COMMENT '主题作者id',
    `from_id`       bigint     NOT NULL COMMENT '评论人id',
    `content`       mediumtext NOT NULL COMMENT '评论内容',
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT '收藏量',
    `replies`       bigint     NOT NULL DEFAULT '0' COMMENT '回复量',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`        tinyint    NOT NULL COMMENT '状态 0停用 1启用',
    `create_time`   bigint     NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY             `idx_sid_stype`(`sub_id`,`sub_type`) USING BTREE,
    KEY             `idx_sau_stype_status`(`sub_author_id`,`sub_type`,`status`) USING BTREE,
    KEY             `idx_favorites`(`favorites`) USING BTREE,
    KEY             `idx_replies`(`replies`) USING BTREE,
    KEY             `idx_likes`(`likes`) USING BTREE,
    KEY             `idx_boring`(`boring`) USING BTREE,
    KEY             `idx_create`(`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='主题评论表1';



CREATE TABLE `reply_0`
(
    `id`            bigint     NOT NULL COMMENT '主键',
    `sub_id`        bigint     NOT NULL COMMENT '主题id',
    `sub_type`      tinyint    NOT NULL COMMENT '主题类型 1文章 2链接 待添加',
    `sub_author_id` bigint     NOT NULL COMMENT '主题作者id',
    `comment_id`    bigint     NOT NULL COMMENT '评论id',
    `from_id`       bigint     NOT NULL COMMENT '回复人id',
    `to_id`         bigint     NOT NULL COMMENT '被回复人或被回复评论id',
    `type`          tinyint    NOT NULL COMMENT '回复类型 1回复评论的回复 2回复他人回复的回复',
    `content`       mediumtext NOT NULL COMMENT '回复内容',
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT '收藏量',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`        tinyint    NOT NULL COMMENT '状态 0停用 1启用',
    `create_time`   bigint     NOT NULL COMMENT '创建时间',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='回复表0';

CREATE TABLE `reply_1`
(
    `id`            bigint     NOT NULL COMMENT '主键',
    `sub_id`        bigint     NOT NULL COMMENT '主题id',
    `sub_type`      tinyint    NOT NULL COMMENT '主题类型 1文章 2链接 待添加',
    `sub_author_id` bigint     NOT NULL COMMENT '主题作者id',
    `comment_id`    bigint     NOT NULL COMMENT '评论id',
    `from_id`       bigint     NOT NULL COMMENT '回复人id',
    `to_id`         bigint     NOT NULL COMMENT '被回复人或被回复评论id',
    `type`          tinyint    NOT NULL COMMENT '回复类型 1回复评论的回复 2回复他人回复的回复',
    `content`       mediumtext NOT NULL COMMENT '回复内容',
    `favorites`     bigint     NOT NULL DEFAULT '0' COMMENT '收藏量',
    `likes`         bigint     NOT NULL DEFAULT '0' COMMENT '点赞数量',
    `boring`        bigint     NOT NULL DEFAULT '0' COMMENT '点踩数量',
    `status`        tinyint    NOT NULL COMMENT '状态 0停用 1启用',
    `create_time`   bigint     NOT NULL COMMENT '创建时间',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='回复表1';
