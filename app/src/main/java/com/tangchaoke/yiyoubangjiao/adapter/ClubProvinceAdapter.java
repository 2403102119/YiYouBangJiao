package com.tangchaoke.yiyoubangjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.model.ClubProvinceInfoModel;

import java.util.List;

/**
 * Created by Administrator on 2018/10/26.
 */

public class ClubProvinceAdapter extends RecyclerView.Adapter<ClubProvinceAdapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    List<ClubProvinceInfoModel.ClubProvinceInfoListModel> mList;

    private int mSelectProvincePos = -1;//保存当前选中的position 重点!

    public void setOnItemCilckLisener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public ClubProvinceAdapter(int mSelectProvincePos, Context mContext,
                               List<ClubProvinceInfoModel.ClubProvinceInfoListModel> mList,
                               OnItemClickListener mOnItemClickListener) {
        this.mSelectProvincePos = mSelectProvincePos;
        this.mContext = mContext;
        this.mList = mList;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_club_pop, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {

    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty()) {
            //payloads即有效负载，
            // 当首次加载或调用notifyDatasetChanged() ,
            // notifyItemChange(int position)进行刷新时，
            // payloads为empty 即空
            holder.mLlOnclick.setBackgroundResource(R.color.colorPrimary);
            holder.mTvClubName.setTextColor(mContext.getResources().getColor(R.color.color666666));
        } else {
            //当调用notifyItemChange(int position, Object payload)进行布局刷新时，
            // payloads不会empty ，
            // 所以真正的布局刷新应该在这里实现 重点！
            holder.mLlOnclick.setBackgroundResource(R.color.colorPrimary);
            holder.mTvClubName.setTextColor(mContext.getResources().getColor(R.color.color666666));
        }
        holder.mTvClubName.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout mLlOnclick;

        TextView mTvClubName;

        public MyViewHold(View itemView) {
            super(itemView);
            mLlOnclick = itemView.findViewById(R.id.ll_onclick);
            mLlOnclick.setOnClickListener(this);
            mTvClubName = itemView.findViewById(R.id.tv_club_name);
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