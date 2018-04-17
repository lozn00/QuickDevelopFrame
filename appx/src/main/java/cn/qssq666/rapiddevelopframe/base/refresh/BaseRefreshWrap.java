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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import cn.qssq666.rapiddevelopframe.R;
import cn.qssq666.rapiddevelopframe.global.SuperAppContext;
import cn.qssq666.rapiddevelopframe.interfaces.AdapterI;
import cn.qssq666.rapiddevelopframe.utils.AppThemeUtilsX;
import cn.qssq666.rapiddevelopframe.utils.ToastUtils;


/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/2/24.
 */

public abstract class BaseRefreshWrap<ADAPTER extends RecyclerView.Adapter> implements RereshLogicI<ADAPTER> {


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
            getSwipyRefreshLayout().setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshLayout) {
                    doOnTopRefreshLogic();
                }
            });

            getSwipyRefreshLayout().setEnableRefresh(true);

        } else {
            getSwipyRefreshLayout().setEnableRefresh(false);
        }


        if (enableLoadMore()) {
            getSwipyRefreshLayout().setOnLoadmoreListener(new OnLoadmoreListener() {
                @Override
                public void onLoadmore(final RefreshLayout refreshlayout) {
                    doOnBottomRefreshLogic();
                }
            });


            getSwipyRefreshLayout().setEnableLoadmore(true);
//            getSwipyRefreshLayout().setEnableLoadmoreWhenContentNotFull(false);//没有数据的时候不允许加载共读
        } else {
            getSwipyRefreshLayout().setEnableLoadmore(false);
        }
        getSwipyRefreshLayout().setEnableAutoLoadmore(true);//到底部自动加载更多
        onPreInitFinish();
        onInitFinish();
        if (autoLoad()) {
            getSwipyRefreshLayout().autoRefresh();
        }
        return this;

    }

    public static  void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right);
//                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
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
