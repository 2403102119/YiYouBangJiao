package com.tangchaoke.yiyoubangjiao.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Login;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Main;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Settings;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.view.CleanUpCacheDialogView;

/**
 * @author HG
 * @version v1.0
 * @ClassName CheckLoginExceptionUtil.java
 * @Description 检测登陆异常的工具类
 * @decode HG
 * @time 2017/8/15 17:33
 */
public class CheckLoginExceptionUtil {

    private static CleanUpCacheDialogView mDialog;


    /**
     * 校验用户登陆是否超时
     *
     * @param mMessage
     * @param mActivity
     * @param isClose
     */
    public static boolean checkLoginState(String mMessage, Activity mActivity, boolean isClose) {
        boolean isAccountException = false;//是否登录超时
        if (mMessage.equals("9") || mMessage.equals("8")) {
            //提醒用户重新登陆之前清空本地数据
            SharedPreferences.Editor editor = BaseApplication.getApplication().getEditor();
            editor.putBoolean("isLogined", false);
            editor.clear();
            editor.commit();
            userNotice(mMessage, mActivity, isClose);
            isAccountException = true;
        }
        return isAccountException;
    }


    /**
     * 用户提醒弹窗--用户异常登录提醒用户重新登录
     */
    private static void userNotice(String mMessage, final Activity mActivity, final boolean isClose) {
        try {
            mDialog = new CleanUpCacheDialogView(mActivity);
            mDialog.setTitle("温馨提示");
            if (mMessage.equals("9")) {
                mDialog.setContent(mActivity.getResources().getString(R.string.login_failure));//提示框标题
                mDialog.setYes("重新登录");//确定按钮文本
            } else if (mMessage.equals("8")) {
                mDialog.setContent(mActivity.getResources().getString(R.string.login_freeze));//提示框标题
                mDialog.setYes("退出登录");//确定按钮文本
            }

            mDialog.setCancelable(false);
            mDialog.setCustomOnClickListener(new CleanUpCacheDialogView.OnCustomDialogListener() {
                @Override
                public void setYesOnclick() {
                    EMClient.getInstance().logout(false, new EMCallBack() {

                        @Override
                        public void onSuccess() {
                            EMClient.getInstance().logout(true);
                            //确定
                            Intent mIntent = new Intent(mActivity, Activity_Login.class);
                            /**
                             * Activity 类名
                             */
                            mIntent.putExtra("islogin", mActivity.getClass().getSimpleName());
                            mActivity.startActivity(mIntent);
                            mDialog.dismiss();
                            if (!mActivity.getClass().getSimpleName().equals("Activity_Main")) {
                                mActivity.finish();
                            }
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
            });
            mDialog.show();
        } catch (Exception e) {

        }
    }
}
