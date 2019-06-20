package com.tangchaoke.yiyoubangjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.MessageModel;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2018/3/15.
 */

public class MessageAdapter extends SwipeMenuAdapter<MessageAdapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    List<MessageModel.MessageModelModel> mList;
    private ItemOnClick mOnItemClickListener;

    public List<MessageModel.MessageModelModel> getDatas() {
        return mList;
    }

    public void setOnItemCilckLisener(ItemOnClick mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public MessageAdapter(Context mContext, List<MessageModel.MessageModelModel> mList, ItemOnClick mOnItemClickListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return mLayoutInflater.inflate(R.layout.item_message, parent, false);
    }

    @Override
    public MyViewHold onCompatCreateViewHolder(View realContentView, int viewType) {
        return new MyViewHold(realContentView);
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        MessageModel.MessageModelModel mMessageModel = mList.get(position);
        if (!HGTool.isEmpty(mMessageModel.getMessage().getStatus())) {
            if (mMessageModel.getMessage().getStatus().equals("0")) {
                holder.mTvMessageTitle.setText("系统消息");
            } else if (mMessageModel.getMessage().getStatus().equals("1")) {
                holder.mTvMessageTitle.setText("个人消息");
            }
        }

        if (!HGTool.isEmpty(mMessageModel.getMessage().getSendingTime())) {
            holder.mTvSendingTime.setText(mMessageModel.getMessage().getSendingTime());
        }
        if (!HGTool.isEmpty(mMessageModel.getMessage().getTitle())) {
            holder.mTvMessageInfo.setText(mMessageModel.getMessage().getTitle());
        }

        if (!HGTool.isEmpty(mMessageModel.getStatus())) {
            if (mMessageModel.getStatus().equals("0")) {
                holder.mImgStatus.setVisibility(View.VISIBLE);
            } else if (mMessageModel.getStatus().equals("1")) {
                holder.mImgStatus.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<MessageModel.MessageModelModel> mMessageModel) {
        mMessageModel.addAll(mMessageModel);
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

        TextView mTvMessageTitle;

        TextView mTvSendingTime;

        TextView mTvMessageInfo;

        ImageView mImgStatus;

        public MyViewHold(View itemView) {
            super(itemView);
            mLlOnclick = itemView.findViewById(R.id.ll_onclick);
            mLlOnclick.setOnClickListener(this);
            mTvMessageTitle = itemView.findViewById(R.id.tv_message_title);
            mTvSendingTime = itemView.findViewById(R.id.tv_sending_time);
            mTvMessageInfo = itemView.findViewById(R.id.tv_message_info);
            mImgStatus = itemView.findViewById(R.id.img_status);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.ll_onclick:
                    mOnItemClickListener.onItemOnClick(getLayoutPosition(), view);
                    break;

            }
        }
    }

    public interface ItemOnClick {
        public void onItemOnClick(int position, View view);
    }

}