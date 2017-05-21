package com.example.administrator.kiravideoplayer;

import android.Manifest;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.kiravideoplayer.tools.permissionApplicant.PermissionApplicant;
import com.example.administrator.kiravideoplayer.videoList.ListSetter;
import com.example.administrator.kiravideoplayer.databinding.ActivityMainBinding;
import com.example.administrator.kiravideoplayer.videoList.ListTools;

public class MainActivity extends Activity {
    ActivityMainBinding binding;
    String path ="http://route.showapi.com/255-1?showapi_appid=38531&showapi_sign=1e0b438ef93a4cf3b36a64d9f0b4f344&num=18908712xxx&";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        PermissionApplicant permissionApplicant = new PermissionApplicant(this,
                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE});
        permissionApplicant.insertDummyContactWrapper();

        ListSetter listSetter = new ListSetter(this, binding);
        listSetter.getListByUrl(path);

        binding.recodingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListTools.openRecording();
            }
        });
    }
}
