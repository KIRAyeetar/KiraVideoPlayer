package com.example.administrator.kiravideoplayer.tools.permissionApplicant;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class PermissionApplicant {
    private int REQUEST_CODE_ASK_PERMISSIONS=1;
    private String[] permissions = null;
    private Activity activity;

    public PermissionApplicant(Activity activity, String[]permissions){
        this.permissions = permissions;
        this.activity = activity;
    }

    public void insertDummyContactWrapper() {
        final int hasWriteContactsPermission = ContextCompat.checkSelfPermission(activity, permissions[0]);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_CONTACTS)) {
                new AlertDialog.Builder(activity)
                        .setTitle("Apply For Permissions !")
                        .setMessage("You need to allow these permissions as followed ")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                activity.finish();
                            }
                        }).show();
                return;
            }
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_ASK_PERMISSIONS);;
        }
    }
}
