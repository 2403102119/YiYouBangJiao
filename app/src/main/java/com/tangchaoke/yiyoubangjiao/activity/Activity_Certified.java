package com.tangchaoke.yiyoubangjiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.CertificationInformationModel;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.utils.DataCleanUtils;
import com.tangchaoke.yiyoubangjiao.view.CleanUpCacheDialogView;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.InputBoxDialogView;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 认证
*/
public class Activity_Certified extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    /**
     * 1 家教 2 代课老师 3 答题者
     */
    private String mTag = "";

    /**
     * 上传 返回 认证 类型
     */
    private String mType = "";

    /**
     * 提示 二选一  成人学历认证  学生认证
     */
    @BindView(R.id.tv_prompt_pick_one_of_two)
    TextView mTvPromptPickOneOfTwo;

    /**
     * 成人学历认证
     */
    @BindView(R.id.fl_academic_certificate)
    FrameLayout mFlAcademicCertificate;

    /**
     * 在校学生证认证
     */
    @BindView(R.id.fl_student_id_certification)
    FrameLayout mFlStudentIDCertification;

    /**
     * 学历已认证
     */
    @BindView(R.id.img_academic_certificate_verified)
    ImageView mImgAcademicCertificateVerified;

    /**
     * 在校学生证认证
     */
    @BindView(R.id.img_student_id_certification)
    ImageView mImgStudentIDCertification;

    @BindView(R.id.but_open)
    Button mButOpen;

    @BindView(R.id.ll_academic_certificate_shadow)
    LinearLayout mLlAcademicCertificateShadow;

    @BindView(R.id.ll_student_id_certification_shadow)
    LinearLayout mLlStudentIDCertificationShadow;

    /**
     * @param view
     */
    @OnClick({R.id.ll_back, R.id.fl_academic_certificate, R.id.fl_student_id_certification, R.id.but_open})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                mEditor.putString("activity", "");
                mEditor.commit();
                break;

            /**
             * 成人学历认证
             */
            case R.id.fl_academic_certificate:
                if (mCertificationInformationModel.getModel().getStudentIdCardStatus().equals("0")) {
                    IToast.show(Activity_Certified.this, "资料正在审核中，请耐心等待");
                } else if (mCertificationInformationModel.getModel().getStudentIdCardStatus().equals("2")) {
                    IToast.show(Activity_Certified.this, "在校学生认证成功");
                } else {
                    if (!HGTool.isEmpty(mCertificationInformationModel.getModel().getUserDiplomaStatus())) {
                        if (mCertificationInformationModel.getModel().getUserDiplomaStatus().equals("3")) {
                            Intent mIntentAcademicCertificate = new Intent(Activity_Certified.this, Activity_AcademicCertificate.class);
                            startActivity(mIntentAcademicCertificate);
                        } else if (mCertificationInformationModel.getModel().getUserDiplomaStatus().equals("1")) {
                            Intent mIntentAcademicCertificate = new Intent(Activity_Certified.this, Activity_AcademicCertificate.class);
                            mIntentAcademicCertificate.putExtra("type", "1");
                            startActivity(mIntentAcademicCertificate);
                        } else if (mCertificationInformationModel.getModel().getUserDiplomaStatus().equals("0")) {
                            IToast.show(Activity_Certified.this, "资料正在审核中，请耐心等待");
                        } else if (mCertificationInformationModel.getModel().getUserDiplomaStatus().equals("2")) {
                            IToast.show(Activity_Certified.this, "成人学历认证成功");
                        }
                    }
                }
                break;

            /**
             * 在校学生证认证
             */
            case R.id.fl_student_id_certification:
                if (mCertificationInformationModel.getModel().getUserDiplomaStatus().equals("0")) {
                    IToast.show(Activity_Certified.this, "资料正在审核中，请耐心等待");
                } else if (mCertificationInformationModel.getModel().getUserDiplomaStatus().equals("2")) {
                    IToast.show(Activity_Certified.this, "成人学历认证成功");
                } else {
                    if (!HGTool.isEmpty(mCertificationInformationModel.getModel().getStudentIdCardStatus())) {
                        if (mCertificationInformationModel.getModel().getStudentIdCardStatus().equals("3")) {
                            Intent mIntentAcademicCertificate = new Intent(Activity_Certified.this, Activity_StudentIDCertification.class);
                            startActivity(mIntentAcademicCertificate);
                        } else if (mCertificationInformationModel.getModel().getStudentIdCardStatus().equals("1")) {
                            Intent mIntentAcademicCertificate = new Intent(Activity_Certified.this, Activity_StudentIDCertification.class);
                            mIntentAcademicCertificate.putExtra("type", "1");
                            startActivity(mIntentAcademicCertificate);
                        } else if (mCertificationInformationModel.getModel().getStudentIdCardStatus().equals("0")) {
                            IToast.show(Activity_Certified.this, "资料正在审核中，请耐心等待");
                        } else if (mCertificationInformationModel.getModel().getStudentIdCardStatus().equals("2")) {
                            IToast.show(Activity_Certified.this, "在校学生认证成功");
                        }
                    }
                }
                break;

            case R.id.but_open:
                if (mTag.equals("3")) {
                    if (mCertificationInformationModel.getModel().getUserDiplomaStatus().equals("2")
                            || mCertificationInformationModel.getModel().getStudentIdCardStatus().equals("2")) {
                        if (BaseApplication.getApplication().isClub().equals("2")) {
                            initOpen();
                        } else if (BaseApplication.getApplication().isClub().equals("3")) {
                            Intent mIntentPushRange = new Intent(Activity_Certified.this, Activity_PushRange.class);
                            mIntentPushRange.putExtra("type", mType);
                            startActivity(mIntentPushRange);
                        } else if (mCertificationInformationModel.getModel().getUserDiplomaStatus().equals("2")
                                || mCertificationInformationModel.getModel().getStudentIdCardStatus().equals("2")) {
                            initBindingSchool();
                        }
                    } else {
                        initOpenPromptDialog();
                    }
                }
                break;
        }
    }

    private void initOpen() {
        OkHttpUtils
                .post()
                .url(Api.BEING_TUTOR)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("type", mType)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Certified.this, "服务器开小差！请稍后重试");
                        Log.e("==开通身份", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==开通身份", response);
                        Log.e("==开通身份", Api.BEING_TUTOR);
                        Log.e("==开通身份", BaseApplication.getApplication().getToken());
                        Log.e("==开通身份", mType);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
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
                            IToast.show(Activity_Certified.this, mSuccessModel.getMessage());
                            mButOpen.setVisibility(View.GONE);
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(),
                                        Activity_Certified.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_Certified.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_Certified.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
    }

    private void initBindingSchool() {
        final InputBoxDialogView mInputBoxDialogView = new InputBoxDialogView(Activity_Certified.this);
        mInputBoxDialogView.setContentInputBox("成为学校教师");
        mInputBoxDialogView.setHint("请输入所在学校教师识别码");
        mInputBoxDialogView.setNo("暂不绑定");
        mInputBoxDialogView.setCustomOnClickListener(new InputBoxDialogView.OnCustomDialogListener() {
            @Override
            public void setYesOnclick() {
                //需要请求接口 成为在校教师
                String mTeacherCode = mInputBoxDialogView.getText().toString();
                initAddBindingSchool(mTeacherCode, mInputBoxDialogView);
            }

            @Override
            public void setNoOnclick() {
                mInputBoxDialogView.dismiss();
                Intent mIntentPushRange = new Intent(Activity_Certified.this, Activity_PushRange.class);
                mIntentPushRange.putExtra("type", mType);
                startActivity(mIntentPushRange);
            }
        });
        mInputBoxDialogView.setCancelable(false);
        mInputBoxDialogView.show();
    }

    private void initAddBindingSchool(final String mTeacherCode, final InputBoxDialogView mInputBoxDialogView) {
        OkHttpUtils
                .post()
                .url(Api.ADD_TEACHER_TO_SCHOOL)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("teacherCode", mTeacherCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("==学校教师绑定学校:::", e.getMessage());
                        IToast.show(Activity_Certified.this, "服务器开小差 请稍后再试 ！ ");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==学校教师绑定学校:::", response);
                        Log.e("==学校教师绑定学校:::", Api.ADD_TEACHER_TO_SCHOOL);
                        Log.e("==学校教师绑定学校:::", BaseApplication.getApplication().getToken());
                        Log.e("==学校教师绑定学校:::", mTeacherCode);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            mInputBoxDialogView.dismiss();
                            SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                            //是否有学校：0没有 1学校学生 2学校老师  isSchool
                            mEditor.putString("isSchool", "2");
                            mEditor.commit();
                            Intent mIntentPushRange = new Intent(Activity_Certified.this, Activity_PushRange.class);
                            mIntentPushRange.putExtra("type", mType);
                            startActivity(mIntentPushRange);
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {

                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_Certified.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
    }

    private String mTitle = "";

    private String mPrompt = "";

    private void initOpenPromptDialog() {
        if (mTag.equals("3")) {
            mTitle = "答题者认证";
            mPrompt = "只有成人学历认证或者在校学生认证通过后，才可申请开通答题者";
        }
        try {
            final CleanUpCacheDialogView mCleanUpCacheDialogView = new CleanUpCacheDialogView(Activity_Certified.this);
            mCleanUpCacheDialogView.setTitle(mTitle);
            mCleanUpCacheDialogView.setContent(mPrompt);
            mCleanUpCacheDialogView.setCancelable(false);
            mCleanUpCacheDialogView.setCustomOnClickListener(new CleanUpCacheDialogView.OnCustomDialogListener() {
                @Override
                public void setYesOnclick() {
                    DataCleanUtils.clearAllCache(Activity_Certified.this);
                    mCleanUpCacheDialogView.dismiss();
                }
            });
            mCleanUpCacheDialogView.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_certified;
    }

    @Override
    public void initTitleBar() {
        if (!HGTool.isEmpty(mTag)) {
            if (mTag.equals("3")) {
                mTvTopTitle.setText("答题者认证");
                mFlAcademicCertificate.setVisibility(View.VISIBLE);
                mFlStudentIDCertification.setVisibility(View.VISIBLE);
                mTvPromptPickOneOfTwo.setVisibility(View.VISIBLE);
            }
        }
    }

    private LocalBroadcastManager localBroadcastManager;
    private CertifiedReceiver localReceiver;

    @Override
    protected void initData() {
        mTag = getIntent().getStringExtra("type");
        if (mTag.equals("3")) {
            /**
             * 答题者
             */
            mType = "2";
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tangchaoke.yiyoubangjiao.certified");
        localReceiver = new CertifiedReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    class CertifiedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra("tuisong");
            if (num.equals("certified")) {
                initCertificationInformation();
            }
        }
    }

    CertificationInformationModel mCertificationInformationModel;

    /**
     * 用户认证信息
     */
    private void initCertificationInformation() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_Certified.this, "请稍后",
                true, false, null);
        OkHttpUtils
                .post()
                .url(Api.GET_AUTHENTICA_INFORMA)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("type", mType)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Certified.this, "服务器开小差！请稍后重试");
                        Log.e("====用户认证信息：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====用户认证信息：：：", response);
                        Log.e("====用户认证信息：：：", Api.GET_AUTHENTICA_INFORMA);
                        Log.e("====用户认证信息：：：", BaseApplication.getApplication().getToken());
                        Log.e("====用户认证信息：：：", mType);
                        mCertificationInformationModel = JSONObject.parseObject(response, CertificationInformationModel.class);
                        if (RequestType.SUCCESS.equals(mCertificationInformationModel.getStatus())) {
                            mProgressHUD.dismiss();
                            initCertificationInformationDisplay(mCertificationInformationModel);
                        } else {
                            mProgressHUD.dismiss();
                            if (mCertificationInformationModel.getStatus().equals("9")
                                    || mCertificationInformationModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil
                                        .checkLoginState(mCertificationInformationModel.getStatus(),
                                                Activity_Certified.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_Certified.this, "登录失效");
                                }
                            } else if (mCertificationInformationModel.getStatus().equals("0")) {
                                IToast.show(Activity_Certified.this, mCertificationInformationModel.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCertificationInformation();
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putString("activity", "Activity_Certified");
        mEditor.commit();
    }

    /**
     * 判断是否认证
     * <p>
     * 家教  需要认证  身份认证  成人学历认证  或   在校学生证认证  补充信息
     * <p>
     * 代课  需要认证  身份认证  教师资格证认证  成人学历认证   或  在校学生证认证   补充信息
     * <p>
     * 答题者  需要认证  成人学历认证  或  在校学生证认证
     *
     * @param mCertificationInformationModel
     */
    private void initCertificationInformationDisplay(CertificationInformationModel mCertificationInformationModel) {

        if (mTag.equals("3")) {
            if (!HGTool.isEmpty(BaseApplication.getApplication().isRespondent())) {
                if (BaseApplication.getApplication().isRespondent().equals("1")) {
                    mButOpen.setVisibility(View.GONE);
                } else if (BaseApplication.getApplication().isRespondent().equals("0")) {
                    mButOpen.setVisibility(View.VISIBLE);
                }
            }
        }

        /**
         * 用户成人学历认证状态:0认证中 1认证失败 2认证成功 3未认证
         */
        if (mCertificationInformationModel.getModel().getUserDiplomaStatus().equals("2")) {
            mImgAcademicCertificateVerified.setVisibility(View.VISIBLE);
            mLlAcademicCertificateShadow.setVisibility(View.INVISIBLE);
            mLlStudentIDCertificationShadow.setVisibility(View.VISIBLE);
            mImgAcademicCertificateVerified.setImageDrawable(ContextCompat.getDrawable(Activity_Certified.this.getApplicationContext(), R.drawable.ic_verified));
        } else if (mCertificationInformationModel.getModel().getUserDiplomaStatus().equals("0")) {
            mImgAcademicCertificateVerified.setVisibility(View.VISIBLE);
            mLlAcademicCertificateShadow.setVisibility(View.INVISIBLE);
            mLlStudentIDCertificationShadow.setVisibility(View.VISIBLE);
            mImgAcademicCertificateVerified.setImageDrawable(ContextCompat.getDrawable(Activity_Certified.this.getApplicationContext(), R.drawable.ic_under_review));
        } else if (mCertificationInformationModel.getModel().getUserDiplomaStatus().equals("1")) {
            mImgAcademicCertificateVerified.setVisibility(View.VISIBLE);
            mImgAcademicCertificateVerified.setImageDrawable(ContextCompat.getDrawable(Activity_Certified.this.getApplicationContext(), R.drawable.ic_audit_failure));
        } else if (mCertificationInformationModel.getModel().getUserDiplomaStatus().equals("3")) {
            mImgAcademicCertificateVerified.setVisibility(View.INVISIBLE);
        }

        /**
         * 用户在校学生证认证状态:0认证中 1认证失败 2认证成功 3未认证
         */
        if (mCertificationInformationModel.getModel().getStudentIdCardStatus().equals("2")) {
            mImgStudentIDCertification.setVisibility(View.VISIBLE);
            mLlAcademicCertificateShadow.setVisibility(View.VISIBLE);
            mLlStudentIDCertificationShadow.setVisibility(View.INVISIBLE);
            mImgStudentIDCertification.setImageDrawable(ContextCompat.getDrawable(Activity_Certified.this.getApplicationContext(), R.drawable.ic_verified));
        } else if (mCertificationInformationModel.getModel().getStudentIdCardStatus().equals("0")) {
            mImgStudentIDCertification.setVisibility(View.VISIBLE);
            mLlAcademicCertificateShadow.setVisibility(View.VISIBLE);
            mLlStudentIDCertificationShadow.setVisibility(View.INVISIBLE);
            mImgStudentIDCertification.setImageDrawable(ContextCompat.getDrawable(Activity_Certified.this.getApplicationContext(), R.drawable.ic_under_review));
        } else if (mCertificationInformationModel.getModel().getStudentIdCardStatus().equals("1")) {
            mImgStudentIDCertification.setVisibility(View.VISIBLE);
            mImgStudentIDCertification.setImageDrawable(ContextCompat.getDrawable(Activity_Certified.this.getApplicationContext(), R.drawable.ic_audit_failure));
        } else if (mCertificationInformationModel.getModel().getStudentIdCardStatus().equals("3")) {
            mImgStudentIDCertification.setVisibility(View.INVISIBLE);
        }

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
