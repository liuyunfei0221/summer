-- noinspection SpellCheckingInspectionForFile

-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

-- auth
-- init

-- resources

USE
auth;

INSERT INTO `auth`.`resource`(`id`, `request_method`, `module`, `uri`, `relation_view`, `authenticate`,
                              `request_un_decryption`,
                              response_un_encryption, `existence_request_body`, `existence_response_body`,
                              `type`, `name`, `description`, `create_time`, `update_time`,
                              `creator`, `updater`)


-- base api

VALUES (100001, 'GET', 'blue-base', '/countries', '', b'0', b'1', b'1', b'0', b'1', 1,
        'countries', 'countries', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100002, 'GET', 'blue-base', '/states/{pid}', '', b'0', b'1', b'1', b'0', b'1', 1,
        'states', 'states', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100003, 'GET', 'blue-base', '/cities/{pid}', '', b'0', b'1', b'1', b'0', b'1', 1,
        'cities', 'cities', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100004, 'GET', 'blue-base', '/areas/{pid}', '', b'0', b'1', b'1', b'0', b'1', 1,
        'areas', 'areas', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100005, 'GET', 'blue-base', '/languages', '', b'0', b'1', b'1', b'0', b'1', 1,
        'support languages', 'support languages', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100006, 'GET', 'blue-base', '/language', '', b'0', b'1', b'1', b'0', b'1', 1,
        'get default language', 'get default language', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100007, 'GET', 'blue-base', '/element', '', b'0', b'1', b'1', b'0', b'1', 1,
        'select all element', 'select all element', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100008, 'POST', 'blue-base', '/element', '', b'0', b'1', b'1', b'0', b'1', 1,
        'select target element', 'select target element', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100009, 'GET', 'blue-base', '/dictType', '', b'0', b'1', b'1', b'0', b'1', 1,
        'query dict types', 'query dict types', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (100010, 'GET', 'blue-base', '/style/{type}', '', b'0', b'1', b'1', b'0', b'1', 1,
        'active style of api', 'active style of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- base manage

       (110001, 'POST', 'blue-base', '/manager/countries', '', b'1', b'1', b'1', b'1', b'1', 3,
        'country page of manager', 'country page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110002, 'POST', 'blue-base', '/manager/country', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert country', 'insert country', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110003, 'PUT', 'blue-base', '/manager/country', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update country', 'update country', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110004, 'DELETE', 'blue-base', '/manager/country/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete country', 'delete country', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (110005, 'POST', 'blue-base', '/manager/states', '', b'1', b'1', b'1', b'1', b'1', 3,
        'state page of manager', 'state page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110006, 'POST', 'blue-base', '/manager/state', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert state', 'insert state', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110007, 'PUT', 'blue-base', '/manager/state', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update state', 'update state', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110008, 'DELETE', 'blue-base', '/manager/state/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete state', 'delete state', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (110009, 'POST', 'blue-base', '/manager/cities', '', b'1', b'1', b'1', b'1', b'1', 3,
        'city page of manager', 'city page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110010, 'POST', 'blue-base', '/manager/city', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert city', 'insert city', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110011, 'PUT', 'blue-base', '/manager/city', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update city', 'update city', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110012, 'DELETE', 'blue-base', '/manager/city/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete city', 'delete city', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (110013, 'POST', 'blue-base', '/manager/areas', '', b'1', b'1', b'1', b'1', b'1', 3,
        'area page of manager', 'area page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110014, 'POST', 'blue-base', '/manager/area', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert area', 'insert area', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110015, 'PUT', 'blue-base', '/manager/area', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update area', 'update area', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110016, 'DELETE', 'blue-base', '/manager/area/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete area', 'delete area', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (110017, 'GET', 'blue-base', '/manager/languages', '', b'1', b'1', b'1', b'0', b'1', 3,
        'support languages manager', 'support languages', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110018, 'GET', 'blue-base', '/manager/language', '', b'1', b'1', b'1', b'0', b'1', 3,
        'get default language manager', 'get default language', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110019, 'GET', 'blue-base', '/manager/message', '', b'1', b'1', b'1', b'0', b'1', 3,
        'i18n message', 'i18n message', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110020, 'GET', 'blue-base', '/manager/element', '', b'1', b'1', b'1', b'0', b'1', 3,
        'i18n element', 'i18n element', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (110021, 'POST', 'blue-base', '/manager/styles', '', b'1', b'1', b'1', b'1', b'1', 3,
        'style page of manager', 'style page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110022, 'POST', 'blue-base', '/manager/style', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert style', 'insert style', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110023, 'PUT', 'blue-base', '/manager/style', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update style', 'update style', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110024, 'DELETE', 'blue-base', '/manager/style/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete style', 'delete style', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (110025, 'PATCH', 'blue-base', '/manager/style/active', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update active style', 'update active style', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- verify api

       (120001, 'POST', 'blue-verify', '/verify/generate', '', b'0', b'1', b'1', b'1', b'0', 1,
        'generate verify with param', 'generate verify with param', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- verify manage

       (130001, 'POST', 'blue-verify', '/manager/verifyTemplates', '', b'1', b'1', b'1', b'1', b'1', 3,
        'verify template page of manager', 'verify template page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (130002, 'POST', 'blue-verify', '/manager/verifyTemplate', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert verify template', 'insert verify template', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (130003, 'PUT', 'blue-verify', '/manager/verifyTemplate', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update verify template', 'update verify template', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (130004, 'DELETE', 'blue-verify', '/manager/verifyTemplate/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete verify template', 'delete verify template', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (130005, 'POST', 'blue-verify', '/manager/verifyHistories', '', b'1', b'1', b'1', b'1', b'1', 3,
        'verify history list of manager', 'verify history list of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- auth api

       (160001, 'POST', 'blue-auth', '/session', '', b'0', b'1', b'1', b'1', b'1', 1,
        'login', 'login', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160002, 'DELETE', 'blue-auth', '/session', '', b'1', b'1', b'1', b'0', b'1', 1,
        'logout', 'logout', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160003, 'DELETE', 'blue-auth', '/sessions', '', b'1', b'1', b'1', b'0', b'1', 1,
        'logout everywhere', 'logout everywhere', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160004, 'PATCH', 'blue-auth', '/session', '', b'0', b'1', b'1', b'1', b'1', 1,
        'refresh session', 'refresh session', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160005, 'PATCH', 'blue-auth', '/secret', '', b'1', b'1', b'1', b'0', b'1', 1,
        'refresh private key', 'refresh private key', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (160006, 'POST', 'blue-auth', '/credential', '', b'1', b'1', b'1', b'0', b'1', 1,
        'credential setting up', 'credential setting up', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160007, 'PUT', 'blue-auth', '/credential', '', b'1', b'1', b'1', b'0', b'1', 1,
        'credential update', 'credential update', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160008, 'PUT', 'blue-auth', '/access', '', b'1', b'1', b'1', b'0', b'1', 1,
        'update access', 'update access', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160009, 'POST', 'blue-auth', '/access', '', b'0', b'1', b'1', b'0', b'1', 1,
        'reset access', 'reset access', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160010, 'POST', 'blue-auth', '/question', '', b'1', b'1', b'1', b'0', b'1', 1,
        'insert security question', 'insert security question', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160011, 'POST', 'blue-auth', '/questions', '', b'1', b'1', b'1', b'0', b'1', 1,
        'insert security questions', 'insert security questions', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160012, 'GET', 'blue-auth', '/authorities', '', b'1', b'1', b'1', b'0', b'1', 1,
        'query authorities', 'query authorities', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (160013, 'GET', 'blue-auth', '/authority', '', b'1', b'1', b'1', b'0', b'1', 1,
        'get authority', 'get authority', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- auth manage

       (170001, 'DELETE', 'blue-auth', '/manager/auth/{mid}', '', b'1', b'1', b'1', b'0', b'1', 3,
        'invalidate member auth', 'invalidate member auth', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (170002, 'POST', 'blue-auth', '/manager/resources', '', b'1', b'1', b'1', b'1', b'1', 3,
        'resource page', 'resource page', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170003, 'POST', 'blue-auth', '/manager/resource', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert resource', 'insert resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170004, 'PUT', 'blue-auth', '/manager/resource', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update resource', 'update resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170005, 'DELETE', 'blue-auth', '/manager/resource/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete resource', 'delete resource', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170006, 'POST', 'blue-auth', '/manager/resource/auth', '', b'1', b'1', b'1', b'1', b'1', 3,
        'resource auth', 'resource auth', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (170007, 'POST', 'blue-auth', '/manager/roles', '', b'1', b'1', b'1', b'1', b'1', 3,
        'role page', 'role page', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170008, 'POST', 'blue-auth', '/manager/role', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert role', 'insert role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170009, 'PUT', 'blue-auth', '/manager/role', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update role', 'update role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170010, 'DELETE', 'blue-auth', '/manager/role/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete role', 'delete role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170011, 'PUT', 'blue-auth', '/manager/role/default', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update default role', 'update default role', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170012, 'POST', 'blue-auth', '/manager/role/auth', '', b'1', b'1', b'1', b'1', b'1', 3,
        'role auth', 'role auth', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (170013, 'PUT', 'blue-auth', '/manager/relation/role-res-update', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update role-resources-relation', 'update role-resources-relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170014, 'PATCH', 'blue-auth', '/manager/relation/mem-role-insert', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert member-role-relation', 'insert member-role-relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170015, 'PUT', 'blue-auth', '/manager/relation/mem-role-update', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update member-role-relation', 'update member-role-relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170016, 'PATCH', 'blue-auth', '/manager/relation/mem-role-delete', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete member-role-relation', 'delete member-role-relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (170017, 'GET', 'blue-auth', '/manager/auth/security/{mid}', '', b'1', b'1', b'1', b'0', b'1', 3,
        'select members security info', 'select members security info', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (170018, 'POST', 'blue-auth', '/manager/operation/payload', '', b'1', b'1', b'1', b'1', b'1', 3,
        'parse payload', 'parse payload', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170019, 'POST', 'blue-auth', '/manager/operation/access', '', b'1', b'1', b'1', b'1', b'1', 3,
        'parse access', 'parse access', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170020, 'POST', 'blue-auth', '/manager/operation/session', '', b'1', b'1', b'1', b'1', b'1', 3,
        'parse session', 'parse session', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (170021, 'POST', 'blue-auth', '/manager/operation/encrypted', '', b'1', b'1', b'1', b'1', b'1', 3,
        'parse encrypted', 'parse encrypted', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- member api

       (180001, 'GET', 'blue-member', '/basic', '', b'1', b'1', b'1', b'0', b'1', 1,
        'member basic info', 'member basic info', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180002, 'PATCH', 'blue-member', '/basic/icon', '', b'1', b'1', b'1', b'0', b'1', 1,
        'update member icon', 'update member icon', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180003, 'PATCH', 'blue-member', '/basic/qrCode', '', b'1', b'1', b'1', b'0', b'1', 1,
        'update member qrCode', 'update member qrCode', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180004, 'PATCH', 'blue-member', '/basic/profile', '', b'1', b'1', b'1', b'0', b'1', 1,
        'update member profile', 'update member profile', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180005, 'PUT', 'blue-member', '/detail', '', b'1', b'1', b'1', b'0', b'1', 1,
        'update member detail', 'update member detail', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180006, 'GET', 'blue-member', '/detail', '', b'1', b'1', b'1', b'0', b'1', 1,
        'member detail info', 'member detail info', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180007, 'PUT', 'blue-member', '/realname', '', b'1', b'1', b'1', b'0', b'1', 1,
        'update member realname', 'update member realname', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180008, 'GET', 'blue-member', '/realname', '', b'1', b'1', b'1', b'0', b'1', 1,
        'member realname info', 'member realname info', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180009, 'POST', 'blue-member', '/address', '', b'1', b'1', b'1', b'1', b'1', 1,
        'add address', 'add address', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180010, 'PUT', 'blue-member', '/address', '', b'1', b'1', b'1', b'0', b'1', 1,
        'update address', 'update address', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180011, 'DELETE', 'blue-member', '/address/{id}', '', b'1', b'1', b'1', b'0', b'1', 1,
        'delete address', 'delete address', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180012, 'GET', 'blue-member', '/address', '', b'1', b'1', b'1', b'0', b'1', 1,
        'select address for api', 'select address for api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180013, 'POST', 'blue-member', '/card', '', b'1', b'1', b'1', b'1', b'1', 1,
        'add card', 'add card', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180014, 'PUT', 'blue-member', '/card', '', b'1', b'1', b'1', b'0', b'1', 1,
        'update card', 'update card', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180015, 'DELETE', 'blue-member', '/card/{id}', '', b'1', b'1', b'1', b'0', b'1', 1,
        'delete card', 'delete card', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (180016, 'GET', 'blue-member', '/card', '', b'1', b'1', b'1', b'0', b'1', 1,
        'select card for api', 'select card for api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- member manage

       (190001, 'POST', 'blue-member', '/manager/basics', '', b'1', b'1', b'1', b'1', b'1', 3,
        'member basic page of manager', 'member basic page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (190002, 'POST', 'blue-member', '/manager/details', '', b'1', b'1', b'1', b'1', b'1', 3,
        'member detail list', 'member detail list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (190003, 'POST', 'blue-member', '/manager/realnames', '', b'1', b'1', b'1', b'1', b'1', 3,
        'member realname page of manager', 'member realname page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (190004, 'POST', 'blue-member', '/manager/authorities', '', b'1', b'1', b'1', b'1', b'1', 3,
        'authority list', 'authority list', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (190005, 'POST', 'blue-member', '/manager/addresses', '', b'1', b'1', b'1', b'1', b'1', 3,
        'select address page for manager', 'select address page for manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (190006, 'POST', 'blue-member', '/manager/cards', '', b'1', b'1', b'1', b'1', b'1', 3,
        'select card page for manager', 'select card page for manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- finance api

       (200001, 'GET', 'blue-finance', '/account', '', b'1', b'1', b'1', b'0', b'1', 1,
        'get finance account', 'get finance account', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200002, 'POST', 'blue-finance', '/withdraw', '', b'1', b'0', b'0', b'1', b'1', 1,
        'withdraw/test encrypt in finance', 'withdraw/test encrypt in finance', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1,
        1),

-- finance open

       (200003, 'GET', 'blue-finance', '/dynamic/{placeholder}', '', b'0', b'1', b'1', b'1', b'1',
        4, 'GET dynamic endpoint', 'GET dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200004, 'HEAD', 'blue-finance', '/dynamic/{placeholder}', '', b'0', b'1', b'1', b'1', b'1',
        4, 'HEAD dynamic endpoint', 'HEAD dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200005, 'POST', 'blue-finance', '/dynamic/{placeholder}', '', b'0', b'1', b'1', b'1', b'1',
        4, 'POST dynamic endpoint', 'POST dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200006, 'PUT', 'blue-finance', '/dynamic/{placeholder}', '', b'0', b'1', b'1', b'1', b'1',
        4, 'PUT dynamic endpoint', 'PUT dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200007, 'PATCH', 'blue-finance', '/dynamic/{placeholder}', '', b'0', b'1', b'1', b'1', b'1',
        4, 'PATCH dynamic endpoint', 'PATCH dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200008, 'DELETE', 'blue-finance', '/dynamic/{placeholder}', '', b'0', b'1', b'1', b'1', b'1', 4,
        'DELETE dynamic endpoint', 'DELETE dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (200009, 'OPTIONS', 'blue-finance', '/dynamic/{placeholder}', '', b'0', b'1', b'1', b'1', b'1', 4,
        'OPTIONS dynamic endpoint', 'OPTIONS dynamic endpoint', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- finance manage


-- media api

       (220001, 'POST', 'blue-media', '/file/upload', '', b'1', b'1', b'1', b'1', b'1', 1,
        'media upload of api', 'media upload of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220002, 'POST', 'blue-media', '/file/download', '', b'1', b'1', b'1', b'1', b'0', 1,
        'file download of api', 'file download of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220003, 'POST', 'blue-media', '/attachments/scroll', '', b'1', b'1', b'1', b'1', b'1', 1,
        'attachment scroll list of api', 'attachment scroll list of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220004, 'POST', 'blue-media', '/downloadHistories/scroll', '', b'1', b'1', b'1', b'1', b'1', 1,
        'download history scroll list of api', 'download history scroll list of api', UNIX_TIMESTAMP(),
        UNIX_TIMESTAMP(), 1, 1),
       (220005, 'POST', 'blue-media', '/qrCode', '', b'1', b'1', b'1', b'1', b'1', 1,
        'generate qr code for api', 'generate qr code for api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (220006, 'POST', 'blue-media', '/withdraw', '', b'1', b'0', b'0', b'1', b'1', 1,
        'withdraw test encrypt in media', 'withdraw test encrypt in media', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220007, 'POST', 'blue-media', '/trd', '', b'1', b'1', b'1', b'1', b'1', 1,
        'test temp redirect in media', 'test temp redirect in medi', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220008, 'POST', 'blue-media', '/rd', '', b'1', b'1', b'1', b'1', b'1', 1,
        'test redirect in media', 'test redirect in media', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220009, 'POST', 'blue-media', '/fd', '', b'1', b'1', b'1', b'1', b'1', 1,
        'test forward in media', 'test forward in media', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

       (220010, 'GET', 'blue-media', '/mail/send', '', b'0', b'1', b'1', b'0', b'1', 2,
        'test send', 'test send', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (220011, 'GET', 'blue-media', '/mail/read', '', b'0', b'1', b'1', b'0', b'1', 2,
        'test read', 'test read', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- media manage

       (230006, 'POST', 'blue-media', '/manager/attachments', '', b'1', b'1', b'1', b'0', b'1', 3,
        'attachment page of manager', 'attachment page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (230007, 'POST', 'blue-media', '/manager/downloadHistories', '', b'1', b'1', b'1', b'0', b'1', 3,
        'download history list of manager', 'download history list of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1,
        1),
       (230008, 'POST', 'blue-media', '/manager/messageTemplates', '', b'1', b'1', b'1', b'1', b'1', 3,
        'messageTemplate page of manager', 'messageTemplate page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (230009, 'POST', 'blue-media', '/manager/messageTemplate', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert messageTemplate', 'insert messageTemplate', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (230010, 'PUT', 'blue-media', '/manager/messageTemplate', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update messageTemplate', 'update messageTemplate', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (230011, 'DELETE', 'blue-media', '/manager/messageTemplate/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete messageTemplate', 'delete messageTemplate', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (230012, 'POST', 'blue-media', '/manager/qrCodeConfigs', '', b'1', b'1', b'1', b'1', b'1', 3,
        'qr code config page of manager', 'qr code config page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (230013, 'POST', 'blue-media', '/manager/qrCodeConfig', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert qr code config', 'insert qr code config', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (230014, 'PUT', 'blue-media', '/manager/qrCodeConfig', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update qr code config', 'update qr code config', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (230015, 'DELETE', 'blue-media', '/manager/qrCodeConfig/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete qr code config', 'delete qr code config', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),


-- portal api

       (250001, 'GET', 'blue-portal', '/bulletins/{type}', '', b'0', b'1', b'1', b'0', b'1', 1,
        'bulletin list of api', 'bulletin list of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (250002, 'GET', 'blue-portal', '/notice/{type}', '', b'0', b'1', b'1', b'0', b'1', 1,
        'notice of api', 'notice of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (250003, 'GET', 'blue-portal', '/formatter/{formatter}.html', '', b'1', b'1', b'1', b'0', b'1', 1,
        'formatter test', 'formatter test', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (250004, 'GET', 'blue-portal', '/fallBack', '', b'0', b'1', b'1', b'0', b'1', 1,
        'GET fallback', 'GET fallback', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (250005, 'POST', 'blue-portal', '/fallBack', '', b'0', b'1', b'1', b'1', b'1', 1,
        'POST fallback', 'POST fallback', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- portal manage

       (260001, 'POST', 'blue-portal', '/manager/bulletins', '', b'1', b'1', b'1', b'0', b'1', 3,
        'bulletin page of manager', 'bulletin page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (260002, 'POST', 'blue-portal', '/manager/bulletin', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert portal', 'insert portal', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (260003, 'PUT', 'blue-portal', '/manager/bulletin', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update portal', 'update portal', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (260004, 'DELETE', 'blue-portal', '/manager/bulletin/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete portal', 'delete portal', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (260005, 'POST', 'blue-portal', '/manager/notices', '', b'1', b'1', b'1', b'0', b'1', 3,
        'notice page of manager', 'notice page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (260006, 'POST', 'blue-portal', '/manager/notice', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert notice', 'insert notice', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (260007, 'PUT', 'blue-portal', '/manager/notice', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update notice', 'update notice', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (260008, 'DELETE', 'blue-portal', '/manager/notice/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete notice', 'delete notice', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- marketing api

       (270001, 'POST', 'blue-marketing', '/signIn', '', b'1', b'1', b'1', b'0', b'1', 1,
        'sign in', 'sign in', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (270002, 'GET', 'blue-marketing', '/signIn', '', b'1', b'1', b'1', b'0', b'1', 1,
        'query sign in record by month', 'query sign in record by month', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (270003, 'POST', 'blue-marketing', '/eventRecords', '', b'1', b'1', b'1', b'0', b'1', 1,
        'event record page of api', 'event record page of api', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- marketing manage

       (280001, 'POST', 'blue-marketing', '/manager/rewards', '', b'1', b'1', b'1', b'1', b'1', 3,
        'reward page of manager', 'reward page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (280002, 'POST', 'blue-marketing', '/manager/reward', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert reward', 'insert reward', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (280003, 'PUT', 'blue-marketing', '/manager/reward', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update reward', 'update reward', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (280004, 'DELETE', 'blue-marketing', '/manager/reward/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete reward', 'delete reward', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (280005, 'POST', 'blue-marketing', '/manager/eventRecords', '', b'1', b'1', b'1', b'0', b'1', 3,
        'event record list of manager', 'event record list of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1,
        1),

       (280006, 'POST', 'blue-marketing', '/manager/relations', '', b'1', b'1', b'1', b'1', b'1', 3,
        'reward date rel page of manager', 'reward date rel page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (280007, 'POST', 'blue-marketing', '/manager/date/relations', '', b'1', b'1', b'1', b'1', b'1', 3,
        'reward date rel list of manager by date', 'reward date rel list of manager by date', UNIX_TIMESTAMP(),
        UNIX_TIMESTAMP(), 1, 1),
       (280008, 'POST', 'blue-marketing', '/manager/relation', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert relation', 'insert relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (280009, 'POST', 'blue-marketing', '/manager/date/relation', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert relations by date', 'insert relations by date', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (280010, 'PUT', 'blue-marketing', '/manager/relation', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update relation', 'update relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (280011, 'DELETE', 'blue-marketing', '/manager/relation/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete relation', 'delete relation', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),


-- shine api

       (290001, 'POST', 'blue-shine', '/shines/scroll', '', b'0', b'1', b'1', b'0', b'1', 1,
        'commonweal information', 'commonweal information', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- shine manage

       (300001, 'POST', 'blue-shine', '/manager/shines/scroll/snapshot', '', b'1', b'1', b'1', b'0', b'1', 3,
        'shine scroll on snapshot of manager', 'shine scroll on snapshot of manager', UNIX_TIMESTAMP(),
        UNIX_TIMESTAMP(), 1, 1),
       (300002, 'POST', 'blue-shine', '/manager/shines/page', '', b'1', b'1', b'1', b'0', b'1', 3,
        'shine page of manager', 'shine page of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (300003, 'POST', 'blue-shine', '/manager/shine/scroll', '', b'1', b'1', b'1', b'0', b'1', 3,
        'shine scroll of manager', 'shine scroll of manager', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (300004, 'POST', 'blue-shine', '/manager/shine', '', b'1', b'1', b'1', b'1', b'1', 3,
        'insert shine', 'insert shine', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (300005, 'PUT', 'blue-shine', '/manager/shine', '', b'1', b'1', b'1', b'1', b'1', 3,
        'update shine', 'update shine', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (300006, 'DELETE', 'blue-shine', '/manager/shine/{id}', '', b'1', b'1', b'1', b'1', b'1', 3,
        'delete shine', 'delete shine', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- event api

       (310001, 'POST', 'blue-event', '/event', '', b'0', b'1', b'1', b'1', b'1', 1,
        'event report', 'event report', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),

-- event manage


-- data api

-- data manage

       (320001, 'POST', 'blue-lake', '/events', '', b'1', b'1', b'1', b'1', b'1', 3,
        'test lake eventRecord', 'test lake eventRecord', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (320002, 'POST', 'blue-analyze', '/manager/statistics/active/simple', '', b'1', b'1', b'1', b'1', b'1', 3,
        'statistics active simple', 'statistics active simple', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (320003, 'POST', 'blue-analyze', '/manager/statistics/active/merge', '', b'1', b'1', b'1', b'1', b'1', 3,
        'statistics merge active', 'statistics merge active', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (320004, 'POST', 'blue-analyze', '/manager/statistics/active/summary', '', b'1', b'1', b'1', b'0', b'1', 3,
        'statistics summary', 'statistics summary', UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);


-- role

INSERT INTO `auth`.`role`(`id`, `type`, `name`, `description`, `level`, `is_default`, `create_time`, `update_time`,
                          `creator`, `updater`)
VALUES (1, 2, 'blue', 'blue', 0, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (2, 2, 'admin', 'admin', 1, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (3, 2, 'manager', 'manager', 2, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (4, 2, 'tester', 'tester', 3, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (5, 2, 'customer', 'customer', 4, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1),
       (6, 1, 'member', 'member', 5, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1, 1);


-- blue admin res
INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `creator`)
SELECT id + 1000000,
       1,
       id,
       UNIX_TIMESTAMP(),
       1
FROM `auth`.`resource`;


-- admin res
INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `creator`)
SELECT id + 2000000,
       2,
       id,
       UNIX_TIMESTAMP(),
       1
FROM `auth`.`resource`
WHERE `type` in (1, 3);


-- manager res
INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `creator`)
SELECT id + 3000000,
       3,
       id,
       UNIX_TIMESTAMP(),
       1
FROM `auth`.`resource`
WHERE `type` in (1, 3);


-- tester res
INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `creator`)
SELECT id + 4000000,
       4,
       id,
       UNIX_TIMESTAMP(),
       1
FROM `auth`.`resource`
WHERE `type` in (1, 3);


-- customer res
INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `creator`)
SELECT id + 5000000,
       5,
       id,
       UNIX_TIMESTAMP(),
       1
FROM `auth`.`resource`
WHERE `type` in (1, 3);


-- member res
INSERT INTO `auth`.`role_res_relation`(`id`, `role_id`, `res_id`, `create_time`, `creator`)
SELECT id + 6000000,
       6,
       id,
       UNIX_TIMESTAMP(),
       1
FROM `auth`.`resource`
WHERE `type` in (1, 2);





