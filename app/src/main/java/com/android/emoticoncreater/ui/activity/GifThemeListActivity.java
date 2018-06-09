package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.model.GifTheme;
import com.android.emoticoncreater.ui.adapter.GifThemeListAdapter;
import com.android.emoticoncreater.ui.adapter.IOnListClickListener;
import com.android.emoticoncreater.ui.adapter.OnListClickListener;
import com.android.emoticoncreater.utils.AssetsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * GIF 图片列表
 */
public class GifThemeListActivity extends BaseActivity {

    private RecyclerView rvThemeList;

    private List<GifTheme> mThemeList;
    private GifThemeListAdapter mThemeAdapter;

    public static void show(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, GifThemeListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_gif_theme_list;
    }

    @Override
    protected void initData() {
        super.initData();

        mThemeList = new ArrayList<>();
        final String jsonText = AssetsUtil.getAssetsTxtByName(this, "Gif_Theme.json");
        if (!TextUtils.isEmpty(jsonText)) {
            final List<GifTheme> themeList = JSON.parseArray(jsonText, GifTheme.class);
            if (themeList != null && !themeList.isEmpty()) {
                mThemeList.addAll(themeList);
            }
        }

        mThemeAdapter = new GifThemeListAdapter(this, mThemeList);
        mThemeAdapter.setListClick(mListClick);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setToolbarBackEnable();
        setToolbarTitle("选择GIF主题");

        rvThemeList = findViewById(R.id.rv_theme_list);
        rvThemeList.setLayoutManager(new LinearLayoutManager(this));
        rvThemeList.setAdapter(mThemeAdapter);
    }

    private IOnListClickListener mListClick = new OnListClickListener() {
        @Override
        public void onItemClick(int position) {
            final GifTheme theme = mThemeList.get(position);
            GifEditActivity.show(GifThemeListActivity.this, theme);
        }
    };
}
