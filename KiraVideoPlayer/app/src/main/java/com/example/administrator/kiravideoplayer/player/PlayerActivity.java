package com.example.administrator.kiravideoplayer.player;

import android.app.Activity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.kiravideoplayer.tools.downLoad.DownLoadService;
import com.example.administrator.kiravideoplayer.R;
import com.example.administrator.kiravideoplayer.databinding.ActivityPlayerBinding;
import com.example.administrator.kiravideoplayer.videoList.ListSetter;
import com.example.administrator.kiravideoplayer.videoList.ListTools;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/20.
 */

public class PlayerActivity extends Activity  {
    ActivityPlayerBinding binding;
    private static final int UPDATE_UI = 1;

    private MediaPlayer mediaPlayer ;
    private int cachePosition = 0;
    private int position = 0;
    private int flag = 0;
    private Presenter presenter;
    private AudioManager audioManager;
    private String path;
    private String historyTime;

    private DownLoadService.DownLoadBinder downLoadBinder;
    public ServiceConnection connection = new ServiceConnection(){
        @Override
        public void onServiceDisconnected(ComponentName name){
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
           downLoadBinder = (DownLoadService.DownLoadBinder) service;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player);

        Bundle bundle=getIntent().getExtras();
        path=bundle.getString("url");
        position=bundle.getInt("position");

        binding.setUrl(path);
        binding.setButton(new ButtonOperation());

        setPlayer();
        setAudio();
        setSeekBar();
        setService();
    }

    private void setPlayer(){
        mediaPlayer = new MediaPlayer();
        presenter = new Presenter(mediaPlayer, binding.surface, path, position);
        binding.surface.getHolder().setKeepScreenOn(true);
        binding.surface.getHolder().addCallback(presenter.new SurfaceViewHolder());
        if(hasLoad(path)){
            changeDownLoadIcon();
        }
        UIHandler.sendEmptyMessage(UPDATE_UI);

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                cachePosition = i;
            }
        });
    }

    private void setAudio(){
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int streamVolume =audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int streamMaxVolume =  audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        binding.violenceSeekbar.setMax(streamMaxVolume);
        binding.violenceSeekbar.setProgress(streamVolume);
    }

    private void setSeekBar(){
        binding.violenceSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.playSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.timeNowTv.setText(updateTextViewWithTimeFormat(i));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                UIHandler.removeMessages(UPDATE_UI);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress =  seekBar.getProgress();
                mediaPlayer.seekTo(progress);
                UIHandler.sendEmptyMessage(UPDATE_UI);
            }
        });
    }

    private void setService(){
        Intent intent = new Intent(this, DownLoadService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP: {
                Log.d("PlayerActivity", "up");
                if(flag == 0){
                    flag = 1;
                    binding.menuLayout.setVisibility(View.GONE);
                } else{
                    binding.menuLayout.setVisibility(View.VISIBLE);
                    flag = 0;
                }
                break;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        historyTime = updateTextViewWithTimeFormat(mediaPlayer.getCurrentPosition())
                + " / " + updateTextViewWithTimeFormat(mediaPlayer.getDuration());
        ListTools.updateList(path, historyTime, position);
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.release();
        UIHandler.removeMessages(UPDATE_UI);
        unbindService(connection);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mediaPlayer.isPlaying()) {
            position = mediaPlayer.getCurrentPosition();
            mediaPlayer.stop();
            UIHandler.removeMessages(UPDATE_UI);
        }
        super.onPause();
    }

    private Handler UIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if( msg.what == UPDATE_UI){
                int position = mediaPlayer.getCurrentPosition();
                int totalPosition = mediaPlayer.getDuration();
                if (position == totalPosition){
                    mediaPlayer.pause();
                }
                String tol = updateTextViewWithTimeFormat(totalPosition);
                String now = updateTextViewWithTimeFormat(position);
                binding.timeNowTv.setText(now);
                binding.timeTotalTv.setText(tol);
                binding.playSeekbar.setMax(totalPosition);
                binding.playSeekbar.setProgress(position);
                binding.playSeekbar.setSecondaryProgress(cachePosition*totalPosition/100);

                UIHandler.sendEmptyMessageDelayed(UPDATE_UI,500);
            }
        }
    };

    public class ButtonOperation {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void setPre(){
            binding.pauseImg.setBackground(null);
            presenter.pre();
            if(isPlaying()){
                binding.pauseImg.setImageResource(R.drawable.pause) ;
                UIHandler.sendEmptyMessage(UPDATE_UI);
            } else{
                binding.pauseImg.setImageResource(R.drawable.play);
                UIHandler.removeMessages(UPDATE_UI);
            }
        }
        public void setNext(){
            PlayerActivity.this.finish();
            ListTools.startPlay(ListTools.getNextUrl(path), 0);
        }
        public void setLast(){
            PlayerActivity.this.finish();
            ListTools.startPlay(ListTools.getLastUrl(path), 0);
        }
        public void setStop(){
            binding.pauseImg.setImageResource(R.drawable.play);
            mediaPlayer.seekTo(0);
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        }

        public void setDownLoad(){
            downLoadBinder.startDownLoad(path);
        }

        public boolean isPlaying(){
            if (mediaPlayer.isPlaying())
                return true;
            return false;
        }
    }

    private static boolean hasLoad(String url){
        try {
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            String fileName = url.substring(url.lastIndexOf("/"));
            File file = new File(directory + fileName);
            if (file.exists()){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private String updateTextViewWithTimeFormat(int time){
        int second = time/1000;
        int hh = second/3600;
        int mm = second%3600/60;
        int ss = second%60;
        String str = null;
        if(hh!=0){
            str = String.format("%02d:%02d:%02d", hh, mm, ss);
        }else {
            str = String.format("%02d:%02d", mm, ss);
        }
        return str;
    }

    private void changeDownLoadIcon(){
        binding.download.setImageResource(R.drawable.download_success);
    }

    private void setVideoViewScale(int width, int height){
        ViewGroup.LayoutParams layoutParams = binding.videoLayout.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        binding.videoLayout.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams layoutParams1 = binding.videoLayout.getLayoutParams();
        layoutParams1.width = width;
        layoutParams1.height = height;
        binding.videoLayout.setLayoutParams(layoutParams1);
    }

}