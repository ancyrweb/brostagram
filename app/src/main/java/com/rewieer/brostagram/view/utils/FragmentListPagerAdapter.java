package com.rewieer.brostagram.view.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentListPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> mFragments;

    public FragmentListPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }
}
