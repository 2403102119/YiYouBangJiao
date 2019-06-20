package com.tangchaoke.yiyoubangjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.tangchaoke.yiyoubangjiao.model.ClubModel;

import java.util.List;

/**
 * Created by Administrator on 2018/10/17.
 */

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    List<ClubModel.ClubListModel> mClubList;

    public void setOnItemCilckLisener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public ClubAdapter(Context mContext, List<ClubModel.ClubListModel> mClubList, OnItemClickListener mOnItemClickListener) {
        this.mContext = mContext;
        this.mClubList = mClubList;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_club, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        ClubModel.ClubListModel mList = mClubList.get(position);

        if (!HGTool.isEmpty(mList.getLogo())) {
            Glide.with(mContext).load(Api.PATH + mList.getLogo()).into(holder.mImgClubLogo);
        }

        if (!HGTool.isEmpty(mList.getName())) {
            holder.mTvClubName.setText(mList.getName());
        }

        if (!HGTool.isEmpty(mList.getCity().get(0).getName())
                && !HGTool.isEmpty(mList.getArea().get(0).getName())) {
            holder.mTvClubCityArea.setText(mList.getCity().get(0).getName() + "   " + mList.getArea().get(0).getName());
        }

        if (!HGTool.isEmpty(mList.getBriefIntroduction())) {
            holder.mTvBriefIntroduction.setText(mList.getBriefIntroduction());
        }

    }

    @Override
    public int getItemCount() {
        return mClubList.size();
    }

    public void addAll(List<ClubModel.ClubListModel> mClubList) {
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

        ImageView mImgClubLogo;

        TextView mTvClubName;

        TextView mTvClubCityArea;

        TextView mTvBriefIntroduction;

        public MyViewHold(View itemView) {
            super(itemView);
            mLlOnclick = itemView.findViewById(R.id.ll_onclick);
            mLlOnclick.setOnClickListener(this);
            mImgClubLogo = itemView.findViewById(R.id.img_club_logo);
            mTvClubName = itemView.findViewById(R.id.tv_club_name);
            mTvClubCityArea = itemView.findViewById(R.id.tv_club_city_area);
            mTvBriefIntroduction = itemView.findViewById(R.id.tv_briefIntroduction);
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