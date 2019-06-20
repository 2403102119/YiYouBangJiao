package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.LoginModel;
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
* description: 设置登录密码
*/
public class Activity_LoginPassWord extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.edit_new_password)
    EditText mEditNewPassword;

    private String mNewPassword;

    @BindView(R.id.edit_again_new_password)
    EditText mEditAgainNewPassword;

    private String mAgainNewPassword;

    private String mAccount = "";

    private String mHead = "";

    private String mNickName = "";

    private String mOpenid = "";

    @BindView(R.id.edit_coupon)
    EditText mEditCoupon;

    private String mCoupon;

    @BindView(R.id.edit_school_code)
    EditText mEditSchoolCode;

    private String mSchoolCode;

    @BindView(R.id.edit_club_code)
    EditText mEditClubCode;

    private String mClubCode;

    @OnClick({R.id.ll_back, R.id.but_confirm_the_changes})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.but_confirm_the_changes:
                mNewPassword = mEditNewPassword.getText().toString().trim();
                mAgainNewPassword = mEditAgainNewPassword.getText().toString().trim();
                mCoupon = mEditCoupon.getText().toString().trim();
                mSchoolCode = mEditSchoolCode.getText().toString().trim();
                mClubCode = mEditClubCode.getText().toString().trim();
                initConfirmTheChanges(mNewPassword, mAgainNewPassword, mCoupon, mSchoolCode, mClubCode);
                break;

        }
    }

    ProgressHUD mProgressHUD;

    private void initConfirmTheChanges(final String mNewPassword, String mAgainNewPassword, final String mCoupon,
                                       final String mSchoolCode, final String mClubCode) {

        if (HGTool.isEmpty(mNewPassword)) {
            IToast.show(Activity_LoginPassWord.this, "请输入新密码");
            return;
        }

        if (mNewPassword.length() < 6) {
            IToast.show(Activity_LoginPassWord.this, "密码至少6位或6位以上");
            return;
        }

        if (HGTool.isEmpty(mAgainNewPassword)) {
            IToast.show(Activity_LoginPassWord.this, "请再次输入新密码");
            return;
        }

        if (!mNewPassword.equals(mAgainNewPassword)) {
            IToast.show(Activity_LoginPassWord.this, "两次输入的新密码不一致");
            return;
        }

        mProgressHUD = ProgressHUD.show(Activity_LoginPassWord.this, "注册中", true, true, null);
        OkHttpUtils
                .post()
                .url(Api.SET_WX_PASSWORD)
                .addParams("model.account", mAccount)
                .addParams("model.head", mHead)
                .addParams("model.nickName", mNickName)
                .addParams("model.openid", mOpenid)
                .addParams("model.password", mNewPassword)
                .addParams("code", mCoupon)
                .addParams("schoolCode", mSchoolCode)
                .addParams("clubCode", mClubCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_LoginPassWord.this, "服务器开小差！请稍后重试");
                        Log.e("====微信绑定新账户设置密码：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        mProgressHUD.dismiss();
                        Log.e("====微信绑定新账户设置密码：：：", response);
                        Log.e("====微信绑定新账户设置密码：：：", Api.SET_WX_PASSWORD);
                        Log.e("====微信绑定新账户设置密码：：：", BaseApplication.getApplication().getToken());
                        Log.e("====微信绑定新账户设置密码：：：", mAccount);
                        Log.e("====微信绑定新账户设置密码：：：", mHead);
                        Log.e("====微信绑定新账户设置密码：：：", mNickName);
                        Log.e("====微信绑定新账户设置密码：：：", mOpenid);
                        Log.e("====微信绑定新账户设置密码：：：", mNewPassword);
                        Log.e("====微信绑定新账户设置密码：：：", mCoupon + "====");
                        Log.e("====微信绑定新账户设置密码：：：", mSchoolCode + "====");
                        Log.e("====微信绑定新账户设置密码：：：", mClubCode + "====");
                        final LoginModel mLoginModel = JSONObject.parseObject(response, LoginModel.class);
                        if (RequestType.SUCCESS.equals(mLoginModel.getStatus())) {
                            /**
                             * 环信注册
                             */
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Looper.prepare();
                                        Log.e("==注册账号", mLoginModel.getModel().getOid().toLowerCase() + "..."
                                                + mLoginModel.getModel().getOid().toLowerCase());
                                        EMClient.getInstance().createAccount(
                                                mLoginModel.getModel().getOid().toLowerCase(),
                                                mLoginModel.getModel().getOid().toLowerCase());//同步方法
                                        initHuanXin(mLoginModel);
                                        Looper.loop();
                                    } catch (HyphenateException e) {
                                        e.printStackTrace();
                                        Log.e("==注册失败", e.getErrorCode() + "..." + e.getMessage());
                                    }
                                }
                            }).start();
                        } else {
                            if (mLoginModel.getStatus().equals("0")) {
                                IToast.show(Activity_LoginPassWord.this, mLoginModel.getMessage());
                            }
                        }
                    }
                });
    }

    /**
     * 登录
     *
     * @param mLoginModel
     */
    private void initHuanXin(final LoginModel mLoginModel) {
        Log.e("==登录账号", mLoginModel.getModel().getOid().toLowerCase() + "..." + mLoginModel.getModel().getOid().toLowerCase());
        EMClient.getInstance().login(
                mLoginModel.getModel().getOid().toLowerCase(),
                mLoginModel.getModel().getOid().toLowerCase(),
                new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        initSaveUserInfo(mLoginModel);
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        mProgressHUD.dismiss();
                        Intent mIntentMain = new Intent(Activity_LoginPassWord.this, Activity_Main.class);
                        startActivity(mIntentMain);
                        finish();
                        clearActivity();
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        Log.e("==登录失败", progress + "..." + status);
                    }

                    @Override
                    public void onError(final int code, final String message) {
                        /**
                         * 登录失败
                         */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("==登录失败", "Error code:" + code + ", message:" + message);
                                /**
                                 * 关于错误码可以参考官方api详细说明
                                 * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                                 */
                                switch (code) {
                                    // 网络异常 2
                                    case EMError.NETWORK_ERROR:
                                        Toast.makeText(Activity_LoginPassWord.this, "网络错误 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 无效的用户名 101
                                    case EMError.INVALID_USER_NAME:
                                        Toast.makeText(Activity_LoginPassWord.this, "无效的用户名 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 无效的密码 102
                                    case EMError.INVALID_PASSWORD:
                                        Toast.makeText(Activity_LoginPassWord.this, "无效的密码 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 用户认证失败，用户名或密码错误 202
                                    case EMError.USER_AUTHENTICATION_FAILED:
                                        Toast.makeText(Activity_LoginPassWord.this, "用户认证失败，用户名或密码错误 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 用户不存在 204
                                    case EMError.USER_NOT_FOUND:
                                        Toast.makeText(Activity_LoginPassWord.this, "用户不存在 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 无法访问到服务器 300
                                    case EMError.SERVER_NOT_REACHABLE:
                                        Toast.makeText(Activity_LoginPassWord.this, "无法访问到服务器 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 等待服务器响应超时 301
                                    case EMError.SERVER_TIMEOUT:
                                        Toast.makeText(Activity_LoginPassWord.this, "等待服务器响应超时 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 服务器繁忙 302
                                    case EMError.SERVER_BUSY:
                                        Toast.makeText(Activity_LoginPassWord.this, "服务器繁忙 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 未知 Server 异常 303 一般断网会出现这个错误
                                    case EMError.SERVER_UNKNOWN_ERROR:
                                        Toast.makeText(Activity_LoginPassWord.this, "未知的服务器异常 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    default:
                                        Toast.makeText(Activity_LoginPassWord.this, "ml_sign_in_failed code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        });
                    }

                });
    }

    /**
     * 保存用户信息
     *
     * @param mLoginModel
     */
    private void initSaveUserInfo(LoginModel mLoginModel) {
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putBoolean("isLogined", true);
        mEditor.putString("token", mLoginModel.getModel().getToken());
        mEditor.putString("oid", mLoginModel.getModel().getOid());
        if (!HGTool.isEmpty(mLoginModel.getModel().getHead())) {
            if (mLoginModel.getModel().getHead().toString().contains("http://")) {
                mEditor.putString("head", mLoginModel.getModel().getHead());
            } else {
                mEditor.putString("head", Api.PATH + mLoginModel.getModel().getHead());
            }
        }
        mEditor.putString("account", mLoginModel.getModel().getAccount());
        mEditor.putString("nickName", mLoginModel.getModel().getNickName());
        mEditor.putString("grade", mLoginModel.getModel().getGrade());
        mEditor.putString("respondent", mLoginModel.getModel().getRespondent());
        if (mLoginModel.getModel().getRespondentAgreement().equals("0")) {
            /**
             * 答题者认证 协议
             */
            mEditor.putBoolean("respondentAgreement", false);
            mEditor.putString("pushRange", "0");
        } else if (mLoginModel.getModel().getRespondentAgreement().equals("1")) {
            /**
             * 答题者认证 协议
             */
            mEditor.putBoolean("respondentAgreement", true);
            mEditor.putString("pushRange", "1");
        }
        mEditor.putString("userDiplomaStatus", mLoginModel.getModel().getUserDiplomaStatus());
        mEditor.putString("studentIdCardStatus", mLoginModel.getModel().getStudentIdCardStatus());
        mEditor.putString("isCoach", mLoginModel.getModel().getIsCoach());
        mEditor.putString("isClub", mLoginModel.getModel().getIsClub());
        mEditor.putInt("pushSwitch", Integer.parseInt(mLoginModel.getModel().getPushSwitch()));
        mEditor.putString("isSchool", mLoginModel.getModel().getIsSchool());
        mEditor.commit();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login_pass_word;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("设置登录密码");
    }

    @Override
    protected void initData() {
        mAccount = getIntent().getStringExtra("account");
        mNickName = getIntent().getStringExtra("nickName");
        mHead = getIntent().getStringExtra("head");
        mOpenid = getIntent().getStringExtra("openid");
    }

}
