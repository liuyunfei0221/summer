package com.blue.base.constant.base;

import com.blue.base.model.base.NonValueParam;

import java.time.Clock;
import java.time.format.DateTimeFormatter;

import static java.time.Clock.system;
import static java.time.ZoneId.of;
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

    /**
     * clock
     */
    public static final Clock CLOCK = system(of(TIME_ZONE));

    /**
     * non value param
     */
    public static final NonValueParam NON_VALUE_PARAM = new NonValueParam();

}
