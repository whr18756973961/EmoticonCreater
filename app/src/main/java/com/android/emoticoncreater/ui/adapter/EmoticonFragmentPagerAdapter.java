package com.android.emoticoncreater.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.emoticoncreater.ui.activity.EmoticonFragment;

import java.util.ArrayList;

/**
 *  一个表情适配器
 */

public class EmoticonFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;
    private String[] mTitles;

    public EmoticonFragmentPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);

        this.mTitles = titles;
        mFragments = new ArrayList<>();
        for (String title : mTitles) {
            mFragments.add(EmoticonFragment.newInstance(title));
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
