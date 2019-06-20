package com.tangchaoke.yiyoubangjiao.activity;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.adapter.HighSchoolGradeAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.HighSchoolSubjectAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.JuniorHighSchoolGradeAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.JuniorHighSchoolSubjectAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.PrimarySchoolGradeAdapter;
import com.tangchaoke.yiyoubangjiao.adapter.PrimarySchoolSubjectAdapter;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.type.SubjectType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 选择推送题目范围
*/
public class Activity_PushRange extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    private String mType;

    /**
     * 小学 年级
     */
    @BindView(R.id.recycler_primary_school_grade)
    RecyclerView mRecyclerPrimarySchoolGrade;

    PrimarySchoolGradeAdapter mPrimarySchoolGradeAdapter;

    /**
     * 小学 科目
     */
    @BindView(R.id.recycler_primary_school_subject)
    RecyclerView mRecyclerPrimarySchoolSubject;

    PrimarySchoolSubjectAdapter mPrimarySchoolSubjectAdapter;

    /**
     * 初中 年级
     */
    @BindView(R.id.recycler_junior_high_school_grade)
    RecyclerView mRecyclerJuniorHighSchoolGrade;

    JuniorHighSchoolGradeAdapter mJuniorHighSchoolGradeAdapter;

    /**
     * 初中 科目
     */
    @BindView(R.id.recycler_junior_high_school_subject)
    RecyclerView mRecyclerJuniorHighSchoolSubject;

    JuniorHighSchoolSubjectAdapter mJuniorHighSchoolSubjectAdapter;

    /**
     * 高中 年级
     */
    @BindView(R.id.recycler_high_school_grade)
    RecyclerView mRecyclerHighSchoolGrade;

    HighSchoolGradeAdapter mHighSchoolGradeAdapter;

    /**
     * 高中 科目
     */
    @BindView(R.id.recycler_high_school_subject)
    RecyclerView mRecyclerHighSchoolSubject;

    HighSchoolSubjectAdapter mHighSchoolSubjectAdapter;

    private String mInfo;

    @OnClick({R.id.ll_back, R.id.but_upload})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.but_upload:
                /**
                 * 小学年级
                 */
                if (mPrimarySchoolGradeAdapter.getSelectedItem().size() != 0) {
                    for (int i = 0; i < mPrimarySchoolGradeAdapter.getSelectedItem().size(); i++) {
                        mStringBuilderPrimarySchoolGrade.append(SubjectType.getListIdentification(mPrimarySchoolGradeAdapter.getSelectedItem().get(i)).get(0) + ",");
                    }
                    mStringBuilderPrimarySchoolGrade.delete(mStringBuilderPrimarySchoolGrade.length() - 1, mStringBuilderPrimarySchoolGrade.length());
                    mEducationalFeaturesPrimarySchoolGrade = mStringBuilderPrimarySchoolGrade.toString();
                    Paint paint = new Paint();
                    float size = paint.measureText(mEducationalFeaturesPrimarySchoolGrade);
                    mStringBuilderPrimarySchoolGrade.delete(0, (int) size);
                } else {
                    mEducationalFeaturesPrimarySchoolGrade = "";
                }
                Log.e("==选中小学年级：：：", mEducationalFeaturesPrimarySchoolGrade + "");

                /**
                 * 小学科目
                 */
                if (mPrimarySchoolSubjectAdapter.getSelectedItem().size() != 0) {
                    for (int i = 0; i < mPrimarySchoolSubjectAdapter.getSelectedItem().size(); i++) {
                        mStringBuilderPrimarySchoolSubject.append(SubjectType.getListIdentification(mPrimarySchoolSubjectAdapter.getSelectedItem().get(i)).get(0) + ",");
                    }
                    mStringBuilderPrimarySchoolSubject.delete(mStringBuilderPrimarySchoolSubject.length() - 1, mStringBuilderPrimarySchoolSubject.length());
                    mEducationalFeaturesPrimarySchoolSubject = mStringBuilderPrimarySchoolSubject.toString();
                    Paint paint = new Paint();
                    float size = paint.measureText(mEducationalFeaturesPrimarySchoolSubject);
                    mStringBuilderPrimarySchoolSubject.delete(0, (int) size);
                } else {
                    mEducationalFeaturesPrimarySchoolSubject = "";
                }
                Log.e("==选中小学科目：：：", mEducationalFeaturesPrimarySchoolSubject + "");

                /**
                 * 初中年级
                 */
                if (mJuniorHighSchoolGradeAdapter.getSelectedItem().size() != 0) {
                    for (int i = 0; i < mJuniorHighSchoolGradeAdapter.getSelectedItem().size(); i++) {
                        mStringJuniorHighSchoolGrade.append(SubjectType.getListIdentification(mJuniorHighSchoolGradeAdapter.getSelectedItem().get(i)).get(0) + ",");
                    }
                    mStringJuniorHighSchoolGrade.delete(mStringJuniorHighSchoolGrade.length() - 1, mStringJuniorHighSchoolGrade.length());
                    mEducationalJuniorHighSchoolGrade = mStringJuniorHighSchoolGrade.toString();
                    Paint paint = new Paint();
                    float size = paint.measureText(mEducationalJuniorHighSchoolGrade);
                    mStringJuniorHighSchoolGrade.delete(0, (int) size);
                } else {
                    mEducationalJuniorHighSchoolGrade = "";
                }
                Log.e("==选中初中年级：：：", mEducationalJuniorHighSchoolGrade + "");

                /**
                 * 初中科目
                 */
                if (mJuniorHighSchoolSubjectAdapter.getSelectedItem().size() != 0) {
                    for (int i = 0; i < mJuniorHighSchoolSubjectAdapter.getSelectedItem().size(); i++) {
                        mStringJuniorHighSchoolSubject.append(SubjectType.getListIdentification(mJuniorHighSchoolSubjectAdapter.getSelectedItem().get(i)).get(0) + ",");
                    }
                    mStringJuniorHighSchoolSubject.delete(mStringJuniorHighSchoolSubject.length() - 1, mStringJuniorHighSchoolSubject.length());
                    mEducationalJuniorHighSchoolSubject = mStringJuniorHighSchoolSubject.toString();
                    Paint paint = new Paint();
                    float size = paint.measureText(mEducationalJuniorHighSchoolSubject);
                    mStringJuniorHighSchoolSubject.delete(0, (int) size);
                } else {
                    mEducationalJuniorHighSchoolSubject = "";
                }
                Log.e("==选中初中科目：：：", mEducationalJuniorHighSchoolSubject + "");

                /**
                 * 高中年级
                 */
                if (mHighSchoolGradeAdapter.getSelectedItem().size() != 0) {
                    for (int i = 0; i < mHighSchoolGradeAdapter.getSelectedItem().size(); i++) {
                        mStringHighSchoolGrade.append(SubjectType.getListIdentification(mHighSchoolGradeAdapter.getSelectedItem().get(i)).get(0) + ",");
                    }
                    mStringHighSchoolGrade.delete(mStringHighSchoolGrade.length() - 1, mStringHighSchoolGrade.length());
                    mEducationalHighSchoolGrade = mStringHighSchoolGrade.toString();
                    Paint paint = new Paint();
                    float size = paint.measureText(mEducationalHighSchoolGrade);
                    mStringHighSchoolGrade.delete(0, (int) size);
                } else {
                    mEducationalHighSchoolGrade = "";
                }
                Log.e("==选中高中年级：：：", mEducationalHighSchoolGrade + "");

                /**
                 * 高中科目
                 */
                if (mHighSchoolSubjectAdapter.getSelectedItem().size() != 0) {
                    for (int i = 0; i < mHighSchoolSubjectAdapter.getSelectedItem().size(); i++) {
                        mStringHighSchoolSubject.append(SubjectType.getListIdentification(mHighSchoolSubjectAdapter.getSelectedItem().get(i)).get(0) + ",");
                    }
                    mStringHighSchoolSubject.delete(mStringHighSchoolSubject.length() - 1, mStringHighSchoolSubject.length());
                    mEducationalHighSchoolSubject = mStringHighSchoolSubject.toString();
                    Paint paint = new Paint();
                    float size = paint.measureText(mEducationalHighSchoolSubject);
                    mStringHighSchoolSubject.delete(0, (int) size);
                } else {
                    mEducationalHighSchoolSubject = "";
                }
                initPushRange(mEducationalFeaturesPrimarySchoolGrade,
                        mEducationalFeaturesPrimarySchoolSubject,
                        mEducationalJuniorHighSchoolGrade,
                        mEducationalJuniorHighSchoolSubject,
                        mEducationalHighSchoolGrade,
                        mEducationalHighSchoolSubject);
                break;

        }
    }

    private void initPushRange(final String mEducationalFeaturesPrimarySchoolGrade,
                               final String mEducationalFeaturesPrimarySchoolSubject,
                               final String mEducationalJuniorHighSchoolGrade,
                               final String mEducationalJuniorHighSchoolSubject,
                               final String mEducationalHighSchoolGrade,
                               final String mEducationalHighSchoolSubject) {

        if (HGTool.isEmpty(mEducationalFeaturesPrimarySchoolGrade)
                && HGTool.isEmpty(mEducationalFeaturesPrimarySchoolSubject)) {
            if (HGTool.isEmpty(mEducationalJuniorHighSchoolGrade)
                    && HGTool.isEmpty(mEducationalJuniorHighSchoolSubject)) {
                if (HGTool.isEmpty(mEducationalHighSchoolGrade)
                        && HGTool.isEmpty(mEducationalHighSchoolSubject)) {
                    IToast.show(Activity_PushRange.this, "请选择教学年级和课程（年级和科目分别各必须选择一项）");
                    return;
                }
            }
        }

        if (!HGTool.isEmpty(mEducationalFeaturesPrimarySchoolGrade)
                && HGTool.isEmpty(mEducationalFeaturesPrimarySchoolSubject)) {
            IToast.show(Activity_PushRange.this, "请选择小学科目");
            return;
        }

        if (HGTool.isEmpty(mEducationalFeaturesPrimarySchoolGrade)
                && !HGTool.isEmpty(mEducationalFeaturesPrimarySchoolSubject)) {
            IToast.show(Activity_PushRange.this, "请选择小学年级");
            return;
        }

        if (!HGTool.isEmpty(mEducationalJuniorHighSchoolGrade)
                && HGTool.isEmpty(mEducationalJuniorHighSchoolSubject)) {
            IToast.show(Activity_PushRange.this, "请选择初中科目");
            return;
        }

        if (HGTool.isEmpty(mEducationalJuniorHighSchoolGrade)
                && !HGTool.isEmpty(mEducationalJuniorHighSchoolSubject)) {
            IToast.show(Activity_PushRange.this, "请选择初中年级");
            return;
        }

        if (!HGTool.isEmpty(mEducationalHighSchoolGrade)
                && HGTool.isEmpty(mEducationalHighSchoolSubject)) {
            IToast.show(Activity_PushRange.this, "请选择高中科目");
            return;
        }

        if (HGTool.isEmpty(mEducationalHighSchoolGrade)
                && !HGTool.isEmpty(mEducationalHighSchoolSubject)) {
            IToast.show(Activity_PushRange.this, "请选择高中年级");
            return;
        }

        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_PushRange.this,
                "正在提交", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.SET_RESPONDENT_RANGE)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("grade1", mEducationalFeaturesPrimarySchoolGrade)
                .addParams("subject1", mEducationalFeaturesPrimarySchoolSubject)
                .addParams("grade2", mEducationalJuniorHighSchoolGrade)
                .addParams("subject2", mEducationalJuniorHighSchoolSubject)
                .addParams("grade3", mEducationalHighSchoolGrade)
                .addParams("subject3", mEducationalHighSchoolSubject)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_PushRange.this, "服务器开小差！请稍后重试");
                        Log.e("==选择推送题目范围", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==选择推送题目范围", response);
                        Log.e("==选择推送题目范围", Api.SET_RESPONDENT_RANGE);
                        Log.e("==选择推送题目范围", BaseApplication.getApplication().getToken());
                        Log.e("==选择推送题目范围", mEducationalFeaturesPrimarySchoolGrade);
                        Log.e("==选择推送题目范围", mEducationalFeaturesPrimarySchoolSubject);
                        Log.e("==选择推送题目范围", mEducationalJuniorHighSchoolGrade);
                        Log.e("==选择推送题目范围", mEducationalJuniorHighSchoolSubject);
                        Log.e("==选择推送题目范围", mEducationalHighSchoolGrade);
                        Log.e("==选择推送题目范围", mEducationalHighSchoolSubject);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (mSuccessModel.getStatus().equals(RequestType.SUCCESS)) {
                            if (!HGTool.isEmpty(mInfo)) {
                                SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                                //已设置推送范围
                                mEditor.putString("pushRange", "1");
                                mEditor.commit();
                                mProgressHUD.dismiss();
                                IToast.show(Activity_PushRange.this, mSuccessModel.getMessage());
                                finish();
                            } else {
                                initOpen(mProgressHUD);
                            }
                        } else {
                            mProgressHUD.dismiss();
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(),
                                        Activity_PushRange.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_PushRange.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_PushRange.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });

    }

    private void initOpen(final ProgressHUD mProgressHUD) {
        OkHttpUtils
                .post()
                .url(Api.BEING_TUTOR)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("type", mType)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_PushRange.this, "服务器开小差！请稍后重试");
                        mProgressHUD.dismiss();
                        Log.e("==开通身份", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        mProgressHUD.dismiss();
                        Log.e("==开通身份", response);
                        Log.e("==开通身份", Api.BEING_TUTOR);
                        Log.e("==开通身份", BaseApplication.getApplication().getToken());
                        Log.e("==开通身份", mType);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            mProgressHUD.dismiss();
                            SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                            //已设置推送范围
                            mEditor.putString("pushRange", "1");
                            if (mType.equals("2")) {
                                mEditor.putString("respondent", "1");
                            } else if (mType.equals("1")) {
                                mEditor.putString("substituteTeacher", "1");
                            } else if (mType.equals("0")) {
                                mEditor.putString("tutor", "1");
                            }
                            mEditor.commit();
                            IToast.show(Activity_PushRange.this, mSuccessModel.getMessage());
                            finish();
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                mProgressHUD.dismiss();
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(),
                                        Activity_PushRange.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_PushRange.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                mProgressHUD.dismiss();
                                IToast.show(Activity_PushRange.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_push_range;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("选择推送题目范围");
    }

    @Override
    protected void initData() {
        mType = getIntent().getStringExtra("type");
        mInfo = getIntent().getStringExtra("info");
        initRecyclerPrimarySchoolGrade();
        initPrimarySchoolSubject();
        initRecyclerJuniorHighSchoolGrade();
        initJuniorHighSchoolSubject();
        initRecyclerHighSchoolGrade();
        initHighSchoolSubject();
    }

    StringBuilder mStringBuilderPrimarySchoolGrade = new StringBuilder();

    /**
     * 上传
     */
    String mEducationalFeaturesPrimarySchoolGrade = "";

    /**
     * 小学 年级
     */
    private void initRecyclerPrimarySchoolGrade() {
        mRecyclerPrimarySchoolGrade.setHasFixedSize(true);
        mRecyclerPrimarySchoolGrade.setNestedScrollingEnabled(false);
        mRecyclerPrimarySchoolGrade.setLayoutManager(new GridLayoutManager(Activity_PushRange.this, 4));
        mRecyclerPrimarySchoolGrade.setHasFixedSize(true);
        mRecyclerPrimarySchoolGrade.setItemAnimator(new DefaultItemAnimator());
        mPrimarySchoolGradeAdapter = new PrimarySchoolGradeAdapter(Activity_PushRange.this, SubjectType.mGradePrimarySchoolNo, new PrimarySchoolGradeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                TextView mTvName = view.findViewById(R.id.tv_name);
                if (mPrimarySchoolGradeAdapter.isItemChecked(position)) {
                    mPrimarySchoolGradeAdapter.setItemChecked(position, false);
                    mLlItem.setBackgroundResource(R.drawable.shape_light_gray_fillet_5);
                    mTvName.setTextColor(getResources().getColor(R.color.color666666));
                } else {
                    mPrimarySchoolGradeAdapter.setItemChecked(position, true);
                    mLlItem.setBackgroundResource(R.drawable.shape_light_blue_fillet_square);
                    mTvName.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        mRecyclerPrimarySchoolGrade.setAdapter(mPrimarySchoolGradeAdapter);
    }

    StringBuilder mStringBuilderPrimarySchoolSubject = new StringBuilder();

    /**
     * 上传
     */
    String mEducationalFeaturesPrimarySchoolSubject = "";

    /**
     * 小学 科目
     */
    private void initPrimarySchoolSubject() {
        mRecyclerPrimarySchoolSubject.setHasFixedSize(true);
        mRecyclerPrimarySchoolSubject.setNestedScrollingEnabled(false);
        mRecyclerPrimarySchoolSubject.setLayoutManager(new GridLayoutManager(Activity_PushRange.this, 4));
        mRecyclerPrimarySchoolSubject.setHasFixedSize(true);
        mRecyclerPrimarySchoolSubject.setItemAnimator(new DefaultItemAnimator());
        mPrimarySchoolSubjectAdapter = new PrimarySchoolSubjectAdapter(Activity_PushRange.this, SubjectType.mFirstGradeListNo, new PrimarySchoolSubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                TextView mTvName = view.findViewById(R.id.tv_name);
                if (mPrimarySchoolSubjectAdapter.isItemChecked(position)) {
                    mPrimarySchoolSubjectAdapter.setItemChecked(position, false);
                    mLlItem.setBackgroundResource(R.drawable.shape_light_gray_fillet_5);
                    mTvName.setTextColor(getResources().getColor(R.color.color666666));
                } else {
                    mPrimarySchoolSubjectAdapter.setItemChecked(position, true);
                    mLlItem.setBackgroundResource(R.drawable.shape_light_blue_fillet_square);
                    mTvName.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
//                mPrimarySchoolSubjectAdapter.notifyItemChanged(position);
            }
        });
        mRecyclerPrimarySchoolSubject.setAdapter(mPrimarySchoolSubjectAdapter);
    }

    StringBuilder mStringJuniorHighSchoolGrade = new StringBuilder();

    /**
     * 上传
     */
    String mEducationalJuniorHighSchoolGrade = "";

    /**
     * 初中 年级
     */
    private void initRecyclerJuniorHighSchoolGrade() {
        mRecyclerJuniorHighSchoolGrade.setHasFixedSize(true);
        mRecyclerJuniorHighSchoolGrade.setNestedScrollingEnabled(false);
        mRecyclerJuniorHighSchoolGrade.setLayoutManager(new GridLayoutManager(Activity_PushRange.this, 4));
        mRecyclerJuniorHighSchoolGrade.setHasFixedSize(true);
        mRecyclerJuniorHighSchoolGrade.setItemAnimator(new DefaultItemAnimator());
        mJuniorHighSchoolGradeAdapter = new JuniorHighSchoolGradeAdapter(Activity_PushRange.this, SubjectType.mGradeJuniorHighSchoolListNo, new JuniorHighSchoolGradeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                TextView mTvName = view.findViewById(R.id.tv_name);
                if (mJuniorHighSchoolGradeAdapter.isItemChecked(position)) {
                    mJuniorHighSchoolGradeAdapter.setItemChecked(position, false);
                    mLlItem.setBackgroundResource(R.drawable.shape_light_gray_fillet_5);
                    mTvName.setTextColor(getResources().getColor(R.color.color666666));
                } else {
                    mJuniorHighSchoolGradeAdapter.setItemChecked(position, true);
                    mLlItem.setBackgroundResource(R.drawable.shape_light_blue_fillet_square);
                    mTvName.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
//                mJuniorHighSchoolGradeAdapter.notifyItemChanged(position);
            }
        });
        mRecyclerJuniorHighSchoolGrade.setAdapter(mJuniorHighSchoolGradeAdapter);
    }

    StringBuilder mStringJuniorHighSchoolSubject = new StringBuilder();

    /**
     * 上传
     */
    String mEducationalJuniorHighSchoolSubject = "";

    /**
     * 初中 科目
     */
    private void initJuniorHighSchoolSubject() {
        mRecyclerJuniorHighSchoolSubject.setHasFixedSize(true);
        mRecyclerJuniorHighSchoolSubject.setNestedScrollingEnabled(false);
        mRecyclerJuniorHighSchoolSubject.setLayoutManager(new GridLayoutManager(Activity_PushRange.this, 4));
        mRecyclerJuniorHighSchoolSubject.setHasFixedSize(true);
        mRecyclerJuniorHighSchoolSubject.setItemAnimator(new DefaultItemAnimator());
        mJuniorHighSchoolSubjectAdapter = new JuniorHighSchoolSubjectAdapter(Activity_PushRange.this, SubjectType.mDefaultListNo, new JuniorHighSchoolSubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                TextView mTvName = view.findViewById(R.id.tv_name);
                if (mJuniorHighSchoolSubjectAdapter.isItemChecked(position)) {
                    mJuniorHighSchoolSubjectAdapter.setItemChecked(position, false);
                    mLlItem.setBackgroundResource(R.drawable.shape_light_gray_fillet_5);
                    mTvName.setTextColor(getResources().getColor(R.color.color666666));
                } else {
                    mJuniorHighSchoolSubjectAdapter.setItemChecked(position, true);
                    mLlItem.setBackgroundResource(R.drawable.shape_light_blue_fillet_square);
                    mTvName.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
//                mJuniorHighSchoolSubjectAdapter.notifyItemChanged(position);
            }
        });
        mRecyclerJuniorHighSchoolSubject.setAdapter(mJuniorHighSchoolSubjectAdapter);
    }

    StringBuilder mStringHighSchoolGrade = new StringBuilder();

    /**
     * 上传
     */
    String mEducationalHighSchoolGrade = "";

    /**
     * 高中 年级
     */
    private void initRecyclerHighSchoolGrade() {
        mRecyclerHighSchoolGrade.setHasFixedSize(true);
        mRecyclerHighSchoolGrade.setNestedScrollingEnabled(false);
        mRecyclerHighSchoolGrade.setLayoutManager(new GridLayoutManager(Activity_PushRange.this, 4));
        mRecyclerHighSchoolGrade.setHasFixedSize(true);
        mRecyclerHighSchoolGrade.setItemAnimator(new DefaultItemAnimator());
        mHighSchoolGradeAdapter = new HighSchoolGradeAdapter(Activity_PushRange.this, SubjectType.mGradeHighSchoolListNo, new HighSchoolGradeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                TextView mTvName = view.findViewById(R.id.tv_name);
                if (mHighSchoolGradeAdapter.isItemChecked(position)) {
                    mHighSchoolGradeAdapter.setItemChecked(position, false);
                    mLlItem.setBackgroundResource(R.drawable.shape_light_gray_fillet_5);
                    mTvName.setTextColor(getResources().getColor(R.color.color666666));
                } else {
                    mHighSchoolGradeAdapter.setItemChecked(position, true);
                    mLlItem.setBackgroundResource(R.drawable.shape_light_blue_fillet_square);
                    mTvName.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
//                mHighSchoolGradeAdapter.notifyItemChanged(position);
            }
        });
        mRecyclerHighSchoolGrade.setAdapter(mHighSchoolGradeAdapter);
    }

    StringBuilder mStringHighSchoolSubject = new StringBuilder();

    /**
     * 上传
     */
    String mEducationalHighSchoolSubject = "";

    /**
     * 高中 科目
     */
    private void initHighSchoolSubject() {
        mRecyclerHighSchoolSubject.setHasFixedSize(true);
        mRecyclerHighSchoolSubject.setNestedScrollingEnabled(false);
        mRecyclerHighSchoolSubject.setLayoutManager(new GridLayoutManager(Activity_PushRange.this, 4));
        mRecyclerHighSchoolSubject.setHasFixedSize(true);
        mRecyclerHighSchoolSubject.setItemAnimator(new DefaultItemAnimator());
        mHighSchoolSubjectAdapter = new HighSchoolSubjectAdapter(Activity_PushRange.this, SubjectType.mDefaultListNo, new HighSchoolSubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                LinearLayout mLlItem = view.findViewById(R.id.ll_onclick);
                TextView mTvName = view.findViewById(R.id.tv_name);
                if (mHighSchoolSubjectAdapter.isItemChecked(position)) {
                    mHighSchoolSubjectAdapter.setItemChecked(position, false);
                    mLlItem.setBackgroundResource(R.drawable.shape_light_gray_fillet_5);
                    mTvName.setTextColor(getResources().getColor(R.color.color666666));
                } else {
                    mHighSchoolSubjectAdapter.setItemChecked(position, true);
                    mLlItem.setBackgroundResource(R.drawable.shape_light_blue_fillet_square);
                    mTvName.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
//                mHighSchoolSubjectAdapter.notifyItemChanged(position);
            }
        });
        mRecyclerHighSchoolSubject.setAdapter(mHighSchoolSubjectAdapter);
    }

}
