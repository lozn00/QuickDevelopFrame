package cn.qssq666.rapiddevelopframe.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by luozheng on 15/11/3.
 */
public class DateUtils {

    /**
     * 毫秒
     */
    public static final int TYPE_MS = 1;
    /**
     * 秒
     */
    public static final int TYPE_SECOND = 2;
    /**
     * 分钟
     */
    public static final int TYPE_MINUTE = 3;
    /**
     * 小时
     */
    public static final int TYPE_HOUR = 4;
    public static final int TYPE_DAY = 5;
    private static final String TAG = "DateUtils";


    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";


    /**
     * 返回时间差  type 可以调用静态方法 endTime, startTime
     *
     * @param type
     * @param bigTime
     * @param time
     * @return
     */
    public static long getTimeDistance(int type, long bigTime, long time) {

//      Prt.w(TAG, "时间:" + getTimeStr(new Date(bigTime)) + "," + getTimeStr(new Date(time)));
        /**
         *   Calendar calendar = Calendar.getInstance();
         calendar.setTime(new Date(personInfo.getTime()));
         //http://tool.chinaz.com/Tools/unixtime.aspx 时间戳换算
         */
//        long result=time2-time1;
//        long days = result / (24 * 60 * 60 * 1000);
////        long hours = result / (1000 * 60 * 60);
//        long hours = (result-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
//        long minutes= (result-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);

//        long between = startTime - endTime/1000;
//        Log.i(TAG, "时间" + startTime + "," + endTime
//        );
//        long day = between / (24L * 3600L);
//        long hour = between%(24*3600)/3600;
//        long min = between%3600/60;
//        long s = between%60/60;
//        long ms = (between - day * 24 * 60 * 60L * 1000L - hour * 60L * 60L * 1000L
//       不除1000 long day = between / (24L * 60L * 60L * 1000L);
//        long hour = (between / (60L * 60L * 1000L) - day * 24);
//        long min = ((between / (60L * 1000L)) - day * 24L * 60L - hour * 60L);
//        long s = (between / 1000L - day * 24L * 60L * 60L - hour * 60L * 60L - min * 60L);
//        long ms = (between - day * 24 * 60 * 60L * 1000L - hour * 60L * 60L * 1000L
//                - min * 60L * 1000L - s * 1000L);

        //==========

        long l = bigTime - time;
        long day = l / 1000 / 60 / 60 / 24;
        long hour = l / 1000 / 60 / 60;
        long min = l / 1000 / 60;
        long s = l / 1000;
//        Log.i(TAG, "day" + day + "hour" + hour + ",min" + min + ",s:" + s + "ms:");
        switch (type) {
            case TYPE_SECOND:
                return s;
            case TYPE_MINUTE:
                return min;
            case TYPE_HOUR:
                return hour;
            case TYPE_DAY:
                return day;
            case TYPE_MS:
                return l;
            default:
                return -110;
        }
    }

    public static String getDetailTime(long date) {
        //  protected String getBuildTimeDescription() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date);
    }

    public static String getDetailTime(Date date) {
        //  protected String getBuildTimeDescription() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date);
    }

    /**
     * 获取当前时间 字符串 在下拉加载 的时候需要调用 2015-11-06 14:24:41
     *
     * @return
     */
    public static String getTimeStr() {
        return getTimeStr(new Date());
    }

    public static String getEightTimeStr() {
        return getTimeEightFormatStr(new Date());
    }

