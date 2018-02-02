package com.android.emoticoncreater.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.model.SecretBean;
import com.android.emoticoncreater.widget.imageloader.ImageLoaderFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 秘密表情列表
 */

public class SecretPictureListAdapter extends RecyclerView.Adapter {

    private IOnListClickListener mListClick;
    private Context mContext;
    private List<SecretBean> mList;
    private LayoutInflater mInflater;

    public SecretPictureListAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);

        final int[] resourceIds = {
                R.drawable.img_1, R.drawable.img_2, R.drawable.img_3,
                R.drawable.img_4, R.drawable.img_5, R.drawable.img_6,
                R.drawable.img_7, R.drawable.img_8, R.drawable.img_9, R.drawable.img_10
        };
        final String[] titles = {
                "伏尔泰 + 乌蝇哥", "教皇 + 乌蝇哥", "金馆长 + 乌蝇哥",
                "伏尔泰 + 教皇", "教皇 + 教皇", "金馆长 + 教皇",
                "伏尔泰 + 教皇", "教皇 + 教皇", "金馆长 + 教皇", "教皇 + 教皇"
        };
        mList.clear();
        for (int i = 0; i < resourceIds.length; i++) {
            final SecretBean secret = new SecretBean();
            secret.setResourceId(resourceIds[i]);
            secret.setTitle(titles[i]);
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
        final SecretBean model = mList.get(position);
        final int resourceId = model.getResourceId();
        final String title = model.getTitle();

        ImageLoaderFactory.getLoader().loadImageFitCenter(mContext, holder.ivPicture, resourceId, 0, 0);

        holder.tvTitle.setText(title);

        holder.itemView.setTag(holder.getAdapterPosition());
        holder.itemView.setOnClickListener(mClick);
    }

    private class ListViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private ImageView ivPicture;
        private TextView tvTitle;

        private ListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivPicture = (ImageView) itemView.findViewById(R.id.iv_picture);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
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
                mListClick.onItemClick(position);
            }
        }
    };

}