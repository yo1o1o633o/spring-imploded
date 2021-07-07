package com.imploded.push.utils;

import com.to8to.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

/**
 * @author yason.li
 */
@Slf4j
public class TimeUtils {

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static int getCurrentTimestamp() {
        Long timestamp = System.currentTimeMillis() / 1000;
        return timestamp.intValue();
    }

    /**
     * 获取当前时间戳-长整型
     *
     * @return
     */
    public static long getCurrentTimestampLong() {
        Long timestamp = System.currentTimeMillis() / 1000;
        return timestamp.longValue();
    }

    /**
     * 格式化日期
     *
     * @param timestamp
     * @return
     */
    public static String timestampToDate(int timestamp) {
        if (timestamp > 0) {
            int startTime = new Long(DateUtils.getDayStartTime()).intValue();
            if (timestamp > startTime) {
                return DateUtils.format(new Date((timestamp * 1000L)), "HH:mm");
            } else {
                return DateUtils.format(new Date((timestamp * 1000L)), "MM-dd");
            }
        }
        return "";
    }

    public static String timestampToYear(int timestamp) {
        if (timestamp > 0){
            int startTime = getYearStartTime().intValue();
            if (timestamp > startTime) {
                return timestampToDate(timestamp);
            }else {
                return DateUtils.format(new Date(timestamp * 1000L),"yyyy-MM-dd");
            }
        }
        return "";
    }

    public static Long getYearStartTime(){

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.DATE, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime().getTime() / 1000L;
    }

    /**
     * 生成自然周的起始时间
     *
     * @return
     */
    public static int generateNatureWeekStartTime() {
        return getDayTimePoint(-7);
    }

    /**
     * 计算未来或过去某天的时间
     *
     * @param days
     * @return
     */
    public static int getDayTimePoint(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(DAY_OF_YEAR, days);
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * 得到本周周一0点时间戳
     *
     * @return
     */
    public static int getTimesWeekMorning() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return (int) (calendar.getTimeInMillis() / 1000);
    }

    /**
     * 本周日24点时间
     *
     * @return
     */
    public static int getTimesWeekNight() {
        return getTimesWeekMorning() + 7 * 86400;
    }


    /**
     * 得到当天0点时间戳
     *
     * @return
     */
    public static int getTimesDayMorning() {
        return getDayTimePoint(0);
    }

    /**
     * 当天24点时间戳
     *
     * @return
     */
    public static int getTimesDayNight() {
        return getTimesDayMorning() + 86400;
    }

    public static  Integer  getCurrMonthAgoTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //当前时间前去一个月，即一个月前的时间
        calendar.add(Calendar.MONTH, -1);
        log.info("一个月前时间："+calendar.getTime());
        return (int)(calendar.getTimeInMillis()/1000);
    }

    /**
     * 传入Data类型日期，返回字符串类型时间，精确到分钟（ISO8601标准时间）
     * @param date
     * @return
     */
    public static String getISO8601Timestamp(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String nowAsISO = df.format(date);
        return nowAsISO;
    }

    /**
     * 格式化日期
     *
     * @param timestamp
     * @return
     */
    public static String formatDateByTimestamp(Integer timestamp) {
        String date = "";
        if (timestamp > 0) {
            String format = "yyyy-MM-dd HH:mm";
            long millSeconds = timestamp.longValue() * 1000;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            date = simpleDateFormat.format(new Date(millSeconds));
        }
        return date;
    }
}
