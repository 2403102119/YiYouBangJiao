package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.NewsAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.NewsBean;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
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
* description: 最新动态
*/
public class Activity_News extends BaseActivity {

    @BindView(R.id.ll_top_activity)
    LinearLayout mLlTopActivity;

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    NewsAdapter mNewsAdapter;

    @BindView(R.id.ll_no)
    LinearLayout mLlNo;

    /**
     * 界面：1首页互动答题 2我的学校互动答题
     */
    private String mType = "";

    /**
     * 上拉加载 下拉刷新
     */
    @BindView(R.id.pull_to_refresh)
    PullToRefreshLayout mPullToRefresh;

    private int mPageIndex = 0;

    private int mPageNum = 10;

    private String mLogo = "";

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
        return R.layout.activity_news;
    }

    @Override
    public void initTitleBar() {
        if (!HGTool.isEmpty(mType)) {
            if (mType.equals("2")) {
                mLlTopActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_top_activity_club));
                mTvTopTitle.setText("最新动态");
                mTvTopTitle.setTextColor(getResources().getColor(R.color.color333333));
                mRecycler.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mLlLogo.setVisibility(View.VISIBLE);
                mLlAdoption.setVisibility(View.GONE);
            } else if (mType.equals("1")) {
                mLlTopActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_top_activity));
                mTvTopTitle.setText("最新动态");
                mTvTopTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
                mRecycler.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mLlLogo.setVisibility(View.GONE);
                mLlAdoption.setVisibility(View.INVISIBLE);
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
        if (!HGTool.isEmpty(mLogo)) {
            Glide.with(Activity_News.this)
                    .load(Api.PATH + mLogo)
                    .into(mImgLogo);
        }
        initPulltoRefresh();
        initTeacher();
    }

    List<NewsBean.NewsListBean> mClubList = new ArrayList<NewsBean.NewsListBean>();

    private void initTeacher() {
        OkHttpUtils
                .post()
                .url(Api.GET_CLUB_NEWST_LIST)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("type", mType)
                .addParams("page.index", mPageIndex + "")
                .addParams("page.num", mPageNum + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_News.this, "服务器开小差！请稍后重试");
                        Log.e("==获取俱乐部新闻资讯列表：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==获取俱乐部新闻资讯列表：：：", response);
                        Log.e("==获取俱乐部新闻资讯列表：：：", Api.GET_CLUB_NEWST_LIST);
                        Log.e("==获取俱乐部新闻资讯列表：：：", BaseApplication.getApplication().getToken());
                        Log.e("==获取俱乐部新闻资讯列表：：：", mType);
                        Log.e("==获取俱乐部新闻资讯列表：：：", mPageIndex + "");
                        Log.e("==获取俱乐部新闻资讯列表：：：", mPageNum + "");
                        NewsBean mNewsBean = JSONObject.parseObject(response, NewsBean.class);
                        if (RequestType.SUCCESS.equals(mNewsBean.getStatus())) {
                            if (mNewsBean.getList().size() != 0) {
                                initRecyclerNews();
                                isShowNoData(false);
                                if (mPageIndex == 0) {
                                    mNewsAdapter.clear();
                                }
                                mClubList.addAll(mNewsBean.getList());
                            } else {
                                if (mPageIndex == 0) {
                                    isShowNoData(true);
                                }
                            }
                        } else {
                            if (mNewsBean.getStatus().equals("9") || mNewsBean.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mNewsBean.getStatus(),
                                        Activity_News.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_News.this, "登录失效");
                                }
                            } else if (mNewsBean.getStatus().equals("0")) {
                                IToast.show(Activity_News.this, mNewsBean.getMessage());
                            }
                        }
                    }
                });
    }

    private void initRecyclerNews() {
        mRecycler.setHasFixedSize(true);
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(Activity_News.this));
        mRecycler.setHasFixedSize(true);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mNewsAdapter = new NewsAdapter(Activity_News.this, mClubList,
                new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Intent mIntentNewsInfo = new Intent(Activity_News.this, Activity_NewsInfo.class);
                        mIntentNewsInfo.putExtra("type", mType);//界面：1首页互动答题 2我的学校互动答题
                        mIntentNewsInfo.putExtra("title", mClubList.get(position).getTitle());
                        mIntentNewsInfo.putExtra("content", mClubList.get(position).getContent());
                        mIntentNewsInfo.putExtra("path", mClubList.get(position).getPath());
                        startActivity(mIntentNewsInfo);
                    }
                });
        mRecycler.setAdapter(mNewsAdapter);
    }

    /**
     * 刷新
     */
    private void initPulltoRefresh() {
        mPullToRefresh.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                if (NetWorkUsefulUtils.getActiveNetwork(Activity_News.this)) {
                    mPageIndex = 0;
                    mPageNum = 10;
                    initTeacher();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    IToast.show(Activity_News.this, "" + getResources().getString(R.string.netNotUseful));
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                mPageIndex = mPageIndex + 10;
                mPageNum = 10;
                initTeacher();
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
