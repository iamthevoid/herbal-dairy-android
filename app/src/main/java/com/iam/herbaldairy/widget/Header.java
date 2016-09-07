package com.iam.herbaldairy.widget;

import android.app.Activity;
import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.iam.herbaldairy.R;
import com.iam.herbaldairy.widget.assets.font;
import com.iam.herbaldairy.widget.assets.svg;
import com.iam.herbaldairy.widget.text.Text;
import com.rengwuxian.materialedittext.MaterialEditText;


public class Header extends RelativeLayout implements Animation.AnimationListener {

    private Animation scale_search_animation;

    private boolean searchMode = false;
    private boolean animationInProgress = false;

    private HeaderManipulation callbacks;

    private IconContainer leftSideImage;

    private Text title;
    private Text subtitle;
    private Text textButton;
    private FrameLayout text_holder;

    private FrameLayout shadow;

    private IconContainer rightIcon1;
    private IconContainer rightIcon2;

    private RelativeLayout titleHolder;

    private MaterialEditText search_et;

    private TextWatcher textWatcher;

    public Header(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ((Activity)context).getLayoutInflater().inflate(R.layout.widget_header, this);
        findChildViews();
        visible(true);
    }

    private void findChildViews() {
        scale_search_animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_view_out_to_left);
        scale_search_animation.setAnimationListener(this);
        titleHolder = (RelativeLayout) findViewById(R.id.titleHolder);

        search_et = (MaterialEditText) findViewById(R.id.search_et);
        search_et.setIconLeft(svg.search.drawable());

        leftSideImage = new IconContainer((FrameLayout) findViewById(R.id.lheader), R.id.left_action_image);
        rightIcon1 = new IconContainer((FrameLayout) findViewById(R.id.rheader1), R.id.right_action_image_1);
        rightIcon2 = new IconContainer((FrameLayout) findViewById(R.id.rheader2), R.id.right_action_image_2);

        shadow = (FrameLayout) findViewById(R.id.shadow);

