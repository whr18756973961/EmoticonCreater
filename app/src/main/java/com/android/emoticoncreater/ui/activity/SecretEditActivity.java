package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

        ivPicture = findViewById(R.id.iv_picture);
        etTitle = findViewById(R.id.et_title);

        etTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
            }
        });

        if (mSecret != null) {
            final int resourceId = mSecret.getResourceId();
            final String title = mSecret.getTitle();

            ivPicture.setImageResource(resourceId);
            etTitle.setHint(title);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_done == item.getItemId()) {
            doAdd();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_secret, menu);
        return super.onCreateOptionsMenu(menu);
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
}
