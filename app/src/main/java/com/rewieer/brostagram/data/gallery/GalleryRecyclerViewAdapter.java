package com.rewieer.brostagram.data.gallery;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rewieer.brostagram.R;
import com.rewieer.brostagram.RequestStatus;
import com.rewieer.brostagram.data.entity.Image;
import com.rewieer.brostagram.service.ImageDecoder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder> {
    private final static ImageDecoder.DecodingOptions IMAGE_DECODING_OPTIONS = new ImageDecoder.DecodingOptions.Builder()
        .thumbnail(true)
        .setHeight(256)
        .setWidth(256)
        .build(); // Serves

    /*************
     * ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(ImageView v) {
            super(v);
            this.imageView = v;
        }

        public void load(Image image) {
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
        mViewModel.getData().observe(lifecycleOwner, new Observer<RequestStatus<ArrayList<Image>>>() {
            @Override
            public void onChanged(@Nullable RequestStatus<ArrayList<Image>> arrayListRequestStatus) {
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public GalleryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ImageView imageView = new ImageView(mContext);
        imageView.setAdjustViewBounds(true);
        imageView.setPadding(3,0,3,3);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] {
            android.R.attr.state_activated
        }, mContext.getResources().getDrawable(R.drawable.transparent_overlay));

        imageView.setForeground(stateListDrawable);
        return new GalleryRecyclerViewAdapter.ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryRecyclerViewAdapter.ViewHolder viewHolder, final int i) {
        if (mSelectionTracker.isSelected((long) mViewModel.getImages().get(i).ID)) {
            viewHolder.imageView.setActivated(true);
        } else {
            viewHolder.imageView.setActivated(false);
        }

        viewHolder.load(mViewModel.getImages().get(i));
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectionTracker.select((long) mViewModel.getImages().get(i).ID);
            }
        });
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