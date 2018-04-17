/*
 *
 *                     .::::.
 *                   .::::::::.
 *                  :::::::::::  by qssq666@foxmail.com
 *              ..:::::::::::'
 *            '::::::::::::'
 *              .::::::::::
 *         '::::::::::::::..
 *              ..::::::::::::.
 *            ``::::::::::::::::
 *             ::::``:::::::::'        .:::.
 *            ::::'   ':::::'       .::::::::.
 *          .::::'      ::::     .:::::::'::::.
 *         .:::'       :::::  .:::::::::' ':::::.
 *        .::'        :::::.:::::::::'      ':::::.
 *       .::'         ::::::::::::::'         ``::::.
 *   ...:::           ::::::::::::'              ``::.
 *  ```` ':.          ':::::::::'                  ::::..
 *                     '.:::::'                    ':'````..
 *
 */

package cn.qssq666.rapiddevelopframe.base.refresh;

import android.support.v7.widget.RecyclerView;

import cn.qssq666.rapiddevelopframe.interfaces.AdapterI;

/**
 * Created by qssq on 2017/9/5 qssq666@foxmail.com
 */

public interface RereshLogicI<ADAPTER extends RecyclerView.Adapter> {


    public ADAPTER getAdapter();


    /**
     * 如果这个界面都没有更多就不需要设置。
     *
     * @return
     */
    public boolean viewHasMore();

    public int getPage();

    public void setPage(int page);


    boolean isLoadMore();
  /*    void onInitFinish();

     void onInitStart() ;*/


    RecyclerView.LayoutManager onCreateLayoutManager();

    AdapterI getRecyclervdapter();

    ADAPTER onCreateAdapter();


    String getUrl(int page);

    void queryDataRereshAndSetProgressUi();

    /**
     * 不设置进度按钮
     */
    abstract public void queryData();

    void onNotMoreData();


    boolean autoLoad();

    //是否能过加载更多
    public boolean enableLoadMore();


    RecyclerView getRecyclerView();

    void onInitFinish();
    void onInitStart();
    Object getSwipyRefreshLayout();


/*    @Override
    public void onViewCreatedFix(View view, @Nullable Bundle savedInstanceState) {
        onInitStart();
        getRecyclerView().setLayoutManager(onCreateLayoutManager());
//        getRecyclerView().setHasFixedSize(true);
        mAdapter = onCreateAdapter();
        getRecyclerView().setAdapter(mAdapter);
        AppThemeUtils.setSwiperRefreshLayoutStyle(getSwipyRefreshLayout());

        getSwipyRefreshLayout().setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                doOnTopRefreshLogic();
            }
        });


        if (enableLoadMore()) {
            getSwipyRefreshLayout().setOnLoadmoreListener(new OnLoadmoreListener() {
                @Override
                public void onLoadmore(final RefreshLayout refreshlayout) {
                    doOnBottomRefreshLogic();
                }
            });


            getSwipyRefreshLayout().setEnableLoadmore(true);
            getSwipyRefreshLayout().setEnableLoadmoreWhenContentNotFull(false);
        } else {
            getSwipyRefreshLayout().setEnableLoadmore(false);
        }

        onInitFinish();
        if (autoLoad()) {
            getSwipyRefreshLayout().autoRefresh();
        }

    }*/


    void doOnBottomRefreshLogic();

    void doOnTopRefreshLogic();
}
