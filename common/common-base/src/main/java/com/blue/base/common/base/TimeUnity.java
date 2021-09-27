package com.blue.base.common.base;

import org.springframework.util.Assert;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.blue.base.constant.base.SummerAttr.DATE_TIME_FORMATTER;
import static java.time.Instant.now;
import static java.time.ZoneId.systemDefault;


/**
 * time util
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "WeakerAccess", "unused"})
public final class TimeUnity {

    private static final DateTimeFormatter FORMATTER = DATE_TIME_FORMATTER;

    public static final ZoneId ZONE_ID = systemDefault();

    /**
     * Get the current timestamp (seconds)
     *
     * @return
     */
    public static long currentEpochSecond() {
        return now().getEpochSecond();
    }

    /**
     * Get the current timestamp (millis)
     *
     * @return
     */
    public static long currentEpochMilli() {
        return now().toEpochMilli();
    }

    /**
     * Get the current time str
     *
     * @return
     */
    public static String currentStampStr() {
        return LocalDateTime.now().format(FORMATTER);
    }

    /**
     * millis -> second
     *
     * @return
     */
    public static long convertEpochMilliToSecond(long epochMilli) {
        Assert.isTrue(epochMilli > 0L, "epochMilli can't be less than 1");
        return epochMilli / 1000L;
    }

    /**
     * Date -> second stamp
     *
     * @return
     */
    public static long convertDateToStamp(Date date) {
        Assert.isTrue(date != null, "date can't be null");
        return date.toInstant().getEpochSecond();
    }

    /**
     * LocalDateTime -> second stamp
     *
     * @return
     */
    public static long convertLocalDateTimeToStamp(LocalDateTime localDateTime) {
        Assert.isTrue(localDateTime != null, "localDateTime can't be null");
        return localDateTime.atZone(ZONE_ID).toInstant().getEpochSecond();
    }

    /**
     * second -> data
     *
     * @return
     */
    public static Date convertEpochSecondToDate(long epochSecond) {
        Assert.isTrue(epochSecond > 0L, "epochSecond can't be null or less than 1");
        return Date.from(Instant.ofEpochSecond(epochSecond));
    }

    /**
     * millis -> data
     *
     * @return
     */
    public static Date convertMilliToDate(long milli) {
        Assert.isTrue(milli > 0L, "milli can't be null or less than 1");
        return Date.from(Instant.ofEpochMilli(milli));
    }

    /**
     * LocalDateTime -> Date
     *
     * @return
     */
    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        Assert.isTrue(localDateTime != null, "localDateTime can't be null");
        return Date.from(localDateTime.atZone(ZONE_ID).toInstant());
    }

    /**
     * second -> LocalDateTime
     *
     * @return
     */
    public static LocalDateTime convertEpochSecondToLocalDateTime(long epochSecond) {
        Assert.isTrue(epochSecond > 0, "epochSecond can't be less than 1");
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZONE_ID);
    }

    /**
     * millis -> LocalDateTime
     *
     * @return
     */
    public static LocalDateTime convertMilliToLocalDateTime(long milli) {
        Assert.isTrue(milli > 0L, "milli can't be less than 1");
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZONE_ID);
    }

    /**
     * Date -> LocalDateTime
     *
     * @return
     */
    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        Assert.isTrue(date != null, "date can't be null");
        return LocalDateTime.ofInstant(date.toInstant(), ZONE_ID);
    }

    /**
     * second stamp -> str
     *
     * @return
     */
    public static String convertEpochSecondToStr(long epochSecond) {
        return convertEpochSecondToLocalDateTime(epochSecond).format(FORMATTER);
    }

    /**
     * millis stamp -> str
     *
     * @return
     */
    public static String convertMilliToStr(long milli) {
        return convertMilliToLocalDateTime(milli).format(FORMATTER);
    }

    /**
     * Date -> str
     *
     * @return
     */
    public static String convertDataToStr(Date date) {
        return convertDateToLocalDateTime(date).format(FORMATTER);
    }

    /**
     * LocalDateTime -> str
     *
     * @return
     */
    public static String convertLocalDateTimeToStr(LocalDateTime localDateTime) {
        Assert.isTrue(localDateTime != null, "localDateTime can't be null");
        return localDateTime.format(FORMATTER);
    }

}
