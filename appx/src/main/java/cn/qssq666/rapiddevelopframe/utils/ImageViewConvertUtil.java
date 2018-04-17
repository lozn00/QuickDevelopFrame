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

package cn.qssq666.rapiddevelopframe.utils;

import android.widget.ImageView;

import cn.qssq666.rapiddevelopframe.R;


/**
 * Created by luozheng on 2016/5/31.  qssq666.cn
 * modify 2016年12月29日 10:33:30
 */

public class ImageViewConvertUtil {

    public static void clearCover(ImageView imageView, String url) {
        if (ImageViewConvertUtil.needClear(imageView, url)) {
            ImageViewConvertUtil.claerImg(imageView);
        }
        ImageViewConvertUtil.setTagUrl(imageView, url);
    }

    private static final String TAG = "ImageViewConvertUtil";

    public static String getTAGUrl(ImageView imageView) {
        Object tag = imageView.getTag(cn.qssq666.rapiddevelopframe.R.id.convertview_tag);
        if (tag != null && tag instanceof String) {
            return tag.toString();
        }
        return null;
    }

    public static void setTagUrl(ImageView imageView, String url) {
        imageView.setTag(R.id.convertview_tag, url);
    }

    public static void claerImg(ImageView imageView) {
        imageView.setImageResource(0);//白色？
    }

    /**
     * 如果复用的图片 没有设置tag或者 是 设置了但是地址不相等就清理复用的。
     *
     * @param imageView
     * @param url
     * @return
     */
    public static boolean needClear(ImageView imageView, String url) {
//        Prt.i(TAG, "是否需要清空呢?" + imageView.getTag(R.id.convertview_tag) + ",URL:" + url + ",drawable:" + imageView.getDrawable());
        if (imageView.getTag() != null) {//设置了tag说明有图片了。
            return !ImageViewConvertUtil.getTAGUrl(imageView).equals(url);
        } else if (imageView.getDrawable() != null) {
            return true;
        } else {
            return false;
        }
/*
        if (imageView.getTag() != null) {//设置了tag说明有图片了。
            return !ImageViewConvertUtil.getTAGUrl(imageView).equals(url);
        } else if (imageView.getDrawable() != null) {
            return true;
        } else {
            return false;
        }
*/

    }
}
