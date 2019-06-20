package com.tangchaoke.yiyoubangjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.ExpensesRecordModel;

import java.util.List;

/**
 * Created by Administrator on 2018/3/12.
 */

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    List<ExpensesRecordModel.BalanceModelModel> mList;

    public BalanceAdapter(Context mContext, List<ExpensesRecordModel.BalanceModelModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_balance, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        ExpensesRecordModel.BalanceModelModel mBalanceModelModel = mList.get(position);

        if (!HGTool.isEmpty(mBalanceModelModel.getOperationTime())) {
            holder.mTvOperationTime.setText(mBalanceModelModel.getOperationTime());
        }

        if (!HGTool.isEmpty(mBalanceModelModel.getAmountMoney())) {
            if (!HGTool.isEmpty(mBalanceModelModel.getType())) {
                if (mBalanceModelModel.getType().equals("0")) {
                    holder.mTvAmountMoney.setTextColor(mContext.getResources().getColor(R.color.colorFF2626));
                    /**
                     * 提现
                     */
                    if (!HGTool.isEmpty(mBalanceModelModel.getBalanceStatus())) {
                        if (mBalanceModelModel.getBalanceStatus().equals("0")) {
                            if (!HGTool.isEmpty(mBalanceModelModel.getConsumptionContent())) {
                                holder.mTvConsumptionContent.setText(mBalanceModelModel.getConsumptionContent() + "(提现中)");
                            }
                            holder.mTvAmountMoney.setText(" - " + mBalanceModelModel.getAmountMoney());
                        } else if (mBalanceModelModel.getBalanceStatus().equals("1")) {
                            if (!HGTool.isEmpty(mBalanceModelModel.getConsumptionContent())) {
                                holder.mTvConsumptionContent.setText(mBalanceModelModel.getConsumptionContent() + "(提现失败)");
                            }
                            holder.mTvAmountMoney.setText(" + " + mBalanceModelModel.getAmountMoney());
                        } else if (mBalanceModelModel.getBalanceStatus().equals("2")) {
                            if (!HGTool.isEmpty(mBalanceModelModel.getConsumptionContent())) {
                                holder.mTvConsumptionContent.setText(mBalanceModelModel.getConsumptionContent() + "(提现成功)");
                            }
                            holder.mTvAmountMoney.setText(" - " + mBalanceModelModel.getAmountMoney());
                        }
                    }
                } else if (mBalanceModelModel.getType().equals("1")) {
                    holder.mTvConsumptionContent.setText(mBalanceModelModel.getConsumptionContent());
                    /**
                     * 消费
                     */
                    holder.mTvAmountMoney.setText(" - " + mBalanceModelModel.getAmountMoney());
                } else if (mBalanceModelModel.getType().equals("2")) {
                    holder.mTvConsumptionContent.setText(mBalanceModelModel.getConsumptionContent());
                    /**
                     * 金额
                     */
                    holder.mTvAmountMoney.setText(" + " + mBalanceModelModel.getAmountMoney());
                }
            }
        }
        if (!HGTool.isEmpty(mBalanceModelModel.getIntegral())) {
            if (!HGTool.isEmpty(mBalanceModelModel.getConsumptionContent())) {
                holder.mTvConsumptionContent.setText(mBalanceModelModel.getConsumptionContent());
            }
            if (!HGTool.isEmpty(mBalanceModelModel.getType())) {
                if (mBalanceModelModel.getType().equals("1")) {
                    /**
                     * 增加
                     */
                    holder.mTvAmountMoney.setText(" + " + mBalanceModelModel.getIntegral());
                } else if (mBalanceModelModel.getType().equals("2")) {
                    /**
                     * 减少
                     */
                    holder.mTvAmountMoney.setText(" - " + mBalanceModelModel.getIntegral());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<ExpensesRecordModel.BalanceModelModel> mBalanceModelModel) {
        mBalanceModelModel.addAll(mBalanceModelModel);
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

    public class MyViewHold extends RecyclerView.ViewHolder {

        TextView mTvConsumptionContent;

        TextView mTvOperationTime;

        TextView mTvAmountMoney;

        public MyViewHold(View itemView) {
            super(itemView);
            mTvConsumptionContent = itemView.findViewById(R.id.tv_consumption_content);
            mTvOperationTime = itemView.findViewById(R.id.tv_operation_time);
            mTvAmountMoney = itemView.findViewById(R.id.tv_amount_money);
        }
    }

}
