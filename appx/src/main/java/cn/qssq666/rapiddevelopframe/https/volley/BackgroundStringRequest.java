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

package cn.qssq666.rapiddevelopframe.https.volley;

import android.os.Looper;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

import cn.qssq666.rapiddevelopframe.BuildConfig;
import cn.qssq666.rapiddevelopframe.utils.Prt;

/**
 * Created by qssq on 2017/5/25 qssq666@foxmail.com
 */

public class BackgroundStringRequest<T> extends Request<T> {
    private static final String TAG = "BackgroundStringRequest";
    private BackgroundResposeListener<T> mListener;

    /**
     * Creates a new request with the given method.
     *
     * @param method        the request {@link Method} to use
     * @param url           URL to fetch the string at
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public BackgroundStringRequest(int method, String url, BackgroundResposeListener<T> listener,
                                   Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    /**
     * Creates a new GET request.
     *
     * @param url           URL to fetch the string at
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public BackgroundStringRequest(String url, BackgroundResposeListener<T> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String str;
        if (Prt.LOGGABLE) {

            if (Looper.myLooper() == Looper.getMainLooper()) {
                Prt.d(TAG, "MainThread~");
            } else {
                Prt.d(TAG, "NotMainThread");
            }
        }
        try {
            str = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

        } catch (UnsupportedEncodingException e) {
            str = new String(response.data);
        }
        try {
            T t = mListener.onBackgroundParseEntry(str);
            return Response.success(t, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            if(BuildConfig.DEBUG){
                Log.e(TAG,"解析jison错误",e);
            }
            return Response.error(new ParseError(e));

        }

    }
}
