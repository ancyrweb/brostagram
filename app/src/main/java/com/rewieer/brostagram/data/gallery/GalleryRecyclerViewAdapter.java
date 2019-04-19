package com.rewieer.brostagram.data.gallery;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rewieer.brostagram.R;
import com.rewieer.brostagram.RequestStatus;
import com.rewieer.brostagram.data.entity.LocalImage;
import com.rewieer.brostagram.service.Image.ImageDecoder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder> {
    private final static ImageDecoder.DecodingOptions IMAGE_DECODING_OPTIONS = new ImageDecoder.DecodingOptions.Builder()
        .setHeight(256)
        .setWidth(256)
        .build(); // Serves

    private static int IMAGE_HEIGHT_COUNT = 180;
    private static int IMAGE_HEIGHT;

    /*************
     * ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public int imageID;

        ViewHolder(ImageView v) {
            super(v);
            this.imageView = v;
        }

        public void load(LocalImage image) {
            // It's okay to call it on every update because the image is cached
            ImageDecoder.getInstance().loadImageIntoView(
                image.path,
                this.imageView,
                IMAGE_DECODING_OPTIONS
            );
        }
    }


    private SelectionTracker<Long> mSelectionTracker;
    private GalleryPhotosViewModel mViewModel;
    private Context mContext;

    public GalleryRecyclerViewAdapter(Context context, LifecycleOwner lifecycleOwner, GalleryPhotosViewModel viewModel) {
        mContext = context;
        mViewModel = viewModel;
        mViewModel.getData().observe(lifecycleOwner, new Observer<RequestStatus<ArrayList<LocalImage>>>() {
            @Override
            public void onChanged(@Nullable RequestStatus<ArrayList<LocalImage>> arrayListRequestStatus) {
                notifyDataSetChanged();
            }
        });

        IMAGE_HEIGHT = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, IMAGE_HEIGHT_COUNT, mContext.getResources().getDisplayMetrics());
    }

    @NonNull
    @Override
    public GalleryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ImageView imageView = new ImageView(mContext);
        imageView.setAdjustViewBounds(true);
        imageView.setPadding(3,0,3,3);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(
            new GridLayoutManager.LayoutParams(
                GridLayoutManager.LayoutParams.MATCH_PARENT,
                IMAGE_HEIGHT));

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] {
            android.R.attr.state_activated
        }, mContext.getResources().getDrawable(R.drawable.transparent_overlay));

        imageView.setForeground(stateListDrawable);

        final ViewHolder viewHolder = new GalleryRecyclerViewAdapter.ViewHolder(imageView);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectionTracker.select((long) viewHolder.imageID);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryRecyclerViewAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.imageID = mViewModel.getImages().get(i).ID;

        if (mSelectionTracker.isSelected((long) viewHolder.imageID)) {
            viewHolder.imageView.setActivated(true);
        } else {
            viewHolder.imageView.setActivated(false);
        }

        viewHolder.load(mViewModel.getImages().get(i));
    }

    @Override
    public int getItemCount() {
        if (!mViewModel.hasImages())
            return 0;

        return mViewModel.getImages().size();
    }

    public void setSelectionTracker(SelectionTracker<Long> mSelectionTracker) {
        this.mSelectionTracker = mSelectionTracker;
    }
}