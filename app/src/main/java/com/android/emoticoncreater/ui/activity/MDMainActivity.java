package com.android.emoticoncreater.ui.activity;

import android.os.Bundle;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.MDBaseDrawerActivity;

/**
 * Material Design MainActivity
 */

public class MDMainActivity extends MDBaseDrawerActivity {

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
    protected boolean navigationItemSelected(int itemId) {
        switch (itemId) {
            case R.id.nav_my_picture:

                break;
            case R.id.nav_three_send:

                break;
            default:
                break;
        }
        return true;
    }
}
