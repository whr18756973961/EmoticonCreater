package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.db.LiteOrmHelper;
import com.android.emoticoncreater.model.ThreeProverbBean;
import com.android.emoticoncreater.utils.DataCleanManager;
import com.android.emoticoncreater.utils.FileUtils;
import com.android.emoticoncreater.utils.ImageUtils;
import com.android.emoticoncreater.utils.SDCardUtils;
import com.android.emoticoncreater.utils.ThreadPoolUtil;
import com.android.emoticoncreater.utils.TripleSendHelper;
import com.android.emoticoncreater.widget.imageloader.ImageLoaderFactory;

import java.io.File;

/**
 * 表情三连发
 */

public class TripleSendActivity extends BaseActivity {

    private static final int REQUEST_CODE_PICTURE1 = 1;
    private static final int REQUEST_CODE_PICTURE2 = 1 << 1;
    private static final int REQUEST_CODE_PICTURE3 = 1 << 2;
    private static final int REQUEST_CODE_SELECT_PICTURE = 1 << 4;
    private static final int REQUEST_CODE_CUTE_PICTURE = 1 << 5;

    private static final int REQUEST_CODE_TO_PROVERB = 1004;

    private EditText etTitle;
    private ImageView ivPicture1;
    private ImageView ivPicture2;
    private ImageView ivPicture3;
    private EditText etName1;
    private EditText etName2;
    private EditText etName3;
    private Button btnDoCreate;
    private ImageView ivPreview;
    private Button btnDoSave;
    private Button btnDoSend;

    private String mPath1;
    private String mPath2;
    private String mPath3;
    private String mSavePath;
    private String mTempPath;
    private File mCutePhotoFile;
    private File mCurrentImage;
    private LiteOrmHelper mDBHelper;

    public static void show(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, TripleSendActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_triple_send;
    }

