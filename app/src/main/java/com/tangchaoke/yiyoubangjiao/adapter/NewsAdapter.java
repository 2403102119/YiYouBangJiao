package com.tangchaoke.yiyoubangjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.NewsBean;

import java.util.List;

/**
 * Created by Administrator on 2018/10/29.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHold> {

    private Context mContext;
    List<NewsBean.NewsListBean> mClubList;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemCilckLisener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public NewsAdapter(Context mContext, List<NewsBean.NewsListBean> mClubList, OnItemClickListener mOnItemClickListener) {
        this.mContext = mContext;
        this.mClubList = mClubList;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        NewsBean.NewsListBean mClubStyleListModel = mClubList.get(position);
        if (!HGTool.isEmpty(mClubStyleListModel.getPath())) {
            Glide.with(mContext).load(Api.PATH + mClubStyleListModel.getPath()).into(holder.mImgPath);
        }

        if (!HGTool.isEmpty(mClubStyleListModel.getTitle())) {
            holder.mTvTitle.setText(mClubStyleListModel.getTitle());
        }

        if (!HGTool.isEmpty(mClubStyleListModel.getContent())) {
            holder.mTvContent.setText(mClubStyleListModel.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mClubList.size();
    }

    public void addAll(List<NewsBean.NewsListBean> mClubList) {
        mClubList.addAll(mClubList);
        notifyDataSetChanged();
    }

    /**
     * 清楚
     */
    public void clear() {
        mClubList.clear();
        notifyDataSetChanged();
    }

    /**
     * 删除某条item
     *
     * @param position
     */
    public void removeItem(int position) {
        mClubList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout mLlOnclick;

        ImageView mImgPath;

        TextView mTvTitle;

        TextView mTvContent;


        public MyViewHold(View itemView) {
            super(itemView);
            mLlOnclick = itemView.findViewById(R.id.ll_onclick);
            mLlOnclick.setOnClickListener(this);
            mImgPath = itemView.findViewById(R.id.img_path);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvContent = itemView.findViewById(R.id.tv_content);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.ll_onclick:
                    mOnItemClickListener.onItemClick(getLayoutPosition(), view);
                    break;

            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int position, View view);
    }

}