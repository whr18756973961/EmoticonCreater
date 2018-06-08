package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.model.PictureBean;
import com.android.emoticoncreater.ui.adapter.EmoticonFragmentPagerAdapter;
import com.android.emoticoncreater.utils.DataCleanManager;
import com.android.emoticoncreater.utils.FileUtils;
import com.android.emoticoncreater.utils.ImageDataHelper;
import com.android.emoticoncreater.utils.ImageUtils;
import com.android.emoticoncreater.utils.SDCardUtils;

import java.io.File;

/**
 * 一个表情
 */

public class OneEmoticonActivity extends BaseActivity {

    private static final int REQUEST_CODE_SELECT_PICTURE = 100;
    private static final int REQUEST_CODE_CUTE_PICTURE = 101;

    private ViewPager vpPicture;

    private File mCutePhotoFile;
    private String mTempPath;

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
    protected void onDestroy() {
        super.onDestroy();
        final File cacheDir = new File(mTempPath);
        if (cacheDir.exists()) {
            DataCleanManager.deleteAllFiles(cacheDir);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_one_emoticon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hideKeyboard();
        int id = item.getItemId();
        if (id == R.id.action_album) {
            doSelectPicture();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (REQUEST_CODE_SELECT_PICTURE == requestCode) {
                if (data != null) {
                    doCutPicture(data.getData());
                }
            } else if (REQUEST_CODE_CUTE_PICTURE == requestCode) {
                if (mCutePhotoFile != null && mCutePhotoFile.exists()) {
                    final String filePath = mCutePhotoFile.getAbsolutePath();

                    final PictureBean picture = new PictureBean();
                    picture.setFilePath(filePath);

                    OneEmoticonEditActivity.show(this, picture);
                } else {
                    showSnackbar("图片不存在");
                }
            }
        }
    }

    @Override
    protected void initData() {
        super.initData();

        mTempPath = SDCardUtils.getExternalCacheDir(this);
        FileUtils.createdirectory(mTempPath);
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

    private void doSelectPicture() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, REQUEST_CODE_SELECT_PICTURE);
    }

    private void doCutPicture(Uri inputUri) {
        if (inputUri.toString().contains("file://")) {
            final String path = inputUri.getPath();
            final File inputFile = new File(path);
            inputUri = ImageUtils.getImageContentUri(this, inputFile);
        }

        mCutePhotoFile = new File(mTempPath, System.currentTimeMillis() + ".jpg");
        final Uri outputUri = Uri.fromFile(mCutePhotoFile);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CODE_CUTE_PICTURE);
    }
}
