package com.tangchaoke.yiyoubangjiao.activity;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;

import butterknife.BindView;
import butterknife.OnClick;

/*
* @author hg
* create at 2019/1/2
* description: 轮播图详情
*/
public class Activity_Banner extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.web_view)
    WebView mWebView;

    private String mPath;

    @OnClick(R.id.ll_back)
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_banner;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("轮播图详情");
    }

    @Override
    protected void initData() {
        mPath = getIntent().getStringExtra("path");
        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
        webSettings.setJavaScriptEnabled(true);//设置能够解析Javascript
        webSettings.setDomStorageEnabled(true);//设置适应Html5 //重点是这个设置
        if (!HGTool.isEmpty(mPath)) {
            if (mPath.toString().contains("http://")) {
                mPath = mPath + "?token=" + BaseApplication.getApplication().getToken();
                mWebView.loadUrl(mPath);
            } else if (mPath.toString().contains("https://")) {
                mPath = mPath + "?token=" + BaseApplication.getApplication().getToken();
                mWebView.loadUrl(mPath);
            } else {
                mPath = "http://" + mPath + "?token=" + BaseApplication.getApplication().getToken();
                mWebView.loadUrl(mPath);
            }
            Log.e("==海哥:::", mPath);
        }
        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_Banner.this,
                "加载中", true, false, null);
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
