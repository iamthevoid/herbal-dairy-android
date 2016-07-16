package com.iam.herbaldairy.arch.root;

import android.graphics.drawable.Drawable;

import com.iam.herbaldairy.widget.assets.svg;

public enum OptionsMain {
    herbas      ("HERBS", svg.zelenwhite.drawable()),
    absinthes   ("ABSINTHES", svg.productswhite.drawable()),
    recipes     ("RECIPES", svg.orderwhite.drawable()),
    calc        ("ALCO CALC", svg.editwhite.drawable()),
    info        ("INFO", svg.helpwhite.drawable()),
    settings    ("SETTINGS", svg.settingswhite.drawable());

    String name;
    Drawable icon;

    OptionsMain(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }
}