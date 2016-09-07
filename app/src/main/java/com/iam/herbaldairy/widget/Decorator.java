package com.iam.herbaldairy.widget;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public final class Decorator {

    private Decorator() {}

    public static final int BLACK =                         0xFF000000;
    public static final int LAVKA_RED =                     0xFFA21D20;
    public static final int WHITE =                         0xFFFFFFFF;
    public static final int WHITE_TRANSPARENT_65 =          0xA3FFFFFF;
    public static final int WHITE_TRANSPARENT_75 =          0xC0FFFFFF;
    public static final int WHITE_TRANSPARENT_80 =          0xCCFFFFFF;
    public static final int WHITE_TRANSPARENT_100 =         0x00FFFFFF;
    public static final int GRAY_50 =                       0xFF888888;
    public static final int ORANGE =                        0xFFED5F07;

    public static final int CALDROID_GRAY_TEXT =            0xFFA4A4A4;
    public static final int CALDROID_TEXT =                 0xFF333333;
    public static final int CALDROID_SELECTED_DAY_TEXT =    WHITE;
    public static final int CALDROID_ALL_AVAILABLE =        LAVKA_RED;
    public static final int CALDROID_PART_AVAILABLE =       ORANGE;
    public static final int CALDROID_SELECTED_DAY =         0xFF1E1E1E;

    public static final int PROFILE_GRADIENT_TOP_COLOR =    0xFF2D5E96;
    public static final int PROFILE_GRADIENT_BOTTOM_COLOR = 0xFF288F8C;

    public static final int COMMENT_TIME_COLOR =            0xFF999999;

    public static final int DRAWER_COLOR =                  0xFF1C1C1C;
    public static final int DRAWER_OPENED_COLOR =           0xFF131313;

    public static final int SLIDER_SHADOW_COLOR =           0xFF262626;

    public static final int ND_PROD_MONTH =                 0xFF1C1C1C;
    public static final int ND_PROD_WEEK_DAY =              0xFF444444;

    public static final int POPULAR_PRODS_HEADER =          0xFF333333;

    public static final int TEXT_ALMOST_BLACK =             0xFF333333;

    public static final int PORTION_SPINNER_STROKE_COLOR =  0xFF777777;
    public static final int PORTION_SPINNER_TEXT_COLOR =    0xFF1C1C1C;

    public static final int ACTION_BAR_TITLE_COLOR =        0xFF333333;
    public static final int ACTION_BAR_SUBTITLE_COLOR =     0xFF777777;

    public static final int RECYCLER_DIVIDER =              0xFFAAAAAA;

    public static final int NOT_ACTIVE_TEXT =               0xFFAAAAAA;
    public static final int ACTIVE_TEXT =                   0xFF333333;

    public static final int PROD_FRAGMENT_LINES_COLOR =     0xFFAAAAAA;
    public static final int PROD_FRAGMENT_DIVIDER_COLOR =   0xFFF3F3F3;

    public static final int PROFILE_GRADIENT_TOP =          0xFF3264A3;
    public static final int PROFILE_GRADIENT_BOTTOM =       0xFF2E8F86;

    public static final int PRODUCT_INCR_BORDER =           0xFF777777;

    public static final int CHECKBOX_BORDER =               0xFFCCCCCC;

    public static final int SWITCHER_BORDER =               0xFFCCCCCC;

    public static final int TEXT_ORDERS_TYPE_BACKGROUND =   0xFFF3F3F3;
    public static final int TEXT_ORDERS_TYPE_STROKE =       0xFFAAAAAA;

    public static final int RLMP = RelativeLayout.LayoutParams.MATCH_PARENT;
    public static final int RLWC = RelativeLayout.LayoutParams.WRAP_CONTENT;
    public static final int LLMP = LinearLayout.LayoutParams.MATCH_PARENT;
    public static final int LLWC = LinearLayout.LayoutParams.WRAP_CONTENT;
    public static final int VGMP = ViewGroup.LayoutParams.WRAP_CONTENT;

    private static int statusBarHeight;
    private static int widthPixels;
    private static int heightPixels;
    private static int appHeight;
    private static float dencity;

    synchronized public static void init(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        dencity = metrics.density;
        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        appHeight = heightPixels - statusBarHeight;
    }

    public static float getpixels(int dp){
        int px = (int) (dp * dencity + 0.5f);
        return px;

    }

    synchronized public static int getScreenWidth() {
        return widthPixels;
    }

    synchronized public static int getAppHeight() {
        return appHeight;
    }

    synchronized public static int getScreenHeight() {
        return heightPixels;
    }

    synchronized public static int getHeightBasedOnIPhone960(int pictureHeight) {
        float relative = (float) heightPixels / 960;
        return (int)(pictureHeight * relative);
    }

    public static void setSquareSizeAndMargins(View v, int h, int ml, int mt, int mr, int mb) {
        v.getLayoutParams().height = getHeightBasedOnIPhone960(h);
        v.getLayoutParams().width = v.getLayoutParams().height;
        setMargins(v, ml, mt, mr, mb);
    }

    public static void setSquareSizeAndPadding(View v, int h, int ml, int mt, int mr, int mb) {
        v.getLayoutParams().height = getHeightBasedOnIPhone960(h);
        v.getLayoutParams().width = v.getLayoutParams().height;
        setPadding(v, ml, mt, mr, mb);
    }

    public static void setPadding(View v, int ml, int mt, int mr, int mb) {
        v.setPadding(
                        getWidthBasedOnIPhone640(ml),
                        getHeightBasedOnIPhone960(mt),
                        getWidthBasedOnIPhone640(mr),
                        getHeightBasedOnIPhone960(mb)
                );
    }

    public static void setMargins(View v, int ml, int mt, int mr, int mb) {
        ((ViewGroup.MarginLayoutParams)v.getLayoutParams())
                .setMargins(
                        getWidthBasedOnIPhone640(ml),
                        getHeightBasedOnIPhone960(mt),
                        getWidthBasedOnIPhone640(mr),
                        getHeightBasedOnIPhone960(mb)
                );
    }

    public static void setRectSize(View v, int w, int h) {
        v.getLayoutParams().height = getHeightBasedOnIPhone960(h);
        v.getLayoutParams().width = getWidthBasedOnIPhone640(w);
    }

    public static void setSquareSize(View v, int a) {
        v.getLayoutParams().height = getHeightBasedOnIPhone960(a);
        v.getLayoutParams().width = getHeightBasedOnIPhone960(a);
    }

    public static void setRectSizeAndMargins(View v, int w, int h, int ml, int mt, int mr, int mb) {
        v.getLayoutParams().height = getHeightBasedOnIPhone960(h);
        v.getLayoutParams().width = getWidthBasedOnIPhone640(w);
        setMargins(v, ml, mt, mr, mb);
    }

    public static void setRectSizeAndPadding(View v, int w,int h, int ml, int mt, int mr, int mb) {
        v.getLayoutParams().height = getHeightBasedOnIPhone960(h);
        v.getLayoutParams().width = getWidthBasedOnIPhone640(w);
        setPadding(v, ml, mt, mr, mb);
    }

    synchronized public static int getWidthBasedOnIPhone640(int pictureWidth) {
        float relative = (float) widthPixels / 640;
        return (int)(pictureWidth * relative);
    }
}