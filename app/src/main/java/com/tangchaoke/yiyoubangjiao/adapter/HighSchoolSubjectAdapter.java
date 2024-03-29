package com.tangchaoke.yiyoubangjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/13.
 */

public class HighSchoolSubjectAdapter extends RecyclerView.Adapter<HighSchoolSubjectAdapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    List<String> mList;

    private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
    private boolean mIsSelectable = false;

    public void setOnItemCilckLisener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public HighSchoolSubjectAdapter(Context mContext, List<String> mList, OnItemClickListener mOnItemClickListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_educational_features, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        if (!HGTool.isEmpty(mList.get(position))) {
            holder.mTvName.setText(mList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //获得选中条目的结果
    public ArrayList<String> getSelectedItem() {
        ArrayList<String> selectList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            if (isItemChecked(i)) {
                selectList.add(mList.get(i));
            }
        }
        return selectList;
    }

    //设置给定位置条目的选择状态
    public void setItemChecked(int position, boolean isChecked) {
        mSelectedPositions.put(position, isChecked);
    }

    //根据位置判断条目是否选中
    public boolean isItemChecked(int position) {
        return mSelectedPositions.get(position);
    }

    public class MyViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout mLlOnclick;

        TextView mTvName;

        public MyViewHold(View itemView) {
            super(itemView);
            mLlOnclick = itemView.findViewById(R.id.ll_onclick);
            mLlOnclick.setOnClickListener(this);
            mTvName = itemView.findViewById(R.id.tv_name);
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