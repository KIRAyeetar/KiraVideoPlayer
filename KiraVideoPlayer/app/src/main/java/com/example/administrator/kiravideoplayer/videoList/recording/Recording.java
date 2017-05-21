package com.example.administrator.kiravideoplayer.videoList.recording;

/**
 * Created by Administrator on 2017/5/21.
 */

public class Recording {
    private int position;
    private long order;
    private String videoUrl;
    private String avatarUrl;
    private String introduce;
    private String time;

    public void setOrder(long order) {
        this.order = order;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public int getPosition() {
        return position;
    }

    public String getTime() {
        return time;
    }

    public long getOrder() {
        return order;
    }
}
