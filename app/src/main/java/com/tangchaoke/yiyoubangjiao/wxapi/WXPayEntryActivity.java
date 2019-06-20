package com.tangchaoke.yiyoubangjiao.wxapi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Answered;
import com.tangchaoke.yiyoubangjiao.activity.Activity_BuyOrder;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Order;
import com.tangchaoke.yiyoubangjiao.api.Constants;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import static com.tangchaoke.yiyoubangjiao.base.BaseActivity.clearActivity;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    private AlertDialog.Builder builder;

    String appid, partnerid, prepayid, packagex, noncestr, timestamp, sign;

    private Dialog mDialog;

    /**
     * 0 发布问题 1 购买商品
     */
    private String mType = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        appid = intent.getStringExtra("appid");
        partnerid = intent.getStringExtra("partnerid");
        prepayid = intent.getStringExtra("prepayid");
        packagex = intent.getStringExtra("package");
        noncestr = intent.getStringExtra("noncestr");
        timestamp = intent.getStringExtra("timestamp");
        sign = intent.getStringExtra("sign");
        mType = intent.getStringExtra("type");
        api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID);
        api.handleIntent(getIntent(), this);
        mDialog = new Dialog(WXPayEntryActivity.this);
        if (!api.isWXAppInstalled()) {
            Toast.makeText(WXPayEntryActivity.this, "没有安装微信", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (!api.isWXAppSupportAPI()) {
            Toast.makeText(WXPayEntryActivity.this, "当前微信版本不支持支付", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        PayReq req = new PayReq();
        req.appId = appid;
        req.partnerId = partnerid;
        req.prepayId = prepayid;
        req.nonceStr = noncestr;
        req.timeStamp = timestamp;
        req.packageValue = packagex;
        req.sign = sign;
        api.sendReq(req);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        //支付结果回调
        myHandler.sendEmptyMessage(0x001);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            mDialog = builder.show();
            builder.setMessage(getString(R.string.pay_result_callback_msg,
                    baseResp.errStr + ";code=" + String.valueOf(baseResp.errCode)));
            mDialog.show();
            if (String.valueOf(baseResp.errCode).equals("-1")) {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                    finish();
                    IToast.show(WXPayEntryActivity.this, "支付失败");
                }
            }
            if (String.valueOf(baseResp.errCode).equals("0")) {
                IToast.show(WXPayEntryActivity.this, "支付成功");
                if (mType.equals("0")) {
                    Intent mIntentUnAnswered = new Intent(WXPayEntryActivity.this, Activity_Answered.class);
                    mIntentUnAnswered.putExtra("type", "0");//0 未解答 1 待采纳 2 已解答
                    startActivity(mIntentUnAnswered);
                    clearActivity();
                    finish();
                } else if (mType.equals("1")) {
                    clearActivity();
                    Intent mIntent = new Intent(WXPayEntryActivity.this, Activity_Order.class);
                    mIntent.putExtra("type", "2");
                    startActivity(mIntent);
                    finish();
                }
            } else {
                finish();
                IToast.show(WXPayEntryActivity.this, "用户取消");
            }
        } else {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
                IToast.show(WXPayEntryActivity.this, "支付失败");
                finish();
            }

        }
    }

    @Override
    protected void onStop() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        super.onStop();
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    break;
            }
            super.handleMessage(msg);
        }
    };

}