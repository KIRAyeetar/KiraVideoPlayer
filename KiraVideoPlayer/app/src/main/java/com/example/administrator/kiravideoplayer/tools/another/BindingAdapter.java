package com.example.administrator.kiravideoplayer.tools.another;

import android.view.View;
import android.widget.ImageView;

import com.example.administrator.kiravideoplayer.tools.imageTools.ImageTools;
import com.example.administrator.kiravideoplayer.videoList.ListSetter;
import com.example.administrator.kiravideoplayer.videoList.ListTools;
import com.example.administrator.kiravideoplayer.videoList.recording.Recording;
import com.example.administrator.kiravideoplayer.videoList.video.Video;

/**
 * Created by Administrator on 2017/5/17.
 */

public class BindingAdapter {

    @android.databinding.BindingAdapter({"imageUrl"})
    public static void loadImageFromUrl(ImageView view, String url){
        ImageTools.LoadImageIntoViewByURL(view, url);
    }

    @android.databinding.BindingAdapter({"videoUrl"})
    public static void loadVideoFromUrl(View view, final Video video){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListTools.startPlay(video.getVideoUrl(), 0);
                Recording recording = new Recording();
                recording.setAvatarUrl(video.getAvatarUrl());
                recording.setVideoUrl(video.getVideoUrl());
                recording.setPosition(0);
                recording.setTime(null);
                recording.setOrder(System.currentTimeMillis());
                recording.setIntroduce(video.getIntroduce().substring(2,20)+"...");
                ListTools.addRecording(recording);
            }
        });
    }

    @android.databinding.BindingAdapter({"firstImageUrl"})
    public static void loadFirstImageFromUrl(final ImageView view, final String url){
        ImageTools.LoadFirstImageFromVideo(view, url);
    }

}
