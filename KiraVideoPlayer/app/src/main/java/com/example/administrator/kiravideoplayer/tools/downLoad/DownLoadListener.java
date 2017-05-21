package com.example.administrator.kiravideoplayer.tools.downLoad;

/**
 * Created by Administrator on 2017/5/20.
 */

public interface DownLoadListener {
    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCanceled();
}
