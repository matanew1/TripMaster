package com.example.tripmaster;

import android.app.Application;

import com.example.tripmaster.Utils.FireBaseOperations;

public class MyApp extends Application {

    //TODO: GENERAL.xml -> Add color for the buttons
    //TODO: create profile page
    //TODO: add animation + improve ui/ux of the application

    @Override
    public void onCreate() {
        super.onCreate();
        FireBaseOperations.getInstance();

    }
}
