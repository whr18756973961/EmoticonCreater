package com.android.emoticoncreater.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.model.SecretBean;

import java.util.List;

/**
 * 告诉你个秘密列表
 */

public class SecretListAdapter extends RecyclerView.Adapter {

    private IOnListClickListener mListClick;
    private List<SecretBean> mList;
    private LayoutInflater mInflater;

    public SecretListAdapter(Context context, List<SecretBean> list) {
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(mInflater.inflate(R.layout.item_secret_list, parent, false));
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

        holder.ivPicture.setImageResource(resourceId);
        holder.tvTitle.setText(title);

        holder.ibDelete.setTag(holder.getAdapterPosition());
        holder.ibDelete.setOnClickListener(mClick);
    }

    private class ListViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPicture;
        private TextView tvTitle;
        private ImageButton ibDelete;

        private ListViewHolder(View itemView) {
            super(itemView);
            ivPicture = (ImageView) itemView.findViewById(R.id.iv_picture);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ibDelete = (ImageButton) itemView.findViewById(R.id.ib_delete);
        }
    }

    public void setListClick(IOnListClickListener listClick) {
        this.mListClick = listClick;
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListClick != null) {
                final int position = (int) v.getTag();
                mListClick.onTagClick(IOnListClickListener.ITEM_TAG0, position);
            }
        }
    };

}
