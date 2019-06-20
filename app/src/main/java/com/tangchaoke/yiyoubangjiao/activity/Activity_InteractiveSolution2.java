package com.tangchaoke.yiyoubangjiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.AnswerPopChessAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.FindTeacherHigePopAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.FindTeacherInPopAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.FindTeacherPopSubjectAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.FindTeacherSmallPopAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.InteractiveSolution2Adapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.NoReleaseProblemModel;
import com.tangchaoke.yiyoubangjiao.model.ProblemBean;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.type.SubjectType;
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
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 互动解答
*/
public class Activity_InteractiveSolution2 extends BaseActivity {

    @BindView(R.id.ll_top_activity)
    LinearLayout mLlTopActivity;

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    /**
     * 根目录
     */
    @BindView(R.id.ll_online_answer)
    LinearLayout mLlOnlineAnswer;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    InteractiveSolution2Adapter mInteractiveSolution2Adapter;

    List<ProblemBean.ProblemModelBean> mProblemModelList = new ArrayList<ProblemBean.ProblemModelBean>();

    @BindView(R.id.tv_chess)
    TextView mTvChess;

    @BindView(R.id.img_chess)
    ImageView mImgChess;

    @BindView(R.id.view_find_teacher_pop)
    View mViewFindTeacherPop;

    @BindView(R.id.tv_find_tearch_grade)
    TextView mTvFindTearchGrade;

    @BindView(R.id.img_find_tearch_grade)
    ImageView mImgFindTearchGrade;

    @BindView(R.id.tv_find_tearch_subject)
    TextView mTvFindTearchSubject;

    @BindView(R.id.img_find_tearch_subject)
    ImageView mImgFindTearchSubject;

    @BindView(R.id.ll_adoption)
    LinearLayout mLlAdoption;

    @BindView(R.id.ll_top_right)
    LinearLayout mLlTopRight;

    //界面：1首页互动答题 2我的学校互动答题
    private String mType3 = "";

    //问题类型:1文化课问题 2棋类问题
    private String mType2 = "";

    private String mChessType3 = "";

    @OnClick({R.id.ll_back, R.id.ll_chess, R.id.ll_find_tearch_grade, R.id.ll_find_tearch_subject, R.id.ll_top_right})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                mEditor.putString("activity", "");
                mEditor.commit();
                break;

