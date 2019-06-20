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
import com.tangchaoke.yiyoubangjiao.model.HomeBean;

import java.util.List;

/*
* @author hg
* create at 2019/1/2
* description: 首页 新闻资讯 适配器
*/
public class HomeNewsAdapter extends RecyclerView.Adapter<HomeNewsAdapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    List<HomeBean.HomeMode12Bean> mList;

    public void setOnItemCilckLisener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public HomeNewsAdapter(Context mContext, List<HomeBean.HomeMode12Bean> mList, OnItemClickListener mOnItemClickListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        HomeBean.HomeMode12Bean mHomeModel = mList.get(position);
        if (!HGTool.isEmpty(mHomeModel.getPath())) {
            Glide.with(mContext).load(Api.PATH + mHomeModel.getPath()).into(holder.mImgPath);
        }

        if (!HGTool.isEmpty(mHomeModel.getTitle())) {
            holder.mTvTitle.setText(mHomeModel.getTitle());
        }

        if (!HGTool.isEmpty(mHomeModel.getContent())) {
            holder.mTvContent.setText(mHomeModel.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<HomeBean.HomeMode12Bean> mHomeMode12) {
        mHomeMode12.addAll(mHomeMode12);
        notifyDataSetChanged();
    }

    /**
     * 清楚
     */
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * 删除某条item
     *
     * @param position
     */
    public void removeItem(int position) {
        mList.remove(position);
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