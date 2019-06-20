package com.tangchaoke.yiyoubangjiao.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.activity.Activity_ClubAddress;
import com.tangchaoke.yiyoubangjiao.activity.Activity_ClubInfo;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Main;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Search;
import com.tangchaoke.yiyoubangjiao.adapter.ClubAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.ClubCityAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.ClubCountyAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.ClubProvinceAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.ClubModel;
import com.tangchaoke.yiyoubangjiao.model.ClubProvinceInfoModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.NetWorkUsefulUtils;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 俱乐部
 */
public class Fragment_Club extends Fragment {

    View view;

    @BindView(R.id.ll_top_right_nearby)
    LinearLayout ll_top_right_nearby;

    @BindView(R.id.ll_top_right)
    LinearLayout ll_top_right;

    /**
     * 上拉加载 下拉刷新
     */
    @BindView(R.id.pull_to_refresh)
    PullToRefreshLayout mPullToRefresh;

    @BindView(R.id.recycler_club)
    RecyclerView mRecyclerClub;

    ClubAdapter mClubAdapter;

    @BindView(R.id.ll_no_problem)
    LinearLayout mLlNoProblem;

    private int mPageIndex = 0;

    private int mPageNum = 10;

    @BindView(R.id.view_club)
    View mViewClub;

    @BindView(R.id.tv_province)
    TextView mTvProvince;

    @BindView(R.id.img_province)
    ImageView mImgProvince;

    @BindView(R.id.tv_city)
    TextView mTvCity;

    @BindView(R.id.img_city)
    ImageView mImgCity;

    @BindView(R.id.tv_county)
    TextView mTvCounty;

    @BindView(R.id.img_county)
    ImageView mImgCounty;

    @BindView(R.id.edit_search)
    EditText mEditSearch;

    public static Fragment_Club newInstance() {
        Fragment_Club fragment = new Fragment_Club();
        return fragment;
    }

