package com.yanbo.lib_screen.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 描述：
 *
 * @author Yanbo
 * @date 2018/11/6
 */
public class VMDate {

    /**
     * 获取最近时间字符串
     */
    private static final long MINUTE = 60 * 1000;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long TwoDAY = 2 * 24 * HOUR;
    private static final long ThreeDAY = 3 * 24 * HOUR;
    private static final long WEEK = 7 * DAY;
    private static final long MONTH = 31 * DAY;
    private static final long YEAR = 12 * MONTH;

    /**
     * 定义时间的格式化不同样式
     */
    public static SimpleDateFormat sdfNormal = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    public static SimpleDateFormat sdfFilename = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
    public static SimpleDateFormat sdfUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static SimpleDateFormat sdfNoYear = new SimpleDateFormat("MM/dd HH:mm");
    public static SimpleDateFormat sdfOnlyDate = new SimpleDateFormat("yyyy/MM/dd");
    public static SimpleDateFormat sdfOnlyDateNoDay = new SimpleDateFormat("yyyy月MM日");
    public static SimpleDateFormat sdfOnlyDateNoYear = new SimpleDateFormat("MM/dd");
    public static SimpleDateFormat sdfOnlyTime = new SimpleDateFormat("HH:mm");

    /**
     * 获取当前时间的毫秒值
     */
    public static long currentMilli() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前格式化后的标准时间
     *
     * @return 返回格式化后的时间
     */
    public static String currentDateTime() {
        return sdfNormal.format(new Date());
    }

    /**
     * 获取当前时间的 UTC 格式字符串表示
     */
    public static String currentUTCDateTime() {
        sdfUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdfUTC.format(new Date());
    }

    /**
     * 获取当前时间拼接的字符串，没有间隔，主要用于文件命名等
     *
     * @return 返回格式化后的时间
     */
    public static String filenameDateTime() {
        return sdfFilename.format(new Date());
    }

    /**
     * 将给定的字符串型时间格式化为另一种样式
     *
     * @param srcFormat 原来的时间格式
     * @param desFormat 目标的时间格式
     * @param dateStr 原来的时间
     * @return 返回格式化后的时间格式
     */
    public static String converDateTime(String srcFormat, String desFormat, String dateStr) {
        try {
            Date date = new SimpleDateFormat(srcFormat).parse(dateStr);
            return new SimpleDateFormat(desFormat).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据格林尼格式的日志获取其毫秒表示值
     *
     * @param dateStr 需要转换的日期
     */
    public static long milliFormUTC(String dateStr) {
        if (VMStr.isEmpty(dateStr)) {
            return 0l;
        }
        try {
            sdfUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = sdfUTC.parse(dateStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取时间的字符串格式，将秒单位的时间转为如 "00:00:00"这样的格式
     *
     * @param time 时间，单位 秒
     */
    public static String toTimeString(long time) {
        long seconds = time % 60;
        long minutes = (time / 60) % 60;
        long hours = time / 3600;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * 获取时间的秒数，将形如 "00:00:00"这样格式的时间转为秒
     *
     * @param time 字符串格式的时间
     */
    public static long fromTimeString(String time) {
        // Handle "00:00:00.000" pattern, drop the milliseconds
        if (time.lastIndexOf(".") != -1) {
            time = time.substring(0, time.lastIndexOf("."));
        }
        String[] split = time.split(":");
        if (split.length != 3) {
            throw new IllegalArgumentException("Can't parse time string: " + time);
        }
        long h = Long.parseLong(split[0]) * 3600;
        long m = Long.parseLong(split[1]) * 60;
        long s = Long.parseLong(split[2]);
        return h + m + s;
    }

    /**
     * 从 long 整型的时间戳里取出时间
     *
     * @param time 需要格式化的 long 型的时间
     * @return 返回得到的不包含年月日的时间值
     */
    public static String long2Time(long time) {
        Date date = new Date(time);
        return sdfOnlyTime.format(date);
    }

    /**
     * 从 long 整型的时间格式化为正常时间
     *
     * @param time 需要格式化的时间毫秒值
     */
    public static String long2Normal(long time) {
        Date date = new Date(time);
        return sdfNormal.format(date);
    }

    /**
     * 从 long 整型的时间格式化为正常时间，不包含年份
     *
     * @param time 需要格式化的时间毫秒值
     */
    public static String long2NormalNoYear(long time) {
        Date date = new Date(time);
        return sdfNoYear.format(date);
    }

    /**
     * 从 long 整型的时间戳里取出日期 不带有时间
     *
     * @param time 需要格式化的时间毫秒值
     */
    public static String long2Date(long time) {
        Date date = new Date(time);
        return sdfOnlyDate.format(date);
    }

    /**
     * 从 long 整型的时间戳里取出日期，这里取的不带有年份
     *
     * @param time 需要格式化的时间毫秒值
     */
    public static String long2DateNoYear(long time) {
        Date date = new Date(time);
        return sdfOnlyDateNoYear.format(date);
    }

    /**
     * 从 long 整型的时间戳里取出日期，这里取的不带有日
     *
     * @param time 需要格式化的时间毫秒值
     */
    public static String long2DateNoDay(long time) {
        Date date = new Date(time);
        return sdfOnlyDateNoDay.format(date);
    }

    /**
     * 获取相对时间
     *
     * @param time 需要判断的时间点的毫秒值
     * @return 返回相对时间
     */
    public static String getRelativeTime(long time) {
        long currentTime = System.currentTimeMillis();
        long offset = currentTime - time;
        String timeStr = null;
        if (isSameDate(currentTime, time)) {
            // 今天
            timeStr = long2Time(time);
        } else if (isSameDate(currentTime, time + DAY)) {
            // 昨天
            return "昨天 " + long2Time(time);
        } else if (isSameDate(currentTime, time + 2 * DAY)) {
            // 前天
            return "前天 " + long2Time(time);
        } else if (isSameDate(currentTime, time + YEAR)) {
            // XXXX年XX月XX日
            timeStr = long2Normal(time);
        } else {
            // XX月XX日
            timeStr = long2NormalNoYear(time);
        }
        return timeStr;
    }

    /**
     * 判断两个日期是不是同一天
     *
     * @param time1 第一个日期
     * @param time2 第二个日期
     * @return 返回是否是同一天
     */
    public static boolean isSameDate(long time1, long time2) {
        // 求余取整，判断天数是否相等
        long day1 = time1 / DAY;
        long day2 = time2 / DAY;
        return day1 == day2;
    }
}
