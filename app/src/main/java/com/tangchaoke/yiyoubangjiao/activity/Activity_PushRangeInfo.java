package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.HighSchoolAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.PushRangeHighSchoolAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.PushRangeInfoAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.PushRangeMiddkeSchoolAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.model.PushRangeInfoModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 查看推送题目范围
*/
public class Activity_PushRangeInfo extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.recycler_view_primary_school)
    RecyclerView mRecyclerViewPrimarySchool;

    PushRangeInfoAdapter mPushRangeInfoAdapter;

    @BindView(R.id.recycler_view_middle_school)
    RecyclerView mRecyclerViewMiddleSchool;

    PushRangeMiddkeSchoolAdapter mPushRangeMiddkeSchoolAdapter;

    @BindView(R.id.recycler_view_high_school)
    RecyclerView mRecyclerViewHighSchool;

    PushRangeHighSchoolAdapter mPushRangeHighSchoolAdapter;

    @BindView(R.id.tv_title_primary_school)
    TextView mTvTitlePrimarySchool;

    @BindView(R.id.tv_title_middle_school)
    TextView mTvTitleMiddleSchool;

    @BindView(R.id.tv_title_high_school)
    TextView mTvTitleHighSchool;

    @BindView(R.id.ll_adoption)
    LinearLayout mLlAdoption;

    @BindView(R.id.tv_tight_title)
    TextView mTvTightTitle;

    @BindView(R.id.ll_primary_school)
    LinearLayout mLlPrimarySchool;

    @BindView(R.id.ll_junior_high_school)
    LinearLayout mLlJuniorHighSchool;

    @BindView(R.id.ll_high_school)
    LinearLayout mLlHighSchool;

    @OnClick({R.id.ll_back, R.id.ll_adoption})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.ll_adoption:
                Intent mIntentPushRange = new Intent(Activity_PushRangeInfo.this, Activity_PushRange.class);
                mIntentPushRange.putExtra("type", "2");
                mIntentPushRange.putExtra("info", "yes");
                startActivity(mIntentPushRange);
                break;

        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_push_range_info;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("查看推送题目范围");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPushRange();
    }

    private void initPushRange() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_PushRangeInfo.this,
                "加载中", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.GET_RESPONDENT_RANGE)
                .addParams("token", BaseApplication.getApplication().getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_PushRangeInfo.this, "服务器开小差！请稍后重试");
                        Log.e("==推送题目范围", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==推送题目范围", response);
                        Log.e("==推送题目范围", Api.GET_RESPONDENT_RANGE);
                        Log.e("==推送题目范围", BaseApplication.getApplication().getToken());
                        mProgressHUD.dismiss();
                        PushRangeInfoModel mPushRangeInfoModel = JSONObject.parseObject(response, PushRangeInfoModel.class);

                        if (RequestType.SUCCESS.equals(mPushRangeInfoModel.getStatus())) {
                            if (mPushRangeInfoModel.getRange().getPrimarySchool().size() != 0) {
                                mLlPrimarySchool.setVisibility(View.VISIBLE);
                                mRecyclerViewPrimarySchool.setHasFixedSize(true);
                                mRecyclerViewPrimarySchool.setNestedScrollingEnabled(false);
                                mRecyclerViewPrimarySchool.setLayoutManager(new LinearLayoutManager(Activity_PushRangeInfo.this));
                                mRecyclerViewPrimarySchool.setHasFixedSize(true);
                                mRecyclerViewPrimarySchool.setItemAnimator(new DefaultItemAnimator());
                                mPushRangeInfoAdapter = new PushRangeInfoAdapter(Activity_PushRangeInfo.this,
                                        mPushRangeInfoModel.getRange().getPrimarySchool());
                                mRecyclerViewPrimarySchool.setAdapter(mPushRangeInfoAdapter);
                            } else {
                                mLlPrimarySchool.setVisibility(View.GONE);
                            }

                            if (mPushRangeInfoModel.getRange().getMiddleSchool().size() != 0) {
                                mLlJuniorHighSchool.setVisibility(View.VISIBLE);
                                mRecyclerViewMiddleSchool.setHasFixedSize(true);
                                mRecyclerViewMiddleSchool.setNestedScrollingEnabled(false);
                                mRecyclerViewMiddleSchool.setLayoutManager(new LinearLayoutManager(Activity_PushRangeInfo.this));
                                mRecyclerViewMiddleSchool.setHasFixedSize(true);
                                mRecyclerViewMiddleSchool.setItemAnimator(new DefaultItemAnimator());
                                mPushRangeMiddkeSchoolAdapter = new PushRangeMiddkeSchoolAdapter(Activity_PushRangeInfo.this,
                                        mPushRangeInfoModel.getRange().getMiddleSchool());
                                mRecyclerViewMiddleSchool.setAdapter(mPushRangeMiddkeSchoolAdapter);
                            } else {
                                mLlJuniorHighSchool.setVisibility(View.GONE);
                            }

                            if (mPushRangeInfoModel.getRange().getHighSchool().size() != 0) {
                                mLlHighSchool.setVisibility(View.VISIBLE);
                                mRecyclerViewHighSchool.setHasFixedSize(true);
                                mRecyclerViewHighSchool.setNestedScrollingEnabled(false);
                                mRecyclerViewHighSchool.setLayoutManager(new LinearLayoutManager(Activity_PushRangeInfo.this));
                                mRecyclerViewHighSchool.setHasFixedSize(true);
                                mRecyclerViewHighSchool.setItemAnimator(new DefaultItemAnimator());
                                mPushRangeHighSchoolAdapter = new PushRangeHighSchoolAdapter(Activity_PushRangeInfo.this,
                                        mPushRangeInfoModel.getRange().getHighSchool());
                                mRecyclerViewHighSchool.setAdapter(mPushRangeHighSchoolAdapter);
                            } else {
                                mLlHighSchool.setVisibility(View.GONE);
                            }
                        } else {
                            if (mPushRangeInfoModel.getStatus().equals("9") || mPushRangeInfoModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mPushRangeInfoModel.getStatus(),
                                        Activity_PushRangeInfo.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_PushRangeInfo.this, "登录失效");
                                }
                            } else if (mPushRangeInfoModel.getStatus().equals("0")) {
                                IToast.show(Activity_PushRangeInfo.this, mPushRangeInfoModel.getMessage());
                            }
                        }

                    }
                });
    }

    @Override
    protected void initData() {
        mLlAdoption.setVisibility(View.VISIBLE);
        mTvTightTitle.setText("重置");
    }
}
