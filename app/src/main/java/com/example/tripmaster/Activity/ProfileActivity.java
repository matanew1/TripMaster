package com.example.tripmaster.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tripmaster.Data.DataManager;
import com.example.tripmaster.Model.UserDB;
import com.example.tripmaster.R;
import com.example.tripmaster.Service.FileStorageService;

public class ProfileActivity extends AppCompatActivity implements IScreenSwitch {

    private static final int PICK_FILE_REQUEST = 1;
    private FileStorageService fileStorageService;
    private Button backButton;
    private ImageView profileImage;
    private TextView fullName, address, email, phone;
    private UserDB currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        fileStorageService = new FileStorageService();

        initViews();
        setUpDataAndListeners();
    }

    private void setUpDataAndListeners() {
        backButton.setOnClickListener(v -> switchScreen());
        profileImage.setOnClickListener(v -> openFilePicker());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            uploadFile(fileUri);
        }
    }

    private void uploadFile(Uri fileUri) {
        fileStorageService.uploadFileImage(fileUri, new FileStorageService.FileUploadCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(ProfileActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                currentUser.setPhotoUrl(String.valueOf(fileUri.getLastPathSegment()));
                DataManager.getInstance().getDatabaseService().updateUser(currentUser);
                // Update the profile image immediately
                Glide.with(ProfileActivity.this)
                        .load(fileUri)
                        .into(profileImage);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ProfileActivity.this, "File upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        backButton = findViewById(R.id.back_button);
        profileImage = findViewById(R.id.profile_image_view);
        fullName = findViewById(R.id.full_name_profile);
        email = findViewById(R.id.email_user);
        currentUser = UserDB.getCurrentUser();

        // TODO: verify why the UserDB doesn't contain the image loaded
        if (currentUser != null) {
            fullName.setText(currentUser.getName());
            email.setText(currentUser.getEmail()); // Set email field

            String profileImageUrl = currentUser.getPhotoUrl();
            if (profileImageUrl != null) {
                Glide.with(this)
                        .load(profileImageUrl)
                        .into(profileImage);
            }
        }
    }

    @Override
    public void switchScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}