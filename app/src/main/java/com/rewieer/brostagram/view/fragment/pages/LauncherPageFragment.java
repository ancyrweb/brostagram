package com.rewieer.brostagram.view.fragment.pages;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rewieer.brostagram.R;
import com.rewieer.brostagram.service.Authenticator;

import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

public class LauncherPageFragment extends Fragment {
    public LauncherPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_launcher, container, false);

        Authenticator authenticator = new Authenticator();
        if (authenticator.getCurrentUser() == null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    TextView textView = view.findViewById(R.id.loginTextView);

                    FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                        .addSharedElement(textView, ViewCompat.getTransitionName(textView))
                        .build();

                    Navigation.findNavController(view).navigate(
                        R.id.action_launcherPageFragment_to_loginPageFragment,
                        null,
                        null,
                        extras
                    );
                }
            }, 2000);
        } else {
            Navigation.findNavController(getActivity(), R.id.navhost).navigate(
                R.id.action_navLauncherPage_to_homePageFragment
            );
        }

        return view;
    }

}
