package com.tangchaoke.yiyoubangjiao.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Balance;
import com.tangchaoke.yiyoubangjiao.activity.Activity_MessageSystem;
import com.tangchaoke.yiyoubangjiao.adapter.BalanceAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.base.BaseFragment;
import com.tangchaoke.yiyoubangjiao.model.ExpensesRecordModel;
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
import okhttp3.Call;

/**
 * 余额明细
 */
public class Fragment_Balance extends BaseFragment {

    View view;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    BalanceAdapter mBalanceAdapter;

    List<ExpensesRecordModel.BalanceModelModel> mBalanceModelList = new ArrayList<ExpensesRecordModel.BalanceModelModel>();

    /**
     * 刷新
     */
    @BindView(R.id.pull_to_refresh)
    PullToRefreshLayout mPullToRefresh;

    @BindView(R.id.ll_no)
    LinearLayout mLlNo;

    public static Fragment_Answered newInstance() {
        Fragment_Answered fragment = new Fragment_Answered();
        return fragment;
    }

    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_balance, container, false);
        ButterKnife.bind(this, view);
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tangchaoke.yiyoubangjiao.balance");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
        initPulltoRefresh();
        /**
         * 收支明细
         */
        initExpensesRecord();
        return view;
    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra("tuisong");
            /**
             * 如果有新的消息，在此界面要 及时进行刷新
             */
            if (num.equals("balance")) {
                mPageIndex = 0;
                mPageNum = 10;
                initExpensesRecord();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putString("activity", "Fragment_Balance");
        mEditor.commit();
    }

    private int mPageIndex = 0;

    private int mPageNum = 10;

    /**
     * 消费明细
     */
    private void initExpensesRecord() {
        OkHttpUtils
                .post()
                .url(Api.GET_BALANCE_LIST)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("page.index", mPageIndex + "")
                .addParams("page.num", mPageNum + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(getActivity(), "服务器开小差！请稍后重试");
                        Log.e("==消费明细列表", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==消费明细列表", response);
                        Log.e("==消费明细列表", Api.GET_BALANCE_LIST);
                        Log.e("==消费明细列表", BaseApplication.getApplication().getToken());
                        Log.e("==消费明细列表", mPageIndex + "");
                        Log.e("==消费明细列表", mPageNum + "");
                        ExpensesRecordModel mBalanceModel = JSONObject.parseObject(response, ExpensesRecordModel.class);
                        if (RequestType.SUCCESS.equals(mBalanceModel.getStatus())) {
                            if (mBalanceModel.getModel().size() != 0) {
                                initRecyclerbalance();
                                isShowNoData(false);
                                if (mPageIndex == 0) {
                                    mBalanceAdapter.clear();
                                }
                                mBalanceModelList.addAll(mBalanceModel.getModel());
                            } else {
                                if (mPageIndex == 0) {
                                    isShowNoData(true);
                                }
                            }
                        } else {
                            isShowNoData(true);
                            if (mBalanceModel.getStatus().equals("9") || mBalanceModel.getStatus().equals("8")) {

                            } else if (mBalanceModel.getStatus().equals("0")) {
                                IToast.show(getActivity(), mBalanceModel.getMessage());
                            }
                        }
                    }
                });
    }

    /**
     * 消费明细
     */
    private void initRecyclerbalance() {
        mRecycler.setHasFixedSize(true);
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setHasFixedSize(true);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mBalanceAdapter = new BalanceAdapter(getActivity(), mBalanceModelList);
        mRecycler.setAdapter(mBalanceAdapter);
    }

    /**
     * 刷新界面
     */
    private void initPulltoRefresh() {
        mPullToRefresh.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                if (NetWorkUsefulUtils.getActiveNetwork(getActivity())) {
                    mPageIndex = 0;
                    mPageNum = 10;
                    initExpensesRecord();
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
                initExpensesRecord();
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
            mLlNo.setVisibility(View.VISIBLE);
            mRecycler.setVisibility(View.GONE);
        } else {
            mLlNo.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
        }
    }

}
