package com.iam.herbaldairy.widget.assets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.iam.herbaldairy.HerbalDairy;

import java.util.Hashtable;

public enum font {

    font133("regular", "133.ttf"),
    font133b("bold", "133b.ttf"),
    font133i("italic", "133i.ttf"),
    font133l("light", "133l.ttf"),
    font133sb("semibold", "133sb.ttf");

    private static final Hashtable<String, Typeface> cache = new Hashtable<>();

    font(String style, String resource) {
        this.style = style;
        this.resource = resource;
    }

    public Typeface typeface() {
        return get(HerbalDairy.context, resource);
    }

    private String resource;
    private String style;


    private static Typeface get(Context c, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(),
                            assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    Log.e("Typeface", "Could not get typeface '" + assetPath
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }
}

