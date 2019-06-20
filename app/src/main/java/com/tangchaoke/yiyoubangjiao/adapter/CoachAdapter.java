package com.tangchaoke.yiyoubangjiao.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.CoachBean;
import com.tangchaoke.yiyoubangjiao.view.RoundImageView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/10/18.
 */

public class CoachAdapter extends RecyclerView.Adapter<CoachAdapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    String mChessType;
    private OnItemClickListener mOnItemClickListener;
    CoachAdvantageAdapter mCoachAdvantageAdapter;

    List<CoachBean.CoachListBean> mList;

    public void setOnItemCilckLisener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public CoachAdapter(Context mContext, String mChessType, List<CoachBean.CoachListBean> mList, OnItemClickListener mOnItemClickListener) {
        this.mContext = mContext;
        this.mChessType = mChessType;
        this.mList = mList;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mChessType.equals("2")) {
            return new MyViewHold(mLayoutInflater.inflate(R.layout.item_club_teacher, parent, false));
        }
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_coach, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        CoachBean.CoachListBean mListModel = mList.get(position);

        if (!HGTool.isEmpty(mListModel.getName())) {
            holder.mTvTeacherNickName.setText(mListModel.getName());
        }

        if (!HGTool.isEmpty(mListModel.getCity().getName()) && !HGTool.isEmpty(mListModel.getType())
                && !HGTool.isEmpty(mListModel.getSex()) && !HGTool.isEmpty(mListModel.getAge())) {
            String mType = "";
            if (mChessType.equals("1")) {
                mType = mListModel.getType().replace(",", " | ");
                if (mListModel.getSex().equals("1")) {
                    holder.mTvCityTypeSexAge.setText(mListModel.getCity().getName()
                            + " | " + "男"
                            + " | " + mListModel.getAge());
                    holder.mTvTypeChess.setText(mType);
                    if (!HGTool.isEmpty(mListModel.getHead())) {
                        Glide.with(mContext).load(Api.PATH + mListModel.getHead()).centerCrop().into(holder.mImgTeacherHead);
                    } else {
                        Glide.with(mContext).load(R.drawable.ic_nan).into(holder.mImgTeacherHead);
                    }
                } else if (mListModel.getSex().equals("2")) {
                    holder.mTvCityTypeSexAge.setText(mListModel.getCity().getName()
                            + " | " + "女"
                            + " | " + mListModel.getAge());
                    holder.mTvTypeChess.setText(mType);
                    if (!HGTool.isEmpty(mListModel.getHead())) {
                        Glide.with(mContext).load(Api.PATH + mListModel.getHead()).centerCrop().into(holder.mImgTeacherHead);
                    } else {
                        Glide.with(mContext).load(R.drawable.ic_nv).into(holder.mImgTeacherHead);
                    }
                }
            } else if (mChessType.equals("2")) {
                if (mListModel.getType().equals("1")) {
                    mType = "国际象棋";
                } else if (mListModel.getType().equals("2")) {
                    mType = "国际跳棋";
                } else if (mListModel.getType().equals("3")) {
                    mType = "围棋";
                } else if (mListModel.getType().equals("4")) {
                    mType = "五子棋";
                } else if (mListModel.getType().equals("5")) {
                    mType = "象棋";
                }
                if (mListModel.getSex().equals("1")) {
                    holder.mTvCityTypeSexAge.setText(mListModel.getCity().getName()
                            + " | " + mType
                            + " | " + "男"
                            + " | " + mListModel.getAge());
                    if (!HGTool.isEmpty(mListModel.getHead())) {
                        Glide.with(mContext).load(Api.PATH + mListModel.getHead()).centerCrop().into(holder.mImgTeacherHead);
                    } else {
                        Glide.with(mContext).load(R.drawable.ic_nan).into(holder.mImgTeacherHead);
                    }
                } else if (mListModel.getSex().equals("2")) {
                    holder.mTvCityTypeSexAge.setText(mListModel.getCity().getName()
                            + " | " + mType
                            + " | " + "女"
                            + " | " + mListModel.getAge());
                    if (!HGTool.isEmpty(mListModel.getHead())) {
                        Glide.with(mContext).load(Api.PATH + mListModel.getHead()).centerCrop().into(holder.mImgTeacherHead);
                    } else {
                        Glide.with(mContext).load(R.drawable.ic_nv).into(holder.mImgTeacherHead);
                    }
                }
            }
        }

        if (!HGTool.isEmpty(mListModel.getPrice())) {
            holder.mTvStartPrice.setText(mListModel.getPrice());
            holder.mLlPrice.setVisibility(View.VISIBLE);
        } else {
            holder.mLlPrice.setVisibility(View.INVISIBLE);
        }

        String[] arr = mListModel.getCharacteristicsofEducation().split(",");
        List<String> mListString = Arrays.asList(arr);

        initRecyclerFindTeachAdvantage(holder.mRecyclerCoachAdvantage, mListString);
    }

    private void initRecyclerFindTeachAdvantage(RecyclerView mRecyclerCoachAdvantage, List<String> mListString) {
        mRecyclerCoachAdvantage.setHasFixedSize(true);
        mRecyclerCoachAdvantage.setNestedScrollingEnabled(false);
        mRecyclerCoachAdvantage.setLayoutManager(new GridLayoutManager(mContext, 4));
        mRecyclerCoachAdvantage.setHasFixedSize(true);
        mRecyclerCoachAdvantage.setItemAnimator(new DefaultItemAnimator());
        mCoachAdvantageAdapter = new CoachAdvantageAdapter(mContext, mListString);
        mRecyclerCoachAdvantage.setAdapter(mCoachAdvantageAdapter);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<CoachBean.CoachListBean> mList) {
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

    public class MyViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout mLlOnclick;

        RecyclerView mRecyclerCoachAdvantage;

        TextView mTvTeacherNickName;

        TextView mTvCityTypeSexAge;

        TextView mTvStartPrice;

        RoundImageView mImgTeacherHead;

        LinearLayout mLlPrice;

        TextView mTvTypeChess;

        public MyViewHold(View itemView) {
            super(itemView);
            mLlOnclick = itemView.findViewById(R.id.ll_onclick);
            mLlOnclick.setOnClickListener(this);
            mRecyclerCoachAdvantage = itemView.findViewById(R.id.recycler_coach_advantage);
            mTvTeacherNickName = itemView.findViewById(R.id.tv_teacher_nick_name);
            mTvCityTypeSexAge = itemView.findViewById(R.id.tv_city_type_sex_age);
            mLlPrice = itemView.findViewById(R.id.ll_price);
            mTvStartPrice = itemView.findViewById(R.id.tv_start_price);
            mImgTeacherHead = itemView.findViewById(R.id.img_teacher_head);
            mTvTypeChess = itemView.findViewById(R.id.tv_type_chess);
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