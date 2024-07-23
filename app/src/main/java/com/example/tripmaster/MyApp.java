package com.example.tripmaster;

import android.app.Application;

import com.example.tripmaster.Utils.FireBaseOperations;

public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        FireBaseOperations.getInstance();

    }
}
