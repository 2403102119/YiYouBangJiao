package com.tangchaoke.yiyoubangjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.tangchaoke.yiyoubangjiao.model.ProblemBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/3/12.
 */

public class InteractiveSolution2Adapter extends RecyclerView.Adapter<InteractiveSolution2Adapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    List<ProblemBean.ProblemModelBean> mList;

    public void setOnItemCilckLisener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public InteractiveSolution2Adapter(Context mContext, List<ProblemBean.ProblemModelBean> mList, OnItemClickListener mOnItemClickListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_online_answer_modify, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        ProblemBean.ProblemModelBean mProblemModel = mList.get(position);

        if (!HGTool.isEmpty(mProblemModel.getHead())) {
            Glide.with(mContext).load(Api.PATH + mProblemModel.getHead()).centerCrop().into(holder.mImgHead);
        }

        if (!HGTool.isEmpty(mProblemModel.getNickName())) {
            if (mProblemModel.getNickName().length() > 11) {
                holder.mTvName.setMaxLines(1);
                holder.mTvName.setMaxEms(6);
                holder.mTvName.setEllipsize(TextUtils.TruncateAt.END);
                holder.mTvName.setSingleLine();
                holder.mTvName.setText(mProblemModel.getNickName());
            } else {
                holder.mTvName.setText(mProblemModel.getNickName());
            }
        }

        if (!HGTool.isEmpty(mProblemModel.getGrade())) {
            holder.mTvGrade.setText(mProblemModel.getGrade());
            holder.mLlSubject.setVisibility(View.VISIBLE);
        } else if (!HGTool.isEmpty(mProblemModel.getChessSpecies())) {
            holder.mLlSubject.setVisibility(View.INVISIBLE);
            //1国际象棋 2国际跳棋 3围棋 4五子棋 5象棋
            if (mProblemModel.getChessSpecies().equals("1")) {
                holder.mTvGrade.setText("国际象棋");
            } else if (mProblemModel.getChessSpecies().equals("2")) {
                holder.mTvGrade.setText("国际跳棋");
            } else if (mProblemModel.getChessSpecies().equals("3")) {
                holder.mTvGrade.setText("围棋");
            } else if (mProblemModel.getChessSpecies().equals("4")) {
                holder.mTvGrade.setText("五子棋");
            } else if (mProblemModel.getChessSpecies().equals("5")) {
                holder.mTvGrade.setText("象棋");
            }
        }

        if (!HGTool.isEmpty(mProblemModel.getSubject())) {
            holder.mTvSubject.setText(mProblemModel.getSubject());
        }

        if (!HGTool.isEmpty(mProblemModel.getTitle())) {
            holder.mTvTitle.setText(mProblemModel.getTitle());
        }

        if (!HGTool.isEmpty(mProblemModel.getSubmissionTime())) {
            holder.mTvSubmissionTime.setText("发布时间：" + mProblemModel.getSubmissionTime());
        }

        if (!HGTool.isEmpty(mProblemModel.getPrice())) {
            if (mProblemModel.getPrice().equals("0")) {
                holder.mTvBounty.setVisibility(View.GONE);
            } else {
                holder.mTvBounty.setVisibility(View.VISIBLE);
                holder.mTvBounty.setText("￥ " + mProblemModel.getPrice());
            }
        }

        if (!HGTool.isEmpty(mProblemModel.getPhoto())) {
            holder.mImgQuestionFigure.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(Api.PATH + mProblemModel.getPhoto()).into(holder.mImgQuestionFigure);
            if (!HGTool.isEmpty(mProblemModel.getContent().trim())) {
                holder.mTvQuestion.setVisibility(View.VISIBLE);
                holder.mTvQuestion.setMaxLines(2);
                holder.mTvQuestion.setEllipsize(TextUtils.TruncateAt.END);
                holder.mTvQuestion.setText(mProblemModel.getContent());
            } else {
                holder.mTvQuestion.setVisibility(View.GONE);
            }
        } else {
            holder.mImgQuestionFigure.setVisibility(View.GONE);
            if (!HGTool.isEmpty(mProblemModel.getContent().trim())) {
                holder.mTvQuestion.setVisibility(View.VISIBLE);
                holder.mTvQuestion.setMaxLines(3);
                holder.mTvQuestion.setEllipsize(TextUtils.TruncateAt.END);
                holder.mTvQuestion.setText(mProblemModel.getContent());
            } else {
                holder.mTvQuestion.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<ProblemBean.ProblemModelBean> mProblemModel) {
        mProblemModel.addAll(mProblemModel);
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

        ImageView mImgQuestionFigure;

        TextView mTvQuestion;

        CircleImageView mImgHead;

        TextView mTvName;

        TextView mTvGrade;

        TextView mTvSubject;

        TextView mTvTitle;

        TextView mTvSubmissionTime;

        TextView mTvBounty;

        LinearLayout mLlSubject;

        public MyViewHold(View itemView) {
            super(itemView);
            mLlOnclick = itemView.findViewById(R.id.ll_onclick);
            mLlOnclick.setOnClickListener(this);
            mImgQuestionFigure = itemView.findViewById(R.id.img_question_figure);
            mTvQuestion = itemView.findViewById(R.id.tv_question);
            mImgHead = itemView.findViewById(R.id.img_head);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvGrade = itemView.findViewById(R.id.tv_grade);
            mLlSubject = itemView.findViewById(R.id.ll_subject);
            mTvSubject = itemView.findViewById(R.id.tv_subject);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvSubmissionTime = itemView.findViewById(R.id.tv_submission_time);
            mTvBounty = itemView.findViewById(R.id.tv_bounty);
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