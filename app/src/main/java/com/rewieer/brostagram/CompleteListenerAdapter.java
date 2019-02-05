package com.rewieer.brostagram;

import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Utility class that provide an adapter from the OnCompleteListener of GMS
 * to our RequestStatus.
 */
public class CompleteListenerAdapter<ResultType> implements OnCompleteListener<ResultType> {
    private MutableLiveData<RequestStatus<ResultType>> mLiveData;

    public CompleteListenerAdapter(MutableLiveData<RequestStatus<ResultType>> liveData) {
        mLiveData = liveData;
    }
    @Override
    public void onComplete(@NonNull Task<ResultType> task) {
        if (task.isSuccessful()) {
            mLiveData.setValue(RequestStatus.success(task.getResult()));
        } else {
            mLiveData.setValue(RequestStatus.error(task.getException().toString(), (ResultType) null ));
        }
    }
}
