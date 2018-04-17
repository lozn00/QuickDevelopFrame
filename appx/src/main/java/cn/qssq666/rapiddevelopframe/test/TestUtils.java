package cn.qssq666.rapiddevelopframe.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cn.qssq666.rapiddevelopframe.db.ReflectUtils;
import cn.qssq666.rapiddevelopframe.utils.Prt;

/**
 * Created by qssq on 2017/11/3 qssq666@foxmail.com luozheng
 */

public class TestUtils {
    private static final String TAG = "TestUtils";
    public static String[] strs = new String[]{"我很快乐", "这他妈是为什么", "妈的智障", "又犯二了", "口头禅是什么东西?", "非诚勿扰", "你好世界", "一万个理由"};
    public static String[] images = new String[]{"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509690990292&di=6cd521ecac68df8734bbdea15fb98c35&imgtype=0&src=http%3A%2F%2Fwww.zhlzw.com%2FUploadFiles%2FArticle_UploadFiles%2F201204%2F20120412123914329.jpg"
            , "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2618776981,1527535159&fm=27&gp=0.jpg"
            , "https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D220/sign=c043c32e8c44ebf87271633de9f9d736/2e2eb9389b504fc27e414a28eedde71190ef6d85.jpg"
            , "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509691068813&di=3709acd4a6b08c2e9c481023fee85614&imgtype=0&src=http%3A%2F%2Fxnnews.com.cn%2Fwenyu%2Flxsj%2F201709%2FW020170928748935368882.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509690990292&di=ea8d7cb0c080087258a553a999cf612e&imgtype=0&src=http%3A%2F%2Fwww.zhlzw.com%2FUploadFiles%2FArticle_UploadFiles%2F201204%2F20120412123926750.jpg", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509691069176&di=f81ea7c885ea5447cdd481dd811b5c14&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201505%2F27%2F20150527064518_CRne2.jpeg"
    };
    public static String[] nicknames = new String[]{"张三", "李四", "王老五", "小二", "老二", "阿猫", "蠢猪", "萝莉", "白痴", "混蛋", "二逼青年", "逗比"};
    public static int[] ints = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
    public static long[] durations = new long[]{1000 * 60 + (1000 * 50), 1000 * 90 + (5000 * 50), 1000 * 60 + (1000 * 550), 1000 * 60 + (1000 * 55550)};
    public static int[] numbers = new int[]{1000, 2111111, 33434343, 333433444, 33434335, 3343436, 7334334, 3333338};
    public static String[] urls = new String[]{"http://baidu.com", "http://qssq666.cn"};
    private static long[] times = new long[]{
            new Date().getTime(), addTime(Calendar.DAY_OF_WEEK, 1), addTime(Calendar.DAY_OF_WEEK, 55), addTime(Calendar.DAY_OF_WEEK, 10), addTime(Calendar.DAY_OF_WEEK, 133)

    };

    public static Calendar getCalendarInstance() {
        if (instance == null) {
            instance = Calendar.getInstance(Locale.CHINA);
        }
        return instance;
    }

    private static Calendar instance;

    static {

        instance.setTimeInMillis(new Date().getTime());

    }

    private static long setTime(int field, int value) {
        getCalendarInstance().set(field, value);
        return getCalendarInstance().getTimeInMillis();
    }

    private static long addTime(int field, int value) {
        getCalendarInstance().add(field, value);
        return getCalendarInstance().getTimeInMillis();
    }


    public static String getRandomImage() {
        return images[new Random().nextInt(images.length)];
    }

    public static String getRandomName() {
        return nicknames[new Random().nextInt(nicknames.length)];
    }

    public static String getRandomTitle() {
        return strs[new Random().nextInt(strs.length)];
    }

    public static String getRandomUrl() {
        return urls[new Random().nextInt(urls.length)];
    }

    public static int getRandomInts() {
        return ints[new Random().nextInt(ints.length)];
    }

    public static <T> void createModelAndFillField(List list, Class<T> srcClass, int count) {
        for (int i = 0; i < count; i++) {
            list.add(createModelAndFillField(srcClass));
        }
    }

    public static <T> List<T> createModeListAndFillField(Class<T> srcClass) {
        return createModeListAndFillField(srcClass, 5);
    }

    public static <T> List<T> createModeListAndFillField(Class<T> srcClass, int count) {
        ArrayList list = new ArrayList<>();
        createModelAndFillField(list, srcClass, count);
        return list;
    }

    public static <T> T createModelAndFillField(Class<T> srcClass) {
        T srcObject = null;
        try {
            srcObject = srcClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        boolean isChildClass = true;
//        ArrayList<String> hasCallMethods = new ArrayList<>();
        while (srcClass != null && srcClass != Object.class) {
            Field[] fieldsSrc = srcClass.getDeclaredFields();

            Prt.w(TAG, "正在给类自动赋值:" + srcClass.getName());

            for (int i = 0; i < fieldsSrc.length; i++) {
                Field fieldSrc = fieldsSrc[i];
                if (fieldSrc.isSynthetic()) {
                    continue;
                }
                if ("serialVersionUID".equals(fieldSrc.getName())) {
                    continue;
                }
                //public static final long com.buyao.buliao.bean.DetailPersonModel.serialVersionUID
                String getMethodName = ReflectUtils.getGetName(fieldSrc);

                String setMethodName = ReflectUtils.getSetName(fieldSrc);
              /*  if (hasCallMethods.contains(setMethodName)) {//理论上不存在。没有字段就自然不会调用子类方法。

                    Prt.w(TAG, "忽略父类,因为子类已经赋值 " + setMethodName);
                    continue;
                }*/

                try {

                    Method getMethod = srcClass.getMethod(getMethodName);
                    Method setMethod = srcClass.getMethod(setMethodName, getMethod.getReturnType());

                    Object invoke = null;
                    switch (fieldSrc.getName()) {
                        case "face":
                        case "image":
                            invoke = TestUtils.getRandomImage();
                            break;
                        case "id":
                        case "userid":
                            invoke = TestUtils.getRandomInts();
                            break;
                        case "url":
                            invoke = TestUtils.getRandomUrl();
                            break;
                        case "name":
                        case "nickname":
                        case "username":
                        case "uname":
                            invoke = TestUtils.getRandomName();
                        case "title":
                        case "content":
                            invoke = TestUtils.getRandomTitle();
                            break;
                        case "regtime":
                        case "endime":
                            invoke = TestUtils.getRandomTime();
                            break;
                        default:
                            String name = fieldSrc.getName();
                            if (name.contains("count") || name.contains("num")) {
                                invoke = TestUtils.getRandomNumber();
                            } else if (name.contains("duration")) {
                                invoke = getRandomDuration();
                            }
                            break;

                    }

                    Prt.d(TAG, "" + getMethodName + ":" + invoke + ",setMethod:" + setMethod);
                    if (invoke != null) {
                        setMethod.invoke(srcObject, invoke);
                    }


                } catch (NoSuchMethodException e) {
                    e.printStackTrace();

                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }

            srcClass = (Class<T>) srcClass.getSuperclass();

        }

        Prt.d(TAG, "创建测试对象结果:" + srcObject);
        return srcObject;
    }

    public static int getRandomNumber() {
        return numbers[new Random().nextInt(numbers.length)];
    }

    public static long getRandomDuration() {
        return durations[new Random().nextInt(durations.length)];
    }

    public static long getRandomTime() {
        return times[new Random().nextInt(times.length)];
    }

}
