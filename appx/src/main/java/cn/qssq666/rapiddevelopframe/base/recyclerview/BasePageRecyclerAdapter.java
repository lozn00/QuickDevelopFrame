package cn.qssq666.rapiddevelopframe.base.recyclerview;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by luozheng on 2016/3/31.  qssq.space  数据 +viewholder模型 加点击事件
 */
public abstract  class BasePageRecyclerAdapter<MODEL,VH extends RecyclerView.ViewHolder> extends BaseRecyclervdapter<MODEL,VH> {
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private int page;
    public BasePageRecyclerAdapter() {
        super();
    }
    public BasePageRecyclerAdapter( List models) {
        super( models);
    }

}
