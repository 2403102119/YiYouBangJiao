package com.tangchaoke.yiyoubangjiao.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;

import butterknife.BindView;
import butterknife.OnClick;

/*
* @author hg
* create at 2019/1/2
* description: 新闻详情
*/
public class Activity_NewsInfo extends BaseActivity {

    @BindView(R.id.ll_top_activity)
    LinearLayout mLlTopActivity;

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private String mTitle;

    @BindView(R.id.tv_content)
    TextView mTvContent;

    private String mContent;

    @BindView(R.id.img_path)
    ImageView mImgPath;

    private String mPath;

    private String mType = "";

    @OnClick({R.id.ll_back})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_news_info;
    }

    @Override
    public void initTitleBar() {
        if (!HGTool.isEmpty(mType)) {
            if (mType.equals("2")) {
                mLlTopActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_top_activity_club));
                mTvTopTitle.setText("新闻详情");
                mTvTopTitle.setTextColor(getResources().getColor(R.color.color333333));
            } else if (mType.equals("1")) {
                mLlTopActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_top_activity));
                mTvTopTitle.setText("新闻详情");
                mTvTopTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    @Override
    protected void initData() {
        mType = getIntent().getStringExtra("type");
        mTitle = getIntent().getStringExtra("title");
        mContent = getIntent().getStringExtra("content");
        mPath = getIntent().getStringExtra("path");
        if (!HGTool.isEmpty(mTitle)) {
            mTvTitle.setText(mTitle);
        }
        if (!HGTool.isEmpty(mContent)) {
            mTvContent.setText(mContent);
        }
        if (!HGTool.isEmpty(mPath)) {
            Glide.with(Activity_NewsInfo.this).load(Api.PATH + mPath).into(mImgPath);
        }
    }
}
