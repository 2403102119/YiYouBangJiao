package com.tangchaoke.yiyoubangjiao.activity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.MessageInfoModel;
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
* description: 消息详情
*/
public class Activity_MessageInfo extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    private String mOid;

    @BindView(R.id.tv_status)
    TextView mTvStatus;

    @BindView(R.id.tv_time)
    TextView mTvTime;

    @BindView(R.id.tv_content)
    TextView mTvContent;

    @OnClick({R.id.ll_back})
    void onClivk(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_message_info;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("系统消息");
    }

    @Override
    protected void initData() {
        mOid = getIntent().getStringExtra("oid");
        initMessageInfo();
    }

    private void initMessageInfo() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_MessageInfo.this, "加载中", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.VIEW_MESSAGE)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_MessageInfo.this,"服务器开小差！请稍后重试");
                        Log.e("==消息详情：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==消息详情：：：", response);
                        Log.e("==消息详情：：：", Api.VIEW_MESSAGE);
                        Log.e("==消息详情：：：", BaseApplication.getApplication().getToken());
                        Log.e("==消息详情：：：", mOid);
                        mProgressHUD.dismiss();
                        MessageInfoModel mMessageInfoModel = JSONObject.parseObject(response, MessageInfoModel.class);
                        if (RequestType.SUCCESS.equals(mMessageInfoModel.getStatus())) {
                            initMessageDisplay(mMessageInfoModel.getModel());
                        } else {
                            if (mMessageInfoModel.getStatus().equals("9") || mMessageInfoModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mMessageInfoModel.getStatus(), Activity_MessageInfo.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_MessageInfo.this, "登录失效");
                                }
                            } else if (mMessageInfoModel.getStatus().equals("0")) {
                                IToast.show(Activity_MessageInfo.this, mMessageInfoModel.getMessage());
                            }
                        }
                    }
                });
    }

    /**
     * 小时详情显示
     *
     * @param mMessageInfoModel
     */
    private void initMessageDisplay(MessageInfoModel.MessageInfoModelModel mMessageInfoModel) {
        if (!HGTool.isEmpty(mMessageInfoModel.getTitle())) {
            mTvStatus.setText(mMessageInfoModel.getTitle());
        }

//        if (!HGTool.isEmpty(mMessageInfoModel.getStatus())) {
//            if (mMessageInfoModel.getStatus().equals("0")) {
//                mTvStatus.setText("系统消息");
//            } else if (mMessageInfoModel.getStatus().equals("1")) {
//                mTvStatus.setText("个人消息");
//            }
//        }

        if (!HGTool.isEmpty(mMessageInfoModel.getSendingTime())) {
            mTvTime.setText(mMessageInfoModel.getSendingTime());
        }

        if (!HGTool.isEmpty(mMessageInfoModel.getContent())) {
            mTvContent.setText(mMessageInfoModel.getContent());
        }

    }
}
