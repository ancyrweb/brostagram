package com.rewieer.brostagram.data.gallery;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.Nullable;

import com.rewieer.brostagram.RequestStatus;
import com.rewieer.brostagram.data.entity.LocalImage;

import java.util.ArrayList;

public class GalleryPhotosViewModel extends ViewModel {
    MutableLiveData<RequestStatus<ArrayList<LocalImage>>> request = new MutableLiveData<>();

    public GalleryPhotosViewModel() {
        // Loading initial object, that will be reused
        request.setValue(RequestStatus.loading((ArrayList<LocalImage>) null));
    }

    private void update(RequestStatus.Status status, @Nullable ArrayList<LocalImage> images, @Nullable String message) {
        RequestStatus<ArrayList<LocalImage>> requestStatus = request.getValue();
        if (requestStatus == null)
            return;

        requestStatus.status = status;
        requestStatus.message = message;
        requestStatus.data = images;
        request.setValue(requestStatus);
    }

    public void setImages(ArrayList<LocalImage> images) {
        update(RequestStatus.Status.SUCCESS, images, null);
    }

    public void setError(String error) {
        update(RequestStatus.Status.ERROR, null, error);
    }

    public void setLoading() {
        update(RequestStatus.Status.LOADING, null, null);
    }

    public MutableLiveData<RequestStatus<ArrayList<LocalImage>>> getData() {
        return request;
    }

    public RequestStatus<ArrayList<LocalImage>> getRawData() {
        return request.getValue();
    }

    public ArrayList<LocalImage> getImages() {
        return request.getValue().data;
    }

    public boolean isLoading() {
        return request.getValue().status == RequestStatus.Status.LOADING;
    }

    public boolean hasImages() {
        return request.getValue().data != null;
    }
}
