package com.iam.herbaldairy;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;

import com.iam.herbaldairy.entities.Absinth;
import com.iam.herbaldairy.entities.Herb;

public class HerbalDairy extends Application {

    public static Context context;
    public static AssetManager assetManager;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        assetManager = getAssets();
        Herb.readFromPreferences(context);
        Absinth.readFromPreferences(context);
    }
}
