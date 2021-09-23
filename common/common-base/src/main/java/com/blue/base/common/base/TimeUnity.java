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
 * 时间转换
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "WeakerAccess", "unused"})
public final class TimeUnity {

    private static final DateTimeFormatter FORMATTER = DATE_TIME_FORMATTER;

    public static final ZoneId ZONE_ID = systemDefault();

    /**
     * 获取当前时间戳(秒)
     *
     * @return
     */
    public static long currentEpochSecond() {
        return now().getEpochSecond();
    }

    /**
     * 获取当前时间戳(毫秒)
     *
     * @return
     */
    public static long currentEpochMilli() {
        return now().toEpochMilli();
    }

    /**
     * 获取当前时间字符串
     *
     * @return
     */
    public static String currentStampStr() {
        return LocalDateTime.now().format(FORMATTER);
    }

    /**
     * 毫秒级时间戳转秒级时间戳
     *
     * @return
     */
    public static long convertEpochMilliToStamp(long epochMilli) {
        Assert.isTrue(epochMilli > 0L, "epochMilli can't be less than 1");
        return epochMilli / 1000L;
    }

    /**
     * Date转时间戳(秒)
     *
     * @return
     */
    public static long convertDateToStamp(Date date) {
        Assert.isTrue(date != null, "date can't be null");
        return date.toInstant().getEpochSecond();
    }

    /**
     * LocalDateTime转时间戳(秒)
     *
     * @return
     */
    public static long convertLocalDateTimeToStamp(LocalDateTime localDateTime) {
        Assert.isTrue(localDateTime != null, "localDateTime can't be null");
        return localDateTime.atZone(ZONE_ID).toInstant().getEpochSecond();
    }

    /**
     * 秒级时间戳转Date
     *
     * @return
     */
    public static Date convertEpochSecondToDate(long epochSecond) {
        Assert.isTrue(epochSecond > 0L, "epochSecond can't be null or less than 1");
        return Date.from(Instant.ofEpochSecond(epochSecond));
    }

    /**
     * 毫秒级时间戳转Date
     *
     * @return
     */
    public static Date convertMilliToDate(long milli) {
        Assert.isTrue(milli > 0L, "milli can't be null or less than 1");
        return Date.from(Instant.ofEpochMilli(milli));
    }

    /**
     * LocalDateTime转Date
     *
     * @return
     */
    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        Assert.isTrue(localDateTime != null, "localDateTime can't be null");
        return Date.from(localDateTime.atZone(ZONE_ID).toInstant());
    }

    /**
     * 秒级时间戳转LocalDateTime
     *
     * @return
     */
    public static LocalDateTime convertEpochSecondToLocalDateTime(long epochSecond) {
        Assert.isTrue(epochSecond > 0, "epochSecond can't be less than 1");
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZONE_ID);
    }

    /**
     * 毫秒级时间戳转LocalDateTime
     *
     * @return
     */
    public static LocalDateTime convertMilliToLocalDateTime(long milli) {
        Assert.isTrue(milli > 0L, "milli can't be less than 1");
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZONE_ID);
    }

    /**
     * Date转LocalDateTime
     *
     * @return
     */
    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        Assert.isTrue(date != null, "date can't be null");
        return LocalDateTime.ofInstant(date.toInstant(), ZONE_ID);
    }

    /**
     * 秒级时间戳转字符
     *
     * @return
     */
    public static String convertEpochSecondToStr(long epochSecond) {
        return convertEpochSecondToLocalDateTime(epochSecond).format(FORMATTER);
    }

    /**
     * 毫秒级时间戳转字符
     *
     * @return
     */
    public static String convertMilliToStr(long milli) {
        return convertMilliToLocalDateTime(milli).format(FORMATTER);
    }

    /**
     * Date转字符
     *
     * @return
     */
    public static String convertDataToStr(Date date) {
        return convertDateToLocalDateTime(date).format(FORMATTER);
    }

    /**
     * LocalDateTime转字符
     *
     * @return
     */
    public static String convertLocalDateTimeToStr(LocalDateTime localDateTime) {
        Assert.isTrue(localDateTime != null, "localDateTime can't be null");
        return localDateTime.format(FORMATTER);
    }

}
