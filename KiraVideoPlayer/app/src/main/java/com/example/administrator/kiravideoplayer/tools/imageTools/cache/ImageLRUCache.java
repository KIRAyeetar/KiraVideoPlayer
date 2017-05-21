package com.example.administrator.kiravideoplayer.tools.imageTools.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class ImageLRUCache<String, Bitmap> extends LinkedHashMap<String, Bitmap> {
    private static int maxCapacity;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private final Lock lock = new ReentrantLock();

    public static ImageLRUCache getImageCache () {
        return ImageCacheHolder.imageCache;
    }

    public static void setMaxCapacity(int maxCapacity){
        getImageCache().clear();
        ImageLRUCache.maxCapacity = maxCapacity;
    }

    public ImageLRUCache(int maxCapacity) {
        super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
        return size() > maxCapacity;
    }
    @Override
    public boolean containsKey(Object key) {
        try {
            lock.lock();
            return super.containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Bitmap get(Object key) {
        try {
            lock.lock();
            return super.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Bitmap put(String key, Bitmap value) {
        try {
            lock.lock();
            return super.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        try {
            lock.lock();
            return super.size();
        } finally {
            lock.unlock();
        }
    }

    public void clear() {
        try {
            lock.lock();
            super.clear();
        } finally {
            lock.unlock();
        }
    }

    public Collection<Entry<String, Bitmap>> getAll() {
        try {
            lock.lock();
            return new ArrayList<Entry<String, Bitmap>>(super.entrySet());
        } finally {
            lock.unlock();
        }
    }

    private static class ImageCacheHolder{
        private static final ImageLRUCache imageCache = new ImageLRUCache(10);
    }
}
