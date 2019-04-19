package com.rewieer.brostagram.view.fragment.tabs.customize;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rewieer.brostagram.R;

import androidx.fragment.app.Fragment;

public class FiltersCustomizeTab extends Fragment {
    public FiltersCustomizeTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tab_customize_filters, container, false);
        return view;
    }

}
