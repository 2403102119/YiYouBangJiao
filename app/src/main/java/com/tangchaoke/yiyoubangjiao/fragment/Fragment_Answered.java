package com.tangchaoke.yiyoubangjiao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
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
import com.tangchaoke.yiyoubangjiao.activity.Activity_Answered;
import com.tangchaoke.yiyoubangjiao.activity.Activity_AnsweredInfo;
import com.tangchaoke.yiyoubangjiao.adapter.UnAnsweredAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.base.BaseFragment;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.model.ProblemBean;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.utils.NetWorkUsefulUtils;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
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
 * @author hg
 * @create at 2018/3/13
 * @description 我提问已解答的
 */

public class Fragment_Answered extends BaseFragment {

    View view;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    UnAnsweredAdapter mAnsweredAdapter;

    /**
     * 刷新
     */
    @BindView(R.id.pull_to_refresh)
    PullToRefreshLayout mPullToRefresh;

    @BindView(R.id.ll_no)
    LinearLayout mLlNo;

    private int mPageIndex = 0;

    private int mPageNum = 10;

    List<ProblemBean.ProblemModelBean> mUnAnsweredModelList = new ArrayList<ProblemBean.ProblemModelBean>();

    public static Fragment_Answered newInstance() {
        Fragment_Answered fragment = new Fragment_Answered();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_answered, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        initPulltoRefresh();
        initUnAnswer();
    }

    Activity_Answered mActivity_Answered = new Activity_Answered();

    private void initUnAnswer() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), "加载中", true, false, null);

        OkHttpUtils
                .post()
                .url(Api.ALREADY_ANSWERED)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("page.index", mPageIndex + "")
                .addParams("page.num", mPageNum + "")
                .addParams("type1", mActivity_Answered.mType)
                .addParams("type2", "0")
                .addParams("type3", "Android")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(getActivity(), "服务器开小差！请稍后重试");
                        Log.e("==我提问的：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==我提问的：：：", response);
                        Log.e("==我提问的：：：", Api.ALREADY_ANSWERED);
                        Log.e("==我提问的：：：", BaseApplication.getApplication().getToken());
                        Log.e("==我提问的：：：", mPageIndex + "");
                        Log.e("==我提问的：：：", mPageNum + "");
                        Log.e("==我提问的：：：", mActivity_Answered.mType+":::type1");
                        ProblemBean mUnAnsweredModel = JSONObject.parseObject(response, ProblemBean.class);
                        mProgressHUD.dismiss();
                        if (RequestType.SUCCESS.equals(mUnAnsweredModel.getStatus())) {
                            if (mUnAnsweredModel.getModel().size() != 0) {
                                initRecyclerOnlineAnswer();
                                isShowNoData(false);
                                if (mPageIndex == 0) {
                                    mAnsweredAdapter.clear();
                                }
                                mUnAnsweredModelList.addAll(mUnAnsweredModel.getModel());
                            } else {
                                if (mPageIndex == 0) {
                                    isShowNoData(true);
                                }
                            }
                        } else {
                            if (mUnAnsweredModel.getStatus().equals("9") || mUnAnsweredModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mUnAnsweredModel.getStatus(), getActivity(), true);
                                if (isAccountException == false) {
                                    IToast.show(getActivity(), "登录失效");
                                }
                            } else if (mUnAnsweredModel.getStatus().equals("0")) {
                                IToast.show(getActivity(), mUnAnsweredModel.getMessage());
                            }
                        }
                    }
                });

    }

    /**
     * 已解答题目列表
     */
    private void initRecyclerOnlineAnswer() {
        mRecycler.setHasFixedSize(true);
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setHasFixedSize(true);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mAnsweredAdapter = new UnAnsweredAdapter(mActivity_Answered.mType, getActivity(), mUnAnsweredModelList, new UnAnsweredAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent mIntentAnsweredInfo = new Intent(getActivity(), Activity_AnsweredInfo.class);
                mIntentAnsweredInfo.putExtra("oid", mUnAnsweredModelList.get(position).getOid());
                mIntentAnsweredInfo.putExtra("type", "me");
                startActivity(mIntentAnsweredInfo);
            }
        }, new UnAnsweredAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position, View view) {
                String mOid = mUnAnsweredModelList.get(position).getOid();
                isPromptDialog(mOid, position);
            }
        });
        mRecycler.setAdapter(mAnsweredAdapter);
    }

    PromptDialogView mPromptDialogView;

    private void isPromptDialog(final String mOid, final int position) {
        try {
            mPromptDialogView = new PromptDialogView(getActivity());
            mPromptDialogView.setTitle("温馨提示");
            mPromptDialogView.setContent("您是否确认删除该题目？");
            mPromptDialogView.setYes("确认");
            mPromptDialogView.setNo("取消");
            mPromptDialogView.setCancelable(false);
            mPromptDialogView.setCustomOnClickListenerYes(new PromptDialogView.OnCustomDialogListener() {
                @Override
                public void setYesOnclick() {
                    initDelete(mOid, position);
                    mPromptDialogView.dismiss();
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

    LocalBroadcastManager localBroadcastManager;

    private void initDelete(final String mOid, final int position) {
        final ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), "删除中", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.DELETE_EXERCISES)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .addParams("type1", "Android")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(getActivity(), "服务器开小差！请稍后重试");
                        mProgressHUD.dismiss();
                        Log.e("==删除待抢答的题目：：：", e.getMessage());
                        Log.e("==删除待抢答的题目：：：", Api.DELETE_EXERCISES);
                        Log.e("==删除待抢答的题目：：：", BaseApplication.getApplication().getToken());
                        Log.e("==删除待抢答的题目：：：", mOid);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==删除待抢答的题目：：：", Api.DELETE_EXERCISES);
                        Log.e("==删除待抢答的题目：：：", BaseApplication.getApplication().getToken());
                        Log.e("==删除待抢答的题目：：：", mOid);
                        mProgressHUD.dismiss();
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            mUnAnsweredModelList.remove(position);
                            mAnsweredAdapter.notifyDataSetChanged();
                            initUnAnswer();
                            localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
                            Intent mIntent = new Intent("com.tangchaoke.yiyoubangjiao.home");
                            mIntent.putExtra("tuisong", "answered");
                            localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(), getActivity(), true);
                                if (isAccountException == false) {
                                    IToast.show(getActivity(), "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(getActivity(), mSuccessModel.getMessage());
                            }
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
                    initUnAnswer();
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
                initUnAnswer();
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
