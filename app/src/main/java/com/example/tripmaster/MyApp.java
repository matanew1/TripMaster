package com.example.tripmaster;

import android.app.Application;

import com.example.tripmaster.Utils.FireBaseOperations;

public class MyApp extends Application {

    //TODO: fix vars name in xml
    //TODO; check bugs deserialize
    //TODO: added timeline view for a trip
    //TODO: improve design in the UI
    @Override
    public void onCreate() {
        super.onCreate();
        FireBaseOperations.getInstance();

    }
}
