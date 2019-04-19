package com.rewieer.brostagram.view.fragment.tabs;

import com.rewieer.brostagram.view.fragment.tabs.customize.FiltersCustomizeTab;
import com.rewieer.brostagram.view.fragment.tabs.customize.ModifiersCustomizeTab;
import com.rewieer.brostagram.view.fragment.tabs.home.FeedHomeTab;
import com.rewieer.brostagram.view.fragment.tabs.home.NotificationHomeTab;
import com.rewieer.brostagram.view.fragment.tabs.home.ProfileHomeTab;
import com.rewieer.brostagram.view.fragment.tabs.home.SearchHomeTab;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CustomizePageFragmentPagerAdapter extends FragmentPagerAdapter {
    public CustomizePageFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FiltersCustomizeTab();
        } else if (position == 1) {
            return new ModifiersCustomizeTab();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Filters";
        } else {
            return "Modifiers";
        }
    }
}
