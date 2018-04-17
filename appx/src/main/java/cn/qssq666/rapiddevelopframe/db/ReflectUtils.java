package cn.qssq666.rapiddevelopframe.db;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import cn.qssq666.rapiddevelopframe.db.anotation.Column;
import cn.qssq666.rapiddevelopframe.db.anotation.ID;
import cn.qssq666.rapiddevelopframe.db.anotation.Ignore;
import cn.qssq666.rapiddevelopframe.db.anotation.Table;
import cn.qssq666.rapiddevelopframe.utils.Prt;

//import com.example.mydbutils.domain.New;

/**
 * getFields()获得某个类的所有的公共（public）的字段，包括父类。
 * getDeclaredFields()获得某个类的所有申明的字段，即包括public、private和proteced，
 *
 * @author luozheng
 *         <p>
 *         <p>
 *         找id字段
 *         判断是不是id字段
 *         根据字段找列名
 *         根据类找表明字符串
 *         获取 int值 获取字符串值
 *         ，字符串值
 *         需要通过String.valueOf()进行操作
 *         2016-1-9 15:19:34 update http://www.cnblogs.com/avenwu/p/4193000.html
 */
public class ReflectUtils {


    /**
     * 这样做的目的是解决编译报错问题
     *
     * @param appContext
     */
    public static void installLeakCanary(Context appContext) {
        /*
        if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
         */
        try {
            Class<?> aClass = Class.forName("com.squareup.leakcanary.LeakCanary");

            Method isInAnalyzerProcess = aClass.getMethod("isInAnalyzerProcess", Context.class);
            boolean isAna = (boolean) isInAnalyzerProcess.invoke(null, appContext);
            if (isAna) {
                /*
                //这个过程是专门为LeakCanary堆分析。
      //你不应该在这个过程中初始化你的应用程序。
                */
                return;
            }
            Method method = aClass.getDeclaredMethod("install", Application.class);
            method.invoke(null, appContext);
            Prt.w(TAG, "leak初始化完毕");

        } catch (Exception e) {
            Prt.w(TAG, Prt.getStackTraceString(e));

        }
    }



