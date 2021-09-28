package com.blue.base.constant.base;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * project common attr
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public class SummerAttr {

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * default datetime formatter
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern(TIME_FORMAT);

    /**
     * summer identity
     */
    public static final String IDENTITY = "summer";

    /**
     * base time zone
     */
    public static final String TIME_ZONE = "Asia/Shanghai";

}
