package com.iam.herbaldairy;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;

public class HerbalDairy extends Application {

    public static Context context;
    public static AssetManager assetManager;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        assetManager = getAssets();
    }
}
