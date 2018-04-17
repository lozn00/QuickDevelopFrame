package cn.qssq666.rapiddevelopframe.base.activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import cn.qssq666.rapiddevelopframe.R;
import cn.qssq666.rapiddevelopframe.global.SuperAppContext;
import cn.qssq666.rapiddevelopframe.interfaces.ILoadProgress;
import cn.qssq666.rapiddevelopframe.utils.AppUtils;

/**
 * Created by qssq on 2017/9/28 qssq666@foxmail.com
 */

public abstract class BaseNetActionBarActivity extends BaseActionBarActivity implements ILoadProgress {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isNeedHead() {
        return false;
    }

    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected String getHeadTitle() {
        return null;
    }

    public void onLoadStart() {
        onLoadStart(null, null);
    }

    @Override

    public void onLoadStart(String title, String message) {
        progressDialog.show();
        if (title != null) {
            progressDialog.setTitle(title);
        } else {
            progressDialog.setTitle(SuperAppContext.getInstance().getString(R.string.pleasewait));
        }
        if (message != null) {
            progressDialog.setMessage(message);
        } else {
            progressDialog.setMessage("");
        }
    }

    @Override
    public void onLoadSucc() {
        progressDialog.dismiss();
    }


    public void onLoadFail(String str) {
        onLoadFail(null, str);
    }

    @Override
    public void onLoadFail(String title, String error) {

        if (title != null) {
            progressDialog.setTitle(title);
        }
        if (error != null) {
            progressDialog.setMessage(error);
        } else {
            progressDialog.setMessage("");
        }

        AppUtils.delayCloseProgressDialog(progressDialog, error);
    }
}
