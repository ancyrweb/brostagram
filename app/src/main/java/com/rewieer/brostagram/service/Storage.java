package com.rewieer.brostagram.service;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Storage {
    private static Storage mInstance;

    public static Storage getInstance() {
        if (mInstance == null) {
            mInstance = new Storage();
        }

        return mInstance;
    }

    public class UploadData {
        public String uri;
        public UploadTask task;

        public UploadData(String uri, UploadTask task) {
            this.uri = uri;
            this.task = task;
        }
    }

    public interface UploadStatusListener {
        enum STATUS { SUCCESS, ERROR }
        void onStatus(STATUS status, @Nullable UploadData data, @Nullable Exception exception);
    }
    private FirebaseStorage mStorage;

    public Storage() {
        mStorage = FirebaseStorage.getInstance();
    }

    public void upload(String ref, byte[] data, final UploadStatusListener listener) {
        StorageReference rootRef = mStorage.getReference();
        final StorageReference imgRef = rootRef.child(ref);
        final UploadTask task = imgRef.putBytes(data);

        task
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> subtask) {
                            listener.onStatus(
                                UploadStatusListener.STATUS.SUCCESS,
                                new UploadData(subtask.getResult().toString(), task),
                                null
                            );
                        }
                    });
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onStatus(
                        UploadStatusListener.STATUS.ERROR,
                        null,
                        e
                    );
                }
            });
    }
}
