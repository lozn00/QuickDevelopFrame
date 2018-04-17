package cn.qssq666.rapiddevelopframe.base.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.qssq666.rapiddevelopframe.R;


/**
 * Created by luozheng on 2016/7/5.  qssq.space 可以返回
 */
public abstract class BaseActionBackFragment extends BaseFragment {


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (!mInit) {
            mInit = true;
            findViewById(R.id.view_head_left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBack();
                }
            });
            init(savedInstanceState);
        }
    }

    protected void setTitle(String title) {
        ((TextView) findViewById(R.id.view_head_center)).setText("" + title);
    }

    protected void setTitle(int title) {
        ((TextView) findViewById(R.id.view_head_center)).setText(title);
    }

    protected void onBack() {
        finishFragment();
    }
}
