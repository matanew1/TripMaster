package com.example.tripmaster.Service;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FileStorageService {

    private final StorageReference storageRef;

    public FileStorageService() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public void uploadFile(Uri fileUri, final FileUploadCallback callback) {
        if (fileUri == null) {
            if (callback != null) callback.onFailure(new IllegalArgumentException("File URI is null"));
            return;
        }

        StorageReference fileRef = storageRef.child("uploads/" + fileUri.getLastPathSegment());

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    if (callback != null) callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    if (callback != null) callback.onFailure(e);
                });
    }

    public interface FileUploadCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
}
