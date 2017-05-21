package com.example.administrator.kiravideoplayer.tools.downLoad;

import android.os.AsyncTask;
import android.os.Environment;

import com.example.administrator.kiravideoplayer.tools.another.InternetTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/5/20.
 */

public class DownLoadTask extends AsyncTask<String, Integer, Integer> {

    public static final int SUCCESS = 0;
    public static final int FAILED = 1;
    public static final int PAUSED = 2;
    public static final int CANCELED = 3;

    private DownLoadListener listener;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;

    public DownLoadTask(DownLoadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            long downLoadLength = 0;
            String downLoadUrl = strings[0];
            String fileName = downLoadUrl.substring(downLoadUrl.lastIndexOf("/"));
            System.out.println(fileName);
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            if (file.exists()) {
                downLoadLength = file.length();
            }

            long contentLength = getContentLength(downLoadUrl);
            if (contentLength == 0) {
                return FAILED;
            } else if (contentLength == downLoadLength) {
                return SUCCESS;
            }

            HttpURLConnection conn = null;
            try {
                URL mURL = new URL(downLoadUrl);
                conn = (HttpURLConnection) mURL.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            is = conn.getInputStream();
            savedFile = new RandomAccessFile(file, "rw");
            savedFile.seek(downLoadLength);
            byte[] b = new byte[1024];
            int total = 0;
            int len;
            while ((len = is.read(b)) != -1) {
                if (isCanceled) {
                    return CANCELED;
                } else if (isPaused) {
                    return PAUSED;
                } else {
                    total += len;
                    savedFile.write(b, 0, len);
                    int progress = (int) ((int) ((total + downLoadLength)) * 100 / contentLength);
                    publishProgress(progress);
                }
            }
            conn.disconnect();
            return SUCCESS;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (is != null ){
                    is.close();
                }
                if (savedFile != null){
                    savedFile.close();
                }
                if (isCanceled && file != null){
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if(progress > lastProgress){
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status){
            case SUCCESS:
                listener.onSuccess();
                break;
            case FAILED:
                listener.onFailed();
                break;
            case PAUSED:
                listener.onPaused();
                break;
            case CANCELED:
                listener.onCanceled();
                break;
        }
    }

    public void pauseDownload(){
        isPaused = true;
    }
    public void cancelDownLoad(){
        isCanceled = true;
    }

    private long getContentLength(String downLoadUrl) throws IOException {
        long contentLength = InternetTools.getFileSizeOfInternet(downLoadUrl);
        return contentLength;
    }
}
