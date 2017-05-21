package com.example.administrator.kiravideoplayer;

import android.Manifest;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.administrator.kiravideoplayer.databinding.ActivityRecordingBinding;
import com.example.administrator.kiravideoplayer.tools.permissionApplicant.PermissionApplicant;
import com.example.administrator.kiravideoplayer.videoList.ListSetter;

/**
 * Created by Administrator on 2017/5/21.
 */

public class RecordingActivity extends Activity {
    ActivityRecordingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recording);

        String i ="共 "+ ListSetter.recordingList.size() + " 条记录";
        binding.setRecordingNum(i);
        ListSetter listSetter = new ListSetter(this, binding);
        listSetter.setRecording_RC_View(ListSetter.recordingList);

    }

}
