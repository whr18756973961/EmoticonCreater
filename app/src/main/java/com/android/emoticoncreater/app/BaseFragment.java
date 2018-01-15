package com.android.emoticoncreater.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.ui.dialog.DefaultProgressDialog;


/**
 * 基类
 */

public abstract class BaseFragment extends Fragment {

    protected View mFragmentView;
    protected CoordinatorLayout mRootView;

    private DefaultProgressDialog mProgress;
    private InputMethodManager manager;

    protected Activity mActivity;

    protected void initData() {
        manager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    protected void initView(Bundle savedInstanceState) {
        mRootView = (CoordinatorLayout) mFragmentView.findViewById(R.id.rootview);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(getLayoutId(), container, false);

        initView(savedInstanceState);

        return mFragmentView;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgress();
    }

    protected void showProgress(@StringRes int stringId) {
        if (mProgress == null) {
            mProgress = new DefaultProgressDialog(getActivity());
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

    protected void hideKeyboard() {
        final View currentFocus = mActivity.getCurrentFocus();
        if (currentFocus != null) {
            final IBinder windowToken = currentFocus.getWindowToken();
            if (windowToken != null && manager != null) {
                manager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @LayoutRes
    abstract protected int getLayoutId();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e(this.getClass().getSimpleName(), "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.e(this.getClass().getSimpleName(), "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e(this.getClass().getSimpleName(), "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e(this.getClass().getSimpleName(), "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e(this.getClass().getSimpleName(), "onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.e(this.getClass().getSimpleName(), "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean getUserVisibleHint() {
        Log.e(this.getClass().getSimpleName(), "getUserVisibleHint");
        return super.getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(this.getClass().getSimpleName(), "setUserVisibleHint:" + isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(this.getClass().getSimpleName(), "onHiddenChanged:" + hidden);
    }
}
