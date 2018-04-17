package cn.qssq666.rapiddevelopframe.base.fragment;

import android.util.Log;

import cn.qssq666.rapiddevelopframe.global.SuperAppContext;
import cn.qssq666.rapiddevelopframe.https.HttpUtil;
import cn.qssq666.rapiddevelopframe.https.NetQuestTask;
import cn.qssq666.rapiddevelopframe.interfaces.ILoadProgress;
import cn.qssq666.rapiddevelopframe.utils.ToastUtils;


/**
 * Created by luozheng on 2016/3/25.  qssq.space
 */

abstract public class BaseNetFragment extends BaseFragment implements ILoadProgress {
    public String lastUrl = null;//上次请求的地址
    public boolean needWait = false;//上次请求的地址


    public void queryData(String url, final NetQuestTask.SimpleRequestDataListener stringSimpleRequestDataListener) {
        queryData(url, true, stringSimpleRequestDataListener);
    }

    public void queryData(String url, boolean needLoadAnim, final NetQuestTask.SimpleRequestDataListener stringSimpleRequestDataListener) {
        queryData(url, needLoadAnim, true,stringSimpleRequestDataListener);
    }

    public void queryData(String url, boolean needLoadAnim, int flag, final NetQuestTask.SimpleFlagRequestDataListener stringSimpleRequestDataListener) {
        queryData(url, flag, needLoadAnim, stringSimpleRequestDataListener);
    }

    /**
     * 请求这个方法会出现加载动画...
     *
     * @param url
     * @param flag
     * @param stringSimpleRequestDataListener
     */
    public void queryData(String url, int flag, final NetQuestTask.SimpleFlagRequestDataListener stringSimpleRequestDataListener) {
        queryData(url, flag, true, stringSimpleRequestDataListener);
    }


    /**
     * @param url                             地址
     * @param flag                            类型 会在结果也返回
     * @param needLoadAnim                    是否需要动画
     * @param stringSimpleRequestDataListener 请求成功或者失败的回调
     */
    public void queryData(String url, int flag, final boolean needLoadAnim, final NetQuestTask.SimpleFlagRequestDataListener stringSimpleRequestDataListener) {
        queryData(url, flag, needLoadAnim, true, stringSimpleRequestDataListener);
    }
/*
    public void queryData(String url, final boolean needLoadAnim, boolean enableCache, final NetQuestTask.SimpleRequestDataListener stringSimpleRequestDataListener) {
        queryData(url,  needLoadAnim, true, stringSimpleRequestDataListener);
    }*/

    public void queryData(String url, final int flag, final boolean needLoadAnim, boolean enableCache, final NetQuestTask.SimpleFlagRequestDataListener stringSimpleRequestDataListener) {
        if (needWait && lastUrl != null) {
            ToastUtils.showToast(SuperAppContext.getInstance(), "请稍等,当前任务还在请求中。。。");
            return;
        }
        lastUrl = url;
        Log.d(TAG, "地址:" + url);
        HttpUtil.queryData(SuperAppContext.getInstance(), url, new NetQuestTask.SimpleRequestDataListener() {
            @Override
            public void onSuccess(String str) {
                if (needLoadAnim) {
                    onLoadSucc();
                }
                lastUrl = null;
                stringSimpleRequestDataListener.onSuccess(flag, str);
            }

            @Override
            public void onFail(String str) {
                if (needLoadAnim) {
                    onLoadFail(null,str);
                }
                lastUrl = null;
                stringSimpleRequestDataListener.onFail(flag, str);
            }
        });
        if (needLoadAnim) {
            onLoadStart(null, null);
        }

    }

    public void queryData(String url, final boolean needLoadAnim, boolean enableCache, final NetQuestTask.SimpleRequestDataListener stringSimpleRequestDataListener) {
        if (needWait && lastUrl != null) {
            ToastUtils.showToast(getActivity(), "请稍等,当前任务还在请求中。。。");
            return;
        }
        lastUrl = url;
        Log.d(TAG, "地址:" + url);
        HttpUtil.queryData(SuperAppContext.getInstance(), url, new NetQuestTask.SimpleRequestDataListener() {
            @Override
            public void onSuccess(String str) {
                if (needLoadAnim) {
                    onLoadSucc();
                }
                lastUrl = null;
                stringSimpleRequestDataListener.onSuccess(str);
            }

            @Override
            public void onFail(String str) {
                if (needLoadAnim) {
                    onLoadFail(null,str);
                }
                lastUrl = null;
                stringSimpleRequestDataListener.onFail(str);
            }
        });
        if (needLoadAnim) {
            onLoadStart(null, null);
        }
    }

/*    public void requestPostFileupload(String url, Map<String, String> strFileds, Map<String, File> fileFileds, final ProgressCallBack callBack) {
        new HttpMultipartPost(getActivity(), strFileds, fileFileds, new IUploadProgress() {
            @Override
            public void onStart(long fleSize) {
                Log.i(TAG, "上传的文件大小:" + fleSize);
                onLoadStart();

            }

            @Override
            public void onLoading(int progress) {

            }

            @Override
            public void onSuccess(String str) {
                callBack.onSuccess(str);
                onLoadEnd();
            }

            @Override
            public void onFail(String str) {
                callBack.onFailure(str);
                onLoadEnd();
            }
        }).execute(url);
    }*/

    @Override
    public void onLoadStart(String title, String content) {
        if (getActivity() instanceof ILoadProgress) {
            ILoadProgress iLoadProgress = (ILoadProgress) getActivity();
            iLoadProgress.onLoadStart(title, content);
        } else {
            Log.e(TAG, "无法调用Activity onLoadStart");
        }
    }

    @Override
    public void onLoadFail(String str,String error) {
        if (getActivity() instanceof ILoadProgress) {
            ILoadProgress iLoadProgress = (ILoadProgress) getActivity();
            iLoadProgress.onLoadFail(str,error);
        } else {
            Log.e(TAG, "无法调用Activity onLoadFail");
        }
    }
    public void onLoadFail(String error) {
        onLoadFail(null,error);
    }

    @Override
    public void onLoadSucc() {
        if (getActivity() instanceof ILoadProgress) {
            ILoadProgress iLoadProgress = (ILoadProgress) getActivity();
            iLoadProgress.onLoadSucc();
        } else {
            Log.e(TAG, "无法调用Activity onLoadSucc");
        }
    }

    /*
    @Override
    public void onLoadEnd() {
        if (getActivity() instanceof ILoadProgress) {
            ILoadProgress iLoadProgress = (ILoadProgress) getActivity();
            iLoadProgress.ON();
        } else {
            Log.e(TAG, "无法调用Activity onLoadEnd");
        }
    }*/

    @Override
    public void onDestroy() {
        if (closeAutoCancel()) {
            NetQuestTask.cancelAllDefaultTagRequest(getActivity());
        }
        super.onDestroy();
    }

    protected boolean closeAutoCancel() {
        return true;
    }
}
