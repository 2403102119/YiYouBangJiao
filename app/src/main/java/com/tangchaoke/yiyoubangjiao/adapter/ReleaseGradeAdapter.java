package com.tangchaoke.yiyoubangjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;

import java.util.List;

/**
 * 发布问题选择班级 年级 适配器
 */
public class ReleaseGradeAdapter extends RecyclerView.Adapter<ReleaseGradeAdapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    List<String> mGradeList;
    private OnItemClickListener mOnItemClickListener;

    private int mSelectedPos = -1;//保存当前选中的position 重点!

    public void setOnItemCilckLisener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public ReleaseGradeAdapter(int mSelectedPos, Context mContext, List<String> mGradeList, OnItemClickListener mOnItemClickListener) {
        this.mSelectedPos = mSelectedPos;
        this.mContext = mContext;
        this.mGradeList = mGradeList;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_release, parent, false));
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
            if (mSelectedPos == position) {//当前选中的position和上次选中不是同一个position 执行
                holder.mLlOnclick.setBackgroundResource(R.drawable.select_blue_gradient_fillet_20);
                holder.mTvReleaseGrade.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else {
                holder.mLlOnclick.setBackgroundResource(R.drawable.shape_white_fillet20_line);
                holder.mTvReleaseGrade.setTextColor(mContext.getResources().getColor(R.color.color333333));
            }
        } else {
            //当调用notifyItemChange(int position, Object payload)进行布局刷新时，
            // payloads不会empty ，
            // 所以真正的布局刷新应该在这里实现 重点！
            holder.mLlOnclick.setBackgroundResource(R.drawable.shape_white_fillet20_line);
            holder.mTvReleaseGrade.setTextColor(mContext.getResources().getColor(R.color.color333333));
        }

        if (!HGTool.isEmpty(mGradeList.get(position))) {
            holder.mTvReleaseGrade.setText(mGradeList.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return mGradeList.size();
    }

    public class MyViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout mLlOnclick;
        TextView mTvReleaseGrade;

        public MyViewHold(View itemView) {
            super(itemView);
            mLlOnclick = itemView.findViewById(R.id.ll_onclick);
            mLlOnclick.setOnClickListener(this);
            mTvReleaseGrade = itemView.findViewById(R.id.tv_release_grade);
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