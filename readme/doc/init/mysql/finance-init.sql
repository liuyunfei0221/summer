-- noinspection SpellCheckingInspectionForFile

-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile


USE
finance_1;


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





