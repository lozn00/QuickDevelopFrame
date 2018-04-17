package cn.qssq666.rapiddevelopframe.base.refresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import cn.qssq666.rapiddevelopframe.base.fragment.BaseCacheViewFragment;
import cn.qssq666.rapiddevelopframe.interfaces.AdapterI;

/**
 * Created by qssq on 2017/9/26 qssq666@foxmail.com
 * 2017年9月26日 16:02:35
 * 逻辑好乱 一部分代码是控制wrap ，一部分是提供给包裹。
 */

public abstract class BaseJSONRefreshFragmentN<ADAPTER extends RecyclerView.Adapter> extends BaseCacheViewFragment implements BaseJSONRefreshLogicI<ADAPTER> {

    public JSONRefreshRefreshWrap<ADAPTER> getRefreshWrap() {
        return refreshWrap;
    }

    private JSONRefreshRefreshWrap<ADAPTER> refreshWrap;


    @Override
    public void onViewCreatedFix(View view, @Nullable Bundle savedInstanceState) {
        refreshWrap = new JSONRefreshRefreshWrap<ADAPTER>() {
            @Override
            protected List parseJsonResult(String json) {
                return BaseJSONRefreshFragmentN.this.parseJsonResult(json);
            }

            @Override
            public void onParseSucc(List list) {
                BaseJSONRefreshFragmentN.this.onParseSucc(list);
            }

            @Override
            public RecyclerView getRecyclerView() {

                return BaseJSONRefreshFragmentN.this.getRecyclerView();
            }

            @Override
            public SmartRefreshLayout getSwipyRefreshLayout() {
                return (SmartRefreshLayout) BaseJSONRefreshFragmentN.this.getSwipyRefreshLayout();
            }

            @Override
            public void onInitStart() {
                BaseJSONRefreshFragmentN.this.onInitStart();
            }

            @Override
            public void onInitFinish() {
                BaseJSONRefreshFragmentN.this.onInitFinish();
            }

            @Override
            public RecyclerView.LayoutManager onCreateLayoutManager() {
                return BaseJSONRefreshFragmentN.this.onCreateLayoutManager();
            }

            @Override
            public ADAPTER onCreateAdapter() {
                return BaseJSONRefreshFragmentN.this.onCreateAdapter();
            }

            @Override
            public String getUrl(int page) {
                return BaseJSONRefreshFragmentN.this.getUrl(page);
            }

            @Override
            public boolean autoLoad() {
                return BaseJSONRefreshFragmentN.this.autoLoad();
            }

            @Override
            public boolean enableLoadMore() {
                return BaseJSONRefreshFragmentN.this.enableLoadMore();

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
