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

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONException;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import cn.qssq666.rapiddevelopframe.BuildConfig;
import cn.qssq666.rapiddevelopframe.R;
import cn.qssq666.rapiddevelopframe.databinding.ViewDataEmptyBinding;
import cn.qssq666.rapiddevelopframe.global.SuperAppContext;
import cn.qssq666.rapiddevelopframe.https.GenericParseResposeListener;
import cn.qssq666.rapiddevelopframe.https.HttpUtil;
import cn.qssq666.rapiddevelopframe.https.httperror.LocalCacheAvaliableException;
import cn.qssq666.rapiddevelopframe.test.TestUtils;
import cn.qssq666.rapiddevelopframe.utils.Prt;
import cn.qssq666.rapiddevelopframe.utils.ToastUtils;
import cz.msebera.android.httpclient.HttpException;

/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/2/24.
 */

public abstract class JSONRefreshRefreshWrap<ADAPTER extends RecyclerView.Adapter> extends BaseRefreshWrap<ADAPTER> {

    public JSONRefreshRefreshWrap() {
    }


    public ViewDataEmptyBinding getEmptyViewBinding() {
        return emptyViewBinding;
    }

    private ViewDataEmptyBinding emptyViewBinding;

    public void onParseSucc(List list) {

    }


    @Override
    public JSONRefreshRefreshWrap<ADAPTER> init() {
        super.init();
        return this;
    }

    GenericParseResposeListener genericParseResposeListener = new GenericParseResposeListener<List>() {
        @Override
        public void onError(Exception error) {
            onParseError(error);
            if (BuildConfig.DEBUG) {
                Prt.w(TAG, "Net Error:" + Log.getStackTraceString(error));
            }
            if (isLoadMore()) {
                getSwipyRefreshLayout().finishLoadmore();

            } else {
                if (needEmptyView()) {
                    getSwipyRefreshLayout().finishRefresh(0, false);
                    //TOBO 点击重新加载过快后会出现不回调的情况， 当允许显示空视图加重新加载的按钮，点击重新加载后马上又回调了，那么这个空视图view被我马上显示了
                    //TODO BUG 而此时动画是没有完毕导致？已经完毕了动画还没有结束，不允许重新动画，否则用户手残马上又点击重新加载按钮，那么动画如果还没结束，手动调用重新设置刷新状态   getSwipyRefreshLayout().autoRefresh();操作刷新操作是无效的，。
                } else {
                    getSwipyRefreshLayout().finishRefresh(false);

                }
                if (error instanceof JSONException || error instanceof ParseError) {

                    onEmptyData(SuperAppContext.getInstance().getString(R.string.server_data_parse_error, error.getMessage()));
                } else if (error instanceof NetworkError) {
                    onEmptyData("网络错误");
                } else if (error instanceof LocalCacheAvaliableException) {
                    onEmptyData("本地缓存不可用");
                }  else if (error instanceof HttpException) {

                    onEmptyData("网络错误 " + error.getMessage());
                } else {

                    onEmptyData(SuperAppContext.getInstance().getString(R.string.net_error));
                }
            }
        }



        @Override
        public void onSucc(List result) {
            onParseSucc(result);

            if (isLoadMore()) {
                getSwipyRefreshLayout().finishLoadMore();
                if (result == null || result.size() == 0) {
                    getSwipyRefreshLayout().setNoMoreData(true);//没有更多数据了。
                    onNotMoreData();
                    return;
                }
                getRecyclervdapter().appendModels(result);
            } else {
             /*   if (getPage() == 0 && getSwipyRefreshLayout().isLoadmoreFinished()) {
                    getSwipyRefreshLayout().setNoMoreData(false);
                }*/
                getSwipyRefreshLayout().finishRefresh();
                if (result == null || result.size() == 0) {
                    onEmptyData(null);
                    return;
                }


                if (viewHasMore()) {

                }
                getRecyclervdapter().setData(result);

            }
            onChangeData();
            getRecyclervdapter().notifyDataSetChanged();
        }

        /**
         * 后台执行
         * @param json
         * @return
         */
        @Override
        public List onBackgroundParse(String json) {

            return parseJsonResult(json);
        }
    };

