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

import com.android.volley.Response;

import java.util.List;

/**
 * Created by qssq on 2017/5/25 qssq666@foxmail.com
 */

public interface VolleyListResposeListener<T> extends Response.Listener<List<T>> {
    /**
     * 由于有的入口很深，需要相对处理一下
     *
     * @param json
     * @return
     */
    String onParseEntryJSON(String json);
}
