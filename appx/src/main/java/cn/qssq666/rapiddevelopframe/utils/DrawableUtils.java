package cn.qssq666.rapiddevelopframe.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.io.ByteArrayOutputStream;

/**
 * Created by luozheng on 2016/9/29.  qssq666.cn Administrator
 */

public class DrawableUtils {
    //    1.  Bitmap 转化为 byte
/*    ByteArrayOutputStream out = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
    byte[] array = out.toByteArray();

    2.byte转化为bitmap
    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

    3.bitmap转化为Drawable
    Drawable drawable = new FastBitmapDrawable(bitmap);

    4.Drawable转化为bitmap
    a.BitmapDrawable,FastBitmapDrawable直接用getBitmap
    b.其他类型的Drawable用Canvas画到一个bitmap上
    Canvas canvas = new Canvas(bitmap)
    drawable.draw(canvas)*/
    public static Bitmap byteToBitmap(byte data[]) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
//        return context.getResources().getDrawable(id);
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        byte[] array = out.toByteArray();
        return array;
//        return context.getResources().getDrawable(id);
    }

    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
//        return context.getResources().getDrawable(id);
    }
    private Bitmap drawableToBitamp1(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }


    private Bitmap drawableToBitamp(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

    /*    public static Drawable drawableToBitmap(Context context, Drawable drawable) {
            Canvas canvas = new Canvas(bitmap);
            drawable.draw(canvas);
    //        return context.getResources().getDrawable(id);
        }*/
    /*

            5.id转化graphic.drawable
     */
    public static Drawable IdToDrawable(Context context, int id) {
        return ContextCompat.getDrawable(context, id);
//        return context.getResources().getDrawable(id);
    }

    /*
        6.id转化成Bitmap
     */
    public static Bitmap IdToBitmap(Context context, int id) {

        return BitmapFactory.decodeResource(context.getResources(), id);
    }


}
