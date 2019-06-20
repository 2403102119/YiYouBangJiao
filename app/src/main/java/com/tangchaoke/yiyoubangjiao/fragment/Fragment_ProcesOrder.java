package com.tangchaoke.yiyoubangjiao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.tangchaoke.yiyoubangjiao.activity.Activity_OrderComment;
import com.tangchaoke.yiyoubangjiao.activity.Activity_OrderInfo;
import com.tangchaoke.yiyoubangjiao.adapter.OrderAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.base.BaseFragment;
import com.tangchaoke.yiyoubangjiao.model.OrderBean;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.NetWorkUsefulUtils;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.PromptDialogView;
import com.tangchaoke.yiyoubangjiao.view.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 进行中
 */
public class Fragment_ProcesOrder extends BaseFragment {

    View view;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    OrderAdapter mOrderAdapter;

    /**
     * 刷新
     */
    @BindView(R.id.pull_to_refresh)
    PullToRefreshLayout mPullToRefresh;

    @BindView(R.id.ll_no)
    LinearLayout mLlNo;

    public static Fragment_ProcesOrder newInstance() {
        Fragment_ProcesOrder fragment = new Fragment_ProcesOrder();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_wait_order, container, false);
        ButterKnife.bind(this, view);
        initPulltoRefresh();
        initOrder();
        return view;
    }

    List<OrderBean.OrderListBean> mList = new ArrayList<OrderBean.OrderListBean>();

    private void initOrder() {
        OkHttpUtils
                .post()
                .url(Api.GET_USER_ORDER_LIST)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("page.index", mPageIndex + "")
                .addParams("page.num", mPageNum + "")
                .addParams("type", "2")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(getActivity(), "服务器开小差 请稍后再试 ！ ");
                        Log.e("==获取用户订单列表2:::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==获取用户订单列表2:::", response);
                        Log.e("==获取用户订单列表2:::", Api.GET_USER_ORDER_LIST);
                        Log.e("==获取用户订单列表2:::", BaseApplication.getApplication().getToken());
                        Log.e("==获取用户订单列表2:::", mPageIndex + "");
                        Log.e("==获取用户订单列表2:::", mPageNum + "");
                        Log.e("==获取用户订单列表2:::", "1");
                        OrderBean mOrderBean = JSONObject.parseObject(response, OrderBean.class);
                        if (RequestType.SUCCESS.equals(mOrderBean.getStatus())) {
                            if (mOrderBean.getList().size() != 0) {
                                initRecyclerbalance();
                                isShowNoData(false);
                                if (mPageIndex == 0) {
                                    mOrderAdapter.clear();
                                }
                                mList.addAll(mOrderBean.getList());
                            } else {
                                if (mPageIndex == 0) {
                                    isShowNoData(true);
                                }
                            }
                        } else {
                            isShowNoData(true);
                            IToast.show(getActivity(), mOrderBean.getMessage());
                        }
                    }
                });
    }

    private int mPageIndex = 0;

    private int mPageNum = 10;

    private void initRecyclerbalance() {
        mLlNo.setVisibility(View.GONE);
        mRecycler.setHasFixedSize(true);
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setHasFixedSize(true);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mOrderAdapter = new OrderAdapter(getActivity(), 2, mList, new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent mIntentOrderInfo = new Intent(getActivity(), Activity_OrderInfo.class);
                mIntentOrderInfo.putExtra("oid", mList.get(position).getOid());
                startActivity(mIntentOrderInfo);
            }
        }, new OrderAdapter.OnEvaluationClickListener() {
            @Override
            public void onEvaluationClick(int position, View view) {
                String mOid = mList.get(position).getOid();
                isPromptDialog(mOid, position);
            }
        });
        mRecycler.setAdapter(mOrderAdapter);
    }

    PromptDialogView mPromptDialogView;

    private void isPromptDialog(final String mOid, final int position) {
        try {
            mPromptDialogView = new PromptDialogView(getActivity());
            mPromptDialogView.setTitle("温馨提示");
            mPromptDialogView.setContent("是否确认已收到货物 ？");
            mPromptDialogView.setYes("确认收货");
            mPromptDialogView.setNo("取消");
            mPromptDialogView.setCancelable(false);
            mPromptDialogView.setCustomOnClickListenerYes(new PromptDialogView.OnCustomDialogListener() {
                @Override
                public void setYesOnclick() {
                    initConfirmReceipt(mOid, position);
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

    private void initConfirmReceipt(final String mOid, final int position) {
        OkHttpUtils
                .post()
                .url(Api.CONFIRM_RECEIPT)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(getActivity(), "服务器开小差 请稍后再试 ！ ");
                        Log.e("==确认收货:::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==确认收货:::", response);
                        Log.e("==确认收货:::", Api.CONFIRM_RECEIPT);
                        Log.e("==确认收货:::", BaseApplication.getApplication().getToken());
                        Log.e("==确认收货:::", mOid);
                        Log.e("==确认收货:::", position + "");
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            mOrderAdapter.removeItem(position);
                            mPromptDialogView.dismiss();
                            Intent mIntentOrderComment = new Intent(getActivity(), Activity_OrderComment.class);
                            mIntentOrderComment.putExtra("oid", mOid);
                            startActivity(mIntentOrderComment);
                        } else {
                            IToast.show(getActivity(), mSuccessModel.getMessage());
                        }
                    }
                });
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
                    initOrder();
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
                initOrder();
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
