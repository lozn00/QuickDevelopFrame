package cn.qssq666.rapiddevelopframe.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.loopj.android.http.MySSLSocketFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.List;
import java.util.Locale;

import cn.qssq666.rapiddevelopframe.activity.PhotoActivity;
import cn.qssq666.rapiddevelopframe.global.SuperAppContext;
import cn.qssq666.rapiddevelopframe.interfaces.CheckPositionI;
import cn.qssq666.rapiddevelopframe.webview.QSSQWebViewActivity;
import cn.qssq666.rapiddevelopframe.webview.WebViewBaseFragment;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.conn.scheme.PlainSocketFactory;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.params.HttpProtocolParams;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by luozheng on 2016/5/6.  qssq.space
 */
public class AppUtils {


    private static final String TAG = "AppUtils";

    public static void fixStatusHeight(Context context, View root) {
        fixStatusHeight(context, root, false);
    }


    public static void installApkFile(Activity activity, File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

/*    public static void notification(Context context){
        nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        notification = new Notification();

        notification.icon = android.R.drawable.stat_sys_download;

// notification.icon=android.R.drawable.stat_sys_download_done;

// 放置在"正在运行"栏目中

        notification.flags = Notification.FLAG_ONGOING_EVENT;

        notification.tickerText = getString(R.string.app_name) + "更新";

        notification.when = System.currentTimeMillis();

        notification.defaults = Notification.DEFAULT_LIGHTS;

// 设置任务栏中下载进程显示的views

        views = new RemoteViews(getPackageName(), R.layout.update_service);

        notification.contentView = views;

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,

                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        notification.contentIntent = contentIntent;

// 将下载任务添加到任务栏中

        nm.notify(notificationId, notification);
    }*/

    /**
     * fragment的toolbar padding need setmargin
     *
     * @param context
     * @param root
     * @param usePadding
     */
    public static void fixStatusHeight(Context context, final View root, boolean usePadding) {
        final int statusHeight = SystemBarTintManager.getStatusBarHeight(context);
        ViewGroup.LayoutParams params = root.getLayoutParams();

        if (params == null || usePadding) {//https://www.cnblogs.com/jukan/p/6054729.html
            root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    root.setPadding(root.getPaddingLeft(), statusHeight, root.getPaddingRight(), root.getPaddingBottom());
                    root.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                }
            });
        } else if (params instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
            root.setLayoutParams(layoutParams);
        } else {


            root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    root.setPadding(root.getPaddingLeft(), statusHeight, root.getPaddingRight(), root.getPaddingBottom());
                    root.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                }
            });
        }

       /* if (params instanceof ViewPager.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) root.getLayoutParams();
            layoutParams.topMargin = statusHeight;
            root.setPadding(0, statusHeight, 0, 0);
        } else if (params instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) root.getLayoutParams();
            layoutParams.topMargin = statusHeight;
            root.setLayoutParams(params);
        } else if (params instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) root.getLayoutParams();
            layoutParams.topMargin = statusHeight;
            root.setLayoutParams(params);
        } else if (params instanceof ScrollView.LayoutParams) {
            ScrollView.LayoutParams layoutParams = (ScrollView.LayoutParams) root.getLayoutParams();
            layoutParams.topMargin = statusHeight;
            root.setLayoutParams(params);
        } else if (params instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) root.getLayoutParams();
            layoutParams.topMargin = statusHeight;
            root.setLayoutParams(params);
        } else {

            params.getClass().getDeclaredFields()
            if ()

        }*/
    }

    public static boolean closeKeyboard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            return inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
        return false;
    }

    public static boolean closeKeyboard(Context activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void closeKeyboardNew(Context activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromInputMethod(view.getApplicationWindowToken(), 0);
    }

    /**
     * 无法弹出原因，没有获取焦点， 获取焦点还是失败了。 那就是根据局的焦点touchle 为true问题
     * view没加载完毕。
     *
     * @param activity
     * @param view
     */
    public static void showKeyboard(Activity activity, View view) {
        if (view != null) {//https://stackoverflow.com/questions/5520085/android-show-softkeyboard-with-showsoftinput-is-not-working
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isShowing = inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            if (!isShowing) {
                Prt.e(TAG, "弹出输入法失败！");
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                //                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
//            inputMethodManager.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }
    }


/*
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
*/


    public static void refreshCheckPosition(RecyclerView.Adapter adapter, CheckPositionI checkPositionI, int newPosition) {
        int checkPosition = checkPositionI.getCheckPosition();
        if (checkPosition != newPosition) {

            checkPositionI.setCheckPosition(newPosition);
            if (checkPosition != -1) {
                adapter.notifyItemChanged(checkPosition);
            }
            if (newPosition != -1) {
                adapter.notifyItemChanged(newPosition);
            } else {
                adapter.notifyDataSetChanged();
            }
        }

    }

    public static String encodeUrlParam(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }

    }


    public static void toPhotoPage(Context context, String images) {
        toPhotoPage(context, new String[]{images}, 0);
    }

    public static void toPhotoPage(Context context, ImageView view, String images) {
        toPhotoPage(context, view, new String[]{images}, 0);
    }

    public static void toPhotoPage(Context context, List<String> imgs, int currentPosition) {
        String[] imgsArr = new String[imgs.size()];
        for (int i = 0; i < imgs.size(); i++) {
            imgsArr[i] = imgs.get(i).toString();
        }
        toPhotoPage(context, imgsArr, currentPosition);
    }

    public static void toPhotoPage(Context context, String[] imgs, int currentPosition) {
        toPhotoPage(context, null, imgs, currentPosition);
    }

    public static void toPhotoPage(Context context, ImageView view, String[] imgs, int currentPosition) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(PhotoActivity.INTENT_IMAGES, imgs);
        intent.putExtra(PhotoActivity.INTENT_POSITION, currentPosition);
        AppUtils.skipActivity(context, intent);
    }

    public static void toWebView(Context context, String url) {
        Intent intent = new Intent(context, QSSQWebViewActivity.class);
        intent.putExtra(WebViewBaseFragment.INTENT_NAME_URL, url);
        context.startActivity(intent);
    }

    public static boolean requestAudioFocus() {
        AudioManager audioManager = (AudioManager) SuperAppContext.getInstance().getSystemService(Context.AUDIO_SERVICE);

        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.requestAudioFocus(null,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);
    }


    public static void abandonAudioFocus() {
        AudioManager audioManager = (AudioManager) SuperAppContext.getInstance().getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
    }

    public static void skipActivity(Context context, Class<? extends Activity> clazz) {
        skipActivity(context, new Intent(context, clazz));
    }

    public static void skipActivity(Context context, Intent intent) {

        context.startActivity(intent);
    }


    public static int lanuchWeinXin(Context context) {

        try {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
//            intent.setAction(Intent.ACTION_VIEW);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Intent.FLAG_ACTIVITY_NEW_TASK ok 否则返回不了
            intent.setComponent(cmp);
            context.startActivity(intent);
            return 0;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return -1;
        } catch (Exception e) {
            return -2;
        }
    }

    public static void marketComment(Context context, String packageName) {

        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public static void copy(Context context, String content) {
//   // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(content);
    }

    public static void installApk(Activity activity, File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }
//    public static void lanuchApp(Activity context){
//        Intent intent = new Intent();
//
//        ComponentName cmp = new ComponentName("com.sina.weibo","com.sina.weibo.EditActivity");
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setComponent(cmp);
//        context.startActivityForResult(intent, 0);
//    }

    /**
     * 分享发送
     */
    public static void shareSend(Context context, String content) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(intent);
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * 随机指定范围内N个不重复的数   +randomCommon(0,2,1));//0-1   0-2则传递   0  3
     * 最简单最基本的方法
     *
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n   随机数个数  但是不会重复?
     */
    public static String randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
//		    int[] result = new int[n];
        StringBuffer sb = new StringBuffer();
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            sb.append("" + num);
            count++;
        }
        return sb.toString();
    }

    /**
     * 精确到毫秒  不是时间戳  new Date().getTime()-new Date().getTime()的时间 比如。
     *
     * @param position
     * @return
     */
    public static String generateTime(long position) {
        if (position <= 0) {
            return "00:00";
        }
        int totalSeconds = (int) (position / 1000);

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

    public static void delayCloseProgressDialog(final ProgressDialog progressDialog) {
        delayCloseProgressDialog(progressDialog, "加载结束");
    }

    public static void delayCloseProgressDialog(final ProgressDialog progressDialog, String title) {
        progressDialog.setMessage("" + title);
        progressDialog.setCancelable(true);
        delayCloseProgressDialog(progressDialog, 1000);
    }

    public static void delayCloseProgressDialog(final ProgressDialog progressDialog, int waitTime) {
        SuperAppContext.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, waitTime);
    }

    public static String getCurProcessName(SuperAppContext instance) {


        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) instance.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static String decodeParam(String json) {
        try {
            return URLDecoder.decode(json, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
        return json;
    }


    public static SchemeRegistry getSchemeRegistry() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            HttpConnectionParams.setSoTimeout(params, 10000);
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            return registry;
        } catch (Exception e) {
            return null;
        }
    }
}