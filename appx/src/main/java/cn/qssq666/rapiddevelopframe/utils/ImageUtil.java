package cn.qssq666.rapiddevelopframe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.qssq666.rapiddevelopframe.R;
import cn.qssq666.rapiddevelopframe.global.SuperAppContext;


/**
 * Created by luozheng on 15/11/3.
 */
public class ImageUtil {

    private static final String TAG = "ImageUtil";


    public static boolean saveBitmapToSDCardByJPG(String filepath, Bitmap bitmap) {
        return   saveBitmapToSDCardBy(filepath, bitmap, Bitmap.CompressFormat.JPEG, 100);
    }

    public static boolean saveBitmapToSDCardBy(String filepath, Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality) {
        try {
            FileOutputStream fos = new FileOutputStream(filepath);
            bitmap.compress(compressFormat, 100, fos);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 可以解决着色模式问题。
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2BitmapAny(Drawable drawable) {
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Byte[]转Bitmap
     */
    public static Bitmap bytes2Bitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    /**
     * Bitmap转Byte[]
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap转Drawable
     */
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    /**
     * Drawable转Bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

    /**
     * 旋转图像
     */
    public static Bitmap rotateBitmap(Bitmap bmp, int degrees) {
        if (degrees != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);
            return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }
        return bmp;
    }


    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    private static int mDesiredWidth;

    private static int mDesiredHeight;

    /**
     * 从Resources中加载图片
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置成了true,不占用内存，只获取bitmap宽高
        options.inJustDecodeBounds = true;
        // 初始化options对象
        BitmapFactory.decodeResource(res, resId, options);
        // 得到计算好的options，目标宽、目标高
        options = getBestOptions(options, reqWidth, reqHeight);
        Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
        return createScaleBitmap(src, mDesiredWidth, mDesiredHeight); // 进一步得到目标大小的缩略图
    }

    /**
     * 从SD卡上加载图片
     */
    public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options = getBestOptions(options, reqWidth, reqHeight);
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, mDesiredWidth, mDesiredHeight);
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }


    public static File compressBitmapFileByCurrentPhone(String file, File savePath) {
        Bitmap bitmap = compressBitmapFileByCurrentPhone(file);
        boolean b = saveBitmap(bitmap, savePath.getAbsolutePath());
        if (b == false) {
            Prt.w(TAG, "保存失败" + savePath.getAbsolutePath());
        }
        return savePath;
    }

    public static Bitmap compressBitmapFileByCurrentPhone(String file) {
        DisplayMetrics displayMetrics = SuperAppContext.getInstance().getResources().getDisplayMetrics();
        float hh = displayMetrics.heightPixels;//这里设置高度为800f
        float ww = displayMetrics.widthPixels;//这里设置宽度为480f
        return compressBitmapFileBy(file, hh, ww);
    }

    public static Bitmap compressBitmapFileBy(String file, float height, float width) {
        Bitmap bitmap = BitmapFactory.decodeFile(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.reset();//重置baos即清空baos

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 < 200) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出

            Prt.i(TAG, "文件比较小 不进行压缩");
//            return file;//也处理吧
        } else {

//            baos.reset();//重置baos即清空baos
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//这里压缩50%，把压缩后的数据存放到baos中

            Prt.i(TAG, "文件过大进行压缩");
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            //开始读入图片，此时把options.inJustDecodeBounds 设回true了
            newOpts.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

            int w = newOpts.outWidth;
            int h = newOpts.outHeight;
            //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
            DisplayMetrics displayMetrics = SuperAppContext.getInstance().getResources().getDisplayMetrics();
            float hh = height;//这里设置高度为800f
            float ww = width;//这里设置宽度为480f
            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be = 1;//be=1表示不缩放
            if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
                be = (int) (newOpts.outWidth / ww);
            } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
                be = (int) (newOpts.outHeight / hh);
            }
            if (be <= 0)
                be = 1;

