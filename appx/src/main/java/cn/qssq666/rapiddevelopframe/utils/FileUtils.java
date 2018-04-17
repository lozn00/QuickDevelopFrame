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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by luozheng on 15/12/25.
 */
public class FileUtils {
    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * /**
     *
     * @param object      object or activity 传递错误将抛出异常
     * @param requestCode requestcode 将 会调用
     */
    public static void chooVideoFile(Object object, int requestCode) {
        chooseFile(object, requestCode, "video/*;");
    }

    public static void chooMediaFile(Object object, int requestCode) {
        chooseFile(object, requestCode, "video/*;image/*;audio/*");
    }

    public static void chooseAnyFile(Object object, int requestCode) {
        chooseFile(object, requestCode, "*/*");
    }

    public static boolean checkAudioFormat(String path) {
        return checkFormat(path, audioFormat);

    }

    public static boolean checkVideoFormat(String path) {
        return checkFormat(path, videoFormat);

    }
    public static boolean checkPhotoFormat(String path) {
        return checkFormat(path, photoFormat);

    }

    public static boolean checkFormat(String path, String[] format) {
        int value;
        if (path == null || (value = path.lastIndexOf(".")) == -1) {

            return false;
        }
        String end = path.substring(value+1, path.length());
        for (String s : format) {
            if (s.equalsIgnoreCase(end)) {
                return true;
            }
        }
        return false;
    }

    /*
    ：mp3 wav mp3pro ASF AAC VQF.而在音乐格式中用的最多的是mp3 wav wma格
    ：mp3 wav mp3pro ASF AAC VQF.而在音乐格式中用的最多的是mp3 wav wma格
     */
    public static final String[] audioFormat = new String[]{"mp3", "wav", "asf", "aac", "vqf", "amr", "mp3pro","mov","ogg"};
    public static final String[] videoFormat = new String[]{"mp4", "3gp", "wmv", "avi", "rm", "rmvb", "mkv", "flv"};
    public static final String[] photoFormat = new String[]{"jpg","png","gif","bmp"};
    /*

电脑里播放的一般为     rm\rmvb\wmv\avi\mp4\3gp\mkv
电视一般支持的格式为   rm\rmvb\avi
手机一般支持的格式为   rm\rmvb\avi\mp4\3gp
而mkv则一般用于高清影片，一般一个影片文件大概4G左右。
其他答案（共 1个回答）

常用的有rmvb rm flv mp4 avi

     */

    /**
     * 搞不懂为什么无法进行选择
     *
     * @param object
     * @param requestCode
     */
    public static void chooseMusicFile(Object object, int requestCode) {
        chooseFile(object, requestCode, "audio/*");
    }

    public static void chooseFile(Object object, int requestCode, String type) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            if (object instanceof android.support.v4.app.Fragment) {
                ((android.support.v4.app.Fragment) object).startActivityForResult(Intent.createChooser(intent, "请选择文件"), requestCode);
            }
            if (object instanceof android.app.Fragment) {
                ((android.app.Fragment) object).startActivityForResult(Intent.createChooser(intent, "请选择文件"), requestCode);
            } else if (object instanceof Activity) {
                ((Activity) object).startActivityForResult(Intent.createChooser(intent, "请选择文件"), requestCode);
            } else {
                throw new RuntimeException("参数必须为fragment 或者activity");
            }

        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with MyInputConnectionWrapper Dialog
            ToastUtils.showToast("无法选择此文件,文件管理器未安装");
        }
    }

    public static File byte2File(byte[] bytes, File file) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            Prt.e("fileUtil", "byte2File: Exception!");
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return file;
    }
/** */
    /**
     * 读取源文件内容
     *
     * @param filename String 文件路径
     * @return byte[] 文件内容
     * @throws IOException
     */


    public static byte[] readFile(String filename) throws IOException


    {

        File file = new File(filename);
        if (filename == null || filename.equals("")) {
            throw new NullPointerException("无效的文件路径");
        }
        long len = file.length();
        byte[] bytes = new byte[(int) len];

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        int r = bufferedInputStream.read(bytes);
        if (r != len)
            throw new IOException("读取文件不正确");
        bufferedInputStream.close();

        return bytes;

    }

/** */
    /**
     * 将数据写入文件
     *
     * @param data byte[]
     * @throws IOException
     */
    public static void writeFile(byte[] data, String filename) throws IOException

    {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        bufferedOutputStream.write(data);
        bufferedOutputStream.close();

    }

/** */
    /**
     * 从jar文件里读取class
     *
     * @param filename String
     * @return byte[]
     * @throws IOException
     */
    public byte[] readFileJar(String filename) throws IOException


    {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(getClass().getResource(filename).openStream());
        int len = bufferedInputStream.available();
        byte[] bytes = new byte[len];
        int r = bufferedInputStream.read(bytes);
        if (len != r) {
            bytes = null;
            throw new IOException("读取文件不正确");
        }
        bufferedInputStream.close();
        return bytes;
    }

