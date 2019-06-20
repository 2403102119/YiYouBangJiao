package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.AnswerPopChessAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.ClubCityAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.ClubCountyAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.ClubProvinceAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.CoachAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.ClubProvinceInfoModel;
import com.tangchaoke.yiyoubangjiao.model.CoachBean;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.type.SubjectType;
import com.tangchaoke.yiyoubangjiao.utils.NetWorkUsefulUtils;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 师资简介/大师简介
*/
public class Activity_Coach extends BaseActivity {

    @BindView(R.id.ll_top_activity)
    LinearLayout mLlTopActivity;

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.view_coach_line)
    View mViewCoachLine;

    /**
     * 界面：1首页互动答题 2我的学校互动答题
     */
    private String mType = "";

    /**
     * 顶部筛选条件
     */
    @BindView(R.id.ll_coach_top)
    LinearLayout mLlCoachTop;

    /**
     * 大师简介
     */
    @BindView(R.id.ll_grandmaster)
    LinearLayout mLlGrandmaster;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    CoachAdapter mCoachAdapter;

    @BindView(R.id.ll_no)
    LinearLayout mLlNo;

    @BindView(R.id.pull_to_refresh)
    PullToRefreshLayout mPullToRefresh;

    private int mPageIndex = 0;

    private int mPageNum = 10;

    /**
     * 师资简介
     */
    @BindView(R.id.ll_my_club_teacher)
    LinearLayout mLlMyClubTeacher;

    @BindView(R.id.recycler_my_club_teacher)
    RecyclerView mRecyclerMyClubTeacher;

    @BindView(R.id.ll_chess)
    LinearLayout mLlChess;

    private String mLogo = "";

    @OnClick({R.id.ll_back, R.id.ll_chess, R.id.ll_province, R.id.ll_city, R.id.ll_county})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.ll_chess:
                if (mCityPopupWindow.isShowing()) {
                    mCityPopupWindow.dismiss();
                }
                if (mCountyPopupWindow.isShowing()) {
                    mCountyPopupWindow.dismiss();
                }
                if (mProvincePopupWindow.isShowing()) {
                    mProvincePopupWindow.dismiss();
                }
                if (mChessPopupWindow.isShowing()) {
                    mChessPopupWindow.dismiss();
                    mTvChess.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color999999));
                    mImgChess.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                } else {
                    if (Build.VERSION.SDK_INT < 24) {
                        // 只有24这个版本有问题，好像是源码的问题
                        mChessPopupWindow.showAsDropDown(mViewCoachLine);
                    } else {
                        Rect rect = new Rect();
                        mViewCoachLine.getGlobalVisibleRect(rect);
                        int h = mViewCoachLine.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                        mChessPopupWindow.setHeight(h);
                        mChessPopupWindow.showAsDropDown(mViewCoachLine);
                    }
                    mTvChess.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color73D1EE));
                    mImgChess.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_un_down_arrow));
                    mTvProvince.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgProvince.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                    mTvCity.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgCity.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                    mTvCounty.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgCounty.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                }
                break;

            case R.id.ll_province:
                if (mCityPopupWindow.isShowing()) {
                    mCityPopupWindow.dismiss();
                }
                if (mCountyPopupWindow.isShowing()) {
                    mCountyPopupWindow.dismiss();
                }
                if (mChessPopupWindow.isShowing()) {
                    mChessPopupWindow.dismiss();
                }
                if (mProvincePopupWindow.isShowing()) {
                    mProvincePopupWindow.dismiss();
                    mTvProvince.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgProvince.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                } else {
                    if (Build.VERSION.SDK_INT < 24) {
                        // 只有24这个版本有问题，好像是源码的问题
                        mProvincePopupWindow.showAsDropDown(mViewCoachLine);
                    } else {
                        Rect rect = new Rect();
                        mViewCoachLine.getGlobalVisibleRect(rect);
                        int h = mViewCoachLine.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                        mProvincePopupWindow.setHeight(h);
                        mProvincePopupWindow.showAsDropDown(mViewCoachLine);
                    }
                    mTvProvince.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color73D1EE));
                    mImgProvince.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_un_down_arrow));
                    mTvCity.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgCity.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                    mTvCounty.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgCounty.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                    mTvChess.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgChess.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                }
                break;

            case R.id.ll_city:
                if (mProvincePopupWindow.isShowing()) {
                    mProvincePopupWindow.dismiss();
                }
                if (mCountyPopupWindow.isShowing()) {
                    mCountyPopupWindow.dismiss();
                }
                if (mChessPopupWindow.isShowing()) {
                    mChessPopupWindow.dismiss();
                }
                if (mCityPopupWindow.isShowing()) {
                    mCityPopupWindow.dismiss();
                    mTvCity.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgCity.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                } else {
                    if (Build.VERSION.SDK_INT < 24) {
                        // 只有24这个版本有问题，好像是源码的问题
                        mCityPopupWindow.showAsDropDown(mViewCoachLine);
                    } else {
                        Rect rect = new Rect();
                        mViewCoachLine.getGlobalVisibleRect(rect);
                        int h = mViewCoachLine.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                        mCityPopupWindow.setHeight(h);
                        mCityPopupWindow.showAsDropDown(mViewCoachLine);
                    }
                    mTvCity.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color73D1EE));
                    mImgCity.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_un_down_arrow));
                    mTvProvince.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgProvince.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                    mTvCounty.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgCounty.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                    mTvChess.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgChess.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                }
                break;

            case R.id.ll_county:
                if (mProvincePopupWindow.isShowing()) {
                    mProvincePopupWindow.dismiss();
                }
                if (mCityPopupWindow.isShowing()) {
                    mCityPopupWindow.dismiss();
                }
                if (mChessPopupWindow.isShowing()) {
                    mChessPopupWindow.dismiss();
                }
                if (mCountyPopupWindow.isShowing()) {
                    mCountyPopupWindow.dismiss();
                    mTvCounty.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgCounty.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                } else {
                    if (Build.VERSION.SDK_INT < 24) {
                        // 只有24这个版本有问题，好像是源码的问题
                        mCountyPopupWindow.showAsDropDown(mViewCoachLine);
                    } else {
                        Rect rect = new Rect();
                        mViewCoachLine.getGlobalVisibleRect(rect);
                        int h = mViewCoachLine.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                        mCountyPopupWindow.setHeight(h);
                        mCountyPopupWindow.showAsDropDown(mViewCoachLine);
                    }
                    mTvCounty.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color73D1EE));
                    mImgCounty.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_un_down_arrow));
                    mTvCity.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgCity.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                    mTvProvince.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgProvince.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                    mTvChess.setTextColor(Activity_Coach.this.getResources().getColor(R.color.color7f7f7f));
                    mImgChess.setImageDrawable(ContextCompat.getDrawable(Activity_Coach.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                }
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_coach;
    }

    @Override
    public void initTitleBar() {
        if (!HGTool.isEmpty(mType)) {
            if (mType.equals("2")) {
                mLlTopActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_top_activity_club));
                mTvTopTitle.setText("师资简介");
                mTvTopTitle.setTextColor(getResources().getColor(R.color.color333333));
            } else if (mType.equals("1")) {
                mLlTopActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_top_activity));
                mTvTopTitle.setText("大师简介");
                mTvTopTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    @BindView(R.id.ll_logo)
    LinearLayout mLlLogo;

    @BindView(R.id.img_logo)
    CircleImageView mImgLogo;

    @BindView(R.id.ll_adoption)
    LinearLayout mLlAdoption;

    @Override
    protected void initData() {
        mType = getIntent().getStringExtra("type");
        mLogo = getIntent().getStringExtra("logo");
        initPulltoRefresh();
        if (!HGTool.isEmpty(mType)) {
            if (mType.equals("2")) {
                mLlCoachTop.setVisibility(View.GONE);
                mLlGrandmaster.setVisibility(View.GONE);
                mLlMyClubTeacher.setVisibility(View.VISIBLE);
                mLlLogo.setVisibility(View.VISIBLE);
                mLlAdoption.setVisibility(View.GONE);
                initMyClubCoachList();
            } else if (mType.equals("1")) {
                mLlCoachTop.setVisibility(View.VISIBLE);
                mLlGrandmaster.setVisibility(View.VISIBLE);
                mLlMyClubTeacher.setVisibility(View.GONE);
                mLlLogo.setVisibility(View.GONE);
                mLlAdoption.setVisibility(View.INVISIBLE);
                initClubProvinceInfo();
                ChessPop();
                initGrandmaster();
            }
        }
        if (!HGTool.isEmpty(mLogo)) {
            Glide.with(Activity_Coach.this)
                    .load(Api.PATH + mLogo)
                    .into(mImgLogo);
        }
    }

    /**
     * ========================================================列表========================================================
     */
    List<CoachBean.CoachListBean> mCoachList = new ArrayList<CoachBean.CoachListBean>();

    private void initMyClubCoachList() {
        OkHttpUtils
                .post()
                .url(Api.GET_CLUB_COACH_LIST)
                .addParams("token", BaseApplication.getApplication().getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Coach.this, "服务器开小差！请稍后重试");
                        Log.e("==获取我的俱乐部教练列表 ：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==获取我的俱乐部教练列表 ：：：", response);
                        Log.e("==获取我的俱乐部教练列表 ：：：", Api.GET_CLUB_COACH_LIST);
                        Log.e("==获取我的俱乐部教练列表 ：：：", BaseApplication.getApplication().getToken());
                        CoachBean mFindTeachModel = JSONObject.parseObject(response, CoachBean.class);
                        if (RequestType.SUCCESS.equals(mFindTeachModel.getStatus())) {
                            if (mFindTeachModel.getList().size() != 0) {
                                initRecyclerMyClubCoach();
                                isShowNoData(false);
                                if (mPageIndex == 0) {
                                    mCoachAdapter.clear();
                                }
                                mCoachList.addAll(mFindTeachModel.getList());
                            } else {
                                if (mPageIndex == 0) {
                                    isShowNoData(true);
                                }
                            }
                        } else {
                            isShowNoData(true);
                            if (mFindTeachModel.getStatus().equals("0")) {
                                IToast.show(Activity_Coach.this, mFindTeachModel.getMessage());
                            }
                        }
                    }
                });
    }

    private void initRecyclerMyClubCoach() {
        mRecyclerMyClubTeacher.setHasFixedSize(true);
        mRecyclerMyClubTeacher.setNestedScrollingEnabled(false);
        mRecyclerMyClubTeacher.setLayoutManager(new LinearLayoutManager(Activity_Coach.this));
        mRecyclerMyClubTeacher.setHasFixedSize(true);
        mRecyclerMyClubTeacher.setItemAnimator(new DefaultItemAnimator());
        mCoachAdapter = new CoachAdapter(Activity_Coach.this, mType, mCoachList, new CoachAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {

            }
        });
        mRecyclerMyClubTeacher.setAdapter(mCoachAdapter);
    }

    private String mProvinceOid = "";

    private String mCityOid = "";

    private String mAreaOid = "";

    private String mChessType3 = "";

    private void initGrandmaster() {

        if (mChessPosC == -1 || mChessPosC == 0) {
            mChessType3 = "全部";
        } else {
            mChessType3 = String.valueOf(mChessPosC);
        }

        OkHttpUtils
                .post()
                .url(Api.GET_COACH_LIST)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("page.index", mPageIndex + "")
                .addParams("page.num", mPageNum + "")
                .addParams("type", mChessType3)
                .addParams("province", mProvinceOid)
                .addParams("city", mCityOid)
                .addParams("area", mAreaOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Coach.this, "服务器开小差！请稍后重试");
                        Log.e("====获取教练列表 ：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====获取教练列表 ：：：", response);
                        Log.e("====获取教练列表 ：：：", Api.GET_COACH_LIST);
                        Log.e("====获取教练列表 ：：：", BaseApplication.getApplication().getToken());
                        Log.e("====获取教练列表 ：：：", mPageIndex + "");
                        Log.e("====获取教练列表 ：：：", mPageNum + "");
                        Log.e("====获取教练列表 ：：：", mChessType3);
                        Log.e("====获取教练列表 ：：：", mProvinceOid);
                        Log.e("====获取教练列表 ：：：", mCityOid);
                        Log.e("====获取教练列表 ：：：", mAreaOid);
                        CoachBean mCoachBean = JSONObject.parseObject(response, CoachBean.class);
                        if (RequestType.SUCCESS.equals(mCoachBean.getStatus())) {
                            if (mCoachBean.getList().size() != 0) {
                                initRecyclerGrandmaster();
                                isShowNoData(false);
                                if (mPageIndex == 0) {
                                    mCoachAdapter.clear();
                                }
                                mCoachList.addAll(mCoachBean.getList());
                            } else {
                                if (mPageIndex == 0) {
                                    isShowNoData(true);
                                }
                            }
                        } else {
                            isShowNoData(true);
                            if (mCoachBean.getStatus().equals("0")) {
                                IToast.show(Activity_Coach.this, mCoachBean.getMessage());
                            }
                        }
                    }
                });
    }

    private void initRecyclerGrandmaster() {
        mRecycler.setHasFixedSize(true);
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(Activity_Coach.this));
        mRecycler.setHasFixedSize(true);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mCoachAdapter = new CoachAdapter(Activity_Coach.this, mType, mCoachList, new CoachAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent mIntentFindTeachInfo = new Intent(Activity_Coach.this, Activity_CoachInfo.class);
                mIntentFindTeachInfo.putExtra("oid", mCoachList.get(position).getOid());
                startActivity(mIntentFindTeachInfo);
            }
        });
        mRecycler.setAdapter(mCoachAdapter);
    }

    /**
     * 没有数据显示
     *
     * @param show
     */
    public void isShowNoData(boolean show) {
        if (show) {
            mLlNo.setVisibility(View.VISIBLE);
            mRecycler.setVisibility(View.GONE);
        } else {
            mLlNo.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ========================================================选择 省 市 区========================================================
     */
    ClubProvinceInfoModel mClubProvinceInfoModel;

    private void initClubProvinceInfo() {
        OkHttpUtils
                .post()
                .url(Api.GET_AREALIST)
                .addParams("token", BaseApplication.getApplication().getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("====省市区：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====省市区：：：", Api.GET_AREALIST);
                        Log.e("====省市区：：：", response);
                        mClubProvinceInfoModel = JSONObject.parseObject(response, ClubProvinceInfoModel.class);
                        ProvincePop();
                    }
                });
    }

    PopupWindow mCountyPopupWindow;

    RecyclerView mRecyclerClubCounty;

    TextView mTvClubAllCounty;

    @BindView(R.id.tv_county)
    TextView mTvCounty;

    @BindView(R.id.img_county)
    ImageView mImgCounty;

    private void CountyPop() {
        View mPopView = LayoutInflater.from(Activity_Coach.this).inflate(R.layout.popup_window_club_province, null);
        mRecyclerClubCounty = mPopView.findViewById(R.id.recycler_club_province);
        mTvClubAllCounty = mPopView.findViewById(R.id.tv_club_all);
        mTvClubAllCounty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvClubAllCounty.setTextColor(getResources().getColor(R.color.color73D1EE));
                mTvCounty.setText("所在区");
                mHighCountyPos = -1;
                mAreaOid = "";
                if (mHighGradePos != -1 && mHighCityPos != -1) {
                    initRecyclerClubCounty();
                }
                initGrandmaster();
                mCountyPopupWindow.dismiss();
            }
        });
        if (mHighGradePos != -1 && mHighCityPos != -1) {
            initRecyclerClubCounty();
        }
        mCountyPopupWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mCountyPopupWindow.setAnimationStyle(R.style.style_pop_animation);
        mCountyPopupWindow.setFocusable(false);
        mCountyPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    ClubCountyAdapter mClubCountyAdapter;

    /**
     * 县 选中 标识
     */
    private int mHighCountyPos = -1;//保存当前选中的position 重点!

    private void initRecyclerClubCounty() {
        mRecyclerClubCounty.setHasFixedSize(true);
        mRecyclerClubCounty.setNestedScrollingEnabled(false);
        mRecyclerClubCounty.setLayoutManager(new LinearLayoutManager(Activity_Coach.this));
        mRecyclerClubCounty.setHasFixedSize(true);
        mRecyclerClubCounty.setItemAnimator(new DefaultItemAnimator());
        mClubCountyAdapter = new ClubCountyAdapter(mHighGradePos, mHighCityPos, mHighCountyPos, Activity_Coach.this,
                mClubProvinceInfoModel.getAreaList().get(mHighGradePos).getCity().get(mHighCityPos).getCity(),
                new ClubCountyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                        TextView mTvReleaseGrade = view.findViewById(R.id.tv_club_name);
                        if (mHighCountyPos != position) {//当前选中的position和上次选中不是同一个position 执行
                            mLlItem.setBackgroundResource(R.color.colorPrimary);
                            mTvReleaseGrade.setTextColor(getResources().getColor(R.color.color73D1EE));
                            mTvClubAllCounty.setTextColor(getResources().getColor(R.color.color666666));
                            if (mHighCountyPos != -1) {//判断是否有效 -1是初始值 即无效 第二个参数是Object 随便传个int 这里只是起个标志位
                                mClubCountyAdapter.notifyItemChanged(mHighCountyPos, 0);
                            }
                            mHighCountyPos = position;
                        }
                        mCountyPopupWindow.dismiss();
                        mTvCounty.setText(mClubProvinceInfoModel.getAreaList().get(mHighGradePos)
                                .getCity().get(mHighCityPos)
                                .getCity().get(position).getName());
                        mAreaOid = mClubProvinceInfoModel.getAreaList().get(mHighGradePos)
                                .getCity().get(mHighCityPos)
                                .getCity().get(position).getOid();
                        initGrandmaster();
                    }
                });
        mRecyclerClubCounty.setAdapter(mClubCountyAdapter);
    }

    PopupWindow mCityPopupWindow;

    RecyclerView mRecyclerClubCity;

    TextView mTvClubAllCity;

    @BindView(R.id.tv_city)
    TextView mTvCity;

    @BindView(R.id.img_city)
    ImageView mImgCity;

    private void CityPop() {
        View mPopView = LayoutInflater.from(Activity_Coach.this).inflate(R.layout.popup_window_club_province, null);
        mRecyclerClubCity = mPopView.findViewById(R.id.recycler_club_province);
        mTvClubAllCity = mPopView.findViewById(R.id.tv_club_all);
        mTvClubAllCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvClubAllCity.setTextColor(getResources().getColor(R.color.color73D1EE));
                mTvClubAllCounty.setTextColor(getResources().getColor(R.color.color666666));
                mTvCity.setText("所在城市");
                mTvCounty.setText("所在区");
                mHighCityPos = -1;
                mHighCountyPos = -1;
                mCityOid = "";
                mAreaOid = "";
                CountyPop();
                if (mClubCityAdapter != null) {
                    mClubCityAdapter.notifyDataSetChanged();
                }
                initGrandmaster();
                mCityPopupWindow.dismiss();
            }
        });
        CountyPop();
        if (mHighGradePos != -1) {
            initRecyclerClubCity();
        }
        mCityPopupWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mCityPopupWindow.setAnimationStyle(R.style.style_pop_animation);
        mCityPopupWindow.setFocusable(false);
        mCityPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    ClubCityAdapter mClubCityAdapter;

    /**
     * 市区 选中 标识
     */
    private int mHighCityPos = -1;//保存当前选中的position 重点!

    private void initRecyclerClubCity() {
        mRecyclerClubCity.setHasFixedSize(true);
        mRecyclerClubCity.setNestedScrollingEnabled(false);
        mRecyclerClubCity.setLayoutManager(new LinearLayoutManager(Activity_Coach.this));
        mRecyclerClubCity.setHasFixedSize(true);
        mRecyclerClubCity.setItemAnimator(new DefaultItemAnimator());
        mClubCityAdapter = new ClubCityAdapter(mHighGradePos, mHighCityPos, Activity_Coach.this,
                mClubProvinceInfoModel.getAreaList().get(mHighGradePos).getCity(),
                new ClubCityAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                        TextView mTvReleaseGrade = view.findViewById(R.id.tv_club_name);
                        if (mHighCityPos != position) {//当前选中的position和上次选中不是同一个position 执行
                            mLlItem.setBackgroundResource(R.color.colorPrimary);
                            mTvReleaseGrade.setTextColor(getResources().getColor(R.color.color73D1EE));
                            mTvClubAllCity.setTextColor(getResources().getColor(R.color.color666666));
                            if (mHighCityPos != -1) {//判断是否有效 -1是初始值 即无效 第二个参数是Object 随便传个int 这里只是起个标志位
                                mClubCityAdapter.notifyItemChanged(mHighCityPos, 0);
                            }
                            mHighCityPos = position;
                        }
                        mCityPopupWindow.dismiss();
                        mTvCity.setText(mClubProvinceInfoModel.getAreaList().get(mHighGradePos)
                                .getCity().get(position).getName());
                        mCityOid = mClubProvinceInfoModel.getAreaList().get(mHighGradePos)
                                .getCity().get(position).getOid();
                        initGrandmaster();
                        mTvCounty.setText("所在区");
                        mHighCountyPos = -1;
                        initRecyclerClubCounty();
                    }
                });
        mRecyclerClubCity.setAdapter(mClubCityAdapter);
    }

    PopupWindow mProvincePopupWindow;

    RecyclerView mRecyclerClubProvince;

    TextView mTvClubAllProvince;

    @BindView(R.id.tv_province)
    TextView mTvProvince;

    @BindView(R.id.img_province)
    ImageView mImgProvince;

    private void ProvincePop() {
        View mPopView = LayoutInflater.from(Activity_Coach.this).inflate(R.layout.popup_window_club_province, null);
        mRecyclerClubProvince = mPopView.findViewById(R.id.recycler_club_province);
        mTvClubAllProvince = mPopView.findViewById(R.id.tv_club_all);
        mTvClubAllProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvClubAllProvince.setTextColor(getResources().getColor(R.color.color73D1EE));
                mTvClubAllCity.setTextColor(getResources().getColor(R.color.color666666));
                mTvClubAllCounty.setTextColor(getResources().getColor(R.color.color666666));
                mTvProvince.setText("所在省份");
                mTvCity.setText("所在城市");
                mTvCounty.setText("所在区");
                mHighGradePos = -1;
                mHighCityPos = -1;
                mHighCountyPos = -1;
                mProvinceOid = "";
                mCityOid = "";
                mAreaOid = "";
                if (mClubProvinceAdapter != null) {
                    mClubProvinceAdapter.notifyDataSetChanged();
                }
                CityPop();
                CountyPop();
                initGrandmaster();
                mProvincePopupWindow.dismiss();
            }
        });
        CityPop();
        CountyPop();
        initRecyclerClubProvince();
        mProvincePopupWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mProvincePopupWindow.setAnimationStyle(R.style.style_pop_animation);
        mProvincePopupWindow.setFocusable(false);
        mProvincePopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    ClubProvinceAdapter mClubProvinceAdapter;

    /**
     * 省份 选中 标识
     */
    private int mHighGradePos = -1;//保存当前选中的position 重点!

    private void initRecyclerClubProvince() {
        mRecyclerClubProvince.setHasFixedSize(true);
        mRecyclerClubProvince.setNestedScrollingEnabled(false);
        mRecyclerClubProvince.setLayoutManager(new LinearLayoutManager(Activity_Coach.this));
        mRecyclerClubProvince.setHasFixedSize(true);
        mRecyclerClubProvince.setItemAnimator(new DefaultItemAnimator());
        mClubProvinceAdapter = new ClubProvinceAdapter(mHighGradePos, Activity_Coach.this, mClubProvinceInfoModel.getAreaList(),
                new ClubProvinceAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                        TextView mTvReleaseGrade = view.findViewById(R.id.tv_club_name);
                        if (mHighGradePos != position) {//当前选中的position和上次选中不是同一个position 执行
                            mLlItem.setBackgroundResource(R.color.colorPrimary);
                            mTvReleaseGrade.setTextColor(getResources().getColor(R.color.color73D1EE));
                            mTvClubAllProvince.setTextColor(getResources().getColor(R.color.color666666));
                            mTvClubAllCity.setTextColor(getResources().getColor(R.color.color666666));
                            mTvClubAllCounty.setTextColor(getResources().getColor(R.color.color666666));
                            if (mHighGradePos != -1) {//判断是否有效 -1是初始值 即无效 第二个参数是Object 随便传个int 这里只是起个标志位
                                mClubProvinceAdapter.notifyItemChanged(mHighGradePos, 0);
                            }
                            mHighGradePos = position;
                        }
                        mProvincePopupWindow.dismiss();
                        mTvProvince.setText(mClubProvinceInfoModel.getAreaList().get(position).getName());
                        mProvinceOid = mClubProvinceInfoModel.getAreaList().get(position).getOid();
                        initGrandmaster();
                        mTvCity.setText("所在城市");
                        mTvCounty.setText("所在区");
                        mHighCityPos = -1;
                        mHighCountyPos = -1;
                        initRecyclerClubCity();
                    }
                });
        mRecyclerClubProvince.setAdapter(mClubProvinceAdapter);
    }

    /**
     * ========================================================选择棋种========================================================
     */
    PopupWindow mChessPopupWindow;

    RecyclerView mRecyclerChess;

    AnswerPopChessAdapter mAnswerPopChessAdapter;

    private void ChessPop() {
        View mPopView = LayoutInflater.from(Activity_Coach.this).inflate(R.layout.popup_window_find_teacher_subject, null);
        mRecyclerChess = mPopView.findViewById(R.id.recycler_find_teacher_subject);
        initRecyclerChess(mRecyclerChess);
        mChessPopupWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mChessPopupWindow.setAnimationStyle(R.style.style_pop_animation);
        mChessPopupWindow.setFocusable(false);
        mChessPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    /**
     * 棋种 选中 标识
     */
    private int mChessPos = -1;//保存当前选中的position 重点!

    private int mChessPosC = -1;//保存当前选中的position 重点!

    String mSelectedChess = "";

    @BindView(R.id.tv_chess)
    TextView mTvChess;

    @BindView(R.id.img_chess)
    ImageView mImgChess;

    private void initRecyclerChess(RecyclerView mRecyclerChess) {
        mRecyclerChess.setHasFixedSize(true);
        mRecyclerChess.setNestedScrollingEnabled(false);
        mRecyclerChess.setLayoutManager(new GridLayoutManager(Activity_Coach.this, 4));
        mRecyclerChess.setHasFixedSize(true);
        mRecyclerChess.setItemAnimator(new DefaultItemAnimator());
        mAnswerPopChessAdapter = new AnswerPopChessAdapter(mChessPos, Activity_Coach.this,
                SubjectType.mChessList, new AnswerPopChessAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                TextView mTvReleaseGrade = view.findViewById(R.id.tv_release_grade);
                if (mChessPos != position) {
                    //当前选中的position和上次选中不是同一个position 执行
                    mLlItem.setBackgroundResource(R.drawable.shape_light_blue_fillet_square);
                    mTvReleaseGrade.setTextColor(getResources().getColor(R.color.colorPrimary));
                    if (mChessPos != -1) {//判断是否有效 -1是初始值 即无效 第二个参数是Object 随便传个int 这里只是起个标志位
                        mAnswerPopChessAdapter.notifyItemChanged(mChessPos, 0);
                    }
                    mChessPos = position;
                    mSelectedChess = SubjectType.mChessList.get(mChessPos);
                    mChessPosC = mChessPos;
                    mTvChess.setText(mSelectedChess);
                    mChessPopupWindow.dismiss();
                    initGrandmaster();
                }
            }
        });
        mRecyclerChess.setAdapter(mAnswerPopChessAdapter);
    }

    private void initPulltoRefresh() {
        mPullToRefresh.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                if (NetWorkUsefulUtils.getActiveNetwork(Activity_Coach.this)) {
                    mPageIndex = 0;
                    mPageNum = 10;
                    initGrandmaster();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    IToast.show(Activity_Coach.this, "" + getResources().getString(R.string.netNotUseful));
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                mPageIndex = mPageIndex + 10;
                mPageNum = 10;
                initGrandmaster();
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
    }

}