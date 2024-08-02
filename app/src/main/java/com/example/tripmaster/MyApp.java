package com.example.tripmaster;

import android.app.Application;

import com.example.tripmaster.Utils.FireBaseOperations;

public class MyApp extends Application {

    //TODO: fix vars name in xml
    //TODO; check bugs deserialize
    @Override
    public void onCreate() {
        super.onCreate();
        FireBaseOperations.getInstance();

    }
}
