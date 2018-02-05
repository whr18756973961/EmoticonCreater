package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.model.SecretBean;

/**
 * 编辑秘密
 */

public class SecretEditActivity extends BaseActivity {

    public static final String KEY_NEW_SECRET = "key_new_secret";
    private static final String KEY_SECRET = "key_secret";

    private ImageView ivPicture;
    private EditText etTitle;
    private Button btnAdd;

    private SecretBean mSecret;

    public static void show(Activity activity, ActivityOptions options, SecretBean secret) {
        Intent intent = new Intent();
        intent.setClass(activity, SecretEditActivity.class);
        intent.putExtra(KEY_SECRET, secret);
        activity.startActivity(intent, options.toBundle());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_secret_edit;
    }

    @Override
    protected void initData() {
        super.initData();
        mSecret = getIntent().getParcelableExtra(KEY_SECRET);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setToolbarBackEnable();
        setToolbarTitle("添加秘密");
        setToolbarSubTitle("编写你的秘密");

        ivPicture = (ImageView) findViewById(R.id.iv_picture);
        etTitle = (EditText) findViewById(R.id.et_title);
        btnAdd = (Button) findViewById(R.id.btn_add);

        if (mSecret != null) {
            final int resourceId = mSecret.getResourceId();
            final String title = mSecret.getTitle();

            ivPicture.setImageResource(resourceId);
            etTitle.setHint(title);
        }

        btnAdd.setOnClickListener(mClick);
    }

    private void doAdd() {
        final String title = etTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            showSnackbar("写下你的秘密");
        } else {
            mSecret.setTitle(title);

            Intent intent = new Intent();
            intent.setClass(this, TellTheSecretActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(KEY_NEW_SECRET, mSecret);
            startActivity(intent);
        }
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add:
                    doAdd();
                    break;
            }
        }
    };
}
