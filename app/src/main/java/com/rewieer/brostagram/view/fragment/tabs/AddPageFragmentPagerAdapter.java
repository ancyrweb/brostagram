package com.rewieer.brostagram.view.fragment.tabs;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rewieer.brostagram.view.fragment.tabs.add.CameraAddTab;
import com.rewieer.brostagram.view.fragment.tabs.add.GalleryAddTab;

public class AddPageFragmentPagerAdapter extends FragmentPagerAdapter {
    GalleryAddTab.Listener mListener;

    public AddPageFragmentPagerAdapter(FragmentManager fm, GalleryAddTab.Listener listener) {
        super(fm);
        mListener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return GalleryAddTab.createWithListener(mListener);
        } else if (position == 1) {
            return new CameraAddTab();
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Gallery";
        } else {
            return "Camera";
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
