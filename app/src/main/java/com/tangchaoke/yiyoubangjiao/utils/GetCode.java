package com.tangchaoke.yiyoubangjiao.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.activity.Activity_RecoverPassword;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.GetCodeModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/3/19.
 */

public class GetCode {

    public static void getCode(final Activity mActivity, final String mPhone, final String mType,
                               final TextView mTvSendCode, final EditText mEditCode) {

        if (HGTool.isEmpty(mPhone)) {
            IToast.show(mActivity, "请输入手机号");
            return;
        }

        if (mPhone.length() < 11) {
            IToast.show(mActivity, "请输入正确的手机号");
            return;
        }

        OkHttpUtils
                .post()
                .url(Api.SEND_CODE)
                .addParams("model.account", mPhone)
                .addParams("type", mType)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(mActivity, "服务器开小差！请稍后重试");
                        Log.e("====获取验证码 ：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====获取验证码 ：：：", response);
                        Log.e("====获取验证码 ：：：", Api.SEND_CODE);
                        Log.e("====获取验证码 ：：：", mPhone);
                        Log.e("====获取验证码 ：：：", mType);
                        GetCodeModel mGetCodeModel = JSONObject.parseObject(response, GetCodeModel.class);
                        if (RequestType.SUCCESS.equals(mGetCodeModel.getStatus())) {
                            IToast.show(mActivity, mGetCodeModel.getMessage());
                            CountDownTimerUtils countUtils = new CountDownTimerUtils(60000, 1000, mTvSendCode, mEditCode);
                            countUtils.start();
                        } else {
                            IToast.show(mActivity, mGetCodeModel.getMessage());
                        }
                    }
                });
    }

}
