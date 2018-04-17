package cn.qssq666.rapiddevelopframe.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.qssq666.rapiddevelopframe.https.NetQuestTask;
import cn.qssq666.rapiddevelopframe.utils.CrashHandler;
import cn.qssq666.rapiddevelopframe.utils.leakfix.InputMethodFix;


/**
 * Created by luozheng on 2016/5/6.  qssq.space
 */
public class SuperAppContext extends Application {


    private static Toast mToast;


    public static void setContext(SuperAppContext context) {
        SuperAppContext.context = context;
    }

    private static SuperAppContext context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        initImageLoader(SuperAppContext.getInstance());

//        MobclickAgent.setScenarioType(Context context, EScenarioType etype)
        CrashHandler crashHandler = new CrashHandler();
        crashHandler.init(this);
        InputMethodFix.fixFocusedViewLeak(this);
        NetQuestTask.getRequestQueue(this);
//        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);

    }

    public static void initImageLoader(Context context) {
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                    .createDefault(context);
            ImageLoader.getInstance().init(configuration);
        }


    }


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        android.support.multidex.MultiDex.install(this);
    }

    public static Handler getHandler() {
        return handler;
    }


    static Handler handler = new Handler(Looper.myLooper());

    public static SuperAppContext getInstance() {
        return context;
    }


    public static void showToast(final String str) {
        showToast(str, Toast.LENGTH_SHORT);
    }

    public static void showToast(final String str, final int duration) {

        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {

            mToast.setDuration(duration);
            mToast.setText("" + str);
            mToast.show();
        } else {
            SuperAppContext.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    mToast.setDuration(duration);
                    mToast.setText("" + str);
                    mToast.show();
                }
            });
        }

    }

}
