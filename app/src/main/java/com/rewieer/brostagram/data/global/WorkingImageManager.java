package com.rewieer.brostagram.data.global;

import com.rewieer.brostagram.data.entity.LocalImage;
import com.rewieer.brostagram.data.entity.WorkingImage;

import androidx.annotation.Nullable;
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

    private MutableLiveData<WorkingImage> workingImage = new MutableLiveData<>();
    private @Nullable LocalImage localImage = null;

    public WorkingImageManager() {

    }

    public void init(LocalImage image) {
        this.localImage = image;
        workingImage.setValue(new WorkingImage(image.path));
    }

    public void clear() {
        this.localImage = null;
        workingImage.setValue(null);
    }

    public MutableLiveData<WorkingImage> getLiveData() {
        return workingImage;
    }

    @Nullable
    public LocalImage getLocalImage() {
        return localImage;
    }
}
