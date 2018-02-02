package com.android.emoticoncreater.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.model.ThreeProverbBean;

import java.util.List;

/**
 * 怼人语录列表
 */

public class ThreeProverbListAdapter extends RecyclerView.Adapter {

    private IOnListClickListener mListClick;
    private List<ThreeProverbBean> mList;
    private LayoutInflater mInflater;

    public ThreeProverbListAdapter(Context context, List<ThreeProverbBean> list) {
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(mInflater.inflate(R.layout.item_three_proverb_list, parent, false));
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
        final ThreeProverbBean model = mList.get(position);
        final String title = model.getTitle();
        final String first = model.getFirstProverb();
        final String second = model.getSecondProverb();
        final String third = model.getThirdProverb();
        final String times = "使用次数：" + model.getUseTimes();

        holder.tvTitle.setText(title);
        holder.tvFirst.setText(first);
        holder.tvSecond.setText(second);
        holder.tvThird.setText(third);
        holder.tvUsedTimes.setText(times);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mClick);

        holder.tvDelete.setTag(position);
        holder.tvDelete.setOnClickListener(mClick);
    }

    private class ListViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView tvTitle;
        private TextView tvFirst;
        private TextView tvSecond;
        private TextView tvThird;
        private TextView tvUsedTimes;
        private TextView tvDelete;

        private ListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvFirst = (TextView) itemView.findViewById(R.id.tv_first);
            tvSecond = (TextView) itemView.findViewById(R.id.tv_second);
            tvThird = (TextView) itemView.findViewById(R.id.tv_third);
            tvUsedTimes = (TextView) itemView.findViewById(R.id.tv_used_times);
            tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
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
                switch (v.getId()) {
                    case R.id.tv_delete:
                        mListClick.onTagClick(IOnListClickListener.ITEM_TAG0, position);
                        break;
                    default:
                        mListClick.onItemClick(position);
                        break;
                }
            }
        }
    };
}
