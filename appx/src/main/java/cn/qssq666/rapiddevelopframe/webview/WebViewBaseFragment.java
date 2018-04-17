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

package cn.qssq666.rapiddevelopframe.webview;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.qssq666.rapiddevelopframe.BuildConfig;
import cn.qssq666.rapiddevelopframe.R;
import cn.qssq666.rapiddevelopframe.base.fragment.BaseFragment;
import cn.qssq666.rapiddevelopframe.global.SuperAppContext;
import cn.qssq666.rapiddevelopframe.utils.Prt;
import cn.qssq666.rapiddevelopframe.utils.ToastUtils;


/**
 * Created by luozheng on 15/12/21.
 * <p>
 * //wView.loadUrl("file:///android_asset/index.html");
 * <p>
 * -----打开本包内asset目录下的index.html文件
 * //wView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
 * <p>
 * -----打开本地sd卡内的index.html文件
 * 2017年6月16日 17:47:45
 * 增加自动播放视频 增加alert功能
 */
public class WebViewBaseFragment extends BaseFragment implements WebViewBackControlI {


    @Override
    public boolean requestBackpress() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;

    }


    public static final String INTENT_NAME_URL = "intent_name_url";
    public static final String INTENT_NAME_SHOW_PROGRESS_DIALOG = "intent_name_show_dialog";
    public static final int FLAG_FAIL = 1;
    public static final String INTENT_AUTO_PLAY_VIDEO = "INTENT_AUTO_PLAY_VIDEO";
    private boolean mAutoPlayVideo;
    private boolean mShowDialog;

    public String getFirstUrl() {
        return mFirstUrl;
    }

    private String mFirstUrl;

    public String getLastUrl() {
        return mLastUrl;
    }

    private String mLastUrl;

    public WebView getWebView() {
        return mWebView;
    }

    private WebView mWebView;
    private ProgressDialog mProgressDialog;
    private String mTitle;

    public String getTitle() {
        return mTitle;
    }

    public void setDefaultUrl(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    private String defaultUrl = "http://baidu.com";


    public void loadUrl(String url) {
        if (isNeedProgressBar()) {

            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setMessage("加载中");
            mProgressDialog.setMax(100);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
        }
//        mWebView.addJavascrip
        mFirstUrl = url;
        mWebView.loadUrl(url);
    }

    public boolean isNeedProgressBar() {

        return mShowDialog;
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mTitle = title;

            Prt.d(TAG, "onReceivedTitle " + mTitle);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            if (getActivity() instanceof IWebViewTitleControl) {
                ((IWebViewTitleControl) getActivity()).onReceivedIcon(icon);
            }

        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
            super.onReceivedTouchIconUrl(view, url, precomposed);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            // TODO Auto-generated method stub
            if (BuildConfig.DEBUG) {
                if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.ERROR) {
                    Prt.e(TAG, consoleMessage.message() + "/" + consoleMessage.messageLevel() + ": 在 " + consoleMessage.sourceId() + "第" + consoleMessage.lineNumber() + "行");

                } else {
                    Prt.w(TAG, consoleMessage.message() + "/" + consoleMessage.messageLevel() + ": 在 " + consoleMessage.sourceId() + "第" + consoleMessage.lineNumber() + "行");

                }
            }
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            Prt.w(TAG, "onShowCustomView " + view);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
            b.setTitle("温馨提示");
            b.setMessage(message);
            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            b.setCancelable(false);
            b.create().show();
            return true;
        }


        //设置响应js 的Confirm()函数
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
            b.setTitle("请操作");
            b.setMessage(message);
            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            });
            b.create().show();
            return true;
        }

        //设置响应js 的Prompt()函数
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
            showEditDialog(getActivity(), message, defaultValue, new EditNotify<String>() {
                @Override
                public void onNotify(String param) {
                    result.confirm(param);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            });
            return true;

        }

        @Override
        public void onProgressChanged(WebView view, int progress)//设置 加载进程
        {
            if (isNeedHead()) {
                getHeadView().setText("正在加载...");
            } else {
                if (getActivity() instanceof IWebViewTitleControl) {
                    ((IWebViewTitleControl) getActivity()).getTitleView().setText("" + mTitle != null ? mTitle : getContext().getResources().getText(R.string.app_name));
                }
            }
            if (isNeedProgressBar()) {
                mProgressDialog.setProgress(progress * 100);
            }
            if (progress >= 100) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = handler.obtainMessage();//发送通知，加入线程
                        msg.what = 2;//加载完成
                        handler.sendMessage(msg);//通知发送！
                    }
                }).start();
                if (isNeedHead()) {
                    getHeadView().setText(R.string.app_name);
                } else {
                    if (getActivity() instanceof IWebViewTitleControl) {
                        ((IWebViewTitleControl) getActivity()).getTitleView().setText("" + mTitle != null ? mTitle : getContext().getResources().getText(R.string.app_name));
                    }
                }
            }
        }
    }


        /* 无法覆盖
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Prt.i(TAG,"onJsAlert");
            ToastUtils.showToast(WebViewActivity.this,""+message);
        return false;
//            return super.onJsAlert(view, url, message, result);
        }
        */

    protected Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case FLAG_FAIL:
                    String data = "<html><body align='center'><big><b>请求出现错误" + message.obj + "\nERROR CODE:" + message.arg1 + "!!</b></big></body></html>";
