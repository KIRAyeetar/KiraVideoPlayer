package com.example.administrator.kiravideoplayer.tools.imageTools.asyncTask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.administrator.kiravideoplayer.tools.imageTools.ImageTools;

/**
 * Created by Administrator on 2017/1/22 0022.
 */
//异步加载单条新闻
public class AsyncInternetImages extends AsyncTask<String, Integer, Void>{
    private ImageView mImageView;
    private Bitmap bitmap = null;

    public AsyncInternetImages(ImageView mImageView){
        this.mImageView = mImageView;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(String... url) {
        bitmap = ImageTools.getIMGFromInternet(url[0]);
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
