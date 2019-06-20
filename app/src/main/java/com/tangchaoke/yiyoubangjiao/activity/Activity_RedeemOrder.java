package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.ConfirmationOrderBean;
import com.tangchaoke.yiyoubangjiao.model.SubmitOrderBean;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 提交订单
*/
public class Activity_RedeemOrder extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    private String mOid = "";

    /**
     * 1 积分购买 2 金钱购买
     */
    private String mType = "";

    @BindView(R.id.edit_leaving_message)
    EditText mEditLeavingMessage;

    private String mLeavingMessage = "";

    @OnClick({R.id.ll_back, R.id.but_redeem_order_submit, R.id.ll_address_add, R.id.ll_address_have})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.ll_address_add:
                Intent mIntentSaveAddress = new Intent(Activity_RedeemOrder.this, Activity_Address.class);
                mIntentSaveAddress.putExtra("type", "1");//1 添加地址 2 修改地址
                startActivityForResult(mIntentSaveAddress, 200);
                break;

            case R.id.ll_address_have:
                Intent mIntentAddress = new Intent(Activity_RedeemOrder.this, Activity_Address.class);
                mIntentAddress.putExtra("type", "2");//1 添加地址 2 修改地址
                mIntentAddress.putExtra("addressOid", mConfirmationOrderBean.getMap().getUserAddress().getOid());
                mIntentAddress.putExtra("addressName", mConfirmationOrderBean.getMap().getUserAddress().getName());
                mIntentAddress.putExtra("addressPhone", mConfirmationOrderBean.getMap().getUserAddress().getPhone());
                mIntentAddress.putExtra("address", mConfirmationOrderBean.getMap().getUserAddress().getDetailedAddress());
                startActivityForResult(mIntentAddress, 200);
                break;

            case R.id.but_redeem_order_submit:
                mLeavingMessage = mEditLeavingMessage.getText().toString();
                initSubmitOrder(mLeavingMessage);
                break;

        }

    }

    private void initSubmitOrder(final String mLeavingMessage) {
        OkHttpUtils
                .post()
                .url(Api.SUBMIT_ORDER)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.leavingMessage", mLeavingMessage)
                .addParams("model.oid", mConfirmationOrderBean.getMap().getOrderOid())
                .addParams("type", mType)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_RedeemOrder.this, "服务器开小差！请稍后重试");
                        Log.e("==提交订单 ：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==提交订单 ：：：", response);
                        Log.e("==提交订单 ：：：", Api.SUBMIT_ORDER);
                        Log.e("==提交订单 ：：：", BaseApplication.getApplication().getToken());
                        Log.e("==提交订单 ：：：", mOid);
                        Log.e("==提交订单 ：：：", mType);
                        Log.e("==提交订单 ：：：", mLeavingMessage);
                        SubmitOrderBean mSubmitOrderBean = JSONObject.parseObject(response, SubmitOrderBean.class);
                        if (RequestType.SUCCESS.equals(mSubmitOrderBean.getStatus())) {
                            if (mType.equals("1")) {
                                Intent mIntentSuccess = new Intent(Activity_RedeemOrder.this, Activity_Success.class);
                                startActivity(mIntentSuccess);
                                finish();
                            } else if (mType.equals("2")) {
                                Intent mIntentBuyOrder = new Intent(Activity_RedeemOrder.this, Activity_BuyOrder.class);
                                mIntentBuyOrder.putExtra("orderNumber", mSubmitOrderBean.getOrderNumber());
                                mIntentBuyOrder.putExtra("money", mConfirmationOrderBean.getMap().getMoney());
                                startActivity(mIntentBuyOrder);
                                finish();
                            }
                        } else {
                            IToast.show(Activity_RedeemOrder.this, mSubmitOrderBean.getMessage());
                        }
                    }
                });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_redeem_order;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("提交订单");
    }

    @Override
    protected void initData() {
        mOid = getIntent().getStringExtra("oid");
        mType = getIntent().getStringExtra("type");
        initConfirmationOrder();
    }

    ConfirmationOrderBean mConfirmationOrderBean;

    private void initConfirmationOrder() {
        OkHttpUtils
                .post()
                .url(Api.CONFIRMATION_ORDER)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.commodity.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_RedeemOrder.this, "服务器开小差！请稍后重试");
                        Log.e("==确认订单 ：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==确认订单 ：：：", response);
                        Log.e("==确认订单 ：：：", Api.CONFIRMATION_ORDER);
                        Log.e("==确认订单 ：：：", BaseApplication.getApplication().getToken());
                        Log.e("==确认订单 ：：：", mOid);
                        mConfirmationOrderBean = JSONObject.parseObject(response, ConfirmationOrderBean.class);
                        if (RequestType.SUCCESS.equals(mConfirmationOrderBean.getStatus())) {
                            initRedeemOrder(mConfirmationOrderBean.getMap());
                        } else {
                            IToast.show(Activity_RedeemOrder.this, "服务器开小差 请稍后再试 ！ ");
                        }
                    }
                });
    }

    @BindView(R.id.ll_address_add)
    LinearLayout mLlAddressAdd;

    @BindView(R.id.ll_address_have)
    LinearLayout mLlAddressHave;

    @BindView(R.id.tv_address_name_or_phone)
    TextView mTvAddressNameOrPhone;

    @BindView(R.id.tv_detailedAddress)
    TextView mTvDetailedAddress;

    @BindView(R.id.tv_name)
    TextView mTvName;

    @BindView(R.id.tv_material)
    TextView mTvMaterial;

    @BindView(R.id.tv_integral_or_money)
    TextView mTvIntegralOrMoney;

    @BindView(R.id.tv_total)
    TextView mTvTotal;

    @BindView(R.id.tv_need_pay)
    TextView mTvNeedPay;

    @BindView(R.id.img_photo)
    ImageView mImgPhoto;

    private void initRedeemOrder(ConfirmationOrderBean.ConfirmationOrderMapBean mMap) {

        if (!HGTool.isEmpty(mMap.getUserAddress().getName())
                && !HGTool.isEmpty(mMap.getUserAddress().getPhone())
                && !HGTool.isEmpty(mMap.getUserAddress().getDetailedAddress())) {
            mLlAddressAdd.setVisibility(View.GONE);
            mLlAddressHave.setVisibility(View.VISIBLE);
            mTvAddressNameOrPhone.setText(mMap.getUserAddress().getName() + "    " + mMap.getUserAddress().getPhone());
            mTvDetailedAddress.setText(mMap.getUserAddress().getDetailedAddress());
        } else {
            mLlAddressAdd.setVisibility(View.VISIBLE);
            mLlAddressHave.setVisibility(View.GONE);
        }

        if (!HGTool.isEmpty(mMap.getName())) {
            mTvName.setText(mMap.getName());
        }

        if (!HGTool.isEmpty(mMap.getPhoto())) {
            Glide.with(Activity_RedeemOrder.this).load(Api.PATH + mMap.getPhoto()).into(mImgPhoto);
        }

        if (!HGTool.isEmpty(mMap.getMaterial())) {
            mTvMaterial.setText(mMap.getMaterial());
        }

        if (!HGTool.isEmpty(mMap.getIntegral()) && !HGTool.isEmpty(mMap.getMoney())) {
            mTvIntegralOrMoney.setText(mMap.getIntegral() + "积分/" + mMap.getMoney() + "元");
        }

        if (!HGTool.isEmpty(mType)) {
            if (mType.equals("1")) {
                if (!HGTool.isEmpty(mMap.getIntegral())) {
                    mTvTotal.setText(mMap.getIntegral() + "积分");
                    mTvNeedPay.setText(mMap.getIntegral() + "积分");
                }
            } else if (mType.equals("2")) {
                if (!HGTool.isEmpty(mMap.getMoney())) {
                    mTvTotal.setText(mMap.getMoney() + "元");
                    mTvNeedPay.setText(mMap.getMoney() + "元");
                }
            }
        }
    }

    private String mAddressOid = "";
    private String mAddressName = "";
    private String mAddressPhone = "";
    private String mAddress = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 201) {
            mAddressOid = data.getStringExtra("addressOid");
            mAddressName = data.getStringExtra("addressName");
            mAddressPhone = data.getStringExtra("addressPhone");
            mAddress = data.getStringExtra("address");
            mLlAddressAdd.setVisibility(View.GONE);
            mLlAddressHave.setVisibility(View.VISIBLE);
            if (!HGTool.isEmpty(mAddressName) && !HGTool.isEmpty(mAddressPhone)) {
                mTvAddressNameOrPhone.setText(mAddressName + "    " + mAddressPhone);
            }
            if (!HGTool.isEmpty(mAddress)) {
                mTvDetailedAddress.setText(mAddress);
            }
        }
    }
}
