package com.iam.herbaldairy.arch.root;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.iam.herbaldairy.R;

public class DrawerAdapter extends RecyclerView.Adapter<MainViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = ((Activity)parent.getContext()).getLayoutInflater().inflate(R.layout.drawer_list_item, parent, false); //Inflating the layout
            return new MainViewHolder(v, viewType, this);
        } else if (viewType == TYPE_HEADER) {
            View v = ((Activity)parent.getContext()).getLayoutInflater().inflate(R.layout.drawer_header_item, parent, false);
            return new MainViewHolder(v,viewType, this);
        }
        throw new RuntimeException("Unsupported view type");
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return OptionsMain.values().length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}