package com.tangchaoke.yiyoubangjiao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Login;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Main;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Settings;
import com.tangchaoke.yiyoubangjiao.activity.Activity_WelcomeGuide;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.LoginModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;

/*
* @author hg
* @create at 2018/3/8
* @description 导航页
*/
public class Activity_Welcome extends BaseActivity {
    /**
     * 加载数据弹窗 用法
     * mProgressHUD = ProgressHUD.show(this, "请稍候", true, true, null);
     * mProgressHUD.dismiss();
     * <p>
     * 自定义toast
     * IToast.show(Activity_Login.this,"登录成功");//时间短
     * IToast.showLong(Activity_Login.this,"登录成功");//时间长
     */

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

    @Override
    public int getLayoutResId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initTitleBar() {

    }

    @Override
    protected void initData() {
        if (!HGTool.isEmpty(BaseApplication.getApplication().getOid())) {
            initUserInfo();
        } else {
            new Thread() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    if (!BaseApplication.getApplication().isEnter()) {
                        startActivity(new Intent(Activity_Welcome.this, Activity_WelcomeGuide.class));
                        finish();//进入引导页
                        return;
                    }
                    startActivity(new Intent(Activity_Welcome.this, Activity_Main.class));
                    finish();//进入主页
                }
            }.start();
        }
    }

    private void initUserInfo() {
        OkHttpUtils
                .post()
                .url(Api.GET_USER_INFO_BYOID)
                .addParams("model.oid", BaseApplication.getApplication().getOid())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("==根据用户ID获取用户信息 ：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==根据用户ID获取用户信息 ：：：", response);
                        Log.e("==根据用户ID获取用户信息 ：：：", Api.GET_USER_INFO_BYOID);
                        Log.e("==根据用户ID获取用户信息 ：：：", BaseApplication.getApplication().getOid());
                        LoginModel mLoginModel = JSONObject.parseObject(response, LoginModel.class);
                        if (RequestType.SUCCESS.equals(mLoginModel.getStatus())) {
                            initSaveUserInfo(mLoginModel);
                            new Thread() {
                                @Override
                                public void run() {
                                    SystemClock.sleep(2000);
                                    if (!BaseApplication.getApplication().isEnter()) {
                                        startActivity(new Intent(Activity_Welcome.this, Activity_WelcomeGuide.class));
                                        finish();//进入引导页
                                        return;
                                    }
                                    startActivity(new Intent(Activity_Welcome.this, Activity_Main.class));
                                    finish();//进入主页
                                }
                            }.start();
                        }else{
                            EMClient.getInstance().logout(false, new EMCallBack() {

                                @Override
                                public void onSuccess() {
                                    initReleaseOpenClear();
                                    /**
                                     * 登录之后才能注册别名
                                     */
                                    if (!BaseApplication.getApplication().isLogined()) {
                                        JPushInterface.onResume(Activity_Welcome.this);
                                        tuisongSetAlias("");
                                    }
                                    if (!BaseApplication.getApplication().isEnter()) {
                                        startActivity(new Intent(Activity_Welcome.this, Activity_WelcomeGuide.class));
                                        finish();//进入引导页
                                        return;
                                    }
                                    startActivity(new Intent(Activity_Welcome.this, Activity_Main.class));
                                    finish();//进入主页
                                }

                                @Override
                                public void onProgress(int progress, String status) {
                                    Log.e("退出失败", progress + "..." + status);
                                }

                                @Override
                                public void onError(int code, String message) {

                                }
                            });
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

    /**
     * 为用户设置极光推送的别名
     */
    private void tuisongSetAlias(String mAlias) {
        JPushInterface.setAlias(Activity_Welcome.this, mAlias + "", new TagAliasCallback() {
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