    public static String getTimeStr(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    public static String getHourTime(long date) {
        return new SimpleDateFormat("HH:mm").format(date);
    }

    public static String formatStr(long time) {
        if (time <= 9) {
            return "0" + time;
        }
        return time + "";
    }

    /**
     * 获取时间8位
     *
     * @param date
     * @return
     */
    public static String getTimeEightFormatStr(Date date) {
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    /**
     * 7-7
     *
     * @param time
     * @return
     */
    public static String getUnixMD(long time) {
        return new SimpleDateFormat("MM-dd").format(timeStamp2Date(time));
    }

    /**
     * 2016-7-7
     *
     * @param time
     * @return
     */
    public static String getUnixYMD(long time) {
        return getYMD(timeStamp2Date(time));
    }

    public static String getYMD(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String getMDHMTimeStr(Date date) {
        return new SimpleDateFormat("MM-dd HH:mm").format(date);
    }


    public static String getHourMinuteSecond(long time) {
        return new SimpleDateFormat("HH:mm:ss").format(time);
    }

    public static String get4y2d2h2mTime(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    /**
     * yyyyMMdd "MM-dd HH:mm"
     *
     * @param pattern
     * @param date
     * @return
     */
    public static String getFormatDate(String pattern, Date date) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String getMonthDayUnix(long unixTime) {
        return getMonthDay(unixTime * 1000);
    }


    /**
     * (
     * 年月日
     *
     * @param date
     * @return
     */
    public static String getSimpleTimeStr(Date date) {
        return new SimpleDateFormat("yyyy.MM.dd").format(date);
    }

    public static String getSimpleTimeStr(long date) {
        return new SimpleDateFormat("yyyy.MM.dd").format(date);
    }

    /**
     * 1天内 昨天**,如果1天则显示天之后看了。
     *
     * @param unixtime
     * @return
     */
    public static String getAboutTimeStr(long unixtime) {
        long time = timeStamp2time(unixtime);
        time = new Date().getTime() - time;
        if (time < 1L * ONE_MINUTE) {
            long seconds = toSeconds(time);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (time < 45L * ONE_MINUTE) {
            long minutes = toMinutes(time);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (time < 24L * ONE_HOUR) {
            long hours = toHours(time);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (time < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (time < 30L * ONE_DAY) {
            long days = toDays(time);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (time < 12L * 4L * ONE_WEEK) {
            long months = toMonths(time);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(time);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }

    }

    /**
     * 精确到毫秒  不是时间戳  new Date().getTime()-new Date().getTime()的时间 比如。
     *
     * @param millisecond
     * @return
     */
    public static String generateTime(long millisecond) {
        if (millisecond <= 0) {
            return "00:00";
        }
        int totalSeconds = (int) (millisecond / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,
                    seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
                    .toString();
        }
    }

    public static String generateTimeSymbolSecond(long second) {
        return generateTimeSymbol(second * 1000);
    }

    /**
     * 仿QQ录音的时分秒
     *
     * @param millisecond
     * @return
     */

    public static String generateTimeSymbol(long millisecond) {
        if (millisecond <= 0) {
            return "00:00";
        }
        int totalSeconds = (int) (millisecond / 1000);

        int seconds = totalSeconds % 60;
//        int minutes = (totalSeconds / 60) % 60;
        int minutesCount = (totalSeconds / 60);
//        int hours = totalSeconds / 3600;

        if (minutesCount > 0) {
            return String.format(Locale.US, "%d'%02d\"", minutesCount, seconds).toString();
        } else {
            return String.format(Locale.US, "%d\"", seconds)
                    .toString();
        }
    }


    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

    public static Date timeStamp2Date(long timestamp) {

        return new Date(timeStamp2time(timestamp));
//     String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(timestamp));
//     return date;
    }

    public static long timeStamp2time(long timestamp) {
        return timestamp * 1000;
    }

    /**
     * @param time 现在毫秒值
     * @return
     */
    public static long date2timeStamp(long time) {
        return time / 1000;
    }

    /**
     * 给我时间戳我将返回 标准北京时间
     *
     * @param timestamp
     * @return
     */
    public static String getUnixToTimeStr(long timestamp) {
        return getTimeStr(timeStamp2Date(timestamp));
    }

    /**
     * @return
     */
    public static String getWeekToWeekStr(int value) {
        switch (value) {
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
            default:
                return "错误";

        }
    }

    /*
    获取月和日 如11:18
     */
    public static String getMonthDay(long time) {
        return new SimpleDateFormat("MM.dd").format(new Date(time));
    }


    /**
     * 获取时间等
     *
     * @param time
     * @return
     */
    public static String getMonthDayWeek(long time) {
//        getTimeDistance()

        String weekStr = "";
//        int week = instance.get(Calendar.DAY_OF_WEEK);
//        int day = instance.get(Calendar.DAY_OF_MONTH);
//        int month = instance.get(Calendar.MONTH);
//        instance.setTime(new Date(time));
//        int queryweek= instance.get(Calendar.DAY_OF_WEEK);
//        int queryday=instance.get(Calendar.DAY_OF_MONTH);
//        int querymonth=instance.get(Calendar.MONTH);
//        Log.i(TAG,","+week+","+day+","+month+","+queryweek+","+queryday+","+querymonth);
//        if(week== queryweek&& day==queryday && month==querymonth){
//            weekStr="今天";
//        } else   if(week+1==(queryweek+1) && day+1==(queryday+1) && month+1==querymonth+1){
//            weekStr="明天";
//        }else{
//            weekStr=getWeekToWeekStr(queryweek);
//        }
        long result = getTimeDistance(TYPE_DAY, time, System.currentTimeMillis() - 1000);
        Log.i(TAG, "时间差：" + result);
        if (result == 0) {
            weekStr = "今天";
        } else if (result == 1) {
            weekStr = "明天";
        } else {
            Calendar instance = Calendar.getInstance();
            instance.setTime(new Date(time));
            weekStr = getWeekToWeekStr(instance.get(Calendar.DAY_OF_WEEK));
        }


        return weekStr + "\n" + "" + getMonthDay(time);
    }

    /**
     * unix 时间戳转 时间再转字符串再返回 2015.12.24
     *
     * @param unixTime
     * @return
     */
    public static String getTimeByUnixStr(long unixTime) {
        long l = timeStamp2time(unixTime);
        return getSimpleTimeStr(new Date(l));
    }

    public static void demoTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now;
        try {
            now = df.parse("2016-03-26 10:30:40");
            Date date = df.parse("2016-03-26 10:10:10");
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            System.out.println("" + day + "天" + hour + "小时" + min + "分" + s + "秒");
        } catch (ParseException e) {

            e.printStackTrace();
        }

    }

    public static String getCurrentDateChinese() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(new Date());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (hour > 0 && hour <= 3) {
            return "拂晓";
        }

    /*    00-03(拂晓)
                03-06(黎明)
                06-09(清晨)
                09-12(上午)
                12-15(中午)
                15-18(下午)
                18-21(傍晚)
                21-00(深夜/午夜)*/
        return "";
    }
}
