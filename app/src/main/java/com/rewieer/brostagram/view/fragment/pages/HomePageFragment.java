package com.rewieer.brostagram.view.fragment.pages;

import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rewieer.brostagram.R;
import com.rewieer.brostagram.view.fragment.tabs.HomePageFragmentPagerAdapter;
import com.rewieer.brostagram.view.utils.TintUtils;

import java.util.HashMap;
import java.util.Map;

import androidx.navigation.Navigation;

public class HomePageFragment extends Fragment {
    static int ACTIVE_ICON_COLOR = Color.rgb(0, 0, 0);
    static int INACTIVE_ICON_COLOR = Color.rgb(180, 180, 180);

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_page_home, container, false);
        final ViewPager pager = view.findViewById(R.id.tabsViewPager);

        // Creating tabs
        HomePageFragmentPagerAdapter adapter = new HomePageFragmentPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        final Map<Integer, Integer> tabPositionMap = createTabMap();

        TabLayout layout = view.findViewById(R.id.tabLayout);
        layout.setTabRippleColor(null);

        // Setting the tint color of each icon
        // Active icons will be shown in a more visible way than Inactive ones
        for (int i = 1; i < layout.getTabCount(); i++) {
            TintUtils.setTintColor(layout.getTabAt(i), INACTIVE_ICON_COLOR);
        }

        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabPositionMap.containsKey(tab.getPosition())) {
                    // Select tab and return
                    pager.setCurrentItem(tabPositionMap.get(tab.getPosition()), false);
                    TintUtils.setTintColor(tab, ACTIVE_ICON_COLOR);
                    return;
                }

                // It's the middle icon, we go to the AddPageFragment
                Navigation
                    .findNavController(getActivity(), R.id.navhost)
                    .navigate(R.id.action_navHomePage_to_addPageFragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (!tabPositionMap.containsKey(tab.getPosition()))
                    return;

                TintUtils.setTintColor(tab, INACTIVE_ICON_COLOR);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }



    /**
     *  map every tab positions to an entry into the pager
     *  That is because the tabLayout doesn't have a 1:1 correspondance with the pager
     *  The middle icon (+) is not an actual entry of the pager, it has a specific handler
     * @return Map
     */
    public Map<Integer, Integer> createTabMap() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 0);
        map.put(1, 1);
        map.put(3, 2);
        map.put(4, 3);
        return map;
    }
}
