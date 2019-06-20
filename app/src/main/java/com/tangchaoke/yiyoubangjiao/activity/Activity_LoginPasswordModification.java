package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.CodeType;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
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
* description: 登录密码修改
*/
public class Activity_LoginPasswordModification extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.edit_old_password)
    EditText mEditOldPassword;

    private String mOldPassword;

    @BindView(R.id.edit_new_password)
    EditText mEditNewPassword;

    private String mNewPassword;

    @BindView(R.id.edit_again_new_password)
    EditText mEditAgainNewPassword;

    private String mAgainNewPassword;

    private String mPhone;

    @BindView(R.id.tv_code)
    TextView mTvCode;

    @OnClick({R.id.ll_back, R.id.tv_code, R.id.but_confirm_the_changes})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_code:
                GetCode.getCode(Activity_LoginPasswordModification.this, mPhone, CodeType.MODIFY_CODE_TYPE, mTvCode, mEditOldPassword);
                break;

            case R.id.but_confirm_the_changes:
                mOldPassword = mEditOldPassword.getText().toString().trim();
                mNewPassword = mEditNewPassword.getText().toString().trim();
                mAgainNewPassword = mEditAgainNewPassword.getText().toString().trim();
                initConfirmTheChanges(mOldPassword, mNewPassword, mAgainNewPassword);
                break;

        }

    }

    /**
     * 确认修改
     *
     * @param mOldPassword
     * @param mNewPassword
     * @param mAgainNewPassword
     */
    private void initConfirmTheChanges(final String mOldPassword, final String mNewPassword, final String mAgainNewPassword) {
        if (HGTool.isEmpty(mOldPassword)) {
            IToast.show(Activity_LoginPasswordModification.this, "请输入验证码");
            return;
        }

        if (HGTool.isEmpty(mNewPassword)) {
            IToast.show(Activity_LoginPasswordModification.this, "请输入新密码");
            return;
        }

        if (mNewPassword.length() < 6) {
            IToast.show(Activity_LoginPasswordModification.this, "密码至少6位或6位以上");
            return;
        }

//        if (!HGTool.isLetterDigit(mNewPassword)) {
//            IToast.show(Activity_LoginPasswordModification.this, "密码至少包含字母及数字两种~");
//            return;
//        }

        if (HGTool.isEmpty(mAgainNewPassword)) {
            IToast.show(Activity_LoginPasswordModification.this, "请再次输入新密码");
            return;
        }

        if (!mNewPassword.equals(mAgainNewPassword)) {
            IToast.show(Activity_LoginPasswordModification.this, "两次输入的新密码不一致");
            return;
        }

        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_LoginPasswordModification.this, "修改中~", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.UPDATE_USER_PASSWORD_BY_ACCOUNT)
                .addParams("model.account", mPhone)
                .addParams("yzm", mOldPassword)
                .addParams("newPassword", mNewPassword)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_LoginPasswordModification.this, "服务器开小差！请稍后重试");
                        Log.e("==修改密码：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        mProgressHUD.dismiss();
                        Log.e("==修改密码：：：", response);
                        Log.e("==修改密码：：：", Api.UPDATE_USER_PASSWORD_BY_ACCOUNT);
                        Log.e("==修改密码：：：", BaseApplication.getApplication().getToken());
                        Log.e("==修改密码：：：", mOldPassword);
                        Log.e("==修改密码：：：", mNewPassword);
                        Log.e("==修改密码：：：", mPhone);
                        final SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            EMClient.getInstance().logout(false, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    finish();
                                    clearActivity();
                                    initReleaseOpenClear();
                                    Intent mIntentLogin = new Intent(Activity_LoginPasswordModification.this, Activity_Login.class);
                                    startActivity(mIntentLogin);
                                    finish();
                                }

                                @Override
                                public void onProgress(int progress, String status) {
                                    Log.e("退出失败", progress + "..." + status);
                                }

                                @Override
                                public void onError(int code, String message) {

                                }
                            });
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(), Activity_LoginPasswordModification.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_LoginPasswordModification.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_LoginPasswordModification.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });

    }

    private void initReleaseOpenClear() {
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putBoolean("isLogined", false);
        mEditor.clear();
        mEditor.commit();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login_password_modification;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("登录密码修改");
    }

    @Override
    protected void initData() {
        if (!HGTool.isEmpty(BaseApplication.getApplication().getAccount())) {
            mPhone = BaseApplication.getApplication().getAccount();
        }
    }

}
