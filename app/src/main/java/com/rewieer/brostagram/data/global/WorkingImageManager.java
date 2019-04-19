package com.rewieer.brostagram.data.global;

import com.rewieer.brostagram.data.entity.WorkingImage;

import androidx.lifecycle.MutableLiveData;

/**
 * Serves as a global store for the image being processed.
 */
public class WorkingImageManager {
    static WorkingImageManager instance;
    public static WorkingImageManager getInstance() {
        if (instance == null) {
            instance = new WorkingImageManager();
        }

        return instance;
    }

    MutableLiveData<WorkingImage> workingImage = new MutableLiveData<>();

    public WorkingImageManager() {

    }

    public void init(String path) {
        workingImage.setValue(new WorkingImage(path));
    }

    public MutableLiveData<WorkingImage> getLiveData() {
        return workingImage;
    }
}
