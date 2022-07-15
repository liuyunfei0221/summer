package com.blue.basic.constant.common;

import com.blue.basic.model.common.EmptyEvent;

import java.time.Clock;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.PropertiesProcessor.loadProps;
import static com.blue.basic.common.base.TimeUnity.convertStrToEpochSecond;
import static java.time.Clock.system;
import static java.time.ZoneId.of;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * project common attr
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class SummerAttr {

    /**
     * summer identity
     */
    public static final String IDENTITY;

    /**
     * default language
     */
    public static final String LANGUAGE;

    /**
     * base time zone
     */
    public static final String TIME_ZONE;

    /**
     * clock
     */
    public static final Clock CLOCK;

    /**
     * default datetime format
     */
    private static final String DATE_TIME_FORMAT, DATE_FORMAT;

    /**
     * default datetime formatter
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER, DATE_FORMATTER;

    /**
     * online time, stamp of seconds
     */
    public static final long ONLINE_TIME;

    /**
     * empty event
     */
    public static final EmptyEvent EMPTY_EVENT = new EmptyEvent();

    /**
     * config prop location
     */
    private static final String
            SUMMER_ATTR_URI = "config/summer.properties";

    /**
     * config keys
     */
    private static final String
            IDENTITY_ATTR_KEY = "identity",
            LANGUAGE_ATTR_KEY = "language",
            TIME_ZONE_ATTR_KEY = "timeZone",
            DATE_TIME_FORMAT_KEY = "dateTimeFormat",
            DATE_FORMAT_KEY = "dateFormat",
            ONLINE_TIME_STR_KEY = "onlineTime";

    static {
        Properties properties = loadProps(SUMMER_ATTR_URI);

        String identity = properties.getProperty(IDENTITY_ATTR_KEY);
        if (isBlank(identity))
            throw new RuntimeException("identity can't be null");

        String language = properties.getProperty(LANGUAGE_ATTR_KEY);
        if (isBlank(language))
            throw new RuntimeException("language can't be null");

        String timeZone = properties.getProperty(TIME_ZONE_ATTR_KEY);
        if (isBlank(identity))
            throw new RuntimeException("timeZone can't be null");

        String dateTimeFormat = properties.getProperty(DATE_TIME_FORMAT_KEY);
        if (isBlank(dateTimeFormat))
            throw new RuntimeException("dateTimeFormat can't be null");

        String dateFormat = properties.getProperty(DATE_FORMAT_KEY);
        if (isBlank(dateFormat))
            throw new RuntimeException("dateFormat can't be null");

        String onlineTimeStr = properties.getProperty(ONLINE_TIME_STR_KEY);
        if (isBlank(dateTimeFormat))
            throw new RuntimeException("onlineTimeStr can't be null");

        IDENTITY = identity;
        LANGUAGE = language;
        TIME_ZONE = timeZone;
        CLOCK = system(of(TIME_ZONE));
        DATE_TIME_FORMAT = dateTimeFormat;
        DATE_FORMAT = dateFormat;
        DATE_TIME_FORMATTER = ofPattern(DATE_TIME_FORMAT);
        DATE_FORMATTER = ofPattern(DATE_FORMAT);
        ONLINE_TIME = convertStrToEpochSecond(onlineTimeStr);
    }

}