//                    String data = "<html><body><div align=\"center\"><font size=\"5\">请求出现错误" + message.obj + "code:" + message.arg1 + "!!</font></div></body></html>";
                    mWebView.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
                    if (isNeedProgressBar()) {
                        mProgressDialog.dismiss();
                    }
                    break;
                case 2:
                    if (isNeedHead()) {
                        getHeadView().setText("" + mTitle != null ? mTitle : getContext().getResources().getText(R.string.app_name));
                    } else {
                        if (getActivity() instanceof IWebViewTitleControl) {
                            ((IWebViewTitleControl) getActivity()).getTitleView().setText("" + mTitle != null ? mTitle : getContext().getResources().getText(R.string.app_name));
                        }
                    }
                    if (isNeedProgressBar()) {
                        mProgressDialog.dismiss();
                    }
                    break;
            }
        }
    };


    //isNeedHead is true
    public TextView getHeadView() {
        return null;//
    }

    @Override
    public int getLayoutID() {
        return R.layout.qssq_fragment_webview;
    }


    public void enablecrossdomain() {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field.setAccessible(true);
            Object webviewcore = field.get(this);
            Method method = webviewcore.getClass().getDeclaredMethod("nativeRegisterURLSchemeAsLocal", String.class);
            method.setAccessible(true);
            method.invoke(webviewcore, "http");
            method.invoke(webviewcore, "https");
        } catch (Exception e) {
            Log.d(TAG, "enablecrossdomain error");
            e.printStackTrace();
        }
    }

    //for android 4.1+
    public void enablecrossdomain41() {
        try {
            Field webviewclassic_field = WebView.class.getDeclaredField("mProvider");
            webviewclassic_field.setAccessible(true);
            Object webviewclassic = webviewclassic_field.get(this);
            Field webviewcore_field = webviewclassic.getClass().getDeclaredField("mWebViewCore");
            webviewcore_field.setAccessible(true);
            Object mWebViewCore = webviewcore_field.get(webviewclassic);
            Field nativeclass_field = webviewclassic.getClass().getDeclaredField("mNativeClass");
            nativeclass_field.setAccessible(true);
            Object mNativeClass = nativeclass_field.get(webviewclassic);

            Method method = mWebViewCore.getClass().getDeclaredMethod("nativeRegisterURLSchemeAsLocal", new Class[]{int.class, String.class});
            method.setAccessible(true);
            method.invoke(mWebViewCore, mNativeClass, "http");
            method.invoke(mWebViewCore, mNativeClass, "https");
        } catch (Exception e) {
            Log.w(TAG, "enablecrossdomain error");
            e.printStackTrace();
        }
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setJavaScriptEnabled(true); //设置js权限，比如js弹出窗，你懂得
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);//设置自动加载图片
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);// ajax处理解决No 'Access-Control-Allow-Origin' header is present on the requested resource.
            mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
            mWebView.getSettings().setAllowContentAccess(true);
            this.enablecrossdomain41();
        } else {
            this.enablecrossdomain();
        }

        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//// 设置加载进来的页面自适应手机屏幕
        doInitWebView(mWebView);


        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //处理无法播放视频问题
        if (Build.VERSION.SDK_INT > 8) {//<8setPluginsEnabled 没法调用

            mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("ftp:") || url.startsWith("rtmp:") || url.startsWith("rtsp:")) {
                    mLastUrl = url;
                    return false;
                } else {

//                  String meiyeScheme = "meiye://";
                /*    if (url.startsWith(meiyeScheme)) {
                        url = url.replace(meiyeScheme, view.getContext().getPackageName()+".");
                    }*/
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
//                        ToastUtils.showToast("正在调用APP客户端");
                    } catch (Exception e) {
                        String protocol;
                        int i = url.indexOf("://");
                        if (i != -1) {
                            protocol = url.substring(0, i);
                        } else {
                            protocol = "(协议无法获取)action:" + url;
                        }
                        ToastUtils.showToast("无法打开协议:" + protocol + ",如果您认识此协议,请安装对应的APP客户端");
                    }
                    return true;
                }
                //表示不d处理 返回true表示我自己操作了

            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.stopLoading();
                view.clearView();
                Message msg = handler.obtainMessage();//发送通知，加入线程
                msg.what = FLAG_FAIL;//通知加载自定义404页面
                handler.sendMessage(msg);//通知发送！
                handler.obtainMessage(FLAG_FAIL, errorCode, -1, description.toString()).sendToTarget();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (enableAutoLoadExtraContent()) {
                    doExtraContentLogic(view);
                }
                handler.sendEmptyMessage(2);
            }

        });
       /* WechatShare object = new WechatShare();

        mWebView.addJavascriptInterface(object, "obj");*/
        doInitWebView(mWebView);
        String url;
        Bundle arguments = getArguments();
        if (arguments != null) {
            url = arguments.getString(INTENT_NAME_URL);
            mShowDialog = arguments.getBoolean(INTENT_NAME_SHOW_PROGRESS_DIALOG, false);
            mAutoPlayVideo = arguments.getBoolean(INTENT_AUTO_PLAY_VIDEO);
        } else {
            url = defaultUrl;
        }
        loadUrl(url);
    }

    protected void doInitWebView(WebView webView) {

    }

    /**
     * 默认处理视频的自动播放问题
     *
     * @param view
     */
    public void doExtraContentLogic(WebView view) {
        String jsonContent = "javascript:(function() { " + exPandVideoAutoPlayContent() + exPandJsContent() + "}})()";
        view.loadUrl(jsonContent);
    }

    private String exPandVideoAutoPlayContent() {
        if (autoPlayVideo()) {
            String js = "var videos = document.getElementsByTagName('video'); console.debug('video'+videos);for(var i=0;i<videos.length;i++){videos[i].play();console.debug('currentVideoUrl:'+video[i].currentSrc);";
            return js; //不知道为毛线不行。算了。

        } else {
            return "";
        }

    }

    protected boolean autoPlayVideo() {
        return mAutoPlayVideo;
    }

    protected boolean enableAutoLoadExtraContent() {
        return true;
    }

    protected String exPandJsContent() {
        String content = "console.debug('情随事迁','66666');";//可以断点调试动态进行修改此处
        return content;
    }


    @Override
    public void onResume() {
        if (mWebView != null) {
            mWebView.onResume();
        }
        super.onResume();
    }


    public static AlertDialog showEditDialog(Context context, String content, String defaultValue, final EditNotify<String> onClickListener, DialogInterface.OnClickListener onCanncelClick) {
        final EditText editText = new EditText(context);
        editText.setText("" + defaultValue);
        editText.setTextColor(SuperAppContext.getInstance().getResources().getColor(R.color.colorThemeColor));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(editText);
//        builder.setTitle(TextUtils.isEmpty(title) ? "温馨提示" : title + "");
        builder.setMessage(content);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onClickListener.onNotify(editText.getText().toString());
            }
        });
        builder.setPositiveButton("取消", onCanncelClick);
        AlertDialog show = builder.show();
        return show;
    }

    public interface EditNotify<T> {
        void onNotify(T param);
    }


    @Override
    public void onPause() {
        if (mWebView != null) {
            mWebView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory() 解决泄漏？？
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }

            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();


            mWebView.destroy();
        }
    }
}