package com.rewieer.brostagram.view.utils;

import android.graphics.PorterDuff;
import com.google.android.material.tabs.TabLayout;

public class TintUtils {
    public static void setTintColor(TabLayout.Tab tab, int color) {
        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
}
