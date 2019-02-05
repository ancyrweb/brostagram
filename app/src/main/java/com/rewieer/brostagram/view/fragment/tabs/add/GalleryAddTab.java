package com.rewieer.brostagram.view.fragment.tabs.add;

import android.Manifest;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rewieer.brostagram.R;
import com.rewieer.brostagram.data.entity.Image;
import com.rewieer.brostagram.data.gallery.GalleryItemDetailsLookup;
import com.rewieer.brostagram.data.gallery.GalleryPhotosViewModel;
import com.rewieer.brostagram.data.ImageProvider;
import com.rewieer.brostagram.data.gallery.GalleryRecyclerViewAdapter;
import com.rewieer.brostagram.data.gallery.GalleryRecyclerViewItemProvider;
import com.rewieer.brostagram.view.fragment.ViewPagerFragmentLifecycle;
import com.rewieer.brostagram.view.ui.IOSButton;

import java.util.List;

public class GalleryAddTab extends Fragment {
    private final static int IMAGES_PER_ROW = 3;

    public interface Listener {
        void onImageSelected(@Nullable Image image);
    }

    GalleryPhotosViewModel mViewModel;
    ImageProvider mImageProvider;
    Listener mListener;

    public GalleryAddTab() {
        // Required empty public constructor
    }

    public static GalleryAddTab createWithListener(Listener listener) {
        GalleryAddTab f = new GalleryAddTab();
        f.mListener = listener;
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tab_add_gallery, container, false);

        if (mImageProvider == null) {
            // We load it only once as it depends on the application context which doesn't change
            mImageProvider = new ImageProvider(getContext().getApplicationContext());
        }

        mViewModel = ViewModelProviders.of(this).get(GalleryPhotosViewModel.class);

        GalleryRecyclerViewAdapter adapter = new GalleryRecyclerViewAdapter(getContext(), getViewLifecycleOwner(), mViewModel);
        RecyclerView recyclerView = view.findViewById(R.id.addGalleryRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), IMAGES_PER_ROW));

        final GalleryRecyclerViewItemProvider provider = new GalleryRecyclerViewItemProvider(mViewModel, ItemKeyProvider.SCOPE_MAPPED);
        final SelectionTracker<Long> tracker = new SelectionTracker.Builder<>(
            "my-selection-id",
            recyclerView,
            provider,
            new GalleryItemDetailsLookup(),
            StorageStrategy.createLongStorage())
            .withSelectionPredicate(new SelectionTracker.SelectionPredicate<Long>() {
                @Override
                public boolean canSetStateForKey(@NonNull Long key, boolean nextState) {
                    return true;
                }

                @Override
                public boolean canSetStateAtPosition(int position, boolean nextState) {
                    return true;
                }

                @Override
                public boolean canSelectMultiple() {
                    return false;
                }
            })
            .build();

        tracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onSelectionChanged() {
                if (mListener == null) {
                    return;
                }

                // Note : alas, this gets called twice each time something is selected
                // Once to deselect the previous item, and once again to select the next
                // This causes the "next" button to flicker on selection..
                // This callback should only be called once, either rewrite some portion of the internals
                // Or add a kind of debounce of a few MS.
                // TODO : fix it

                long id = -1;
                if (tracker.hasSelection()) {
                    id = tracker.getSelection().iterator().next();
                }

                if (id == -1) {
                    mListener.onImageSelected(null);
                } else {
                    mListener.onImageSelected(mViewModel.getImages().get(provider.getPosition(id)));
                }
            }
        });

        adapter.setSelectionTracker(tracker);
        tryToLoadImages();
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            loadImages();
            return;
        }

        // Arrived here, we failed to load images, so we show it up
        // TODO : show the error
        mViewModel.setError("Cannot load the images from your phone.");
    }

    /**
     * Attempt to load images by first checking if permissions are set
     */
    public void tryToLoadImages() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, 1000);
            return;
        }

        loadImages();
    }

    /**
     * Load images, feed it our ViewModel
     */
    public void loadImages() {
        ImageProvider loader = new ImageProvider(getContext().getApplicationContext());
        mViewModel.setImages(loader.loadGalleryImages());
    }
}
