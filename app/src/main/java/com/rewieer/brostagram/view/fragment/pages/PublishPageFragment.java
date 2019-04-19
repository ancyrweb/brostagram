package com.rewieer.brostagram.view.fragment.pages;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;
import com.rewieer.brostagram.R;
import com.rewieer.brostagram.data.entity.LocalImage;
import com.rewieer.brostagram.data.entity.WorkingImage;
import com.rewieer.brostagram.data.global.WorkingImageManager;
import com.rewieer.brostagram.service.Image.ImageDecoder;
import com.rewieer.brostagram.service.Storage;
import com.rewieer.brostagram.view.ui.IOSButton;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

public class PublishPageFragment extends Fragment {
  private final static ImageDecoder.DecodingOptions DECODING_OPTIONS =
    new ImageDecoder.DecodingOptions.Builder()
      .build();

  public PublishPageFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_page_publish, container, false);
    final EditText publishText = view.findViewById(R.id.publishPageText);

    IOSButton publishButton = view.findViewById(R.id.publishPagePublishButton);
    publishButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String text = publishText.getText().toString();

        WorkingImage workingImage = WorkingImageManager.getInstance().getLiveData().getValue();
        if (workingImage == null)
          return; // Should not happen

        final LocalImage localImage = WorkingImageManager.getInstance().getLocalImage();
        if (localImage == null)
          return; // Should not happen either

        ImageDecoder
          .getInstance()
          .loadImage(
            workingImage.path,
            null,
            new ImageDecoder.DecodingListener() {
              @Override
              public void onStatus(STATUS status, @Nullable Bitmap bitmap, @Nullable String errorMessage) {
                if (status == STATUS.SUCCESS) {
                  ByteArrayOutputStream stream = new ByteArrayOutputStream();
                  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                  String name = UUID.randomUUID().toString() + ".jpg";
                  Storage
                    .getInstance()
                    .upload(
                      name,
                      stream.toByteArray(),
                      new Storage.UploadStatusListener() {
                        @Override
                        public void onStatus(STATUS status, @Nullable Storage.UploadData data, @Nullable Exception exception) {
                          if (status == STATUS.SUCCESS) {
                            Map<String, Object> photo = new HashMap<>();
                            photo.put("user_id", FirebaseAuth.getInstance().getUid());
                            photo.put("uri", data.uri);
                            photo.put("text", text);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("photos")
                              .add(photo)
                              .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                  Navigation
                                    .findNavController(getActivity(), R.id.navhost)
                                    .popBackStack(R.id.navHomePage, true);
                                }
                              });

                          }
                        }
                      }
                    );
                }
              }
            }
          );
      }
    });

    final ImageView preview = view.findViewById(R.id.publishPagePreviewImage);
    WorkingImageManager
      .getInstance()
      .getLiveData()
      .observe(getViewLifecycleOwner(), new Observer<WorkingImage>() {
        @Override
        public void onChanged(WorkingImage workingImage) {
          if (workingImage == null)
            return;

          ImageDecoder
            .getInstance()
            .loadImage(
              workingImage.path,
              DECODING_OPTIONS,
              new ImageDecoder.DecodingListener() {
                @Override
                public void onStatus(STATUS status, @Nullable Bitmap bitmap, @Nullable String errorMessage) {
                  if (status == STATUS.SUCCESS) {
                    preview.setImageBitmap(bitmap);
                  }
                }
              }
            );
        }
      });

    return view;
  }
}
