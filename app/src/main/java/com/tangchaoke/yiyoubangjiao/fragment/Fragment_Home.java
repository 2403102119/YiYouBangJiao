package com.tangchaoke.yiyoubangjiao.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.stx.xhb.xbanner.XBanner;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.activity.Activity_ActivityCenter;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Banner;
import com.tangchaoke.yiyoubangjiao.activity.Activity_InteractiveSolution;
import com.tangchaoke.yiyoubangjiao.activity.Activity_News;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Coach;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Login;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Main;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Message;
import com.tangchaoke.yiyoubangjiao.activity.Activity_MyClub;
import com.tangchaoke.yiyoubangjiao.activity.Activity_NewsInfo;
import com.tangchaoke.yiyoubangjiao.activity.Activity_SpecialConsultant;
import com.tangchaoke.yiyoubangjiao.adapter.HomeNewsAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.api.Constants;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.base.BaseFragment;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.HomeBean;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.NetWorkUsefulUtils;
import com.tangchaoke.yiyoubangjiao.utils.RecordSQLiteOpenHelper;
import com.tangchaoke.yiyoubangjiao.utils.WXUtil;
import com.tangchaoke.yiyoubangjiao.view.CleanUpCacheDialogView;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.tangchaoke.yiyoubangjiao.view.PullToRefreshLayout;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author hg
 * @create at 2018/3/8
 * @description 首页
 */
public class Fragment_Home extends BaseFragment {

    View mView;

    /**
     * 轮播图
     */
    @BindView(R.id.xbanner)
    XBanner mXBanner;

    /**
     * 新闻资讯
     */
    @BindView(R.id.recycler_home_news)
    RecyclerView mRecyclerHomeNews;

    HomeNewsAdapter mHomeNewsAdapter;

    /**
     * 上拉加载 下拉刷新
     */
    @BindView(R.id.pull_to_refresh)
    PullToRefreshLayout mPullToRefresh;

    /**
     * 暂无数据
     */
    @BindView(R.id.ll_no_problem)
    LinearLayout mLlNoProblem;

    /**
     * 提示内容
     */
    private String mPrompt = "";

    /**
     * 搜索 文本
     */
    @BindView(R.id.edit_search)
    EditText mEditSearch;

    /**
     * 接收 搜索内容
     */
    private String mSearch;

    /**
     * 实例化 Activity
     */
    Activity_Main mActivityMain = new Activity_Main();

    /**
     * 分享
     */
    @BindView(R.id.ll_top_right_share)
    LinearLayout mLlTopRightShare;

    public static Fragment_Home newInstance() {
        Fragment_Home fragment = new Fragment_Home();
        return fragment;
    }

    @OnClick({R.id.img_home_online_answer,
            R.id.rl_home_activity_center, R.id.rl_home_clup_style, R.id.rl_home_find_teach,
            R.id.img_share, R.id.img_message_top, R.id.tv_news_more, R.id.img_special_consultant, R.id.tv_special_consultant_more})
    void onClick(View view) {

        switch (view.getId()) {

            /**
             * 消息
             */
            case R.id.img_message_top:
                if (!BaseApplication.getApplication().isLogined()) {
                    mPrompt = "请先登录，再进入消息列表";
                    isLoginDialog();
                } else {
                    Intent mIntentMessage = new Intent(getActivity(), Activity_Message.class);
                    startActivity(mIntentMessage);
                }
                break;

            /**
             * 分享
             */
            case R.id.img_share:
                share();
                break;

            /**
             * 互动解答
             */
            case R.id.img_home_online_answer:
                if (!BaseApplication.getApplication().isLogined()) {
                    mPrompt = "请先登录，再进入互动解答";
                    isLoginDialog();
                } else {
                    Intent mIntentOnlineAnswer = new Intent(getActivity(), Activity_InteractiveSolution.class);
                    mIntentOnlineAnswer.putExtra("type3", "1");//界面：1首页互动答题 2我的学校互动答题
                    startActivity(mIntentOnlineAnswer);
                }
                break;

            /**
             * 活动中心
             */
            case R.id.rl_home_activity_center:
                Intent mIntentActivityCenter = new Intent(getActivity(), Activity_ActivityCenter.class);
                startActivity(mIntentActivityCenter);
                break;

            /**
             * 我的棋类机构
             */
            case R.id.rl_home_clup_style:
                if (!BaseApplication.getApplication().isLogined()) {
                    mPrompt = "请先登录，再进入我的学校";
                    isLoginDialog();
                } else {
                    Intent mIntentMyClup = new Intent(getActivity(), Activity_MyClub.class);
                    startActivity(mIntentMyClup);
                }
                break;

            /**
             * 大师简介
             */
            case R.id.rl_home_find_teach:
                Intent mIntentFindTeach = new Intent(getActivity(), Activity_Coach.class);
                mIntentFindTeach.putExtra("type", "1");//界面：1首页互动答题 2我的学校互动答题
                startActivity(mIntentFindTeach);
                break;

            /**
             * 最新动态  更多
             */
            case R.id.tv_news_more:
                Intent mIntentClupStyle = new Intent(getActivity(), Activity_News.class);
                mIntentClupStyle.putExtra("type", "1");//界面：1首页互动答题 2我的学校互动答题
                startActivity(mIntentClupStyle);
                break;

            /**
             * 平台特约顾问
             */
            case R.id.tv_special_consultant_more:
            case R.id.img_special_consultant:
                Intent mIntentSpecialConsultant = new Intent(getActivity(), Activity_SpecialConsultant.class);
                startActivity(mIntentSpecialConsultant);
                break;

        }

    }

