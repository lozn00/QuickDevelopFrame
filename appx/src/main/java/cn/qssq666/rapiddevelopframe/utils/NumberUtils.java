package cn.qssq666.rapiddevelopframe.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by luozheng on 2016/9/5.  qssq.space
 */
public class NumberUtils {
    private static final String TAG = "NumberUtils";

    private void test() {
/*        DecimalFormat df = new DecimalFormat();
        df.setGroupingSize(2);//进行分组，
        df.setGroupingUsed(false);//分组可用
        df.setCurrency(Currency.getInstance(Locale.CHINA));//加上货币符号，根据不同国家地区
        df.applyPattern("'这是我的钱$',#####.###'美圆'");
        String formatStr = "1234";//千 4
        String formatStr1 = "12345";//万   5
        String formatStr2 = "123456";//十万 6
        String formatStr3 = "1234567";//白万 7
        String formatStr4 = "12345678";//千万  8
        String formatStr5 = "123456789";//亿 9
        String formatStr6 = "1234567891";//十亿  10
        String formatStr7 = "12345678912";//百亿  11
        String formatStr8 = "123456789123";//千亿  12
        String formatStr9 = "1234567891234";//兆  13
        String formatStr10 = "12345678912345";//十兆  14
        String formatStr11 = "123456789123456";//百兆  15
        String formatStr12 = "1234567891234567";//千兆  16

        Log.i(TAG, df.format(1210001113));//格式化
        Log.d(TAG, "test q" + numberShortHand(formatStr));
        Log.d(TAG, "test w" + numberShortHand(formatStr1));
        Log.d(TAG, "test sw" + numberShortHand(formatStr2));
        Log.d(TAG, "test bw" + numberShortHand(formatStr3));
        Log.d(TAG, "test qw" + numberShortHand(formatStr4));
        Log.d(TAG, "test y" + numberShortHand(formatStr5));
        Log.d(TAG, "test sy" + numberShortHand(formatStr6));
        Log.d(TAG, "test by" + numberShortHand(formatStr7));
        Log.d(TAG, "test qy" + numberShortHand(formatStr8));
        Log.d(TAG, "test m" + numberShortHand(formatStr9));
        Log.d(TAG, "test sm" + numberShortHand(formatStr10));
        Log.d(TAG, "test bm" + numberShortHand(formatStr11));
        Log.d(TAG, "test qm" + numberShortHand(formatStr12));*/
    }

    public static String numberShortHandFix(String num) {
        return numberShortHand(num + "");
    }

    public static String numberShortHandFix(long num) {
        if (num < 10000) {
            return num + "";
        }
        String[] unit = new String[]{"万", "亿", "兆", "京", "垓", "秭", "穣", "沟", "涧", "正", "载", "极", "恒河沙", "阿僧祇", "那由他", "不可思议", "无量大数"};
        for (String current : unit) {
            if (num < 10000) {
                return String.format("%2d%s", num, current);
            }
            num /= 10000D;
        }
        return "无穷";
    }

    public static String numberShortHandFix1(String num) {
        BigDecimal a = new BigDecimal(num);
        DecimalFormat df = new DecimalFormat(",###,##0"); //没有小数
        return df.format(a);
    }

    /**
     * 数目简写 超过千位进行简写 保留一位小数 切 不进行四舍五入
     *
     * @param number
     * @return
     */
    public static String numberShortHand(String number) {
        if (TextUtils.isEmpty(number)) {
            return number;
        } else {
            int length = number.length();
            // 11111 小于等于4位 直接显示
            if (length > 4) {
                StringBuffer sb = new StringBuffer();
                switch (length) {
                    case 5://12000 1.2万
                        sb.append(number.charAt(0));
                        sb.append(".");
                        sb.append(number.charAt(1));
                        sb.append("万");
                        break;
                    case 6://121000  十万
                        sb.append(number.substring(0, 2));
                        sb.append(".");
                        sb.append(number.charAt(1));
                        sb.append("万");
                        ;//12.3万
                        break;
                    case 7://百万 1212000  0到 3  第四位为小数点
                        sb.append(number.substring(0, 3));
                        sb.append(".");
                        sb.append(number.charAt(4));
                        sb.append("万");
                        ;//123.4万
                        break;
                    case 8:// 千万
                        sb.append(number.substring(0, 4));
                        sb.append(".");
                        sb.append(number.charAt(5));
                        sb.append("万");
                        ;//123.4万
                        break;
                    case 9://亿
                        sb.append(number.substring(0, 1));
                        sb.append(".");
                        sb.append(number.charAt(1));
                        sb.append("亿");
                        ;//1.2亿
                        break;
                    case 10://十亿
                        sb.append(number.substring(0, 2));
                        sb.append(".");
                        sb.append(number.charAt(2));
                        sb.append("亿");
                        ;//12.3亿
                        break;
                    case 11://百亿
                        sb.append(number.substring(0, 3));//012
                        sb.append(".");
                        sb.append(number.charAt(3));
                        sb.append("亿");
                        ;//123.4亿

                        break;
                    case 12://千亿
                        sb.append(number.substring(0, 4));//0123
                        sb.append(".");
                        sb.append(number.charAt(4));
                        ;//1234.5亿
                        sb.append("亿");
                        break;
                    case 13://兆
                        sb.append(number.substring(0, 1));//01234
                        sb.append(".");
                        sb.append(number.charAt(1));
                        ;//12345.6亿
                        sb.append("兆");
                        break;
                    case 14://十兆
                        sb.append(number.substring(0, 2));//01234
                        sb.append(".");
                        sb.append(number.charAt(2));
                        ;//12345.6亿
                        sb.append("兆");
                        break;
                    case 15://百兆
                        sb.append(number.substring(0, 3));//01234
                        sb.append(".");
                        sb.append(number.charAt(3));
                        sb.append("兆");
                        ;//12345.6亿
                        break;
                    case 16://千兆
                        sb.append(number.substring(0, 4));//01234
                        sb.append(".");
                        sb.append(number.charAt(4));
                        sb.append("兆");
                        ;//12345.6亿
                        break;
                    default:
                        sb.append(length + "位(无极)");
                        break;
                }
                return sb.toString();
            } else {
                return number;
            }
        }

    }
}
