package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.RegisteredModel;
import com.tangchaoke.yiyoubangjiao.type.CodeType;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.GetCode;
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
* description: 用户注册1-1
*/
public class Activity_Registered extends BaseActivity {

    @BindView(R.id.tv_send_code)
    TextView mTvSendCode;

    @BindView(R.id.edit_code)
    EditText mEditCode;

    private String mCode;

    @BindView(R.id.edit_phone)
    EditText mEditPhone;

    private String mPhone;

    @OnClick({R.id.ll_back, R.id.tv_send_code, R.id.ll_registra_agreement, R.id.but_registered})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_send_code:
                mPhone = mEditPhone.getText().toString().trim();
                GetCode.getCode(Activity_Registered.this, mPhone, CodeType.REGISTER_CODE_TYPE, mTvSendCode, mEditCode);
                break;

            case R.id.ll_registra_agreement:
                Intent mIntentRegisteraAgreement = new Intent(Activity_Registered.this, Activity_RegisteraAgreement.class);
                startActivity(mIntentRegisteraAgreement);
                break;

            case R.id.but_registered:
                mPhone = mEditPhone.getText().toString().trim();
                mCode = mEditCode.getText().toString().trim();
                initCerificationCode(mPhone, mCode);
                break;

        }

    }

    private void initCerificationCode(final String mPhone, final String mCode) {

        if (HGTool.isEmpty(mPhone)) {
            IToast.show(Activity_Registered.this, "请输入手机号");
            return;
        }

        if (mPhone.length() < 11) {
            IToast.show(Activity_Registered.this, "请输入正确的手机号");
            return;
        }

        if (HGTool.isEmpty(mCode)) {
            IToast.show(Activity_Registered.this, "请输入验证码");
            return;
        }

        final ProgressHUD mProgressHUD = ProgressHUD.show(this, "验证中", true, false, null);

        OkHttpUtils
                .post()
                .url(Api.ADD_USER1)
                .addParams("model.account", mPhone)
                .addParams("yzm", mCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Registered.this, "服务器开小差！请稍后重试");
                        mProgressHUD.dismiss();
                        Log.e("====用户注册1-1：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====用户注册1-1：：：", response);
                        Log.e("====用户注册1-1：：：", Api.ADD_USER1);
                        Log.e("====用户注册1-1：：：", mPhone);
                        Log.e("====用户注册1-1：：：", mCode);
                        final RegisteredModel mRegisteredModel = JSONObject.parseObject(response, RegisteredModel.class);
                        if (RequestType.SUCCESS.equals(mRegisteredModel.getStatus())) {
                            mProgressHUD.dismiss();
                            Intent mIntentRegisteraNext = new Intent(Activity_Registered.this, Activity_RegisteraNext.class);
                            mIntentRegisteraNext.putExtra("phone", mPhone);
                            startActivity(mIntentRegisteraNext);
                            addActivity(Activity_Registered.this);
                        } else {
                            mProgressHUD.dismiss();
                            IToast.show(Activity_Registered.this, mRegisteredModel.getMessage());
                        }
                    }
                });

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_registered;
    }

    @Override
    public void initTitleBar() {

    }

    @Override
    protected void initData() {

    }

}
