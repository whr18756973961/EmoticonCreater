package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.utils.ImageUtils;
import com.android.emoticoncreater.widget.imageloader.ImageLoaderFactory;

import java.io.File;

/**
 * 展示生成的秘密图片
 */

public class ShowSecretActivity extends BaseActivity {

    private static final String KEY_PICTURE_FILE_PATH = "key_picture_file_path";

    private ImageView ivPicture;
    private FloatingActionButton btnSend;

    private File mPictureFile;

    public static void show(Activity activity, String filePath) {
        Intent intent = new Intent();
        intent.setClass(activity, ShowSecretActivity.class);
        intent.putExtra(KEY_PICTURE_FILE_PATH, filePath);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_show_secret;
    }

    @Override
    protected void initData() {
        super.initData();
        final String filePath = getIntent().getStringExtra(KEY_PICTURE_FILE_PATH);
        if (!TextUtils.isEmpty(filePath)) {
            mPictureFile = new File(filePath);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setToolbarBackEnable();
        setToolbarTitle("我的秘密");

        ivPicture = findViewById(R.id.iv_picture);
        btnSend = findViewById(R.id.btn_send);

        if (mPictureFile != null && mPictureFile.exists()) {
            ImageLoaderFactory.getLoader().loadImageFitCenter(this, ivPicture, mPictureFile, 0, 0);
            showSnackbar("保存路径：" + mPictureFile.getAbsolutePath());
        } else {
            showSnackbar("图片生成失败");
        }

        btnSend.setOnClickListener(mClick);
    }

    private void doSend() {
        if (mPictureFile != null && mPictureFile.exists() && mPictureFile.isFile()) {
            final Uri uri = ImageUtils.getUriFromFile(this, mPictureFile);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpg");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, ""));
        } else {
            showSnackbar("图片不存在");
        }
    }


    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_send:
                    doSend();
                    break;
            }
        }
    };
}
