package com.example.administrator.kiravideoplayer.videoList.video;

/**
 * Created by Administrator on 2017/5/19.
 */

public class Video {
    private int type;
    private String videoUrl;
    private String avatarUrl;
    private String author;
    private String introduce;
    private String time;
    private String like;
    private String hate;

    public void setHate(String hate) {
        this.hate = hate;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getType() {
        return type;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getAuthor() {
        return author;
    }

    public String getTime() {
        return time;
    }

    public String getHate() {
        return hate;
    }

    public String getLike() {
        return like;
    }
}
