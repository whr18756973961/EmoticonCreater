package com.android.emoticoncreater.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.MDBaseActivity;
import com.android.emoticoncreater.ui.fragment.SettingsFragment;

/**
 * 设置
 */

public class SettingsActivity extends MDBaseActivity {

    private static final String IS_RECREATE = "is_recreate";
    private static final String FRAGMENT_TAG = "mFragment";

    private CoordinatorLayout mRootLayout;

    private SettingsFragment mFragment;
    private boolean recreated = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarTitle(R.string.settings);
        setToolbarBackEnable();

        mRootLayout = (CoordinatorLayout) findViewById(R.id.root_layout);

        if (savedInstanceState != null) {
            recreated = savedInstanceState.getBoolean(IS_RECREATE);
        }

        if (recreated) {
            mRootLayout.post(this::startAnimation);
            setResult(Activity.RESULT_OK);
            recreated = false;
        }

        Fragment temp = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (temp == null) {
            mFragment = new SettingsFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mFragment, FRAGMENT_TAG)
                    .commit();
        } else {
            mFragment = (SettingsFragment) temp;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//      finish setting activity to avoid  java.io.NotSerializableException
//      com.thirtydegreesray.openhub.ui.widget.colorChooser.ColorChooserPreference
//      android.os.Parcel.writeSerializable(Parcel.java:1761)
        if (recreated) {
            super.onSaveInstanceState(outState);
            outState.putBoolean(IS_RECREATE, true);
        } else {
            finish();
        }
    }

    public void onRecreate() {
        recreated = true;
        recreate();
    }

    private void startAnimation() {
        int cx = mRootLayout.getWidth() / 2;
        int cy = mRootLayout.getHeight() / 2;
        float finalRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(mRootLayout, cx, cy, 0f, finalRadius);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        mRootLayout.setVisibility(View.VISIBLE);
        anim.start();
    }

    public static void show(@NonNull Activity activity, int requestCode) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }
}
