package cn.qssq666.rapiddevelopframe.viewholder;

import android.view.View;
import android.widget.TextView;

import cn.qssq666.rapiddevelopframe.R;

/**
 * Created by qssq on 2018/2/8 qssq666@foxmail.com
 */
public class ViewDataEmptyBinding {
    public ViewDataEmptyBinding() {
    }

    public ViewDataEmptyBinding(View root) {
        setRootView(root);

    }
    //    View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_data_empty, getEmptyViewInsertViewGroup(), false);

    public TextView tvReason;
    public View btnReload;
    private View root;

    public View getRoot() {
        return root;
    }

    public void setRootView(View rootView) {
        this.root = rootView;
        tvReason = root.findViewById(R.id.tv_reason);
        btnReload = root.findViewById(R.id.btn_reload);
    }
}
