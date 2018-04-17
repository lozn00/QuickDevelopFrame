package cn.qssq666.rapiddevelopframe.base.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cn.qssq666.rapiddevelopframe.R;
import cn.qssq666.rapiddevelopframe.utils.SystemBarTintManager;


/**
 * Created by luozheng on 15/10/30.
 * 继承本类的activity必须是线性垂直布局
 */
public abstract class BaseActivity extends EmptyBaseActivity {

    protected final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (needInitStatusBarColor()) {
            initBar();
        }
        View view = onCreateViewFix(savedInstanceState);
        init(savedInstanceState);

    }

    protected View onCreateViewFix(Bundle savedInstanceState) {
        View inflate = LayoutInflater.from(this).inflate(getLayoutID(), null);
        setContentView(inflate);
        return inflate;

    }

    /**
     * BaseActivity Call
     *
     * @param savedInstanceState
     */
    protected void superOnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected boolean needInitStatusBarColor() {
        return false;
    }

    /**
     * 头添加到第一位 主要用于相对 ，frame
     *
     * @return
     */

    /**
     * 需要头布局吗?
     *
     * @return
     */

    protected abstract void init(Bundle savedInstanceState);

    protected abstract int getLayoutID();


    /**
     * default return R.color.colorBlack
     *
     * @return
     */
    protected int getStatusBarColor() {
        return R.color.colorPrimaryDark;
    }

    /**
     * 默认为关闭当前界面
     */


    protected boolean needStatusSpecialeffects() {
        return true;
    }

    protected void initSystemBar(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

//            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            setTranslucentStatus(activity, true);

        } else {
            Log.i(TAG, "您的手机不支持状态栏一体化");
        }


        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);

// 使用颜色资源

        tintManager.setStatusBarTintResource(getStatusBarColor());

    }

    @TargetApi(19)

    protected static void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();

        WindowManager.LayoutParams winParams = win.getAttributes();

        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {

            winParams.flags |= bits;

        } else {

            winParams.flags &= ~bits;

        }

        win.setAttributes(winParams);

    }

    protected void initBar() {
        if (needStatusSpecialeffects()) {
            initSystemBar(this);
        }

    }


    /**
     * 其实是未首页需要才提供的方法,没办法咯
     *
     * @param activity Activity
     * @param color    R.color
     */
    public void setStatusBarColor(Activity activity, int color) {
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);

        tintManager.setStatusBarTintEnabled(true);
// 使用颜色资源

        tintManager.setStatusBarTintResource(color);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

}
