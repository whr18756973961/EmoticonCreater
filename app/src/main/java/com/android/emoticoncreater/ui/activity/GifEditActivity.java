package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.model.GifText;
import com.android.emoticoncreater.model.GifTheme;
import com.android.emoticoncreater.utils.GifHelper;
import com.android.emoticoncreater.utils.SDCardUtils;
import com.android.emoticoncreater.utils.ThreadPoolUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Gif 文字编辑
 */
public class GifEditActivity extends BaseActivity {

    private static final String KEY_GIF_THEME = "key_gif_theme";

    private LinearLayout llContent;
    private GifImageView ivGif;

    private GifTheme mTheme;
    private String mSavePath;
    private GifDrawable mDrawable;

    private List<GifText> mTextList;
    private List<EditText> mEditTextList;

    public static void show(Activity activity, GifTheme theme) {
        Intent intent = new Intent();
        intent.setClass(activity, GifEditActivity.class);
        intent.putExtra(KEY_GIF_THEME, theme);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_gif_edit;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDrawable != null) {
            mDrawable.recycle();
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

    @Override
    protected void initData() {
        super.initData();

        mTheme = getIntent().getParcelableExtra(KEY_GIF_THEME);
        mSavePath = SDCardUtils.getSDCardDir(this) + Constants.PATH_GIF;

        mTextList = new ArrayList<>();
        mEditTextList = new ArrayList<>();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setToolbarBackEnable();
        setToolbarSubTitle("编辑对应的文字");

        llContent = findViewById(R.id.ll_content);
        ivGif = findViewById(R.id.iv_gif);

        if (mTheme != null) {
            final String name = mTheme.getName();
            final String fileName = mTheme.getFileName();
            final List<GifText> textList = mTheme.getTextList();
            setToolbarTitle(name);

            try {
                mDrawable = new GifDrawable(getAssets(), fileName);
                ivGif.setImageDrawable(mDrawable);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (textList != null && !textList.isEmpty()) {
                mTextList.addAll(textList);

                for (int i = 0; i < mTextList.size(); i++) {
                    final GifText gifText = mTextList.get(i);
                    final String tips = "第" + (i + 1) + "句：";
                    final String hint = gifText.getHint();

                    final View view = LayoutInflater.from(this).inflate(R.layout.layout_gif_text, null);
                    final TextView tvTips = view.findViewById(R.id.tv_tips);
                    final EditText etText = view.findViewById(R.id.et_text);

                    tvTips.setText(tips);
                    etText.setHint(hint);
                    mEditTextList.add(etText);

                    llContent.addView(view);
                }
            }
        }
    }

    private void doCreate() {

        boolean isAllEmpty = true;

        for (int i = 0; i < mEditTextList.size(); i++) {
            final EditText editText = mEditTextList.get(i);
            final GifText gifText = mTextList.get(i);
            final String text = editText.getText().toString();

            if (!TextUtils.isEmpty(text)) {
                gifText.setText(text);
                isAllEmpty = false;
            }
        }

        for (int i = 0; i < mTextList.size(); i++) {
            final GifText gifText = mTextList.get(i);
            final String text = gifText.getText();

            if (isAllEmpty) {
                gifText.setText(gifText.getHint());
            } else if (TextUtils.isEmpty(text)) {
                showSnackbar("请输入第" + (i + 1) + "句的内容");
                return;
            }
        }

        showProgress("生成中...");

        ThreadPoolUtil.getInstache().cachedExecute(new Runnable() {
            @Override
            public void run() {

                final File imageFile = GifHelper.create(getAssets(), mTheme, mSavePath);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (imageFile != null && imageFile.exists()) {
                            final Uri uri = Uri.fromFile(imageFile);
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                            showSnackbar("生成成功：" + imageFile.getAbsolutePath());
                        } else {
                            showSnackbar("生成失败");
                        }
                        hideProgress();
                    }
                });
            }
        });
    }
}
