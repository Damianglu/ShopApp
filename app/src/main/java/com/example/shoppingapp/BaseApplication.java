package com.example.shoppingapp;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
