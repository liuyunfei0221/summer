-- noinspection SpellCheckingInspectionForFile

-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile


USE
portal;

INSERT INTO `portal`.`bulletin`(`id`, `title`, `content`, `link`, `type`, `status`, `priority`, `create_time`,
                                `update_time`, `creator`, `updater`)
VALUES (1, 'popular bulletin 2', 'test data', 'www.baidu.com', 1, 1, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (2, 'popular bulletin 1', 'test data', 'cn.bing.com', 1, 1, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1,
        1),
       (3, 'newest bulletin 2', 'test data', 'www.baidu.com', 2, 1, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1,
        1),
       (4, 'newest bulletin 1', 'test data', 'cn.bing.com', 2, 1, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(), 1,
        1),
       (5, 'recommend bulletin 2', 'test data', 'www.baidu.com', 3, 1, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1),
       (6, 'recommend bulletin 1', 'test data', 'cn.bing.com', 3, 1, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),
        1, 1);





