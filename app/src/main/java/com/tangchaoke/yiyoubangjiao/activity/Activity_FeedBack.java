package com.tangchaoke.yiyoubangjiao.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
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
* description:  意见与反馈
*/
public class Activity_FeedBack extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.edit_content)
    EditText mEditContent;

    private String mContent;

    @BindView(R.id.edit_phone_number)
    EditText mEditPhoneNumber;

    private String mPhoneNumber;

    @OnClick({R.id.ll_back, R.id.but_submit})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.but_submit:
                mContent = mEditContent.getText().toString().trim();
                mPhoneNumber = mEditPhoneNumber.getText().toString().trim();
                initSubmitFeedBack(mContent, mPhoneNumber);
                break;

        }

    }

    /**
     * 提交意见反馈
     *
     * @param mContent
     * @param mPhoneNumber
     */
    private void initSubmitFeedBack(final String mContent, final String mPhoneNumber) {
        if (HGTool.isEmpty(mContent)) {
            IToast.show(Activity_FeedBack.this, "请输入反馈意见");
            return;
        }

        if (mContent.length() < 10) {
            IToast.show(Activity_FeedBack.this, "反馈意见不能少于10个字");
            return;
        }

        if (HGTool.isEmpty(mPhoneNumber)) {
            IToast.show(Activity_FeedBack.this, "请输入手机号");
            return;
        }

        if (mPhoneNumber.length() < 11) {
            IToast.show(Activity_FeedBack.this, "请输入正确的手机号");
            return;
        }

        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_FeedBack.this, "提交中", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.SUBMIT_FEEDBACK)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.content", mContent)
                .addParams("model.phoneNumber", mPhoneNumber)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_FeedBack.this, "服务器开小差！请稍后重试");
                        Log.e("==意见反馈", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==意见反馈", response);
                        Log.e("==意见反馈", Api.SUBMIT_FEEDBACK);
                        Log.e("==意见反馈", BaseApplication.getApplication().getToken());
                        Log.e("==意见反馈", mContent);
                        Log.e("==意见反馈", mPhoneNumber);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        mProgressHUD.dismiss();
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            finish();
                            IToast.show(Activity_FeedBack.this, mSuccessModel.getMessage());
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(), Activity_FeedBack.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_FeedBack.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_FeedBack.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_feed_back;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("意见反馈");
    }

    private int maxNum = 200;

    @BindView(R.id.tv_suggestion)
    TextView mTvSuggestion;

    @Override
    protected void initData() {
        mEditContent.setSelection(mEditContent.getText().length());
        mTvSuggestion.setText("还能输入" + (maxNum - mEditContent.getText().toString().length()) + "字");
        mEditContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int number = maxNum - editable.length();
                mTvSuggestion.setText("还能输入" + (number) + "字");
            }
        });
    }

}
