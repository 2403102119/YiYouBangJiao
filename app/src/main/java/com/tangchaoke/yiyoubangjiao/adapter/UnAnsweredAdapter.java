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

public class UnAnsweredAdapter extends RecyclerView.Adapter<UnAnsweredAdapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnDeleteClickListener mOnDeleteClickListener;

    List<ProblemBean.ProblemModelBean> mList;

    String mType;

    public void setOnItemCilckLisener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnDeleteClickListenerr(OnDeleteClickListener mOnDeleteClickListener) {
        this.mOnDeleteClickListener = mOnDeleteClickListener;
    }

    public UnAnsweredAdapter(String mType,
                             Context mContext,
                             List<ProblemBean.ProblemModelBean> mList,
                             OnItemClickListener mOnItemClickListener,
                             OnDeleteClickListener mOnDeleteClickListener) {
        this.mType = mType;
        this.mContext = mContext;
        this.mList = mList;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mOnDeleteClickListener = mOnDeleteClickListener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_online_answer_modify, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        ProblemBean.ProblemModelBean mUnAnsweredModel = mList.get(position);

        if (!HGTool.isEmpty(mUnAnsweredModel.getStatus())) {
            if (mUnAnsweredModel.getStatus().equals("0")) {
                /**
                 * 未读
                 */
                holder.mLlOnclick.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else if (mUnAnsweredModel.getStatus().equals("1")) {
                /**
                 * 已读
                 */
                holder.mLlOnclick.setBackgroundColor(mContext.getResources().getColor(R.color.colorf8f8f8));
            }
        }

        if (mType.equals("0")) {
            if (!HGTool.isEmpty(mUnAnsweredModel.getExercisesStatus())) {
                if (mUnAnsweredModel.getExercisesStatus().equals("0")) {
                    holder.mLlDelete.setVisibility(View.VISIBLE);
                    holder.mTvBountyDelete.setVisibility(View.VISIBLE);
                    holder.mTvBounty.setVisibility(View.GONE);
                }
            }
        } else if (mType.equals("1")) {
            holder.mLlDelete.setVisibility(View.GONE);
            holder.mTvBountyDelete.setVisibility(View.GONE);
            holder.mTvBounty.setVisibility(View.VISIBLE);
        } else if (mType.equals("2")) {
            holder.mLlDelete.setVisibility(View.GONE);
            holder.mTvBountyDelete.setVisibility(View.GONE);
            holder.mTvBounty.setVisibility(View.VISIBLE);
        }

        if (!HGTool.isEmpty(mUnAnsweredModel.getHead())) {
            Glide.with(mContext).load(Api.PATH + mUnAnsweredModel.getHead()).centerCrop().into(holder.mImgHead);
        }

        if (!HGTool.isEmpty(mUnAnsweredModel.getNickName())) {
            if (mUnAnsweredModel.getNickName().length() > 11) {
                holder.mTvName.setMaxLines(1);
                holder.mTvName.setMaxEms(6);
                holder.mTvName.setEllipsize(TextUtils.TruncateAt.END);
                holder.mTvName.setSingleLine();
                holder.mTvName.setText(mUnAnsweredModel.getNickName());
            } else {
                holder.mTvName.setText(mUnAnsweredModel.getNickName());
            }
        }

        if (!HGTool.isEmpty(mUnAnsweredModel.getGrade())) {
            holder.mTvGrade.setText(mUnAnsweredModel.getGrade());
        } else if (!HGTool.isEmpty(mUnAnsweredModel.getChessSpecies())) {
            //1国际象棋 2国际跳棋 3围棋 4五子棋 5象棋
            if (mUnAnsweredModel.getChessSpecies().equals("1")) {
                holder.mTvGrade.setText("国际象棋");
            } else if (mUnAnsweredModel.getChessSpecies().equals("2")) {
                holder.mTvGrade.setText("国际跳棋");
            } else if (mUnAnsweredModel.getChessSpecies().equals("3")) {
                holder.mTvGrade.setText("围棋");
            } else if (mUnAnsweredModel.getChessSpecies().equals("4")) {
                holder.mTvGrade.setText("五子棋");
            } else if (mUnAnsweredModel.getChessSpecies().equals("5")) {
                holder.mTvGrade.setText("象棋");
            }
        }

        if (mUnAnsweredModel.getType().equals("1")) {
            holder.mLlSubject.setVisibility(View.VISIBLE);
            if (!HGTool.isEmpty(mUnAnsweredModel.getSubject())) {
                holder.mTvSubject.setText(mUnAnsweredModel.getSubject());
            }
        } else if (mUnAnsweredModel.getType().equals("2")) {
            holder.mLlSubject.setVisibility(View.INVISIBLE);
        }

        if (!HGTool.isEmpty(mUnAnsweredModel.getTitle())) {
            holder.mTvTitle.setText(mUnAnsweredModel.getTitle());
        }

        if (!HGTool.isEmpty(mUnAnsweredModel.getSubmissionTime())) {
            holder.mTvSubmissionTime.setText("发布时间：" + mUnAnsweredModel.getSubmissionTime());
        }

        if (!HGTool.isEmpty(mUnAnsweredModel.getPrice())) {
            holder.mTvBounty.setText("￥ " + mUnAnsweredModel.getPrice());
            if (mUnAnsweredModel.getPrice().equals("0")){
                holder.mTvBountyDelete.setVisibility(View.GONE);
            }else{
                holder.mTvBountyDelete.setVisibility(View.VISIBLE);
                holder.mTvBountyDelete.setText("￥ " + mUnAnsweredModel.getPrice());
            }
        }

        if (!HGTool.isEmpty(mUnAnsweredModel.getPhoto())) {
            holder.mImgQuestionFigure.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(Api.PATH + mUnAnsweredModel.getPhoto()).into(holder.mImgQuestionFigure);
            if (!HGTool.isEmpty(mUnAnsweredModel.getContent().trim())) {
                holder.mTvQuestion.setVisibility(View.VISIBLE);
                holder.mTvQuestion.setMaxLines(2);
                holder.mTvQuestion.setEllipsize(TextUtils.TruncateAt.END);
                holder.mTvQuestion.setText(mUnAnsweredModel.getContent());
            } else {
                holder.mTvQuestion.setVisibility(View.GONE);
            }
        } else {
            holder.mImgQuestionFigure.setVisibility(View.GONE);
            if (!HGTool.isEmpty(mUnAnsweredModel.getContent().trim())) {
                holder.mTvQuestion.setVisibility(View.VISIBLE);
                holder.mTvQuestion.setMaxLines(3);
                holder.mTvQuestion.setEllipsize(TextUtils.TruncateAt.END);
                holder.mTvQuestion.setText(mUnAnsweredModel.getContent());
            } else {
                holder.mTvQuestion.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<ProblemBean.ProblemModelBean> mUnAnsweredModel) {
        mUnAnsweredModel.addAll(mUnAnsweredModel);
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

        LinearLayout mLlSubject;

        TextView mTvSubject;

        TextView mTvTitle;

        TextView mTvSubmissionTime;

        LinearLayout mLlDelete;

        TextView mTvBounty;

        TextView mTvBountyDelete;

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
            mLlDelete = itemView.findViewById(R.id.ll_delete);
            mLlDelete.setOnClickListener(this);
            mTvBounty = itemView.findViewById(R.id.tv_bounty);
            mTvBountyDelete = itemView.findViewById(R.id.tv_bounty_delete);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.ll_onclick:
                    mOnItemClickListener.onItemClick(getLayoutPosition(), view);
                    break;

                case R.id.ll_delete:
                    mOnDeleteClickListener.onDeleteClick(getLayoutPosition(), view);
                    break;

            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int position, View view);
    }

    public interface OnDeleteClickListener {
        public void onDeleteClick(int position, View view);
    }


}