package com.android.emoticoncreater.ui.activity;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.BaseFragment;
import com.android.emoticoncreater.model.PictureBean;
import com.android.emoticoncreater.ui.adapter.EmoticonListAdapter;
import com.android.emoticoncreater.ui.adapter.IOnListClickListener;
import com.android.emoticoncreater.ui.adapter.OnListClickListener;

/**
 * 一个表情 fragment
 */

public class EmoticonFragment extends BaseFragment {

    public static final String ARGUMENT = "argument";

    private RecyclerView rvEmoticonList;

    private EmoticonListAdapter mEmoticonAdapter;
    private String mTitle;

    public static EmoticonFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, title);
        EmoticonFragment fragment = new EmoticonFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_emoticon;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString(ARGUMENT);
        }

        mEmoticonAdapter = new EmoticonListAdapter(mActivity, mTitle);
        mEmoticonAdapter.setListClick(mListClick);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        rvEmoticonList = (RecyclerView) mFragmentView.findViewById(R.id.rv_emoticon_list);

        rvEmoticonList.setLayoutManager(new GridLayoutManager(mActivity, 3));
        rvEmoticonList.setAdapter(mEmoticonAdapter);
    }

    private IOnListClickListener mListClick = new OnListClickListener() {
        @Override
        public void onItemClick(View view, Object object) {
            if (object instanceof PictureBean) {
                final PictureBean picture = (PictureBean) object;

                final View image = view.findViewById(R.id.iv_picture);

                Pair<View, String> picturePair = Pair.create(image, getString(R.string.transition_name_one_emoticon));

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, picturePair);

                OneEmoticonEditActivity.show(mActivity, options, picture);
            }
        }
    };
}

