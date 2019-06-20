package com.tangchaoke.yiyoubangjiao.activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.model.ActivityCenterModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
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
* description: 活动中心
*/
public class Activity_ActivityCenter extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.img_activity_center)
    ImageView mImgActivityCenter;

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
        return R.layout.activity_activity_center;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("活动中心");
    }

    @Override
    protected void initData() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_ActivityCenter.this, "加载中",
                true, false, null);
        OkHttpUtils
                .post()
                .url(Api.GET_ACTIVITY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        mProgressHUD.dismiss();
                        IToast.show(Activity_ActivityCenter.this, "服务器开小差！请稍后重试");
                    }

                    @Override
                    public void onResponse(String response) {
                        mProgressHUD.dismiss();
                        Log.e("====获取最新活动 :::", Api.GET_ACTIVITY);
                        Log.e("====获取最新活动 :::", response);
                        ActivityCenterModel mActivityCenterModel = JSONObject.parseObject(response, ActivityCenterModel.class);
                        if (RequestType.SUCCESS.equals(mActivityCenterModel.getStatus())) {
                            Glide
                                    .with(Activity_ActivityCenter.this)
                                    .load(Api.PATH + mActivityCenterModel.getModel().getPath())
                                    .asGif()
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(mImgActivityCenter);
                        }
                    }
                });

    }

}
