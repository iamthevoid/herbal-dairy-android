package com.iam.herbaldairy.arch.root;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
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
import com.iam.herbaldairy.widget.text.Text;
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
            ((DrawerActivity)context).clearFragmentStack(fragment);
            ((DrawerCallbacks) context).closeDrawer();
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