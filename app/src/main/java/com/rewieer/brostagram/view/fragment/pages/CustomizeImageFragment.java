package com.rewieer.brostagram.view.fragment.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rewieer.brostagram.R;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class CustomizeImageFragment extends Fragment {
    public CustomizeImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_page_customize, container, false);

        ImageView backImage = view.findViewById(R.id.customizePageBackIcon);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation
                    .findNavController(getActivity(), R.id.navhost)
                    .popBackStack();
            }
        });
        return view;
    }
}