/** */
    /**
     * 读取网络流，为了防止中文的问题，在读取过程中没有进行编码转换，而且采取了动态的byte[]的方式获得所有的byte返回
     *
     * @param bufferedInputStream BufferedInputStream
     * @return byte[]
     * @throws IOException
     */
    public byte[] readUrlStream(BufferedInputStream bufferedInputStream) throws IOException


    {
        byte[] bytes = new byte[100];
        byte[] bytecount = null;
        int n = 0;
        int ilength = 0;
        while ((n = bufferedInputStream.read(bytes)) >= 0) {
            if (bytecount != null)
                ilength = bytecount.length;
            byte[] tempbyte = new byte[ilength + n];
            if (bytecount != null) {
                System.arraycopy(bytecount, 0, tempbyte, 0, ilength);
            }

            System.arraycopy(bytes, 0, tempbyte, ilength, n);
            bytecount = tempbyte;

            if (n < bytes.length)
                break;
        }
        return bytecount;
    }

    public static long readSDCardAvailableBlocks() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            return getFolderAvailableSpace(sdcardDir.getPath());
/*            Prt.d("", "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB");
            Prt.d("", "可用的block数目：:"+ availCount+",剩余空间:"+ availCount*blockSize/1024+"KB");*/
        } else {
            return -1;
        }
    }

    public static long getFolderAvailableSpace(String path) {
        StatFs sf = new StatFs(path);
        long blockSize;
//        long blockCount;
        long availCount;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = sf.getBlockSizeLong();
//            blockCount = sf.getBlockCountLong();
            availCount = sf.getAvailableBlocksLong();
        } else {
            blockSize = sf.getBlockSize();
//            blockCount = sf.getBlockCount();
            availCount = sf.getAvailableBlocks();
        }
        return availCount * blockSize / 1024;
    }

    public static long readSystemAvailableBlocks() {
        File dataDirectory = Environment.getDataDirectory();
        return getFolderAvailableSpace(dataDirectory.getPath());
/*            Prt.d("", "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB");
            Prt.d("", "可用的block数目：:"+ availCount+",剩余空间:"+ availCount*blockSize/1024+"KB");*/
    }
/*        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();*/
   /*     Prt.d("", "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB");
        Prt.d("", "可用的block数目：:" + availCount + ",可用大小:" + availCount * blockSize / 1024 + "KB");*/

    /**
     * 获取某一个文件夹的空间大小
     *
     * @param path
     * @return
     */
    public static long getFolderSpace(String path) {
        StatFs sf = new StatFs(path);
        long blockSize;
//        long blockCount;
//        long availCount;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = sf.getBlockSizeLong();
//            blockCount = sf.getBlockCountLong();
//            availCount = sf.getAvailableBlocksLong();
        } else {
            blockSize = sf.getBlockSize();
//            blockCount = sf.getBlockCount();
//            availCount = sf.getAvailableBlocks();
        }
        return blockSize / 1024;
    }

    /**
     * 获取指定文件夹的大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSizes(File f) {
        long size = 0;
        File flist[] = f.listFiles();//文件夹目录下的所有文件
        if (flist == null) {//4.2的模拟器空指针。
            return 0;
        }
        if (flist != null) {
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {//判断是否父目录下还有子目录
                    size = size + getFileSizes(flist[i]);
                } else {
                    size = size + getFileSize(flist[i]);
                }
            }
        }
        return size;
    }

    /*
    * 根据路径获得，某个文件或文件夹所在的存储器的内存空间总大小
    *
            * @return
            */
    public static long getTotalMemorySize(String path) {

        StatFs stat = new StatFs(path);

        long blockSize = stat.getBlockSize(); // 每个block 占字节数
        long totalBlocks = stat.getBlockCount(); // block总数

        return totalBlocks * blockSize;
        /**
         * 根据路径获得，某个文件或文件夹所在的存储器的内存空间还有多少可用
         *
         * @return
         */
    }

    public static long getAvailableMemorySize(String path) {

        StatFs stat = new StatFs(path);

        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();

        return availableBlocks * blockSize;

    }

    /**
     * 获取指定文件的大小
     *
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) {

        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);//使用FileInputStream读入file的数据流
                size = fis.available();//文件的大小
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
        }
        return size;
    }

    /**

     * 外部存储是否可用 (存在且具有读写权限)

     * @return

     */


    /**
     * 外部存储是否可用 (存在且具有读写权限)
     *
     * @return
     */
    static public boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取手机内部可用空间大小
     *
     * @return
     */
    static public long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }


    /**
     * 获取手机内部空间大小
     *
     * @return
     */
    static public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();//Gets the Android data directory
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();      //每个block 占字节数
        long totalBlocks = stat.getBlockCount();   //block总数
        return totalBlocks * blockSize;
    }

    /**

     * 获取手机外部可用空间大小

     * @return

     */


    /**
     * 获取手机外部可用空间大小
     *
     * @return
     */
    static public long getAvailableExternalMemorySize() {
        if (isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory();//获取SDCard根目录
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }

    /**
     * 获取手机外部总空间大小
     *
     * @return
     */
    static public long getTotalExternalMemorySize() {
        if (isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory(); //获取SDCard根目录
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return -1;
        }
    }
}
