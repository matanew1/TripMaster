package com.example.tripmaster.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tripmaster.Activity.TripViewActivity;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private final ArrayList<Trip> tripList;
    private final Context context;

    public TripAdapter(Context context, ArrayList<Trip> tripList) {
        this.context = context;
        this.tripList = tripList;
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        public ImageView tripImage;
        public TextView tripTitle;
        public TextView tripLocation;
        public TextView tripDate;

        public TripViewHolder(View view) {
            super(view);
            tripImage = view.findViewById(R.id.trip_image);
            tripTitle = view.findViewById(R.id.trip_title);
            tripLocation = view.findViewById(R.id.trip_location);
            tripDate = view.findViewById(R.id.trip_date);
        }
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_item, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);

        // Get the Firebase Storage reference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child("uploads/" + trip.getFileImgName());

        // Create a local file in the cache directory
        File localFile;
        try {
            localFile = File.createTempFile("images", ".jpeg", context.getCacheDir());
        } catch (IOException e) {
            Log.e("TripAdapter", "Error creating local file", e);
            holder.tripImage.setImageResource(R.drawable.def_img); // Set a placeholder image
            return;
        }

        // Download the file
        fileRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            // File downloaded successfully
            Uri localFileUri = Uri.fromFile(localFile);
            Glide.with(holder.itemView.getContext())
                    .load(localFileUri)
                    .into(holder.tripImage);
        }).addOnFailureListener(exception -> {
            // Download failed
            Log.e("TripAdapter", "Image download failed: ", exception);
            holder.tripImage.setImageResource(R.drawable.def_img); // Set a placeholder image
        });

        holder.tripTitle.setText(trip.getTitle());
        holder.tripLocation.setText(trip.getLocation());
        holder.tripDate.setText(trip.getStartDate());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TripViewActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}
