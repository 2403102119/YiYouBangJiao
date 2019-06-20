package com.tangchaoke.yiyoubangjiao.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.JudgeProblemModel;
import com.tangchaoke.yiyoubangjiao.model.OnlineAnswerInfoModel;
import com.tangchaoke.yiyoubangjiao.model.ProblemMessageModel;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.view.CleanUpCacheDialogView;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.tangchaoke.yiyoubangjiao.view.PromptDialogView;
import com.tangchaoke.yiyoubangjiao.view.ZoomImageView;
import com.tangchaoke.yiyoubangjiao.xf.FloatDragView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 聊天界面
*/
public class Activity_Chat extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.ll_chat)
    LinearLayout mLlChat;

    /**
     * 问题ID
     */
    private String mOid;

    private String userId;

    private String mToName;

    @BindView(R.id.ll_click_on)
    LinearLayout mLlClickOn;

    /**
     * @param view
     */
    @OnClick({R.id.ll_back, R.id.ll_adoption, R.id.ll_click_on, R.id.but_problem, R.id.ll_complete})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                mEditor.putString("activity", "");
                mEditor.commit();
                break;

            case R.id.ll_adoption:
                initAdoption();
                break;

            case R.id.ll_click_on:
                break;

            case R.id.but_problem:
                ReleaseOpenPop();
                break;

            case R.id.ll_complete:
                initComplete();
                break;

        }

    }

    private void initComplete() {
        OkHttpUtils
                .post()
                .url(Api.END_ANSWER_BY_RESPONDENT)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("==答题人解答完毕:::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==答题人解答完毕:::", response);
                        Log.e("==答题人解答完毕:::", Api.END_ANSWER_BY_RESPONDENT);
                        Log.e("==答题人解答完毕:::", BaseApplication.getApplication().getToken());
                        Log.e("==答题人解答完毕:::", mOid);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            initJudgeProblem();
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(), Activity_Chat.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_Chat.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_Chat.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
    }

    /**
     * 发布问题弹窗
     */
    View mPopViewProblem;
    public static PopupWindow mPopupWindowProblem;
    /**
     * 左上角 返回 按钮
     */
    ImageView mImgBack;

    TextView mTvProblem;

    ImageView mImgPhono;

    String mPhotoPath;

    /**
     * 问题详情
     * <p>
     * 弹出底部对话框，达到背景背景透明效果
     * <p>
     * 实现原理：弹出一个全屏popupWind+ow,将Gravity属性设置bottom,根背景设置为一个半透明的颜色，
     * 弹出时popupWindow的半透明效果背景覆盖了在Activity之上 给人感觉上是popupWindow弹出后，背景变成半透明
     */
    private void ReleaseOpenPop() {
        mPopViewProblem = LayoutInflater.from(Activity_Chat.this).inflate(R.layout.popup_window_problem, null);
        mImgBack = mPopViewProblem.findViewById(R.id.img_back);
        mTvProblem = mPopViewProblem.findViewById(R.id.tv_problem);
        mImgPhono = mPopViewProblem.findViewById(R.id.img_phono);
        if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getModel().getContent())) {
            mTvProblem.setVisibility(View.VISIBLE);
            mTvProblem.setText(mOnlineAnswerInfoModel.getModel().getContent());
        } else {
            mTvProblem.setVisibility(View.GONE);
        }
        if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getModel().getPhoto())) {
            mImgPhono.setVisibility(View.VISIBLE);
            Glide.with(Activity_Chat.this).load(Api.PATH + mOnlineAnswerInfoModel.getModel().getPhoto()).into(mImgPhono);
        } else {
            mImgPhono.setVisibility(View.GONE);
        }
        mImgPhono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoPath = Api.PATH + mOnlineAnswerInfoModel.getModel().getPhoto();
                initPhotoPow(mPhotoPath);
            }
        });
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindowProblem.dismiss();//关闭弹窗
            }
        });
        mPopupWindowProblem = new PopupWindow(mPopViewProblem,
                GridLayoutManager.LayoutParams.MATCH_PARENT,
                GridLayoutManager.LayoutParams.MATCH_PARENT);
        // 设置弹出动画
        mPopupWindowProblem.setAnimationStyle(R.style.take_photo_anim);
        mPopupWindowProblem.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindowProblem.setFocusable(true);
        // 显示在根布局的底部
        mPopupWindowProblem.showAtLocation(mLlChat, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    View popView;

    PopupWindow mPopupWindowPhone;

    /**
     * 弹出底部对话框，达到背景背景透明效果
     * <p>
     * 实现原理：弹出一个全屏popupWindow,将Gravity属性设置bottom,根背景设置为一个半透明的颜色，
     * 弹出时popupWindow的半透明效果背景覆盖了在Activity之上 给人感觉上是popupWindow弹出后，背景变成半透明
     *
     * @param mPhotoPath
     */
    public void initPhotoPow(String mPhotoPath) {
        popView = LayoutInflater.from(Activity_Chat.this).inflate(R.layout.popup_window_online_answer_info, null);
        ZoomImageView mZoomImageView = popView.findViewById(R.id.web_view_img);
        Glide.with(Activity_Chat.this).load(mPhotoPath).into(mZoomImageView);
        mPopupWindowPhone = new PopupWindow(popView,
                GridLayoutManager.LayoutParams.MATCH_PARENT,
                GridLayoutManager.LayoutParams.MATCH_PARENT);
        // 设置弹出动画
        mPopupWindowPhone.setAnimationStyle(R.style.take_photo_anim);
        mPopupWindowPhone.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindowPhone.setFocusable(true);
        // 顯示在根佈局的底部
        mPopupWindowPhone.showAtLocation(mLlChat, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    /**
     * 结束通话
     */
    private void initAdoption() {
        if (HGTool.isEmpty(mOid)) {
            IToast.show(Activity_Chat.this, "此题出现问题");
            return;
        }
        OkHttpUtils
                .post()
                .url(Api.END_ANSWER)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Chat.this, "服务器开小差！请稍后重试");
                        Log.e("==结束通话：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==结束通话：：：", response);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            initAdopted();
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(), Activity_Chat.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_Chat.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_Chat.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
    }

    View mPopView;
    PopupWindow mPopupWindow;

    TextView mTvPrompt;

    /**
     * 结束弹窗
     */
    private void initAdopted() {
        mPopView = LayoutInflater.from(Activity_Chat.this).inflate(R.layout.popup_window_chat, null);
        mTvPrompt = mPopView.findViewById(R.id.tv_prompt);
        mLlAdoption.setVisibility(View.INVISIBLE);
        mLlComplete.setVisibility(View.GONE);
        HGTool.hintKbTwo(Activity_Chat.this);
        if (mJudgeProblemModel.getModel().getRespondentEndAnswer().equals("0")) {
            if (mJudgeProblemModel.getModel().getStatus().equals("1")) {
                mTvPrompt.setText("答案已采纳");
            } else if (mJudgeProblemModel.getModel().getStatus().equals("2")) {
                mTvPrompt.setText("平台介入");
            } else if (mJudgeProblemModel.getModel().getStatus().equals("3")) {
                mTvPrompt.setText("问题已关闭");
            }
        } else if (mJudgeProblemModel.getModel().getRespondentEndAnswer().equals("1")) {
            if (!HGTool.isEmpty(mProblemMessageModel.getModel().getUserOid())
                    && !HGTool.isEmpty(mProblemMessageModel.getModel().getRespondentOid())) {
                if (mProblemMessageModel.getModel().getUserOid().equals(BaseApplication.getApplication().getOid())) {
                    if (mJudgeProblemModel.getModel().getStatus().equals("1")) {
                        mTvPrompt.setText("答案已采纳");
                    } else if (mJudgeProblemModel.getModel().getStatus().equals("2")) {
                        mTvPrompt.setText("平台介入");
                    } else if (mJudgeProblemModel.getModel().getStatus().equals("3")) {
                        mTvPrompt.setText("问题已关闭");
                    } else {
                        mTvPrompt.setText("采纳该题");
                    }
                } else {
                    if (mJudgeProblemModel.getModel().getStatus().equals("1")) {
                        mTvPrompt.setText("答案已采纳");
                    } else if (mJudgeProblemModel.getModel().getStatus().equals("2")) {
                        mTvPrompt.setText("平台介入");
                    } else if (mJudgeProblemModel.getModel().getStatus().equals("3")) {
                        mTvPrompt.setText("问题已关闭");
                    } else {
                        mTvPrompt.setText("待发题人采纳");
                    }
                }
            }
        }
        mPopupWindow = new PopupWindow(mPopView,
                GridLayoutManager.LayoutParams.MATCH_PARENT,
                GridLayoutManager.LayoutParams.WRAP_CONTENT);
        // 设置弹出动画
        mPopupWindow.setAnimationStyle(R.style.take_photo_anim);
        mPopupWindow.setBackgroundDrawable(new
                BitmapDrawable());
        mPopupWindow.setFocusable(false);
        // 显示在根布局的底部
        mPopupWindow.showAtLocation(mLlChat, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_chat;
    }

    @BindView(R.id.ll_adoption)
    LinearLayout mLlAdoption;

    @BindView(R.id.ll_complete)
    LinearLayout mLlComplete;

    @Override
    public void initTitleBar() {
        // 听筒模式下设置为false
        BaseApplication.mAudioManager.setSpeakerphoneOn(false);
        // 设置成听筒模式
        BaseApplication.mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;

    private String mIsChat = "";

    int screenWidth;

    int screenHeight;

    @BindView(R.id.rl_pr)
    RelativeLayout rl_pr;

    @Override
    protected void initData() {

        checkCameraPermission();

        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

//         自定义颜色
        tintManager.setTintDrawable(getResources().getDrawable(R.drawable.select_blue_gradient));

        mOid = getIntent().getStringExtra("oid");
        userId = getIntent().getStringExtra("userId");
        mIsChat = getIntent().getStringExtra("is_chat");

        EaseChatFragment chatFragment = new EaseChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

        localBroadcastManager = LocalBroadcastManager.getInstance(Activity_Chat.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tangchaoke.yiyoubangjiao.home");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        FloatDragView.addFloatDragView(this, rl_pr, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击事件
                ReleaseOpenPop();
            }
        });

    }

    OnlineAnswerInfoModel mOnlineAnswerInfoModel;

    private void initProblem() {
        OkHttpUtils
                .post()
                .url(Api.EXERCISES_INFORMATION)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .addParams("type1", "Android")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Chat.this, "服务器开小差！请稍后重试");
                        Log.e("==互动解答详情：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==互动解答详情：：：", response);
                        Log.e("==互动解答详情：：：", Api.EXERCISES_INFORMATION);
                        Log.e("==互动解答详情：：：", BaseApplication.getApplication().getToken());
                        Log.e("==互动解答详情：：：", mOid);
                        mOnlineAnswerInfoModel = JSONObject.parseObject(response, OnlineAnswerInfoModel.class);
                        if (RequestType.SUCCESS.equals(mOnlineAnswerInfoModel.getStatus())) {

                        } else {
                            if (mOnlineAnswerInfoModel.getStatus().equals("9") || mOnlineAnswerInfoModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mOnlineAnswerInfoModel.getStatus(), Activity_Chat.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_Chat.this, "登录失效");
                                }
                            } else if (mOnlineAnswerInfoModel.getStatus().equals("0")) {
                                IToast.show(Activity_Chat.this, mOnlineAnswerInfoModel.getMessage());
                            }
                        }
                    }
                });
    }

    /**
     * 检查麦克风权限
     */
    public void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, CAMERA_PERMISSION_CODE);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initProblemMessage();
        initProblem();
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putString("activity", "Activity_Chat");
        mEditor.commit();
    }

    JudgeProblemModel mJudgeProblemModel;

    /**
     * 根据题目oid判断题目是否被采纳
     */
    private void initJudgeProblem() {
        if (HGTool.isEmpty(mOid)) {
            return;
        }
        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_Chat.this, "加载中", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.JUDGE_PROBLEM)
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Chat.this, "服务器开小差！请稍后重试");
                        Log.e("==根据题目oid判断题目是否被采纳：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==根据题目oid判断题目是否被采纳：：：", response);
                        Log.e("==根据题目oid判断题目是否被采纳：：：", Api.JUDGE_PROBLEM);
                        Log.e("==根据题目oid判断题目是否被采纳：：：", mOid);
                        mProgressHUD.dismiss();
                        mJudgeProblemModel = JSONObject.parseObject(response, JudgeProblemModel.class);
                        if (RequestType.SUCCESS.equals(mJudgeProblemModel.getStatus())) {
                            mLlClickOn.setVisibility(View.GONE);
                            if (!HGTool.isEmpty(mJudgeProblemModel.getModel().getStatus())) {
                                if (mJudgeProblemModel.getModel().getRespondentEndAnswer().equals("0")) {
                                    if (mJudgeProblemModel.getModel().getStatus().equals("1")
                                            || mJudgeProblemModel.getModel().getStatus().equals("2")
                                            || mJudgeProblemModel.getModel().getStatus().equals("3")) {
                                        initAdopted();
                                    }
                                } else if (mJudgeProblemModel.getModel().getRespondentEndAnswer().equals("1")) {
                                    if (!HGTool.isEmpty(mProblemMessageModel.getModel().getUserOid())
                                            && !HGTool.isEmpty(mProblemMessageModel.getModel().getRespondentOid())) {
                                        if (mProblemMessageModel.getModel().getUserOid().equals(BaseApplication.getApplication().getOid())) {
                                            if (mJudgeProblemModel.getModel().getStatus().equals("1")
                                                    || mJudgeProblemModel.getModel().getStatus().equals("2")
                                                    || mJudgeProblemModel.getModel().getStatus().equals("3")) {
                                                initAdopted();
                                            } else {
                                                isPromptDialog();
                                            }
                                        } else {
                                            initAdopted();
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
    }

    PromptDialogView mPromptDialogView;

    private void isPromptDialog() {
        try {
            mPromptDialogView = new PromptDialogView(Activity_Chat.this);
            mPromptDialogView.setTitle("温馨提示");
            mPromptDialogView.setContent("该问题已被答题者解答完毕，您是否采纳该答案?");
            mPromptDialogView.setYes("立即采纳");
            mPromptDialogView.setNo("暂不采纳");
            mPromptDialogView.setCancelable(false);
            mPromptDialogView.setCustomOnClickListenerYes(new PromptDialogView.OnCustomDialogListener() {
                @Override
                public void setYesOnclick() {
                    initAdoption();
                    mPromptDialogView.dismiss();
                }

                @Override
                public void setNoOnclick() {
                    finish();
                    mPromptDialogView.dismiss();
                }
            });
            mPromptDialogView.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ProblemMessageModel mProblemMessageModel;

    /**
     * 根据题目oid返回数据
     */
    private void initProblemMessage() {
        if (HGTool.isEmpty(mOid)) {
            return;
        }
        OkHttpUtils
                .post()
                .url(Api.GET_PROBLEM_MESSAGE)
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_Chat.this, "服务器开小差！请稍后重试");
                        Log.e("==根据题目oid返回数据：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==根据题目oid返回数据：：：", response);
                        Log.e("==根据题目oid返回数据：：：", Api.GET_PROBLEM_MESSAGE);
                        Log.e("==根据题目oid返回数据：：：", mOid);
                        mProblemMessageModel = JSONObject.parseObject(response, ProblemMessageModel.class);
                        if (RequestType.SUCCESS.equals(mProblemMessageModel.getStatus())) {
                            mLlClickOn.setVisibility(View.GONE);
                            if (!HGTool.isEmpty(mProblemMessageModel.getModel().getUserOid())
                                    && !HGTool.isEmpty(mProblemMessageModel.getModel().getRespondentOid())) {
                                if (mProblemMessageModel.getModel().getUserOid().equals(BaseApplication.getApplication().getOid())) {
                                    mToName = mProblemMessageModel.getModel().getRespondentNickName();
                                    mTvTopTitle.setText(mToName);
                                    mLlAdoption.setVisibility(View.VISIBLE);
                                    mLlComplete.setVisibility(View.GONE);
                                } else {
                                    mToName = mProblemMessageModel.getModel().getUserNickName();
                                    mTvTopTitle.setText(mToName);
                                    mLlComplete.setVisibility(View.GONE);
                                    mLlAdoption.setVisibility(View.INVISIBLE);
                                }
                            }
                            initJudgeProblem();
                        }
                    }
                });
    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra("tuisong");
            String mObject = intent.getStringExtra("object");
            String mStatus = intent.getStringExtra("status");
            if (!HGTool.isEmpty(mIsChat)) {
                if (mIsChat.equals("1")) {
                    if (num.equals("answer")) {
                        initStartAnswer();
                    } else if (num.equals("no_answer")) {
                        initNoAnswerDialog();
                    } else if (num.equals("token")) {
                        boolean isAccountException = CheckLoginExceptionUtil.checkLoginState("9", Activity_Chat.this, true);
                        if (isAccountException == false) {
                            IToast.show(Activity_Chat.this, "登录失效");
                        }
                    } else if (num.equals("popups")) {
                        if (mObject.equals(mOid)) {
                            if (!HGTool.isEmpty(mStatus)) {
                                if (mStatus.equals("3")) {
                                    //结束答题
                                    initJudgeProblem();
                                } else if (mStatus.equals("4")) {
                                    //重回大厅
                                    initNoAnswerDialog();
                                } else if (mStatus.equals("5")) {
                                    //已介入
                                    initJudgeProblem();
                                } else if (mStatus.equals("6")) {
                                    //已关闭
                                    initJudgeProblem();
                                }
                            } else {
                                initNoAnswerDialog();
                            }
                        }
                    }
                }
            } else {
                if (num.equals("popups")) {
                    if (mObject.equals(mOid)) {
                        if (!HGTool.isEmpty(mStatus)) {
                            initProblemMessage();
                        } else {
                            initNoAnswerDialog();
                        }
                    }
                }
            }
        }

    }

    private void initNoAnswerDialog() {
        try {
            final CleanUpCacheDialogView mCleanUpCacheDialogView = new CleanUpCacheDialogView(Activity_Chat.this);
            mCleanUpCacheDialogView.setTitle("温馨提示");
            mCleanUpCacheDialogView.setContent("您未在两分钟之内给对方回复，\n已失去对该题的答题资格");
            mCleanUpCacheDialogView.setCancelable(false);
            mCleanUpCacheDialogView.setCustomOnClickListener(new CleanUpCacheDialogView.OnCustomDialogListener() {
                @Override
                public void setYesOnclick() {
                    finish();
                    mCleanUpCacheDialogView.dismiss();
                }
            });
            mCleanUpCacheDialogView.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {//在onDestroy()方法中取消注册。
        super.onDestroy();
        //取消注册调用的是unregisterReceiver()方法，并传入接收器实例。
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    private void initStartAnswer() {
        OkHttpUtils
                .post()
                .url(Api.ANSWER_EXERCISES)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("==开始解答：：：", e.getMessage());
                        IToast.show(Activity_Chat.this, "服务器开小差！请稍后重试");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==开始解答：：：", response);
                        Log.e("==开始解答：：：", Api.ANSWER_EXERCISES);
                        Log.e("==开始解答：：：", BaseApplication.getApplication().getToken());
                        Log.e("==开始解答：：：", mOid);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {

                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(), Activity_Chat.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_Chat.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                IToast.show(Activity_Chat.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
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
