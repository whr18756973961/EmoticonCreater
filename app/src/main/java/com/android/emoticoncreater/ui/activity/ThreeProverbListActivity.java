package com.android.emoticoncreater.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.db.LiteOrmHelper;
import com.android.emoticoncreater.model.ThreeProverbBean;
import com.android.emoticoncreater.ui.adapter.OnListClickListener;
import com.android.emoticoncreater.ui.adapter.ThreeProverbListAdapter;
import com.android.emoticoncreater.ui.dialog.DefaultAlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 三连怼人语录
 */

public class ThreeProverbListActivity extends BaseActivity {

    private CoordinatorLayout mRootView;
    private Toolbar mToolbar;
    private RecyclerView rvProverbList;

    private DefaultAlertDialog mAlertDialog;

    private List<ThreeProverbBean> mProverbList;
    private ThreeProverbListAdapter mProverbAdapter;

    private LiteOrmHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_proverb_list);

        initData();
        initView();
        getProverbList();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDBHelper != null) {
            mDBHelper.closeDB();
        }
        if (mAlertDialog != null) {
            mAlertDialog.dismissDialog();
        }
    }

    private void initData() {
        mDBHelper = new LiteOrmHelper(this);

        mProverbList = new ArrayList<>();
        mProverbAdapter = new ThreeProverbListAdapter(this, mProverbList);
        mProverbAdapter.setListClick(mListClick);
    }

    private void initView() {
        mRootView = (CoordinatorLayout) findViewById(R.id.rootview);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        rvProverbList = (RecyclerView) findViewById(R.id.rv_proverb_list);

        mToolbar.setTitle("怼人语录");
        setSupportActionBar(mToolbar);

        rvProverbList.setLayoutManager(new LinearLayoutManager(this));
        rvProverbList.setAdapter(mProverbAdapter);
    }

    private void showDeleteDialog(final int position) {
        if (mAlertDialog == null) {
            mAlertDialog = new DefaultAlertDialog(this);
            mAlertDialog.setMessage("是否删除这条语录？");
            mAlertDialog.setCancelButton("取消");
        }
        mAlertDialog.setConfirmButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final ThreeProverbBean proverb = mProverbList.get(position);
                mDBHelper.delete(proverb);
                getProverbList();
                Snackbar.make(mRootView, "删除成功", Snackbar.LENGTH_LONG).show();
            }
        });
        mAlertDialog.showDialog();
    }

    private void getProverbList() {
        final List<ThreeProverbBean> proverbList = mDBHelper.queryAllOrderDescBy(ThreeProverbBean.class, "useTimes");
        mProverbList.clear();
        if (proverbList != null) {
            mProverbList.addAll(proverbList);
        }
        mProverbAdapter.notifyDataSetChanged();

        if (mProverbList.size() == 0) {
            Snackbar.make(mRootView, "一句话都没有", Snackbar.LENGTH_LONG).show();
        }
    }

    private OnListClickListener mListClick = new OnListClickListener() {
        @Override
        public void onItemClick(int position) {
            final ThreeProverbBean proverb = mProverbList.get(position);
            Intent intent = new Intent();
            intent.putExtra(Constants.KEY_RETURN_DATA, proverb);
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void onTagClick(int tag, int position) {
            if (ITEM_TAG0 == tag) {
                showDeleteDialog(position);
            }
        }
    };
}
