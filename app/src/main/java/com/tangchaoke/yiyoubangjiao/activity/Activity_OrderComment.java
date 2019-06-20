package com.tangchaoke.yiyoubangjiao.activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hedgehog.ratingbar.RatingBar;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 发表评论
*/
public class Activity_OrderComment extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    private String mOid = "";

    @BindView(R.id.rating_bar)
    RatingBar mRatingBar;

    float mStarF;

    String mStar = "";

    @BindView(R.id.edit_content)
    EditText mEditContent;

    private String mContent;

    @OnClick({R.id.ll_back, R.id.but_publish})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.but_publish:
                mContent = mEditContent.getText().toString();
                initPublish(mContent);
                break;

        }

    }

    private void initPublish(final String mContent) {

        if (HGTool.isEmpty(mStar)) {
            IToast.show(Activity_OrderComment.this, "最少一个星");
            return;
        }

        if (HGTool.isEmpty(mContent)) {
            IToast.show(Activity_OrderComment.this, "请输入评价内容");
            return;
        }

        if (mContent.length() < 8) {
            IToast.show(Activity_OrderComment.this, "评价内容不能少于8个字");
            return;
        }

        OkHttpUtils
                .post()
                .url(Api.PUBLICATION_EVALUATION)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.order.oid", mOid)
                .addParams("model.star", mStar)
                .addParams("model.commentContent", mContent)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_OrderComment.this, "服务器开小差 请稍后再试 ！ ");
                        Log.e("==发表订单评价 :::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==发表订单评价 :::", response);
                        Log.e("==发表订单评价 :::", Api.PUBLICATION_EVALUATION);
                        Log.e("==发表订单评价 :::", BaseApplication.getApplication().getToken());
                        Log.e("==发表订单评价 :::", mOid);
                        Log.e("==发表订单评价 :::", mStar);
                        Log.e("==发表订单评价 :::", mContent);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            finish();
                            IToast.show(Activity_OrderComment.this, mSuccessModel.getMessage());
                        } else {
                            IToast.show(Activity_OrderComment.this, mSuccessModel.getMessage());
                        }
                    }
                });

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_comment;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("发表评论");
    }

    @Override
    protected void initData() {
        mOid = getIntent().getStringExtra("oid");
        mRatingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                mStarF = RatingCount;
                mStar = String.valueOf(mStarF);
                mStar = mStar.replace(".0", "");
            }
        });
    }
}
