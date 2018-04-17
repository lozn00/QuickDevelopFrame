package cn.qssq666.rapiddevelopframe.engine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import java.io.File;

import cn.qssq666.rapiddevelopframe.interfaces.INotify;
import cn.qssq666.rapiddevelopframe.utils.ImageUtil;
import cn.qssq666.rapiddevelopframe.utils.ToastUtils;

/**
 * Created by luozheng on 2016/7/12.  qssq.space
 */
public class NetPictreCompressAsyncTask extends AsyncTask<String, Void, Pair<Integer, String>> {

    private static final String TAG = "NetPictreCompressAsyncTask";
    private ProgressDialog mProgressDialog;
    private Context context;
    private INotify<String> iNotify;
    public static File localFile;

    public NetPictreCompressAsyncTask(Context context, File localFile, INotify<String> iNotify) {
        this.context = context;
        this.localFile = localFile;
        this.iNotify = iNotify;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Pair doInBackground(String... params) {
        try {
            boolean net = ImageUtil.writeFileFromNet(params[0], localFile);

            if (net) {
                if (localFile.exists()) {
                    //写进去还不行，需要进行压缩
                    Bitmap smallBitmap = ImageUtil.getSmallBitmap(localFile.getAbsolutePath());
                    if (smallBitmap != null) {
                        ImageUtil.saveBitmap(smallBitmap, localFile.getAbsolutePath());
                    } else {
                        return Pair.create(-6, null);
                    }
                } else {
                    return Pair.create(-7, null);//文件无法写入
                }
            } else {
                return Pair.create(-10, null);
            }

            return Pair.create(0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("正在初始化分享数据");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(Pair<Integer, String> pair) {
        if (pair != null) {
            if (pair.first == 0) {

            } else {
//            Toast.makeText(context, "警告:可能无法分享,小图片可能不能展示 FailStr:" + getShareCodeStr(pair.first) + " " + pair.second + ",FailCode:" + pair.first, Toast.LENGTH_LONG);A
                ToastUtils.showToast("温馨提示：压缩分享图片失败,图片将默认显示为app图标," + getShareCodeStr(pair.first));
            }
            this.iNotify.onNotify(pair.second);

        }
        mProgressDialog.dismiss();

    }

    public static String getShareCodeStr(int code) {
        switch (code) {
            case -10:
                return "网络地址或数据为空";
            case -7:
                return "文件创建失败";
            case -6:
                return "压缩图片出错";
            case -3:
                return "IO异常";
            case -4:
                return "网络地址解析异常";
            case -1:
                return "未知异常";
            case 0:
                return "没有检测到问题";
            case 404:
                return "图片地址没找到";
            case 403:
                return "访问禁止";
            case 400:
                return "无效的请求";
            case 501:
                return "用来访问本页面的 HTTP 谓词不被允许";
            case 503:
                return "服务器不可用";
        }
        return "未知错误码";
    }
}
