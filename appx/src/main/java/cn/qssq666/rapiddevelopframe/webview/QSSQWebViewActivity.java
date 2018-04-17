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

package cn.qssq666.rapiddevelopframe.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.widget.TextView;

import cn.qssq666.rapiddevelopframe.R;
import cn.qssq666.rapiddevelopframe.base.activity.BaseActionBarActivity;
import cn.qssq666.rapiddevelopframe.utils.FragmentUtil;


/**
 * Created by luozheng on 2016/11/5.  qssq.space
 */

public class QSSQWebViewActivity extends BaseActionBarActivity implements IWebViewTitleControl, FragmentManager.OnBackStackChangedListener {


    private Fragment fragment;


    protected TextView tvTitle;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;


    @Override
    protected void init(Bundle savedInstanceState) {

        tvTitle = ((TextView) findViewById(R.id.view_head_center));
        Bundle bundle = new Bundle();
        Intent intent = getIntent();
        if (intent != null) {
            String temp = intent.getStringExtra(WebViewBaseFragment.INTENT_NAME_URL);
            if (!TextUtils.isEmpty(temp)) {
                url = temp;
            }

            bundle.putBoolean(WebViewBaseFragment.INTENT_AUTO_PLAY_VIDEO, intent.getBooleanExtra(WebViewBaseFragment.INTENT_AUTO_PLAY_VIDEO, false));
            bundle.putBoolean(WebViewBaseFragment.INTENT_NAME_SHOW_PROGRESS_DIALOG, intent.getBooleanExtra(WebViewBaseFragment.INTENT_NAME_SHOW_PROGRESS_DIALOG, false));
        }

        bundle.putString(WebViewBaseFragment.INTENT_NAME_URL, url);
//        WebViewBaseFragment webViewBaseFragment = new WebViewBaseFragment();

        this.getSupportFragmentManager().addOnBackStackChangedListener(this);
        doLoadWebViewFragment(bundle);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getSupportFragmentManager().removeOnBackStackChangedListener(this);
    }

    @Override
    public void onBackStackChanged() {
        fragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_space);
    }


    protected void doLoadWebViewFragment(Bundle bundle) {
        Class<? extends WebViewBaseFragment> webViewClass = getWebViewClass();
        try {
            fragment = webViewClass.newInstance();
            FragmentUtil.replaceFragment(this, fragment, bundle, false);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    protected Class<? extends WebViewBaseFragment> getWebViewClass() {
        return WebViewBaseFragment.class;

    }

    @Override
    protected boolean isNeedHead() {
        return true;
    }

    @Override
    protected int getHeadViewResouce() {
        return R.layout.view_head_back;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.qssq_activity_frag_container;
    }

    @Override
    protected String getHeadTitle() {
        return "网页浏览";
    }


    @Override
    public TextView getTitleView() {
        return tvTitle;
    }

    @Override
    public void onReceivedIcon(Bitmap bitmap) {

    }

    @Override
    protected void onHeadLeftViewOnclick() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (fragment instanceof WebViewBackControlI) {
            boolean b = ((WebViewBackControlI) fragment).requestBackpress();
            if (b) {
                return;
            }
        }
        super.onBackPressed();
    }
}
