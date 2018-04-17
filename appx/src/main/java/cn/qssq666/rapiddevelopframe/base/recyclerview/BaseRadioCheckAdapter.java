package cn.qssq666.rapiddevelopframe.base.recyclerview;

import android.support.v7.widget.RecyclerView;

import cn.qssq666.rapiddevelopframe.interfaces.OnItemClickListener;

/**
 * Created by qssq on 2018/4/11 qssq666@foxmail.com
 */

public abstract class BaseRadioCheckAdapter<MODEL, VH extends RecyclerView.ViewHolder> extends BaseRecyclervdapter<MODEL, VH> {


    public boolean hasCheck() {
        return checkPosition != INVAILD_POSITION;
    }

    public int getCheckPosition() {
        return checkPosition;
    }

    protected int checkPosition = INVAILD_POSITION;

    private static final int INVAILD_POSITION = -1;
    /**
     * 用来花瓣总数乘多少value
     */
    int value = 1;

    public void clearPosition() {
        checkPositon(INVAILD_POSITION);
    }

    public OnItemClickListener getOnCheckListener() {
        return onCheckListener;
    }

    public void setOnCheckListener(OnItemClickListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    OnItemClickListener onCheckListener;


    public void checkPositon(int position) {
        int lastPosition = checkPosition;
        checkPosition = position;
        if (lastPosition != INVAILD_POSITION) {
            notifyItemChanged(lastPosition);
        }
        if (position != INVAILD_POSITION) {
            notifyItemChanged(position);
        }
    }

    public boolean checkPositionIsVolid() {
        if(checkPosition ==INVAILD_POSITION || checkPosition>=getItemCount()){
            return true;
        }
        return false;
    }
}
