package com.android.emoticoncreater.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.model.PictureBean;
import com.android.emoticoncreater.utils.ImageDataHelper;
import com.android.emoticoncreater.widget.imageloader.ImageLoaderFactory;
import com.android.emoticoncreater.widget.imageloader.SquareImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 表情列表
 */

public class EmoticonListAdapter extends RecyclerView.Adapter {

    private IOnListClickListener mListClick;
    private Context mContext;
    private List<PictureBean> mList;
    private LayoutInflater mInflater;

    public EmoticonListAdapter(Context context, String title) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = new ArrayList<>();

        final int[] pictures;
        if ("熊猫人".equals(title)) {
            pictures = ImageDataHelper.XIONG_MAO_REN_LIST;
        } else if ("插兜".equals(title)) {
            pictures = ImageDataHelper.CHA_DOU_LIST;
        } else if ("北方酱".equals(title)) {
            pictures = ImageDataHelper.BEI_FANG_JIANG_LIST;
        } else {
            pictures = ImageDataHelper.MO_GU_TOU_LIST;
        }

        mList.clear();
        for (int picture : pictures) {
            final PictureBean secret = new PictureBean();
            secret.setResourceId(picture);
            mList.add(secret);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(mInflater.inflate(R.layout.item_emoticon_list, parent, false));
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

        ImageLoaderFactory.getLoader().loadImageFitCenter(mContext, holder.ivPicture, resourceId,
                R.drawable.ic_photo, R.drawable.ic_photo);

        holder.itemView.setTag(holder.getAdapterPosition());
        holder.itemView.setOnClickListener(mClick);

    }

    private class ListViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private SquareImageView ivPicture;

        private ListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivPicture = (SquareImageView) itemView.findViewById(R.id.iv_picture);
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
