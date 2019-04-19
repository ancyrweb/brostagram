package com.rewieer.brostagram.view.fragment.pages;

import androidx.lifecycle.Observer;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.AuthResult;
import com.rewieer.brostagram.R;
import com.rewieer.brostagram.RequestStatus;
import com.rewieer.brostagram.service.Authenticator;
import com.rewieer.brostagram.view.ui.BroButton;
import com.rewieer.brostagram.view.ui.BroInput;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

public class LoginPageFragment extends Fragment {
    TextView mErrorMessageView;
    BroInput mEmailAddress;
    BroInput mPasswordView;
    BroButton mButton;

    public LoginPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move);
            transition.setDuration(800);
            setSharedElementEnterTransition(transition);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // We check the destination of the navigation
        // If we just entered the Login page, it will be egal to the ID of the fragment
        // holding the Login page. In which case we can set up our animation

        NavController navController = Navigation.findNavController(getActivity(), R.id.navhost);
        NavDestination destination = navController.getCurrentDestination();

        if (destination.getId() == R.id.navLoginPage) {
            ConstraintLayout layout = v.findViewById(R.id.contentConstraintLayout);
            layout.setAlpha(0.0f);
            layout
                .animate()
                .alpha(1.0f)
                .setStartDelay(500)
                .setDuration(800);
        }

        TextView registerText = v.findViewById(R.id.register);
        registerText.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginPageFragment_to_navRegisterPage, null));


        mErrorMessageView = v.findViewById(R.id.errorMessage);
        mEmailAddress = v.findViewById(R.id.emailAddress);
        mPasswordView = v.findViewById(R.id.password);
        mButton = v.findViewById(R.id.signIn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        return v;
    }

    void signIn() {
        String emailAddress = mEmailAddress.getText().toString();
        String password = mPasswordView.getText().toString();

        if (emailAddress.isEmpty()) {
            showErrorMessage("You have provided no username");
            return;
        } else if (password.isEmpty()) {
            showErrorMessage("You have provided no password");
            return;
        }

        Authenticator authenticator = new Authenticator();
        authenticator.signIn(emailAddress, password).observe(this, new Observer<RequestStatus<AuthResult>>() {
            @Override
            public void onChanged(@Nullable RequestStatus<AuthResult> request) {
                if (request.status == RequestStatus.Status.LOADING) {
                    hideErrorMessage();
                    mButton.setEnabled(false);
                } else if (request.status == RequestStatus.Status.SUCCESS) {
                    // Al right !!
                    mButton.setEnabled(true);
                } else {
                    showErrorMessage(request.message);
                    mButton.setEnabled(true);
                }
            }
        });
    }

    public void showErrorMessage(String message) {
        mErrorMessageView.setText(message);
        mErrorMessageView.setVisibility(View.VISIBLE);
    }

    public void hideErrorMessage() {
        mErrorMessageView.setVisibility(View.GONE);
    }
}
