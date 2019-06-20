package com.tangchaoke.yiyoubangjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.TeacherInfoModel;

import java.util.List;

/**
 * Created by Administrator on 2018/4/7.
 */

public class HighSchoolAdapter extends RecyclerView.Adapter<HighSchoolAdapter.MyViewHold> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    List<TeacherInfoModel.TeacherInfoModelModel.TeacherInfoModelHighSchoolModel> mList;

    public HighSchoolAdapter(Context mContext,
                             List<TeacherInfoModel.TeacherInfoModelModel.TeacherInfoModelHighSchoolModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHold(mLayoutInflater.inflate(R.layout.item_parmary_school, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        TeacherInfoModel.TeacherInfoModelModel.TeacherInfoModelHighSchoolModel mHomeMode12CharacterModel = mList.get(position);
        if (!HGTool.isEmpty(mHomeMode12CharacterModel.getRangenian()) || !HGTool.isEmpty(mHomeMode12CharacterModel.getRangeke())) {
            holder.mTvGradeSubject.setText(mHomeMode12CharacterModel.getRangenian() + " : " + mHomeMode12CharacterModel.getRangeke());
        }
    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class MyViewHold extends RecyclerView.ViewHolder {

        TextView mTvGradeSubject;

        public MyViewHold(View itemView) {
            super(itemView);
            mTvGradeSubject = itemView.findViewById(R.id.tv_grade_subject);
        }
    }

}