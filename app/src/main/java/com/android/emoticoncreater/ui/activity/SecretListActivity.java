package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.model.SecretBean;
import com.android.emoticoncreater.ui.adapter.OnListClickListener;
import com.android.emoticoncreater.ui.adapter.SecretPictureListAdapter;

/**
 * 秘密图片列表
 */

public class SecretListActivity extends BaseActivity {

    private RecyclerView rvSecretList;

    private SecretPictureListAdapter mSecretAdapter;

    public static void show(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, SecretListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_secret_list;
    }

    @Override
    protected void initData() {
        super.initData();

        mSecretAdapter = new SecretPictureListAdapter(this);
        mSecretAdapter.setListClick(mListClick);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setToolbarBackEnable();
        setToolbarTitle("秘密表情包");
        setToolbarSubTitle("选择一个秘密表情");

        rvSecretList = findViewById(R.id.rv_secret_list);
        rvSecretList.setLayoutManager(new LinearLayoutManager(this));
        rvSecretList.setAdapter(mSecretAdapter);

    }

    private OnListClickListener mListClick = new OnListClickListener() {
        @Override
        public void onItemClick(View view, Object object) {
            if (object instanceof SecretBean) {
                final SecretBean secret = (SecretBean) object;
                Pair<View, String> picturePair = Pair.create(view, getString(R.string.transition_name_secret));

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SecretListActivity.this, picturePair);

                SecretEditActivity.show(SecretListActivity.this, options, secret);
            }
        }
    };

}
