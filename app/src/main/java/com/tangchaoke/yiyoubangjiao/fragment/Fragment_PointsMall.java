package com.tangchaoke.yiyoubangjiao.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.activity.Activity_MessageSystem;
import com.tangchaoke.yiyoubangjiao.activity.Activity_PointsMallInfo;
import com.tangchaoke.yiyoubangjiao.adapter.ClubAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.PointsMallAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.model.PointsMallBean;
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
 * 积分商城
 */
public class Fragment_PointsMall extends Fragment {

    View view;

    @BindView(R.id.ll_top_right_nearby)
    LinearLayout mLlTopRightNearby;

    @BindView(R.id.ll_top_right)
    LinearLayout mLlTopRight;

    /**
     * 上拉加载 下拉刷新
     */
    @BindView(R.id.pull_to_refresh)
    PullToRefreshLayout mPullToRefresh;

    @BindView(R.id.recycler_points_mall)
    RecyclerView mRecyclerPointsMall;

    private int mPageIndex = 0;

    private int mPageNum = 10;

    PointsMallAdapter mPointsMallAdapter;

    @BindView(R.id.ll_no_problem)
    LinearLayout mLlNoProblem;

    public static Fragment_PointsMall newInstance() {
        Fragment_PointsMall fragment = new Fragment_PointsMall();
        return fragment;
    }

    @BindView(R.id.img_layout_switch)
    ImageView mImgLayoutSwitch;

    /**
     * 默认列表布局  1
     * <p>
     * 2  方格布局
     */
    int mLayoutSwitch = 1;

    /**
     * 默认列表布局  0
     * <p>
     * 1 降序  2 降序
     */
    int mPrice = 0;

    @BindView(R.id.tv_comprehensive)
    TextView mTvComprehensive;

    @BindView(R.id.tv_sales_volume)
    TextView mTvSalesVolume;

    @BindView(R.id.tv_price)
    TextView mTvPrice;

    private int mType = 0;

    @BindView(R.id.ll_top_search)
    LinearLayout mLlTopSearch;

    @BindView(R.id.img_price)
    ImageView mImgPrice;