    protected void onParseError(Exception error) {

    }

    public void onChangeData() {

    }

    public  String getInterceptEmptyDataTip(){
        return "数据为空";
    }
    public boolean isInterceptEmptyData(){
        return false;
    }
    protected void onEmptyData(Object object) {

        if (needEmptyView()) {

            if(isInterceptEmptyData()){
                emptyViewBinding.tvReason.setText(getInterceptEmptyDataTip());
            }else{
                if (object == null) {
                    emptyViewBinding.tvReason.setText(getEmptyDataTip());
                } else if (object instanceof String) {
                    emptyViewBinding.tvReason.setText(object + "");
                } else if (object instanceof Exception) {
                    emptyViewBinding.tvReason.setText(SuperAppContext.getInstance().getString(R.string.data_load_fail_new_error));

                }
            }

            emptyViewBinding.getRoot().setVisibility(View.VISIBLE);
        }
    }

    public void queryData() {
        ensureEmptyViewHide();
        String url = getUrl(getPage());
        if (BuildConfig.DEBUG && TextUtils.isEmpty(url)) {


            Type types = getAdapter().getClass().getGenericSuperclass();

            Type[] genericType = ((ParameterizedType) types).getActualTypeArguments();

            if (genericType != null && genericType.length > 0) {//模拟数据。
                Class classs = (Class) genericType[0];
                List<Object> modeListAndFillField = TestUtils.createModeListAndFillField(classs, 15);
                genericParseResposeListener.onSucc(modeListAndFillField);
            } else {
                throw new RuntimeException("无法模拟填充数据,适配器不支持 " + getAdapter().getClass().getName() + "没有添加泛型模型");
            }

        } else {
            HttpUtil.genericParseRequest(SuperAppContext.getInstance(), url, getRequestHead(), genericParseResposeListener);

        }

    }

    private void ensureEmptyViewHide() {
        if (needEmptyView() && emptyViewBinding != null && emptyViewBinding.getRoot().getVisibility() == View.VISIBLE) {
            emptyViewBinding.getRoot().setVisibility(View.GONE);

        }
    }

    @Override
    public void queryDataRereshAndSetProgressUi() {
        ensureEmptyViewHide();
        super.queryDataRereshAndSetProgressUi();
    }

    public void onNotMoreData() {
        if (viewHasMore()) {
//            getSwipyRefreshLayout().setDirection(SwipyRefreshLayoutDirection.TOP);

        }

        ToastUtils.showToast(SuperAppContext.getInstance().getString(R.string.not_more_data));
    }

    public boolean autoLoad() {
        return true;
    }

    /**
     * 子线程
     *
     * @param json
     * @return
     */
    abstract protected List parseJsonResult(String json);


    abstract public RecyclerView getRecyclerView();

    abstract public SmartRefreshLayout getSwipyRefreshLayout();


    public void onPreInitFinish() {

        if (needEmptyView()) {
            emptyViewBinding = DataBindingUtil.inflate(LayoutInflater.from(getSwipyRefreshLayout().getContext()), R.layout.view_data_empty, (ViewGroup) getSwipyRefreshLayout().getParent(), false);
            View root = emptyViewBinding.getRoot();
            emptyViewBinding.btnReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emptyViewBinding.getRoot().setVisibility(View.GONE);
//                    getSwipyRefreshLayout().setVisibility(View.VISIBLE);
                    getSwipyRefreshLayout().autoRefresh();
                }
            });
            emptyViewBinding.getRoot().setVisibility(View.GONE);
            ViewGroup parent = (ViewGroup) getSwipyRefreshLayout().getParent();
            int index = parent.indexOfChild(getSwipyRefreshLayout());
            parent.addView(emptyViewBinding.getRoot(), index + 1);
        }

    }

    protected boolean needEmptyView() {
        return true;
    }


    public HashMap<String, String> getRequestHead() {
        return null;
    }

    public String getEmptyDataTip() {
        return SuperAppContext.getInstance().getString(R.string.data_load_fail_is_empty);
    }
}
