package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.ReleaseChessAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.ReleaseGradeAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.ReleaseSubjectAdapter;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.type.SubjectType;

import butterknife.BindView;
import butterknife.OnClick;

/*
* @author hg
* create at 2019/1/2
* description: 发布问题选择班级 年级
*/
public class Activity_SelectSubject extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    /**
     * 发布问题  年级
     */
    @BindView(R.id.recycler_select_subject_grade)
    RecyclerView mReleaseRecyclerGrade;
    /**
     * 年级  适配器
     */
    ReleaseGradeAdapter mReleaseGradeAdapter;

    /**
     * 发布问题  科目   （科目是根据年级变化的）
     */
    @BindView(R.id.recycler_select_subject)
    RecyclerView mReleaseRecyclerSubject;
    /**
     * 科目 适配器
     */
    ReleaseSubjectAdapter mReleaseSubjectAdapter;

    /**
     * 发布问题  科目   （科目是根据年级变化的）
     */
    @BindView(R.id.recycler_select_chess)
    RecyclerView mReleaseRecyclerChess;

    //问题类型：1文化课问题 2棋类问题
    private String mType3 = "";

    @BindView(R.id.ll_select_grade)
    LinearLayout mLlSelectGrade;

    @BindView(R.id.ll_select_subject)
    LinearLayout mLlSelectSubject;

    @BindView(R.id.ll_select_chess)
    LinearLayout mLlSelectChess;

    private String mInterface = "";

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
        return R.layout.activity_select_subject;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("发布问题");
        if (!HGTool.isEmpty(mType3)) {
            if (mType3.equals("2")) {
                //棋类课问题
                mLlSelectChess.setVisibility(View.VISIBLE);
                mLlSelectGrade.setVisibility(View.GONE);
                mLlSelectSubject.setVisibility(View.GONE);
            } else if (mType3.equals("1")) {
                //文化课问题
                mLlSelectChess.setVisibility(View.GONE);
                mLlSelectGrade.setVisibility(View.VISIBLE);
                mLlSelectSubject.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void initData() {
        mType3 = getIntent().getStringExtra("type3");
        mInterface = getIntent().getStringExtra("interface");
        initReleaseRecyclerGrade();
        initReleaseRecyclerSubject();
        initReleaseRecyclerChess();
    }

    /**
     * 年级选中标识
     */
    private int mGradePos = -1;//保存当前选中的position 重点!

    /**
     * 接收 选中  的  年级
     */
    String mSelectedGrade = "";

    private void initReleaseRecyclerGrade() {
        mReleaseRecyclerGrade.setHasFixedSize(true);
        mReleaseRecyclerGrade.setNestedScrollingEnabled(false);
        mReleaseRecyclerGrade.setLayoutManager(new GridLayoutManager(Activity_SelectSubject.this, 3));
        mReleaseRecyclerGrade.setHasFixedSize(true);
        mReleaseRecyclerGrade.setItemAnimator(new DefaultItemAnimator());
        mReleaseGradeAdapter = new ReleaseGradeAdapter(mGradePos, Activity_SelectSubject.this, SubjectType.mGradeList, new ReleaseGradeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                TextView mTvReleaseGrade = view.findViewById(R.id.tv_release_grade);
                if (mGradePos != position) {//当前选中的position和上次选中不是同一个position 执行
                    mLlItem.setBackgroundResource(R.drawable.select_blue_gradient_fillet_20);
                    mTvReleaseGrade.setTextColor(getResources().getColor(R.color.colorPrimary));
                    if (mGradePos != -1) {//判断是否有效 -1是初始值 即无效 第二个参数是Object 随便传个int 这里只是起个标志位
                        mReleaseGradeAdapter.notifyItemChanged(mGradePos, 0);
                    }
                    mGradePos = position;
                    /**
                     * 获取选中的年级
                     */
                    mSelectedGrade = SubjectType.mGradeList.get(mGradePos);
                    mSelectedSubject = "";
                    mSubjectPos = -1;
                    /**
                     * 刷新 科目列表  科目是跟着 选择的年级变化的
                     */
                    initReleaseRecyclerSubject();
                }
            }
        });
        mReleaseRecyclerGrade.setAdapter(mReleaseGradeAdapter);
    }

    /**
     * 科目选中标识
     */
    private int mSubjectPos = -1;//保存当前选中的position 重点!

    /**
     * 接收 选中的 科目
     */
    String mSelectedSubject = "";

    private void initReleaseRecyclerSubject() {
        mReleaseRecyclerSubject.setHasFixedSize(true);
        mReleaseRecyclerSubject.setNestedScrollingEnabled(false);
        mReleaseRecyclerSubject.setLayoutManager(new GridLayoutManager(Activity_SelectSubject.this, 3));
        mReleaseRecyclerSubject.setHasFixedSize(true);
        mReleaseRecyclerSubject.setItemAnimator(new DefaultItemAnimator());
        mReleaseSubjectAdapter = new ReleaseSubjectAdapter(mSubjectPos, Activity_SelectSubject.this, SubjectType.getListPop(mSelectedGrade), new ReleaseSubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                TextView mTvReleaseGrade = view.findViewById(R.id.tv_release_grade);
                if (mSubjectPos != position) {//当前选中的position和上次选中不是同一个position 执行
                    mLlItem.setBackgroundResource(R.drawable.select_blue_gradient_fillet_20);
                    mTvReleaseGrade.setTextColor(getResources().getColor(R.color.colorPrimary));
                    if (mSubjectPos != -1) {//判断是否有效 -1是初始值 即无效 第二个参数是Object 随便传个int 这里只是起个标志位
                        mReleaseSubjectAdapter.notifyItemChanged(mSubjectPos, 0);
                    }
                    mSubjectPos = position;
                    /**
                     * 获取选中的 科目
                     */
                    mSelectedSubject = SubjectType.getListPop(mSelectedGrade).get(mSubjectPos);

                    Intent mIntentRelease = new Intent(Activity_SelectSubject.this, Activity_Release.class);
                    mIntentRelease.putExtra("grade", mSelectedGrade);//选择的年级
                    mIntentRelease.putExtra("subject", mSelectedSubject);//选择的科目
                    mIntentRelease.putExtra("type3", mType3);//	问题类型：1文化课问题 2棋类问题
                    mIntentRelease.putExtra("interface", mInterface);//界面：1首页互动答题 2我的学校互动答题
                    startActivity(mIntentRelease);
                    addActivity(Activity_SelectSubject.this);
                }
            }
        });
        mReleaseRecyclerSubject.setAdapter(mReleaseSubjectAdapter);
    }

    /**
     * 科目选中标识
     */
    private int mChessPos = -1;//保存当前选中的position 重点!

    /**
     * 接收 选中的 科目
     */
    String mSelectedChess = "";

    private void initReleaseRecyclerChess() {
        mReleaseRecyclerChess.setHasFixedSize(true);
        mReleaseRecyclerChess.setNestedScrollingEnabled(false);
        mReleaseRecyclerChess.setLayoutManager(new GridLayoutManager(Activity_SelectSubject.this, 2));
        mReleaseRecyclerChess.setHasFixedSize(true);
        mReleaseRecyclerChess.setItemAnimator(new DefaultItemAnimator());
        mReleaseSubjectAdapter = new ReleaseSubjectAdapter(mChessPos, Activity_SelectSubject.this,
                SubjectType.mChessList1, new ReleaseSubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                TextView mTvReleaseGrade = view.findViewById(R.id.tv_release_grade);
                if (mChessPos != position) {//当前选中的position和上次选中不是同一个position 执行
                    mLlItem.setBackgroundResource(R.drawable.select_blue_gradient_fillet_20);
                    mTvReleaseGrade.setTextColor(getResources().getColor(R.color.colorPrimary));
                    if (mChessPos != -1) {//判断是否有效 -1是初始值 即无效 第二个参数是Object 随便传个int 这里只是起个标志位
                        mReleaseSubjectAdapter.notifyItemChanged(mChessPos, 0);
                    }
                    mChessPos = position;
                    /**
                     * 获取选中的 科目
                     */
                    mSelectedChess = SubjectType.mChessList1.get(mChessPos);

                    Intent mIntentRelease = new Intent(Activity_SelectSubject.this, Activity_Release.class);
                    mIntentRelease.putExtra("chessSpecies", mSelectedChess);//选择的棋种
                    mIntentRelease.putExtra("type3", mType3);//	问题类型：1文化课问题 2棋类问题
                    mIntentRelease.putExtra("interface", mInterface);//界面：1首页互动答题 2我的学校互动答题
                    startActivity(mIntentRelease);
                    addActivity(Activity_SelectSubject.this);
                }
            }
        });
        mReleaseRecyclerChess.setAdapter(mReleaseSubjectAdapter);
    }

}
