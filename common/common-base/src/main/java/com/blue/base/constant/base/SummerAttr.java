package com.blue.base.constant.base;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * 放置项目的一些通用属性
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public class SummerAttr {

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认时间模板
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern(TIME_FORMAT);

    /**
     * 项目标识
     */
    public static final String IDENTITY = "summer";

    /**
     * 统一基于上海时区
     */
    public static final String TIME_ZONE = "Asia/Shanghai";

}
