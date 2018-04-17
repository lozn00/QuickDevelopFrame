package cn.qssq666.rapiddevelopframe.https;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by luozheng on 2016/11/7.  qssq.space
 * 默认utf-8
 */

public class UTF_8StringRequest extends StringRequest {
    public UTF_8StringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public UTF_8StringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data,parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data, Charset.defaultCharset());
        }

        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
    public static String parseCharset(Map<String, String> headers) {
        String contentType = (String)headers.get("Content-Type");
        if(contentType != null) {
            String[] params = contentType.split(";");

            for(int i = 1; i < params.length; ++i) {
                String[] pair = params[i].trim().split("=");
                if(pair.length == 2 && pair[0].equals("charset")) {
                    return pair[1];
                }
            }
        }

        return "utf-8";
//        return "ISO-8859-1";
    }
}
