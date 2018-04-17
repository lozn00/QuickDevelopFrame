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

/**
 * Created by qssq on 2017/5/26 qssq666@foxmail.com
 */

public abstract class LeakListener implements Response.Listener<String> {
  /*  private final WeakReference<Activity> activityWeakReference;
    private final WeakReference<VolleyCallback> callbackWeakReference;

    public SListener(Activity activity, VolleyCallback callback) {
        activityWeakReference = new WeakReference<Activity>(activity);
        callbackWeakReference = new WeakReference<VolleyCallback>(callback);
    }

    @Override
    public void onResponse(String jsonObject) {
        Activity act = activityWeakReference.get();
        VolleyCallback vc = callbackWeakReference.get();
        if (act != null && vc != null) {
            LogUtil.d(TAG, act.toString() + "   " + jsonObject.toString());
            something you need to do ;
            vc.onSuccess(jsonObject);
        }
    }*/
}