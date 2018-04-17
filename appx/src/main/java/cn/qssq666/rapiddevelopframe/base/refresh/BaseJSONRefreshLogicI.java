package cn.qssq666.rapiddevelopframe.base.refresh;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by qssq on 2017/9/26 qssq666@foxmail.com
 */

public interface BaseJSONRefreshLogicI<ADAPTER extends RecyclerView.Adapter> extends RereshLogicI<ADAPTER> {

     List parseJsonResult(String json);

}
