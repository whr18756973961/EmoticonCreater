package com.android.emoticoncreater.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.model.GifTheme;

import java.util.List;

/**
 * Gif 主题类型
 */
public class GifThemeListAdapter extends RecyclerView.Adapter {

    private List<GifTheme> mDatas;
    private LayoutInflater mInflater;
    private IOnListClickListener mListClick;

    public GifThemeListAdapter(Context context, List<GifTheme> datas) {
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(mInflater.inflate(R.layout.item_gif_theme_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bindItem((BaseViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    private void bindItem(BaseViewHolder holder, final int position) {
        final GifTheme data = mDatas.get(position);
        final String name = data.getName();

        holder.btnTheme.setText(name);
        holder.btnTheme.setTag(position);
        holder.btnTheme.setOnClickListener(mClick);
    }

    private class BaseViewHolder extends RecyclerView.ViewHolder {

        private Button btnTheme;

        private BaseViewHolder(View itemView) {
            super(itemView);
            btnTheme = itemView.findViewById(R.id.btn_theme);
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
                mListClick.onItemClick(position);
            }
        }
    };

}
