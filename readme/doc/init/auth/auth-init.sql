-- noinspection SpellCheckingInspectionForFile

-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- auth
-- init

-- resources

INSERT INTO `auth`.`resource`(`id`, `request_method`, `module`, `uri`, `authenticate`, `request_un_decryption`,
                              response_un_encryption, `existence_request_body`, `existence_response_body`, `type`,
                              `name`, `description`, `create_time`, `update_time`, `creator`, `updater`)

-- base api

VALUES (110001, 'GET', 'blue-base', '/countries', b'0', b'1', b'1', b'0', b'1', 1,
        'countries', 'countries', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110002, 'GET', 'blue-base', '/states/{pid}', b'0', b'1', b'1', b'0', b'1', 1,
        'states', 'states', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110003, 'GET', 'blue-base', '/cities/{pid}', b'0', b'1', b'1', b'0', b'1', 1,
        'cities', 'cities', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110004, 'GET', 'blue-base', '/areas/{pid}', b'0', b'1', b'1', b'0', b'1', 1,
        'areas', 'areas', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110005, 'GET', 'blue-base', '/language', b'0', b'1', b'1', b'0', b'1', 1,
        'language', 'language', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110006, 'GET', 'blue-base', '/dictType', b'0', b'1', b'1', b'0',
        b'1', 1, 'query dict types', 'query dict types', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110007, 'GET', 'blue-base', '/bulletin/{type}', b'0', b'1', b'1', b'0', b'1', 1,
        'test get endpoint', 'test get endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110008, 'POST', 'blue-base', '/bulletin/{type}', b'0', b'1', b'1', b'0', b'1', 1,
        'test post endpoint', 'test post endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),


-- base manage


-- verify api

       (130001, 'GET', 'blue-verify', '/verify/generate', b'0', b'1', b'1', b'0', b'1', 1,
        'generate verify default', 'generate verify default', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (130002, 'POST', 'blue-verify', '/verify/generate', b'0', b'1', b'1', b'0', b'1', 1,
        'generate verify with param', 'generate verify with param', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),


-- verify manage


-- auth api

       (150001, 'POST', 'blue-auth', '/auth/login', b'0', b'1', b'1', b'1', b'1', 1,
        'login', 'login', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (150002, 'PUT', 'blue-auth', '/auth/access/refresh', b'0', b'1', b'1', b'1', b'1', 1,
        'refresh access', 'refresh access', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (150003, 'PUT', 'blue-auth', '/auth/secret', b'1', b'1', b'1', b'0', b'1', 1,
        'refresh private key', 'refresh private key', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (150004, 'POST', 'blue-auth', '/blue-auth/credential', b'1', b'1', b'1', b'0', b'1', 1,
        'credential setting up', 'credential setting up', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (150005, 'PUT', 'blue-auth', '/auth/access', b'1', b'1', b'1', b'0', b'1', 1,
        'update access', 'update access', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (150006, 'PUT', 'blue-auth', '/auth/access/reset', b'1', b'1', b'1', b'0', b'1', 1,
        'reset access', 'reset access', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (150007, 'DELETE', 'blue-auth', '/auth/logout', b'1', b'1', b'1', b'0', b'1', 1,
        'logout', 'logout', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (150008, 'DELETE', 'blue-auth', '/auth/logout/everywhere', b'1', b'1', b'1', b'0', b'1', 1,
        'logout everywhere', 'logout everywhere', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (150009, 'GET', 'blue-auth', '/auth/authority', b'1', b'1', b'1', b'0', b'1', 1,
        'query authority', 'query authority', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- auth manage

       (160001, 'POST', 'blue-auth', '/blue-auth/manager/auth', b'1', b'1', b'1', b'0', b'1', 2,
        'invalidate member auth', 'invalidate member auth', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160002, 'POST', 'blue-auth', '/manager/resources', b'1', b'1', b'1', b'1', b'1', 2,
        'resource list', 'resource list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160003, 'POST', 'blue-auth', '/manager/resource', b'1', b'1', b'1', b'1', b'1', 2,
        'insert resource', 'insert resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160004, 'PUT', 'blue-auth', '/manager/resource', b'1', b'1', b'1', b'1', b'1', 2,
        'update resource', 'update resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160005, 'DELETE', 'blue-auth', '/manager/resource/{id}', b'1', b'1', b'1', b'0', b'1', 2,
        'delete resource', 'delete resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160006, 'POST', 'blue-auth', '/manager/resource/auth', b'1', b'1', b'1', b'1', b'1', 2,
        'resource auth', 'resource auth', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (160007, 'POST', 'blue-auth', '/manager/roles', b'1', b'1', b'1', b'1', b'1', 2,
        'role list', 'role list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160008, 'POST', 'blue-auth', '/manager/role', b'1', b'1', b'1', b'1', b'1', 2,
        'insert role', 'insert role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160009, 'PUT', 'blue-auth', '/manager/role', b'1', b'1', b'1', b'1', b'1', 2,
        'update role', 'update role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160010, 'DELETE', 'blue-auth', '/manager/role/{id}', b'1', b'1', b'1', b'0', b'1', 2,
        'delete role', 'delete role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160011, 'POST', 'blue-auth', '/manager/role/auth', b'1', b'1', b'1', b'1', b'1', 2,
        'role auth', 'role auth', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (160012, 'PUT', 'blue-auth', '/manager/relation/role-res', b'1', b'1', b'1', b'1', b'1', 2,
        'update role-resources-relation', 'update role-resources-relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160013, 'PUT', 'blue-auth', '/manager/relation/mem-role', b'1', b'1', b'1', b'1', b'1', 2,
        'update member-role-relation', 'update member-role-relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),


-- member api

       (170001, 'POST', 'blue-member', '/registry', b'0', b'1', b'1', b'1', b'1', 1,
        'member registry', 'member registry', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170002, 'GET', 'blue-member', '/member', b'1', b'1', b'1', b'0', b'1', 1,
        'member info', 'member info', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- member manage

       (180001, 'POST', 'blue-member', '/manager/member/list', b'1', b'1', b'1', b'1', b'1', 2,
        'member list', 'member list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180002, 'POST', 'blue-member', '/manager/authority/list', b'1', b'1', b'1', b'1', b'1', 2,
        'authority list', 'authority list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),


-- finance api
       (190001, 'GET', 'blue-finance', '/finance/balance', b'1', b'1', b'1', b'0', b'1', 1,
        'query balance', 'query balance', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (190002, 'POST', 'blue-finance', '/withdraw', b'1', b'0', b'0', 1, b'1', b'1',
        'withdraw/test encrypt in finance', 'withdraw/test encrypt in finance', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1,
        1),


-- finance open

       (200001, 'GET', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3, 'GET dynamic endpoint', 'GET dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200002, 'HEAD', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3, 'HEAD dynamic endpoint', 'HEAD dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200003, 'POST', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3, 'POST dynamic endpoint', 'POST dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200004, 'PUT', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3, 'PUT dynamic endpoint', 'PUT dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200005, 'PATCH', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1', b'1',
        3, 'PATCH dynamic endpoint', 'PATCH dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200006, 'DELETE', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1',
        b'1', 3, 'DELETE dynamic endpoint', 'DELETE dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200007, 'OPTIONS', 'blue-finance', '/dynamic/{placeholder}', b'0', b'1', b'1', b'1',
        b'1', 3, 'OPTIONS dynamic endpoint', 'OPTIONS dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- finance manage


-- media api

       (210001, 'POST', 'blue-media', '/file/upload', b'1', b'1', b'1', b'1', b'1', 1,
        'media upload of api', 'media upload of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (210002, 'POST', 'blue-media', '/file/download', b'1', b'1', b'1', b'1', b'0', 1,
        'file download of api', 'file download of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (210003, 'POST', 'blue-media', '/attachment/list', b'1', b'1', b'1', b'1', b'1', 1,
        'attachment list of api', 'attachment list of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (210004, 'POST', 'blue-media', '/attachment/withdraw', b'1', b'0', b'0', b'1', b'1', 1,
        'withdraw/test encrypt in media', 'withdraw/test encrypt in media', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (210005, 'GET', 'blue-media', '/mail/send', b'0', b'1', b'1', b'0', b'1', 2,
        'test send', 'test send', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (210006, 'GET', 'blue-media', '/mail/read', b'0', b'1', b'1', b'0', b'1', 2,
        'test read', 'test read', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),


-- media manage


-- portal api

       (230001, 'GET', 'blue-portal', '/bulletin/{type}', b'0', b'1', b'1', b'0', b'1', 1,
        'bulletin list of api', 'bulletin list of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (230002, 'GET', 'blue-portal', '/formatter/{formatter}.html', b'1', b'1', b'1', b'0', b'1', 1,
        'formatter test', 'formatter test', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (230003, 'GET', 'blue-portal', '/fallBack', b'0', b'1', b'1', b'0', b'1', 1,
        'GET fallback', 'GET fallback', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (230004, 'POST', 'blue-portal', '/fallBack', b'0', b'1', b'1', b'1', b'1', 1,
        'POST fallback', 'POST fallback', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- portal manage


-- marketing api

       (250001, 'POST', 'blue-marketing', '/signIn', b'1', b'1', b'1', b'0', b'1', 1,
        'sign in', 'sign in', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (250002, 'GET', 'blue-marketing', '/signIn', b'1', b'1', b'1', b'0', b'1', 1,
        'query sign in record by month', 'query sign in record by month', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- marketing manage


-- shine api

       (270001, 'GET', 'blue-shine', '/shine', b'0', b'1', b'1', b'0', b'1', 1,
        'commonweal information', 'commonweal information', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- shine manage


-- data api

-- data manage

       (320001, 'POST', 'blue-lake', '/event/list', b'0', b'1', b'1', b'1', b'1', 2,
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
VALUES (1, 'summer admin', 'summer admin', 0, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (2, 'normal', 'normal', 999999999, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);






-- role resource relations

-- admin auth

INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `update_time`, `creator`,
                                       `updater`)
VALUES (1, 2, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (2, 2, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (3, 2, 3, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (4, 2, 4, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (5, 2, 5, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (6, 2, 6, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (7, 2, 7, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (8, 2, 8, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (9, 2, 9, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (10, 2, 10, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (11, 2, 11, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (12, 2, 12, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (13, 2, 13, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (14, 2, 14, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (15, 2, 15, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (16, 2, 16, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (17, 2, 17, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (18, 2, 18, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (19, 2, 19, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (20, 2, 20, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (21, 2, 21, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (22, 2, 37, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (23, 2, 38, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (24, 2, 39, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (25, 2, 40, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (26, 2, 41, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (27, 2, 42, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (28, 2, 43, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (29, 2, 44, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (30, 2, 45, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (31, 2, 46, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (32, 2, 47, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (33, 2, 48, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (34, 2, 49, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (35, 2, 50, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (56, 2, 59, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (57, 2, 60, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- manager auth

       (36, 1, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (37, 1, 3, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (38, 1, 4, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (39, 1, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (40, 1, 5, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (41, 1, 6, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (42, 1, 7, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (43, 1, 8, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (44, 1, 9, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (45, 1, 10, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (46, 1, 11, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (47, 1, 12, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (48, 1, 13, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (49, 1, 14, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (50, 1, 15, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (51, 1, 16, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (52, 1, 17, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (53, 1, 19, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (54, 1, 20, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (55, 1, 21, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);




