package com.rewieer.brostagram.service;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rewieer.brostagram.CompleteListenerAdapter;
import com.rewieer.brostagram.RequestStatus;

/**
 * Core class for dealing with authentication
 */
public class Authenticator {
    FirebaseAuth mAuth;

    public Authenticator() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Create new account for the user
     * @param emailAddress
     * @param password
     * @return
     */
    public MutableLiveData<RequestStatus<AuthResult>> signUp(String emailAddress, String password) {
        final MutableLiveData<RequestStatus<AuthResult>> liveData = new MutableLiveData<>();
        liveData.setValue(RequestStatus.loading((AuthResult) null));

        mAuth
            .createUserWithEmailAndPassword(emailAddress, password)
            .addOnCompleteListener(new CompleteListenerAdapter<>(liveData));

        return liveData;
    }

    /**
     * Sign the user in
     * @param emailAddress
     * @param password
     * @return
     */
    public MutableLiveData<RequestStatus<AuthResult>> signIn(String emailAddress, String password) {
        final MutableLiveData<RequestStatus<AuthResult>> liveData = new MutableLiveData<>();
        liveData.setValue(RequestStatus.loading((AuthResult) null));

        mAuth
            .signInWithEmailAndPassword(emailAddress, password)
            .addOnCompleteListener(new CompleteListenerAdapter<>(liveData));

        return liveData;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void sighOut() {
        mAuth.signOut();
    }
}
