package com.iam.herbaldairy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.iam.herbaldairy.R;

import java.util.ArrayList;

public class Divider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private Context context;
    boolean hide = false;
    private int[] disabled;
    private int[] leftMargined;

    public Divider(Context context, int resource, int[] disabled, int[] leftMargined) {
        this.disabled = disabled;
        this.leftMargined = leftMargined;
        this.context = context;
//        mDivider = new ColorDrawable(Decorator.RECYCLER_DIVIDER);
        mDivider = context.getResources().getDrawable(resource);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            super.onDrawOver(c, parent, state);
            return;
        }

        mDivider = hide ? new ColorDrawable(Decorator.WHITE_TRANSPARENT_100) : mDivider;
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int[] childs = exclude(disabled, parent.getChildCount());
        final int childCount = childs.length;

        for (int i = 0; i < childCount; ++i) {
            final View child = parent.getChildAt(childs[i]);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int size = mDivider.getIntrinsicHeight();
            final int top = (int) (child.getTop() - params.topMargin - size + child.getTranslationY());
            final int bottom = top + size;
            mDivider.setBounds(
                    isInArray(leftMargined, i) ? left + Decorator.getWidthBasedOnIPhone640(32) : left,
                    top,
                    right,
                    bottom
            );
            mDivider.draw(c);

            if (i == childCount - 1) {
                final int newTop = (int) (child.getBottom() + params.bottomMargin + child.getTranslationY());
                final int newBottom = newTop + size;
                mDivider.setBounds(left, newTop, right, newBottom);
                mDivider.draw(c);
            }
        }
    }

    private int[] exclude(int[] ex, int parent) {
        ArrayList<Integer> newA = new ArrayList<>();
        for (int i = 0; i < parent; i++) {
            if (!isInArray(ex, i)) newA.add(i);
        }
        int size = newA.size();
        int a[] = new int[size];
        for (int i = 0; i < size; i++) {
            a[i] = newA.get(i);
        }
        return a;
    }

    private boolean isInArray(int[] ex, int i) {
        for (int a : ex) {
            if (i == a) {
                return true;
            }
        }
        return false;
    }

    public void show(boolean show) {
        hide = !show;
    }
}