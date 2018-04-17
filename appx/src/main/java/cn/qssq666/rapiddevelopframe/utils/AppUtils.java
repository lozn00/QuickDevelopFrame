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
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.loopj.android.http.MySSLSocketFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public static Intent getShareFileIntent(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType("application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }


    private Uri startPhotoZoom(Activity activity, Uri uri, int size, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("margin", 20.5d);
        Uri uritempFile = Uri.parse("file:///" + MediaUtils.getCachePath() + "small.jpg");
        intent.putExtra("output", uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        activity.startActivityForResult(intent, requestCode);
        return uritempFile;
    }

    private void takePhoto(Activity activity, int requestCode) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", Uri.fromFile(MediaUtils.getSdcardpApkFileName("poster_head.jpg")));
        activity.startActivityForResult(intent, requestCode);
    }


    public static void installApkFile(Activity activity, File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        activity.startActivity(intent);


    }

    private void selectPicture(Activity context, int requestCode) {
        context.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Audio.Media.EXTERNAL_CONTENT_URI), requestCode);
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

    public static void hiddenKeyboard(Activity activity) {
        closeKeyboard(activity);
    }

    public static void closeKeyboard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        closeKeyboard(activity,activity.getWindow(),view);
    }

    public static void closeKeyboard(Context activity, Window window, View view){
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /**
     * 判断当前软键盘是否打开
     *
     * @param activity
     * @return
     */
    public static boolean isSoftInputShow(Activity activity) {

        // 虚拟键盘隐藏 判断view是否为空
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            // 隐藏虚拟键盘
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
//       inputmanger.hideSoftInputFromWindow(view.getWindowToken(),0);

            return inputmanger.isActive() && activity.getWindow().getCurrentFocus() != null;
        }
        return false;
    }


    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date) + AppUtils.getRandom(3) + ".jpg";
//        return "temp_head.png";
    }

    public static File getCachePath() {
        File dir = null;
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dir = new File(Environment.getDataDirectory(), "meimi/temp");
        } else {
            dir = new File(Environment.getExternalStorageDirectory(), "meimi/temp");
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getTempCacheFileName() {
        return new File(getCachePath(), getPhotoFileName());
    }

    public static int getRandom(int n) {
        int ans = 0;
        while (Math.log10(ans) + 1 < n)
            ans = (int) (Math.random() * Math.pow(10, n));
        return ans;
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
        AppUtils.jumpActivity(context, intent);
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

    public static void jumpActivity(Context context, Class<? extends Activity> clazz) {
        jumpActivity(context, new Intent(context, clazz));
    }

    public static void jumpActivity(Context context, Intent intent) {

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

    public static void openExtraWebView(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        try {
            context.startActivity(intent);

        } catch (Exception e) {

            ToastUtils.showToast("无法打开" + url);
        }
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

    public static int generateViewId() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return View.generateViewId();
        } else {

            for (; ; ) {
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = 100000 + 1;
                if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
                return 100000;
            }
        }
    }

    public static void cutOutPic(Activity activity, File operaFile, File savePath, int requestCode) {
        cutOutPic(activity, operaFile, savePath, 1000, 1000, requestCode);
    }


    public static Uri parseUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        } else {
            return Uri.parse(file.getAbsolutePath());
        }
    }

    public static void cutOutPic(Activity activity, File operaFile, File saveFile, int width, int height, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri operaUri;

        if (Build.VERSION.SDK_INT >= 24) {
            operaUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", operaFile);
        } else {
            operaUri = Uri.parse(saveFile.getAbsolutePath());
        }
        intent.setDataAndType(operaUri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("circleCrop", false);
        //该参数可以不设定用来规定裁剪区的宽高比
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //该参数设定为你的imageView的大小
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("scale", true);
        //是否返回bitmap对象
        intent.putExtra("return-data", true);
        Uri saveUri;
        if (Build.VERSION.SDK_INT >= 24) {
            saveUri = FileProvider.getUriForFile(activity,
                    activity.getPackageName() + ".fileprovider", saveFile.getParentFile());
        } else {
            saveUri = Uri.parse(saveFile.getAbsolutePath());//可能填写的是目录
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);//无法保存裁剪
  /*      photoReference = new SoftReference<>((Bitmap) (extras.getParcelable("data")));
        ivfront.setImageBitmap(photoReference.get());*/
        //不启用人脸识别
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片的格式
        activity.startActivityForResult(intent, requestCode);
    }
}