    @OnClick({R.id.ll_layout_switch, R.id.ll_comprehensive, R.id.ll_sales_volume, R.id.ll_price})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_layout_switch:
                if (mLayoutSwitch == 1) {
                    mLayoutSwitch = 2;
                    mImgLayoutSwitch.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_points_mall_grid));
                    initRecyclerClub();
                } else if (mLayoutSwitch == 2) {
                    mLayoutSwitch = 1;
                    mImgLayoutSwitch.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_points_mall_list));
                    initRecyclerClub();
                }
                mPrice = 0;
                break;

            case R.id.ll_comprehensive:
                mTvComprehensive.setTextColor(getResources().getColor(R.color.color43C9FE));
                mTvSalesVolume.setTextColor(getResources().getColor(R.color.color7f7f7f));
                mTvPrice.setTextColor(getResources().getColor(R.color.color7f7f7f));
                mImgPrice.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_price));
                mPrice = 1;
                mType = 2;
                initCommodityList();
                break;

            case R.id.ll_sales_volume:
                mTvComprehensive.setTextColor(getResources().getColor(R.color.color7f7f7f));
                mTvSalesVolume.setTextColor(getResources().getColor(R.color.color43C9FE));
                mTvPrice.setTextColor(getResources().getColor(R.color.color7f7f7f));
                mImgPrice.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_price));
                mPrice = 1;
                mType = 1;
                initCommodityList();
                break;

            case R.id.ll_price:
                if (mPrice == 0) {
                    mPrice = 2;
                    mTvComprehensive.setTextColor(getResources().getColor(R.color.color7f7f7f));
                    mTvSalesVolume.setTextColor(getResources().getColor(R.color.color7f7f7f));
                    mTvPrice.setTextColor(getResources().getColor(R.color.color43C9FE));
                    mImgPrice.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_price_drop));
                } else if (mPrice == 2) {
                    mPrice = 1;
                    mTvComprehensive.setTextColor(getResources().getColor(R.color.color7f7f7f));
                    mTvSalesVolume.setTextColor(getResources().getColor(R.color.color7f7f7f));
                    mTvPrice.setTextColor(getResources().getColor(R.color.color43C9FE));
                    mTvPrice.setText("价格");
                    mImgPrice.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_price_rise));
                } else if (mPrice == 1) {
                    mPrice = 2;
                    mTvComprehensive.setTextColor(getResources().getColor(R.color.color7f7f7f));
                    mTvSalesVolume.setTextColor(getResources().getColor(R.color.color7f7f7f));
                    mTvPrice.setTextColor(getResources().getColor(R.color.color43C9FE));
                    mTvPrice.setText("价格");
                    mImgPrice.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_price_drop));
                }
                mType = 3;
                initCommodityList();
                break;

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_points_mall, container, false);
        ButterKnife.bind(this, view);
        mLlTopRightNearby.setVisibility(View.GONE);
        mLlTopRight.setVisibility(View.GONE);
        mLlTopSearch.setVisibility(View.INVISIBLE);
        initPulltoRefresh();
        initCommodityList();
        return view;
    }

    List<PointsMallBean.PointsMallListBean> mPointsMallList = new ArrayList<PointsMallBean.PointsMallListBean>();

    private void initCommodityList() {
        OkHttpUtils
                .post()
                .url(Api.GET_COMMODITY_LIST)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("page.index", mPageIndex + "")
                .addParams("page.num", mPageNum + "")
                .addParams("type", mType + "")
                .addParams("sort", mPrice + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(getActivity(), "服务器开小差！请稍后重试");
                        Log.e("==获取商品列表 ：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==获取商品列表 ：：：", response);
                        Log.e("==获取商品列表 ：：：", Api.GET_COMMODITY_LIST);
                        Log.e("==获取商品列表 ：：：", BaseApplication.getApplication().getToken());
                        Log.e("==获取商品列表 ：：：", mPageIndex + "");
                        Log.e("==获取商品列表 ：：：", mPageNum + "");
                        Log.e("==获取商品列表 ：：：", mType + "");
                        Log.e("==获取商品列表 ：：：", mPrice + "");
                        PointsMallBean mPointsMallBean = JSONObject.parseObject(response, PointsMallBean.class);
                        if (RequestType.SUCCESS.equals(mPointsMallBean.getStatus())) {
                            if (mPointsMallBean.getList().size() != 0) {
                                initRecyclerClub();
                                isShowNoData(false);
                                if (mPageIndex == 0) {
                                    mPointsMallAdapter.clear();
                                }
                                mPointsMallList.addAll(mPointsMallBean.getList());
                            } else {
                                if (mPageIndex == 0) {
                                    isShowNoData(true);
                                }
                            }
                        } else {
                            isShowNoData(true);
                            IToast.show(getActivity(), mPointsMallBean.getMessage());
                        }
                    }
                });
    }

    private void initRecyclerClub() {
        mRecyclerPointsMall.setHasFixedSize(true);
        mRecyclerPointsMall.setNestedScrollingEnabled(false);
        if (mLayoutSwitch == 2) {
            mRecyclerPointsMall.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else if (mLayoutSwitch == 1) {
            mRecyclerPointsMall.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerPointsMall.setHasFixedSize(true);
        mRecyclerPointsMall.setItemAnimator(new DefaultItemAnimator());
        mPointsMallAdapter = new PointsMallAdapter(getActivity(), mLayoutSwitch, mPointsMallList,
                new PointsMallAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Intent mIntentPointsMallInfo = new Intent(getActivity(), Activity_PointsMallInfo.class);
                        mIntentPointsMallInfo.putExtra("oid", mPointsMallList.get(position).getOid());
                        startActivity(mIntentPointsMallInfo);
                    }
                });
        mRecyclerPointsMall.setAdapter(mPointsMallAdapter);
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
                    initCommodityList();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    IToast.show(getActivity(), "" + getResources().getString(R.string.netNotUseful));
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                mPageIndex = mPageIndex + 10;
                mPageNum = 10;
                initCommodityList();
                // 千万别忘了告诉控件刷新完毕了哦！
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
            mRecyclerPointsMall.setVisibility(View.GONE);
        } else {
            mLlNoProblem.setVisibility(View.GONE);
            mRecyclerPointsMall.setVisibility(View.VISIBLE);
        }
    }

}
