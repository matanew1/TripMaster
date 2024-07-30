package com.example.tripmaster.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tripmaster.Activity.TripViewActivity;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;
import com.example.tripmaster.Service.FileStorageService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@SuppressLint("NotifyDataSetChanged")
public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private final ArrayList<Trip> tripList;
    private final Context context;
    private final FileStorageService fileStorageService;

    public TripAdapter(Context context, ArrayList<Trip> tripList) {
        this.context = context;
        this.tripList = tripList;
        this.fileStorageService = new FileStorageService();
    }

    public void updateTrips(ArrayList<Trip> filteredTrips) {
        tripList.clear();
        tripList.addAll(filteredTrips);
        notifyDataSetChanged();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        public ImageView tripImage;
        public TextView tripTitle;
        public TextView tripLocation;
        public TextView tripDate;
        public TextView tripAverageRating;

        public TripViewHolder(View view) {
            super(view);
            tripImage = view.findViewById(R.id.trip_image);
            tripTitle = view.findViewById(R.id.trip_title);
            tripLocation = view.findViewById(R.id.trip_location);
            tripDate = view.findViewById(R.id.trip_date);
            tripAverageRating = view.findViewById(R.id.trip_rating_avg);
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
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;

        String filePath = "uploads/" + firebaseUser.getUid() + "/" + trip.getFileImgName();
        fileStorageService.downloadFileImage(filePath, new FileStorageService.FileDownloadCallback() {
            @Override
            public void onSuccess(Uri fileUri) {
                Glide.with(context)
                        .load(fileUri)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(holder.tripImage);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("TripAdapter", "Image download failed: ", e);
                holder.tripImage.setImageResource(R.drawable.def_img); // Set a placeholder image
            }
        });

        holder.tripTitle.setText(trip.getTitle());
        holder.tripLocation.setText(trip.getLocation());
        holder.tripDate.setText(trip.getStartDate());
        holder.tripAverageRating.setText("Average rating: " + trip.getAverageRating());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TripViewActivity.class);
            intent.putExtra("clicked_trip", trip);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}
