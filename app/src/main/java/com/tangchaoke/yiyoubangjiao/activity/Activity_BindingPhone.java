package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.LoginModel;
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
* description: 绑定手机
*/
public class Activity_BindingPhone extends BaseActivity {

    @BindView(R.id.tv_send_code)
    TextView mTvSendCode;

    @BindView(R.id.edit_code)
    EditText mEditCode;

    private String mCode;

    @BindView(R.id.edit_phone)
    EditText mEditPhone;

    private String mPhone;

    private String openid = "";

    private String name = "";

    private String head = "";

    @OnClick({R.id.ll_back, R.id.tv_send_code, R.id.but_confirm})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_send_code:
                mPhone = mEditPhone.getText().toString().trim();
                GetCode.getCode(Activity_BindingPhone.this, mPhone, CodeType.BIND_CODE_TYPE, mTvSendCode, mEditCode);
                break;

            case R.id.but_confirm:
                mPhone = mEditPhone.getText().toString().trim();
                mCode = mEditCode.getText().toString().trim();
                initBindingPhone(mPhone, mCode);
                break;

        }

    }

    ProgressHUD mProgressHUD;

    private void initBindingPhone(final String mPhone, final String mCode) {

        if (HGTool.isEmpty(mPhone)) {
            IToast.show(Activity_BindingPhone.this, "请输入手机号");
            return;
        }

        if (mPhone.length() < 11) {
            IToast.show(Activity_BindingPhone.this, "请输入正确的手机号");
            return;
        }

        if (HGTool.isEmpty(mCode)) {
            IToast.show(Activity_BindingPhone.this, "请输入验证码");
            return;
        }
        mProgressHUD = ProgressHUD.show(this, "绑定中", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.ADD_USER_BY_WX)
                .addParams("model.account", mPhone)
                .addParams("yzm", mCode)
                .addParams("model.head", head)
                .addParams("model.nickName", name)
                .addParams("model.openid", openid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_BindingPhone.this, "服务器开小差！请稍后重试");
                        Log.e("====第三方绑定手机：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====第三方绑定手机：：：", response);
                        Log.e("====第三方绑定手机：：：", Api.ADD_USER_BY_WX);
                        Log.e("====第三方绑定手机：：：", mPhone);
                        Log.e("====第三方绑定手机：：：", mCode);
                        Log.e("====第三方绑定手机：：：", head);
                        Log.e("====第三方绑定手机：：：", name);
                        Log.e("====第三方绑定手机：：：", openid);
                        LoginModel mLoginModel = JSONObject.parseObject(response, LoginModel.class);
                        if (RequestType.SUCCESS.equals(mLoginModel.getStatus())) {
                            if (!HGTool.isEmpty(mLoginModel.getModel().getSetPassword())) {
                                if (mLoginModel.getModel().getSetPassword().equals("0")) {
                                    /**
                                     * 不需要设置密码
                                     */
                                    initHuanXin(mLoginModel);
                                } else if (mLoginModel.getModel().getSetPassword().equals("1")) {
                                    mProgressHUD.dismiss();
                                    /**
                                     * 需要设置密码
                                     */
                                    Intent mIntentLoginPassWord = new Intent(Activity_BindingPhone.this, Activity_LoginPassWord.class);
                                    mIntentLoginPassWord.putExtra("account", mPhone);
                                    mIntentLoginPassWord.putExtra("head", head);
                                    mIntentLoginPassWord.putExtra("nickName", name);
                                    mIntentLoginPassWord.putExtra("openid", openid);
                                    startActivity(mIntentLoginPassWord);
                                    addActivity(Activity_BindingPhone.this);
                                }
                            }
                        } else {
                            mProgressHUD.dismiss();
                            IToast.show(Activity_BindingPhone.this, mLoginModel.getMessage());
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
        Log.e("登录账号", mLoginModel.getModel().getOid().toLowerCase() + "..." + mLoginModel.getModel().getOid().toLowerCase());
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
                        Intent mIntentMain = new Intent(Activity_BindingPhone.this, Activity_Main.class);
                        startActivity(mIntentMain);
                        finish();
                        clearActivity();
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        Log.e("登录失败", progress + "..." + status);
                    }

                    @Override
                    public void onError(final int code, final String message) {
                        /**
                         * 登录失败
                         */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("登录失败", "Error code:" + code + ", message:" + message);
                                /**
                                 * 关于错误码可以参考官方api详细说明
                                 * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                                 */
                                switch (code) {
                                    // 网络异常 2
                                    case EMError.NETWORK_ERROR:
                                        Toast.makeText(Activity_BindingPhone.this, "网络错误 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 无效的用户名 101
                                    case EMError.INVALID_USER_NAME:
                                        Toast.makeText(Activity_BindingPhone.this, "无效的用户名 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 无效的密码 102
                                    case EMError.INVALID_PASSWORD:
                                        Toast.makeText(Activity_BindingPhone.this, "无效的密码 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 用户认证失败，用户名或密码错误 202
                                    case EMError.USER_AUTHENTICATION_FAILED:
                                        Toast.makeText(Activity_BindingPhone.this, "用户认证失败，用户名或密码错误 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 用户不存在 204
                                    case EMError.USER_NOT_FOUND:
                                        Toast.makeText(Activity_BindingPhone.this, "用户不存在 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 无法访问到服务器 300
                                    case EMError.SERVER_NOT_REACHABLE:
                                        Toast.makeText(Activity_BindingPhone.this, "无法访问到服务器 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 等待服务器响应超时 301
                                    case EMError.SERVER_TIMEOUT:
                                        Toast.makeText(Activity_BindingPhone.this, "等待服务器响应超时 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 服务器繁忙 302
                                    case EMError.SERVER_BUSY:
                                        Toast.makeText(Activity_BindingPhone.this, "服务器繁忙 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 未知 Server 异常 303 一般断网会出现这个错误
                                    case EMError.SERVER_UNKNOWN_ERROR:
                                        Toast.makeText(Activity_BindingPhone.this, "未知的服务器异常 code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    default:
                                        Toast.makeText(Activity_BindingPhone.this, "ml_sign_in_failed code: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
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
        return R.layout.activity_binding_phone;
    }

    @Override
    public void initTitleBar() {

    }

    @Override
    protected void initData() {
        openid = getIntent().getStringExtra("openid");
        name = getIntent().getStringExtra("name");
        head = getIntent().getStringExtra("head");
    }
}
