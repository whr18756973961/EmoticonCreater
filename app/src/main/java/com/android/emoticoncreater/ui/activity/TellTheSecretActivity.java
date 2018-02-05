package com.android.emoticoncreater.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.model.SecretBean;
import com.android.emoticoncreater.ui.adapter.OnListClickListener;
import com.android.emoticoncreater.ui.adapter.SecretListAdapter;
import com.android.emoticoncreater.utils.FileUtils;
import com.android.emoticoncreater.utils.SDCardUtils;
import com.android.emoticoncreater.utils.SecretUtils;
import com.android.emoticoncreater.utils.ThreadPoolUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 告诉你个秘密
 */

public class TellTheSecretActivity extends BaseActivity {

    private RecyclerView rvSecretList;
    private Button btnAdd;
    private Button btnDoCreate;

    private List<SecretBean> mSecretList;
    private SecretListAdapter mSecretAdapter;

    private LinearLayoutManager mLayoutManager;
    private ItemTouchHelper mItemTouchHelper;

    private String mSavePath;

    public static void show(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, TellTheSecretActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_tell_the_secret;
    }

    @Override
    protected void initData() {
        super.initData();
        mSavePath = SDCardUtils.getSDCardDir() + Constants.PATH_SECRET;

        FileUtils.createdirectory(mSavePath);

        mSecretList = new ArrayList<>();
        mSecretAdapter = new SecretListAdapter(this, mSecretList);
        mSecretAdapter.setListClick(mListClick);

        mLayoutManager = new LinearLayoutManager(this);
        mItemTouchHelper = new ItemTouchHelper(mCallback);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setToolbarBackEnable();
        setToolbarTitle("告诉你个秘密");

        rvSecretList = (RecyclerView) findViewById(R.id.rv_secret_list);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDoCreate = (Button) findViewById(R.id.btn_do_create);

        rvSecretList.setLayoutManager(mLayoutManager);
        rvSecretList.setAdapter(mSecretAdapter);

        mItemTouchHelper.attachToRecyclerView(rvSecretList);

        btnAdd.setOnClickListener(mClick);
        btnDoCreate.setOnClickListener(mClick);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final SecretBean secret = intent.getParcelableExtra(SecretEditActivity.KEY_NEW_SECRET);
        if (secret != null) {
            final int index = mSecretList.size();
            mSecretList.add(secret);
            mSecretAdapter.notifyItemInserted(index);
            rvSecretList.scrollToPosition(index);
        }
    }

    private void doAdd() {
        if (mSecretList.size() >= 10) {
            showSnackbar("最多只能添加10个秘密");
        } else {
            SecretListActivity.show(this);
        }
    }

    private void doCreate() {
        if (mSecretList.size() <= 0) {
            showSnackbar("你还没添加秘密");
        } else {
            showProgress("图片处理中...");
            ThreadPoolUtil.getInstache().cachedExecute(new Runnable() {
                @Override
                public void run() {
                    final File imageFile = SecretUtils.createSecret(getResources(), mSecretList, mSavePath);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (imageFile.exists()) {
                                final String filePath = imageFile.getAbsolutePath();
                                refreshAlbum(imageFile);

                                ShowSecretActivity.show(TellTheSecretActivity.this, filePath);
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

    private void refreshAlbum(File file) {
        if (file != null && file.exists()) {
            final Uri uri = Uri.fromFile(file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        }
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add:
                    doAdd();
                    break;
                case R.id.btn_do_create:
                    doCreate();
                    break;
            }
        }
    };

    private OnListClickListener mListClick = new OnListClickListener() {
        @Override
        public void onTagClick(int tag, int position) {
            if (ITEM_TAG0 == tag) {
                final int count = mSecretList.size();
                if (position >= 0 && position < count) {
                    mSecretList.remove(position);
                    mSecretAdapter.notifyItemRemoved(position);
                    mSecretAdapter.notifyItemRangeChanged(position, count - position);
                }
            }
        }
    };

    private ItemTouchHelper.Callback mCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            final int fromPosition = viewHolder.getAdapterPosition();
            final int toPosition = target.getAdapterPosition();

            Collections.swap(mSecretList, fromPosition, toPosition);
            mSecretAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}
