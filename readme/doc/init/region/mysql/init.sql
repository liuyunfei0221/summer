USE
base;

CREATE TABLE `country`
(
    `id`               bigint       NOT NULL COMMENT 'id',
    `name`             varchar(255) NOT NULL COMMENT 'name',
    `native_name`      varchar(255) DEFAULT '' COMMENT 'native',
    `numeric_code`     char(4)      DEFAULT '' COMMENT 'numeric code',
    `country_code`     char(4)      DEFAULT '' COMMENT 'iso2',
    `phone_code`       varchar(255) DEFAULT '' COMMENT 'phone_code',
    `capital`          varchar(255) DEFAULT '' COMMENT 'capital',
    `currency`         varchar(255) DEFAULT '' COMMENT 'currency',
    `currency_symbol`  varchar(255) DEFAULT '' COMMENT 'currency_symbol',
    `top_level_domain` varchar(255) DEFAULT '' COMMENT 'top-level domain',
    `region`           varchar(255) DEFAULT '' COMMENT 'region',
    `emoji`            varchar(255) DEFAULT '' COMMENT 'emoji',
    `emojiU`           varchar(255) DEFAULT '' COMMENT 'emoji U',
    `status`           tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time`      bigint       DEFAULT '1' COMMENT 'data create time',
    `update_time`      bigint       DEFAULT '1' COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY                `idx_country_code`(`country_code`) USING BTREE,
    KEY                `idx_phone_code`(`phone_code`) USING BTREE,
    KEY                `idx_status`(`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of country';

CREATE TABLE `state`
(
    `id`           bigint       NOT NULL COMMENT 'id',
    `country_id`   bigint       NOT NULL COMMENT 'country id',
    `name`         varchar(255) NOT NULL COMMENT 'name',
    `fips_code`    varchar(255) DEFAULT '' COMMENT 'fips code',
    `country_code` char(16)     NOT NULL COMMENT 'country code',
    `state_code`   char(16)     DEFAULT '' COMMENT 'state code',
    `status`       tinyint      DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time`  bigint       DEFAULT '1' COMMENT 'data create time',
    `update_time`  bigint       DEFAULT '1' COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY            `idx_country_status`(`country_id`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of state';

CREATE TABLE `city`
(
    `id`           bigint       NOT NULL COMMENT 'id',
    `country_id`   bigint       NOT NULL COMMENT 'country id',
    `state_id`     bigint       NOT NULL COMMENT 'state id',
    `name`         varchar(255) NOT NULL COMMENT 'name',
    `country_code` char(16)     NOT NULL COMMENT 'country code',
    `state_code`   char(16)     NOT NULL COMMENT 'state code',
    `status`       tinyint DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time`  bigint  DEFAULT '1' COMMENT 'data create time',
    `update_time`  bigint  DEFAULT '1' COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY            `idx_country`(`country_id`) USING BTREE,
    KEY            `idx_state_status`(`state_id`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of city';

CREATE TABLE `area`
(
    `id`           bigint       NOT NULL COMMENT 'id',
    `country_id`   bigint       NOT NULL COMMENT 'country id',
    `state_id`     bigint       NOT NULL COMMENT 'state id',
    `city_id`      bigint       NOT NULL COMMENT 'city id',
    `name`         varchar(255) NOT NULL COMMENT 'name',
    `country_code` char(16)     NOT NULL COMMENT 'country code',
    `state_code`   char(16)     NOT NULL COMMENT 'state code',
    `status`       tinyint DEFAULT '1' COMMENT 'data status: 1-valid 0-invalid',
    `create_time`  bigint  DEFAULT '1' COMMENT 'data create time',
    `update_time`  bigint  DEFAULT '1' COMMENT 'data update time',
    PRIMARY KEY (`id`),
    KEY            `idx_country`(`country_id`) USING BTREE,
    KEY            `idx_state`(`state_id`) USING BTREE,
    KEY            `idx_city_status`(`city_id`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='table of area';