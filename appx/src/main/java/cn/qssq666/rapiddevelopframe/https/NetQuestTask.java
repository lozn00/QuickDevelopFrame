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

package cn.qssq666.rapiddevelopframe.https;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.qssq666.rapiddevelopframe.global.SuperAppContext;
import cn.qssq666.rapiddevelopframe.utils.Prt;
import cn.qssq666.rapiddevelopframe.utils.RegexUtils;

/**
 * File cacheDir = new File(this.getCacheDir(), volley);
 * DiskBasedCache cache = new DiskBasedCache(cacheDir);
 * mRequestQueue.start();
 * <p/>
 * // clear all volley caches.
 * mRequestQueue.add(new ClearCacheRequest(cache, null));
 * return mRequestQueue;
 * Created by luozheng on 15/12/3.
 * <p>
 * <p>
 * 2017年5月25日 15:46:27  内存泄漏 VideoMediaFragment,
 */
public class NetQuestTask<A> {
    private static final int MSG_LOCAL_CACHE_SUCC = 2;
    private static final int MSG_LOCAL_CACHE_SUCC_BEAN = 4;
    private static final int MSG_LOCAL_CACHE_FAIL = 3;
    public static int NET_TYPE = -1;//隐藏
    public static boolean hasTip = false;
    private static final int MSG_START = 4;

    public static boolean isNetError() {
        return netError;
    }

    public static boolean netError = true;
    public static RequestQueue requestQueue = null;

    public static ExecutorService getScheduledExecutorService() {
        if (scheduledExecutorService == null) {
            scheduledExecutorService = Executors.newFixedThreadPool(10);
        }
        return scheduledExecutorService;
    }

    private static ExecutorService scheduledExecutorService;

    public static RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            synchronized (NetQuestTask.class) {
                if (requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(context.getApplicationContext());//50m 1024*1024*50

                    /*equestQueue.setRetryPolicy( new DefaultRetryPolicy( 500000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数*/
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );

                    netError = !HttpUtil.judgeNetIsConnected(context);
                    registerNetChange(context);
                }
            }
        }
//        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
        return requestQueue;

    }

    public static void registerNetChange(Context context) {
        ////动态注册广播

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.getApplicationContext().registerReceiver(myNetReceiver, filter);

    }

    public static void destory(Context context) {
        if (myNetReceiver != null) {
            context.getApplicationContext().unregisterReceiver(myNetReceiver);
        }
        myNetReceiver = null;
        requestQueue = null;
        scheduledExecutorService = null;
    }

    private static BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                Prt.w(TAG, "收到了網絡變化的廣播" + netInfo);
                hasTip = false;
                if (netInfo != null) {
                    NET_TYPE = netInfo.getType();

                }
                if (netInfo != null && netInfo.isAvailable()) {
                    /////////////网络连接
                    netError = false;
                    String name = netInfo.getTypeName();

                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        /////WiFi网络

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        /////有线网络
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        /////////3g网络
                    }
                } else {
                    ////////网络断开
                    netError = true;
                }
            }

        }
    };


    public static void setDefaultRequestTAG(Request request, String tag) {
        request.setTag(tag == null ? NetQuestTask.TAG : tag);
    }

    public static boolean is3GNet() {
        return NET_TYPE == ConnectivityManager.TYPE_MOBILE;
    }

    public static final String TAG = "NetQuestTask";
    IOnRequestDataListener mOnRequestDataListener = null;


    public NetQuestTask(IOnRequestDataListener mOnRequestDataListener) {
        this.mOnRequestDataListener = mOnRequestDataListener;
    }

    public NetQuestTask() {
        setOnRequestDataListener(new IOnRequestDataListener() {
            @Override
            public void onStart(String t) {
                //可以用来谈对话框之类的。
            }

            @Override
            public void onLoadding(String t) {
                Prt.i(TAG, "onLoadding:" + t);
            }

            @Override
            public void onSuccess(String t) {
//                Prt.i(TAG, "onSuccess:" + t);
            }

            @Override
            public void onFail(String t) {
                Prt.i(TAG, "onFail:" + t);
            }
        });
    }

    //    String str = new String((getRequestQueue(context).getCache().get(url).data));
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START:
                    mOnRequestDataListener.onStart(null);
                    break;
                case MSG_LOCAL_CACHE_SUCC:
                    mOnRequestDataListener.onSuccess(msg.obj.toString());
                    break;
                case MSG_LOCAL_CACHE_FAIL:
                    mOnRequestDataListener.onFail("网络不可用且没有磁盘中没有缓存数据!");
                    break;
            }
        }
    };

    public static String getCache(Context context, String url) {
//        Cache.Entry entry = getRequestQueue(context).getCache().get(url);//版本更新了
        Cache.Entry entry = getRequestQueue(context).getCache().get(Request.Method.GET + ":" + url);//版本更新了
        if (entry != null && entry.data != null) {
            return new String(entry.data);
        }
        return null;
    }

    public void executeGet(final String url, final Context context) {
        executeGet(url, true, context);
    }

    public void executeGet(final String url, final Context context, final HashMap<String, String> hashMap) {
        if (Prt.LOGGABLE) {
            Prt.w(TAG, "URL:" + url);
        }
        if (netError) {
            handler.sendEmptyMessage(MSG_START);
            scheduledExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    String cache = getCache(context, url);
                    Prt.w(TAG, "缓存数据：" + cache);
                    if (cache == null) {
                        handler.sendEmptyMessage(MSG_LOCAL_CACHE_FAIL);
                    } else {
                        handler.obtainMessage(MSG_LOCAL_CACHE_SUCC, cache).sendToTarget();
                    }
                }
            });
            return;
        }
        StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
