package com.example.administrator.kiravideoplayer.videoList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;

import com.example.administrator.kiravideoplayer.RecordingActivity;
import com.example.administrator.kiravideoplayer.databinding.ActivityRecordingBinding;
import com.example.administrator.kiravideoplayer.tools.another.InternetTools;
import com.example.administrator.kiravideoplayer.player.PlayerActivity;
import com.example.administrator.kiravideoplayer.videoList.recording.Recording;
import com.example.administrator.kiravideoplayer.videoList.recording.Recording_RC_Adapter;
import com.example.administrator.kiravideoplayer.videoList.video.Video;
import com.example.administrator.kiravideoplayer.videoList.video.Video_RC_Adapter;
import com.example.administrator.kiravideoplayer.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/19.
 */

public class ListSetter {
    public static ArrayList<Recording> recordingList= new ArrayList<Recording>();;
    public static ArrayList<Video> list = new ArrayList();
    private final int LOAD_FINISHED = 1;
    private static Context context;
    private ActivityMainBinding mBinding;
    private ActivityRecordingBinding rBinding;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==LOAD_FINISHED){
                setVideo_RC_View(list);
            }
        }
    };

    public ListSetter (Context context, ActivityMainBinding binding){
        this.context = context;
        this.mBinding = binding;
    }
    public ListSetter (Context context, ActivityRecordingBinding binding){
        this.context = context;
        this.rBinding = binding;
    }

    public static Context getContext(){
        return context;
    }

    public void setVideo_RC_View(ArrayList<Video> list){
        Video_RC_Adapter adapter = new Video_RC_Adapter(context, list);
        mBinding.rcView.setLayoutManager(new LinearLayoutManager(context));
        mBinding.rcView.setAdapter(adapter);
    }

    public void setRecording_RC_View(ArrayList<Recording> list){
        Recording_RC_Adapter adapter = new Recording_RC_Adapter(context, list);
        rBinding.rcView.setLayoutManager(new LinearLayoutManager(context));
        rBinding.rcView.setAdapter(adapter);
    }

    public void getListByUrl(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String responseData = InternetTools.getJson(url);
                try {
                    JSONArray jsonArray = new JSONArray("["+responseData+"]");
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        responseData = jsonObject.getString("showapi_res_body");
                    }
                    jsonArray = new JSONArray("["+responseData+"]");
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        responseData = jsonObject.getString("pagebean");
                    }
                    jsonArray = new JSONArray("["+responseData+"]");
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        responseData = jsonObject.getString("contentlist");
                    }
                    jsonArray = new JSONArray(responseData);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Video video = new Video();
                        if(isVideo(jsonObject.getInt("type")) ){
                            if(jsonObject.getString("text") != null){
                                video.setIntroduce(jsonObject.getString("text"));
                            }
                            if(jsonObject.getString("profile_image") != null){
                                video.setAvatarUrl(jsonObject.getString("profile_image"));
                            }
                            if(jsonObject.getString("video_uri") != null){
                                video.setVideoUrl(jsonObject.getString("video_uri"));
                            }
                            if(jsonObject.getString("name") != null){
                                video.setAuthor(jsonObject.getString("name"));
                            }
                            if(jsonObject.getString("create_time") != null){
                                video.setTime(jsonObject.getString("create_time"));
                            }
                            if(jsonObject.getString("love") != null){
                                video.setLike(jsonObject.getString("love"));
                            }
                            if(jsonObject.getString("hate") != null){
                                video.setHate(jsonObject.getString("hate"));
                            }
                            list.add(video);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message=new Message();
                            message.what=LOAD_FINISHED;
                            handler.sendMessage(message);
                        }
                    }).start();
                }
            }
        }).start();
    }

    private boolean isVideo(int type){
        if(type==41)
            return true;
        return false;
    }
}
