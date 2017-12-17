package com.android.emoticoncreater.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.emoticoncreater.R;


/**
 * 信息显示，并选择操作对话框
 */
public class TipsAlertDialog extends AlertDialog {

    private TextView tvTitle;
    private TextView tvConfirm, tvCancel;

    public TipsAlertDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tips_alert);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);

        tvConfirm.setEnabled(false);
        tvCancel.setEnabled(false);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void setTitleText(String title) {
        tvTitle.setText(title);
    }

    public TextView getTvConfirm() {
        return tvConfirm;
    }

    public TextView getTvCancel() {
        return tvCancel;
    }
}
