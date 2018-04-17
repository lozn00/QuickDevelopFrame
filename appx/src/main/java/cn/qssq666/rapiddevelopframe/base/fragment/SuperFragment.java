package cn.qssq666.rapiddevelopframe.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

import cn.qssq666.rapiddevelopframe.BuildConfig;


/**
 * Created by luozheng on 2016/9/26.  qssq666.cn Administrator
 */

public class SuperFragment extends Fragment {
    public final String TAG = this.getClass().getSimpleName() + ".class";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate" + this.getClass().hashCode());
        }
//        Log.i(TAG, "onCreate->" + this.getClass().hashCode());

    }
    protected Fragment getFragment(){
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onStop() {
        super.onStop();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            //onDestory
            Log.d(TAG, "onDestory" + this.getClass().hashCode());
        }
    }
}
