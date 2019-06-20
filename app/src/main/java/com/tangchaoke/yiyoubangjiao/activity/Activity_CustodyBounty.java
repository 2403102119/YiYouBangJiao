package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.AlipayRechargeModel;
import com.tangchaoke.yiyoubangjiao.model.CalculationModel;
import com.tangchaoke.yiyoubangjiao.model.CouponListModel;
import com.tangchaoke.yiyoubangjiao.model.PaymentModel;
import com.tangchaoke.yiyoubangjiao.model.WeChatPayRechargeModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.utils.alipay.PayResult;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.tangchaoke.yiyoubangjiao.wxapi.WXPayEntryActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 发布问题支付
*/
public class Activity_CustodyBounty extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    /**
     * 问题内容
     */
    private String mContent;

    @BindView(R.id.tv_price)
    TextView mTvPrice;

    @BindView(R.id.edit_price)
    TextView mEditPrice;

    /**
     * 问题金额
     */
    private String mPrice = "0.0";

    /**
     * 问题年级
     */
    private String mSelectedGrade;

    /**
     * 问题科目
     */
    private String mSelectedSubject;

    private String mChessSpecies;

    private String mType3;

    /**
     * 问题图片
     */
    Uri mBitmap;

    @BindView(R.id.img_wx)
    ImageView mImgWx;

    @BindView(R.id.img_zfb)
    ImageView mImgZfb;

    private int mPlay = 1;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((String) msg.obj);
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                Intent mIntentUnAnswered = new Intent(Activity_CustodyBounty.this, Activity_Answered.class);
                mIntentUnAnswered.putExtra("type", "0");//0 未解答 1 待采纳 2 已解答
                startActivity(mIntentUnAnswered);
                clearActivity();
                finish();
            } else {
                // 判断resultStatus 为非“9000”则代表可能支付失败
                // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，
                // 最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    IToast.show(Activity_CustodyBounty.this, "支付结果确认中");
                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    IToast.show(Activity_CustodyBounty.this, "支付失败");
                }
            }
        }
    };

    @OnClick({R.id.ll_back, R.id.ll_wx, R.id.ll_zfb, R.id.but_confirm_play, R.id.edit_price})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.edit_price:
                if (mList.size() != 0) {
                    initCustodyBounty(mList);
                }
                break;

            case R.id.ll_wx:
                mPlay = 1;
                Glide.with(Activity_CustodyBounty.this).load(R.drawable.ic_wechat).into(mImgWx);
                Glide.with(Activity_CustodyBounty.this).load(R.drawable.ic_unalipay).into(mImgZfb);
                break;

            case R.id.ll_zfb:
                mPlay = 2;
                Glide.with(Activity_CustodyBounty.this).load(R.drawable.ic_unwechat).into(mImgWx);
                Glide.with(Activity_CustodyBounty.this).load(R.drawable.ic_alipay).into(mImgZfb);
                break;

            /**
             * 发布
             */
            case R.id.but_confirm_play:
                mPrice = mEditPrice.getText().toString().trim();
                initRelease(mContent, mPrice, mSelectedGrade, mSelectedSubject);
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_custody_bounty;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("托管金额");
        if (!HGTool.isEmpty(mPrice)) {
            mEditPrice.setText(mPrice + "");
            mTvPrice.setText("本次提问需支付" + mPrice + "元答题金额");
        }
    }

    @Override
    protected void initData() {
        mContent = getIntent().getStringExtra("content");
        mPrice = getIntent().getStringExtra("price");
        mSelectedGrade = getIntent().getStringExtra("grade");
        mSelectedSubject = getIntent().getStringExtra("subject");
        mChessSpecies = getIntent().getStringExtra("chessSpecies");
        mType3 = getIntent().getStringExtra("type3");
        mBitmap = Uri.parse(getIntent().getStringExtra("bitmap"));
        initCalculation();
    }

    private void initCalculation() {
        if (HGTool.isEmpty(mSelectedGrade)) {
            IToast.show(Activity_CustodyBounty.this, "请返回上一級选择年级");
            return;
        }

        OkHttpUtils
                .post()
                .url(Api.PAY_GET_FREE_COURSE_LIST)
                .addParams("model.grade", mSelectedGrade)
                .addParams("type", "Android")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_CustodyBounty.this, "服务器开小差！请稍后重试");
                        Log.e("==根据年级科目获取问题起始金额", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==根据年级科目获取问题起始金额", response);
                        Log.e("==根据年级科目获取问题起始金额", Api.PAY_GET_FREE_COURSE_LIST);
                        Log.e("==根据年级科目获取问题起始金额", mSelectedGrade);
                        Log.e("==根据年级科目获取问题起始金额", mSelectedSubject);
                        CalculationModel mCalculationModel = JSONObject.parseObject(response, CalculationModel.class);
                        if (RequestType.SUCCESS.equals(mCalculationModel.getStatus())) {
                            if (mCalculationModel.getMoney().size() != 0) {
                                for (int i = 0; i < mCalculationModel.getMoney().size(); i++) {
                                    mList.add(mCalculationModel.getMoney().get(i).getMoney());
                                }
                                initCustodyBounty(mList);
                            }
                        } else {
                            if (mCalculationModel.getStatus().equals("0")) {
                                IToast.show(Activity_CustodyBounty.this, mCalculationModel.getMessage());
                            }
                        }
                    }
                });
    }

    OptionsPickerView mEducationPvOptions;

    List<String> mList = new ArrayList<String>();

    private void initCustodyBounty(final List<String> mMoneyList) {
        mEducationPvOptions = new OptionsPickerView.Builder(Activity_CustodyBounty.this,
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //返回的分别是选中位置
                        mPrice = mMoneyList.get(options1);
                        mEditPrice.setText(mPrice + "");
                        mTvPrice.setText("本次提问需支付" + mPrice + "元答题金额");

                    }
                })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("请选择问题金额")//标题
                .setSubCalSize(20)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Activity_CustodyBounty.this.getResources().getColor(R.color.color999999))//确定按钮文字颜色
                .setCancelColor(Activity_CustodyBounty.this.getResources().getColor(R.color.color999999))//取消按钮文字颜色
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .setOutSideCancelable(false)//点击外部dismiss default true
                .build();
        mEducationPvOptions.setPicker(mMoneyList);
        mEducationPvOptions.show();
    }

    /**
     * 问题图片文件
     */
    File mFileReleaseQuestioLicense;
    private String mReleaseQuestioLicense;

    /**
     * 发布问题
     */
    private void initRelease(final String mContent, final String mPrice, final String mSelectedGrade, final String mSelectedSubject) {

        if (mPrice.equals("0.0")) {
            IToast.show(Activity_CustodyBounty.this, "请输入托管金额");
            return;
        }

        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_CustodyBounty.this, "提交中",
                true, false, null);
        PostFormBuilder builder = OkHttpUtils.post();
        builder.url(Api.PRESERVA_EXERCISES
                + "?token=" + BaseApplication.getApplication().getToken()
                + "&model.grade=" + mSelectedGrade
                + "&model.subject=" + mSelectedSubject
                + "&model.price=" + mPrice
                + "&model.content= " + mContent
                + "&model.chessSpecies=" + mChessSpecies
                + "&type3=" + mType3
                + "&type2=" + "Android");

        if (mBitmap != null) {
            if (!mBitmap.toString().equals("")) {
                mReleaseQuestioLicense = "releasequestio.png";
                mFileReleaseQuestioLicense = compressImage(decodeUriAsBitmap(mBitmap));/* 题目图片 */
            } else {
                mReleaseQuestioLicense = "";
                mFileReleaseQuestioLicense = new File("");/* 题目图片 */
            }
        } else {
            mReleaseQuestioLicense = "";
            mFileReleaseQuestioLicense = new File("");/* 题目图片 */
        }

        if (mFileReleaseQuestioLicense.exists()) {
            builder.addFile("release_questio", mReleaseQuestioLicense, mFileReleaseQuestioLicense);
        }

        builder
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_CustodyBounty.this, "服务器开小差！请稍后重试");
                        Log.e("====发布问题：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====发布问题：：：", response);
                        Log.e("====发布问题：：：", Api.PRESERVA_EXERCISES);
                        Log.e("====发布问题：：：", BaseApplication.getApplication().getToken());
                        Log.e("====发布问题：：：", mSelectedGrade + "::::model.grade");
                        Log.e("====发布问题：：：", mSelectedSubject + "::::model.subject");
                        Log.e("====发布问题：：：", mContent + "::::model.content");
                        Log.e("====发布问题：：：", mPrice + "::::model.price");
                        Log.e("====发布问题：：：", "Android" + "::::type2");
                        Log.e("====发布问题：：：", mType3 + "::::type3");
                        Log.e("====发布问题：：：", mChessSpecies + "::::model.chessSpecies");
                        PaymentModel mPaymentModel = JSONObject.parseObject(response, PaymentModel.class);
                        mProgressHUD.dismiss();
                        if (RequestType.SUCCESS.equals(mPaymentModel.getStatus())) {
                            if (mPaymentModel.getIsFree().equals("1")) {
                                //免费
                                Intent mIntentUnAnswered = new Intent(Activity_CustodyBounty.this, Activity_Answered.class);
                                mIntentUnAnswered.putExtra("type", "0");//0 未解答 1 待采纳 2 已解答
                                startActivity(mIntentUnAnswered);
                                clearActivity();
                                finish();
                            } else if (mPaymentModel.getIsFree().equals("0")) {
                                //付费
                                if (mPlay == 1) {
                                    /**
                                     * 微信支付
                                     */
                                    weChatPayRecharge(mPaymentModel.getOrderNumber());
                                } else if (mPlay == 2) {
                                    /**
                                     * 支付宝支付
                                     */
                                    alipayRecharge(mPaymentModel.getOrderNumber());
                                }
                            }
                        } else {
                            if (mPaymentModel.getStatus().equals("9") || mPaymentModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mPaymentModel.getStatus(),
                                        Activity_CustodyBounty.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_CustodyBounty.this, "登录失效");
                                }
                            } else if (mPaymentModel.getStatus().equals("0")) {
                                IToast.show(Activity_CustodyBounty.this, mPaymentModel.getMessage());
                            }
                        }
                    }
                });


    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 压缩图片（质量压缩）
     *
     * @param bitmap
     */
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(), filename + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        recycleBitmap(bitmap);
        return file;
    }

    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

    /**
     * 支付宝支付
     * <p>
     * 请求本公司服务器获取支付信息
     *
     * @param mOrderNumber
     */
    private void alipayRecharge(final String mOrderNumber) {
        final ProgressHUD mProgressHUD = ProgressHUD.show(this, "请稍候", true,
                false, null);
        OkHttpUtils
                .post()
                .url(Api.APP_PAY)
                .addParams("out_trade_no", mOrderNumber)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_CustodyBounty.this, "服务器开小差！请稍后重试");
                        Log.e("==支付宝支付获取单号：：：", e.getMessage());
                        mProgressHUD.dismiss();
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
                            IToast.show(Activity_CustodyBounty.this, mAlipayRechargeModel.getMessage());
                        }
                        mProgressHUD.dismiss();
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
                PayTask alipay = new PayTask(Activity_CustodyBounty.this);
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
     * <p>
     * 1.微信支付第一步：获取微信支付订单号
     *
     * @param mOrderNumber
     */
    private void weChatPayRecharge(final String mOrderNumber) {
        final ProgressHUD mProgressHUD = ProgressHUD.show(this, "请稍候", true,
                false, null);
        OkHttpUtils
                .post()
                .url(Api.APP_PAY_WX)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("out_trade_no", mOrderNumber)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_CustodyBounty.this, "服务器开小差！请稍后重试");
                        Log.e("==微信支付获取单号：：：", e.getMessage());
                        Log.e("==微信支付获取单号：：：", Api.APP_PAY_WX);
                        Log.e("==微信支付获取单号：：：", mOrderNumber + "");
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==微信支付获取单号：：：", response);
                        mProgressHUD.dismiss();
                        WeChatPayRechargeModel mWeChatPayRechargeModel = JSONObject.parseObject(response, WeChatPayRechargeModel.class);
                        if (RequestType.SUCCESS.equals(mWeChatPayRechargeModel.getStatus())) {
                            Intent intent = new Intent(Activity_CustodyBounty.this, WXPayEntryActivity.class);
                            intent.putExtra("appid", mWeChatPayRechargeModel.getApp_id());
                            intent.putExtra("partnerid", mWeChatPayRechargeModel.getPartner_id());
                            intent.putExtra("prepayid", mWeChatPayRechargeModel.getPrepay_id());
                            intent.putExtra("package", mWeChatPayRechargeModel.getPackage_value());
                            intent.putExtra("noncestr", mWeChatPayRechargeModel.getNonce_str());
                            intent.putExtra("timestamp", mWeChatPayRechargeModel.getTime_stamp() + "");
                            intent.putExtra("sign", mWeChatPayRechargeModel.getSign());
                            intent.putExtra("type", "0");
                            startActivity(intent);
                            addActivity(Activity_CustodyBounty.this);
                        } else if (mWeChatPayRechargeModel.getStatus().equals("0")) {
                            IToast.show(Activity_CustodyBounty.this, mWeChatPayRechargeModel.getMessage());
                        }
                    }
                });
    }

}
