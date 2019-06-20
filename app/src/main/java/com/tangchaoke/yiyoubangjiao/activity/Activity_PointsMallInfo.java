package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.stx.xhb.xbanner.XBanner;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.GuideViewPagerAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.PointsMallInfoCommentAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.BalanceModel;
import com.tangchaoke.yiyoubangjiao.model.CommodityInfoBean;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 商品详情
*/
public class Activity_PointsMallInfo extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.recycler_points_mall_info_comment)
    RecyclerView mRecyclerPointsMallInfoComment;

    PointsMallInfoCommentAdapter mPointsMallInfoCommentAdapter;

    private String mOid = "";

    @OnClick({R.id.ll_back, R.id.but_points_mall_info_redeem, R.id.but_buy, R.id.tv_view_all_comment, R.id.but_banner_info})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.but_points_mall_info_redeem:
                if (!BaseApplication.getApplication().isLogined()) {
                    Intent mIntentMine = new Intent(Activity_PointsMallInfo.this, Activity_Login.class);
                    mIntentMine.putExtra("name", "");
                    startActivity(mIntentMine);
                } else {
                    initBalance();
                }
                break;

            case R.id.but_buy:
                if (!BaseApplication.getApplication().isLogined()) {
                    Intent mIntentMine = new Intent(Activity_PointsMallInfo.this, Activity_Login.class);
                    mIntentMine.putExtra("name", "");
                    startActivity(mIntentMine);
                } else {
                    Intent mIntentRedeemOrder = new Intent(Activity_PointsMallInfo.this, Activity_RedeemOrder.class);
                    mIntentRedeemOrder.putExtra("oid", mCommodityInfoBean.getMap().getOid());
                    mIntentRedeemOrder.putExtra("type", "2");//1 积分购买 2 金钱购买
                    startActivity(mIntentRedeemOrder);
                }
                break;

            case R.id.tv_view_all_comment:
                if (mCommodityInfoBean.getMap().getCommentList().size() != 0) {
                    Intent mIntentViewAllComment = new Intent(Activity_PointsMallInfo.this, Activity_ViewAllComment.class);
                    mIntentViewAllComment.putExtra("oid", mCommodityInfoBean.getMap().getOid());
                    startActivity(mIntentViewAllComment);
                } else {
                    IToast.show(Activity_PointsMallInfo.this, "暂无评论");
                    return;
                }
                break;

            case R.id.but_banner_info:
                if (mCommodityInfoBean.getMap().getBannerList().size() != 0) {
                    BannerInfoPop();
                }
                break;

        }

    }

    private void initBalance() {
        OkHttpUtils
                .post()
                .url(Api.GET_USER_BALANCE)
                .addParams("token", BaseApplication.getApplication().getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("==获取用户余额", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==获取用户余额", response);
                        Log.e("==获取用户余额", Api.GET_USER_BALANCE);
                        Log.e("==获取用户余额", BaseApplication.getApplication().getToken());
                        BalanceModel mBalanceModel = JSONObject.parseObject(response, BalanceModel.class);
                        if (RequestType.SUCCESS.equals(mBalanceModel.getStatus())) {
                            if (!HGTool.isEmpty(mBalanceModel.getIntegral())) {
                                if (Integer.parseInt(mBalanceModel.getIntegral()) >
                                        Integer.parseInt(mCommodityInfoBean.getMap().getIntegral())) {
                                    Intent mIntentRedeemOrder = new Intent(Activity_PointsMallInfo.this, Activity_RedeemOrder.class);
                                    mIntentRedeemOrder.putExtra("oid", mCommodityInfoBean.getMap().getOid());
                                    mIntentRedeemOrder.putExtra("type", "1");//1 积分购买 2 金钱购买
                                    startActivity(mIntentRedeemOrder);
                                } else {
                                    IToast.show(Activity_PointsMallInfo.this, "账户积分不足 ！");
                                }
                            }
                        } else {
                            IToast.show(Activity_PointsMallInfo.this, mBalanceModel.getMessage());
                        }
                    }
                });
    }

    View mPopViewProblem;

    public static PopupWindow mPopupWindowProblem;

    @BindView(R.id.rl_points_mall_info)
    RelativeLayout mRlPointsMallInfo;

    /**
     * 左上角 返回 按钮
     */
    ImageView mImgBack;

    ViewPager mVpGuide;

    private GuideViewPagerAdapter mGuideViewPagerAdapter;

    String[] mImageInt;

    List<View> mImagesList = new ArrayList<>();

    private void BannerInfoPop() {
        mPopViewProblem = LayoutInflater.from(Activity_PointsMallInfo.this).inflate(R.layout.popup_window_banner_info, null);
        mImgBack = mPopViewProblem.findViewById(R.id.img_back);
        mVpGuide = mPopViewProblem.findViewById(R.id.vp_guide);
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindowProblem.dismiss();//关闭弹窗
            }
        });
        processLogic();
        mPopupWindowProblem = new PopupWindow(mPopViewProblem,
                GridLayoutManager.LayoutParams.MATCH_PARENT,
                GridLayoutManager.LayoutParams.MATCH_PARENT);
        // 设置弹出动画
        mPopupWindowProblem.setAnimationStyle(R.style.take_photo_anim);
        mPopupWindowProblem.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindowProblem.setFocusable(true);
        // 显示在根布局的底部
        mPopupWindowProblem.showAtLocation(mRlPointsMallInfo, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    private void processLogic() {
        for (String anImage : mImageInt) {
            ImageView mImageView = new ImageView(this);
            mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(Activity_PointsMallInfo.this).load(anImage).into(mImageView);
            mImagesList.add(mImageView);
        }
        mGuideViewPagerAdapter = new GuideViewPagerAdapter(mImagesList);
        mVpGuide.setAdapter(mGuideViewPagerAdapter);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_points_mall_info;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("商品详情");
    }

    @Override
    protected void initData() {
        mOid = getIntent().getStringExtra("oid");
        initCommodityInfo();
    }

    CommodityInfoBean mCommodityInfoBean;

    @BindView(R.id.tv_view_all_comment)
    TextView mTvViewAllComment;

    private void initCommodityInfo() {
        OkHttpUtils
                .post()
                .url(Api.GET_COMMODITY)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_PointsMallInfo.this, "服务器开小差！请稍后重试");
                        Log.e("==获取商品详情 ：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==获取商品详情 ：：：", response);
                        Log.e("==获取商品详情 ：：：", Api.GET_COMMODITY);
                        Log.e("==获取商品详情 ：：：", BaseApplication.getApplication().getToken());
                        Log.e("==获取商品详情 ：：：", mOid);
                        mCommodityInfoBean = JSONObject.parseObject(response, CommodityInfoBean.class);
                        if (RequestType.SUCCESS.equals(mCommodityInfoBean.getStatus())) {
                            if (mCommodityInfoBean.getMap().getBannerList().size() != 0) {
                                initBannerList(mCommodityInfoBean.getMap().getBannerList());
                                mImageInt = new String[mCommodityInfoBean.getMap().getBannerList().size()];
                                for (int i = 0; i < mCommodityInfoBean.getMap().getBannerList().size(); i++) {
                                    mImageInt[i] = Api.PATH + mCommodityInfoBean.getMap().getBannerList().get(i).getPhoto();
                                }
                            }
                            if (mCommodityInfoBean.getMap().getCommentList().size() != 0) {
                                mTvViewAllComment.setText("查看全部评论");
                                initCommentRecycler(mCommodityInfoBean.getMap().getCommentList());
                            } else {
                                mTvViewAllComment.setText("暂无评论");
                            }
                            initPointsMallInfo(mCommodityInfoBean.getMap());
                        } else {
                            IToast.show(Activity_PointsMallInfo.this, mCommodityInfoBean.getMessage());
                        }
                    }
                });
    }

    @BindView(R.id.xbanner)
    XBanner mXBanner;

    private void initBannerList(final List<CommodityInfoBean.CommodityInfoMapBean.CommodityInfoMapBannerBean> mBannerList) {
        /**
         * 第一个参数为图片资源集合
         * 第二个参数为提示文字资源集合
         *
         * 默认 轮播切换时间 5s
         */
        mXBanner.setData(mBannerList, null);
        mXBanner.setmAdapter(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                Glide.with(Activity_PointsMallInfo.this)
                        .load(Api.PATH + mBannerList.get(position).getPhoto())
                        .asBitmap()
                        .centerCrop()
                        .into((ImageView) view);
            }
        });
        mXBanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, int position) {

            }
        });
    }

    @BindView(R.id.tv_points_mall_info_name)
    TextView mTvPointsMallInfoName;

    @BindView(R.id.tv_points_mall_info_material)
    TextView mTvPointsMallInfoMaterial;

    @BindView(R.id.tv_points_mall_info_price)
    TextView mTvPointsMallInfoPrice;

    @BindView(R.id.tv_sold)
    TextView mTvSold;

    @BindView(R.id.tv_points_mall_info_quantity)
    TextView mTvPointsMallInfoQuantity;

    private void initPointsMallInfo(CommodityInfoBean.CommodityInfoMapBean mMap) {

        if (!HGTool.isEmpty(mMap.getName())) {
            mTvPointsMallInfoName.setText(mMap.getName());
        }
        if (!HGTool.isEmpty(mMap.getMaterial())) {
            mTvPointsMallInfoMaterial.setText(mMap.getMaterial());
        }
        if (!HGTool.isEmpty(mMap.getIntegral()) && !HGTool.isEmpty(mMap.getMoney())) {
            mTvPointsMallInfoPrice.setText(mMap.getIntegral() + "积分/" + mMap.getMoney() + "元");
        }
        if (!HGTool.isEmpty(mMap.getSold())) {
            mTvSold.setText("已售 " + mMap.getSold() + " 件");
        }
        if (!HGTool.isEmpty(mMap.getCreditNum())) {
            mTvPointsMallInfoQuantity.setText("商品评价（" + mMap.getCreditNum() + "）");
        }
    }

    private void initCommentRecycler(List<CommodityInfoBean.CommodityInfoMapBean.CommodityInfoMapCommentBean> mComment) {
        mRecyclerPointsMallInfoComment.setHasFixedSize(true);
        mRecyclerPointsMallInfoComment.setNestedScrollingEnabled(false);
        mRecyclerPointsMallInfoComment.setLayoutManager(new LinearLayoutManager(Activity_PointsMallInfo.this));
        mRecyclerPointsMallInfoComment.setHasFixedSize(true);
        mRecyclerPointsMallInfoComment.setItemAnimator(new DefaultItemAnimator());
        mPointsMallInfoCommentAdapter = new PointsMallInfoCommentAdapter(Activity_PointsMallInfo.this, mComment);
        mRecyclerPointsMallInfoComment.setAdapter(mPointsMallInfoCommentAdapter);
    }

}
