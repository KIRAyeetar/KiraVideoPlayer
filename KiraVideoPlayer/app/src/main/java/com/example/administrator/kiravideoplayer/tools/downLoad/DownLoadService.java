package com.example.administrator.kiravideoplayer.tools.downLoad;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.administrator.kiravideoplayer.player.PlayerActivity;
import com.example.administrator.kiravideoplayer.R;

import java.io.File;

public class DownLoadService extends Service {
    private DownLoadTask downLoadTask;
    private String downLoadUrl;

    private DownLoadListener listener = new DownLoadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("DownLoading...", progress));
        }

        @Override
        public void onSuccess() {
            downLoadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("DownLoad Success!", -1));
            Toast.makeText(DownLoadService.this, "DownLoad Success!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {

            downLoadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("DownLoad Failed!", -1));
            Toast.makeText(DownLoadService.this, "DownLoad Failed!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downLoadTask = null;
            Toast.makeText(DownLoadService.this, "DownLoad Paused", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downLoadTask = null;
            stopForeground(true);
            Toast.makeText(DownLoadService.this, "DownLoad Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    private DownLoadBinder mBinder = new DownLoadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public class DownLoadBinder extends Binder {

        public void startDownLoad(String url){
            if(downLoadTask == null){
                downLoadUrl = url;
                downLoadTask = new DownLoadTask(listener);
                downLoadTask.execute(downLoadUrl);
                startForeground(1, getNotification("DownLoading...", 0));
                Toast.makeText(DownLoadService.this, "DownLoading...", Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDownLoad(){
            if(downLoadTask != null){
                downLoadTask.pauseDownload();
            }
        }

        public void cancelDownLoad() {
            if(downLoadTask != null){
                downLoadTask.cancelDownLoad();
            }else {
                if (downLoadUrl != null){
                    String fileName = downLoadUrl.substring(downLoadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory + fileName);
                    if(file.exists()){
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownLoadService.this, "Canceled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress){
        Intent intent = new Intent(this, PlayerActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.background);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if(progress > 0){
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }
}
