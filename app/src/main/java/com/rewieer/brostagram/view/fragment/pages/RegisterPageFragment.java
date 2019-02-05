package com.rewieer.brostagram.view.fragment.pages;

import androidx.lifecycle.Observer;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import androidx.navigation.Navigation;

public class RegisterPageFragment extends Fragment {
    TextView mErrorMessageView;
    BroInput mEmailAddress;
    BroInput mPasswordView;
    BroInput mPasswordConfirmView;
    BroButton mButton;

    public RegisterPageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_page_register, container, false);

        mErrorMessageView = v.findViewById(R.id.errorMessage);
        mEmailAddress = v.findViewById(R.id.emailAddress);
        mPasswordView = v.findViewById(R.id.password);
        mPasswordConfirmView = v.findViewById(R.id.password2);

        mButton = v.findViewById(R.id.submit);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              register();
            }
        });
        return v;
    }

    public void register() {
        String emailAddress = mEmailAddress.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordConfirm = mPasswordConfirmView.getText().toString();

        if (emailAddress.isEmpty()) {
            showErrorMessage("You have provided no username");
            return;
        } else if (emailAddress.length() < 3) {
            showErrorMessage("The username you provided is under 3 characters");
            return;
        } else if (password.equals(passwordConfirm) == false) {
            showErrorMessage("Passwords dont match");
            return;
        }

        Authenticator authenticator = new Authenticator();
        authenticator.signUp(emailAddress, password).observe(this, new Observer<RequestStatus<AuthResult>>() {
            @Override
            public void onChanged(@Nullable RequestStatus<AuthResult> request) {
                if (request.status == RequestStatus.Status.LOADING) {
                    hideErrorMessage();
                    mButton.setEnabled(false);
                } else if (request.status == RequestStatus.Status.SUCCESS) {
                    Navigation.findNavController(getActivity(), R.id.navhost).popBackStack();
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
