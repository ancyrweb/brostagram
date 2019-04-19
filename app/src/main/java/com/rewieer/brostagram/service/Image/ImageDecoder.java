package com.rewieer.brostagram.service.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.rewieer.brostagram.RequestStatus;

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
            public int width = -1;
            public int height = -1;

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
                options.width = width;
                options.height = height;

                return options;
            }
        }

        public int width = -1;
        public int height = -1;

        public boolean hasDimensionsSet() {
            return this.height > -1 && this.width > -1;
        }
    }

    public interface DecodingListener {
        enum STATUS { SUCCESS, ERROR }
        void onStatus(STATUS status, @Nullable Bitmap bitmap, @Nullable String errorMessage);
    }

    /*************
     * Main Code
     */

    private static final int CORE_AMOUNT = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private ThreadPoolExecutor mThreadPool;
    private Handler mUIThreadHandler;
    private final LruCache<String, Bitmap> mCache;

    private ImageDecoder() {
        // Either 10MB or 1/8 of maximal VM. Takes the min of the two.
        int cacheSize = (int) Math.min(10, (Runtime.getRuntime().maxMemory() / 1048576) / 8);

        mThreadPool = new ThreadPoolExecutor(
            CORE_AMOUNT,
            CORE_AMOUNT,
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            new LinkedBlockingDeque<Runnable>()
        );

        mUIThreadHandler = new Handler(Looper.getMainLooper());
        mCache = new LruCache<String, Bitmap>(cacheSize << 20) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public void loadImage(
        @NonNull final String path,
        @Nullable final DecodingOptions options,
        @NonNull final DecodingListener listener
    ) {
        final MutableLiveData<RequestStatus<Bitmap>> result = new MutableLiveData<>();

        // Check into the cache for existing bitmap
        Bitmap cached;
        if ((cached = mCache.get(hashPathAndOptions(path, options))) != null) {
            result.setValue(RequestStatus.success(cached));
            listener.onStatus(DecodingListener.STATUS.SUCCESS, cached, null);
            return;
        }

        result.setValue(RequestStatus.loading((Bitmap) null));
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                // Step 1 : decode the image to get dimensions
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, bitmapOptions);

                // Step 2 : determine optimal sample size
                int imageWidth = bitmapOptions.outWidth;
                int imageHeight = bitmapOptions.outHeight;
                int sampleSize = 1;

                if (options.hasDimensionsSet()) {
                    // TODO : if desired width/height are superior, skip
                    sampleSize = SampleUtils.getSampleSize(imageWidth, imageHeight, options.width, options.height);
                }

                // Step 3 : decode actual image
                bitmapOptions.inJustDecodeBounds = false;
                bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmapOptions.inSampleSize = sampleSize;
                Bitmap file = BitmapFactory.decodeFile(path, bitmapOptions);

                // Decoding might have failed
                if (file == null) {
                    mUIThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onStatus(DecodingListener.STATUS.ERROR, null, "Cannot decode file");
                        }
                    });
                    return;
                }

                // Step 4 : extract thumbnail if dimensions are set
                if (options != null && options.hasDimensionsSet()) {
                    file = ThumbnailUtils.extractThumbnail(file, options.width, options.height);
                }

                // Step 5 : cache for future reuse
                synchronized (mCache) {
                    // Double check for sure
                    if (mCache.get(hashPathAndOptions(path, options)) == null) {
                        mCache.put(hashPathAndOptions(path, options), file);
                    }
                }

                final Bitmap out = file;
                mUIThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onStatus(DecodingListener.STATUS.SUCCESS, out, null);
                    }
                });
            }
        });
    }

    public void loadImageIntoView(
        @NonNull final String path,
        @NonNull final ImageView imageView,
        @Nullable final DecodingOptions options) {
        loadImage(path, options, new DecodingListener() {
            @Override
            public void onStatus(STATUS status, @Nullable Bitmap bitmap, @Nullable String errorMessage) {
                setImageOnImageView(imageView, bitmap);
            }
        });
    }

    private void setImageOnImageView(final ImageView imageView, final Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    private String hashPathAndOptions(String path, @Nullable DecodingOptions options) {
        return path + (options == null ? "" : "::" + options.hashCode());
    }
}
