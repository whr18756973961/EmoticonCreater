package com.android.emoticoncreater.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.android.emoticoncreater.utils.SDCardUtils;
import com.android.emoticoncreater.utils.ThreadPoolUtil;
import com.android.emoticoncreater.utils.TripleSendUtils;
import com.android.emoticoncreater.widget.imageloader.ImageLoaderFactory;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.List;

/**
 * 表情三连发
 */

public class TripleSendActivity extends BaseActivity {

    private static final int REQUEST_CODE_PICTURE1 = 1001;
    private static final int REQUEST_CODE_PICTURE2 = 1002;
    private static final int REQUEST_CODE_PICTURE3 = 1003;
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
    private String mUsedPath;
    private File mCurrentImage;
    private LiteOrmHelper mDBHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_triple_send;
    }

    @Override
    protected void initData() {
        super.initData();
        mSavePath = SDCardUtils.getSDCardDir() + Constants.PATH_TRIPLE_SEND;
        mTempPath = SDCardUtils.getExternalCacheDir(this);
        mUsedPath = SDCardUtils.getSDCardDir() + Constants.PATH_USED_PICTURE;

        FileUtils.createdirectory(mSavePath);
        FileUtils.createdirectory(mTempPath);
        FileUtils.createdirectory(mUsedPath);

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
                final List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList != null && selectList.size() > 0) {
                    final LocalMedia media = selectList.get(0);
                    final String path = media.getPath();
                    final String compressPath = media.getCompressPath();
                    if (REQUEST_CODE_PICTURE1 == requestCode) {
                        mPath1 = path.contains(mUsedPath) ? path : compressPath;
                        ImageLoaderFactory.getLoader().loadImage(TripleSendActivity.this, ivPicture1, mPath1);
                        mPath2 = mPath1;
                        ImageLoaderFactory.getLoader().loadImage(TripleSendActivity.this, ivPicture2, mPath2);
                        mPath3 = mPath1;
                        ImageLoaderFactory.getLoader().loadImage(TripleSendActivity.this, ivPicture3, mPath3);
                    } else if (REQUEST_CODE_PICTURE2 == requestCode) {
                        mPath2 = path.contains(mUsedPath) ? path : compressPath;
                        ImageLoaderFactory.getLoader().loadImage(TripleSendActivity.this, ivPicture2, mPath2);
                    } else {
                        mPath3 = path.contains(mUsedPath) ? path : compressPath;
                        ImageLoaderFactory.getLoader().loadImage(TripleSendActivity.this, ivPicture3, mPath3);
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
            Intent intent = new Intent();
            intent.setClass(TripleSendActivity.this, ThreeProverbListActivity.class);
            startActivityForResult(intent, REQUEST_CODE_TO_PROVERB);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectPicture(int requestCode) {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageSpanCount(3)
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(false)
                .isCamera(false)
                .imageFormat(PictureMimeType.JPEG)
                .isZoomAnim(false)
                .glideOverride(200, 200)
                .enableCrop(true)
                .compress(true)
                .withAspectRatio(1, 1)
                .hideBottomControls(false)
                .isGif(false)
                .compressSavePath(mTempPath)
                .minimumCompressSize(100)
                .freeStyleCropEnabled(false)
                .circleDimmedLayer(false)
                .showCropFrame(true)
                .showCropGrid(false)
                .cropCompressQuality(90)
                .minimumCompressSize(100)
                .cropWH(200, 200)
                .rotateEnabled(false)
                .scaleEnabled(true)
                .forResult(requestCode);
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
                    copyUsedPicture();

                    mCurrentImage = TripleSendUtils.createExpression(title, mPath1, mPath2, mPath3, name1, name2, name3, mSavePath);

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

    private void copyUsedPicture() {
        if (!TextUtils.isEmpty(mPath1) && !mPath1.contains(mUsedPath)) {
            refreshAlbum(FileUtils.copyFile(mPath1, mUsedPath));
        }
        if (!TextUtils.isEmpty(mPath2) && !mPath2.equals(mPath1) && !mPath2.contains(mUsedPath)) {
            refreshAlbum(FileUtils.copyFile(mPath2, mUsedPath));
        }
        if (!TextUtils.isEmpty(mPath3) && !mPath3.equals(mPath1) && !mPath3.contains(mUsedPath)) {
            refreshAlbum(FileUtils.copyFile(mPath3, mUsedPath));
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
            final Uri uri = Uri.fromFile(mCurrentImage);

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

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard();
            switch (v.getId()) {
                case R.id.iv_picture1:
                    selectPicture(REQUEST_CODE_PICTURE1);
                    break;
                case R.id.iv_picture2:
                    selectPicture(REQUEST_CODE_PICTURE2);
                    break;
                case R.id.iv_picture3:
                    selectPicture(REQUEST_CODE_PICTURE3);
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
