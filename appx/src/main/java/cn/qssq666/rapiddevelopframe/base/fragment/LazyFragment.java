package cn.qssq666.rapiddevelopframe.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qssq666.rapiddevelopframe.BuildConfig;

/**
 * Created by qssq on 2017/11/14 qssq666@foxmail.com
 * BUG 第一个选项卡，切换一次就不显示了。
 */

public abstract class LazyFragment extends SuperFragment {
    protected static final String TAG = "LazyFragment";

    public View getRootView() {
        return mRootView;
    }

    private View mRootView;
    private boolean mIsInited;
    private boolean mIsPrepared;


    public View onCreateViewFix(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutID(), container, false);

    }


    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {

            try {
                mRootView = onCreateViewFix(inflater, container, savedInstanceState);

            } catch (Exception e) {
//                Prt.e(TAG, "" + this.getClass().getSimpleName(), e);

                throw new UnsupportedOperationException("Fragment[" + this.getClass().getSimpleName()+"]", e);
            }
        } else {

            mRootView = onCreateViewFix(inflater, container, savedInstanceState);
        }
        mIsPrepared = true;
        lazyLoad(inflater, container);
        return mRootView;
    }

    public void lazyLoad(LayoutInflater inflater, ViewGroup container) {
        if (getUserVisibleHint() && mIsPrepared && !mIsInited) {
            //异步初始化，在初始化后显示正常UI
            mIsInited = true;
            init(inflater, container);
        }
    }

    //1. 加载数据
    //2. 更新UI
    //3. mIsInited = true

    public abstract void init(LayoutInflater inflater, ViewGroup container);


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyLoad(null, null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*if (getRootView() != null) {
            ViewGroup viewGroup = ((ViewGroup) getRootView());
            if (viewGroup != null) {
                viewGroup.removeView(getRootView());
            }
        }*/

    }

    protected abstract int getLayoutID();
}
