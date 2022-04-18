-- noinspection SpellCheckingInspectionForFile

-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile


USE
marketing;


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




