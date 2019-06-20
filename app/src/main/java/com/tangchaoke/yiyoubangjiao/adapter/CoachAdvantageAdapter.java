package com.tangchaoke.yiyoubangjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;

import java.util.List;

/**
 * Created by Administrator on 2018/10/18.
 */
public class CoachAdvantageAdapter extends RecyclerView.Adapter<CoachAdvantageAdapter.MyViewHold> {

    private Context mContext;
    List<String> mListString;
    private LayoutInflater mLayoutInflater;

    public CoachAdvantageAdapter(Context mContext, List<String> mListString) {
        this.mContext = mContext;
        this.mListString = mListString;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_coach_advantage, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        if (!HGTool.isEmpty(mListString.get(position))) {
            holder.mTvAdvantage.setText(mListString.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mListString.size();
    }

    public class MyViewHold extends RecyclerView.ViewHolder {

        TextView mTvAdvantage;

        public MyViewHold(View itemView) {
            super(itemView);
            mTvAdvantage = itemView.findViewById(R.id.tv_advantage);
        }
    }

}
