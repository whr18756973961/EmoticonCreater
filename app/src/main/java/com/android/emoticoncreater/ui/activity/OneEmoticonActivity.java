package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.ui.adapter.EmoticonFragmentPagerAdapter;
import com.android.emoticoncreater.utils.ImageDataHelper;

/**
 * 一个表情
 */

public class OneEmoticonActivity extends BaseActivity {

    private ViewPager vpPicture;

    public static void show(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, OneEmoticonActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_one_emoticon;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setToolbarBackEnable();
        setToolbarTitle("一个表情");

        vpPicture = (ViewPager) findViewById(R.id.vp_picture);
        vpPicture.setAdapter(new EmoticonFragmentPagerAdapter(getSupportFragmentManager(), ImageDataHelper.EMOTICON_TITLES));

        setToolbarScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
        setTabMode(TabLayout.MODE_SCROLLABLE);
        setupWithViewPager(vpPicture);
    }

}
