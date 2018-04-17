package cn.qssq666.rapiddevelopframe.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qssq666.rapiddevelopframe.interfaces.ILoadProgress;

/**
 * Created by luozheng on 2016/9/26.  qssq666.cn Administrator
 * <p>
 * 适用于  FragmentPagerAdapter 维护的fragment 且是fragment->fragment
 */

public abstract class CacheFragment extends SuperFragment implements ILoadProgress {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//    return super.onCreateViewFix(inflater, container, savedInstanceState);
        return inflater.inflate(getLayoutID(), container, false);
    }
    public abstract int getLayoutID();

    @Nullable

    @Override
    public void onLoadStart(String title, String content) {
        if (getActivity() instanceof ILoadProgress) {
            ILoadProgress iLoadProgress = (ILoadProgress) getActivity();
            iLoadProgress.onLoadStart(title, content);
        } else {
            Log.e(TAG, "无法调用Activity onLoadStart");
        }
    }

    @Override
    public void onLoadFail(String str, String error) {
        if (getActivity() instanceof ILoadProgress) {
            ILoadProgress iLoadProgress = (ILoadProgress) getActivity();
            iLoadProgress.onLoadFail(str, error);
        } else {
            Log.e(TAG, "无法调用Activity onLoadFail");
        }
    }

    public void onLoadFail(String error) {
        onLoadFail(null, error);
    }

    @Override
    public void onLoadSucc() {
        if (getActivity() instanceof ILoadProgress) {
            ILoadProgress iLoadProgress = (ILoadProgress) getActivity();
            iLoadProgress.onLoadSucc();
        } else {
            Log.e(TAG, "无法调用Activity onLoadSucc");
        }
    }


}
