package cn.qssq666.rapiddevelopframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import cn.qssq666.rapiddevelopframe.R;
import cn.qssq666.rapiddevelopframe.base.activity.BaseActionBarActivity;
import cn.qssq666.rapiddevelopframe.utils.FragmentUtil;

/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/4/6.
 * 返回键直接关闭。
 */

public class FragmentContainerActivity extends BaseActionBarActivity {

    public static final String INTENT_FRAGMENT_NAME = "INTENT_FRAGMENT_NAME";
    public static final String INTENT_TITLE = "INTENT_TITLE";
    private String mTitle;
    private String fragmentClassName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        fragmentClassName = intent.getStringExtra(INTENT_FRAGMENT_NAME);
        mTitle = intent.getStringExtra(INTENT_TITLE);
        super.onCreate(savedInstanceState);


    }

    @Override
    protected boolean isNeedHead() {
        return mTitle != null;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_fragment_container;
    }

    @Override
    protected String getHeadTitle() {
        return mTitle;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        Class<Fragment> fragmentClass = null;
        try {
            fragmentClass = (Class<Fragment>) Class.forName(fragmentClassName);
            FragmentUtil.replaceFragment(this, fragmentClass, this.getIntent().getExtras());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        finish();
    }

}
