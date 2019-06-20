package com.tangchaoke.yiyoubangjiao.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.CommentAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.PointsMallInfoCommentAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.model.CommentBean;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.NetWorkUsefulUtils;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/8
* description: 全部评论
*/
public class Activity_ViewAllComment extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    CommentAdapter mCommentAdapter;

    @BindView(R.id.ll_no)
    LinearLayout mLlNo;

    @BindView(R.id.pull_to_refresh)
    PullToRefreshLayout mPullToRefresh;

    private int mPageIndex = 0;

    private int mPageNum = 10;

    private String mOid = "";

    @OnClick({R.id.ll_back})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_view_all_comment;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("商品评论");
    }

    @Override
    protected void initData() {
        mOid = getIntent().getStringExtra("oid");
        initPulltoRefresh();
        initCommentList();
    }

    List<CommentBean.CommentListBean> mList = new ArrayList<CommentBean.CommentListBean>();

    private void initCommentList() {
        OkHttpUtils
                .post()
                .url(Api.GET_COMMENT_LIST)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("page.index", mPageIndex + "")
                .addParams("page.num", mPageNum + "")
                .addParams("oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_ViewAllComment.this, "服务器开小差 请稍后再试 ！ ");
                        Log.e("==获取商品评论列表", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==获取商品评论列表", response);
                        Log.e("==获取商品评论列表", Api.GET_COMMENT_LIST);
                        Log.e("==获取商品评论列表", BaseApplication.getApplication().getToken());
                        Log.e("==获取商品评论列表", mPageIndex + "");
                        Log.e("==获取商品评论列表", mPageNum + "");
                        Log.e("==获取商品评论列表", mOid);
                        CommentBean mCommentListBean = JSONObject.parseObject(response, CommentBean.class);
                        if (RequestType.SUCCESS.equals(mCommentListBean.getStatus())) {
                            if (mCommentListBean.getList().size() != 0) {
                                initRecyclerGrandmaster();
                                isShowNoData(false);
                                if (mPageIndex == 0) {
                                    mCommentAdapter.clear();
                                }
                                mList.addAll(mCommentListBean.getList());
                            } else {
                                if (mPageIndex == 0) {
                                    isShowNoData(true);
                                }
                            }
                        } else {
                            isShowNoData(true);
                            IToast.show(Activity_ViewAllComment.this, mCommentListBean.getMessage());
                        }
                    }
                });
    }

    private void initRecyclerGrandmaster() {
        mRecycler.setHasFixedSize(true);
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(Activity_ViewAllComment.this));
        mRecycler.setHasFixedSize(true);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mCommentAdapter = new CommentAdapter(Activity_ViewAllComment.this, mList);
        mRecycler.setAdapter(mCommentAdapter);
    }

    private void initPulltoRefresh() {
        mPullToRefresh.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                if (NetWorkUsefulUtils.getActiveNetwork(Activity_ViewAllComment.this)) {
                    mPageIndex = 0;
                    mPageNum = 10;
                    initCommentList();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    IToast.show(Activity_ViewAllComment.this, "" + getResources().getString(R.string.netNotUseful));
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                mPageIndex = mPageIndex + 10;
                mPageNum = 10;
                initCommentList();
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
