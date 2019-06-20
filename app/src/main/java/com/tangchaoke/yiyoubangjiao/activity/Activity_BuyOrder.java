package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.AlipayRechargeModel;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.model.WeChatPayRechargeModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.alipay.PayResult;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.wxapi.WXPayEntryActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 订单支付
*/
public class Activity_BuyOrder extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    private String mMoney = "";

    private String mOrderNumber = "";

    @BindView(R.id.cb_zfb)
    CheckBox mCbZfb;

    @BindView(R.id.cb_wx)
    CheckBox mCbWx;

    @BindView(R.id.cb_balance)
    CheckBox mCbBalance;

    @BindView(R.id.tv_balance)
    TextView mTvBalance;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((String) msg.obj);
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                IToast.show(Activity_BuyOrder.this, "支付成功");
                Intent mIntent = new Intent(Activity_BuyOrder.this, Activity_Order.class);
                mIntent.putExtra("type", "2");
                startActivity(mIntent);
                finish();
            } else {
                // 判断resultStatus 为非“9000”则代表可能支付失败
                // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，
                // 最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    IToast.show(Activity_BuyOrder.this, "支付结果确认中");
                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    IToast.show(Activity_BuyOrder.this, "支付失败");
                }
            }
        }
    };

    @OnClick({R.id.ll_back, R.id.but_buy})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.but_buy:
                if (!HGTool.isEmpty(mType)) {
                    if (mType.equals("1")) {
                        initZfbPay();
                    } else if (mType.equals("2")) {
                        initWxPay();
                    } else if (mType.equals("3")) {
                        initBalancePay();
                    }
                } else {
                    IToast.show(Activity_BuyOrder.this, "请选择支付方式 ！ ");
                }
                break;

        }

    }

    /**
     * 支付宝支付
     */
    private void initZfbPay() {
        OkHttpUtils
                .post()
                .url(Api.APP_PAY)
                .addParams("out_trade_no", mOrderNumber)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_BuyOrder.this, "服务器开小差！请稍后重试");
                        Log.e("==支付宝支付获取单号：：：", e.getMessage());
                        Log.e("==微信支付获取单号：：：", Api.APP_PAY);
                        Log.e("==微信支付获取单号：：：", mOrderNumber + "");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==支付宝支付获取单号：：：", response);
                        Log.e("==支付宝支付获取单号：：：", Api.APP_PAY);
                        Log.e("==支付宝支付获取单号：：：", mOrderNumber + "");
                        AlipayRechargeModel mAlipayRechargeModel = JSONObject.parseObject(response, AlipayRechargeModel.class);
                        if (RequestType.SUCCESS.equals(mAlipayRechargeModel.getStatus())) {
                            realAlipay(mAlipayRechargeModel.getData());
                        } else if (mAlipayRechargeModel.getStatus().equals("0")) {
                            IToast.show(Activity_BuyOrder.this, mAlipayRechargeModel.getMessage());
                        }
                    }
                });

    }

    /**
     * 2.支付宝支付第三步：真正请求
     *
     * @param data
     */
    private void realAlipay(String data) {
        // 订单
        final String orderInfo = data;
        // 必须异步调用支付接口进行支付
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(Activity_BuyOrder.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(orderInfo, true);
                Message msg = new Message();
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 微信支付
     */
    private void initWxPay() {
        OkHttpUtils
                .post()
                .url(Api.APP_PAY_WX)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("out_trade_no", mOrderNumber)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_BuyOrder.this, "服务器开小差！请稍后重试");
                        Log.e("==微信支付获取单号：：：", e.getMessage());
                        Log.e("==微信支付获取单号：：：", Api.APP_PAY_WX);
                        Log.e("==微信支付获取单号：：：", mOrderNumber + "");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==微信支付获取单号：：：", response);
                        WeChatPayRechargeModel mWeChatPayRechargeModel = JSONObject.parseObject(response, WeChatPayRechargeModel.class);
                        if (RequestType.SUCCESS.equals(mWeChatPayRechargeModel.getStatus())) {
                            Intent intent = new Intent(Activity_BuyOrder.this, WXPayEntryActivity.class);
                            intent.putExtra("appid", mWeChatPayRechargeModel.getApp_id());
                            intent.putExtra("partnerid", mWeChatPayRechargeModel.getPartner_id());
                            intent.putExtra("prepayid", mWeChatPayRechargeModel.getPrepay_id());
                            intent.putExtra("package", mWeChatPayRechargeModel.getPackage_value());
                            intent.putExtra("noncestr", mWeChatPayRechargeModel.getNonce_str());
                            intent.putExtra("timestamp", mWeChatPayRechargeModel.getTime_stamp() + "");
                            intent.putExtra("sign", mWeChatPayRechargeModel.getSign());
                            intent.putExtra("type", "1");
                            startActivity(intent);
                            addActivity(Activity_BuyOrder.this);
                        } else if (mWeChatPayRechargeModel.getStatus().equals("0")) {
                            IToast.show(Activity_BuyOrder.this, mWeChatPayRechargeModel.getMessage());
                        }
                    }
                });
    }

    /**
     * 余额支付
     */
    private void initBalancePay() {

        OkHttpUtils
                .post()
                .url(Api.PAY_ORDER)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("type", "1")
                .addParams("model.orderNumber", mOrderNumber)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_BuyOrder.this, "服务器开小差 请稍后再试 ！");
                        Log.e("==支付订单", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==支付订单", response);
                        Log.e("==支付订单", Api.PAY_ORDER);
                        Log.e("==支付订单", BaseApplication.getApplication().getToken());
                        Log.e("==支付订单", "1");
                        Log.e("==支付订单", mOrderNumber);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            Intent mIntent = new Intent(Activity_BuyOrder.this, Activity_Order.class);
                            mIntent.putExtra("type", "2");
                            startActivity(mIntent);
                            finish();
                            IToast.show(Activity_BuyOrder.this, mSuccessModel.getMessage());
                        } else {
                            IToast.show(Activity_BuyOrder.this, mSuccessModel.getMessage());
                        }
                    }
                });

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_buy_order;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("支付");
    }

    /**
     * 1 支付宝  2 微信 3 余额
     */
    private String mType = "1";

    @Override
    protected void initData() {
        mMoney = getIntent().getStringExtra("money");
        mOrderNumber = getIntent().getStringExtra("orderNumber");
        mTvBalance.setText("¥" + mMoney);
        mCbZfb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mCbZfb.setChecked(true);
                    mCbWx.setChecked(false);
                    mCbBalance.setChecked(false);
                    mType = "1";
                } else {
                    mType = "";
                }
            }
        });
        mCbWx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mCbWx.setChecked(true);
                    mCbZfb.setChecked(false);
                    mCbBalance.setChecked(false);
                    mType = "2";
                } else {
                    mType = "";
                }
            }
        });
        mCbBalance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mCbBalance.setChecked(true);
                    mCbZfb.setChecked(false);
                    mCbWx.setChecked(false);
                    mType = "3";
                } else {
                    mType = "";
                }
            }
        });
    }
}
