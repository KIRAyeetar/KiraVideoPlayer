package com.example.administrator.kiravideoplayer.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/20.
 */

public class Presenter{
    private static SurfaceView surface;
    private static MediaPlayer mediaPlayer = null;
    private int position = 0;
    private String path;

    public Presenter (MediaPlayer Player, SurfaceView surface, String path, int position) {
        if (mediaPlayer!=null){
            mediaPlayer=null;
        }else {
            mediaPlayer =new MediaPlayer();
        }
        this.mediaPlayer = Player;
        this.surface = surface;
        this.path = path;
        this.position = position;
    }

    public void pre(){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public void play() throws IllegalArgumentException, SecurityException,
            IllegalStateException, IOException {
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            String fileName = path.substring(path.lastIndexOf("/"));
            File file = new File(directory + fileName);
            if (file.exists()) {
                mediaPlayer.setDataSource(String.valueOf(file));
            }else {
                mediaPlayer.setDataSource(path);
            }
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }try {
            mediaPlayer.prepare();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setDisplay(surface.getHolder());
                mp.start();
            }
        });
    }

    public class SurfaceViewHolder implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mediaPlayer.setDisplay(surface.getHolder());
            if (position == 0) {
                try {
                    play();
                    mediaPlayer.seekTo(position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    public static void releaseMediaPlayer(){
        if(mediaPlayer!=null){
            try {
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer=null;
            }
        }
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}