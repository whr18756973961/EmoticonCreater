package com.android.emoticoncreater.app;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.ui.dialog.DefaultProgressDialog;

/**
 * Material Design BaseActivity
 */

public abstract class MDBaseActivity extends AppCompatActivity {

    protected CoordinatorLayout mRootView;
    protected Toolbar mToolbar;

    private InputMethodManager manager;
    private DefaultProgressDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        initData();
        initView(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyboard();
        hideProgress();
    }

    protected void initData() {
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    protected void initView(Bundle savedInstanceState) {
        mRootView = (CoordinatorLayout) findViewById(R.id.rootview);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    protected void setToolbarTitle(@StringRes int stringId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(stringId));
        }
    }

    protected void hideKeyboard() {
        final View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            final IBinder windowToken = currentFocus.getWindowToken();
            if (windowToken != null && manager != null) {
                manager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    protected void showProgress(@StringRes int stringId) {
        if (mProgress == null) {
            mProgress = new DefaultProgressDialog(this);
        }
        mProgress.setMessage(getString(stringId));
        mProgress.showDialog();
    }

    protected void hideProgress() {
        if (mProgress != null) {
            mProgress.dismissDialog();
        }
    }

    protected void showSnackbar(@StringRes int contentId) {
        if (mRootView != null) {
            Snackbar.make(mRootView, contentId, Snackbar.LENGTH_LONG).show();
        }
    }

    protected void showSnackbar(@StringRes int btnTextId, @StringRes int contentId, View.OnClickListener click) {
        if (mRootView != null) {
            Snackbar.make(mRootView, contentId, Snackbar.LENGTH_LONG)
                    .setAction(btnTextId, click)
                    .show();
        }
    }

    @LayoutRes
    abstract protected int getContentView();
}
