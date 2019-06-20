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
import com.tangchaoke.yiyoubangjiao.model.CommodityInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2018/10/17.
 */

public class PointsMallInfoCommentAdapter extends RecyclerView.Adapter<PointsMallInfoCommentAdapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    List<CommodityInfoBean.CommodityInfoMapBean.CommodityInfoMapCommentBean> mComment;

    public PointsMallInfoCommentAdapter(Context mContext, List<CommodityInfoBean.CommodityInfoMapBean.CommodityInfoMapCommentBean> mComment) {
        this.mContext = mContext;
        this.mComment = mComment;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_points_mall_info_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        CommodityInfoBean.CommodityInfoMapBean.CommodityInfoMapCommentBean mCommodityInfoBean = mComment.get(position);
        if (!HGTool.isEmpty(mCommodityInfoBean.getUserInfoHead())) {
            Glide.with(mContext).load(Api.PATH + mCommodityInfoBean.getUserInfoHead()).into(holder.mImgUserInfoHead);
        }

        if (!HGTool.isEmpty(mCommodityInfoBean.getUserInfoNickName())) {
            holder.mTvUserInfoNickName.setText(mCommodityInfoBean.getUserInfoNickName());
        }

        if (!HGTool.isEmpty(mCommodityInfoBean.getCommentTime())) {
            holder.mTvCommentTime.setText(mCommodityInfoBean.getCommentTime());
        }

        if (!HGTool.isEmpty(mCommodityInfoBean.getCommentContent())) {
            holder.mTvCommentContent.setText(mCommodityInfoBean.getCommentContent());
        }

        if (!HGTool.isEmpty(mCommodityInfoBean.getStar())) {
            float markF = Float.valueOf(mCommodityInfoBean.getStar());
            holder.mRatingBar.setStar(markF);
        }

    }

    @Override
    public int getItemCount() {
        return mComment.size();
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