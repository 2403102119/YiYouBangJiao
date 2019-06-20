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
import com.tangchaoke.yiyoubangjiao.activity.Activity_Answered;
import com.tangchaoke.yiyoubangjiao.activity.Activity_AnsweredInfo;
import com.tangchaoke.yiyoubangjiao.adapter.HelpUnAnsweredAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.base.BaseFragment;
import com.tangchaoke.yiyoubangjiao.model.ProblemBean;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.utils.NetWorkUsefulUtils;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
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
 * @description 我帮助已解答的
 */

public class Fragment_HelpAnswered extends BaseFragment {

    View view;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    HelpUnAnsweredAdapter mHelpUnAnsweredAdapter;

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

    public static Fragment_HelpAnswered newInstance() {
        Fragment_HelpAnswered fragment = new Fragment_HelpAnswered();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_answered, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initData() {
        initPulltoRefresh();
        initUnAnswer();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
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
                .addParams("type2", "1")
                .addParams("type3", "Android")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(getActivity(), "服务器开小差！请稍后重试");
                        Log.e("==我解答的：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==我解答的：：：", response);
                        Log.e("==我解答的：：：", Api.ALREADY_ANSWERED);
                        Log.e("==我解答的：：：", BaseApplication.getApplication().getToken());
                        Log.e("==我解答的：：：", mPageIndex + "");
                        Log.e("==我解答的：：：", mPageNum + "");
                        Log.e("==我提问的：：：", mActivity_Answered.mType+":::type1");
                        ProblemBean mUnAnsweredModel = JSONObject.parseObject(response, ProblemBean.class);
                        mProgressHUD.dismiss();
                        if (RequestType.SUCCESS.equals(mUnAnsweredModel.getStatus())) {
                            if (mUnAnsweredModel.getModel().size() != 0) {
                                initRecyclerOnlineAnswer();
                                isShowNoData(false);
                                if (mPageIndex == 0) {
                                    mHelpUnAnsweredAdapter.clear();
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
        mHelpUnAnsweredAdapter = new HelpUnAnsweredAdapter(getActivity(), mUnAnsweredModelList, new HelpUnAnsweredAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent mIntentAnsweredInfo = new Intent(getActivity(), Activity_AnsweredInfo.class);
                mIntentAnsweredInfo.putExtra("oid", mUnAnsweredModelList.get(position).getOid());
                mIntentAnsweredInfo.putExtra("type", "help");
                startActivity(mIntentAnsweredInfo);
            }
        });
        mRecycler.setAdapter(mHelpUnAnsweredAdapter);
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
