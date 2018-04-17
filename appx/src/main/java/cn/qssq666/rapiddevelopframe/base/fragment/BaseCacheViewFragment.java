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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/2/22.
 */

public abstract class BaseCacheViewFragment extends SuperFragment {

    public final String TAG = this.getClass().getSimpleName();
    protected boolean mInit;
    private View mView;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null || mInit == false) {//mInitfalse则重新加载

            mView = onCreateViewFix(inflater, container, savedInstanceState);
        }
        return mView;

    }

    public void clearCacheView() {
        mView = null;
    }

    abstract public View onCreateViewFix(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (!mInit) {
            mInit = true;
            onViewCreatedFix(view, savedInstanceState);
        }
    }

    public abstract void onViewCreatedFix(View view, @Nullable Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mView != null) {
            ViewGroup viewGroup = ((ViewGroup) mView.getParent());
            if (viewGroup != null) {
                viewGroup.removeView(mView);
            }
        }

    }


}
