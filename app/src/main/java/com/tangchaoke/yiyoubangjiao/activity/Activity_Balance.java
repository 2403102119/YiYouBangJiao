package com.tangchaoke.yiyoubangjiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.BalanceAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.BalanceModel;
import com.tangchaoke.yiyoubangjiao.model.ExpensesRecordModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.utils.NetWorkUsefulUtils;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.tangchaoke.yiyoubangjiao.view.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 我的账户
*/
public class Activity_Balance extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.ll_adoption)
    LinearLayout mLlAdoption;

    @BindView(R.id.ll_top_right_release)
    LinearLayout mLlTopRightRelease;

    /**
     * 账户余额
     */
    @BindView(R.id.tv_balance)
    TextView mTvBalance;

    private String mBalance;

    /**
     * 用户积分
     */
    @BindView(R.id.tv_integral)
    TextView mTvIntegral;

    @OnClick({R.id.ll_back, R.id.but_withdraw, R.id.but_detail})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                mEditor.putString("activity", "");
                mEditor.commit();
                break;

            /**
             * 提现
             */
            case R.id.but_withdraw:
                if (!HGTool.isEmpty(mBalance)) {
                    if (mBalance.equals("0.0")) {
                        IToast.show(Activity_Balance.this, "暂无提现金额");
                    } else {
                        Intent mIntentWithdraw = new Intent(Activity_Balance.this, Activity_Withdraw.class);
                        startActivity(mIntentWithdraw);
                    }
                }
                break;

            case R.id.but_detail:
                Intent mIntentDetail = new Intent(Activity_Balance.this, Activity_Detail.class);
                startActivity(mIntentDetail);
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_balance;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setVisibility(View.VISIBLE);
        mTvTopTitle.setText("我的账户");
        mLlAdoption.setVisibility(View.GONE);
        mLlTopRightRelease.setVisibility(View.INVISIBLE);
    }

    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;

    @Override
    protected void initData() {
        localBroadcastManager = LocalBroadcastManager.getInstance(Activity_Balance.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tangchaoke.yiyoubangjiao.balance");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra("tuisong");
            if (num.equals("balance")) {
                initBalance();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initBalance();
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putString("activity", "Activity_Balance");
        mEditor.commit();
    }

    /**
     * 账户余额
     */
    private void initBalance() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_Balance.this, "加载中", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.GET_USER_BALANCE)
                .addParams("token", BaseApplication.getApplication().getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Balance.this, "服务器开小差！请稍后重试");
                        Log.e("==获取用户余额", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==获取用户余额", response);
                        Log.e("==获取用户余额", Api.GET_USER_BALANCE);
                        Log.e("==获取用户余额", BaseApplication.getApplication().getToken());
                        BalanceModel mBalanceModel = JSONObject.parseObject(response, BalanceModel.class);
                        mProgressHUD.dismiss();
                        if (RequestType.SUCCESS.equals(mBalanceModel.getStatus())) {
                            if (!HGTool.isEmpty(mBalanceModel.getBalance())) {
                                mBalance = mBalanceModel.getBalance();
                                mTvBalance.setText(mBalance);
                            }
                            if (!HGTool.isEmpty(mBalanceModel.getIntegral())) {
                                mTvIntegral.setText(mBalanceModel.getIntegral());
                            }
                        } else {
                            if (mBalanceModel.getStatus().equals("9") || mBalanceModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mBalanceModel.getStatus(), Activity_Balance.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_Balance.this, "登录失效");
                                }
                            } else if (mBalanceModel.getStatus().equals("0")) {
                                IToast.show(Activity_Balance.this, mBalanceModel.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
            mEditor.putString("activity", "");
            mEditor.commit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


}
