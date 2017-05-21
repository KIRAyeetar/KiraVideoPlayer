package com.example.administrator.kiravideoplayer.videoList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.administrator.kiravideoplayer.RecordingActivity;
import com.example.administrator.kiravideoplayer.player.PlayerActivity;
import com.example.administrator.kiravideoplayer.videoList.recording.Recording;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/21.
 */

public class ListTools {


    public static void startPlay(String url, int position){
        Intent intent=new Intent(ListSetter.getContext(), PlayerActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        bundle.putInt("position",position);
        intent.putExtras(bundle);
        ListSetter.getContext().startActivity(intent);
    }

    public static void openRecording(){
        Intent intent=new Intent(ListSetter.getContext(), RecordingActivity.class);
        ListSetter.getContext().startActivity(intent);
    }

    public static void createRecordings(){
        ListSetter.recordingList = new ArrayList<Recording>();
    }

    public static void addRecording(Recording recording){
        int flag = 0;
        if(ListSetter.recordingList == null){
            createRecordings();
        }
        for(int i=0; i<ListSetter.recordingList.size(); i++){
            if(ListSetter.recordingList.get(i).getVideoUrl() == recording.getVideoUrl()){
                ListSetter.recordingList.set(i, recording);
                flag = 1;
                break;
            }
        }
        if (flag == 0){
            ListSetter.recordingList.add(recording);
        }
        sortRecordingList();
    }

    public static void sortRecordingList(){
        for (int i = 0;i<ListSetter.recordingList.size()-1;i++){
            for(int j =1; j<ListSetter.recordingList.size(); j++){
                if(ListSetter.recordingList.get(i).getOrder()<ListSetter.recordingList.get(j).getOrder()){
                    Recording temp = ListSetter.recordingList.get(i);
                    ListSetter.recordingList.set(i, ListSetter.recordingList.get(j));
                    ListSetter.recordingList.set(j, temp);
                }
            }
        }
    }

    public static void updateList(String url, String time, int position){
        for(int i=0;i<ListSetter.recordingList.size();i++){
            if(ListSetter.recordingList.get(i).getVideoUrl().equals(url)){
                ListSetter.recordingList.get(i).setTime("\t"+time);
                ListSetter.recordingList.get(i).setPosition(position);
                break;
            }
        }
    }

    public static String getLastUrl(String url){
        for (int i=0; i<ListSetter.list.size(); i++){
            if(ListSetter.list.get(i).getVideoUrl().equals(url)) {
                if(i==0){
                    return ListSetter.list.get(ListSetter.list.size()-1).getVideoUrl();
                }else return ListSetter.list.get(i-1).getVideoUrl();

            }
        }
        return null;
    }

    public static String getNextUrl(String url){
        System.out.println("\n");
        for (int i=0; i<ListSetter.list.size(); i++){
            if(ListSetter.list.get(i).getVideoUrl().equals(url)){
                if(i==ListSetter.list.size()-1){
                    System.out.println("\n"+ListSetter.list.get(0).getVideoUrl());
                    return ListSetter.list.get(0).getVideoUrl();
                }else
                    System.out.println("\n"+ListSetter.list.get(i+1).getVideoUrl());
                return ListSetter.list.get(i+1).getVideoUrl();
            }
        }
        return null;
    }

}
