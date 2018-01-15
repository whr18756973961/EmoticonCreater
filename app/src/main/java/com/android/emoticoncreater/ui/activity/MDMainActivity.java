package com.android.emoticoncreater.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.MDBaseDrawerActivity;

/**
 * Material Design MainActivity
 */

public class MDMainActivity extends MDBaseDrawerActivity {

    private static final int REQUEST_CODE_SETTINGS = 100;

    @Override
    protected int getContentView() {
        return R.layout.activity_md_main;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setStartDrawerEnable(true);
        super.initView(savedInstanceState);
        initMenu(R.menu.activity_main_drawer);
        setToolbarTitle(R.string.my_picture);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SETTINGS && resultCode == RESULT_OK) {
            recreate();
        }
    }

    @Override
    protected void navigationItemSelected(int itemId) {
        switch (itemId) {
            case R.id.nav_my_picture:

                break;
            case R.id.nav_three_send:

                break;
            case R.id.nav_settings:
                SettingsActivity.show(MDMainActivity.this, REQUEST_CODE_SETTINGS);
                break;
            case R.id.nav_about:

                break;
            default:
                break;
        }
    }
}
