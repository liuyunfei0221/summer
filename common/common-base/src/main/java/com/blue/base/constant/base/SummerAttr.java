package com.blue.base.constant.base;

import com.blue.base.model.base.NonValueParam;

import java.io.File;
import java.time.Clock;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.FileGetter.getFiles;
import static com.blue.base.common.base.PropertiesProcessor.loadProp;
import static java.time.Clock.system;
import static java.time.ZoneId.of;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * project common attr
 *
 * @author DarkBlue
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
    private static final String DATE_TIME_FORMAT;

    /**
     * default datetime formatter
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER;

    /**
     * non value param
     */
    public static final NonValueParam NON_VALUE_PARAM = new NonValueParam();

    /**
     * config prop location
     */
    private static final String
            MESSAGES_URI = "classpath:config/summer.properties";

    /**
     * config keys
     */
    private static final String
            IDENTITY_ATTR_KEY = "identity",
            LANGUAGE_ATTR_KEY = "language",
            TIME_ZONE_ATTR_KEY = "timeZone",
            DATE_TIME_FORMAT_KEY = "dateTimeFormat";

    static {
        List<File> files = getFiles(MESSAGES_URI, false);
        if (files == null || files.size() != 1)
            throw new RuntimeException("summer.properties is not exist or more than 1");

        Properties properties = loadProp(files.get(0));

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

        IDENTITY = identity;
        LANGUAGE = language;
        TIME_ZONE = timeZone;
        CLOCK = system(of(TIME_ZONE));
        DATE_TIME_FORMAT = dateTimeFormat;
        DATE_TIME_FORMATTER = ofPattern(DATE_TIME_FORMAT);
    }

}
