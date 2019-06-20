package com.tangchaoke.yiyoubangjiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.OrderDetailsBean;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.PromptDialogView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 订单详情
*/
public class Activity_OrderInfo extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    private String mOid = "";

    @OnClick({R.id.ll_back, R.id.but_submit})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                mEditor.putString("activity", "");
                mEditor.commit();
                break;

            case R.id.but_submit:
                if (mOrderDetailsBean.getMap().getOrderStatus().equals("0")) {
                    Intent mIntentBuyOrder = new Intent(Activity_OrderInfo.this, Activity_BuyOrder.class);
                    mIntentBuyOrder.putExtra("orderNumber", mOrderDetailsBean.getMap().getOrderNumber());
                    mIntentBuyOrder.putExtra("money", mOrderDetailsBean.getMap().getAllMoney());
                    startActivity(mIntentBuyOrder);
                } else if (mOrderDetailsBean.getMap().getOrderStatus().equals("2")) {
                    isPromptDialog(mOid);
                } else if (mOrderDetailsBean.getMap().getOrderStatus().equals("3")) {
                    Intent mIntentOrderComment = new Intent(Activity_OrderInfo.this, Activity_OrderComment.class);
                    mIntentOrderComment.putExtra("oid", mOid);
                    startActivity(mIntentOrderComment);
                }
                break;

        }

    }

    PromptDialogView mPromptDialogView;

    private void isPromptDialog(final String mOid) {
        try {
            mPromptDialogView = new PromptDialogView(Activity_OrderInfo.this);
            mPromptDialogView.setTitle("温馨提示");
            mPromptDialogView.setContent("是否确认已收到货物 ？");
            mPromptDialogView.setYes("确认收货");
            mPromptDialogView.setNo("取消");
            mPromptDialogView.setCancelable(false);
            mPromptDialogView.setCustomOnClickListenerYes(new PromptDialogView.OnCustomDialogListener() {
                @Override
                public void setYesOnclick() {
                    initConfirmReceipt(mOid);
                }

                @Override
                public void setNoOnclick() {
                    mPromptDialogView.dismiss();
                }
            });
            mPromptDialogView.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initConfirmReceipt(final String mOid) {
        OkHttpUtils
                .post()
                .url(Api.CONFIRM_RECEIPT)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_OrderInfo.this, "服务器开小差 请稍后再试 ！ ");
                        Log.e("==确认收货:::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==确认收货:::", response);
                        Log.e("==确认收货:::", Api.CONFIRM_RECEIPT);
                        Log.e("==确认收货:::", BaseApplication.getApplication().getToken());
                        Log.e("==确认收货:::", mOid);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            mPromptDialogView.dismiss();
                            Intent mIntentOrderComment = new Intent(Activity_OrderInfo.this, Activity_OrderComment.class);
                            mIntentOrderComment.putExtra("oid", mOid);
                            startActivity(mIntentOrderComment);
                        } else {
                            IToast.show(Activity_OrderInfo.this, mSuccessModel.getMessage());
                        }
                    }
                });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_info;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("订单详情");
    }

    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;

    @Override
    protected void initData() {
        mOid = getIntent().getStringExtra("oid");

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tangchaoke.yiyoubangjiao.order");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra("tuisong");
            String mTOid = intent.getStringExtra("oid");
            /**
             * 如果有新的消息，在此界面要 及时进行刷新
             */
            if (num.equals("order")) {
                if (mTOid.equals(mOid)) {
                    initOrderDetails();
                } else {
                    return;
                }
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        initOrderDetails();
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putString("activity", "Activity_OrderInfo");
        mEditor.commit();
    }

    OrderDetailsBean mOrderDetailsBean;

    private void initOrderDetails() {
        OkHttpUtils
                .post()
                .url(Api.GET_ORDER_DETAILS)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_OrderInfo.this, "服务器开小差 请稍后再试 ！ ");
                        Log.e("==获取订单详情:::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==获取订单详情:::", response);
                        Log.e("==获取订单详情:::", Api.GET_ORDER_DETAILS);
                        Log.e("==获取订单详情:::", BaseApplication.getApplication().getToken());
                        Log.e("==获取订单详情:::", mOid);
                        mOrderDetailsBean = JSONObject.parseObject(response, OrderDetailsBean.class);
                        if (RequestType.SUCCESS.equals(mOrderDetailsBean.getStatus())) {
                            initOrderInfoShow(mOrderDetailsBean.getMap());
                        } else {
                            IToast.show(Activity_OrderInfo.this, mOrderDetailsBean.getMessage());
                        }
                    }
                });

    }

    @BindView(R.id.tv_address_name_or_phone)
    TextView mTvAddressNameOrPhone;

    @BindView(R.id.tv_address)
    TextView mTvAddress;

    @BindView(R.id.img_commodity_photo)
    ImageView mImgCommodityPhoto;

    @BindView(R.id.tv_commodity_name)
    TextView mTvCommodityName;

    @BindView(R.id.tv_commodity_material)
    TextView mTvCommodityMaterial;

    @BindView(R.id.tv_integral_or_money)
    TextView mTvIntegralOrMoney;

    @BindView(R.id.tv_payType)
    TextView mTvPayType;

    @BindView(R.id.tv_leavingMessage)
    TextView mTvLeavingMessage;

    @BindView(R.id.tv_orderNumber)
    TextView mTvOrderNumber;

    @BindView(R.id.tv_startTime)
    TextView mTvStartTime;

    @BindView(R.id.tv_pay_style)
    TextView mTvPayStyle;

    @BindView(R.id.but_submit)
    Button mButSubmit;

    private void initOrderInfoShow(OrderDetailsBean.OrderDetailsMapBean mMap) {

        if (!HGTool.isEmpty(mMap.getName()) && !HGTool.isEmpty(mMap.getPhone())) {
            mTvAddressNameOrPhone.setText(mMap.getName() + "    " + mMap.getPhone());
        }

        if (!HGTool.isEmpty(mMap.getDetailedAddress())) {
            mTvAddress.setText(mMap.getDetailedAddress());
        }

        if (!HGTool.isEmpty(mMap.getCommodityPhoto())) {
            Glide.with(Activity_OrderInfo.this).load(Api.PATH + mMap.getCommodityPhoto()).into(mImgCommodityPhoto);
        }

        if (!HGTool.isEmpty(mMap.getCommodityName())) {
            mTvCommodityName.setText(mMap.getCommodityName());
        }

        if (!HGTool.isEmpty(mMap.getCommodityMaterial())) {
            mTvCommodityMaterial.setText(mMap.getCommodityMaterial());
        }

        if (!HGTool.isEmpty(mMap.getAllIntegral()) && !HGTool.isEmpty(mMap.getAllMoney())) {
            mTvIntegralOrMoney.setText(mMap.getAllIntegral() + "积分/" + mMap.getAllMoney() + "元");
        }

        if (!HGTool.isEmpty(mMap.getPayType())) {
            if (mMap.getPayType().equals("1")) {
                mTvPayType.setText(mMap.getAllIntegral() + "积分");
            } else if (mMap.getPayType().equals("2") || mMap.getPayType().equals("3") || mMap.getPayType().equals("4")) {
                mTvPayType.setText(mMap.getAllMoney() + "元");
            }
        }

        if (!HGTool.isEmpty(mMap.getLeavingMessage())) {
            mTvLeavingMessage.setText(mMap.getLeavingMessage());
        }

        if (!HGTool.isEmpty(mMap.getOrderNumber())) {
            mTvOrderNumber.setText(mMap.getOrderNumber());
        }

        if (!HGTool.isEmpty(mMap.getStartTime())) {
            mTvStartTime.setText(mMap.getStartTime());
        }

        if (!HGTool.isEmpty(mMap.getPayType())) {
            if (mMap.getPayType().equals("1")) {
                mTvPayStyle.setText("积分支付");
            } else if (mMap.getPayType().equals("2")) {
                mTvPayStyle.setText("余额支付");
            } else if (mMap.getPayType().equals("3")) {
                mTvPayStyle.setText("支付宝支付");
            } else if (mMap.getPayType().equals("4")) {
                mTvPayStyle.setText("微信支付");
            }
        } else {
            mTvPayStyle.setText("暂无支付");
        }

        if (mMap.getOrderStatus().equals("0")) {
            mButSubmit.setVisibility(View.VISIBLE);
            mButSubmit.setText("去支付");
        } else if (mMap.getOrderStatus().equals("1")) {
            mButSubmit.setVisibility(View.GONE);
        } else if (mMap.getOrderStatus().equals("2")) {
            mButSubmit.setVisibility(View.VISIBLE);
            mButSubmit.setText("确认完成");
        } else if (mMap.getOrderStatus().equals("3")) {
            mButSubmit.setVisibility(View.VISIBLE);
            mButSubmit.setText("发表评论");
        } else if (mMap.getOrderStatus().equals("4")) {
            mButSubmit.setVisibility(View.GONE);
        }
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
