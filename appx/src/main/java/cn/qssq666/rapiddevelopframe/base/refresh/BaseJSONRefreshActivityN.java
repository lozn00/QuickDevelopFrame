package cn.qssq666.rapiddevelopframe.base.refresh;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import cn.qssq666.rapiddevelopframe.base.activity.BaseActionBarActivity;
import cn.qssq666.rapiddevelopframe.interfaces.AdapterI;

/**
 * Created by qssq on 2017/9/26 qssq666@foxmail.com
 * 2017年9月26日 16:02:35
 * 逻辑好乱 一部分代码是控制wrap ，一部分是提供给包裹。
 */

public abstract class BaseJSONRefreshActivityN<ADAPTER extends RecyclerView.Adapter> extends BaseActionBarActivity implements BaseJSONRefreshLogicI<ADAPTER> {

    public JSONRefreshRefreshWrap<ADAPTER> getRefreshWrap() {
        return refreshWrap;
    }

    private JSONRefreshRefreshWrap<ADAPTER> refreshWrap;

    @Override
    protected final void init(Bundle savedInstanceState) {
        refreshWrap = new JSONRefreshRefreshWrap<ADAPTER>() {
            @Override
            protected List parseJsonResult(String json) {
                return BaseJSONRefreshActivityN.this.parseJsonResult(json);
            }

            @Override
            public RecyclerView getRecyclerView() {

                return BaseJSONRefreshActivityN.this.getRecyclerView();
            }


            public  String getInterceptEmptyDataTip(){
                return BaseJSONRefreshActivityN.this.getInterceptEmptyDataTip();
            }
            public boolean isInterceptEmptyData(){
                return BaseJSONRefreshActivityN.this.isInterceptEmptyData();
            }


            @Override
            public void onParseSucc(List list) {
                BaseJSONRefreshActivityN.this.onParseSucc(list);
            }


            @Override
            public SmartRefreshLayout getSwipyRefreshLayout() {
                return (SmartRefreshLayout) BaseJSONRefreshActivityN.this.getSwipyRefreshLayout();
            }

            @Override
            public void onInitStart() {
                BaseJSONRefreshActivityN.this.onInitStart();
            }

            @Override
            public void onInitFinish() {
                BaseJSONRefreshActivityN.this.onInitFinish();
            }

            @Override
            public RecyclerView.LayoutManager onCreateLayoutManager() {
                return BaseJSONRefreshActivityN.this.onCreateLayoutManager();
            }

            @Override
            public ADAPTER onCreateAdapter() {
                return BaseJSONRefreshActivityN.this.onCreateAdapter();
            }

            @Override
            public String getUrl(int page) {
                return BaseJSONRefreshActivityN.this.getUrl(page);
            }

            @Override
            public boolean autoLoad() {
                return BaseJSONRefreshActivityN.this.autoLoad();
            }

            @Override
            public boolean enableLoadMore() {
                return BaseJSONRefreshActivityN.this.enableLoadMore();

            }

            @Override
            protected boolean needEmptyView() {
                return BaseJSONRefreshActivityN.this.needEmptyView();
            }
        };
        refreshWrap.init();
    }

    protected boolean needEmptyView() {
        return true;
    }

    /**
     *  主线程的数据处理完毕方法。
     * @param list
     */
    protected void onParseSucc(List list) {

    }


    @Override
    protected View onCreateViewFix() {
        return super.onCreateViewFix();
    }

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

    protected boolean isInterceptEmptyData() {
        return false;
    }

    protected String getInterceptEmptyDataTip() {
        return "data is empty";
    }
}
