package com.android.emoticoncreater.app;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.android.emoticoncreater.ui.dialog.DefaultProgressDialog;
import com.orhanobut.logger.Logger;

/**
 * 基类
 */

public class BaseActivity extends AppCompatActivity {

    private InputMethodManager manager;
    private DefaultProgressDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(this.getLocalClassName());

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyboard();
        hideProgress();
    }

    private void setStatusBar() {
        Window window = getWindow();
        // 状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 导航栏透明
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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

    protected void showProgress(String message) {
        if (mProgress == null) {
            mProgress = new DefaultProgressDialog(this);
        }
        mProgress.setMessage(message);
        mProgress.showDialog();
    }

    protected void hideProgress() {
        if (mProgress != null) {
            mProgress.dismissDialog();
        }
    }
}
