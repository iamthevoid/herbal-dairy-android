package com.iam.herbaldairy.arch.root;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iam.herbaldairy.R;
import com.iam.herbaldairy.arch.fragments.AbsinthesFragment;
import com.iam.herbaldairy.arch.fragments.AlcoCalcFragment;
import com.iam.herbaldairy.arch.fragments.HerbFragment;
import com.iam.herbaldairy.widget.Decorator;
import com.iam.herbaldairy.widget.NestedLinearLayoutManager;
import com.iam.herbaldairy.widget.assets.font;
import com.iam.herbaldairy.widget.assets.svg;
import com.iam.herbaldairy.widget.text.Text;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MainViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    private int selected = 0;

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

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int onUserClickCounter = 0;

        int holderId;

        View itemView;

        OptionsMain options;

        DrawerAdapter adapter;

        /**
         * Header
         */
        ImageView profile_image;
        ImageView icon;
        TextView name;
        RecyclerView userOptionsList;
        RecyclerView.LayoutManager userOptionsLayoutManager;

        /**
         * Item
         */
        Text textView;
        Text addition;
        ImageView imageView;
        FrameLayout bottomdivider;
        FrameLayout topdivider;

        private Context context;

        public int height;

        public MainViewHolder(View itemView, int ViewType, DrawerAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.itemView = itemView;
            setBaseParams(ViewType);
        }

        public int onBind(int position) {
            context = itemView.getContext();
            if (holderId == DrawerAdapter.TYPE_HEADER) {

            } else if (holderId == DrawerAdapter.TYPE_ITEM) {
                if (position - 1 == selected) ((View) textView.getParent()).setBackgroundColor(context.getResources().getColor(R.color.header_background));
                else ((View)textView.getParent()).setBackgroundColor(context.getResources().getColor(R.color.almost_black));
                textView.setText(OptionsMain.values()[position - 1].name);
                imageView.setImageDrawable(OptionsMain.values()[position - 1].icon);
                topdivider.getLayoutParams().height = Decorator.getHeightBasedOnIPhone960(1);
                options = OptionsMain.values()[position-1];
                itemView.setOnClickListener(this);
            }
            itemView.measure(0, View.MeasureSpec.UNSPECIFIED);
            return itemView.getMeasuredHeight();
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.drawerItemLayout) {
                Fragment fragment = null;
                switch (options) {
                    case herbas:
                        fragment = new HerbFragment();
                        break;
                    case absinthes:
                        fragment = new AbsinthesFragment();
                        break;
                    case calc:
                        fragment = new AlcoCalcFragment();
                        break;
                }
                selected = options.ordinal();
                ((DrawerActivity)context).clearFragmentStack(fragment);
                ((DrawerCallbacks) context).closeDrawer();
                DrawerAdapter.this.notifyDataSetChanged();
            }
        }

        private void setBaseParams(int ViewType) {
            if (ViewType == DrawerAdapter.TYPE_ITEM) {

                textView = (Text) itemView.findViewById(R.id.section_label);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Decorator.getHeightBasedOnIPhone960(18)); // Creating TextView object with the id of textView from item_row.xml
                textView.setTextColor(Decorator.WHITE);

                addition = (Text) itemView.findViewById(R.id.section_addition);
                addition.setTypeface(font.font133sb.typeface());
                Decorator.setMargins(addition, 0, 0, 32, 0);

                bottomdivider = (FrameLayout) itemView.findViewById(R.id.bottomdivider);
                topdivider = (FrameLayout) itemView.findViewById(R.id.topdivider);
                topdivider.setVisibility(View.INVISIBLE);

                Decorator.setMargins(addition, 0, 0, 13, 0);

                imageView = (ImageView) itemView.findViewById(R.id.image);
                imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                Decorator.setSquareSizeAndMargins(imageView, 40, 28, 22, 28, 22);

                itemView.measure(0, View.MeasureSpec.UNSPECIFIED);
                holderId = DrawerAdapter.TYPE_ITEM;
            } else if (ViewType == DrawerAdapter.TYPE_HEADER) {
                profile_image = (ImageView) itemView.findViewById(R.id.circleView);
                Decorator.setSquareSizeAndMargins(profile_image, 96, 16, 28, 16, 28);

                userOptionsList = (RecyclerView) itemView.findViewById(R.id.user_options);
                userOptionsLayoutManager = new NestedLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

                name = (TextView) itemView.findViewById(R.id.name);
                name.setTextSize(TypedValue.COMPLEX_UNIT_PX, Decorator.getHeightBasedOnIPhone960(28));
                ((RelativeLayout.LayoutParams)name.getLayoutParams())
                        .addRule(RelativeLayout.CENTER_VERTICAL, R.id.circleView);

                icon = (ImageView) itemView.findViewById(R.id.icon);
                icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                ((RelativeLayout.LayoutParams)icon.getLayoutParams())
                        .addRule(RelativeLayout.CENTER_VERTICAL, R.id.circleView);

                holderId = DrawerAdapter.TYPE_HEADER;
            }
        }
    }

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

    public interface DrawerCallbacks {
        void closeDrawer();
    }
}