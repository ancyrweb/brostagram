package com.rewieer.brostagram.view.fragment.pages;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.rewieer.brostagram.R;
import com.rewieer.brostagram.data.entity.WorkingImage;
import com.rewieer.brostagram.data.global.WorkingImageManager;
import com.rewieer.brostagram.service.Image.ImageDecoder;
import com.rewieer.brostagram.view.fragment.tabs.CustomizePageFragmentPagerAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

public class CustomizeImageFragment extends Fragment {
    private final static ImageDecoder.DecodingOptions DECODING_OPTIONS =
        new ImageDecoder.DecodingOptions.Builder()
            .build();

    public CustomizeImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_page_customize, container, false);

        final ImageView workingImageView = view.findViewById(R.id.customizeWorkingImage);

        ImageView backImage = view.findViewById(R.id.customizePageBackIcon);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation
                    .findNavController(getActivity(), R.id.navhost)
                    .popBackStack();
            }
        });

        final ViewPager pager = view.findViewById(R.id.customizePageViewPager);

        // Creating tabs
        CustomizePageFragmentPagerAdapter adapter = new CustomizePageFragmentPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        TabLayout layout = view.findViewById(R.id.customizePageTabLayout);
        layout.setupWithViewPager(pager);
        layout.setTabRippleColor(null);

        WorkingImageManager
            .getInstance()
            .getLiveData()
            .observe(this, new Observer<WorkingImage>() {
                @Override
                public void onChanged(WorkingImage workingImage) {
                    if (workingImage == null)
                        return;

                    ImageDecoder
                        .getInstance()
                        .loadImage(
                            workingImage.path,
                            DECODING_OPTIONS,
                            new ImageDecoder.DecodingListener() {
                                @Override
                                public void onStatus(STATUS status, @Nullable Bitmap bitmap, @Nullable String errorMessage) {
                                    if (status == STATUS.SUCCESS) {
                                        workingImageView.setImageBitmap(bitmap);
                                    }
                                }
                            }
                        );
                }
            });

        return view;
    }
}
