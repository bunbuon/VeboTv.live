package com.hoanmy.football;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import io.paperdb.Paper;

public class App extends MultiDexApplication implements ComponentCallbacks2 {
    private static String deviceId;

    private static App mInstance;

    public static App getmInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(getApplicationContext());
    }
}
