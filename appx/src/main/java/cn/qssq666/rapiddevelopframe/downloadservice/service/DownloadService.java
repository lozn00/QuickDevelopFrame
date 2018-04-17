package cn.qssq666.rapiddevelopframe.downloadservice.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

import cn.qssq666.rapiddevelopframe.downloadservice.DownloadTask;
import cn.qssq666.rapiddevelopframe.downloadservice.listener.DownloadListener;

public class DownloadService extends Service {
    private DownloadTask mDownloadTask;
    private String mDownloadUrl;
    private DownloadBinder mDownloadBinder = new DownloadBinder();

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mDownloadBinder;
    }

    public class DownloadBinder extends Binder {

        public void startDownload(@NonNull String downloadUrl){
            if(mDownloadTask == null){
                mDownloadUrl = downloadUrl;
                mDownloadTask = new DownloadTask(new ServiceDownloadListener());
                mDownloadTask.execute(mDownloadUrl);
                startForeground(1,getNotification("下载中...",0));
            }
        }

        public void pauseDownload(){
            if(mDownloadTask != null){
                mDownloadTask.pauseDownload();
            }
        }

        /**
         * 不在下载的时候点击取消，也删除本地文件
         */
        public void cancelDownload(){
            if(mDownloadTask != null){
                mDownloadTask.cancelDownload();
            }else{
                    if(mDownloadUrl != null){
                        String fileName = mDownloadUrl.substring(mDownloadUrl.lastIndexOf("/"));
                        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                        File file = new File(directoryPath + fileName);
                        if(file.exists()){
                            file.delete();
                        }
                        getNotificationManager().cancel(1);
                        Toast.makeText(DownloadService.this, "cancelDownload", Toast.LENGTH_SHORT).show();
                    }
            }
        }

    }

    public class ServiceDownloadListener implements DownloadListener{

        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("下载中...",progress));
        }

        @Override
        public void onSuccess() {
            mDownloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载成功",-1));
            Toast.makeText(DownloadService.this, "下载成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            mDownloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载失败",-1));
            Toast.makeText(DownloadService.this, "下载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            mDownloadTask = null;
            Toast.makeText(DownloadService.this, "暂停下载", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            mDownloadTask = null;
            //关闭前台服务通知
            stopForeground(true);
            Toast.makeText(DownloadService.this, "取消下载", Toast.LENGTH_SHORT).show();
        }
    }

    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title,int progress){
//        Intent intent = new Intent(this, com.buyao.buliao.MainActivity.class);
//        PendingIntent pendingIntent =PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"Download");
        builder.setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_MAX);
//                .setContentIntent(pendingIntent);
             /*   .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_round));*/

        if(progress >= 0){
            builder.setContentText(progress + "%");
            builder.setProgress(100,progress,false);
        }
        return builder.build();
    }
}
