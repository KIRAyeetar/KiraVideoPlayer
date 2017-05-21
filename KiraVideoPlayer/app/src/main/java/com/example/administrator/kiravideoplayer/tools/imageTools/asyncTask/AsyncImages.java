package com.example.administrator.kiravideoplayer.tools.imageTools.asyncTask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/5/20.
 */

public class AsyncImages extends AsyncTask<Bitmap, Integer, Void> {
    private ImageView mImageView;
    private Bitmap bitmap = null;

    public AsyncImages(ImageView mImageView){
        this.mImageView = mImageView;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Bitmap... bitmaps) {
        bitmap = bitmaps[0];
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int value = values[0];
    }
}

