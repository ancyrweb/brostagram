package com.rewieer.brostagram.data.gallery;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

public class GalleryItemDetailsLookup extends ItemDetailsLookup<Long> {
    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        return null;
    }
}