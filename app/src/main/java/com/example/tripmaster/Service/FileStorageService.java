package com.example.tripmaster.Service;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class FileStorageService {

    private final StorageReference storageRef;

    public FileStorageService() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public void uploadFileImage(Uri fileUri, final FileUploadCallback callback) {
        if (fileUri == null) {
            if (callback != null) callback.onFailure(new IllegalArgumentException("File URI is null"));
            return;
        }
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            if (callback != null) callback.onFailure(new IllegalStateException("User not authenticated"));
            return;
        }

        String userId = currentUser.getUid();
        StorageReference fileRef = storageRef.child("uploads/" + userId + "/" + fileUri.getLastPathSegment());

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    if (callback != null) callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    if (callback != null) callback.onFailure(e);
                });
    }

    public void downloadFileImage(String filePath, FileDownloadCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            if (callback != null) callback.onFailure(new IllegalStateException("User not authenticated"));
            return;
        }

        StorageReference fileRef = storageRef.child(filePath);

        File localFile;
        try {
            localFile = File.createTempFile("images", ".jpeg");
        } catch (IOException e) {
            if (callback != null) callback.onFailure(e);
            return;
        }

        fileRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    Uri localFileUri = Uri.fromFile(localFile);
                    if (callback != null) callback.onSuccess(localFileUri);
                })
                .addOnFailureListener(e -> {
                    if (callback != null) callback.onFailure(e);
                });
    }

    public interface FileUploadCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface FileDownloadCallback {
        void onSuccess(Uri fileUri);
        void onFailure(Exception e);
    }
}
