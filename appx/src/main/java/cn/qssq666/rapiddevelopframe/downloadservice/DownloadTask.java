package cn.qssq666.rapiddevelopframe.downloadservice;

import android.os.AsyncTask;

import java.io.IOException;

import cn.qssq666.rapiddevelopframe.downloadservice.listener.DownloadListener;

/**
 * Created by wm on 2017/11/29.
 */

public class DownloadTask extends AsyncTask<String,Integer,Integer> {
    private static final int TYPE_SUCCESS = 0;
    private static final int TYPE_FAILED = 1;
    private static final int TYPE_PAUSED = 2;
    private static final int TYPE_CANCELED = 3;
    private boolean isCanceled;
    private boolean isPaused;
    private int lastProgress;
    private DownloadListener mDownloadListenr;

    public DownloadTask(DownloadListener mDownloadListenr) {
        this.mDownloadListenr = mDownloadListenr;
    }

    @Override
    protected Integer doInBackground(String... strings) {
/*        InputStream is = null;
        RandomAccessFile randomAccessFile = null;
        File file = null;
        try {

            //创建文件
            long downloadedLength = 0;
            String downloadUrl = strings[0];
            //filename为: /yunguanjia-dev-debug.apk
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directoryPath + fileName);
            if(file.exists()){
                downloadedLength = file.length();
            }

            //判断文件总大小,如果文件长度为0则直接返回错误，如果长度相同则说明已下载，返回成功
            long contentLength = getContentLength(downloadUrl);
            if(contentLength == 0){
                return TYPE_FAILED;
            }else if(contentLength == downloadedLength){
                return TYPE_SUCCESS;
            }

            //开始下载
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .header("RANGE","bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if(response != null){
                is = response.body().byteStream();
                randomAccessFile = new RandomAccessFile(file,"rw");
                randomAccessFile.seek(downloadedLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len=is.read(b)) != -1){
                    if(isCanceled){
                        return TYPE_CANCELED;
                    }else if(isPaused){
                        return TYPE_PAUSED;
                    }else{
                        total += len;
                    }
                    randomAccessFile.write(b,0,len);
                    int progress = (int) ((total + downloadedLength) *100/contentLength);
                    publishProgress(progress);
                }
                response.body().close();
                return TYPE_SUCCESS;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null){
                is.close();}

                if(randomAccessFile !=null){
                    randomAccessFile.close();
                }

                if(isCanceled && file != null){
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        //不能放在finally里面。finally里面代码每次都执行
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if(progress > lastProgress){
            mDownloadListenr.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
       switch (integer){
           case TYPE_SUCCESS:
               mDownloadListenr.onSuccess();
               break;
           case TYPE_PAUSED:
               mDownloadListenr.onPaused();
               break;
           case TYPE_CANCELED:
               mDownloadListenr.onCancel();
               break;
           case TYPE_FAILED:
               mDownloadListenr.onFailed();
               break;
           default:
               break;

       }
    }

    /**
     * 获取要下载文件的总大小
     * @param downloadUrl
     * @return
     */
    private long getContentLength(String downloadUrl) throws IOException {
  /*      OkHttpClient okHttpClient =new OkHttpClient();
        Request request =new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if(response != null && response.isSuccessful()){
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;*/
  return 0;
    }

    public void pauseDownload(){
        isPaused = true;
    }

    public void cancelDownload(){
        isCanceled = true;
    }
}