    @OnClick({R.id.ll_top_right_nearby, R.id.ll_province, R.id.ll_city, R.id.ll_county,
            R.id.edit_search, R.id.ll_top_search})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_top_right_nearby:
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    //没有权限，向系统申请该权限
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 103);
                } else {
                    Intent mIntentClubAddress = new Intent(getActivity(), Activity_ClubAddress.class);
                    mIntentClubAddress.putExtra("type", "2");
                    startActivity(mIntentClubAddress);
                }
                break;

            case R.id.edit_search:
            case R.id.ll_top_search:
                Intent mIntentSearch = new Intent(getActivity(), Activity_Search.class);
                startActivityForResult(mIntentSearch, 200);
                break;

            case R.id.ll_province:
                if (mCityPopupWindow.isShowing()) {
                    mCityPopupWindow.dismiss();
                }
                if (mCountyPopupWindow.isShowing()) {
                    mCountyPopupWindow.dismiss();
                }
                if (mProvincePopupWindow.isShowing()) {
                    mProvincePopupWindow.dismiss();
                    mTvProvince.setTextColor(getActivity().getResources().getColor(R.color.color7f7f7f));
                    mImgProvince.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_clup_arrow));
                } else {
                    if (Build.VERSION.SDK_INT < 24) {
                        // 只有24这个版本有问题，好像是源码的问题
                        mProvincePopupWindow.showAsDropDown(mViewClub);
                    } else {
                        Rect rect = new Rect();
                        mViewClub.getGlobalVisibleRect(rect);
                        int h = mViewClub.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                        mProvincePopupWindow.setHeight(h);
                        mProvincePopupWindow.showAsDropDown(mViewClub);
                    }
                    mTvProvince.setTextColor(getActivity().getResources().getColor(R.color.color73D1EE));
                    mImgProvince.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_un_down_arrow));
                    mTvCity.setTextColor(getActivity().getResources().getColor(R.color.color7f7f7f));
                    mImgCity.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_clup_arrow));
                    mTvCounty.setTextColor(getActivity().getResources().getColor(R.color.color7f7f7f));
                    mImgCounty.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_clup_arrow));
                }
                break;

            case R.id.ll_city:
                if (mProvincePopupWindow.isShowing()) {
                    mProvincePopupWindow.dismiss();
                }
                if (mCountyPopupWindow.isShowing()) {
                    mCountyPopupWindow.dismiss();
                }
                if (mCityPopupWindow.isShowing()) {
                    mCityPopupWindow.dismiss();
                    mTvCity.setTextColor(getActivity().getResources().getColor(R.color.color7f7f7f));
                    mImgCity.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_clup_arrow));
                } else {
                    if (Build.VERSION.SDK_INT < 24) {
                        // 只有24这个版本有问题，好像是源码的问题
                        mCityPopupWindow.showAsDropDown(mViewClub);
                    } else {
                        Rect rect = new Rect();
                        mViewClub.getGlobalVisibleRect(rect);
                        int h = mViewClub.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                        mCityPopupWindow.setHeight(h);
                        mCityPopupWindow.showAsDropDown(mViewClub);
                    }
                    mTvCity.setTextColor(getActivity().getResources().getColor(R.color.color73D1EE));
                    mImgCity.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_un_down_arrow));
                    mTvProvince.setTextColor(getActivity().getResources().getColor(R.color.color7f7f7f));
                    mImgProvince.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_clup_arrow));
                    mTvCounty.setTextColor(getActivity().getResources().getColor(R.color.color7f7f7f));
                    mImgCounty.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_clup_arrow));
                }
                break;

            case R.id.ll_county:
                if (mProvincePopupWindow.isShowing()) {
                    mProvincePopupWindow.dismiss();
                }
                if (mCityPopupWindow.isShowing()) {
                    mCityPopupWindow.dismiss();
                }
                if (mCountyPopupWindow.isShowing()) {
                    mCountyPopupWindow.dismiss();
                    mTvCounty.setTextColor(getActivity().getResources().getColor(R.color.color7f7f7f));
                    mImgCounty.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_clup_arrow));
                } else {
                    if (Build.VERSION.SDK_INT < 24) {
                        // 只有24这个版本有问题，好像是源码的问题
                        mCountyPopupWindow.showAsDropDown(mViewClub);
                    } else {
                        Rect rect = new Rect();
                        mViewClub.getGlobalVisibleRect(rect);
                        int h = mViewClub.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                        mCountyPopupWindow.setHeight(h);
                        mCountyPopupWindow.showAsDropDown(mViewClub);
                    }
                    mTvCounty.setTextColor(getActivity().getResources().getColor(R.color.color73D1EE));
                    mImgCounty.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_un_down_arrow));
                    mTvCity.setTextColor(getActivity().getResources().getColor(R.color.color7f7f7f));
                    mImgCity.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_clup_arrow));
                    mTvProvince.setTextColor(getActivity().getResources().getColor(R.color.color7f7f7f));
                    mImgProvince.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_clup_arrow));
                }
                break;

        }

    }

    /**
     * 首页点击 服务
     */
    NetBroadCastFindteacher mNetBroadCastFindteacher;

    Activity_Main mActivityMain = new Activity_Main();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_club, container, false);
        ButterKnife.bind(this, view);
        mActivityMain = (Activity_Main) getActivity();
        ll_top_right_nearby.setVisibility(View.VISIBLE);
        ll_top_right.setVisibility(View.GONE);
        mEditSearch.setFocusable(false);//让EditText失去焦点，然后获取点击事件

        /**
         * 注册广播
         */
        mNetBroadCastFindteacher = new NetBroadCastFindteacher();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.tangchaoke.yiyoubangjiao.fragment.club");
        getActivity().registerReceiver(mNetBroadCastFindteacher, mIntentFilter);

        initPulltoRefresh();
        initReceiver();
        initClubData();
        initClubProvinceInfo();
        return view;
    }

    /**
     * 从首页点击跳转
     */
    class NetBroadCastFindteacher extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initReceiver();
        }
    }

    private void initReceiver() {
        mSearchText = mActivityMain.getSearch();
        if (!HGTool.isEmpty(mSearchText)) {
            mEditSearch.setText(mSearchText);
        } else {
            mEditSearch.setText("");
        }
        initClubData();
    }

    private String mSearchText = "";
    private String mProvinceOid = "";
    private String mCityOid = "";
    private String mAreaOid = "";

    List<ClubModel.ClubListModel> mClubList = new ArrayList<ClubModel.ClubListModel>();

    private void initClubData() {
        OkHttpUtils
                .post()
                .url(Api.GET_CLUB_LIST)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("page.index", mPageIndex + "")
                .addParams("page.num", mPageNum + "")
                .addParams("searchText", mSearchText)
                .addParams("province", mProvinceOid)
                .addParams("city", mCityOid)
                .addParams("area", mAreaOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(getActivity(), "服务器开小差 请稍后再试 ！ ");
                        Log.e("====获取俱乐部列表:::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====获取俱乐部列表:::", response);
                        Log.e("====获取俱乐部列表:::", Api.GET_CLUB_LIST);
                        Log.e("====获取俱乐部列表:::", BaseApplication.getApplication().getToken());
                        Log.e("====获取俱乐部列表:::", mPageIndex + "");
                        Log.e("====获取俱乐部列表:::", mPageNum + "");
                        Log.e("====获取俱乐部列表:::", mSearchText + "");
                        Log.e("====获取俱乐部列表:::", mProvinceOid + "");
                        Log.e("====获取俱乐部列表:::", mCityOid + "");
                        Log.e("====获取俱乐部列表:::", mAreaOid + "");
                        ClubModel mClubModel = JSONObject.parseObject(response, ClubModel.class);
                        if (RequestType.SUCCESS.equals(mClubModel.getStatus())) {
                            if (mClubModel.getList().size() != 0) {
                                initRecyclerClub();
                                isShowNoData(false);
                                if (mPageIndex == 0) {
                                    mClubAdapter.clear();
                                }
                                mClubList.addAll(mClubModel.getList());
                            } else {
                                if (mPageIndex == 0) {
                                    isShowNoData(true);
                                }
                            }
                        } else {
                            if (mClubModel.getStatus().equals("0")) {
                                IToast.show(getActivity(), mClubModel.getMessage());
                            }
                        }
                    }
                });
    }

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

    private void CountyPop() {
        View mPopView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window_club_province, null);
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
                initClubData();
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
        mRecyclerClubCounty.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerClubCounty.setHasFixedSize(true);
        mRecyclerClubCounty.setItemAnimator(new DefaultItemAnimator());
        mClubCountyAdapter = new ClubCountyAdapter(mHighGradePos, mHighCityPos, mHighCountyPos, getActivity(),
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
                        initClubData();
                    }
                });
        mRecyclerClubCounty.setAdapter(mClubCountyAdapter);
    }

    PopupWindow mCityPopupWindow;
    RecyclerView mRecyclerClubCity;
    TextView mTvClubAllCity;

    private void CityPop() {
        View mPopView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window_club_province, null);
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
                initClubData();
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
        mRecyclerClubCity.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerClubCity.setHasFixedSize(true);
        mRecyclerClubCity.setItemAnimator(new DefaultItemAnimator());
        mClubCityAdapter = new ClubCityAdapter(mHighGradePos, mHighCityPos, getActivity(),
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
                        initClubData();
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

    private void ProvincePop() {
        View mPopView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window_club_province, null);
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
                initClubData();
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
        mRecyclerClubProvince.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerClubProvince.setHasFixedSize(true);
        mRecyclerClubProvince.setItemAnimator(new DefaultItemAnimator());
        mClubProvinceAdapter = new ClubProvinceAdapter(mHighGradePos, getActivity(), mClubProvinceInfoModel.getAreaList(),
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
                        initClubData();
                        mTvCity.setText("所在城市");
                        mTvCounty.setText("所在区");
                        mHighCityPos = -1;
                        mHighCountyPos = -1;
                        initRecyclerClubCity();
                    }
                });
        mRecyclerClubProvince.setAdapter(mClubProvinceAdapter);
    }

    private void initRecyclerClub() {
        mRecyclerClub.setHasFixedSize(true);
        mRecyclerClub.setNestedScrollingEnabled(false);
        mRecyclerClub.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerClub.setHasFixedSize(true);
        mRecyclerClub.setItemAnimator(new DefaultItemAnimator());
        mClubAdapter = new ClubAdapter(getActivity(), mClubList, new ClubAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent mIntentClubInfo = new Intent(getActivity(), Activity_ClubInfo.class);
                mIntentClubInfo.putExtra("oid", mClubList.get(position).getOid());
                startActivity(mIntentClubInfo);
            }
        });
        mRecyclerClub.setAdapter(mClubAdapter);
    }

    /**
     * 刷新
     */
    private void initPulltoRefresh() {
        mPullToRefresh.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                if (NetWorkUsefulUtils.getActiveNetwork(getActivity())) {
                    mPageIndex = 0;
                    mPageNum = 10;
                    initClubData();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    IToast.show(getActivity(), "" + getResources().getString(R.string.netNotUseful));
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                mPageIndex = mPageIndex + 10;
                mPageNum = 10;
                initClubData();
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
    }

    /**
     * 没有数据显示
     *
     * @param show
     */
    public void isShowNoData(boolean show) {
        if (show) {
            mLlNoProblem.setVisibility(View.VISIBLE);
            mRecyclerClub.setVisibility(View.GONE);
        } else {
            mLlNoProblem.setVisibility(View.GONE);
            mRecyclerClub.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 201 && data != null) {
            mSearchText = data.getStringExtra("mSearchText");
            mEditSearch.setText(mSearchText);
            initClubData();
        }
    }
}
