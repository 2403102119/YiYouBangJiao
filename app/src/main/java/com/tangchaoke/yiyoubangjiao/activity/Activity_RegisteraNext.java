package com.tangchaoke.yiyoubangjiao.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.RegisteredModel;
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
* description: 用户注册1-2
*/
public class Activity_RegisteraNext extends BaseActivity {

    private String mPhone;

    @BindView(R.id.edit_password)
    EditText mEditPassword;

    private String mPassWord;

    @BindView(R.id.edit_promo_code)
    EditText mEditPromoCode;

    private String mPromoCode;

    @BindView(R.id.edit_school_code)
    EditText mEditSchoolCode;

    private String mSchoolCode;

    @BindView(R.id.edit_club_code)
    EditText mEditClubCode;

    private String mClubCode;

    @OnClick({R.id.ll_back, R.id.but_registered})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.but_registered:
                mPassWord = mEditPassword.getText().toString().trim();
                mPromoCode = mEditPromoCode.getText().toString().trim();
                mSchoolCode = mEditSchoolCode.getText().toString().trim();
                mClubCode = mEditClubCode.getText().toString().trim();
                initRegistered(mPhone, mPassWord, mPromoCode, mSchoolCode, mClubCode);
                break;

        }

    }

    /**
     * 注册
     *
     * @param mPhone
     * @param mPassWord
     */
    private void initRegistered(final String mPhone, final String mPassWord, final String mPromoCode,
                                final String mSchoolCode, final String mClubCode) {

        if (HGTool.isEmpty(mPassWord)) {
            IToast.show(Activity_RegisteraNext.this, "请输入密码");
            return;
        }

        if (mPassWord.length() < 6) {
            IToast.show(Activity_RegisteraNext.this, "密码至少6位或6位以上");
            return;
        }

        final ProgressHUD mProgressHUD = ProgressHUD.show(this, "注册中", true, false, null);

        OkHttpUtils
                .post()
                .url(Api.ADD_USER2)
                .addParams("model.account", mPhone)
                .addParams("model.password", mPassWord)
                .addParams("schoolCode", mSchoolCode)
                .addParams("clubCode", mClubCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_RegisteraNext.this, "服务器开小差！请稍后重试");
                        mProgressHUD.dismiss();
                        Log.e("====用户注册1-2：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====用户注册1：：：", response);
                        Log.e("====用户注册1：：：", Api.ADD_USER2);
                        Log.e("====用户注册1：：：", mPhone);
                        Log.e("====用户注册1：：：", mPassWord);
                        Log.e("====用户注册1：：：", mPromoCode);
                        Log.e("====用户注册1：：：", mSchoolCode);
                        Log.e("====用户注册1：：：", mClubCode);
                        final RegisteredModel mRegisteredModel = JSONObject.parseObject(response, RegisteredModel.class);
                        if (RequestType.SUCCESS.equals(mRegisteredModel.getStatus())) {
                            mProgressHUD.dismiss();
                            /**
                             * 环信注册
                             */
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Log.e("==注册账号", mRegisteredModel.getOid().toLowerCase() + "..." + mRegisteredModel.getOid().toUpperCase());
                                        EMClient.getInstance().createAccount(mRegisteredModel.getOid().toLowerCase(),
                                                mRegisteredModel.getOid().toLowerCase());//同步方法
                                        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                                        mEditor.putString("account", mPhone);
                                        mEditor.commit();
                                        finish();
                                        clearActivity();
                                    } catch (HyphenateException e) {
                                        e.printStackTrace();
                                        if (e.getErrorCode() == 203) {
                                            SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                                            mEditor.putString("account", mPhone);
                                            mEditor.commit();
                                            finish();
                                            clearActivity();
                                        }
                                        Log.e("==注册失败", e.getErrorCode() + "..." + e.getMessage());
                                    }
                                }
                            }).start();
                        } else {
                            mProgressHUD.dismiss();
                            IToast.show(Activity_RegisteraNext.this, mRegisteredModel.getMessage());
                        }
                    }
                });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_registera_next;
    }

    @Override
    public void initTitleBar() {

    }

    @Override
    protected void initData() {
        mPhone = getIntent().getStringExtra("phone");
    }
}
