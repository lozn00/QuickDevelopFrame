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

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import cn.qssq666.rapiddevelopframe.R;
import cn.qssq666.rapiddevelopframe.global.SuperAppContext;
import cn.qssq666.rapiddevelopframe.interfaces.AdapterI;
import cn.qssq666.rapiddevelopframe.utils.AppThemeUtilsX;
import cn.qssq666.rapiddevelopframe.utils.Prt;
import cn.qssq666.rapiddevelopframe.utils.ToastUtils;


/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/2/24.
 */

public abstract class BaseRefreshWrap<ADAPTER extends RecyclerView.Adapter> implements RereshLogicI<ADAPTER> {


    public static final String TAG = "BaseRefreshWrap";

    public ADAPTER getAdapter() {
        return mAdapter;
    }

    private ADAPTER mAdapter;

    /**
     * 如果这个界面都没有更多就不需要设置。
     *
     * @return
     */
    public boolean viewHasMore() {
        return true;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private int page;

    public final boolean isLoadMore() {
        return page == 0 ? false : true;
    }

    /**
     * 执行完毕本方法后才会调用网络请求查询
     */
    abstract public void onInitFinish();

    public void onInitStart() {

    }


    abstract public RecyclerView.LayoutManager onCreateLayoutManager();

    /**
     * 此方法会多次调用
     *
     * @return
     */
    public AdapterI getRecyclervdapter() {
        return (AdapterI) mAdapter;
    }

    abstract public ADAPTER onCreateAdapter();


    abstract public String getUrl(int page);

    public void queryDataRereshAndSetProgressUi() {

        setPage(0);
        getSwipyRefreshLayout().autoRefresh(20);
//        queryData();
    }

    /**
     * 不设置进度按钮
     */
    abstract public void queryData();

    public void onNotMoreData() {
        if (viewHasMore()) {
        }
        ToastUtils.showToast(SuperAppContext.getInstance().getResources().getString(R.string.not_more_data));
    }


    public boolean autoLoad() {
        return true;
    }

    //是否能过加载更多
    public boolean enableLoadMore() {
        return true;
    }

    public boolean enableRefresh() {
        return true;
    }


    abstract public RecyclerView getRecyclerView();

    abstract public SmartRefreshLayout getSwipyRefreshLayout();


    public BaseRefreshWrap<ADAPTER> init() {
        onInitStart();
        getRecyclerView().setLayoutManager(onCreateLayoutManager());
//        getRecyclerView().setHasFixedSize(true);
        mAdapter = onCreateAdapter();
        getRecyclerView().setAdapter(mAdapter);
        AppThemeUtilsX.setSwiperRefreshLayoutStyle(getSwipyRefreshLayout());

        if (enableRefresh()) {
        /*    getSwipyRefreshLayout().setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshLayout) {
                    doOnTopRefreshLogic();
                }

            });*/


            getSwipyRefreshLayout().setEnableRefresh(true);

        } else {

            getSwipyRefreshLayout().setEnableRefresh(false);
        }

        if (enableLoadMore()) {
            getSwipyRefreshLayout().setEnableLoadMore(true);
        } else {
            getSwipyRefreshLayout().setEnableLoadMore(false);

        }

        getSwipyRefreshLayout().setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
                Prt.w(TAG, "onHeaderPulling");
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int extendHeight) {
                Prt.w(TAG, "onHeaderReleased");
            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
                Prt.w(TAG, "onHeaderReleasing");
            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int extendHeight) {
                Prt.w(TAG, "onHeaderStartAnimator");
            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
                Prt.w(TAG, "onHeaderFinish");

                BaseRefreshWrap.this.onHeaderFinish();
            }

            @Override
            public void onFooterPulling(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {
                Prt.w(TAG, "onFooterPulling");
            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int extendHeight) {
                Prt.w(TAG, "onFooterReleased");
            }

            @Override
            public void onFooterReleasing(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {
                Prt.w(TAG, "onFooterReleasing");
            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int extendHeight) {
                Prt.w(TAG, "onFooterStartAnimator");
            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {
                Prt.w(TAG, "onFooterFinish");
            }

            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                doOnBottomRefreshLogic();
                Prt.w(TAG, "onLoadMore");
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                doOnTopRefreshLogic();
                Prt.w(TAG, "onRefresh");
            }

            @Override
            public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
                Prt.w(TAG, "onStateChanged");
            }
        });


/*        if (enableLoadMore()) {
            getSwipyRefreshLayout().setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshLayout) {
                    doOnBottomRefreshLogic();
                }
            });


//            getSwipyRefreshLayout().setEnableLoadmore(true);
        } else {
//            getSwipyRefreshLayout().setEnableLoadmore(false);
        }*/
//        getSwipyRefreshLayout().setEnableAutoLoadmore(true);//到底部自动加载更多
        onPreInitFinish();
        onInitFinish();
        if (autoLoad()) {
            getSwipyRefreshLayout().autoRefresh();
        }
        return this;

    }

    protected void onHeaderFinish() {

    }


    public void doOnBottomRefreshLogic() {
        page++;
        queryData();
    }

    public void doOnTopRefreshLogic() {
        page = 0;
        queryData();
    }

    public void onPreInitFinish() {
    }
}
