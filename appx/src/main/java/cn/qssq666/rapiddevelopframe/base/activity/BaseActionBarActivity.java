package cn.qssq666.rapiddevelopframe.base.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.qssq666.rapiddevelopframe.R;
import cn.qssq666.rapiddevelopframe.utils.SystemBarTintManager;


/**
 * Created by luozheng on 15/10/30.
 * 继承本类的activity必须是线性垂直布局
 */
public abstract class BaseActionBarActivity extends EmptyBaseActivity {

    protected final String TAG = this.getClass().getSimpleName();

    private View mTvTitle;
    private View mTvLeft;
    private View mTvRight;
    protected RelativeLayout mHeadView;

    /**
     * @param strLeft
     * @param strRight
     */
    protected void setBarTitle(String strLeft, String strRight) {
        if (!isNeedHead()) {
            throw new RuntimeException("不需要头就不能在调用本方法");
        }
        if (strLeft != null) {
            mTvLeft.setBackgroundResource(0);
            ((TextView) mTvLeft).setText("" + strLeft);

        }
        if (strRight != null) {
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setBackgroundResource(0);
            ((TextView) mTvRight).setText("" + strRight);
        }

    }


    protected void setBarTitle(int resourceLeft, int resourceRight) {
        if (resourceLeft != 0) {

            mTvLeft.setBackgroundResource(resourceLeft);
        }
        if (resourceRight != 0) {
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setBackgroundResource(resourceRight);
        }
    }

    public boolean needInitStatusBarColor() {
        return false;
    }

    public boolean fromSon() {
        return false;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onCreateBefore(savedInstanceState);
        if (needInitStatusBarColor()) {
            initBar();
        }

        View view = onCreateViewFix();
        onCreateViewFixFinish(view);

        if (isNeedHead()) {
            if (view instanceof LinearLayout) {

                onAddHeadView((ViewGroup) view);
            } else if (
                    view instanceof ViewGroup) {
                if (fromSon()) {

                    onAddHeadView((ViewGroup) ((ViewGroup) view).getChildAt(0));
                } else {
                    onAddHeadView((ViewGroup) ((ViewGroup) view).getChildAt(0));
                }
//            ((ScrollView) view).addView(LayoutInflater.from(this).inflate(com.aidigong.tongrushi.R.layout.view_head, null), 0);
            } else {
            }
            mTvLeft = view.findViewById(R.id.view_head_left);
            mTvTitle = view.findViewById(R.id.view_head_center);
            mTvRight = view.findViewById(R.id.view_head_right);
            if (mTvLeft != null) {
                mTvLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onHeadLeftViewOnclick();
                    }
                });

            }
            if (mTvRight != null) {
                mTvRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onHeadRightViewOnclick();
                    }
                });

            }
            setHeaderTitle(getHeadTitle());
        }

        init(savedInstanceState);
    }

    protected void onCreateBefore(Bundle savedInstanceState) {

    }

    protected ViewGroup onAddHeadView(ViewGroup viewGroup) {
        viewGroup.addView(LayoutInflater.from(this).inflate(getHeadViewResouce(), viewGroup, false), getHeadViewIndex());
        return viewGroup;
    }


    protected void onCreateViewFixFinish(View view) {

    }

    protected View onCreateViewFix() {

        View view = LayoutInflater.from(this).inflate(getLayoutID(), null);
        setContentView(view);
        return view;
    }


    abstract protected void init(Bundle savedInstanceState);

    public void setHeaderTitle(String str) {

        if(mTvTitle!=null){
        ((TextView) mTvTitle).setText(str);
        }
    }

    protected int getHeadViewResouce() {
        return R.layout.view_head;
    }

    /**
     * 头添加到第一位 主要用于相对 ，frame
     *
     * @return
     */
    protected int getHeadViewIndex() {
        return 0;
    }

    /**
     * 需要头布局吗?
     *
     * @return
     */
    protected boolean isNeedHead() {
        return false;
    }

    protected abstract int getLayoutID();

    /**
     * 每一个界面顶部中间的的标题 不能使用getTitle因为activity这个类已经使用了我日 后面发现有一个巨大的问题
     */
    protected abstract String getHeadTitle();

    protected View getHeadLeftView() {
        return mTvLeft;
    }

    protected View getHeadRightView() {
        return mTvRight;
    }

    protected TextView getHeadCenterView() {
        return (TextView) mTvTitle;
    }

    /**
     * default return R.color.colorBlack
     *
     * @return
     */
    protected int getStatusBarColor() {
        return android.R.attr.statusBarColor;
    }

    /**
     * 默认为关闭当前界面
     */
    protected void onHeadLeftViewOnclick() {
        finish();
    }

    protected void onHeadRightViewOnclick() {

    }

    protected boolean needStatusSpecialeffects() {
        return true;
    }

    protected void initSystemBar(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

//            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            setTranslucentStatus(activity, false);

        } else {
            Log.i(TAG, "您的手机不支持状态栏一体化");
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);

        tintManager.setStatusBarTintEnabled(true);

// 使用颜色资源

        tintManager.setStatusBarTintResource(getStatusBarColor());

    }

    public boolean controlLogin() {
        return true;
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

    @Override
    protected void onResume() {
        super.onResume();
//      MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    protected void hiddenHeadLeft() {
        mTvLeft.setVisibility(View.INVISIBLE);
    }

    protected void hiddenHeadRight() {
        mTvLeft.setVisibility(View.INVISIBLE);
    }

    protected void hiddenHeadCenter() {
        mTvTitle.setVisibility(View.INVISIBLE);
    }

    protected RelativeLayout getHeadView() {
        if (mHeadView == null) {

            mHeadView = (RelativeLayout) findViewById(R.id.view_head);
        }
        return mHeadView;
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


}
