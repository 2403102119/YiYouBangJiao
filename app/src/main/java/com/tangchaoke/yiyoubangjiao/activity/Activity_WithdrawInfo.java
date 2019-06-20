package com.tangchaoke.yiyoubangjiao.activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.tangchaoke.yiyoubangjiao.view.PromptEchoDialogView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 提现2
*/
public class Activity_WithdrawInfo extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    private String mMoney = "";

    @BindView(R.id.tv_monery)
    TextView mTvMonery;

    @BindView(R.id.edit_name)
    EditText mEditName;

    private String mName = "";

    @BindView(R.id.edit_phone)
    EditText mEditPhone;

    private String mPhone = "";

    private String mWXPhone = "";

    private int mType;

    @OnClick({R.id.ll_back, R.id.tv_back})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_back:
                mMoney = mTvMonery.getText().toString().trim();
                mName = mEditName.getText().toString().trim();
                if (mType == 1) {
                    mPhone = mEditPhone.getText().toString().trim();
                } else if (mType == 2) {
                    mWXPhone = mEditPhone.getText().toString().trim();
                }
                initCommit(mMoney, mName, mPhone, mWXPhone);
                break;

        }

    }

    private void initCommit(final String mMoney, final String mName, final String mPhone, final String mWXPhone) {

        if (HGTool.isEmpty(mMoney)) {
            IToast.show(Activity_WithdrawInfo.this, "请输入提现金额");
            return;
        }

        if (HGTool.isEmpty(mName)) {
            IToast.show(Activity_WithdrawInfo.this, "请输入真实姓名");
            return;
        }

        if (mType == 1) {
            if (HGTool.isEmpty(mPhone)) {
                IToast.show(Activity_WithdrawInfo.this, "请输入支付宝账号");
                return;
            }
        } else if (mType == 2) {
            if (HGTool.isEmpty(mWXPhone)) {
                IToast.show(Activity_WithdrawInfo.this, "请输入微信账号");
                return;
            }
        }

        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_WithdrawInfo.this, "提现中", true, false, null);

        OkHttpUtils
                .post()
                .url(Api.GET_MONEY)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.name", mName)
                .addParams("model.amountMoney", mMoney)
                .addParams("model.statu", mType + "")
                .addParams("model.aliPay", mPhone)
                .addParams("model.wechat", mWXPhone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_WithdrawInfo.this, "服务器开小差！请稍后重试");
                        Log.e("==提现：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==提现：：：", response);
                        Log.e("==提现：：：", Api.GET_MONEY);
                        Log.e("==提现：：：", BaseApplication.getApplication().getToken());
                        Log.e("==提现：：：", mName);
                        Log.e("==提现：：：", mMoney);
                        Log.e("==提现：：：", mType + "");
                        Log.e("==提现：：：", mPhone);
                        Log.e("==提现：：：", mWXPhone);
                        mProgressHUD.dismiss();
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            isLoginDialog();
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(), Activity_WithdrawInfo.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_WithdrawInfo.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_WithdrawInfo.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });

    }

    private void isLoginDialog() {
        try {
            final PromptEchoDialogView mCleanUpCacheDialogView = new PromptEchoDialogView(Activity_WithdrawInfo.this);
            mCleanUpCacheDialogView.setTitle("提交成功");
            mCleanUpCacheDialogView.setContent("您的提现申请已经提交成功，待平台处理后即可到账。");
            mCleanUpCacheDialogView.setCancelable(false);
            mCleanUpCacheDialogView.setCustomOnClickListener(new PromptEchoDialogView.OnCustomDialogListener() {
                @Override
                public void setYesOnclick() {
                    mCleanUpCacheDialogView.dismiss();
                    finish();
                    clearActivity();
                }
            });
            mCleanUpCacheDialogView.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_withdraw_info;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("提现");
        if (!HGTool.isEmpty(mMoney)) {
            mTvMonery.setText(mMoney);
        }
    }

    @Override
    protected void initData() {
        mMoney = getIntent().getStringExtra("money");
        mType = getIntent().getIntExtra("type", 0);
    }
}
