package com.android.emoticoncreater.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.utils.FileUtils;
import com.android.emoticoncreater.utils.SDCardUtils;

/**
 * 告诉你个秘密
 */

public class TellTheSecretActivity extends BaseActivity {

    private CoordinatorLayout mRootView;
    private Toolbar mToolbar;

    private String mSavePath;
    private String mTempPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tell_the_secret);

        initData();
        initView();

    }

    private void initData() {
        mSavePath = SDCardUtils.getSDCardDir() + Constants.PATH_SECRET;
        mTempPath = SDCardUtils.getExternalCacheDir(this);

        FileUtils.createdirectory(mSavePath);
        FileUtils.createdirectory(mTempPath);
    }

    private void initView() {
        mRootView = (CoordinatorLayout) findViewById(R.id.rootview);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mToolbar.setTitle("告诉你个秘密");
        setSupportActionBar(mToolbar);
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    };


}
