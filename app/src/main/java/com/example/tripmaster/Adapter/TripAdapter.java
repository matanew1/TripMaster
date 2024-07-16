package com.example.tripmaster.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tripmaster.Model.Trip;
import com.example.tripmaster.R;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private final List<Trip> tripList;

    public TripAdapter(List<Trip> tripList) {
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
                .inflate(R.layout.fragment_trip_item, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        Glide.with(holder.itemView.getContext()).load(trip.getImageResId()).into(holder.tripImage);
        holder.tripTitle.setText(trip.getTitle());
        holder.tripLocation.setText(trip.getLocation());
        holder.tripDate.setText(trip.getDate());
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}