//            be= (int) ((w/ww+h/hh)/2);
            Prt.i(TAG, "缩放比:" + be);
            newOpts.inSampleSize = be;//设置缩放比例
            newOpts.inJustDecodeBounds = false;
            newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
            //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            isBm = new ByteArrayInputStream(baos.toByteArray());
            bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        }
        return bitmap;
    }

    /**
     * 计算目标宽度，目标高度，inSampleSize
     *
     * @return BitmapFactory.Options对象
     */
    private static BitmapFactory.Options getBestOptions(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 读取图片长宽
        int actualWidth = options.outWidth;
        int actualHeight = options.outHeight;
        // Then compute the dimensions we would ideally like to decode to.
        mDesiredWidth = getResizedDimension(reqWidth, reqHeight, actualWidth, actualHeight);
        mDesiredHeight = getResizedDimension(reqHeight, reqWidth, actualHeight, actualWidth);
        // 根据现在得到计算inSampleSize
        options.inSampleSize = calculateBestInSampleSize(actualWidth, actualHeight, mDesiredWidth, mDesiredHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return options;
    }

    /**
     * Scales one side of a rectangle to fit aspect ratio. 最终得到重新测量的尺寸
     *
     * @param maxPrimary      Maximum size of the primary dimension (i.e. width for max
     *                        width), or zero to maintain aspect ratio with secondary
     *                        dimension
     * @param maxSecondary    Maximum size of the secondary dimension, or zero to maintain
     *                        aspect ratio with primary dimension
     * @param actualPrimary   Actual size of the primary dimension
     * @param actualSecondary Actual size of the secondary dimension
     */
    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary) {
        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    /**
     * Returns the largest power-of-two divisor for use in downscaling a bitmap
     * that will not result in the scaling past the desired dimensions.
     *
     * @param actualWidth   Actual width of the bitmap
     * @param actualHeight  Actual height of the bitmap
     * @param desiredWidth  Desired width of the bitmap
     * @param desiredHeight Desired height of the bitmap
     */
    // Visible for testing.
    private static int calculateBestInSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float inSampleSize = 1.0f;
        while ((inSampleSize * 2) <= ratio) {
            inSampleSize *= 2;
        }

        return (int) inSampleSize;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 通过传入的bitmap，进行压缩，得到符合标准的bitmap
     */
    private static Bitmap createScaleBitmap(Bitmap tempBitmap, int desiredWidth, int desiredHeight) {
        // If necessary, scale down to the maximal acceptable size.
        if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() > desiredHeight)) {
            // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
            Bitmap bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
            tempBitmap.recycle(); // 释放Bitmap的native像素数组
            return bitmap;
        } else {
            return tempBitmap; // 如果没有缩放，那么不回收
        }
    }

    public static DisplayImageOptions getOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
//                .showImageOnLoading(R.drawable.loadbg)
                .build();

    }

    public static DisplayImageOptions getFaceOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.ico_head_default)
                .build();
    }


    public static void initImageLoader(Context context) {

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(context);

        //Initialize ImageLoader with configuration.
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(configuration);
        }

    }


    /**
     * 压缩图片
     */
    public static String compressBitmapFile(String file, File savePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.reset();//重置baos即清空baos
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 < 200) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出

            Log.i(TAG, "文件比较小 不进行压缩");
//            return file;//也处理吧
        } else {

//            baos.reset();//重置baos即清空baos
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//这里压缩50%，把压缩后的数据存放到baos中

            Log.i(TAG, "文件过大进行压缩");
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            //开始读入图片，此时把options.inJustDecodeBounds 设回true了
            newOpts.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

            int w = newOpts.outWidth;
            int h = newOpts.outHeight;
            //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
            float hh = 960f;//这里设置高度为800f
            float ww = 540f;//这里设置宽度为480f
            //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be = 1;//be=1表示不缩放
            if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
                be = (int) (newOpts.outWidth / ww);
            } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
                be = (int) (newOpts.outHeight / hh);
            }
            if (be <= 0)
                be = 1;

