package com.tangchaoke.yiyoubangjiao.activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
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
* description: 忘记密码
*/
public class Activity_RecoverPassword extends BaseActivity {

    @BindView(R.id.tv_send_code)
    TextView mTvSendCode;

    @BindView(R.id.edit_code)
    EditText mEditCode;

    private String mCode;

    @BindView(R.id.edit_phone)
    EditText mEditPhone;

    private String mPhone;

    @BindView(R.id.edit_password)
    EditText mEditPassword;

    private String mPassWord;

    @OnClick({R.id.ll_back,R.id.tv_send_code, R.id.but_confirm})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_send_code:
                    mPhone = mEditPhone.getText().toString().trim();
                    GetCode.getCode(Activity_RecoverPassword.this, mPhone, CodeType.MODIFY_CODE_TYPE, mTvSendCode, mEditCode);
                break;

            case R.id.but_confirm:
                    mPhone = mEditPhone.getText().toString().trim();
                    mCode = mEditCode.getText().toString().trim();
                    mPassWord = mEditPassword.getText().toString().trim();
                    initConfirmRecoverpassWord(mPhone, mCode, mPassWord);
                break;

        }

    }

    /**
     * 忘记密码
     *
     * @param mPhone
     * @param mCode
     * @param mPassWord
     */
    private void initConfirmRecoverpassWord(final String mPhone, final String mCode, final String mPassWord) {
        if (HGTool.isEmpty(mPhone)) {
            IToast.show(Activity_RecoverPassword.this, "请输入手机号");
            return;
        }

        if (mPhone.length() < 11) {
            IToast.show(Activity_RecoverPassword.this, "请输入正确的手机号");
            return;
        }

//        if (!HGTool.isChinaPhoneLegal(mPhone)) {
//            IToast.show(Activity_RecoverPassword.this, "请输入正确的手机号");
//            return;
//        }

        if (HGTool.isEmpty(mCode)) {
            IToast.show(Activity_RecoverPassword.this, "请输入验证码");
            return;
        }

        if (HGTool.isEmpty(mPassWord)) {
            IToast.show(Activity_RecoverPassword.this, "请输入密码");
            return;
        }

        if (mPassWord.length() < 6) {
            IToast.show(Activity_RecoverPassword.this, "密码至少6位或6位以上");
            return;
        }

//        if (!HGTool.isLetterDigit(mPassWord)) {
//            IToast.show(Activity_RecoverPassword.this, "密码至少包含字母及数字两种~");
//            return;
//        }

        final ProgressHUD mProgressHUD = ProgressHUD.show(this, "修改中", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.UPDATE_USER_PASSWORD_BY_ACCOUNT)
                .addParams("model.account", mPhone)
                .addParams("yzm", mCode)
                .addParams("newPassword", mPassWord)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_RecoverPassword.this,"服务器开小差！请稍后重试");
                        Log.e("==忘记密码：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==忘记密码：：：", response);
                        Log.e("==忘记密码：：：", Api.UPDATE_USER_PASSWORD_BY_ACCOUNT);
                        Log.e("==忘记密码：：：", mPhone);
                        Log.e("==忘记密码：：：", mCode);
                        Log.e("==忘记密码：：：", mPassWord);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            mProgressHUD.dismiss();
                            IToast.show(Activity_RecoverPassword.this, mSuccessModel.getMessage());
                            finish();
                        } else {
                            mProgressHUD.dismiss();
                            IToast.show(Activity_RecoverPassword.this, mSuccessModel.getMessage());
                        }
                    }
                });

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_recover_password;
    }

    @Override
    public void initTitleBar() {

    }

    @Override
    protected void initData() {

    }
}
