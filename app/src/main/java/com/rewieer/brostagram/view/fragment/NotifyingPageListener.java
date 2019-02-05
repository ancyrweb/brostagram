package com.rewieer.brostagram.view.fragment;

import java.lang.ref.WeakReference;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class NotifyingPageListener implements ViewPager.OnPageChangeListener {
    private WeakReference<FragmentPagerAdapter> mAdapter;
    private WeakReference<ViewPager> mPager;

    public NotifyingPageListener(FragmentPagerAdapter adapter, ViewPager pager) {
        mAdapter = new WeakReference<>(adapter);
        mPager = new WeakReference<>(pager);
    }

    @Override
    public void onPageSelected(int nextPosition) {
        if (mAdapter.get() == null || mPager.get() == null)
            return;

        ViewPagerFragmentLifecycle next = (ViewPagerFragmentLifecycle) mAdapter.get().getItem(nextPosition);
        ViewPagerFragmentLifecycle current = (ViewPagerFragmentLifecycle) mAdapter.get().getItem(mPager.get().getCurrentItem());

        current.onFragmentPaused();
        next.onFragmentResumed();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
}
