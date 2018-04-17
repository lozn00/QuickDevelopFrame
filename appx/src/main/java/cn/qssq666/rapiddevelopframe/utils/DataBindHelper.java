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

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.Date;

import cn.qssq666.rapiddevelopframe.R;
import cn.qssq666.rapiddevelopframe.viewholder.ViewDataEmptyBinding;

public class DataBindHelper {
    public final static String TAG = "DataBindHelper";

    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);

    }

    /**
     * 如果不覆盖官方的不需要加上android:
     *
     * @param imageView
     * @param url
     */
    @BindingAdapter({"image"})
    public static void imageLoader(final ImageView imageView, String url) {
        ImageViewConvertUtil.clearCover(imageView, url);
        ImageLoader.getInstance().displayImage(url, imageView, ImageUtil.getOptions());
    }


    @BindingAdapter({"drawableid"})
    public static void setImageResource(final ImageView imageView, @DrawableRes int resourcesId) {
        imageView.setImageResource(resourcesId);
    }


    @BindingAdapter({"emptyimage"})
    public static void imageLoaderEmpty(final ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            imageLoader(imageView, url);
        }
    }


    /**
     * 只是变回变灰色
     *
     * @param imageView
     * @param gray
     */
    @BindingAdapter({"gray"})
    public static void imageLoader(final ImageView imageView, boolean gray) {
        ImageUtil.setImageGray(imageView, gray);


    }


    @BindingAdapter({"face"})
    public static void faceBind(final ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.ico_head_default);
            return;
        }
        ImageViewConvertUtil.clearCover(imageView, url);
        ImageLoader.getInstance().displayImage(url, imageView, ImageUtil.getFaceOptions());
    }

    @BindingAdapter({"error", "url"})
    public static void imageLoaderTest(final ImageView imageView, Drawable resoucrceId, String url) {
        Prt.d(TAG, "drawable" + resoucrceId + "url:" + url);
        if (url == null) {
            imageView.setImageResource(R.drawable.ico_head_default);
        } else {
            ImageViewConvertUtil.clearCover(imageView, url);
            ImageLoader.getInstance().displayImage(url, imageView, ImageUtil.getOptions());

        }
    }


    @BindingAdapter({"error", "url"})
    public static void imageLoaderTest(final ImageView imageView, int resoucrceId, String url) {
        Prt.d(TAG, "imageLoaderTest recourse Idid" + resoucrceId + "url:" + url);
        ImageViewConvertUtil.clearCover(imageView, url);
        ImageLoader.getInstance().displayImage(url, imageView, ImageUtil.getOptions());
    }

    @BindingAdapter({"textColor"})
    public static void setTextColor(TextView textView, int textColor) {
        textView.setTextColor(textColor);
    }

    @BindingAdapter({"textColorHint"})
    public static void setHintTextColor(TextView textView, int textColor) {
        textView.setHintTextColor(textColor);
    }

    @BindingAdapter({"handleNumber"})
    public static void handleNumber(TextView textView, String number) {
//        textView.setText(NumberUtils.numberShortHandFix(number));
        String text = NumberUtils.numberShortHandFix1(number);
        String text1 = NumberUtils.numberShortHand(number);
        Prt.w(TAG, "英语读法:" + text + " chinese " + text1);//英语读法:20,191,098chinese 2019.0万
        textView.setText(text);
//        textView.setText(NumberUtils.numberShortHand(number));
    }

    /**
     * 解决预览的奇葩问题
     *
     * @param textView
     * @param text
     */
    @BindingAdapter({"text"})
    public static void setTextValue(final TextView textView, String text) {
        textView.setText(text);

    }


    /**
     * 这种写法会导致第二次进入的时候 还是使用了之前的信息。 一直走emptyValue，那么 是否是isEmpty判断错误导致的一系列问题呢？ 果然 卧槽 fuck
     *
     * @param textView
     * @param content
     * @param emptyValue
     */
    @SuppressWarnings("tip")
    @BindingAdapter({"content", "empty"})
//Error:(241) No resource identifier found for attribute 'error' in package 'com.buyao.tv' 如果 没有用@{}包括起来就会报错 这个
    public static void imageLoaderTest(final TextView textView, String content, String emptyValue) {
        if (TextUtils.isEmpty(content)) {
            textView.setText(emptyValue);
        } else {
            textView.setText(content);

        }
    }

    /**
     * app:contentLrc="@{model.content}"
     * app:emptyLrc="@{`No lyrics `}"
     *
     * @param textView
     * @param content
     * @param emptyValue
     */

    @SuppressWarnings("tip")
    @BindingAdapter({"contentLrc", "emptyLrc"})
//Error:(241) No resource identifier found for attribute 'error' in package 'com.buyao.tv' 如果 没有用@{}包括起来就会报错 这个
    public static void imageLoaderLrcHtml(final TextView textView, String content, String emptyValue) {
        if (TextUtils.isEmpty(content)) {
            textView.setText(emptyValue);
        } else {
            textView.setText(Html.fromHtml(content));

        }
    }


    @BindingAdapter({"visibility"})
    public static void setVisibility(View view, int visibility) {
        view.setVisibility(visibility);
                /*
                   android:visibility="@{TextUtils.isEmpty(bean.image)?View.GONE:View.VISIBLE}"
                 */
    }


    @BindingAdapter({"src"})
    public static void imageLoader(final ImageView imageView, int resourceId) {
        imageView.setImageResource(resourceId);
    }



    @BindingAdapter({"background"})
    public static void setBackground(final ImageView imageView, Drawable resourceId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setBackground(resourceId);
        } else {
            imageView.setBackgroundDrawable(resourceId);

        }
    }

    @BindingAdapter({"background"})


    public static void setBackground(final ImageView imageView, int resourceId) {
        imageView.setBackgroundResource(resourceId);
    }


    @BindingAdapter({"drawableLeft"})
    public static void imageLoader(final TextView textView, Drawable drawableLeft) {

        textView.setCompoundDrawablesWithIntrinsicBounds(
                drawableLeft, null, null, null);
    }


    @BindingAdapter("paddingLeft")
    public static void setPaddingLeft(View view, int padding) {
        view.setPadding(padding,
                view.getPaddingTop(),
                view.getPaddingRight(),
                view.getPaddingBottom());
    }

    @BindingAdapter({"src"})
    public static void imageLoader(final ImageView imageView, Drawable resourceId) {
        imageView.setImageDrawable(resourceId);
    }

    @BindingAdapter({"localImage"})
    public static void localLoader(final ImageView imageView, String path) {
        ImageLoader.getInstance().displayImage("file://" + path, imageView, ImageUtil.getOptions());
    }

    @BindingAdapter({"localICompressmage"})
    public static void localLoaderCompress(final ImageView imageView, String path) {
        File tempCachePictureFileName = MediaUtils.getTempCachePictureFileName();
        ImageUtil.compressBitmapFile(path, tempCachePictureFileName);
        localLoader(imageView, path);
    }


    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter("android:text")
    public static void setSrc(TextView view, int resId) {
        view.setText(resId);
    }

    @BindingConversion
    public static String convertDate(Date date) {
        /*
        用法:
          android:text="@{time}" 只要@{}变量是 这个日期类型类型的就会自动查找
         */
        java.text.SimpleDateFormat sdf = null;
        sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static void initEmptyViewBinding(ViewDataEmptyBinding emptyViewBinding, View view) {
        emptyViewBinding.setRootView(view);

    }



}
