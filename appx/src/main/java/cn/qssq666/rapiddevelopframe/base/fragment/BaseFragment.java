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

package cn.qssq666.rapiddevelopframe.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.umeng.analytics.MobclickAgent;

import cn.qssq666.rapiddevelopframe.BuildConfig;
import cn.qssq666.rapiddevelopframe.utils.Prt;

/**
 * Created by ccccc on 15/11/2.
 */
public abstract class BaseFragment extends SuperFragment {
    public View getRootView() {
        return mRootView;
    }

    private View mRootView;
    protected boolean mInit;

    public void setContext(Context mContext) {
        if(mContext==null){
            return;
        }
        this.mContext = mContext;
        Prt.w(TAG, "setContext hashCode:" + this.hashCode());

    }

    private Context mContext;
    public final String TAG = this.getClass().getSimpleName() + ".class";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Prt.d(TAG, "onCreate");
        }
    }

    protected View onCreateViewFix(LayoutInflater inflater, ViewGroup group) {
        return inflater.inflate(getLayoutID(), group, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
   setContext(getActivity());

        if (mRootView == null) {
            this.mRootView = onCreateViewFix(inflater, container);
            if (isNeedHead()) {
                //false就只保留参数而不是他的孩子,
                View headView = LayoutInflater.from(getFragmentActivity()).inflate(getHeadViewResouce(), container, false);
                //不能有父亲因为在Viewg
                if (getBaseView() instanceof LinearLayout) {
                    ((LinearLayout) getBaseView()).addView(headView, getHeadViewIndex());
                } else if (getBaseView() instanceof RelativeLayout) {
                    if (isFromSonGetIndex()) {
                        ((ViewGroup) ((RelativeLayout) getBaseView()).getChildAt(0)).addView(headView, getHeadViewIndex());
                    } else {
                        ((RelativeLayout) getBaseView()).addView(headView, getHeadViewIndex());
                    }
                } else if (getBaseView() instanceof ScrollView) {
                    ((ViewGroup) ((ScrollView) getBaseView()).getChildAt(0)).addView(headView, getHeadViewIndex());
                } else if (getBaseView() instanceof FrameLayout) {
                    if (isFromSonGetIndex()) {
                        ((ViewGroup) ((FrameLayout) getBaseView()).getChildAt(0)).addView(headView, getHeadViewIndex());
                    } else {
                        ((FrameLayout) getBaseView()).addView(headView, getHeadViewIndex());
                    }
                } else {
                    throw new RuntimeException("继承错误");
                }
            }
        }
        return mRootView;
    }

    protected View findViewById(int id) {
        return getBaseView().findViewById(id);
    }

    protected View getBaseView() {
        Prt.d(TAG, "mRootView:" + mRootView);
        return mRootView;
    }

    public FragmentActivity getFragmentActivity() {
        return (FragmentActivity) mContext;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (!mInit) {
            mInit = true;
            init(savedInstanceState);
        }

    }

    protected int getHeadViewResouce() {
        return 0;
    }

    protected int getHeadViewIndex() {
        return 0;
    }

    protected boolean isFromSonGetIndex() {
        return false;
    }

    protected boolean isNeedHead() {
        return false;
    }

    protected abstract int getLayoutID();

    protected void finishFragment() {
        FragmentManager fragmentManager = getFragmentActivity().getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
//            Utils.showToast(mActivity, "哇哦,没得返回啦!");
            getFragmentActivity().onBackPressed();
            return;
        }
        fragmentManager.popBackStack();
    }

    /**
     * 关闭自己在自容器
     */
    protected void finishFragmentAtChildContainer() {
        FragmentManager fragmentManager = this.getFragmentManager();//关闭当前子布局这一层，如果调用this.getParent
        if (fragmentManager.getBackStackEntryCount() == 0) {
            finishFragment();
            return;
        }
        fragmentManager.popBackStack();
    }

    /**
     * 要获取container,请调用get
     *
     * @param savedInstanceState
     */
    protected abstract void init(Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRootView != null) {
            ViewGroup viewGroup = ((ViewGroup) mRootView.getParent());
            if (viewGroup != null) {
                viewGroup.removeView(mRootView);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("" + this.getClass().getSimpleName());
        if (BuildConfig.DEBUG) {
            Prt.d(TAG, "onResume");

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) {
            Prt.d(TAG, "onStart");
        }
    }

    @Override
    public void onPause() {
        super.onPause();//http://dev.umeng.com/analytics/android-doc/integration
        MobclickAgent.onPageEnd("" + this.getClass().getSimpleName());
        if (BuildConfig.DEBUG) {
            Prt.d(TAG, "onPause");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            Prt.d(TAG, "onDestroy");
        }
    }
}
