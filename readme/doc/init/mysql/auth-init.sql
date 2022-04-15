-- noinspection SpellCheckingInspectionForFile

-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- auth
-- init

-- resources

USE
auth;

INSERT INTO `auth`.`resource`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                              response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                              `name`, `description`, `create_time`, `update_time`, `creator`, `updater`)

-- base api

VALUES (100001, 'GET', 'blue-base', '/countries', b'0', b'1', b'1', b'0', b'1', 1,
        'countries', 'countries', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100002, 'GET', 'blue-base', '/states/{pid}', b'0', b'1', b'1', b'0', b'1', 1,
        'states', 'states', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100003, 'GET', 'blue-base', '/cities/{pid}', b'0', b'1', b'1', b'0', b'1', 1,
        'cities', 'cities', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100004, 'GET', 'blue-base', '/areas/{pid}', b'0', b'1', b'1', b'0', b'1', 1,
        'areas', 'areas', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100005, 'GET', 'blue-base', '/language', b'0', b'1', b'1', b'0', b'1', 1,
        'language', 'language', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100006, 'GET', 'blue-base', '/dictType', b'0', b'1', b'1', b'0',
        b'1', 1, 'query dict types', 'query dict types', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100007, 'GET', 'blue-base', '/bulletin/{type}', b'0', b'1', b'1', b'0', b'1', 1,
        'test get endpoint', 'test get endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100008, 'POST', 'blue-base', '/bulletin/{type}', b'0', b'1', b'1', b'0', b'1', 1,
        'test post endpoint', 'test post endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),


-- base manage


-- verify api

       (120001, 'GET', 'blue-verify', '/verify/generate', b'0', b'1', b'1', b'0', b'1', 1,
        'generate verify default', 'generate verify default', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (120002, 'POST', 'blue-verify', '/verify/generate', b'0', b'1', b'1', b'0', b'1', 1,
        'generate verify with param', 'generate verify with param', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),


-- verify manage


-- auth api

       (160001, 'POST', 'blue-auth', '/auth/login', b'0', b'1', b'1', b'1', b'1', 1,
        'login', 'login', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160002, 'PUT', 'blue-auth', '/auth/access/refresh', b'0', b'1', b'1', b'1', b'1', 1,
        'refresh access', 'refresh access', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160003, 'PUT', 'blue-auth', '/auth/secret', b'1', b'1', b'1', b'0', b'1', 1,
        'refresh private key', 'refresh private key', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160004, 'POST', 'blue-auth', '/auth/credential', b'1', b'1', b'1', b'0', b'1', 1,
        'credential setting up', 'credential setting up', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160005, 'PUT', 'blue-auth', '/auth/credential', b'1', b'1', b'1', b'0', b'1', 1,
        'credential update', 'credential update', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160006, 'PUT', 'blue-auth', '/auth/access', b'1', b'1', b'1', b'0', b'1', 1,
        'update access', 'update access', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160007, 'POST', 'blue-auth', '/auth/access', b'0', b'1', b'1', b'0', b'1', 1,
        'reset access', 'reset access', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160008, 'PUT', 'blue-auth', '/auth/logout', b'1', b'1', b'1', b'0', b'1', 1,
        'logout', 'logout', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160009, 'DELETE', 'blue-auth', '/auth/logout', b'1', b'1', b'1', b'0', b'1', 1,
        'logout everywhere', 'logout everywhere', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160010, 'GET', 'blue-auth', '/auth/authority', b'1', b'1', b'1', b'0', b'1', 1,
        'query authority', 'query authority', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- auth manage

       (170001, 'DELETE', 'blue-auth', '/blue-auth/manager/auth', b'1', b'1', b'1', b'0', b'1', 2,
        'invalidate member auth', 'invalidate member auth', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (170002, 'POST', 'blue-auth', '/manager/resources', b'1', b'1', b'1', b'1', b'1', 2,
        'resource list', 'resource list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170003, 'POST', 'blue-auth', '/manager/resource', b'1', b'1', b'1', b'1', b'1', 2,
        'insert resource', 'insert resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170004, 'PUT', 'blue-auth', '/manager/resource', b'1', b'1', b'1', b'1', b'1', 2,
        'update resource', 'update resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170005, 'DELETE', 'blue-auth', '/manager/resource/{id}', b'1', b'1', b'1', b'0', b'1', 2,
        'delete resource', 'delete resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170006, 'POST', 'blue-auth', '/manager/resource/auth', b'1', b'1', b'1', b'1', b'1', 2,
        'resource auth', 'resource auth', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (170007, 'POST', 'blue-auth', '/manager/roles', b'1', b'1', b'1', b'1', b'1', 2,
        'role list', 'role list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170008, 'POST', 'blue-auth', '/manager/role', b'1', b'1', b'1', b'1', b'1', 2,
        'insert role', 'insert role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170009, 'PUT', 'blue-auth', '/manager/role', b'1', b'1', b'1', b'1', b'1', 2,
        'update role', 'update role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170010, 'DELETE', 'blue-auth', '/manager/role/{id}', b'1', b'1', b'1', b'0', b'1', 2,
        'delete role', 'delete role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170011, 'POST', 'blue-auth', '/manager/role/auth', b'1', b'1', b'1', b'1', b'1', 2,
        'role auth', 'role auth', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (170012, 'PUT', 'blue-auth', '/manager/relation/role-res', b'1', b'1', b'1', b'1', b'1', 2,
        'update role-resources-relation', 'update role-resources-relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170013, 'PUT', 'blue-auth', '/manager/relation/mem-role', b'1', b'1', b'1', b'1', b'1', 2,
        'update member-role-relation', 'update member-role-relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),


-- member api

       (180001, 'POST', 'blue-member', '/registry', b'0', b'1', b'1', b'1', b'1', 1,
        'member registry', 'member registry', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180002, 'GET', 'blue-member', '/member', b'1', b'1', b'1', b'0', b'1', 1,
        'member info', 'member info', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- member manage

       (190001, 'POST', 'blue-member', '/manager/members', b'1', b'1', b'1', b'1', b'1', 2,
        'member list', 'member list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (190002, 'POST', 'blue-member', '/manager/authorities', b'1', b'1', b'1', b'1', b'1', 2,
        'authority list', 'authority list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),


-- finance api
       (200001, 'GET', 'blue-finance', '/finance/balance', b'1', b'1', b'1', b'0', b'1', 1,
        'query balance', 'query balance', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200002, 'POST', 'blue-finance', '/withdraw', b'1', b'0', b'0', 1, b'1', b'1',
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
       (200008, 'DELETE', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1',
        b'1', 3, 'DELETE dynamic endpoint', 'DELETE dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200009, 'OPTIONS', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1',
        b'1', 3, 'OPTIONS dynamic endpoint', 'OPTIONS dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- finance manage


-- media api

       (220001, 'POST', 'blue-media', '/file/upload', b'1', b'1', b'1', b'1', b'1', 1,
        'media upload of api', 'media upload of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220002, 'POST', 'blue-media', '/file/download', b'1', b'1', b'1', b'1', b'0', 1,
        'file download of api', 'file download of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220003, 'POST', 'blue-media', '/attachment/list', b'1', b'1', b'1', b'1', b'1', 1,
        'attachment list of api', 'attachment list of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (220004, 'POST', 'blue-media', '/attachment/withdraw', b'1', b'0', b'0', b'1', b'1', 1,
        'withdraw/test encrypt in media', 'withdraw/test encrypt in media', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220005, 'GET', 'blue-media', '/mail/send', b'0', b'1', b'1', b'0', b'1', 2,
        'test send', 'test send', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220006, 'GET', 'blue-media', '/mail/read', b'0', b'1', b'1', b'0', b'1', 2,
        'test read', 'test read', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),


-- media manage


-- portal api

       (250001, 'GET', 'blue-portal', '/bulletin/{type}', b'0', b'1', b'1', b'0', b'1', 1,
        'bulletin list of api', 'bulletin list of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (250002, 'GET', 'blue-portal', '/formatter/{formatter}.html', b'1', b'1', b'1', b'0', b'1', 1,
        'formatter test', 'formatter test', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (250003, 'GET', 'blue-portal', '/fallBack', b'0', b'1', b'1', b'0', b'1', 1,
        'GET fallback', 'GET fallback', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (250004, 'POST', 'blue-portal', '/fallBack', b'0', b'1', b'1', b'1', b'1', 1,
        'POST fallback', 'POST fallback', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- portal manage


-- marketing api

       (270001, 'POST', 'blue-marketing', '/signIn', b'1', b'1', b'1', b'0', b'1', 1,
        'sign in', 'sign in', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (270002, 'GET', 'blue-marketing', '/signIn', b'1', b'1', b'1', b'0', b'1', 1,
        'query sign in record by month', 'query sign in record by month', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- marketing manage


-- shine api

       (290001, 'GET', 'blue-shine', '/shine', b'0', b'1', b'1', b'0', b'1', 1,
        'commonweal information', 'commonweal information', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- shine manage


-- data api

-- data manage

       (320001, 'POST', 'blue-lake', '/events', b'0', b'1', b'1', b'1', b'1', 2,
        'test lake event', 'test lake event', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (320002, 'POST', 'blue-analyze', '/statistics/active/simple', b'0', b'1', b'1', b'1', b'1', 2,
        'statistics active simple', 'statistics active simple', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (320003, 'POST', 'blue-analyze', '/statistics/active/merge', b'0', b'1', b'1', b'1', b'1', 2,
        'statistics merge active', 'statistics merge active', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (320004, 'POST', 'blue-analyze', '/statistics/active/summary', b'0', b'1', b'1', b'0', b'1', 2,
        'statistics summary', 'statistics summary', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);


-- role

INSERT INTO `auth`.`role`(`id`, `name`, `description`, `level`, `is_default`, `create_time`, `update_time`, `creator`,
                          `updater`)
VALUES (1, 'blue', 'blue', 0, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (2, 'normal', 'normal', 999999999, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);


-- super admin res
INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                       `updater`)
SELECT id + 1000000,
       1,
       id,
       UNIX_TIMESTAMP(),
       UNIX_TIMESTAMP(),
       1,
       1
FROM `auth`.`resource`;


-- normal res
INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                       `updater`)
SELECT id,
       2,
       id,
       UNIX_TIMESTAMP(),
       UNIX_TIMESTAMP(),
       1,
       1
FROM `auth`.`resource`
WHERE `type` = 1;




