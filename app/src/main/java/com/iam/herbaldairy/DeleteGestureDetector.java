package com.iam.herbaldairy;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class DeleteGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private Animation slide_cart_item_out_left;
    private Animation slide_cart_item_out_right;
    private Animation slide_cart_item_in_left;
    private Animation slide_cart_item_in_right;

    private Context context;

    private View swipeable;
    //        private View deleteL;
    private View deleter;

    private boolean animationRunning;

    public DeleteGestureDetector(Context context, View swipeable, View deleter) {
        this.context = context;
        this.swipeable = swipeable;
//            this.deleteL = deleteL;
        this.deleter = deleter;
        setAnims();
    }

    private void setAnims() {
        slide_cart_item_out_left = AnimationUtils.loadAnimation(context, R.anim.slide_deleteable_item_out_left);
        slide_cart_item_out_left.setAnimationListener(stopOnSlide());

        slide_cart_item_out_right = AnimationUtils.loadAnimation(context, R.anim.slide_deleteable_item_out_right);
        slide_cart_item_out_right.setAnimationListener(stopOnSlide());

        slide_cart_item_in_left = AnimationUtils.loadAnimation(context, R.anim.slide_deleteable_item_in_left);
        slide_cart_item_in_left.setAnimationListener(stopOnSlide());

        slide_cart_item_in_right = AnimationUtils.loadAnimation(context, R.anim.slide_deleteable_item_in_right);
        slide_cart_item_in_right.setAnimationListener(stopOnSlide());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!animationRunning) {
            try {
                if (e2.getX() < e1.getX()) { // SWIPE RIGHT TO LEFT
                    if (slide_cart_item_out_right.getFillAfter()) {
                        slide_cart_item_out_right.setFillAfter(false);
                        swipeable.startAnimation(slide_cart_item_in_left);
                    } else if (!slide_cart_item_out_left.getFillAfter()) {
                        slide_cart_item_out_left.setFillAfter(true);
                        swipeable.startAnimation(slide_cart_item_out_left);
                    }
                } else { // SWIPE LEFT TO RIGHT
                    if (slide_cart_item_out_left.getFillAfter()) {
                        slide_cart_item_out_left.setFillAfter(false);
                        swipeable.startAnimation(slide_cart_item_in_right);
                    } else if (!slide_cart_item_out_right.getFillAfter()) {
                        slide_cart_item_out_right.setFillAfter(true);
                        swipeable.startAnimation(slide_cart_item_out_right);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    private Animation.AnimationListener stopOnSlide() {
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationRunning = true;
                swipeable.bringToFront();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationRunning = false;
                if (slide_cart_item_out_left.getFillAfter()) {
                    deleter.bringToFront();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };
    }
}