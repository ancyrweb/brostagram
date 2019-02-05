package com.rewieer.brostagram.view.fragment.pages;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rewieer.brostagram.R;
import com.rewieer.brostagram.data.entity.Image;
import com.rewieer.brostagram.view.fragment.tabs.add.CameraAddTab;
import com.rewieer.brostagram.view.fragment.tabs.add.GalleryAddTab;
import com.rewieer.brostagram.view.ui.IOSButton;
import com.rewieer.brostagram.view.utils.NamedFragmentsPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.navigation.Navigation;

public class AddPageFragment extends Fragment implements GalleryAddTab.Listener {
    IOSButton mNextButton;
    public AddPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_page_add, container, false);
        final ViewPager pager = view.findViewById(R.id.addPageViewPager);

        // Creating tabs
        final NamedFragmentsPagerAdapter adapter = new NamedFragmentsPagerAdapter(getFragmentManager(), createTabs());
        pager.setAdapter(adapter);

        TabLayout layout = view.findViewById(R.id.addPageTabLayout);
        layout.setupWithViewPager(pager);
        layout.setTabRippleColor(null);

        ImageView crossImage = view.findViewById(R.id.addPageCrossIcon);
        final TextView pageTitle = view.findViewById(R.id.addPageTitle);
        mNextButton = view.findViewById(R.id.addPageNextButton);
        mNextButton.setVisibility(View.GONE);

        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pageTitle.setText(adapter.getPageTitle(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation
                    .findNavController(getActivity(), R.id.navhost)
                    .popBackStack();
            }
        });
        return view;
    }

    public ArrayList<Pair<String, Fragment>> createTabs() {
        return new ArrayList<>(Arrays.asList(
            new Pair<String, Fragment>("Gallery", GalleryAddTab.createWithListener(this)),
            new Pair<String, Fragment>("Camera", new CameraAddTab())
        ));
    }

    @Override
    public void onImageSelected(@Nullable Image image) {
        if (mNextButton == null)
            return;

        if (image == null) {
            mNextButton.setVisibility(View.GONE);
        } else {
            mNextButton.setVisibility(View.VISIBLE);
        }

    }
}
