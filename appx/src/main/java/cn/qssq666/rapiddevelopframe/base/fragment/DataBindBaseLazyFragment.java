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

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/2/14.
 */

public abstract class DataBindBaseLazyFragment<T extends ViewDataBinding> extends LazyFragment {
    public T getDataBind() {
        return binding;
    }

    protected T binding;

    public boolean isInit() {
        return mInit;
    }

    protected boolean mInit;

    @Override
    public View onCreateViewFix(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {

            binding = DataBindingUtil.inflate(inflater, getLayoutID(), container, false);
        }
        return binding.getRoot();
    }


    abstract protected int getLayoutID();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        /*if (!mInit) {
            mInit = true;
            init(view, savedInstanceState);
        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getRootView() != null) {
            ViewGroup viewGroup = ((ViewGroup) getRootView().getParent());
            if (viewGroup != null) {
                viewGroup.removeView(getRootView());
            }
        }

    }


}
