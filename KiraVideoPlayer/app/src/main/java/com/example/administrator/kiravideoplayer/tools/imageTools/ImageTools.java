package com.example.administrator.kiravideoplayer.tools.imageTools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.kiravideoplayer.tools.imageTools.asyncTask.AsyncImages;
import com.example.administrator.kiravideoplayer.tools.imageTools.asyncTask.AsyncInternetImages;
import com.example.administrator.kiravideoplayer.tools.imageTools.cache.ImageLRUCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class ImageTools{
    private static ImageLRUCache imageCache = null;

    public static void LoadImageIntoViewByURL (ImageView imageView, String url) {
        AsyncInternetImages asyncInternetImages = new AsyncInternetImages(imageView);
        asyncInternetImages.execute(url);
    }

    public static Bitmap getIMGFromInternet (String url){
        if (imageCache == null){
            imageCache = ImageLRUCache.getImageCache();
        }
        if(imageCache.containsKey(url)){
            Bitmap softReference = (Bitmap) imageCache.get(url);
            return softReference;
        }else {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new URL(url).openStream());
                imageCache.put(url,bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                return bitmap;
            }
        }
    }

    public static void LoadFirstImageFromVideo(final ImageView imageView, final String url){
        if (imageCache == null){
            imageCache = ImageLRUCache.getImageCache();
        }if(imageCache.containsKey(url)){
            Bitmap softReference = (Bitmap) imageCache.get(url);
            LoadImageOnMainThread(imageView, softReference);
        }else {
            new Thread(new Runnable() {
                Bitmap bitmap = null;
                @Override
                public void run() {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    try {
                        retriever.setDataSource(url,new HashMap<String, String>());
                        bitmap = retriever.getFrameAtTime(0);
                    } catch (IllegalArgumentException ex)
                    {
                        Log.d("BindingAdapter", "createVideoThumbnail - IllegalArgumentException:" + ex);
                    } catch (RuntimeException ex)
                    {
                        Log.d("BindingAdapter", "createVideoThumbnail - RuntimeException:" + ex);
                    } finally  {
                        imageCache.put(url,bitmap);
                        LoadImageOnMainThread(imageView, bitmap);
                        try  {
                            retriever.release();
                        } catch (RuntimeException ex) {
                            Log.d("BindingAdapter", "createVideoThumbnail - RuntimeException:" + ex);
                        }
                    }
                }
            }).start();
        }
    }

    public static void LoadImageOnMainThread(ImageView imageView, Bitmap bitmap){
        AsyncImages asyncImages = new AsyncImages(imageView);
        asyncImages.execute(bitmap);
    }

    public static void saveImageToGallery(Context context, Bitmap bmp, String imageName, String fileName, int compressionRatio) {
        if (bmp == null){
            Toast.makeText(context, "Error! Please check your file", Toast.LENGTH_LONG).show();
            return;
        }
        File appDir = new File(Environment.getExternalStorageDirectory(), fileName);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        imageName = imageName + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (compressionRatio >= 0 && compressionRatio <=100) {
                bmp.compress(Bitmap.CompressFormat.JPEG, compressionRatio, fos);
            }else {
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            }
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error! Please check your file", Toast.LENGTH_LONG).show();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            String s = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), imageName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error! Please check your file", Toast.LENGTH_LONG).show();
            return;
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
        Toast.makeText(context, "save completely!", Toast.LENGTH_LONG).show();
    }

    public static void setCacheSize(int maxCapacity){
        ImageLRUCache.setMaxCapacity(maxCapacity);
    }
}
