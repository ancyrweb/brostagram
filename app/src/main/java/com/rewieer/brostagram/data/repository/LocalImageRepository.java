package com.rewieer.brostagram.data.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.provider.MediaStore;

import com.rewieer.brostagram.data.entity.LocalImage;

import java.util.ArrayList;

public class LocalImageRepository {
    private Context mContext;

    private static String[] QUERY_FIELDS = new String[] {
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.HEIGHT,
        MediaStore.Images.Media.WIDTH
    };

    public LocalImageRepository(Context context) {
        mContext = context;
    }

    public ArrayList<LocalImage> fetch() {
        Cursor externalImagesCursor = mContext.getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            QUERY_FIELDS,
            null,
            null,
            MediaStore.Images.Media.DATE_TAKEN + " DESC"
        );
        Cursor internalImagesCursor = mContext.getContentResolver().query(
            MediaStore.Images.Media.INTERNAL_CONTENT_URI,
            QUERY_FIELDS,
            null,
            null,
            MediaStore.Images.Media.DATE_TAKEN + " DESC"
        );

        MergeCursor finalCursor = new MergeCursor(new Cursor[]{ externalImagesCursor, internalImagesCursor });
        ArrayList<LocalImage> images = new ArrayList<>(finalCursor.getCount());

        finalCursor.moveToFirst();
        while (finalCursor.isAfterLast() == false) {
            LocalImage image = new LocalImage(
                finalCursor.getString(1),
                finalCursor.getInt(2),
                finalCursor.getInt(3)
            );

            image.ID = finalCursor.getInt(0);
            images.add(image);
            finalCursor.moveToNext();
        }

        return images;
    }
}