//            be= (int) ((w/ww+h/hh)/2);
            Log.i(TAG, "缩放比:" + be);
            newOpts.inSampleSize = be;//设置缩放比例
            newOpts.inJustDecodeBounds = false;
            newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
            //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            isBm = new ByteArrayInputStream(baos.toByteArray());
            bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        }


        ImageUtil.saveBitmap(bitmap, savePath.getAbsolutePath());
        return savePath.getAbsolutePath();
    }

    public static Bitmap compressPicture(Activity activity, String path) {
        WindowManager manager = activity.getWindowManager();
        Display display = manager.getDefaultDisplay();
        int sWidth = display.getWidth();
        int sHeight = display.getHeight();// 得到屏幕的高度	// ,jpg
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 只绑定图片大小为什么还能加载上来
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        int scale = 1;// 缩放尺寸
        int actualHeight = options.outHeight;// 得到图片的实际高度
        int actualWidth = options.outWidth;// 得到图片的实际宽度
        int scaleHeight = actualHeight / sHeight;// 缩放的高度
        int scaleWidth = actualHeight / sWidth;// 缩放的宽度
        if (scaleHeight > 0 && scaleWidth > 0) {
            if (scaleHeight >= scaleWidth) {
                scale = scaleHeight;
            } else {
                scale = scaleWidth;
            }
        }
        options.inSampleSize = scale;  //也可用Math.max()不过老师用min
        options.inJustDecodeBounds = false;
        //Environment.getExternalStorageDirectory() + "/backup/demo.bmp"
        bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }


    public static boolean saveBitmap(Bitmap btm, String fileName, String dirPath) {
        return saveBitmap(btm, fileName, dirPath, false);
    }

    public static File createFilePath(String dirPath, String name) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(dirPath, name);
        if (file2.exists()) {
            file2.delete();
        }
        return file2;
    }


    public static boolean saveBitmap(Bitmap btm, String fileName, String dirPath, boolean isjpg) {
        try {
            OutputStream fileOutputStream = new FileOutputStream(createFilePath(dirPath, fileName));
            if (isjpg) {
                btm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            } else {
                btm.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void saveBitmapFile(Bitmap bitmap, File file) {
        try {
            OutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }


    // 把裁剪后的图片保存到sdcard上 不过要把原来的删除了
    public static boolean saveBitmap(Bitmap bittmap, String filePath) {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fis = null;
        bittmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try {
            fis = new FileOutputStream(filePath);
            fis.write(baos.toByteArray());
            fis.flush();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (null != baos) {
                    baos.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    /**
     * 注意此操作是耗时操作 应该在子线程
     *
     * @param path
     * @param file
     * @return
     * @throws Exception
     */
    public static boolean writeFileFromNet(String path, File file) throws Exception {


        // 从网络上获取图片
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        if (conn.getResponseCode() == 200) {

            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
            return true;
            // 返回一个URI对象
//            return Uri.fromFile(file);
        } else {
            Log.i(TAG, "" + conn.getResponseCode() + "statuCode");
            return false;
        }


    }

    public static Bitmap getBitmapFromFile(String filePath) {
        return BitmapFactory.decodeFile(filePath);


    }


    private Bitmap drawableToBitamp(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public static void setImageGray(ImageView imageView, boolean gray) {
//      第二种，利用ImageView的setColorFilter方法实现：

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0); // 设置饱和度
        ColorMatrixColorFilter grayColorFilter = gray ? new ColorMatrixColorFilter(cm) : null;
        imageView.setColorFilter(grayColorFilter); // 如果想
    }

    public static void saveImageToFile(String formatFile, byte[] bytes) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(formatFile);
        fileOutputStream.write(bytes, 0, bytes.length);
        closeStream(fileOutputStream);
    }

    public static byte[] readImageFormFile(String url) throws IOException {
        File file = new File(url);
        if (!file.exists()) {
            return null;
        }
        byte[] results = null;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        copyStream(bufferedInputStream, arrayOutputStream);
        results = arrayOutputStream.toByteArray();
        return results;
    }

    private Bitmap zoomBitmap(Bitmap oldBitmap, float scaledWidth, float scaledHeight) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaledWidth / ((float) oldBitmap.getWidth()), scaledHeight / ((float) oldBitmap.getHeight()));
        return Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, true);
    }


    public static byte[] readImageFromNet(String urlStr) throws IOException {

        String fileName = MD5Util.MD5Encode(urlStr, "utf-8");
        File cacheFile = MediaUtils.getTempConfirmedCacheFileName(fileName);
        byte[] bytes = null;
        if (cacheFile.exists()) {
            bytes = readImageFormFile(cacheFile.getAbsolutePath());
        }
        if (bytes == null) {
            URL url = new URL(urlStr);
            InputStream inputStream = url.openStream();
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            copyStream(inputStream, arrayOutputStream);
            closeStream(inputStream, arrayOutputStream);
            byte[] byteArray = arrayOutputStream.toByteArray();
            if (byteArray != null) {
                saveImageToFile(cacheFile.getAbsolutePath(), byteArray);
            }
            closeStream(inputStream, arrayOutputStream);// 关流
            return byteArray;

        } else {
            return bytes;
        }


    }


    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    private Bitmap cropBitmapSquare(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int cropWidth = w >= h ? h : w;// 裁切后所取的正方形区域边长
        cropWidth /= 2;
        int cropHeight = (int) (cropWidth / 1.2);
        return Bitmap.createBitmap(bitmap, w / 3, 0, cropWidth, cropHeight, null, false);
    }


    private Bitmap cropBitmap(Bitmap bitmap, int ratio, int width) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int cropWidth = w >= h ? h : w;// 裁切后所取的正方形区域边长
        cropWidth /= 2;
        int cropHeight = (int) (cropWidth / 1.2);
        return Bitmap.createBitmap(bitmap, w / 3, 0, cropWidth, cropHeight, null, false);
    }

    public Bitmap scalecenterInside(Bitmap bitmap, int w, int h) {
        return ThumbnailUtils.extractThumbnail(bitmap, w, h);
    }


    //https://stackoverflow.com/questions/3725501/how-to-crop-the-parsed-image-in-android
    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    /**
     * bytes获得bitmap
     *
     * @return
     */
    public static Bitmap getBitmapByBytes(byte[] data) {

        return getBitmapByBytes(data, 0, 0, -1);
    }

    public static Bitmap getBitmapByBytes(byte[] data, float reqWidth, float reqHeight) {
        return getBitmapByBytes(data, reqWidth, reqHeight, -1);
    }


    public static Bitmap getBitmapByBytes(byte[] data, float reqWidth, float reqHeight, int degree) {
        return getBitmapByBytes(data, reqWidth, reqHeight, degree, -1, false);

    }

    public static Bitmap getBitmapByBytes(byte[] data, int compressquality, boolean castJpg) {
        return getBitmapByBytes(data, 0, 0, -1, compressquality, castJpg);
    }

    public static Bitmap getBitmapByBytes(byte[] data, int degree, int compressquality, boolean castJpg) {
        return getBitmapByBytes(data, 0, 0, degree, compressquality, castJpg);
    }

    /**
     * @param data
     * @param reqWidth
     * @param reqHeight
     * @param degree          如果方向不对就不适用degree.
     * @param compressquality -1 表示不压缩 0 表示超级压缩
     * @param castJpg         不转换则自动
     * @return
     */
    public static Bitmap getBitmapByBytes(byte[] data, float reqWidth, float reqHeight, int degree, int compressquality, boolean castJpg) {
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        /**
         * 1）inBitmap如果设置，当加载内容时该方法将尝试重用这个位图； 2）inDensity使用像素密度来表示位图； 3）
         * inDither如果存在抖动，解码器将尝试解码图像抖动； 4）inPurgeable如果设置为true，则由此产生的位图将分配其像素，
         * 以便系统需要回收内存时可以将它们清除
         * ； 5）inInputShareable与inPurgeable一起使用，如果inPurgeable为false那该设置将被忽略
         * ，如果为true
         * ，那么它可以决定位图是否能够共享一个指向数据源的引用，或者是进行一份拷贝； 6）inJustDecodeBounds如果设置
         * ，那返回的位图将为空，但会保存数据源图像的宽度和高度； 7）inMutable如果设置，解码方法将始终返回一个可变的位图； 8）
         * inPreferQualityOverSpeed如果设置为true
         * ，解码器将尝试重建图像以获得更高质量的解码，甚至牺牲解码速度； 9）inPreferredConfig
         * 如果为非空，解码器将尝试解码成这个内部配置； 10）inSampleSize
         * 如果设置的值大于1，解码器将等比缩放图像以节约内存； 11）inScaled如果设置
         * ，当inDensity和inTargetDensity不为0
         * ，加载时该位图将被缩放，以匹配inTargetDensity，而不是依靠图形系统缩放每次将它绘 // 可以进一步压缩，但效果不好 //
         * 2^8888 #FFFFFFFF 2^4444 : #FFFF // opts.inPreferredConfig
         * =Config.ARGB_4444;
         */
        Log.i(TAG, "getBitmapByBytes");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 只是测量高宽
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        WindowManager manager = (WindowManager) SuperAppContext.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        boolean needChanageAngel = false;

        int ratio;
        if (reqWidth > 0) {
    /*        float scaleWidth =reqWidth/ options.outWidth;
            float scaleHeight = reqHeight/options.outHeight ;*/


            int width = options.outWidth;
            int height = options.outHeight;


            //调整角度

            if (width > height) {
                int temp = width;
                width = height;
                height = temp;

                needChanageAngel = true;

            }

            if (height > reqHeight || width > reqWidth) {
                //使用需要的宽高的最大值来计算比率
                final float suitedValue = reqHeight > reqWidth ? reqHeight : reqWidth;
                final float heightRatio = Math.round((float) height / (float) suitedValue);
                final float widthRatio = Math.round((float) width / (float) suitedValue);
                Prt.w(TAG, String.format(" scaleWidth:%f,scaleHeight:%f", widthRatio, heightRatio));
                ratio = (int) (heightRatio > widthRatio ? heightRatio : widthRatio);//用最大
            } else {
                ratio = 1;
            }
//            options.inSampleSize = (int) 0.5f;
            options.inSampleSize = (int) ratio;
        } else {
            options.inSampleSize = 1; //inSampleSize 表示 是原来的 2分之1
        }


        options.inInputShareable = true;
        options.inPurgeable = true;
        options.inJustDecodeBounds = false;
        // 缩放之后然后


        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);


        if (compressquality >= 0) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if (castJpg) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressquality, byteArrayOutputStream);

            } else {

                if (options.outMimeType != null) {
                    if (options.outMimeType.toUpperCase().contains("png")) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, compressquality, byteArrayOutputStream);

                    }
                    if (options.outMimeType.toUpperCase().contains("gif")) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, compressquality, byteArrayOutputStream);

                    } else if (options.outMimeType.toUpperCase().contains("jpg")) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, compressquality, byteArrayOutputStream);

                    } else {
                        bitmap.compress(Bitmap.CompressFormat.PNG, compressquality, byteArrayOutputStream);
                    }
                }

            }
            byte[] currentBytes = byteArrayOutputStream.toByteArray();
            if (currentBytes != null && bitmap != null) {
                bitmap.recycle();
                bitmap = BitmapFactory.decodeByteArray(currentBytes, 0, currentBytes.length);
            }
        }

        if (degree != -1) {


            Prt.w(TAG, "角度不正确,进行调整:" + degree);

            return rotaingImageView(degree, bitmap);
        } else if (needChanageAngel) {
            return rotaingImageView(90, bitmap);
        }


        return bitmap;

    }


    /**
     * @param srcBitmap
     * @param dwidth     原图片真是宽度
     * @param dheight
     * @param vheight    期望最大高度
     * @param vwidth     期望最大宽度约束.
     * @param isRecycled
     * @return
     */
    public static Bitmap imageCropSystem(Bitmap srcBitmap, int dwidth, int dheight, int vheight, int vwidth, boolean isRecycled) {


        float scale;
        float dx = 0, dy = 0;

        if (dwidth * vheight > vwidth * dheight) {
            scale = (float) vheight / (float) dheight;
            dx = (vwidth - dwidth * scale) * 0.5f;
        } else {
            scale = (float) vwidth / (float) dwidth;
            dy = (vheight - dheight * scale) * 0.5f;
        }

        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);

        matrix.postTranslate(Math.round(dx), Math.round(dy));
        /*
        dth must be <= bitmap.width(
         */
        Bitmap bmp = Bitmap.createBitmap(srcBitmap, 0, 0, vwidth, vheight, matrix, false);

        if (isRecycled && srcBitmap != null && !srcBitmap.equals(bmp) && !srcBitmap.isRecycled()) {
            srcBitmap.recycle();//回收原图片
        }
        return bmp;


    }

    /**
     * 按照一定的宽高比例裁剪图片
     *
     * @param bitmap     要裁剪的图片
     * @param longRatio  长边的比例
     * @param shortRatio 短边的比例
     * @param isRecycled 是否回收原图片
     * @return 裁剪后的图片
     */
    public static Bitmap imageCrop(Bitmap bitmap, int longRatio, int shortRatio, boolean isRecycled) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int retX, retY;
        int nw, nh;
        if (w > h) {
            if (h > w * shortRatio / longRatio) {
                nw = w;
                nh = w * shortRatio / longRatio;
                retX = 0;
                retY = (h - nh) / 2;
            } else {
                nw = h * longRatio / shortRatio;
                nh = h;
                retX = (w - nw) / 2;
                retY = 0;
            }
        } else {
            if (w > h * shortRatio / longRatio) {
                nh = h;
                nw = h * shortRatio / longRatio;
                retY = 0;
                retX = (w - nw) / 2;
            } else {
                nh = w * longRatio / shortRatio;
                nw = w;
                retY = (h - nh) / 2;
                retX = 0;
            }
        }
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null, false);
        if (isRecycled && bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();//回收原图片
            bitmap = null;
        }
        return bmp;
    }


    /**
     * 旋转图片
     *
     * @param angle  被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            Log.e(TAG, "无法旋转图片角度");
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static int readPictureDegree(byte[] byters) {
        int degree = 0;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byters);
            ExifInterface exifInterface = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                exifInterface = new ExifInterface(inputStream);
            }
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 按新的宽高缩放图片
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight) {
        if (bm == null) {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        if (bm != null & !bm.isRecycled()) {
            bm.recycle();
            bm = null;
        }
        return newbm;
    }


    /**
     * fuction: 设置固定的宽度，高度随之变化，使图片不会变形
     *
     * @param target   需要转化bitmap参数
     * @param newWidth 设置新的宽度
     * @return
     */
    public static Bitmap fitBitmap(Bitmap target, int newWidth) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        // float scaleHeight = ((float)newHeight) / height;
        int newHeight = (int) (scaleWidth * height);
        matrix.postScale(scaleWidth, scaleWidth);
        // Bitmap result = Bitmap.createBitmap(target,0,0,width,height,
        // matrix,true);
        Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix,
                true);
        if (target != null && !target.equals(bmp) && !target.isRecycled()) {
            target.recycle();
            target = null;
        }
        return bmp;// Bitmap.createBitmap(target, 0, 0, width, height, matrix,
        // true);
    }


    /**
     * 根据指定的宽度平铺图像
     *
     * @param width
     * @param src
     * @return
     */
    public static Bitmap createRepeater(int width, Bitmap src) {
        int count = (width + src.getWidth() - 1) / src.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idx = 0; idx < count; ++idx) {
            canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
        }
        return bitmap;
    }


    /**
     * 按最大边按一定大小缩放图片
     */
    public static Bitmap scaleImage(byte[] buffer, float size) {
        // 获取原图宽度
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        options.inInputShareable = true;
        Bitmap bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length,
                options);
        // 计算缩放比例
        float reSize = options.outWidth / size;
        if (options.outWidth < options.outHeight) {
            reSize = options.outHeight / size;
        }


        // 如果是小图则放大
        if (reSize <= 1) {
            int newWidth = 0;
            int newHeight = 0;
            if (options.outWidth > options.outHeight) {
                newWidth = (int) size;
                newHeight = options.outHeight * (int) size / options.outWidth;
            } else {
                newHeight = (int) size;
                newWidth = options.outWidth * (int) size / options.outHeight;
            }
            bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            bm = scaleImage(bm, newWidth, newHeight);
            if (bm == null) {
                Log.e(TAG, "convertToThumb, decode fail:" + null);
                return null;
            }
            return bm;
        }
        // 缩放
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) reSize;
        bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
        if (bm == null) {
            Log.e(TAG, "convertToThumb, decode fail:" + null);
            return null;
        }
        return bm;
    }


    /**
     * 从输入流复制到输出流
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024 * 8];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
    }

    /**
     * 关闭流错误被捕获 可关闭多个流
     *
     * @param closeable
     */
    public static void closeStream(Closeable... closeable) {
        try {
            for (int i = 0; i < closeable.length; i++) {
                closeable[i].close();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    public static Pair<File, Boolean> saveBmp2Gallery(File file, String title) {

        //系统相册目录
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;
        File saveFile = new File(galleryPath, file.getName());
        file.renameTo(saveFile);
        try {
            MediaStore.Images.Media.insertImage(SuperAppContext.getInstance().getContentResolver(), saveFile.getAbsolutePath(), "" + title, null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = AppUtils.parseUri(SuperAppContext.getInstance(), saveFile);
            intent.setData(uri);
            SuperAppContext.getInstance().sendBroadcast(intent);
            return Pair.create(saveFile, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Pair.create(file, false);
        }


    }

    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = AppUtils.parseUri(SuperAppContext.getInstance(), file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static Bitmap getDrawingCache(View view) {

        Bitmap bitmap = null;
        int width = view.getRight() - view.getLeft();

        int height = view.getBottom() - view.getTop();
        if (width == 0) {
            Log.e(TAG, "getDrawingCache fail width =0");
            return null;
        }
        if (height == 0) {
            Log.e(TAG, "getDrawingCache fail height =0");
            return null;
        }
        final boolean opaque = view.getDrawingCacheBackgroundColor() != 0 || view.isOpaque();
        Bitmap.Config quality;
        if (!opaque) {
            switch (view.getDrawingCacheQuality()) {
                case View.DRAWING_CACHE_QUALITY_AUTO:
                case View.DRAWING_CACHE_QUALITY_LOW:
                case View.DRAWING_CACHE_QUALITY_HIGH:
                default:
                    quality = Bitmap.Config.ARGB_8888;
                    break;
            }
        } else {
            quality = Bitmap.Config.RGB_565;
        }
        if (opaque) bitmap.setHasAlpha(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            bitmap = Bitmap.createBitmap(view.getResources().getDisplayMetrics(),
                    width, height, quality);
            bitmap.setDensity(view.getResources().getDisplayMetrics().densityDpi);
            boolean clear = view.getDrawingCacheBackgroundColor() != 0;
            Canvas canvas = new Canvas(bitmap);
            if (clear) {
                bitmap.eraseColor(view.getDrawingCacheBackgroundColor());
            }
            view.computeScroll();
            final int restoreCount = canvas.save();
            canvas.translate(-view.getScrollX(), -view.getScrollY());
            view.draw(canvas);
            canvas.restoreToCount(restoreCount);
            canvas.setBitmap(null);
            return bitmap;
        } else {
            return null;
        }
    }
}