    /**
     * 未登录  弹窗
     */
    private void isLoginDialog() {
        try {
            final CleanUpCacheDialogView mCleanUpCacheDialogView = new CleanUpCacheDialogView(getActivity());
            mCleanUpCacheDialogView.setTitle("温馨提示");
            mCleanUpCacheDialogView.setContent(mPrompt);
            mCleanUpCacheDialogView.setCancelable(false);
            mCleanUpCacheDialogView.setCustomOnClickListener(new CleanUpCacheDialogView.OnCustomDialogListener() {
                @Override
                public void setYesOnclick() {
                    Intent mIntentMine = new Intent(getActivity(), Activity_Login.class);
                    mIntentMine.putExtra("name", "home");
                    startActivity(mIntentMine);
                    mCleanUpCacheDialogView.dismiss();
                }
            });
            mCleanUpCacheDialogView.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mView);
        mLlTopRightShare.setVisibility(View.VISIBLE);
        mActivityMain = (Activity_Main) getActivity();
        mRecordSQLiteOpenHelper = new RecordSQLiteOpenHelper(getActivity());
        initPulltoRefresh();
        /**
         * 监听首页搜索框弹出软键盘  回车键 进行搜索
         */
        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mSearch = mEditSearch.getText().toString().trim();
                if (!HGTool.isEmpty(mSearch)) {
                    Log.e("==首页mSearch:::", mSearch + ":::mSearchText");
                    mActivityMain.setSearch(mSearch);
                    Intent mIntentClub = new Intent("com.tangchaoke.yiyoubangjiao.fragment.club");
                    getActivity().sendBroadcast(mIntentClub);
                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                    boolean hasData = hasData(mSearch);
                    if (!hasData) {
                        insertData(mSearch);
                    }
                    mSearch = "";
                    mEditSearch.setText(mSearch);
                }
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

        return mView;
    }

    /**
     * ========================================================搜索  历史========================================================
     */
    private RecordSQLiteOpenHelper mRecordSQLiteOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;

    /**
     * 插入数据
     */
    private void insertData(String tempName) {
        mSQLiteDatabase = mRecordSQLiteOpenHelper.getWritableDatabase();
        mSQLiteDatabase.execSQL("insert into records(name) values('" + tempName + "')");
        mSQLiteDatabase.close();
    }

    Cursor mCursor;

