package com.android.emoticoncreater.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.model.PictureBean;
import com.android.emoticoncreater.utils.ImageDataHelper;
import com.android.emoticoncreater.widget.imageloader.ImageLoaderFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 秘密表情列表
 */

public class SecretPictureListAdapter extends RecyclerView.Adapter {

    private IOnListClickListener mListClick;
    private Context mContext;
    private List<PictureBean> mList;
    private LayoutInflater mInflater;

    public SecretPictureListAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);

        mList.clear();
        for (int i = 0; i < ImageDataHelper.SECRET_LIST.length; i++) {
            final PictureBean secret = new PictureBean();
            secret.setResourceId(ImageDataHelper.SECRET_LIST[i]);
            secret.setTitle(ImageDataHelper.SECRET_TITLES[i]);
            mList.add(secret);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(mInflater.inflate(R.layout.item_secret_picture_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindItem((ListViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void bindItem(ListViewHolder holder, final int position) {
        final PictureBean model = mList.get(position);
        final int resourceId = model.getResourceId();
        final String title = model.getTitle();

        ImageLoaderFactory.getLoader().loadImageFitCenter(mContext, holder.ivPicture, resourceId, 0, 0);
        holder.tvTitle.setText(title);

        holder.itemView.setTag(holder.getAdapterPosition());
        holder.itemView.setOnClickListener(mClick);
    }

    private class ListViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private AppCompatImageView ivPicture;
        private AppCompatTextView tvTitle;

        private ListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivPicture = (AppCompatImageView) itemView.findViewById(R.id.iv_picture);
            tvTitle = (AppCompatTextView) itemView.findViewById(R.id.tv_title);
        }
    }

    public void setListClick(IOnListClickListener listClick) {
        mListClick = listClick;
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListClick != null) {
                final int position = (int) v.getTag();
                final PictureBean secret = mList.get(position);
                mListClick.onItemClick(v, secret);
            }
        }
    };

}
