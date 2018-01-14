package com.android.emoticoncreater.app;

import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.android.emoticoncreater.R;

/**
 * Metrial Design BaseDrawerActivity
 */

public abstract class MDBaseDrawerActivity extends MDBaseActivity {

    protected DrawerLayout mDrawerLayout;
    protected NavigationView navViewStart;

    private boolean mStartDrawerEnable = false;

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView() {
        super.initView();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navViewStart = (NavigationView) findViewById(R.id.nav_view_start);

        initStartDrawerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerLayout != null) {
            openDrawer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!closeDrawer()) {
            super.onBackPressed();
        }
    }

    private void initStartDrawerView() {
        if (mDrawerLayout == null) return;
        if (navViewStart == null) return;
        if (mStartDrawerEnable) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            toggle.syncState();
            navViewStart.setNavigationItemSelectedListener(mNavigationItemSelected);
        } else {
            mDrawerLayout.removeView(navViewStart);
        }
    }

    protected void initMenu(@MenuRes int menuId) {
        if (mDrawerLayout != null && navViewStart != null) {
            navViewStart.getMenu().clear();
            navViewStart.inflateMenu(menuId);
            navViewStart.setCheckedItem(R.id.nav_my_picture);
        }
    }

    protected final void openDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    protected final boolean closeDrawer() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        }

        return false;
    }

    protected void setStartDrawerEnable(boolean startDrawerEnable) {
        this.mStartDrawerEnable = startDrawerEnable;
    }

    protected boolean navigationItemSelected(@IdRes int itemId) {
        return false;
    }

    private final NavigationView.OnNavigationItemSelectedListener mNavigationItemSelected = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            closeDrawer();
            return navigationItemSelected(item.getItemId());
        }
    };
}
