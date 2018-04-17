/*
 *
 *                     .::::.
 *                   .::::::::.
 *                  :::::::::::  by qssq666@foxmail.com
 *              ..:::::::::::'
 *            '::::::::::::'
 *              .::::::::::
 *         '::::::::::::::..
 *              ..::::::::::::.
 *            ``::::::::::::::::
 *             ::::``:::::::::'        .:::.
 *            ::::'   ':::::'       .::::::::.
 *          .::::'      ::::     .:::::::'::::.
 *         .:::'       :::::  .:::::::::' ':::::.
 *        .::'        :::::.:::::::::'      ':::::.
 *       .::'         ::::::::::::::'         ``::::.
 *   ...:::           ::::::::::::'              ``::.
 *  ```` ':.          ':::::::::'                  ::::..
 *                     '.:::::'                    ':'````..
 *
 */

package cn.qssq666.rapiddevelopframe.utils;

import android.util.Log;

/**
 * Created by qssq on 2017/8/8 qssq666@foxmail.com
 */

public class Prt {

    private static final int ERROR_FLAG = -1;
    private static final String ERROR_STR = "Prt";
    public static boolean LOGGABLE = true;

    public static int v(String tag, String msg) {
        if (!LOGGABLE) {
            return ERROR_FLAG;
        }
        return Log.v(tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (!LOGGABLE) {
            return ERROR_FLAG;
        }
        return Log.v(tag, msg, tr);
    }

    public static int d(String tag, String msg) {
        if (!LOGGABLE) {
            return ERROR_FLAG;
        }
        return Log.d(tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (!LOGGABLE) {
            return ERROR_FLAG;
        }
        return Log.d(tag, msg, tr);
    }

    public static int i(String tag, String msg) {
        if (!LOGGABLE) {
            return ERROR_FLAG;
        }
        return Log.i(tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (!LOGGABLE) {
            return ERROR_FLAG;
        }
        return Log.i(tag, msg, tr);
    }

    public static int w(String tag, String msg) {
        if (!LOGGABLE) {
            return ERROR_FLAG;
        }
        return Log.w(tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (!LOGGABLE) {
            return ERROR_FLAG;
        }
        return Log.w(tag, msg, tr);
    }

    public static int w(String tag, Throwable tr) {
        if (!LOGGABLE) {
            return ERROR_FLAG;
        }
        return Log.w(tag, tr);

    }

    public static int e(String tag, String msg) {
        if (!LOGGABLE) {
            return ERROR_FLAG;
        }
        return Log.e(tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (!LOGGABLE) {
            return ERROR_FLAG;
        }
        return Log.e(tag, msg, tr);
    }


    public static String getStackTraceString(Throwable t) {
        if (!LOGGABLE) {
            return ERROR_STR;
        }
        return Log.getStackTraceString(t);
    }
}
