package com.tangchaoke.yiyoubangjiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.SpecialConsultantBean;

import java.util.ArrayList;
import java.util.List;

import static com.tangchaoke.yiyoubangjiao.api.Api.PATH;

/*
* @author hg
* create at 2019/1/2
* description: 特约顾问  适配器
*/
public class SpecialConsultantAdapter extends RecyclerView.Adapter<SpecialConsultantAdapter.MyHolder> {

    Context mContext;
    List<SpecialConsultantBean.SpecialConsultantModelBean> model;

    public SpecialConsultantAdapter(Context mContext, List<SpecialConsultantBean.SpecialConsultantModelBean> model) {
        this.mContext = mContext;
        this.model = model;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_special_consultant, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        if (!HGTool.isEmpty(Api.PATH + model.get(position).getPath())) {
//            Glide.with(mContext).load(Api.PATH + model.get(position).getPath()).into(holder.mImgPath);
            Glide.with(mContext).load(Api.PATH + model.get(position).getPath())
                    .error(R.drawable.ic_special_consultant_bg_item)
                    .placeholder(R.drawable.ic_special_consultant_bg_item)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            holder.mImgPath.setImageDrawable(resource);
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView mImgPath;

        public MyHolder(View itemView) {
            super(itemView);
            mImgPath = itemView.findViewById(R.id.img_path);
        }

    }

}