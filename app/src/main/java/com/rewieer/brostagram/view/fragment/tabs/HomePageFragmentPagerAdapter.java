package com.rewieer.brostagram.view.fragment.tabs;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rewieer.brostagram.view.fragment.tabs.home.FeedHomeTab;
import com.rewieer.brostagram.view.fragment.tabs.home.NotificationHomeTab;
import com.rewieer.brostagram.view.fragment.tabs.home.ProfileHomeTab;
import com.rewieer.brostagram.view.fragment.tabs.home.SearchHomeTab;

public class HomePageFragmentPagerAdapter extends FragmentPagerAdapter {
    public HomePageFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FeedHomeTab();
        } else if (position == 1) {
            return new SearchHomeTab();
        } else if (position == 2) {
            return new NotificationHomeTab();
        } else if (position == 3) {
            return new ProfileHomeTab();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
