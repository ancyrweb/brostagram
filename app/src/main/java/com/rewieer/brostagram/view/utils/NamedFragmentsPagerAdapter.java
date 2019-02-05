package com.rewieer.brostagram.view.utils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.util.Pair;

import java.util.ArrayList;

public class NamedFragmentsPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Pair<String, Fragment>> mFragments;

    public NamedFragmentsPagerAdapter(FragmentManager fm, ArrayList<Pair<String, Fragment>> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i).second;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).first;
    }
}
