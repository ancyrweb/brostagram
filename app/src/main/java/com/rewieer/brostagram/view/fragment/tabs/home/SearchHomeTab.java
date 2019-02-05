package com.rewieer.brostagram.view.fragment.tabs.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rewieer.brostagram.R;

public class SearchHomeTab extends Fragment {
    public SearchHomeTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tab_home_search, container, false);

        return view;
    }

}
