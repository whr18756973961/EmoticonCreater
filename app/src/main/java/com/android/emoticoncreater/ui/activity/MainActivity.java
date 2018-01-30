package com.android.emoticoncreater.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseActivity;
import com.android.emoticoncreater.config.Constants;
import com.android.emoticoncreater.utils.AppManager;
import com.android.emoticoncreater.utils.FastClick;
import com.android.emoticoncreater.utils.FileUtils;
import com.android.emoticoncreater.utils.SDCardUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_TO_SETTING = 1000;//跳转到系统设置权限页面
    private CoordinatorLayout mRootView;
    private Toolbar mToolbar;
    private TextView tvTripleSend;
    private TextView tvSecret;

    private int permissionPosition = 0;//当前请求权限位置
    private String[] permissions;
    private String[] errorTips;

    private AlertDialog mAlertDialog;

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_triple_send:
                    startActivity(new Intent(MainActivity.this, TripleSendActivity.class));
                    break;
                case R.id.tv_secret:
                    startActivity(new Intent(MainActivity.this, TellTheSecretActivity.class));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        requestPermission();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_TO_SETTING == requestCode) {
            if (permissionPosition < permissions.length) {
                if (ContextCompat.checkSelfPermission(this, permissions[permissionPosition]) != PackageManager.PERMISSION_GRANTED) {
                    finish();
                } else {
                    permissionPosition++;
                    requestPermission();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                if (permissionPosition < errorTips.length) {
                    showPermissionTipsDialog();
                } else {
                    finish();
                }
            } else {
                permissionPosition++;
                requestPermission();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!FastClick.isExitClick()) {
            Snackbar.make(mRootView, "再次点击退出程序", Snackbar.LENGTH_LONG).show();
        } else {
            super.onBackPressed();
        }
    }

    private void initData() {
        final String appName = getString(R.string.app_name);
        permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        errorTips = new String[]{
                String.format("在设置-应用-%1$s-权限中开启存储权限，以正常使用该功能", appName)
        };

        final List<String> requestList = new ArrayList<>();
        final List<String> errorTipsList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                String tips = this.errorTips[i];
                requestList.add(permission);
                errorTipsList.add(tips);
            }
        }
        permissions = requestList.toArray(new String[requestList.size()]);
        errorTips = errorTipsList.toArray(new String[errorTipsList.size()]);

        final String dcimPath = SDCardUtils.getSDCardDir() + Constants.PATH_DCIM;
        final String basePath = SDCardUtils.getSDCardDir() + Constants.PATH_BASE;

        FileUtils.createdirectory(dcimPath);
        FileUtils.createdirectory(basePath);
    }

    private void initView() {
        mRootView = (CoordinatorLayout) findViewById(R.id.rootview);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTripleSend = (TextView) findViewById(R.id.tv_triple_send);
        tvSecret = (TextView) findViewById(R.id.tv_secret);

        mToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mToolbar);

        tvTripleSend.setOnClickListener(mClick);
        tvSecret.setOnClickListener(mClick);
    }

    private void requestPermission() {
        if (permissionPosition < permissions.length) {
            ActivityCompat.requestPermissions(this, new String[]{permissions[permissionPosition]}, permissionPosition);
        } else {
            initView();
        }
    }

    private void showPermissionTipsDialog() {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this).create();
            mAlertDialog.setTitle("权限申请");
            mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "去设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    AppManager.showInstalledAppDetails(MainActivity.this, getPackageName(), REQUEST_TO_SETTING);
                }
            });
            mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
        mAlertDialog.setMessage(errorTips[permissionPosition]);
        mAlertDialog.show();
    }
}
