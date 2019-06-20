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
import com.tangchaoke.yiyoubangjiao.model.PointsMallBean;

import java.util.List;

/**
 * Created by Administrator on 2018/10/17.
 */

public class PointsMallAdapter extends RecyclerView.Adapter<PointsMallAdapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    List<PointsMallBean.PointsMallListBean> mPointsMallList;
    private OnItemClickListener mOnItemClickListener;

    int mLayoutSwitch;

    public void setOnItemCilckLisener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public PointsMallAdapter(Context mContext, int mLayoutSwitch,
                             List<PointsMallBean.PointsMallListBean> mPointsMallList, OnItemClickListener mOnItemClickListener) {
        this.mContext = mContext;
        this.mLayoutSwitch = mLayoutSwitch;
        this.mPointsMallList = mPointsMallList;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutSwitch == 2) {
            return new MyViewHold(mLayoutInflater.inflate(R.layout.item_points_mall_grid, parent, false));
        }
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_points_mall, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        PointsMallBean.PointsMallListBean mPointsMallBean = mPointsMallList.get(position);
        if (!HGTool.isEmpty(mPointsMallBean.getPhoto())) {
            Glide.with(mContext).load(Api.PATH + mPointsMallBean.getPhoto()).into(holder.mImgPhoto);
        }

        if (!HGTool.isEmpty(mPointsMallBean.getName())) {
            holder.mTvName.setText(mPointsMallBean.getName());
        }

        if (!HGTool.isEmpty(mPointsMallBean.getIntegral()) && !HGTool.isEmpty(mPointsMallBean.getMoney())) {
            holder.mTvIntegralMoney.setText(mPointsMallBean.getIntegral() + "积分/" + mPointsMallBean.getMoney() + "元");
        }

        if (!HGTool.isEmpty(mPointsMallBean.getSold())) {
            holder.mTvSold.setText("已售" + mPointsMallBean.getSold() + "件");
        }

    }

    @Override
    public int getItemCount() {
        return mPointsMallList.size();
    }

    public void addAll(List<PointsMallBean.PointsMallListBean> mPointsMallBean) {
        mPointsMallBean.addAll(mPointsMallBean);
        notifyDataSetChanged();
    }

    /**
     * 清楚
     */
    public void clear() {
        mPointsMallList.clear();
        notifyDataSetChanged();
    }

    /**
     * 删除某条item
     *
     * @param position
     */
    public void removeItem(int position) {
        mPointsMallList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout mLlOnclick;

        ImageView mImgPhoto;

        TextView mTvName;

        TextView mTvIntegralMoney;

        TextView mTvSold;

        public MyViewHold(View itemView) {
            super(itemView);
            mLlOnclick = itemView.findViewById(R.id.ll_onclick);
            mLlOnclick.setOnClickListener(this);
            mImgPhoto = itemView.findViewById(R.id.img_photo);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvIntegralMoney = itemView.findViewById(R.id.tv_integral_money);
            mTvSold = itemView.findViewById(R.id.tv_sold);
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