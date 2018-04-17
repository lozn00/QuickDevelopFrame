package cn.qssq666.rapiddevelopframe.base.refresh.lazy;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import cn.qssq666.rapiddevelopframe.base.fragment.LazyFragment;
import cn.qssq666.rapiddevelopframe.base.refresh.BaseJSONRefreshLogicI;
import cn.qssq666.rapiddevelopframe.base.refresh.JSONRefreshRefreshWrap;
import cn.qssq666.rapiddevelopframe.interfaces.AdapterI;

/**
 * Created by qssq on 2017/9/26 qssq666@foxmail.com
 * 2017年9月26日 16:02:35
 * 逻辑好乱 一部分代码是控制wrap ，一部分是提供给包裹。
 */

public abstract class BaseJSONLazyFragmentN<ADAPTER extends RecyclerView.Adapter> extends LazyFragment implements BaseJSONRefreshLogicI<ADAPTER> {

    public JSONRefreshRefreshWrap<ADAPTER> getRefreshWrap() {
        return refreshWrap;
    }

    private JSONRefreshRefreshWrap<ADAPTER> refreshWrap;

    @Override
    public void init(LayoutInflater inflater, ViewGroup container) {
        refreshWrap = new JSONRefreshRefreshWrap<ADAPTER>() {
            @Override
            protected List parseJsonResult(String json) {
                return BaseJSONLazyFragmentN.this.parseJsonResult(json);
            }

            @Override
            public void onParseSucc(List list) {
                BaseJSONLazyFragmentN.this.onParseSucc(list);
            }

            @Override
            public RecyclerView getRecyclerView() {

                return BaseJSONLazyFragmentN.this.getRecyclerView();
            }

            @Override
            public SmartRefreshLayout getSwipyRefreshLayout() {
                return (SmartRefreshLayout) BaseJSONLazyFragmentN.this.getSwipyRefreshLayout();
            }

            @Override
            public void onInitStart() {
                BaseJSONLazyFragmentN.this.onInitStart();
            }

            @Override
            public void onInitFinish() {
                BaseJSONLazyFragmentN.this.onInitFinish();
            }

            @Override
            public RecyclerView.LayoutManager onCreateLayoutManager() {
                return BaseJSONLazyFragmentN.this.onCreateLayoutManager();
            }

            @Override
            public ADAPTER onCreateAdapter() {
                return BaseJSONLazyFragmentN.this.onCreateAdapter();
            }

            @Override
            public String getUrl(int page) {
                return BaseJSONLazyFragmentN.this.getUrl(page);
            }

            @Override
            public boolean autoLoad() {
                return BaseJSONLazyFragmentN.this.autoLoad();
            }

            @Override
            public boolean enableLoadMore() {
                return BaseJSONLazyFragmentN.this.enableLoadMore();

            }
        };
        refreshWrap.init();
    }


    /**
     * 主线程
     *
     * @param list
     */
    protected abstract void onParseSucc(List list);


    @Override
    public boolean autoLoad() {
        return true;
    }

    @Override
    public boolean enableLoadMore() {
        return true;
    }


    @Override
    public boolean viewHasMore() {
        return true;

    }

    @Override
    public int getPage() {
        return refreshWrap.getPage();
    }

    @Override
    public void setPage(int page) {
        refreshWrap.setPage(page);
    }

    @Override
    public boolean isLoadMore() {
        return refreshWrap.isLoadMore();
    }

    @Override
    public void queryDataRereshAndSetProgressUi() {
        refreshWrap.queryDataRereshAndSetProgressUi();
    }

    @Override
    public void queryData() {
        refreshWrap.queryData();
    }

    @Override
    public void onNotMoreData() {
        refreshWrap.onNotMoreData();

    }


    @Override
    public void onInitStart() {

    }

    @Override
    public void doOnBottomRefreshLogic() {
        refreshWrap.doOnBottomRefreshLogic();
    }

    @Override
    public void doOnTopRefreshLogic() {
        refreshWrap.doOnTopRefreshLogic();
    }

    @Override
    public ADAPTER getAdapter() {
        return refreshWrap.getAdapter();
    }

    @Override
    public AdapterI getRecyclervdapter() {
        return refreshWrap.getRecyclervdapter();
    }
}
