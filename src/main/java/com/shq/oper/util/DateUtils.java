package com.shq.oper.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 日期/时间工具类
 *
 * @author tjun
 */
public final class DateUtils {

    /**
     * 获取默认时间格式: yyyy-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateFormat.LONG_DATE_PATTERN_LINE.formatter;

    /**
     * String to LocalDateTime
     */
    public static LocalDateTime parse(String dateTime) {
        return LocalDateTime.parse(dateTime, DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * String to LocalDateTime
     */
    public static LocalDateTime parse(String dateTime, DateFormat format) {
        return LocalDateTime.parse(dateTime, format.formatter);
    }

    /**
     * LocalDateTime to String
     */
    public static String to(LocalDateTime dateTime) {
        return DEFAULT_DATETIME_FORMATTER.format(dateTime);
    }

    /**
     * LocalDateTime to String
     */
    public static String to(LocalDateTime dateTime, DateFormat format) {
        return format.formatter.format(dateTime);
    }

    /**
     * 获取当前时间
     */
    public static String now() {
        return DEFAULT_DATETIME_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * 获取当前时间
     */
    public static String now(DateFormat format) {
        return format.formatter.format(LocalDateTime.now());
    }

    /**
     * 获取当前时间距 1970-1-1 毫秒数
     */
    public static long currentMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取指定时间距 1970-1-1 毫秒数
     */
    public static long toMillis(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    
    /**
     * 指定多少天后的时间
     */
    public static LocalDateTime addDays(LocalDateTime dateTime, long days) {
        return dateTime.plusDays(days);
    }
    
    /**
     * 指定多少天后的 0时0分0秒
     */
    public static LocalDateTime addDaysFormatStart(LocalDateTime dateTime, long days) {
    	return dateTime.plusDays(days).withHour(0).withMinute(0).withSecond(0);
    }
    /**
     * 指定多少天后的 23时59分059秒
     */
    public static LocalDateTime addDaysFormatEnd(LocalDateTime dateTime, long days) {
    	return dateTime.plusDays(days).withHour(23).withMinute(59).withSecond(59);
    }

    /**
     * 时间戳转LocalDateTime
     * @param timestamp
     * @return
     */
    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }


    
    /**
     * 时间格式
     */
    public enum DateFormat {

        /**
         * 短时间格式
         */
        SHORT_DATE_PATTERN_LINE("yyyy-MM-dd"),
        SHORT_DATE_PATTERN_SLASH("yyyy/MM/dd"),
        SHORT_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd"),
        SHORT_DATE_PATTERN_NONE("yyyyMMdd"),

        /**
         * 长时间格式
         */
        LONG_DATE_PATTERN_LINE("yyyy-MM-dd HH:mm:ss"),
        LONG_DATE_PATTERN_LINE_SLASH("yyyy-MM-dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_SLASH("yyyy/MM/dd HH:mm:ss"),
        LONG_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss"),
        LONG_DATE_PATTERN_NONE("yyyyMMdd HH:mm:ss"),

        /**
         * 长时间格式 带毫秒
         */
        LONG_DATE_PATTERN_WITH_MILSEC_LINE("yyyy-MM-dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_SLASH("yyyy/MM/dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_NONE("yyyyMMddHHmmssSSS");

        private transient DateTimeFormatter formatter;

        DateFormat(String pattern) {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }
    }

}