//                        Prt.i(TAG, "str:" + str);
                        if (TextUtils.isEmpty(str)) {
                            mOnRequestDataListener.onFail("服务器没有数据");
                            return;
                        }
                        mOnRequestDataListener.onSuccess(str);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Prt.e(TAG, "faill str:" + volleyError.toString());
                mOnRequestDataListener.onFail(doExceptionKey(volleyError.toString()));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return hashMap == null ? super.getHeaders() : hashMap;
            }
        };
        strRequest.setTag(TAG);
        getRequestQueue(context).add(strRequest);
        mOnRequestDataListener.onStart(null);
    }


    /**
     * 是否启用缓存
     *
     * @param url
     * @param enableCache
     * @param context
     */
    public void executeGet(final String url, boolean enableCache, final Context context) {

        if (Prt.LOGGABLE) {
            Prt.w(TAG, "请求url:" + url);
        }
        if (netError && enableCache) {
            handler.sendEmptyMessage(MSG_START);
            getScheduledExecutorService().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String cache = getCache(context, url);
                        Prt.w(TAG, "缓存数据：" + cache);
                        if (cache == null) {
                            handler.sendEmptyMessage(MSG_LOCAL_CACHE_FAIL);
                        } else {
                            boolean b = RegexUtils.checkJson(cache);
                            if (!b) {

                                handler.obtainMessage(MSG_LOCAL_CACHE_FAIL, "读取缓存出现非法字符,请开启网络更新缓存");
                                return;
                            }
                            handler.obtainMessage(MSG_LOCAL_CACHE_SUCC, cache).sendToTarget();
                        }
                    } catch (Exception e) {
                        handler.obtainMessage(MSG_LOCAL_CACHE_FAIL, "读取缓存出错");
                        Prt.e(TAG, "local cache error " + e.toString());
                    }
                }
            });
            return;
        }
        StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
//                        Prt.i(TAG, "str:" + str);
                        if (TextUtils.isEmpty(str)) {
                            mOnRequestDataListener.onFail("服务器没有数据");
                            return;
                        }
                        if (!RegexUtils.checkJson(str)) {
                            getRequestQueue(SuperAppContext.getInstance()).getCache().remove(url);//这样的缓存没有用 移除
                            mOnRequestDataListener.onFail("您的网络路由需要验证才能正常访问！若您已缓存数据,请关闭网络重启APP");
                        } else {
                            mOnRequestDataListener.onSuccess(str);
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                String str = null;
                if (volleyError instanceof NoConnectionError) {
                    str = "没有网络链接";
                } else if (volleyError instanceof TimeoutError) {
                    str = "请求超时";
                } else if (volleyError instanceof ParseError) {
                    str = "解析错误";
                } else if (volleyError instanceof ServerError) {
                    str = "服务器错误";
                } else if (volleyError instanceof AuthFailureError) {
                    str = "授权错误";
                } else {
                    str = doExceptionKey(volleyError.toString());
                }
                Prt.i(TAG, "faill str:" + volleyError);
                mOnRequestDataListener.onFail(str);
            }
        });
        strRequest.setTag(url);
        strRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue(context).add(strRequest);
        mOnRequestDataListener.onStart(null);
    }

    public StringRequest executeGet(final String url, boolean enableCache, String tag, final Context context) {

        if (Prt.LOGGABLE) {
            Prt.w(TAG, "请求url:" + url);
        }
        if (netError && enableCache) {
            handler.sendEmptyMessage(MSG_START);
            getScheduledExecutorService().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String cache = getCache(context, url);

                        Prt.w(TAG, "缓存数据：" + cache);
                        if (cache == null) {
                            handler.sendEmptyMessage(MSG_LOCAL_CACHE_FAIL);
                        } else {
                            handler.obtainMessage(MSG_LOCAL_CACHE_SUCC, cache).sendToTarget();
                        }
                    } catch (Exception e) {
                        handler.obtainMessage(MSG_LOCAL_CACHE_FAIL, "读取缓存出错");
                        Prt.e(TAG, "local cache error " + e.toString());
                    }
                }
            });
            return null;
        }
        StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
