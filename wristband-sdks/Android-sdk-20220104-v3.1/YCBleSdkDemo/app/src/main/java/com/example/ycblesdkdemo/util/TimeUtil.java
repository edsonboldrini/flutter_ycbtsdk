package com.example.ycblesdkdemo.util;

import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    //获取当前日期
    public static String currentDay() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    //获取当前日期
    public static String currentDayDetail() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    //获取当前日期
    public static String currentChatDayDetail() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    //返回yyyy-MM-dd hh:mm:ss
    public static String timeConvert(long timeData){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String date = sdf.format(new Date(timeData));
        return date;
    }

    //返回yyyy-MM-dd
    public static String timeConverDate(long timeData){
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String date2 = sdf2.format(new Date(timeData));
        return date2;
    }


    //返回yyyy-MM-dd
    public static String timeConverTimes(long timeData){
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date2 = sdf2.format(new Date(timeData));
        return date2;
    }


    //返回yyyy-MM-dd hh:mm:ss
    public static String hourTime(long timeData){

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    //返回yyyy-MM-dd
    public static String getCurrentDay(){
        Date currentTime = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String date2 = sdf2.format(currentTime);
        return date2;
    }

    public static Date StringToDate(String datetime){
        SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdFormat.parse(datetime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 判断当前系统时间是否在特定时间的段内
     *
     * @param beginHour 开始的小时，例如5
     * @param beginMin 开始小时的分钟数，例如00
     * @param endHour 结束小时，例如 8
     * @param endMin 结束小时的分钟数，例如00
     * @return true表示在范围内，否则false
     */
    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        boolean result = false;// 结果
        final long aDayInMillis = 1000 * 60 * 60 * 24;// 一天的全部毫秒数
        final long currentTimeMillis = System.currentTimeMillis();// 当前时间

        Time now = new Time();// 注意这里导入的时候选择android.text.format.Time类,而不是java.sql.Time类
        now.set(currentTimeMillis);

        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;

        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;

        if (!startTime.before(endTime)) {
            // 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            // 普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }
}

