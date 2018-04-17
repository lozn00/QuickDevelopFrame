package cn.qssq666.rapiddevelopframe.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

import cn.qssq666.rapiddevelopframe.https.NetQuestTask;
import cn.qssq666.rapiddevelopframe.utils.SystemBarTintManager;
import cn.qssq666.rapiddevelopframe.utils.leakfix.InputMethodFix;

/**
 * Created by luozheng on 2016/6/24.  qssq.space
 */
public class EmptyBaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (needHieghtStatusBar()) {

            SystemBarTintManager.statusBarLightModeFontBlack(this);
        }
    }


    public EmptyBaseActivity getFragmentActivity() {
        return this;
    }
    /**
     * 高粱 zhuangtailan zidi font black
     * @return
     */
    protected boolean needHieghtStatusBar() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        InputMethodFix.fixFocusedViewLeak(this);
        InputMethodFix.fixInputMethodManagerLeakxxxx(this);
//        InputMethodFix.fixInputMethodManagerLeak(this);
//        NetQuestTask.destory(this);
        NetQuestTask.cancelAllDefaultTagRequest(this);
        NetQuestTask.cancelRequest(this.getClass().getName());
    }
}
