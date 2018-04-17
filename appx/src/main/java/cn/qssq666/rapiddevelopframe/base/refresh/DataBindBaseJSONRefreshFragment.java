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

package cn.qssq666.rapiddevelopframe.base.refresh;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qssq666.rapiddevelopframe.utils.AppThemeUtilsX;

/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/2/14.
 */

public abstract class DataBindBaseJSONRefreshFragment<ADAPTER extends RecyclerView.Adapter, T extends ViewDataBinding> extends BaseJSONRefreshFragmentN<ADAPTER> {
    private T binding;

    public T getDataBind() {
        return binding;
    }


    abstract protected int getLayoutID();

    @Override
    public View onCreateViewFix(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutID(), container, false);
        return binding.getRoot();
    }



}
