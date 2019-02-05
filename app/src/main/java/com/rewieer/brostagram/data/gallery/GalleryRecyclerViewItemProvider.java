package com.rewieer.brostagram.data.gallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

public class GalleryRecyclerViewItemProvider extends ItemKeyProvider<Long> {
    GalleryPhotosViewModel mViewModel;

    public GalleryRecyclerViewItemProvider(GalleryPhotosViewModel viewModel, int scope) {
        super(scope);
        mViewModel = viewModel;
    }

    @Nullable
    @Override
    public Long getKey(int position) {
        if (!mViewModel.hasImages())
            return null;

        return (long) mViewModel.getRawData().data.get(position).ID;
    }

    @Override
    public int getPosition(@NonNull Long key) {
        if (!mViewModel.hasImages())
            return -1;

        for (int i = 0; i < mViewModel.getRawData().data.size(); i++) {
            if (mViewModel.getRawData().data.get(i).ID == key)
                return i;
        }

        return -1;
    }
}