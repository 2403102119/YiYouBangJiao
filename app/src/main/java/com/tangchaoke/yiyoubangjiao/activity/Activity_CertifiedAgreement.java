package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
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
* description:  认证协议
*/
public class Activity_CertifiedAgreement extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.cb_agreement)
    CheckBox mCbAgreement;

    /**
     * 1  选中协议  2  未选中协议
     */
    private String mIsSelected = "1";

    @BindView(R.id.web_view)
    WebView mWebView;

    /**
     * 协议类型  3：答题者协议  2：代课老师认证协议  1：家教认证协议
     */
    private String mType = "";

    @OnClick({R.id.ll_back, R.id.tv_next_step})
    void onClivk(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            /**
             * 下一步
             */
            case R.id.tv_next_step:
                if (!mIsSelected.equals("2")) {
                    initAgreement();
                } else {
                    IToast.show(Activity_CertifiedAgreement.this, "请选中协议");
                }
                break;

        }

    }

    private void initAgreement() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_CertifiedAgreement.this, "同意中", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.UPDATE_AGREEMENT_STATUS)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("type", mType)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_CertifiedAgreement.this, "服务器开小差！请稍后重试");
                        Log.e("==同意协议:::", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        mProgressHUD.dismiss();
                        Log.e("==同意协议:::", response);
                        Log.e("==同意协议:::", Api.UPDATE_AGREEMENT_STATUS);
                        Log.e("==同意协议:::", BaseApplication.getApplication().getToken());
                        Log.e("==同意协议:::", mType);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            initSaveUserInfoProtocol();
                            Intent mIntentActorCertified = new Intent(Activity_CertifiedAgreement.this, Activity_Certified.class);
                            mIntentActorCertified.putExtra("type", mType);//1家教 2代课老师 3答题者
                            startActivity(mIntentActorCertified);
                            finish();
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(), Activity_CertifiedAgreement.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_CertifiedAgreement.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_CertifiedAgreement.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });

    }

    /**
     * 存储协议已同意
     */
    private void initSaveUserInfoProtocol() {
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        if (mType.equals("3")) {
            /**
             * 答题者认证 协议
             */
            mEditor.putBoolean("respondentAgreement", true);
        }
        mEditor.commit();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_certified_agreement;
    }

    @Override
    public void initTitleBar() {
        if (mType.equals("3")) {
            /**
             * 答题者认证 协议
             */
            mTvTopTitle.setText("答题者认证协议");
        }
        mCbAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mIsSelected = "1";
                } else {
                    mIsSelected = "2";
                }
            }
        });
    }

    @Override
    protected void initData() {
        mType = getIntent().getStringExtra("type");
        initWebView(mType);
    }

    private void initWebView(String mType) {
        if (mType.equals("3")) {
            /**
             * 答题者认证 协议
             */
            mWebView.loadUrl(Api.PATH + "respondentRules.html");
        }
        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_CertifiedAgreement.this, "加载中",
                true, false, null);
        mWebView.setWebViewClient(new WebViewClient() {
            /**
             * 加载中
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            /**
             * 加载完成
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressHUD.dismiss();
            }

            /**
             * 加载失败
             * @param view
             * @param request
             * @param error
             */
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mProgressHUD.dismiss();
            }
        });
    }

}
