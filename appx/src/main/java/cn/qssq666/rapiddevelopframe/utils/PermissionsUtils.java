package cn.qssq666.rapiddevelopframe.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import cn.qssq666.rapiddevelopframe.interfaces.INotify;


/**
 * Created by luozheng on 2017/1/8.  qssq.space
 */

public class PermissionsUtils {
    private static final String TAG = "PermissionsUtils";

    public static boolean checkSelfPermissions(Activity activity, String[] premissionStr, ArrayList notPremissionList) {
        boolean result = true;
        for (String premission : premissionStr) {
            boolean currentResult = ContextCompat.checkSelfPermission(activity, premission) == PackageManager.PERMISSION_GRANTED;
            if (currentResult == false) {
                result = false;
                if (notPremissionList != null) {
                    notPremissionList.add(premission);
                }
                Prt.w(TAG, "权限" + premission + "尚未获取");
                //可以进行break
            }
        }
        return result;

    }

    /**
     * @param activity
     * @param premissionStr Manifest.permission.WRITE_EXTERNAL_STORAGE
     * @return
     */
    public static boolean requestPermission(Activity activity, String premissionStr) {
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(activity,
                premissionStr)
                != PackageManager.PERMISSION_GRANTED) {

            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    premissionStr)) {
                ToastUtils.showToast("没有" + getPremissionMsg(premissionStr) + "权限");
                return false;
            } else {
                //进行权限请求 红笔4a不提示
                ActivityCompat.requestPermissions(activity,
                        new String[]{premissionStr},
                        1);
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 如果有多个权限被拒绝了,只提示一个
     *
     * @param activity
     * @param premissionStr
     * @param requestCode
     * @return 前面true, false标识 是否可操作,如果前面的都不可以直接进入详情界面 ，后面的true false表示 表示当前是没有权限的。
     */
    public static Pair<Boolean, Boolean> requestPermissions(Activity activity, String[] premissionStr, int requestCode) {
        //判断当前Activity是否已经获得了该权限
        ArrayList<String> strings = new ArrayList<>();
        if (!checkSelfPermissions(activity, premissionStr, strings)) {


            String permission = strings.get(0);
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission)) {
                ActivityCompat.requestPermissions(activity, premissionStr
                        ,
                        requestCode);
                Prt.w(TAG, "没有权限 申请中");
                return Pair.create(true, false);
            } else {
                //如果App的权限申请曾经被用户拒绝过，只能跳转到设置界面开启权限了。
                Prt.w(TAG, "完全没有权限");

                return Pair.create(false, false);
            }
        } else {
            Prt.w(TAG, "有权限");
            return Pair.create(true, true);
        }


    }

    /**
     * 返回对应的汉字
     *
     * @param key Manifest.permission.WRITE_EXTERNAL_STORAGE
     * @return
     */
    public static String getPremissionMsg(String key) {
        String result = key;
        switch (key) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                result = "存储写入权限";
                break;
            case Manifest.permission.READ_PHONE_STATE:
                result = "访问手机信息";
                break;
            case Manifest.permission.ACCESS_NETWORK_STATE:
                result = "访问网络状态";
                break;

            case Manifest.permission.ACCESS_COARSE_LOCATION:
                result = "获取手机位置信息";
                break;
        }
        return result;
    }

    public static void jumpPermissionManagerPageDialog(final Context context, INotify notify) {
        jumpPermissionManagerPageDialog(context, null, notify);
    }

    public static void jumpPermissionManagerPageDialog(final Context context, String title, INotify notify) {
        DialogUtils.showConfirmDialog(context, "检测到权限已被拒绝,是否进入权限管理界面重新赋予权限" + (title == null ? "" : "(" + title + ")") + "?", new INotify<Void>() {
            @Override
            public void onNotify(Void param) {
//                AppUtils.appDetail(context, BuildConfig.APPLICATION_ID);
                enterPermissionPage(context);

            }
        }, notify);
    }

    /**
     * <strong>注：</strong>可能请求权限的相互作用
     * 用户中断。在这种情况下，您将收到空的权限
     * 结果数组，应视为撤销。
     * <p>
     *
     * @param
     * @param requestCode
     * @param permissions  申请的权限数组展示 ，为空则表示去雄了
     * @param grantResults grantresults授予结果对应的权限
     * @param notify
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, INotify<Boolean> notify) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        // BEGIN_INCLUDE(permission_result)
        // Received permission result for camera permission.
        Prt.i(TAG, "Received response for  permission request." + Arrays.toString(grantResults));

        // Check if the only required permission has been granted
        if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Camera permission has been granted, preview can be displayed
            Prt.e(TAG, " permission has now been granted. Showing preview.");
            if (notify != null) {
                notify.onNotify(true);
            }
        } else {
            if (notify != null) {
                notify.onNotify(false);
            }
            Prt.w(TAG, " permission was NOT granted.");

        }
        // END_INCLUDE(permission_result)
        Prt.d(TAG, "onRequestPermissionsResult" + requestCode);
    }


    public static void enterPermissionPage(Context context) {

        boolean executeSucc = false;
        executeSucc = gotoHuaweiPermission(context);
        if (!executeSucc) {
            executeSucc = gotoMeizuPermission(context);
        }
        if (!executeSucc) {
            executeSucc = gotoMiuiPermission(context);
        }
        if (!executeSucc) {
            startGoogleAppDetailPermissionSettingIntent(context);
        }


    }


    public static void openNotificationSetting(Context context) {
        try {

            Intent intent = new Intent();

            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

            intent.putExtra("app_package", context.getPackageName());

            intent.putExtra("app_uid", context.getApplicationInfo().uid);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);

        } catch (ActivityNotFoundException e) {

            enterPermissionPage(context);

        }
    }


    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    public static void startGoogleAppDetailPermissionSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }


    /**
     * 跳转到魅族的权限管理系统
     */
    private static boolean gotoMeizuPermission(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", context.getPackageName());
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 跳转到miui的权限管理页面
     */
    private static boolean gotoMiuiPermission(Context context) {
        Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        i.setComponent(componentName);
        i.putExtra("extra_pkgname", context.getPackageName());
        try {
            context.startActivity(i);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 华为的权限管理页面
     */
    private static boolean gotoHuaweiPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