//                        Prt.i(TAG, "str:" + str);
                        if (TextUtils.isEmpty(str)) {
                            mOnRequestDataListener.onFail("服务器没有数据");
                            return;
                        }
                        mOnRequestDataListener.onSuccess(str);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                String str = null;
                if (volleyError instanceof NoConnectionError) {
                    str = "没有网络链接";
                } else if (volleyError instanceof TimeoutError) {
                    str = "请求超时";
                } else if (volleyError instanceof ParseError) {
                    str = "解析错误";
                } else if (volleyError instanceof ServerError) {
                    str = "服务器错误";
                } else if (volleyError instanceof AuthFailureError) {
                    str = "授权错误";
                } else {
                    str = doExceptionKey(volleyError.toString());
                }
                Prt.i(TAG, "faill str:" + volleyError);
                mOnRequestDataListener.onFail(str);
            }
        });
        strRequest.setTag(tag);
        strRequest.setShouldCache(enableCache);
        if (enableCache) {
//            File cachePath = MediaUtils.getCachePath();
//            strRequest.setCacheEntry(Cache.Entry);

        }
        strRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue(context).add(strRequest);
        mOnRequestDataListener.onStart(null);
        return strRequest;
    }

    public static String doExceptionKey(String key) {
        if (key != null) {
            if (key.contains(":") && (key.contains("http://") || key.contains("host") || key.contains("www"))) {
                return key.substring(0, key.indexOf(":"));
            } else if (key.contains("http://") || key.contains("host") || key.contains("www") || key.contains(".com") || key.contains("cn") || key.contains(".tv") || key.contains(".net")) {
                return "异常不能正常打印,包含网址 主机 www等信息,也能是网络异常";
            } else {
                return key;
            }
        }
        return null;
    }
/*
    public void executeJson(String url, final Context context) {
        Prt.i(TAG, "URL:" + url);

        StringRequest strRequest = new StringRequest(com.android.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        Prt.i(TAG, "str:" + str);
                        if (TextUtils.isEmpty(str)) {
                            mOnRequestDataListener.onFail("服务器没有数据");
                            return;
                        }

                        if (RegexUtils.checkJson(str.trim())) {
                            Prt.i(TAG, "json正则匹配成功");
                        } else {
                            Prt.e(TAG, "json正则匹配失败");
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                            alertDialog.setMessage("非法的数据,程序不能正常解析,请检查您连接的网络是否正常,比如访问网站是否需要验证,下面是返回的数据:\n" + str);
                            alertDialog.updateTitle("网络错误");
                            alertDialog.create();
                            alertDialog.setPositiveButton("确定", null);
                            alertDialog.createPlayStationAndSHow();
                            mOnRequestDataListener.onFail("");
                            return;
                        }
                        mOnRequestDataListener.onSuccess(str);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Prt.i(TAG, "faill str:" + volleyError.toString());
                mOnRequestDataListener.onFail(doExceptionKey(volleyError.toString()));
            }
        });
        strRequest.setTag(TAG);
        getRequestQueue(context).add(strRequest);
        mOnRequestDataListener.onStart(null);
    }*/


    public void setOnRequestDataListener(IOnRequestDataListener mOnRequestDataListener) {
        this.mOnRequestDataListener = mOnRequestDataListener;
    }

    public interface IOnRequestDataListener {
        void onStart(String param);

        void onLoadding(String param);

        void onSuccess(String str);

        void onFail(String str);

    }


    public interface BeanListener<T> {


        void onSuccess(List<T> str);

        void onFail(String str);

    }

    /**
     * 创建java类中类出现is not an enclosing class
     */
    static public abstract class SimpleRequestDataListener implements IOnRequestDataListener {

        @Override
        public void onStart(String param) {

        }

        @Override
        public void onLoadding(String param) {

        }

    }

    static public abstract class SimpleFlagRequestDataListener<String> {

        public void onStart(String param) {

        }

        public void onLoadding(String param) {

        }

        public abstract void onFail(int flag, String str);

        public abstract void onSuccess(int flag, String str);
    }

    public static void cancelAllDefaultTagRequest(Context context) {
        getRequestQueue(context).cancelAll(TAG);
    }

    public static void cancelRequest(String TAG) {
        getRequestQueue(SuperAppContext.getInstance()).cancelAll(TAG);
    }


}
