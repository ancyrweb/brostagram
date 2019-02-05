package com.rewieer.brostagram.data.gallery;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.Nullable;

import com.rewieer.brostagram.RequestStatus;
import com.rewieer.brostagram.data.entity.Image;

import java.util.ArrayList;

public class GalleryPhotosViewModel extends ViewModel {
    MutableLiveData<RequestStatus<ArrayList<Image>>> request = new MutableLiveData<>();

    public GalleryPhotosViewModel() {
        // Loading initial object, that will be reused
        request.setValue(RequestStatus.loading((ArrayList<Image>) null));
    }

    private void update(RequestStatus.Status status, @Nullable ArrayList<Image> images, @Nullable String message) {
        RequestStatus<ArrayList<Image>> requestStatus = request.getValue();
        if (requestStatus == null)
            return;

        requestStatus.status = status;
        requestStatus.message = message;
        requestStatus.data = images;
        request.setValue(requestStatus);
    }

    public void setImages(ArrayList<Image> images) {
        update(RequestStatus.Status.SUCCESS, images, null);
    }

    public void setError(String error) {
        update(RequestStatus.Status.ERROR, null, error);
    }

    public void setLoading() {
        update(RequestStatus.Status.LOADING, null, null);
    }

    public MutableLiveData<RequestStatus<ArrayList<Image>>> getData() {
        return request;
    }

    public RequestStatus<ArrayList<Image>> getRawData() {
        return request.getValue();
    }

    public ArrayList<Image> getImages() {
        return request.getValue().data;
    }

    public boolean isLoading() {
        return request.getValue().status == RequestStatus.Status.LOADING;
    }

    public boolean hasImages() {
        return request.getValue().data != null;
    }
}
