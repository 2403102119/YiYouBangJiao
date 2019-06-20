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
import com.hedgehog.ratingbar.RatingBar;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.CommentBean;
import com.tangchaoke.yiyoubangjiao.model.CommodityInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2018/10/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    List<CommentBean.CommentListBean> mList;

    public CommentAdapter(Context mContext, List<CommentBean.CommentListBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_points_mall_info_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        CommentBean.CommentListBean mCommentListBean = mList.get(position);
        if (!HGTool.isEmpty(mCommentListBean.getUserInfoHead())) {
            Glide.with(mContext).load(Api.PATH + mCommentListBean.getUserInfoHead()).into(holder.mImgUserInfoHead);
        }

        if (!HGTool.isEmpty(mCommentListBean.getUserInfoNickName())) {
            holder.mTvUserInfoNickName.setText(mCommentListBean.getUserInfoNickName());
        }

        if (!HGTool.isEmpty(mCommentListBean.getCommentTime())) {
            holder.mTvCommentTime.setText(mCommentListBean.getCommentTime());
        }

        if (!HGTool.isEmpty(mCommentListBean.getCommentContent())) {
            holder.mTvCommentContent.setText(mCommentListBean.getCommentContent());
        }

        if (!HGTool.isEmpty(mCommentListBean.getStar())) {
            float markF = Float.valueOf(mCommentListBean.getStar());
            holder.mRatingBar.setStar(markF);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<CommentBean.CommentListBean> mList) {
        mList.addAll(mList);
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

    public class MyViewHold extends RecyclerView.ViewHolder {

        LinearLayout mLlOnclick;

        ImageView mImgUserInfoHead;

        TextView mTvUserInfoNickName;

        TextView mTvCommentTime;

        TextView mTvCommentContent;

        RatingBar mRatingBar;

        public MyViewHold(View itemView) {
            super(itemView);
            mLlOnclick = itemView.findViewById(R.id.ll_onclick);
            mImgUserInfoHead = itemView.findViewById(R.id.img_userInfoHead);
            mTvUserInfoNickName = itemView.findViewById(R.id.tv_userInfoNickName);
            mTvCommentTime = itemView.findViewById(R.id.tv_commentTime);
            mTvCommentContent = itemView.findViewById(R.id.tv_commentContent);
            mRatingBar = itemView.findViewById(R.id.rating_bar);
        }

    }

}