    public static void clearFieldValue(Object o, String field) {
        try {
            setNotAccessibleProperty(o, field, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* try {
            Field declaredField = o.getClass().getDeclaredField(field);
            declaredField.set(o, null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/

    }



    @NonNull
    public static String getGetName(Field field) {
        String name = field.getName();
        String getMethodName;
        if (field.getType() == boolean.class) {

            getMethodName = "is" + name.substring(0, 1).toUpperCase() + "" + name.substring(1, name.length());

        } else {
            getMethodName = "get" + name.substring(0, 1).toUpperCase() + "" + name.substring(1, name.length());

        }
//        }
        return getMethodName;
    }

    @NonNull
    public static String getSetName(Field field) {
        String name = field.getName();
        String setMethodNmae;
        setMethodNmae = "set" + name.substring(0, 1).toUpperCase() + "" + name.substring(1, name.length());
        return setMethodNmae;
    }



    /**
     * @param src  被替换的
     * @param from 新的
     * @param <T>
     * @return
     */
    public static <T> T copyObjectValue(T src, T from) {
        Field[] fieldsFrom = from.getClass().getDeclaredFields();
        Field[] fieldsSrc = from.getClass().getDeclaredFields();
        for (int i = 0; i < fieldsFrom.length; i++) {
            Field fieldFrom = fieldsSrc[i];
            Field fieldSrc = fieldsFrom[i];
            if (fieldFrom.isSynthetic()) {
                continue;
            }
            String getMethodName = getGetName(fieldFrom);
            String setMethodName = getSetName(fieldSrc);
            try {
                Method getMethod = from.getClass().getMethod(getMethodName);

                Object invoke = getMethod.invoke(from);
                Prt.d(TAG, "" + getMethodName + ":" + invoke);
                Method setMethod = src.getClass().getMethod(setMethodName, getMethod.getReturnType());
                Prt.d(TAG, "" + getMethodName + ":" + invoke + ",setMethod:" + setMethod);
                setMethod.invoke(src, invoke);


            } catch (NoSuchMethodException e) {
                e.printStackTrace();

            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        Prt.d(TAG, "复制对象后的结果:src:" + src + ",from:" + from);
        return src;
    }

    private static final String TAG = "ReflectUtils";


    /**
     * 对给定对象obj的propertyName指定的成员变量进行赋值
     * 赋值为value所指定的值
     * <p>
     * 该方法可以访问私有成员
     */
    public static void setNotAccessibleProperty(Object obj, String propertyName, Object value) throws Exception {
        setNotAccessibleProperty(obj.getClass(), obj, propertyName, value);
    }

    /**
     * @param aClass       class必须等于或者 是它的父类
     * @param obj
     * @param propertyName
     * @param value
     * @throws Exception
     */
    public static void setNotAccessibleProperty(Class<?> aClass, Object obj, String propertyName, Object value) throws Exception {
        Field field = aClass.getDeclaredField(propertyName);
        //赋值前将该成员变量的访问权限打开
        field.setAccessible(true);
        field.set(obj, value);
        //赋值后将该成员变量的访问权限关闭
        field.setAccessible(false);
    }

    /**
     * 返回类的所有字段 所有私有都会被修改为共有
     *
     * @param klass
     * @return
     */
    public static Field[] getDeclaredFields(Class<?> klass) {
//		Field[] fields = klass.getDeclaredFieldss();//这只能获取共有的方法 包括父类 岁所以没必要修改访问
        Field[] fields = klass.getDeclaredFields();//获取当前的所有
        for (Field field : fields) {
//            field.setAccessible(true);

        }
        return fields;
    }

    public static Field[] getFields(Class<?> klass) {
//		Field[] fields = klass.getDeclaredFieldss();//这只能获取共有的方法 包括父类
        Field[] fields = klass.getFields();//获取当前的所有
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return fields;
    }

    static public boolean isConstant(Field field) {
        String name = field.getName();
        String upCaseName = name.toUpperCase();
        Log.i(TAG, "upCaseName," + upCaseName + ",NAME;" + name);
        if (upCaseName.equals(name)) {
            Log.w(TAG, "大写的为常量不能插入:" + field.getName() + ",FIELD:" + field + "," + field.getModifiers());
            return true;
        } else if (field.isEnumConstant()) {
            Log.w(TAG, "不能插入枚举常量:" + field.getName());
            return true;
        } else if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取当前所有.
     *
     * @param klass
     * @param fieldStr
     * @return
     */
    public static Field getDeclaredField(Class<?> klass, String fieldStr) {
//		Field[] fields = klass.getDeclaredFieldss();//这只能获取共有的方法
        try {
            Field declaredField = klass.getDeclaredField(fieldStr);
            return declaredField;
        } catch (NoSuchFieldException e) {
            mFailListener.onFail(e);
            e.printStackTrace();
            return null;
        }

    }

    public static Field getField(Class<?> klass, String fieldStr) {
//		Field[] fields = klass.getDeclaredFieldss();//这只能获取共有的方法
        try {
            Field field = klass.getField(fieldStr);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            mFailListener.onFail(e);
            e.printStackTrace();
            return null;
        }

    }
    /*
    public static String getDeclaredFieldsStrByMethodName(Class<?> klass, String field) {
//		Field[] fields = klass.getDeclaredFieldss();//这只能获取共有的方法

        try {
            Method declaredMethod = klass.getDeclaredMethod(field, klass);
            String name = declaredMethod.getNickname();
            if (name.startsWith("set") || name.startsWith("get")) {
                return name.substring("set".length());
            }
            return name;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }


    }*/

    /**
     * 返回 字段为id的， id字段请公开
     *
     * @param klass
     * @return
     */
    public static Field getDeclaredIDField(Class<?> klass) {
        Field[] fields = klass.getDeclaredFields();//getDeclaredFields只能获取子类的所有方法。 但是 getDeclaredFieldss只能获取当前类公开的方法
//        Field[] fields = klass.getDeclaredFields();//getDeclaredFields只能获取子类的所有方法。
        ArrayList<Field> listBak = new ArrayList<>();//备份的id字段如果找不到申明的就用listBak
        for (Field field : fields) {
            if (isIDField(field)) {
                field.setAccessible(true);
                return field;
            } else if ("_id".equals(field.getName()) || "id".equals(field.getName())) {
                if (listBak.size() > 0 && listBak.get(0).equals("id")) {//找到的是_id
                    listBak.add(0, field);//_id优先级比id高
                } else if (listBak.size() == 0) {//可能是id也可能是_id
                    listBak.add(field);
                }
            }

        }
        Field field = (listBak.size() == 0 ? null : listBak.get(0));
        if (field == null) {
            throw new RuntimeException("警告,没有找到id,请申明id或_id或使用注解申明其它字符的作为id字段" + fields.length + ",");
        }
        field.setAccessible(true);
        return field;
    }

    public static Field getIDField(Class<?> klass) {
        Field[] fields = klass.getFields();//getDeclaredFields只能获取子类的所有方法。 但是 getDeclaredFieldss只能获取当前类公开的方法

//        Field[] fields = klass.getDeclaredFields();//getDeclaredFields只能获取子类的所有方法。
        ArrayList<Field> listBak = new ArrayList<>();//备份的id字段如果找不到申明的就用listBak
        for (Field field : fields) {
            if (isIDField(field)) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } else if ("_id".equals(field.getName()) || "id".equals(field.getName())) {
                if (listBak.size() > 0 && listBak.get(0).equals("id")) {//找到的是_id
                    listBak.add(0, field);//_id优先级比id高
                } else if (listBak.size() == 0) {//可能是id也可能是_id
                    listBak.add(field);
                }
            }

        }
        Field field = (listBak.size() == 0 ? null : listBak.get(0));
        if (field == null) {
            throw new RuntimeException("警告,没有找到id,请申明id或_id或使用注解申明其它字符的作为id字段");
        }
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return field;

    }

    /**
     * 判断字段是否是int类型
     *
     * @param field
     * @return
     */
    public static boolean isIntType(Field field) {
        return field.getType().equals(int.class) || field.getType().equals(Integer.class);
    }

    /**
     * 判断字段是否是Long类型
     *
     * @param field
     * @return
     */
    public static boolean isLongType(Field field) {
        return field.getType().equals(long.class) || field.getType().equals(Long.class);
    }

    /**
     * 判断字段是否是Long类型
     *
     * @param field
     * @return
     */
    public static boolean isFloatType(Field field) {
        return field.getType().equals(float.class) || field.getType().equals(Float.class);
    }

    /**
     * 判断字段是否是double类型
     *
     * @param field
     * @return
     */
    public static boolean isDoubleType(Field field) {
        return field.getType().equals(double.class) || field.getType().equals(Double.class);
    }

    /**
     * 判断字段是否是short类型
     *
     * @param field
     * @return
     */
    public static boolean isShortType(Field field) {
        return field.getType().equals(short.class) || field.getType().equals(Short.class);
    }

    /**
     * 判断字段是否是beoolean类型
     *
     * @param field
     * @return
     */
    public static boolean isBooleanType(Field field) {
        return field.getType().equals(boolean.class) || field.getType().equals(Boolean.class);
    }

    /**
     * 判断字段是否是string类型
     *
     * @param field
     * @return
     */
    public static boolean isStringType(Field field) {
        return field.getType().equals(String.class);
    }

    public static boolean isStringArrayType(Field field) {
        return field.getType().equals(String[].class);
    }

    public static boolean isBytesType(Field field) {
        return Byte[].class.equals(field);
    }

    public static boolean isByteType(Field field) {
        return byte.class.equals(field) || Byte.class.equals(field);
    }

    public static boolean isIgnoreFiled(Field field) {
        return field.isAnnotationPresent(Ignore.class);
    }

    /**
     * 判断是否是id字段 通过注解
     *
     * @param field
     * @return
     */
    public static boolean isIDField(Field field) {
        Log.i(TAG, "field:" + field);
        if (field != null) {
            if (field.isAnnotationPresent(ID.class)) {
                return true;
            } else if ("id".equals(field.getName())) {
                return true;
            } else if ("_id".equals(field.getName())) {
                return true;
            }
//            field.setAccessible(true);
        }
        return false;
        //不能通过这个来判断了，只能通过注解判断是否是id
        // if(field.getNickname().equalsIgnoreCase("id") ||
        // field.getNickname().equalsIgnoreCase("_id")){
        // {
        // field.setAccessible(true);
        // return true;
        // }
    }

    public void ModifyField(Field field) {
//将字段的访问权限设为true：即去除private修饰符的影响
        field.setAccessible(true);

/*去除final修饰符的影响，将字段设为可修改的*/
        Field modifiersField = null;
        try {
            modifiersField = Field.class.getField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.set(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    public void ModifyDeclaredField(Field field) {
//将字段的访问权限设为true：即去除private修饰符的影响
        field.setAccessible(true);

/*去除final修饰符的影响，将字段设为可修改的*/
        Field modifiersField = null;
        try {

            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.set(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    public static boolean isFinal(Field field, Object o) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(o, null);
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 传递我什么对象我返回什么实例
     *
     * @param kClass
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <E> E getInstance(Class<E> kClass) {
        try {
            return kClass.newInstance();
        } catch (Exception e) {
            mFailListener.onFail(e);
            e.printStackTrace();
        }
        // return null;
        return null;
    }

    /**
     * 设置值
     *
     * @param object 如果是 对象 请传递实例对象
     * @param field  字段
     * @param value  要设置的值
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static void setValue(Object object, Field field, Object value) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(object, value);
        } catch (Exception e) {
            mFailListener.onFail(e);
            e.printStackTrace();
        }
    }

    /**
     * 获取int字段值
     *
     * @param object 静态可以传递为null
     * @param field
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static int getIntValue(Object object, Field field) {
        try {
//                field.setAccessible(true);
            return field.getInt(object);
        } catch (Exception e) {
            mFailListener.onFail(e);
            e.printStackTrace();
        }
        return -1;
    }

    public static double getDoubleValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.getDouble(object);
        } catch (Exception e) {
            mFailListener.onFail(e);
            e.printStackTrace();
        }
        return -1;
    }

    public static float getFloatValue(Object object, Field field) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return field.getFloat(object);
        } catch (Exception e) {
            mFailListener.onFail(e);
            e.printStackTrace();
        }
        return -1;
    }

    public static short getShortValue(Object object, Field field) {
        try {
            return field.getShort(object);
        } catch (Exception e) {
            mFailListener.onFail(e);
            e.printStackTrace();
        }
        return -1;
    }

    public static byte getByteValue(Object object, Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            return field.getByte(object);
        } catch (Exception e) {
            mFailListener.onFail(e);
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean isArrayType(Object object) {
        return object instanceof Object[] || object.getClass().isArray();

    }
    //http://docs.oracle.com/javase/7/docs/api/java/lang/reflect/Array.html

    /**
     * 正确不正确等待验证
     *
     * @param object
     * @return
     */
    public static byte[] getBytesValue(Object object) {
        try {
            int length = Array.getLength(object);
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < length; i++) {
                byte aByte = Array.getByte(object, i);
                stringBuilder.append(aByte);
            }
            stringBuilder.toString().getBytes();


        } catch (Exception e) {
            mFailListener.onFail(e);
            e.printStackTrace();
        }
        return null;
    }

    public static boolean getBooleanValue(Object object, Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            return field.getBoolean(object);
        } catch (Exception e) {
            mFailListener.onFail(e);
            e.printStackTrace();
        }
        return false;
    }

    public static long getLongValue(Object object, Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            return field.getLong(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            mFailListener.onFail(e);
        }
        return -1;
    }

    private static final String NULL_STR = "";
    private static final String NULL_INTEGER = "0";

    /**
     * 获取字符串字段值
     *
     * @param object
     * @param field
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static String getStringValue(Object object, Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        Object value = getValue(object, field);
        if (value == null) {
            return NULL_STR;
        }
        String s = String.valueOf(value);
        return s;
    }

    /**
     * @param object 如果是静态 可传递为null
     * @param field  字段名
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Object getValue(Object object, Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            return field.get(object);
        } catch (Exception e) {
            mFailListener.onFail(e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据字节码对象返回表名字符串
     *
     * @param klass
     * @return
     */
    public static String getTableNameByClass(Class<?> klass) {

        if (klass.isAnnotationPresent(Table.class)) {
            return klass.getAnnotation(Table.class).value();//后面的value我想就是参数的泛型返回 注解的接口然后就可以链式输出value();
        }
        return klass.getSimpleName();
    }

    public static String getColumnNameByField(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        if (field == null) {
            throw new RuntimeException("字段不能为空");
        }
        try {
            if (field.isAnnotationPresent(Column.class)) {
                return field.getAnnotation(Column.class).value();
            }

        } catch (Exception e) {
            mFailListener.onFail(e);
            e.toString();

            Log.w(TAG, field.getName() + "E." + e.toString());
        }
        return field.getName();
    }


    public interface OnFailListener {
        void onFail(Throwable e);
    }

    static OnFailListener mFailListener = new OnFailListener() {

        @Override
        public void onFail(Throwable e) {
            e.printStackTrace();
            Log.w(TAG, "ERR:" + e.toString());
        }
    };

    /**
     * 设置反射的异常监听事件
     *
     * @param onFailListener
     */
    public void setOnFailListener(OnFailListener onFailListener) {
        ReflectUtils.mFailListener = onFailListener;
    }

    //	public static<T> T  getValue(T T,Field field) throws IllegalAccessException, IllegalArgumentException{
//		return (T) field.get(T);
//	}
    {
//		try {
//		} catch (IllegalAccessException e) {
//			
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			
//			e.printStackTrace();
//		}
    }

    /**
     * 拼接某属性get 方法
     *
     * @param fldname
     * @return
     */
    public static String getGetMethodNameByField(String fldname) {
        if (null == fldname || "".equals(fldname)) {
            mFailListener.onFail(new RuntimeException("无法拼接get方法因为字段为空"));
            return null;
        }
        String pro = "get" + fldname.substring(0, 1).toUpperCase() + fldname.substring(1);
        return pro;
    }

    /**
     * 拼接某属性set 方法
     *
     * @param fldname 根据字段名拼接方法
     * @return
     */
    public static String getSetMethodNameField(String fldname) {
        if (null == fldname || "".equals(fldname)) {
            mFailListener.onFail(new RuntimeException("无法拼接set方法因为字段为空"));
            return null;
        }
        String pro = "set" + fldname.substring(0, 1).toUpperCase() + fldname.substring(1);
        return pro;
    }

    /**
     * 判断该方法是否存在
     *
     * @param methods
     * @param met
     * @return
     */
    public static boolean methodIsExist(Method methods[], String met) {
        if (null != methods) {
            for (Method method : methods) {
                if (met.equals(method.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
