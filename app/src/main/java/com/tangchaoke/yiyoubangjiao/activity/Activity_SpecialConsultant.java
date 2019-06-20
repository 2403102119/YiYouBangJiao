package com.tangchaoke.yiyoubangjiao.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.SpecialConsultantAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.model.SpecialConsultantBean;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 特约顾问
*/
public class Activity_SpecialConsultant extends BaseActivity {

    @BindView(R.id.recycler_viewPager)
    RecyclerViewPager mRecyclerViewPager;

    SpecialConsultantAdapter mSpecialConsultantAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_special_consultant;
    }

    @Override
    public void initTitleBar() {

    }

    @Override
    protected void initData() {
        initSpecialAdviserList();
    }

    private void initSpecialAdviserList() {
        OkHttpUtils
                .post()
                .url(Api.GET_SPECIAL_ADVISER_LIST)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("==获取特约顾问列表:::", e.getMessage());
                        IToast.show(Activity_SpecialConsultant.this, "服务器开小差 请稍后再试 ！ ");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==获取特约顾问列表:::", response);
                        Log.e("==获取特约顾问列表:::", Api.GET_SPECIAL_ADVISER_LIST);
                        SpecialConsultantBean mSpecialConsultantModel = JSONObject.parseObject(response, SpecialConsultantBean.class);
                        if (RequestType.SUCCESS.equals(mSpecialConsultantModel.getStatus())) {
                            if (mSpecialConsultantModel.getModel().size() != 0) {
                                initRecyclerView(mSpecialConsultantModel.getModel());
                            }
                        } else {
                            IToast.show(Activity_SpecialConsultant.this, mSpecialConsultantModel.getMessage());
                        }
                    }
                });
    }

    private void initRecyclerView(List<SpecialConsultantBean.SpecialConsultantModelBean> model) {
        mRecyclerViewPager.setAlpha(1f);
        LinearLayoutManager layout = new LinearLayoutManager(Activity_SpecialConsultant.this, LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerViewPager.setLayoutManager(layout);
        mSpecialConsultantAdapter = new SpecialConsultantAdapter(Activity_SpecialConsultant.this, model);
        mRecyclerViewPager.setAdapter(mSpecialConsultantAdapter);
        mRecyclerViewPager.setHasFixedSize(true);
        mRecyclerViewPager.setLongClickable(true);
        mRecyclerViewPager.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                int childCount = mRecyclerViewPager.getChildCount();
                int width = mRecyclerViewPager.getChildAt(0).getWidth();
                int padding = (mRecyclerViewPager.getWidth() - width) / 2;
                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                    float rate = 0;
                    ;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });
        mRecyclerViewPager.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {

            }
        });

        mRecyclerViewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mRecyclerViewPager.getChildCount() < 3) {
                    if (mRecyclerViewPager.getChildAt(1) != null) {
                        if (mRecyclerViewPager.getCurrentPosition() == 0) {
                            View v1 = mRecyclerViewPager.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        } else {
                            View v1 = mRecyclerViewPager.getChildAt(0);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (mRecyclerViewPager.getChildAt(0) != null) {
                        View v0 = mRecyclerViewPager.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (mRecyclerViewPager.getChildAt(2) != null) {
                        View v2 = mRecyclerViewPager.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }

            }
        });
    }

}
