package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.stx.xhb.xbanner.XBanner;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.ClubInfoModel;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.InputBoxDialogView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 棋类机构详情
*/
public class Activity_ClubInfo extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    private String mOid = "";

    @BindView(R.id.but_advisory)
    Button mButAdvisory;

    @OnClick({R.id.ll_back, R.id.ll_club_info_address, R.id.but_advisory})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.ll_club_info_address:
                Intent mIntentClubAddress = new Intent(Activity_ClubInfo.this, Activity_ClubInfoAddress.class);
                mIntentClubAddress.putExtra("type", "1");
                mIntentClubAddress.putExtra("latitude", mClubInfoModel.getModel().getLatitude());
                mIntentClubAddress.putExtra("longitude", mClubInfoModel.getModel().getLongitude());
                startActivity(mIntentClubAddress);
                break;

            case R.id.but_advisory:
                initContactUsDialog();
                break;

        }

    }

    /**
     * 拨打电话Dialog
     */
    private void initContactUsDialog() {
        final InputBoxDialogView mInputBoxDialogView = new InputBoxDialogView(Activity_ClubInfo.this);
        mInputBoxDialogView.setContentInputBox("填写咨询报名信息");
        mInputBoxDialogView.setHint("请输入手机号");
        mInputBoxDialogView.setYes("提交");
        mInputBoxDialogView.setCustomOnClickListener(new InputBoxDialogView.OnCustomDialogListener() {
            @Override
            public void setYesOnclick() {
                String mPhone = mInputBoxDialogView.getText();
                initConsultingClub(mPhone, mInputBoxDialogView);
            }

            @Override
            public void setNoOnclick() {
                mInputBoxDialogView.dismiss();
            }
        });
        mInputBoxDialogView.setCancelable(false);
        mInputBoxDialogView.show();
    }

    private void initConsultingClub(final String mPhone, final InputBoxDialogView mInputBoxDialogView) {
        OkHttpUtils
                .post()
                .url(Api.CONSULTING_CLUB)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("phone", mPhone)
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_ClubInfo.this, "服务器开小差 请稍后再试 ！ ");
                        Log.e("==咨询俱乐部:::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==咨询俱乐部:::", response);
                        Log.e("==咨询俱乐部:::", Api.CONSULTING_CLUB);
                        Log.e("==咨询俱乐部:::", BaseApplication.getApplication().getToken());
                        Log.e("==咨询俱乐部:::", mPhone + "");
                        Log.e("==咨询俱乐部:::", mOid + "");
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            mButAdvisory.setText("请稍等");
                            mInputBoxDialogView.dismiss();
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {

                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(),
                                        Activity_ClubInfo.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_ClubInfo.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_ClubInfo.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_club_info;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("棋类机构详情");
    }

    @Override
    protected void initData() {
        mOid = getIntent().getStringExtra("oid");
        initClubInfo();
    }

    ClubInfoModel mClubInfoModel;

    private void initClubInfo() {
        OkHttpUtils
                .post()
                .url(Api.GET_CLUB_DATA)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_ClubInfo.this, "服务器开小差 请稍后再试 ！ ");
                        Log.e("====俱乐部详情:::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====俱乐部详情:::", response);
                        Log.e("====俱乐部详情:::", Api.GET_CLUB_DATA);
                        Log.e("====俱乐部详情:::", BaseApplication.getApplication().getToken());
                        Log.e("====俱乐部详情:::", mOid + "");
                        mClubInfoModel = JSONObject.parseObject(response, ClubInfoModel.class);
                        if (RequestType.SUCCESS.equals(mClubInfoModel.getStatus())) {
                            initClubInfoDisplayData(mClubInfoModel.getModel());
                        } else {
                            if (mClubInfoModel.getStatus().equals("0")) {
                                IToast.show(Activity_ClubInfo.this, mClubInfoModel.getMessage());
                            }
                        }
                    }
                });
    }

    @BindView(R.id.xbanner_club_info)
    XBanner mXBannerClubInfo;

    @BindView(R.id.tv_club_info_name)
    TextView mTvClubInfoName;

    @BindView(R.id.img_club_info_star)
    ImageView mImgClubInfoStar;

    @BindView(R.id.tv_club_info_businessHours)
    TextView mTvClubInfoBusinessHours;

    @BindView(R.id.tv_club_info_address)
    TextView mTvClubInfoAddress;

    @BindView(R.id.tv_club_info_briefIntroduction)
    TextView mTvClubInfoBriefIntroduction;

    /**
     * 显示 俱乐部 详情
     *
     * @param model
     */
    private void initClubInfoDisplayData(ClubInfoModel.ClubInfoBean model) {

        if (model.getBannerList().size() != 0) {
            initBanner(model.getBannerList());
        }

        if (!HGTool.isEmpty(model.getName())) {
            mTvClubInfoName.setText(model.getName());
        }

        if (!HGTool.isEmpty(model.getStar())) {
            if (model.getStar().equals("1")) {
                Glide.with(Activity_ClubInfo.this).load(R.drawable.ic_club_gold_medal).into(mImgClubInfoStar);
            } else if (model.getStar().equals("2")) {
                Glide.with(Activity_ClubInfo.this).load(R.drawable.ic_club_silver_medal).into(mImgClubInfoStar);
            } else if (model.getStar().equals("3")) {
                Glide.with(Activity_ClubInfo.this).load(R.drawable.ic_club_bronze_medal).into(mImgClubInfoStar);
            }
        }

        if (!HGTool.isEmpty(model.getBusinessHours())) {
            mTvClubInfoBusinessHours.setText(model.getBusinessHours());
        }

        if (!HGTool.isEmpty(model.getAddress())) {
            mTvClubInfoAddress.setText(model.getAddress());
        }

        if (!HGTool.isEmpty(model.getBriefIntroduction())) {
            mTvClubInfoBriefIntroduction.setText(model.getBriefIntroduction());
        }

    }

    /**
     * 俱乐部轮播如
     *
     * @param bannerList
     */
    private void initBanner(final List<ClubInfoModel.ClubInfoBean.ClubInfoBanner> bannerList) {
        /**
         * 第一个参数为图片资源集合
         * 第二个参数为提示文字资源集合
         *
         * 默认 轮播切换时间 5s
         */
        mXBannerClubInfo.setData(bannerList, null);
        mXBannerClubInfo.setmAdapter(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                Glide.with(Activity_ClubInfo.this)
                        .load(Api.PATH + bannerList.get(position).getBannerPhoto())
                        .asBitmap().centerCrop()
                        .into((ImageView) view);
            }
        });
    }

}
