package com.iam.herbaldairy.arch.root;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.iam.herbaldairy.R;
import com.iam.herbaldairy.arch.db.DBCache;
import com.iam.herbaldairy.arch.fragments.HerbsFragment;
import com.iam.herbaldairy.arch.ticker.InfusionTickerService;
import com.iam.herbaldairy.entities.Absinthe;
import com.iam.herbaldairy.widget.AddHerbDialog;
import com.iam.herbaldairy.widget.Decorator;
import com.iam.herbaldairy.widget.Divider;
import com.iam.herbaldairy.widget.Header;

public class DrawerActivity extends AppCompatActivity implements DrawerAdapter.DrawerCallbacks,
        Header.FragmentDataSender,
        Header.ToggleClicker,
        Header.SearchInHeader,
        ProgressBarHolder,
        AddHerbDialog.Container {

    Header header;

    CircularProgressView progressBar;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;
    LinearLayout drawerHolder;

    ImageView backround;

    FrameLayout container;
    FrameLayout dialogContainer;

    ActionBarDrawerToggle mDrawerToggle;

    DisplayMetrics metrics;

    RelativeLayout frame;

    float lastTranslate = 0f;
    int bpCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Decorator.init(this);

        restoreUserData();
        startService(new Intent(this, InfusionTickerService.class));

        setContentView(R.layout.drawer_activity);

        metrics = new DisplayMetrics();

        dialogContainer = (FrameLayout) findViewById(R.id.dialogContainer);

        progressBar = (CircularProgressView) findViewById(R.id.progressBar);

        backround = (ImageView) findViewById(R.id.background);
        backround.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        container = (FrameLayout) findViewById(R.id.container);
//

        drawerHolder = (LinearLayout) findViewById(R.id.drawer);
//        drawerHolder.getLayoutParams().width = Decorator.getWidthBasedOnIPhone640(544);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        ViewTreeObserver vto = Drawer.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //You should be able to get the width and height over here.

                Drawer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        frame = (RelativeLayout) findViewById(R.id.content);
        frame.getLayoutParams().height = metrics.heightPixels;// -
//                Decorator.getHeightBasedOnIPhone960(89);

        header = (Header) findViewById(R.id.actionBar);
        header.bringToFront();
//        Decorator.setRectSize(header, 640, 89);

        mAdapter = new DrawerAdapter();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new Divider(this, R.drawable.black_divider, new int[]{}, new int[]{0,0}));
        mRecyclerView.setAdapter(mAdapter);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                Drawer,
                R.string.app_name,
                R.string.app_name
        ) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                float moveFactor = (drawerView.getWidth() * slideOffset);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    frame.setTranslationX(moveFactor);
                }
                else {
                    TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                    anim.setDuration(0);
                    anim.setFillAfter(true);
                    frame.startAnimation(anim);

                    lastTranslate = moveFactor;
                }
            }
        };

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new HerbsFragment(), HerbsFragment.class.getCanonicalName())
                .commit();

        Drawer.setDrawerListener(mDrawerToggle);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        container.getLayoutParams().height =
                metrics.heightPixels;

    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            bpCount = 0;
            getSupportFragmentManager().popBackStack();
        } else {
            bpCount++;
            switch (bpCount) {
                case 1:
                    Toast.makeText(
                            getApplicationContext(),
                            "Для выхода нажмите \"Назад\" ещё раз",
                            Toast.LENGTH_SHORT
                    ).show();
                    break;
                case 2:
                    super.onBackPressed();
                    break;
            }
        }
    }

    @Override
    public void closeDrawer() {
        Drawer.closeDrawer(Gravity.LEFT);
    }

    @Override
    public void onFragmentOpen(Header.HeaderManipulation fragment) {
        if (fragment != null) header.decorateHeader(fragment);
    }

    @Override
    public void onToggleClick() {
        if (Drawer.isDrawerOpen(drawerHolder)) {
            Drawer.closeDrawer(drawerHolder);
        } else {
            Drawer.openDrawer(drawerHolder);
        }
    }

    @Override
    public void addView(View view) {
        dialogContainer.removeAllViews();
        dialogContainer.addView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveUserData();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void clearFragmentStack(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null) {
                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                    onBackPressed();
                }

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, fragment, fragment.getClass().getCanonicalName())
                        .commit();
            }
        } else {
            Toast.makeText(this, "этой страницы пока нет", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        saveUserData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreUserData();
    }

    private void saveUserData() {
        String herbs = DBCache.getInstance().userHerbs().writeToPreferences(getApplicationContext());
        String absinthes = DBCache.getInstance().userAbsinthes().writeToPreferences(getApplicationContext());
//        new Backup(herbs, absinthes).postToDB();
    }

    private void restoreUserData() {
        DBCache.getInstance().userHerbs().readFromPreferences(getApplicationContext());
        DBCache.getInstance().userAbsinthes().readFromPreferences(getApplicationContext());
    }

    @Override
    public void switchSearch() {
        header.switchSearch();
    }

    @Override
    public void setOnTextListener(TextWatcher textWatcher) {
        header.setOnTextChangedListenerForSearch(textWatcher);
    }

    @Override
    public EditText getSearch() {
        return header.getSearchEditText();
    }

    @Override
    public void showProgres() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    public Header getHeader() {
        return header;
    }
}
