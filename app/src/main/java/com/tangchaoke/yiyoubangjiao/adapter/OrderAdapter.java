package com.tangchaoke.yiyoubangjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.OrderBean;

import java.util.List;

/**
 * Created by Administrator on 2018/10/18.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    List<OrderBean.OrderListBean> mList;
    private int mType;
    private OnItemClickListener mOnItemClickListener;
    private OnEvaluationClickListener mOnEvaluationClickListener;

    public void setOnItemCilckLisener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnEvaluationCilckLisener(OnEvaluationClickListener mOnEvaluationClickListener) {
        this.mOnEvaluationClickListener = mOnEvaluationClickListener;
    }

    public OrderAdapter(Context mContext, int mType,
                        List<OrderBean.OrderListBean> mList,
                        OnItemClickListener mOnItemClickListener,
                        OnEvaluationClickListener mOnEvaluationClickListener) {
        this.mContext = mContext;
        this.mType = mType;
        this.mList = mList;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mOnEvaluationClickListener = mOnEvaluationClickListener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        OrderBean.OrderListBean mOrderList = mList.get(position);

        if (!HGTool.isEmpty(mOrderList.getOrderNumber())) {
            holder.mTvOrderNumber.setText("订单编号：" + mOrderList.getOrderNumber());
        }

        if (!HGTool.isEmpty(mOrderList.getOrderStatus())) {
            if (mOrderList.getOrderStatus().equals("0")) {
                holder.mTvOrderStatus.setText("待支付");
                holder.mButOrder.setVisibility(View.VISIBLE);
                holder.mButOrder.setText("去支付");
            } else if (mOrderList.getOrderStatus().equals("1")) {
                holder.mTvOrderStatus.setText("待发货");
                holder.mButOrder.setVisibility(View.INVISIBLE);
            } else if (mOrderList.getOrderStatus().equals("2")) {
                holder.mTvOrderStatus.setText("待收货");
                holder.mButOrder.setVisibility(View.VISIBLE);
                holder.mButOrder.setText("确认完成");
            } else if (mOrderList.getOrderStatus().equals("3")) {
                holder.mTvOrderStatus.setText("待评价");
                holder.mButOrder.setVisibility(View.VISIBLE);
                holder.mButOrder.setText("发表评论");
            } else if (mOrderList.getOrderStatus().equals("4")) {
                holder.mTvOrderStatus.setText("已完成");
                holder.mButOrder.setVisibility(View.INVISIBLE);
            }
        }

        if (!HGTool.isEmpty(mOrderList.getCommodity().getName())) {
            holder.mTvCommodityName.setText(mOrderList.getCommodity().getName());
        }

        if (!HGTool.isEmpty(mOrderList.getCommodity().getMaterial())) {
            holder.mTvCommodityMaterial.setText(mOrderList.getCommodity().getMaterial());
        }

        if (!HGTool.isEmpty(mOrderList.getCommodity().getPhoto())) {
            Glide.with(mContext).load(Api.PATH + mOrderList.getCommodity().getPhoto()).into(holder.mImgCommodityPhoto);
        }

        if (!HGTool.isEmpty(mOrderList.getPayType())) {
            if (mOrderList.getPayType().equals("1")) {
                holder.mTvPayType.setText("积分：" + mOrderList.getAllIntegral());
            } else if (mOrderList.getPayType().equals("2") || mOrderList.getPayType().equals("3") || mOrderList.getPayType().equals("4")) {
                holder.mTvPayType.setText("金额：" + mOrderList.getAllMoney());
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<OrderBean.OrderListBean> mList) {
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

        Button mButOrder;

        TextView mTvOrderNumber;

        TextView mTvOrderStatus;

        TextView mTvCommodityName;

        TextView mTvCommodityMaterial;

        ImageView mImgCommodityPhoto;

        TextView mTvPayType;

        public MyViewHold(View itemView) {
            super(itemView);
            mLlOnclick = itemView.findViewById(R.id.ll_onclick);
            mLlOnclick.setOnClickListener(this);
            mButOrder = itemView.findViewById(R.id.but_order);
            mButOrder.setOnClickListener(this);
            mTvOrderNumber = itemView.findViewById(R.id.tv_orderNumber);
            mTvOrderStatus = itemView.findViewById(R.id.tv_orderStatus);
            mTvCommodityName = itemView.findViewById(R.id.tv_commodity_name);
            mTvCommodityMaterial = itemView.findViewById(R.id.tv_commodity_material);
            mImgCommodityPhoto = itemView.findViewById(R.id.img_commodity_photo);
            mTvPayType = itemView.findViewById(R.id.tv_payType);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.ll_onclick:
                    mOnItemClickListener.onItemClick(getLayoutPosition(), view);
                    break;

                case R.id.but_order:
                    mOnEvaluationClickListener.onEvaluationClick(getLayoutPosition(), view);
                    break;

            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int position, View view);
    }

    public interface OnEvaluationClickListener {
        public void onEvaluationClick(int position, View view);
    }

}