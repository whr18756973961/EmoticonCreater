package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.model.PictureBean;
import com.android.emoticoncreater.utils.OneEmoticonHelper;
import com.android.emoticoncreater.utils.SDCardUtils;
import com.android.emoticoncreater.utils.ThreadPoolUtil;
import com.android.emoticoncreater.widget.imageloader.ImageLoaderFactory;
import com.android.emoticoncreater.widget.imageloader.SquareImageView;

import java.io.File;

public class OneEmoticonEditActivity extends BaseActivity {

    private static final String KEY_ONE_EMOTICON = "key_one_emoticon";

    private SquareImageView ivPicture;
    private AppCompatEditText etTitle;

    private PictureBean mPicture;
    private String mSavePath;

    public static void show(Activity activity, PictureBean picture) {
        Intent intent = new Intent();
        intent.setClass(activity, OneEmoticonEditActivity.class);
        intent.putExtra(KEY_ONE_EMOTICON, picture);
        activity.startActivity(intent);
    }

    public static void show(Activity activity, ActivityOptions options, PictureBean picture) {
        Intent intent = new Intent();
        intent.setClass(activity, OneEmoticonEditActivity.class);
        intent.putExtra(KEY_ONE_EMOTICON, picture);
        activity.startActivity(intent, options.toBundle());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_one_emoticon_edit;
    }

    @Override
    protected void initData() {
        super.initData();

        mPicture = getIntent().getParcelableExtra(KEY_ONE_EMOTICON);
        mSavePath = SDCardUtils.getSDCardDir(this) + Constants.PATH_ONE_EMOTICON;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setToolbarBackEnable();
        setToolbarTitle("编辑表情");
        setToolbarSubTitle("编写表情的文字");

        ivPicture = (SquareImageView) findViewById(R.id.iv_picture);
        etTitle = (AppCompatEditText) findViewById(R.id.et_title);

        etTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
            }
        });

        if (mPicture != null) {
            final String filePath = mPicture.getFilePath();
            final int resourceId = mPicture.getResourceId();

            if (!TextUtils.isEmpty(filePath)) {
                ImageLoaderFactory.getLoader().loadImageFitCenter(this, ivPicture, filePath, 0, 0);
            } else {
                ImageLoaderFactory.getLoader().loadImageFitCenter(this, ivPicture, resourceId, 0, 0);
            }

            ivPicture.setImageResource(resourceId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_done == item.getItemId()) {
            doCreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void doCreate() {
        showProgress("图片处理中...");

        final String text = etTitle.getText().toString();
        mPicture.setTitle(text);

        ThreadPoolUtil.getInstache().cachedExecute(new Runnable() {
            @Override
            public void run() {
                final File imageFile = OneEmoticonHelper.create(getResources(), mPicture, mSavePath);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (imageFile.exists()) {
                            final String filePath = imageFile.getAbsolutePath();

                            final Uri uri = Uri.fromFile(imageFile);
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                            ShowPictureActivity.show(OneEmoticonEditActivity.this, filePath);//TODO
                        } else {
                            showSnackbar("生成失败，图片不存在");
                        }
                        hideProgress();
                    }
                });
            }
        });
    }
}
