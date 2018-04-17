package cn.qssq666.rapiddevelopframe.base.fragment;

/**
 * Created by luozheng on 2016/9/26.  qssq666.cn Administrator
 */

public abstract class BaseTabFragment extends BaseNetFragment {
    public int getActionBarHeight() {
        return actionBarHeight;
    }

    public void setActionBarHeight(int actionBarHeight) {
        this.actionBarHeight = actionBarHeight;
    }

    int actionBarHeight;
}