    @Override
    protected void initData() {
        super.initData();
        mSavePath = SDCardUtils.getSDCardDir(this) + Constants.PATH_TRIPLE_SEND;
        mTempPath = SDCardUtils.getExternalCacheDir(this);

        FileUtils.createdirectory(mSavePath);
        FileUtils.createdirectory(mTempPath);

        mDBHelper = new LiteOrmHelper(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarBackEnable();
        setToolbarTitle("表情三连发");

        etTitle = (EditText) findViewById(R.id.et_title);
        ivPicture1 = (ImageView) findViewById(R.id.iv_picture1);
        ivPicture2 = (ImageView) findViewById(R.id.iv_picture2);
        ivPicture3 = (ImageView) findViewById(R.id.iv_picture3);
        etName1 = (EditText) findViewById(R.id.et_name1);
        etName2 = (EditText) findViewById(R.id.et_name2);
        etName3 = (EditText) findViewById(R.id.et_name3);
        btnDoCreate = (Button) findViewById(R.id.btn_do_create);
        ivPreview = (ImageView) findViewById(R.id.iv_preview);
        btnDoSave = (Button) findViewById(R.id.btn_do_save);
        btnDoSend = (Button) findViewById(R.id.btn_do_send);

        ivPicture1.setOnClickListener(mClick);
        ivPicture2.setOnClickListener(mClick);
        ivPicture3.setOnClickListener(mClick);
        btnDoCreate.setOnClickListener(mClick);
        btnDoSave.setOnClickListener(mClick);
        btnDoSend.setOnClickListener(mClick);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        final File cacheDir = new File(mTempPath);
        if (cacheDir.exists()) {
            DataCleanManager.deleteAllFiles(cacheDir);
        }
        if (mDBHelper != null) {
            mDBHelper.closeDB();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (REQUEST_CODE_TO_PROVERB == requestCode) {
                final ThreeProverbBean proverb = data.getParcelableExtra(Constants.KEY_RETURN_DATA);
                if (proverb != null) {
                    etTitle.setText(proverb.getTitle());
                    etName1.setText(proverb.getFirstProverb());
                    etName2.setText(proverb.getSecondProverb());
                    etName3.setText(proverb.getThirdProverb());
                }
            } else {
                final int actionCode = getActionCode(requestCode);
                final int pictureCode = getPictureCode(requestCode);
                if (actionCode == REQUEST_CODE_SELECT_PICTURE) {
                    if (data != null) {
                        doCutPicture(data.getData(), pictureCode);
                    }
                } else if (actionCode == REQUEST_CODE_CUTE_PICTURE) {
                    if (mCutePhotoFile != null && mCutePhotoFile.exists()) {
                        if (REQUEST_CODE_PICTURE1 == pictureCode) {
                            mPath1 = mCutePhotoFile.getAbsolutePath();
                            mPath2 = mPath1;
                            mPath3 = mPath1;
                            ImageLoaderFactory.getLoader().loadImage(this, ivPicture1, mPath1);
                            ImageLoaderFactory.getLoader().loadImage(this, ivPicture2, mPath2);
                            ImageLoaderFactory.getLoader().loadImage(this, ivPicture3, mPath3);
                        } else if (REQUEST_CODE_PICTURE2 == pictureCode) {
                            mPath2 = mCutePhotoFile.getAbsolutePath();
                            ImageLoaderFactory.getLoader().loadImage(this, ivPicture2, mPath2);
                        } else {
                            mPath3 = mCutePhotoFile.getAbsolutePath();
                            ImageLoaderFactory.getLoader().loadImage(this, ivPicture3, mPath3);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tools, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hideKeyboard();
        int id = item.getItemId();
        if (id == R.id.action_proverb) {
            ThreeProverbListActivity.show(this, REQUEST_CODE_TO_PROVERB);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doSelectPicture(int requestCode) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, requestCode | REQUEST_CODE_SELECT_PICTURE);
    }

    private void doCutPicture(Uri inputUri, int requestCode) {

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
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, requestCode | REQUEST_CODE_CUTE_PICTURE);
    }

    private void doCreatePicture() {
        final String title = etTitle.getText().toString();
        final String name1 = etName1.getText().toString();
        final String name2 = etName2.getText().toString();
        final String name3 = etName3.getText().toString();
        if (TextUtils.isEmpty(title)) {
            showSnackbar("请先输入标题");
        } else if (TextUtils.isEmpty(mPath1)) {
            showSnackbar("请先选择图片1");
        } else if (TextUtils.isEmpty(mPath2)) {
            showSnackbar("请先选择图片2");
        } else if (TextUtils.isEmpty(mPath3)) {
            showSnackbar("请先选择图片3");
        } else if (TextUtils.isEmpty(name1)) {
            showSnackbar("请输入图片1文字内容");
        } else if (TextUtils.isEmpty(name2)) {
            showSnackbar("请输入图片2文字内容");
        } else if (TextUtils.isEmpty(name3)) {
            showSnackbar("请输入图片3文字内容");
        } else {
            showProgress("图片处理中...");
            ThreadPoolUtil.getInstache().cachedExecute(new Runnable() {
                @Override
                public void run() {

                    TripleSendHelper utils = new TripleSendHelper.Builder()
                            .setTitle(title)
                            .setPath1(mPath1)
                            .setPath2(mPath2)
                            .setPath3(mPath3)
                            .setName1(name1)
                            .setName2(name2)
                            .setName3(name3)
                            .setSavePath(mSavePath).bulid();

                    mCurrentImage = utils.createExpression();

                    doStatistics(title, name1, name2, name3);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mCurrentImage.exists()) {
                                final String filePath = mCurrentImage.getAbsolutePath();
                                ImageLoaderFactory.getLoader().loadImage(TripleSendActivity.this, ivPreview, filePath);

                                refreshAlbum(mCurrentImage);

                                showSnackbar("保存路径：" + filePath);
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

    private void refreshAlbum(File file) {
        if (file != null && file.exists()) {
            final Uri uri = Uri.fromFile(file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        }
    }

    private void doStatistics(String title, String name1, String name2, String name3) {
        final ThreeProverbBean proverb = mDBHelper.queryFirst(ThreeProverbBean.class,
                "title == ? and firstProverb == ? and secondProverb == ? and thirdProverb == ?",
                title, name1, name2, name3);
        if (proverb != null) {
            proverb.setUseTimes(proverb.getUseTimes() + 1);
            mDBHelper.update(proverb);
        }
    }

    private void doSaveProverb() {
        final String title = etTitle.getText().toString();
        final String name1 = etName1.getText().toString();
        final String name2 = etName2.getText().toString();
        final String name3 = etName3.getText().toString();
        if (TextUtils.isEmpty(title)) {
            showSnackbar("请先输入标题");
        } else if (TextUtils.isEmpty(name1)) {
            showSnackbar("请输入图片1文字内容");
        } else if (TextUtils.isEmpty(name2)) {
            showSnackbar("请输入图片2文字内容");
        } else if (TextUtils.isEmpty(name3)) {
            showSnackbar("请输入图片3文字内容");
        } else {
            ThreeProverbBean proverb = mDBHelper.queryFirst(ThreeProverbBean.class,
                    "title == ? and firstProverb == ? and secondProverb == ? and thirdProverb == ?",
                    title, name1, name2, name3);
            if (proverb == null) {
                proverb = new ThreeProverbBean();
                proverb.setTitle(title);
                proverb.setFirstProverb(name1);
                proverb.setSecondProverb(name2);
                proverb.setThirdProverb(name3);
                proverb.setUseTimes(1);
                mDBHelper.save(proverb);
                showSnackbar("保存成功");
            } else {
                showSnackbar("这套语录已在“怼人语录”里");
            }
        }
    }

    private void doSend() {
        if (mCurrentImage != null && mCurrentImage.exists() && mCurrentImage.isFile()) {
            final Uri uri = ImageUtils.getUriFromFile(this, mCurrentImage);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpg");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, ""));
        } else {
            ivPreview.setImageResource(0);
            showSnackbar("图片不存在");
        }
    }

    private int getPictureCode(int code) {
        if ((REQUEST_CODE_PICTURE1 & code) != 0) {
            return REQUEST_CODE_PICTURE1;
        } else if ((REQUEST_CODE_PICTURE2 & code) != 0) {
            return REQUEST_CODE_PICTURE2;
        } else if ((REQUEST_CODE_PICTURE3 & code) != 0) {
            return REQUEST_CODE_PICTURE3;
        }

        return -1;
    }

    private int getActionCode(int code) {
        if ((REQUEST_CODE_SELECT_PICTURE & code) != 0) {
            return REQUEST_CODE_SELECT_PICTURE;
        } else if ((REQUEST_CODE_CUTE_PICTURE & code) != 0) {
            return REQUEST_CODE_CUTE_PICTURE;
        }

        return -1;
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard();
            switch (v.getId()) {
                case R.id.iv_picture1:
                    doSelectPicture(REQUEST_CODE_PICTURE1);
                    break;
                case R.id.iv_picture2:
                    doSelectPicture(REQUEST_CODE_PICTURE2);
                    break;
                case R.id.iv_picture3:
                    doSelectPicture(REQUEST_CODE_PICTURE3);
                    break;
                case R.id.btn_do_create:
                    doCreatePicture();
                    break;
                case R.id.btn_do_save:
                    doSaveProverb();
                    break;
                case R.id.btn_do_send:
                    doSend();
                    break;
            }
        }
    };
}
