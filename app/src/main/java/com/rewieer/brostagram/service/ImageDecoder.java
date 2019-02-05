package com.rewieer.brostagram.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageDecoder {

    /**************
     * Singleton
     */

    static ImageDecoder mInstance;
    public static ImageDecoder getInstance() {
        if (mInstance == null) {
            mInstance = new ImageDecoder();
        }

        return mInstance;
    }

    /*********************
     * Decoding options
     */

    public static class DecodingOptions {
        public static class Builder {
            public boolean isThumbnail = false;
            public int width = -1;
            public int height = -1;

            public Builder thumbnail(boolean thumbnail) {
                isThumbnail = thumbnail;
                return this;
            }

            public Builder setWidth(int width) {
                this.width = width;
                return this;
            }

            public Builder setHeight(int height) {
                this.height = height;
                return this;
            }

            public DecodingOptions build() {
                DecodingOptions options = new DecodingOptions();
                options.isThumbnail = isThumbnail;
                options.width = width;
                options.height = height;

                return options;
            }
        }

        public boolean isThumbnail = false;
        public int width = -1;
        public int height = -1;
    }

    /*************
     * Main Code
     */

    private static final int CORE_AMOUNT = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private static int CACHE_SIZE = 8; // In MB

    private ThreadPoolExecutor mThreadPool;
    private Handler mUIThreadHandler;
    private final LruCache<String, Bitmap> mCache;

    private ImageDecoder() {
        mThreadPool = new ThreadPoolExecutor(
            CORE_AMOUNT,
            CORE_AMOUNT,
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            new LinkedBlockingDeque<Runnable>()
        );

        mUIThreadHandler = new Handler(Looper.getMainLooper());
        mCache = new LruCache<String, Bitmap>(CACHE_SIZE << 20) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    private String hashPathAndOptions(String path, DecodingOptions options) {
        return path + "::" + options.hashCode();
    }

    public void loadImageIntoView(
        @NonNull final String path,
        @NonNull final ImageView imageView,
        @Nullable final DecodingOptions options) {

        // Check into the cache for existing bitmap
        Bitmap cached;
        if ((cached = mCache.get(hashPathAndOptions(path, options))) != null) {
            setImageOnImageView(imageView, cached);
        }

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888; // 4 bytes per img
                Bitmap file = BitmapFactory.decodeFile(path, bitmapOptions);

                if (options != null && options.isThumbnail) {
                    file = ThumbnailUtils.extractThumbnail(file, options.width, options.height);
                }

                synchronized (mCache) {
                    if (mCache.get(hashPathAndOptions(path, options)) == null) {
                        mCache.put(hashPathAndOptions(path, options), file);
                    }
                }

                syncSetImageOnImageView(imageView, file);
            }
        });
    }

    private void syncSetImageOnImageView(final ImageView imageView, final Bitmap bitmap) {
        mUIThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                setImageOnImageView(imageView, bitmap);
            }
        });
    }

    private void setImageOnImageView(final ImageView imageView, final Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
