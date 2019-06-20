package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
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
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 密码登录
*/
public class Activity_Login extends BaseActivity {

    @BindView(R.id.tv_top_right)
    TextView mTvTopRight;

    @BindView(R.id.edit_phone)
    EditText mEditPhone;

    private String mPhone;

    @BindView(R.id.edit_password)
    EditText mEditPassWord;

    private String mPassWord;

    private String islogin;

    private String mName;

    private static final int MSG_SET_ALIAS = 1001;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    tuisongSetAlias(((String) msg.obj));
                    break;
            }
        }
    };

    @OnClick({R.id.ll_back, R.id.img_wx, R.id.tv_login_recover_password,
            R.id.tv_login_registered, R.id.but_login})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            /**
             * 微信登录
             */
            case R.id.img_wx:
                UMShareAPI.get(Activity_Login.this).getPlatformInfo(Activity_Login.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;

            /**
             * 忘记密码
             */
            case R.id.tv_login_recover_password:
                Intent mIntentRecoverPassword = new Intent(Activity_Login.this, Activity_RecoverPassword.class);
                startActivity(mIntentRecoverPassword);
                break;

            /**
             * 注册
             */
            case R.id.tv_login_registered:
                Intent mIntentRegistered = new Intent(Activity_Login.this, Activity_Registered.class);
                startActivity(mIntentRegistered);
                break;

            /**
             * 登录
             */
            case R.id.but_login:
                mPhone = mEditPhone.getText().toString().trim();
                mPassWord = mEditPassWord.getText().toString().trim();
                initLogin(mPhone, mPassWord);
                break;
        }

    }

    /**
     * 第三方登录回调接口
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            mProgressHUD = ProgressHUD.show(Activity_Login.this, "请稍候",
                    true, false, null);
            //授权开始的回调
            IToast.show(Activity_Login.this, "微信授权开始");
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            getLoginInfo(Api.WX_LOGIN, data.get("openid"), data.get("screen_name"), data.get("iconurl"));
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            mProgressHUD.dismiss();
            IToast.show(Activity_Login.this, "微信或QQ授权失败！清检查错误信息!");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            mProgressHUD.dismiss();
            IToast.show(Activity_Login.this, "取消微信或QQ登录!");
        }
    };

    private void getLoginInfo(final String path, final String mOpenid, final String screen_name, final String iconurl) {
        OkHttpUtils
                .get()
                .url(path)
                .addParams("model.openid", mOpenid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Login.this, "服务器开小差！请稍后重试");
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====第三方登录：：：", path);
                        Log.e("====第三方登录：：：", response + "");
                        Log.e("====第三方登录：：：", mOpenid + "");
                        LoginModel mLoginModel = JSONObject.parseObject(response, LoginModel.class);
                        mProgressHUD.dismiss();
                        if (RequestType.SUCCESS.equals(mLoginModel.getStatus())) {
                            if (mLoginModel.getModel().getUserStatus().equals("0")) {
                                Intent mIntentBindingPhone = new Intent(Activity_Login.this, Activity_BindingPhone.class);
                                mIntentBindingPhone.putExtra("head", iconurl);
                                mIntentBindingPhone.putExtra("name", screen_name);
                                mIntentBindingPhone.putExtra("openid", mOpenid);
                                startActivity(mIntentBindingPhone);
                            } else if (mLoginModel.getModel().getUserStatus().equals("1")) {
                                initHuanXin(mLoginModel);
                            }
                        } else {
                            IToast.show(Activity_Login.this, mLoginModel.getMessage());
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(Activity_Login.this).onActivityResult(requestCode, resultCode, data);
    }

    ProgressHUD mProgressHUD;

    /**
     * 登录
     *
     * @param mPhone
     * @param mPassWord
     */
    private void initLogin(final String mPhone, final String mPassWord) {
        if (HGTool.isEmpty(mPhone)) {
            IToast.show(Activity_Login.this, "请输入手机号");
            return;
        }

        if (mPhone.length() < 11) {
            IToast.show(Activity_Login.this, "请输入正确的手机号");
            return;
        }

        if (HGTool.isEmpty(mPassWord)) {
            IToast.show(Activity_Login.this, "请输入密码");
            return;
        }

        if (mPassWord.length() < 6) {
            IToast.show(Activity_Login.this, "密码至少6位或6位以上");
            return;
        }
        mProgressHUD = ProgressHUD.show(this, "请稍候", true,
                false, null);
        OkHttpUtils
                .post()
                .url(Api.USER_LOGIN)
                .addParams("model.account", mPhone)
                .addParams("model.password", mPassWord)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Login.this, "服务器开小差！请稍后重试");
                        Log.e("====使用用户名+密码登录：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====使用用户名+密码登录：：：", response);
                        Log.e("====使用用户名+密码登录：：：", Api.USER_LOGIN);
                        Log.e("====使用用户名+密码登录：：：", mPhone);
                        Log.e("====使用用户名+密码登录：：：", mPassWord);
                        LoginModel mLoginModel = JSONObject.parseObject(response, LoginModel.class);
                        if (RequestType.SUCCESS.equals(mLoginModel.getStatus())) {
                            initHuanXin(mLoginModel);
                        } else {
                            IToast.show(Activity_Login.this, mLoginModel.getMessage());
                            mProgressHUD.dismiss();
                        }
                    }
                });
    }

    Intent mIntentMain;

    private void initHuanXin(final LoginModel mLoginModel) {
        EMClient.getInstance().logout(true);
        EMClient.getInstance().login(
                mLoginModel.getModel().getOid().toLowerCase(),
                mLoginModel.getModel().getOid().toLowerCase(),
                new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        mProgressHUD.dismiss();
                        initSaveUserInfo(mLoginModel);
                        if (!HGTool.isEmpty(islogin)) {
                            finish();
                        } else {
                            if (!HGTool.isEmpty(mName)) {
                                /**
                                 * 消息列表 或 我的  界面未登录 跳转登录  登录后finish登录界面
                                 */
                                finish();
                            } else {
                                /**
                                 *
                                 */
                                mIntentMain = new Intent(Activity_Login.this, Activity_Main.class);
                                startActivity(mIntentMain);
                                finish();
                            }
                        }
                        /**
                         * 登录之后才能注册别名
                         */
                        tuisongSetAlias(BaseApplication.getApplication().getOid() + "");
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        mProgressHUD.dismiss();
                        Log.e("登录失败", progress + "..." + status);
                    }

                    @Override
                    public void onError(final int code, final String message) {
                        mProgressHUD.dismiss();
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
                                        Toast.makeText(Activity_Login.this, "网络错误 code: " + code + ", " +
                                                "message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 无效的用户名 101
                                    case EMError.INVALID_USER_NAME:
                                        Toast.makeText(Activity_Login.this, "无效的用户名 code: " + code + ", " +
                                                "message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 无效的密码 102
                                    case EMError.INVALID_PASSWORD:
                                        Toast.makeText(Activity_Login.this, "无效的密码 code: " + code + ", " +
                                                "message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 用户认证失败，用户名或密码错误 202
                                    case EMError.USER_AUTHENTICATION_FAILED:
                                        Toast.makeText(Activity_Login.this, "用户认证失败，用户名或密码错误 c" +
                                                "ode: " + code + ", message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 用户不存在 204
                                    case EMError.USER_NOT_FOUND:
                                        Toast.makeText(Activity_Login.this, "用户不存在 code: " + code + ", " +
                                                "message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 无法访问到服务器 300
                                    case EMError.SERVER_NOT_REACHABLE:
                                        Toast.makeText(Activity_Login.this, "无法访问到服务器 code: " + code + ", " +
                                                "message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 等待服务器响应超时 301
                                    case EMError.SERVER_TIMEOUT:
                                        Toast.makeText(Activity_Login.this, "等待服务器响应超时 code: " + code + ", " +
                                                "message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 服务器繁忙 302
                                    case EMError.SERVER_BUSY:
                                        Toast.makeText(Activity_Login.this, "服务器繁忙 code: " + code + ", " +
                                                "message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    // 未知 Server 异常 303 一般断网会出现这个错误
                                    case EMError.SERVER_UNKNOWN_ERROR:
                                        Toast.makeText(Activity_Login.this, "未知的服务器异常 code: " + code + ", " +
                                                "message:" + message, Toast.LENGTH_LONG).show();
                                        break;
                                    default:
                                        Toast.makeText(Activity_Login.this, "ml_sign_in_failed code: " + code + ", " +
                                                "message:" + message, Toast.LENGTH_LONG).show();
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
        return R.layout.activity_login;
    }

    @Override
    public void initTitleBar() {
        mTvTopRight.setVisibility(View.INVISIBLE);
        mTvTopRight.setText("快捷登录");
    }

    @Override
    protected void initData() {
        /**
         * 类名
         */
        islogin = getIntent().getStringExtra("islogin");
        /**
         * Fragment 标识
         */
        mName = getIntent().getStringExtra("name");
        /**
         * 登录之后才能注册别名
         */
        if (!BaseApplication.getApplication().isLogined()) {
            JPushInterface.onResume(Activity_Login.this);
            tuisongSetAlias("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!HGTool.isEmpty(BaseApplication.getApplication().getAccount())) {
            mEditPhone.setText(BaseApplication.getApplication().getAccount());
        }
    }

    /**
     * 为用户设置极光推送的别名
     */
    private void tuisongSetAlias(String mAlias) {
        JPushInterface.setAlias(Activity_Login.this, mAlias + "", new TagAliasCallback() {
            @Override
            public void gotResult(int code, String alias, Set<String> set) {
                switch (code) {
                    case 0://设置成功
                        BaseApplication.getApplication().setIsAliasMap(BaseApplication.getApplication().getOid(), true);
                        Log.e("==推送注册别名", BaseApplication.getApplication().getOid());
                        break;
                    case 6002://网络不稳定,稍延迟60秒后重试
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                        break;
                    case 6001:
                        Log.e("==推送注册别名", "无效的设置，tag/alias 不应参数都为 null");
                        break;
                    case 6003://alias字符串不合法
                        break;
                    case 6004://alias超长，最多40个字节
                        break;
                    case 6009://未知错误
                        break;
                    default:
                        break;
                }
            }
        });
    }

}