    /**
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String tempName) {
        mCursor = mRecordSQLiteOpenHelper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return mCursor.moveToNext();
    }

    /**
     * ========================================================获取轮播图列表和新闻资讯列表========================================================
     */
    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), "加载中",
                true, false, null);
        OkHttpUtils
                .post()
                .url(Api.GET_BANNER_LIST)
                .addParams("type", "Android")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(getActivity(), "服务器开小差！请稍后重试");
                        Log.e("====获取轮播图列表和新闻资讯列表：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====获取轮播图列表和新闻资讯列表：：：", response);
                        Log.e("====获取轮播图列表和新闻资讯列表：：：", Api.GET_BANNER_LIST);
                        HomeBean mHomeBean = JSONObject.parseObject(response, HomeBean.class);
                        mProgressHUD.dismiss();
                        if (RequestType.SUCCESS.equals(mHomeBean.getStatus())) {
                            if (mHomeBean.getModel1().size() != 0) {
                                initBanner(mHomeBean.getModel1());
                            }
                            if (mHomeBean.getModel2().size() != 0) {
                                initHomeRecyclerNews();
                                isShowNoData(false);
                                if (mPageIndex == 0) {
                                    mListHomeModel2.clear();
                                }
                                mListHomeModel2.addAll(mHomeBean.getModel2());
                            } else {
                                if (mPageIndex == 0) {
                                    isShowNoData(true);
                                }
                            }
                        } else {
                            isShowNoData(true);
                            if (mHomeBean.getStatus().equals("0")) {
                                IToast.show(getActivity(), mHomeBean.getMessage());
                            }
                        }
                    }
                });
    }

    List<HomeBean.HomeMode12Bean> mListHomeModel2 = new ArrayList<HomeBean.HomeMode12Bean>();

    /**
     * ========================================================最新动态========================================================
     */
    private void initHomeRecyclerNews() {
        mLlNoProblem.setVisibility(View.GONE);
        mRecyclerHomeNews.setHasFixedSize(true);
        mRecyclerHomeNews.setNestedScrollingEnabled(false);
        mRecyclerHomeNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerHomeNews.setHasFixedSize(true);
        mRecyclerHomeNews.setItemAnimator(new DefaultItemAnimator());
        mHomeNewsAdapter = new HomeNewsAdapter(getActivity(), mListHomeModel2,
                new HomeNewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Intent mIntentNewsInfo = new Intent(getActivity(), Activity_NewsInfo.class);
                        mIntentNewsInfo.putExtra("type", "1");
                        mIntentNewsInfo.putExtra("title", mListHomeModel2.get(position).getTitle());
                        mIntentNewsInfo.putExtra("content", mListHomeModel2.get(position).getContent());
                        mIntentNewsInfo.putExtra("path", mListHomeModel2.get(position).getPath());
                        startActivity(mIntentNewsInfo);
                    }
                });
        mRecyclerHomeNews.setAdapter(mHomeNewsAdapter);
    }

    private int mPageIndex = 0;

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
                    initData();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    IToast.show(getActivity(), "" + getResources().getString(R.string.netNotUseful));
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
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
            mRecyclerHomeNews.setVisibility(View.GONE);
        } else {
            mLlNoProblem.setVisibility(View.GONE);
            mRecyclerHomeNews.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ========================================================轮播图========================================================
     */
    private void initBanner(final List<HomeBean.HomeMode11Bean> mList) {
        /**
         * 第一个参数为图片资源集合
         * 第二个参数为提示文字资源集合
         *
         * 默认 轮播切换时间 5s
         */
        mXBanner.setData(mList, null);
        mXBanner.setmAdapter(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                Glide.with(getActivity())
                        .load(Api.PATH + mList.get(position).getBannerPhoto())
                        .into((ImageView) view);
            }
        });
        mXBanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, int position) {
                if (!HGTool.isEmpty(mList.get(position).getPath())) {
                    if (!HGTool.isEmpty(mList.get(position).getStatus())) {
                        if (mList.get(position).getStatus().equals("5")) {
                            if (!BaseApplication.getApplication().isLogined()) {
                                mPrompt = "请先登录，再进入抽奖界面";
                                isLoginDialog();
                            } else {
                                /**
                                 * 轮播图点击事件
                                 */
                                Intent mIntentBanner = new Intent(getActivity(), Activity_Banner.class);
                                mIntentBanner.putExtra("path", mList.get(position).getPath());
                                startActivity(mIntentBanner);
                            }
                        }
                    } else {
                        /**
                         * 轮播图点击事件
                         */
                        Intent mIntentBanner = new Intent(getActivity(), Activity_Banner.class);
                        mIntentBanner.putExtra("path", mList.get(position).getPath());
                        startActivity(mIntentBanner);
                    }
                } else {
                    return;
                }
            }
        });
    }

    /**
     * ========================================================首页分享========================================================
     */
    View mPopView;

    PopupWindow mPopupWindow;

    LinearLayout ll_wx_friend;

    LinearLayout ll_wx_pyq;

    TextView tv_avatar_cancel;

    @BindView(R.id.rl_home)
    RelativeLayout mRlHome;

    private void share() {

        /**
         * 发布弹窗
         * <p>
         * 弹出底部对话框，达到背景背景透明效果
         * <p>
         * 实现原理：弹出一个全屏popupWind+ow,将Gravity属性设置bottom,根背景设置为一个半透明的颜色，
         * 弹出时popupWindow的半透明效果背景覆盖了在Activity之上 给人感觉上是popupWindow弹出后，背景变成半透明
         */

        mPopView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window_share, null);
        ll_wx_friend = mPopView.findViewById(R.id.ll_wx_friend);
        ll_wx_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                wechatShare(false);
            }
        });
        ll_wx_pyq = mPopView.findViewById(R.id.ll_wx_pyq);
        ll_wx_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                wechatShare(true);
            }
        });
        tv_avatar_cancel = mPopView.findViewById(R.id.tv_avatar_cancel);
        tv_avatar_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow = new PopupWindow(mPopView,
                GridLayoutManager.LayoutParams.MATCH_PARENT,
                GridLayoutManager.LayoutParams.MATCH_PARENT);
        // 设置弹出动画
        mPopupWindow.setAnimationStyle(R.style.take_photo_anim);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        // 显示在根布局的底部
        mPopupWindow.showAtLocation(mRlHome, Gravity.BOTTOM | Gravity.LEFT, 0, 0);

    }

    private void wechatShare(boolean isChecked) {
        IWXAPI wxapi = WXAPIFactory.createWXAPI(getActivity(), Constants.WECHAT_APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        webpage.webpageUrl = Api.PATH + "ewm/index.html";
        msg.title = "易优帮教";
        msg.description = "易优帮教是当前教育创业者都在关注课外辅导的环境下，以帮助学生解决学习中的困难答疑迷惑为切入点而开发的互联网平台。  平台的优势聚焦当下帮助学习困难者解决难题，从只知答案到知其原理，提问者解决学习困惑，由学会提问变为学会解答，达到高效学习。 解答者由助力他人学习转化为助力个人收获，形成学习的转化，达到学习的共赢。";
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
        bmp.recycle();
        msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        /**
         * SendMessageToWX.Req.WXSceneTimeline 朋友圈
         *
         * SendMessageToWX.Req.WXSceneSession 好友
         */
        req.scene = isChecked ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        req.message = msg;
        wxapi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
