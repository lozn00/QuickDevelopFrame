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

import com.alibaba.fastjson.JSON;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

import cn.qssq666.rapiddevelopframe.utils.Prt;

/**
 * Created by qssq on 2017/5/25 qssq666@foxmail.com
 */

public class BeanRequest<T> extends Request<T> {
    private static final String TAG = "BeanRequest";
    private final Class<T> clazz;
    private VolleyBeanResposeListener<T> listener;


    /**
     * Like the other, but allows you to specify which {@link Request.Method} you want.
     *
     * @param method
     * @param url
     * @param clazz
     * @param listener
     * @param errorListener
     */
    public BeanRequest(int method, Class<T> clazz, String url, VolleyBeanResposeListener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.listener = listener;
    }

    @Override
    protected void deliverResponse(T response) {
        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Prt.e(TAG, "数据是 ==>在主线程中解析的~");
            } else {
                Prt.e(TAG, "数据不是 ==>在主线程中解析的~");
            }
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            org.json.JSONObject jsonObject = new org.json.JSONObject(json);
            if (null != jsonObject && jsonObject.has("code") && jsonObject.getInt("code") == 200) {
                String entryJSON = listener.onParseEntryJSON(json);
                return Response.success(JSON.parseObject(entryJSON, clazz), HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.error(new ParseError());
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            Prt.e(TAG, "JsonSyntaxException ==== ");
            return Response.error(new ParseError(e));
        }
    }

 /*   @Override
    public void finish(final String tag) {
        listener = null;
    }*/
}