            case R.id.ll_chess:
                if (mChessPopupWindow.isShowing()) {
                    mChessPopupWindow.dismiss();
                    mTvChess.setTextColor(Activity_InteractiveSolution2.this.getResources().getColor(R.color.color999999));
                    mImgChess.setImageDrawable(ContextCompat.getDrawable(Activity_InteractiveSolution2.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                } else {
                    if (Build.VERSION.SDK_INT < 24) {
                        // 只有24这个版本有问题，好像是源码的问题
                        mChessPopupWindow.showAsDropDown(mViewFindTeacherPop);
                    } else {
                        Rect rect = new Rect();
                        mViewFindTeacherPop.getGlobalVisibleRect(rect);
                        int h = mViewFindTeacherPop.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                        mChessPopupWindow.setHeight(h);
                        mChessPopupWindow.showAsDropDown(mViewFindTeacherPop);
                    }
                    mTvChess.setTextColor(Activity_InteractiveSolution2.this.getResources().getColor(R.color.color73D1EE));
                    mImgChess.setImageDrawable(ContextCompat.getDrawable(Activity_InteractiveSolution2.this.getApplicationContext(), R.drawable.ic_un_down_arrow));
                }
                break;

            case R.id.ll_find_tearch_grade:
                if (mSubjectPopupWindow.isShowing()) {
                    mSubjectPopupWindow.dismiss();
                }
                if (mGradePopupWindow.isShowing()) {
                    mGradePopupWindow.dismiss();
                    mTvFindTearchGrade.setTextColor(Activity_InteractiveSolution2.this.getResources().getColor(R.color.color999999));
                    mImgFindTearchGrade.setImageDrawable(ContextCompat.getDrawable(Activity_InteractiveSolution2.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                } else {
                    if (Build.VERSION.SDK_INT < 24) {
                        // 只有24这个版本有问题，好像是源码的问题
                        mGradePopupWindow.showAsDropDown(mViewFindTeacherPop);
                    } else {
                        Rect rect = new Rect();
                        mViewFindTeacherPop.getGlobalVisibleRect(rect);
                        int h = mViewFindTeacherPop.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                        mGradePopupWindow.setHeight(h);
                        mGradePopupWindow.showAsDropDown(mViewFindTeacherPop);
                    }
                    mTvFindTearchGrade.setTextColor(Activity_InteractiveSolution2.this.getResources().getColor(R.color.color73D1EE));
                    mImgFindTearchGrade.setImageDrawable(ContextCompat.getDrawable(Activity_InteractiveSolution2.this.getApplicationContext(), R.drawable.ic_un_down_arrow));
                    mTvFindTearchSubject.setTextColor(Activity_InteractiveSolution2.this.getResources().getColor(R.color.color999999));
                    mImgFindTearchSubject.setImageDrawable(ContextCompat.getDrawable(Activity_InteractiveSolution2.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                }
                break;

            case R.id.ll_find_tearch_subject:
                if (mGradePopupWindow.isShowing()) {
                    mGradePopupWindow.dismiss();
                }
                if (mSubjectPopupWindow.isShowing()) {
                    mSubjectPopupWindow.dismiss();
                    mTvFindTearchSubject.setTextColor(Activity_InteractiveSolution2.this.getResources().getColor(R.color.color999999));
                    mImgFindTearchSubject.setImageDrawable(ContextCompat.getDrawable(Activity_InteractiveSolution2.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                } else {
                    if (Build.VERSION.SDK_INT < 24) {
                        // 只有24这个版本有问题，好像是源码的问题
                        mSubjectPopupWindow.showAsDropDown(mViewFindTeacherPop);
                    } else {
                        Rect rect = new Rect();
                        mViewFindTeacherPop.getGlobalVisibleRect(rect);
                        int h = mViewFindTeacherPop.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                        mSubjectPopupWindow.setHeight(h);
                        mSubjectPopupWindow.showAsDropDown(mViewFindTeacherPop);
                    }
                    mTvFindTearchGrade.setTextColor(Activity_InteractiveSolution2.this.getResources().getColor(R.color.color999999));
                    mImgFindTearchGrade.setImageDrawable(ContextCompat.getDrawable(Activity_InteractiveSolution2.this.getApplicationContext(), R.drawable.ic_clup_arrow));
                    mTvFindTearchSubject.setTextColor(Activity_InteractiveSolution2.this.getResources().getColor(R.color.color73D1EE));
                    mImgFindTearchSubject.setImageDrawable(ContextCompat.getDrawable(Activity_InteractiveSolution2.this.getApplicationContext(), R.drawable.ic_un_down_arrow));
                }
                break;

            case R.id.ll_top_right:
                initNoReleaseProblem();
                break;

        }

    }

    /**
     * 判断用户是否有未解答/为采纳的问题
     * <p>
     * 如果有  直接就不能进入发题界面
     */
    private void initNoReleaseProblem() {
        OkHttpUtils
                .post()
                .url(Api.JUDGE_USER_EXERCISES)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("type1", mType2 + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_InteractiveSolution2.this, "服务器开小差！请稍后重试");
                        Log.e("====判断用户是否有未解答问题:::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====判断用户是否有未解答问题:::", response);
                        Log.e("====判断用户是否有未解答问题:::", Api.JUDGE_USER_EXERCISES);
                        Log.e("====判断用户是否有未解答问题:::", BaseApplication.getApplication().getToken());
                        Log.e("====判断用户是否有未解答问题:::", mType2 + "");
                        NoReleaseProblemModel mNoReleaseProblemModel = JSONObject.parseObject(response, NoReleaseProblemModel.class);
                        if (RequestType.SUCCESS.equals(mNoReleaseProblemModel.getStatus())) {
                            if (mNoReleaseProblemModel.getState().equals("0")) {
                                Intent mIntentSelectSubject = new Intent(Activity_InteractiveSolution2.this, Activity_SelectSubject.class);
                                mIntentSelectSubject.putExtra("type3", mType2);//	问题类型：1文化课问题 2棋类问题
                                mIntentSelectSubject.putExtra("interface", mType3);//界面：1首页互动答题 2我的学校互动答题
                                startActivity(mIntentSelectSubject);
                            } else if (mNoReleaseProblemModel.getState().equals("1")) {
                                IToast.show(Activity_InteractiveSolution2.this, "有未解答或未采纳的问题");
                            }
                        } else {
                            if (mNoReleaseProblemModel.getStatus().equals("9") || mNoReleaseProblemModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mNoReleaseProblemModel.getStatus(),
                                        Activity_InteractiveSolution2.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_InteractiveSolution2.this, "登录失效");
                                }
                            } else if (mNoReleaseProblemModel.getStatus().equals("0")) {
                                IToast.show(Activity_InteractiveSolution2.this, mNoReleaseProblemModel.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_interactive_solution2;
    }

    @BindView(R.id.ll_chess)
    LinearLayout mLlChess;

    @BindView(R.id.ll_find_tearch_grade)
    LinearLayout mLlFindTearchGrade;

    @BindView(R.id.ll_find_tearch_subject)
    LinearLayout mLlFindTearchSubject;

    @Override
    public void initTitleBar() {
        if (!HGTool.isEmpty(mType3)) {
            if (mType3.equals("2")) {
                mLlTopActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_top_activity_club));
                mTvTopTitle.setText("互动答题");
                mTvTopTitle.setTextColor(getResources().getColor(R.color.color333333));
            } else if (mType3.equals("1")) {
                mLlTopActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_top_activity));
                mTvTopTitle.setText("互动答题");
                mTvTopTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }
        if (!HGTool.isEmpty(mType2)) {
            if (mType2.equals("2")) {
                //棋类课问题
                mLlChess.setVisibility(View.VISIBLE);
                mLlFindTearchGrade.setVisibility(View.GONE);
                mLlFindTearchSubject.setVisibility(View.GONE);
            } else if (mType2.equals("1")) {
                //文化课问题
                mLlChess.setVisibility(View.GONE);
                mLlFindTearchGrade.setVisibility(View.VISIBLE);
                mLlFindTearchSubject.setVisibility(View.VISIBLE);
            }
        }
    }

    private LocalBroadcastManager localBroadcastManager;

    private LocalReceiver localReceiver;

    @Override
    protected void initData() {
        mType3 = getIntent().getStringExtra("type3");
        mType2 = getIntent().getStringExtra("type2");
        mLlAdoption.setVisibility(View.GONE);
        if (BaseApplication.getApplication().isClub().equals("3")
                || BaseApplication.getApplication().isClub().equals("2")
                || BaseApplication.getApplication().isSchool().equals("2")) {
            mLlTopRight.setVisibility(View.INVISIBLE);
        } else {
            mLlTopRight.setVisibility(View.VISIBLE);
        }
        ChessPop();
        GradePop();
        SubjectPop();
        initPulltoRefresh();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tangchaoke.yiyoubangjiao.online.answer");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra("tuisong");
            /**
             * 如果有新的消息，在此界面要 及时进行刷新
             */
            if (num.equals("online_answer")) {
                initOnlineAnswer();
            }
        }

    }

    /**
     * ========================================================问题========================================================
     */
    @Override
    protected void onResume() {
        super.onResume();
        initOnlineAnswer();
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putString("activity", "Activity_InteractiveSolution2");
        mEditor.commit();
    }

    private int mPageIndex = 0;

    private int mPageNum = 10;

    /**
     * 刷新
     */
    @BindView(R.id.pull_to_refresh)
    PullToRefreshLayout mPullToRefresh;

    @BindView(R.id.ll_no)
    LinearLayout mLlNo;

    private void initOnlineAnswer() {

        if (!HGTool.isEmpty(mSelectedGrade)) {
            if (mSelectedGrade.equals("默认")) {
                mSelectedGrade = "";
            }
        }

        if (mType2.equals("2")) {
            if (mChessPosC == -1 || mChessPosC == 0) {
                mChessType3 = "全部";
            } else {
                mChessType3 = String.valueOf(mChessPosC);
            }
        }

        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_InteractiveSolution2.this, "加载中",
                true, false, null);

        OkHttpUtils
                .post()
                .url(Api.SEARCH_EXERCISES)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("page.index", mPageIndex + "")
                .addParams("page.num", mPageNum + "")
                .addParams("grade", mSelectedGrade)
                .addParams("subject", mSelectedSubject)
                .addParams("type1", "Android")
                .addParams("type2", mType2 + "")
                .addParams("type3", mType3 + "")
                .addParams("chessSpecies", mChessType3 + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_InteractiveSolution2.this, "服务器开小差！请稍后重试");
                        Log.e("====在线问题列表：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====在线问题列表：：：", response);
                        Log.e("====在线问题列表：：：", Api.SEARCH_EXERCISES);
                        Log.e("====在线问题列表：：：", BaseApplication.getApplication().getToken());
                        Log.e("====在线问题列表：：：", mPageIndex + "::mPageIndex");
                        Log.e("====在线问题列表：：：", mPageNum + "::mPageNum");
                        Log.e("====在线问题列表：：：", mSelectedGrade + "::mSelectedGrade");
                        Log.e("====在线问题列表：：：", mSelectedSubject + "::mSelectedSubject");
                        Log.e("====在线问题列表：：：", "Android" + "::type1");
                        Log.e("====在线问题列表：：：", mType2 + "::type2");
                        Log.e("====在线问题列表：：：", mType3 + "::type3");
                        Log.e("====在线问题列表：：：", mChessType3 + "::chessSpecies");
                        mProgressHUD.dismiss();
                        ProblemBean mProblemBean = JSONObject.parseObject(response, ProblemBean.class);
                        mProgressHUD.dismiss();
                        if (RequestType.SUCCESS.equals(mProblemBean.getStatus())) {
                            if (mProblemBean.getModel().size() != 0) {
                                initRecyclerOnlineAnswer();
                                isShowNoData(false);
                                if (mPageIndex == 0) {
                                    mInteractiveSolution2Adapter.clear();
                                }
                                mProblemModelList.addAll(mProblemBean.getModel());
                            } else {
                                IToast.show(Activity_InteractiveSolution2.this, "暂无更多数据");
                                if (mPageIndex == 0) {
                                    isShowNoData(true);
                                }
                            }
                        } else {
                            if (mProblemBean.getStatus().equals("9") || mProblemBean.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mProblemBean.getStatus(),
                                        Activity_InteractiveSolution2.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_InteractiveSolution2.this, "登录失效");
                                }
                            } else if (mProblemBean.getStatus().equals("0")) {
                                IToast.show(Activity_InteractiveSolution2.this, mProblemBean.getMessage());
                            }
                        }
                    }
                });

    }

    private void initRecyclerOnlineAnswer() {
        mRecycler.setHasFixedSize(true);
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(Activity_InteractiveSolution2.this));
        mRecycler.setHasFixedSize(true);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mInteractiveSolution2Adapter = new InteractiveSolution2Adapter(Activity_InteractiveSolution2.this,
                mProblemModelList, new InteractiveSolution2Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (!HGTool.isEmpty(mProblemModelList.get(position).getUserOid())) {
                    if (!mProblemModelList.get(position).getUserOid().equals(BaseApplication.getApplication().getOid())) {
                        Intent mIntentOnlineAnswerInfo = new Intent(Activity_InteractiveSolution2.this,
                                Activity_OnlineAnswerInfo.class);
                        mIntentOnlineAnswerInfo.putExtra("oid", mProblemModelList.get(position).getOid());
                        mIntentOnlineAnswerInfo.putExtra("type3", mType3);//界面：1首页互动答题 2我的学校互动答题
                        mIntentOnlineAnswerInfo.putExtra("type2", mType2);//问题类型:1文化课问题 2棋类问题
                        startActivity(mIntentOnlineAnswerInfo);
                    } else {
                        Intent mIntentAnsweredInfo = new Intent(Activity_InteractiveSolution2.this,
                                Activity_AnsweredInfo.class);
                        mIntentAnsweredInfo.putExtra("oid", mProblemModelList.get(position).getOid());
                        mIntentAnsweredInfo.putExtra("type", "me");
                        startActivity(mIntentAnsweredInfo);
                    }
                }
            }
        });
        mRecycler.setAdapter(mInteractiveSolution2Adapter);
    }

    /**
     * 刷新界面
     */
    private void initPulltoRefresh() {
        mPullToRefresh.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                if (NetWorkUsefulUtils.getActiveNetwork(Activity_InteractiveSolution2.this)) {
                    mPageIndex = 0;
                    mPageNum = 10;
                    initOnlineAnswer();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    IToast.show(Activity_InteractiveSolution2.this, "" + getResources().getString(R.string.netNotUseful));
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                mPageIndex = mPageIndex + 10;
                mPageNum = 10;
                initOnlineAnswer();
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

    /**
     * ========================================================年级 科目 筛选 条件========================================================
     */
    PopupWindow mGradePopupWindow;

    private LinearLayout mLlSmall;

    private LinearLayout mLlAll;

    private TextView mTvAll;

    /**
     * 年级弹窗
     */
    private void GradePop() {
        View mPopView = LayoutInflater.from(Activity_InteractiveSolution2.this).inflate(R.layout.popup_window_find_teacher_grade, null);
        mLlAll = mPopView.findViewById(R.id.ll_all);
        mTvAll = mPopView.findViewById(R.id.tv_all);
        mLlAll.setBackgroundResource(R.drawable.shape_light_blue_fillet_square);
        mTvAll.setTextColor(getResources().getColor(R.color.colorPrimary));
        mLlAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGradePopupWindow.dismiss();
                mPageIndex = 0;
                mPageNum = 5;
                mSelectedGrade = "默认";
                mHighGradePos = -1;
                mInGradePos = -1;
                mSmallGradePos = -1;
                mSubjectPos = mSubjectPosC;
                GradePop();
                SubjectPop();
                initSmallGone(mSelectedSubject);
                initOnlineAnswer();
                mTvFindTearchGrade.setText("全部");
            }
        });
        mLlSmall = mPopView.findViewById(R.id.ll_small);
        mRecyclerFindTeacherGradeHighSchool = mPopView.findViewById(R.id.recycler_find_teacher_grade_high_school);
        initRecyclerFindTeacherGradeHighSchool(mRecyclerFindTeacherGradeHighSchool);
        mRecyclerFindTeacherGradeJuniorHighSchool = mPopView.findViewById(R.id.recycler_find_teacher_grade_junior_high_school);
        initRecyclerFindTeacherGradeJuniorHighSchool(mRecyclerFindTeacherGradeJuniorHighSchool);
        mRecyclerFindTeacherGradePrimarySchool = mPopView.findViewById(R.id.recycler_find_teacher_grade_primary_school);
        initRecyclerFindTeacherGradePrimarySchool(mRecyclerFindTeacherGradePrimarySchool);
        mGradePopupWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mGradePopupWindow.setAnimationStyle(R.style.style_pop_animation);
        mGradePopupWindow.setFocusable(false);
        mGradePopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    /**
     * 选择年级
     */
    String mSelectedGrade = "默认";

    /**
     * 小学
     */
    RecyclerView mRecyclerFindTeacherGradePrimarySchool;

    /**
     * 小学 年级适配器
     */
    FindTeacherSmallPopAdapter mFindTeacherSmallPopAdapter;

    /**
     * 小学 选中 标识
     */
    private int mSmallGradePos = -1;//保存当前选中的position 重点!

    /**
     * 年级 小学
     *
     * @param mRecyclerFindTeacherGradePrimarySchool
     */
    private void initRecyclerFindTeacherGradePrimarySchool(RecyclerView mRecyclerFindTeacherGradePrimarySchool) {
        mRecyclerFindTeacherGradePrimarySchool.setHasFixedSize(true);
        mRecyclerFindTeacherGradePrimarySchool.setNestedScrollingEnabled(false);
        mRecyclerFindTeacherGradePrimarySchool.setLayoutManager(new GridLayoutManager(Activity_InteractiveSolution2.this, 4));
        mRecyclerFindTeacherGradePrimarySchool.setHasFixedSize(true);
        mRecyclerFindTeacherGradePrimarySchool.setItemAnimator(new DefaultItemAnimator());
        mFindTeacherSmallPopAdapter = new FindTeacherSmallPopAdapter(mSmallGradePos, Activity_InteractiveSolution2.this,
                SubjectType.mGradePrimarySchool, new FindTeacherSmallPopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                mLlAll.setBackgroundResource(R.drawable.shape_light_gray_fillet_5);
                mTvAll.setTextColor(getResources().getColor(R.color.color999999));
                LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                TextView mTvReleaseGrade = view.findViewById(R.id.tv_release_grade);
                if (mSmallGradePos != position) {//当前选中的position和上次选中不是同一个position 执行
                    mLlItem.setBackgroundResource(R.drawable.shape_light_blue_fillet_square);
                    mTvReleaseGrade.setTextColor(getResources().getColor(R.color.colorPrimary));
                    if (mSmallGradePos != -1) {//判断是否有效 -1是初始值 即无效 第二个参数是Object 随便传个int 这里只是起个标志位
                        mFindTeacherSmallPopAdapter.notifyItemChanged(mSmallGradePos, 0);
                    }
                    mSmallGradePos = position;
                    mFindTeacherHigePopAdapter.notifyDataSetChanged();
                    mFindTeacherInPopAdapter.notifyDataSetChanged();
                    mSelectedGrade = SubjectType.mGradePrimarySchool.get(mSmallGradePos);
                    mSubjectPos = mSubjectPosC;
                    if (mSelectedGrade.equals("全部")) {
                        mSelectedGrade = mSelectedGrade + "小学";
                    }
                    initRecyclerFindTeacherSubject(mRecyclerFindTeacherSubject);
                    mTvFindTearchGrade.setText(mSelectedGrade);
                    mGradePopupWindow.dismiss();
                    initOnlineAnswer();
                }
            }
        });
        mRecyclerFindTeacherGradePrimarySchool.setAdapter(mFindTeacherSmallPopAdapter);
    }

    /**
     * 初中
     */
    RecyclerView mRecyclerFindTeacherGradeJuniorHighSchool;

    /**
     * 初中 年级适配器
     */
    FindTeacherInPopAdapter mFindTeacherInPopAdapter;

    /**
     * 初中 选中 标识
     */
    private int mInGradePos = -1;//保存当前选中的position 重点!

    /**
     * 年级 初中
     *
     * @param mRecyclerFindTeacherGradeJuniorHighSchool
     */
    private void initRecyclerFindTeacherGradeJuniorHighSchool(RecyclerView mRecyclerFindTeacherGradeJuniorHighSchool) {
        mRecyclerFindTeacherGradeJuniorHighSchool.setHasFixedSize(true);
        mRecyclerFindTeacherGradeJuniorHighSchool.setNestedScrollingEnabled(false);
        mRecyclerFindTeacherGradeJuniorHighSchool.setLayoutManager(new GridLayoutManager(Activity_InteractiveSolution2.this, 4));
        mRecyclerFindTeacherGradeJuniorHighSchool.setHasFixedSize(true);
        mRecyclerFindTeacherGradeJuniorHighSchool.setItemAnimator(new DefaultItemAnimator());
        mFindTeacherInPopAdapter = new FindTeacherInPopAdapter(mInGradePos, Activity_InteractiveSolution2.this,
                SubjectType.mGradeJuniorHighSchoolList, new FindTeacherInPopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                mLlAll.setBackgroundResource(R.drawable.shape_light_gray_fillet_5);
                mTvAll.setTextColor(getResources().getColor(R.color.color999999));
                LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                TextView mTvReleaseGrade = view.findViewById(R.id.tv_release_grade);
                if (mInGradePos != position) {//当前选中的position和上次选中不是同一个position 执行
                    mLlItem.setBackgroundResource(R.drawable.shape_light_blue_fillet_square);
                    mTvReleaseGrade.setTextColor(getResources().getColor(R.color.colorPrimary));
                    if (mInGradePos != -1) {//判断是否有效 -1是初始值 即无效 第二个参数是Object 随便传个int 这里只是起个标志位
                        mFindTeacherInPopAdapter.notifyItemChanged(mInGradePos, 0);
                    }
                    mInGradePos = position;
                    mFindTeacherHigePopAdapter.notifyDataSetChanged();
                    mFindTeacherSmallPopAdapter.notifyDataSetChanged();
                    mSelectedGrade = SubjectType.mGradeJuniorHighSchoolList.get(mInGradePos);
                    if (mSelectedGrade.equals("全部")) {
                        mSelectedGrade = mSelectedGrade + "初中";
                    }
                    mSubjectPos = mSubjectPosC;
                    initRecyclerFindTeacherSubject(mRecyclerFindTeacherSubject);
                    mTvFindTearchGrade.setText(mSelectedGrade);
                    mGradePopupWindow.dismiss();
                    initOnlineAnswer();
                }
            }
        });
        mRecyclerFindTeacherGradeJuniorHighSchool.setAdapter(mFindTeacherInPopAdapter);
    }

    /**
     * 高中
     */
    RecyclerView mRecyclerFindTeacherGradeHighSchool;

    /**
     * 高中 年级适配器
     */
    FindTeacherHigePopAdapter mFindTeacherHigePopAdapter;

    /**
     * 高中 选中 标识
     */
    private int mHighGradePos = -1;//保存当前选中的position 重点!

    /**
     * 年级 高中
     *
     * @param mRecyclerFindTeacherGradeHighSchool
     */
    private void initRecyclerFindTeacherGradeHighSchool(RecyclerView mRecyclerFindTeacherGradeHighSchool) {
        mRecyclerFindTeacherGradeHighSchool.setHasFixedSize(true);
        mRecyclerFindTeacherGradeHighSchool.setNestedScrollingEnabled(false);
        mRecyclerFindTeacherGradeHighSchool.setLayoutManager(new GridLayoutManager(Activity_InteractiveSolution2.this, 4));
        mRecyclerFindTeacherGradeHighSchool.setHasFixedSize(true);
        mRecyclerFindTeacherGradeHighSchool.setItemAnimator(new DefaultItemAnimator());
        mFindTeacherHigePopAdapter = new FindTeacherHigePopAdapter(mHighGradePos, Activity_InteractiveSolution2.this,
                SubjectType.mGradeHighSchoolList, new FindTeacherHigePopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                mLlAll.setBackgroundResource(R.drawable.shape_light_gray_fillet_5);
                mTvAll.setTextColor(getResources().getColor(R.color.color999999));
                LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                TextView mTvReleaseGrade = view.findViewById(R.id.tv_release_grade);
                if (mHighGradePos != position) {//当前选中的position和上次选中不是同一个position 执行
                    mLlItem.setBackgroundResource(R.drawable.shape_light_blue_fillet_square);
                    mTvReleaseGrade.setTextColor(getResources().getColor(R.color.colorPrimary));
                    if (mHighGradePos != -1) {//判断是否有效 -1是初始值 即无效 第二个参数是Object 随便传个int 这里只是起个标志位
                        mFindTeacherHigePopAdapter.notifyItemChanged(mHighGradePos, 0);
                    }
                    mHighGradePos = position;
                    mFindTeacherInPopAdapter.notifyDataSetChanged();
                    mFindTeacherSmallPopAdapter.notifyDataSetChanged();
                    mSelectedGrade = SubjectType.mGradeHighSchoolList.get(mHighGradePos);
                    if (mSelectedGrade.equals("全部")) {
                        mSelectedGrade = mSelectedGrade + "高中";
                    }
                    mSubjectPos = mSubjectPosC;
                    initRecyclerFindTeacherSubject(mRecyclerFindTeacherSubject);
                    mTvFindTearchGrade.setText(mSelectedGrade);
                    mGradePopupWindow.dismiss();
                    initOnlineAnswer();
                }
            }
        });
        mRecyclerFindTeacherGradeHighSchool.setAdapter(mFindTeacherHigePopAdapter);
    }

    PopupWindow mSubjectPopupWindow;

    RecyclerView mRecyclerFindTeacherSubject;

    FindTeacherPopSubjectAdapter mFindTeacherPopSubjectAdapter;

    /**
     * 科目弹窗
     */
    private void SubjectPop() {
        View mPopView = LayoutInflater.from(Activity_InteractiveSolution2.this).inflate(R.layout.popup_window_find_teacher_subject, null);
        mRecyclerFindTeacherSubject = mPopView.findViewById(R.id.recycler_find_teacher_subject);
        initRecyclerFindTeacherSubject(mRecyclerFindTeacherSubject);
        mSubjectPopupWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mSubjectPopupWindow.setAnimationStyle(R.style.style_pop_animation);
        mSubjectPopupWindow.setFocusable(false);
        mSubjectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    /**
     * 科目 选中 标识
     */
    private int mSubjectPos = -1;//保存当前选中的position 重点!

    private int mSubjectPosC = -1;//保存当前选中的position 重点!

    /**
     * 选择科目
     */
    String mSelectedSubject = "";

    private void initRecyclerFindTeacherSubject(RecyclerView mRecyclerFindTeacherSubject) {
        mRecyclerFindTeacherSubject.setHasFixedSize(true);
        mRecyclerFindTeacherSubject.setNestedScrollingEnabled(false);
        mRecyclerFindTeacherSubject.setLayoutManager(new GridLayoutManager(Activity_InteractiveSolution2.this, 6));
        mRecyclerFindTeacherSubject.setHasFixedSize(true);
        mRecyclerFindTeacherSubject.setItemAnimator(new DefaultItemAnimator());
        mFindTeacherPopSubjectAdapter = new FindTeacherPopSubjectAdapter(mSubjectPos, Activity_InteractiveSolution2.this,
                SubjectType.getList(mSelectedGrade), new FindTeacherPopSubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                TextView mTvReleaseGrade = view.findViewById(R.id.tv_release_grade);
                if (mSubjectPos != position) {
                    //当前选中的position和上次选中不是同一个position 执行
                    mLlItem.setBackgroundResource(R.drawable.shape_light_blue_fillet_square);
                    mTvReleaseGrade.setTextColor(getResources().getColor(R.color.colorPrimary));
                    if (mSubjectPos != -1) {//判断是否有效 -1是初始值 即无效 第二个参数是Object 随便传个int 这里只是起个标志位
                        mFindTeacherPopSubjectAdapter.notifyItemChanged(mSubjectPos, 0);
                    }
                    mSubjectPos = position;
                    mSelectedSubject = SubjectType.getList(mSelectedGrade).get(mSubjectPos);
                    mSubjectPosC = mSubjectPos;
                    initSmallGone(mSelectedSubject);
                    mTvFindTearchSubject.setText(mSelectedSubject);
                    mSubjectPopupWindow.dismiss();
                    initOnlineAnswer();
                }
            }
        });
        mRecyclerFindTeacherSubject.setAdapter(mFindTeacherPopSubjectAdapter);
    }

    private void initSmallGone(String mSelectedSubject) {
        /**
         * 如果选择的科目  该年级没有   就隐藏
         */
        if (mSelectedSubject.equals("历史")
                || mSelectedSubject.equals("政治")
                || mSelectedSubject.equals("地理")
                || mSelectedSubject.equals("生物")
                || mSelectedSubject.equals("化学")
                || mSelectedSubject.equals("物理")) {
            mLlSmall.setVisibility(View.GONE);
        } else if (mSelectedSubject.equals("全部")
                || mSelectedSubject.equals("语文")
                || mSelectedSubject.equals("英语")
                || mSelectedSubject.equals("数学")) {
            mLlSmall.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ========================================================棋类 筛选 条件========================================================
     */
    RecyclerView mRecyclerChess;

    AnswerPopChessAdapter mAnswerPopChessAdapter;

    PopupWindow mChessPopupWindow;

    private void ChessPop() {
        View mPopView = LayoutInflater.from(Activity_InteractiveSolution2.this).inflate(R.layout.popup_window_find_teacher_subject, null);
        mRecyclerChess = mPopView.findViewById(R.id.recycler_find_teacher_subject);
        initRecyclerChess(mRecyclerChess);
        mChessPopupWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mChessPopupWindow.setAnimationStyle(R.style.style_pop_animation);
        mChessPopupWindow.setFocusable(false);
        mChessPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    /**
     * 棋类 选中 标识
     */
    private int mChessPos = -1;//保存当前选中的position 重点!

    private int mChessPosC = -1;//保存当前选中的position 重点!

    /**
     * 选择棋类
     */
    String mSelectedChess = "";

    private void initRecyclerChess(RecyclerView mRecyclerChess) {
        mRecyclerChess.setHasFixedSize(true);
        mRecyclerChess.setNestedScrollingEnabled(false);
        mRecyclerChess.setLayoutManager(new GridLayoutManager(Activity_InteractiveSolution2.this, 4));
        mRecyclerChess.setHasFixedSize(true);
        mRecyclerChess.setItemAnimator(new DefaultItemAnimator());
        mAnswerPopChessAdapter = new AnswerPopChessAdapter(mChessPos, Activity_InteractiveSolution2.this,
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
                    initOnlineAnswer();
                }
            }
        });
        mRecyclerChess.setAdapter(mAnswerPopChessAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
            mEditor.putString("activity", "");
            mEditor.commit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