        title = (Text) findViewById(R.id.title);
        title.setTextColor(Decorator.ACTION_BAR_TITLE_COLOR);
        subtitle = (Text)findViewById(R.id.subtitle);
        textButton = (Text) findViewById(R.id.right_text_button);
        text_holder = (FrameLayout) findViewById(R.id.rholdert);
    }

    private void setLeftSideImage(HeaderManipulation callbacks) {
        svg image = callbacks.leftIcon();
        if (image != null) {
            leftSideImage.setVisibility(VISIBLE);
            leftSideImage.setImageDrawable(image);
            leftSideImage.setOnClickListener(callbacks.leftIconAction());
        } else {
            leftSideImage.setVisibility(GONE);
            leftSideImage.setOnClickListener(null);
        }
    }

    public void showShadow(boolean show) {
        shadow.setVisibility(show ? VISIBLE : INVISIBLE);
    }

    private void setTitle(HeaderManipulation callbacks) {
        String titleText = callbacks.headerTitle();
        if (titleText != null) {
            switch (titleText) {
                case "Профиль":
                    title.setTextColor(Decorator.WHITE);
                    break;
                default:
                    title.setTextColor(Decorator.ACTION_BAR_TITLE_COLOR);
                    break;
            }
            title.setTypeface(font.font133sb.typeface());
            title.setVisibility(VISIBLE);
//            Decorator.setMargins(title, 0, 20, 0, 0);
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, Decorator.getHeightBasedOnIPhone960(24));
            if (titleText.length() > 20) {
                titleText = titleText.subSequence(0, 19) + "...";
            }
            title.setText(titleText);
            if (callbacks.headerSubtitle() == null){
                ((LayoutParams)title.getLayoutParams()).addRule(CENTER_IN_PARENT);
            }
            else {
                LayoutParams params = new LayoutParams(Decorator.RLWC, Decorator.RLWC);
                params.addRule(ALIGN_TOP, R.id.lheader);
                params.addRule(CENTER_HORIZONTAL, R.id.lheader);
                title.setLayoutParams(params);
//                Decorator.setMargins(title, 0, 16, 0, 0);
            }
        } else {
            title.setVisibility(GONE);
        }
    }

    private void setSubtitle(HeaderManipulation callbacks) {
        String subtitleText = callbacks.headerSubtitle();
        if (subtitleText != null) {
            subtitle.setVisibility(VISIBLE);
//            Decorator.setMargins(subtitle, 0, 0, 0, 8);
            subtitle.setTextColor(Decorator.ACTION_BAR_SUBTITLE_COLOR);
            subtitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, Decorator.getHeightBasedOnIPhone960(20));
            subtitle.setText(subtitleText);
        } else {
            subtitle.setVisibility(GONE);
        }
    }

    private void setRightIcon1(HeaderManipulation callbacks) {
        svg image = callbacks.rightIcon1();
        if (image != null) {
            rightIcon1.setVisibility(VISIBLE);
            rightIcon1.setImageDrawable(image);
            textButton.setVisibility(GONE);
            rightIcon1.setOnClickListener(callbacks.rightIcon1Action());
        } else {
            rightIcon1.setVisibility(GONE);
            rightIcon1.setOnClickListener(null);
//            invalidateIconListener(rightIcon1, callbacks);
        }
    }

    private void setRightIcon2(HeaderManipulation callbacks) {
        svg image = callbacks.rightIcon2();
        if (image != null) {
            rightIcon2.setVisibility(VISIBLE);
            rightIcon2.setImageDrawable(image);
            rightIcon2.setOnClickListener(callbacks.rightIcon2Action());
            textButton.setVisibility(GONE);
        } else {
            rightIcon2.setOnClickListener(null);
            rightIcon2.setVisibility(GONE);
        }
    }

    private void setTextButton(HeaderManipulation callbacks) {
        String text = callbacks.headerTextButton();
        if (text != null) {
            switch (text) {
                case "ОФОРМИТЬ":
                case "РЕГИСТРАЦИЯ":
                    textButton.setTextColor(Decorator.LAVKA_RED);
                    break;
                default:
                    textButton.setTextColor(Decorator.BLACK);
                    break;
            }
            textButton.setVisibility(VISIBLE);
            textButton.setText(text);
            rightIcon1.setVisibility(GONE);
            rightIcon2.setVisibility(GONE);
            textButton.setOnClickListener(callbacks.textAction());
            text_holder.setOnClickListener(callbacks.textAction());
        } else {
            textButton.setOnClickListener(null);
            text_holder.setOnClickListener(null);
            textButton.setVisibility(GONE);
        }
    }

    public void visible(boolean visible) {
        if (visible) {
            visibleBackGround();
        } else {
            invisibleBackGround();
        }
    }

    private void invisibleBackGround () {
        setBackgroundColor(Decorator.WHITE_TRANSPARENT_100);
        shadow.setVisibility(INVISIBLE);
    }

    private void visibleBackGround () {
        setBackgroundColor(Decorator.WHITE);
        shadow.setVisibility(VISIBLE);
    }

    public void decorateHeader(HeaderManipulation callbacks) {
        this.callbacks = callbacks;
        if (searchMode) disableSearch();
        setTitle(callbacks);
        setSubtitle(callbacks);
        setLeftSideImage(callbacks);
        setRightIcon1(callbacks);
        setRightIcon2(callbacks);
        setTextButton(callbacks);
    }

    public void setOnTextChangedListenerForSearch(TextWatcher textWatcher) {
        if (this.textWatcher == null) {
            this.search_et.addTextChangedListener(textWatcher);
        } else {
            this.search_et.removeTextChangedListener(this.textWatcher);
            this.textWatcher = null;
            this.search_et.addTextChangedListener(textWatcher);
        }
        this.textWatcher = textWatcher;
    }

    public EditText getSearchEditText() {
        return search_et;
    }

    public void switchSearch() {
        if (searchMode && search_et.getText().length() > 0) {
            search_et.setText("");
        } else if (searchMode && !animationInProgress) {
            disableSearch();
//            searchMode = false;
        }
        else if (!searchMode && !animationInProgress)
        {
            enableSearch();
//            searchMode = true;
        }
    }

    private void disableSearch() {
        titleHolder.setVisibility(VISIBLE);
        search_et.startAnimation(scale_search_animation);
    }

    private void enableSearch() {
        search_et.setVisibility(VISIBLE);

        titleHolder.startAnimation(scale_search_animation);

    }

    @Override
    public void onAnimationStart(Animation animation) {
        animationInProgress = true;
        if (searchMode) {
            rightIcon2.setImageDrawable(callbacks.rightIcon2());
        } else {
            rightIcon2.setImageDrawable(svg.x);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        animationInProgress = false;
        if (!searchMode) {
            titleHolder.setVisibility(INVISIBLE);
            search_et.setVisibility(VISIBLE);
            searchMode = true;
        }
        else {
            search_et.setVisibility(INVISIBLE);
            titleHolder.setVisibility(VISIBLE);
            searchMode = false;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private class IconContainer {

        FrameLayout frameLayout;
        ImageView imageView;
        svg drawable;

        IconContainer(FrameLayout frameLayout, int image_id) {
            this.frameLayout = frameLayout;
//            Decorator.setRectSizeAndPadding(this.frameLayout, 89, 89, 16, 24, 4, 24);
            imageView = (ImageView) frameLayout.findViewById(image_id);
            imageView.setLayerType(LAYER_TYPE_SOFTWARE, null);
//            Decorator.setSquareSizeAndMargins(imageView, 32, 20, 28, 8, 28);
        }

        public void setImageDrawable(svg drawable) {
            this.drawable = drawable;
            imageView.setImageDrawable(drawable.drawable());
        }

        public void setVisibility(int visibility) {
            frameLayout.setVisibility(visibility);
            imageView.setVisibility(visibility);
        }

        public void setOnClickListener(OnClickListener onClickListener) {
            frameLayout.setOnClickListener(onClickListener);
            imageView.setOnClickListener(onClickListener);
        }

        public svg getSvg() {
            return drawable;
        }
    }

    public interface SearchInHeader {
        void switchSearch();
        void setOnTextListener(TextWatcher textWatcher);
        EditText getSearch();
    }


    public interface FragmentDataSender {
        void onFragmentOpen(HeaderManipulation fragment);
    }

    public interface ToggleClicker {
        void onToggleClick();
    }

    public interface ShadowCast {
        void showShadow(boolean show);
    }

    public interface HeaderManipulation {
        svg leftIcon();
        OnClickListener leftIconAction();
        svg rightIcon1();
        OnClickListener rightIcon1Action();
        svg rightIcon2();
        OnClickListener rightIcon2Action();
        String headerTextButton();
        OnClickListener textAction();
        String headerTitle();
        String headerSubtitle();
    }
}
