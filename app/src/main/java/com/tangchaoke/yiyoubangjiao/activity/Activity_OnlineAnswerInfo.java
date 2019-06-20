package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.ClubProportionBean;
import com.tangchaoke.yiyoubangjiao.model.OnlineAnswerInfoModel;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.tangchaoke.yiyoubangjiao.view.PromptDialogView;
import com.tangchaoke.yiyoubangjiao.view.ZoomImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 互动解答详情
*/
public class Activity_OnlineAnswerInfo extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    private String mOid;

    @BindView(R.id.img_head)
    CircleImageView mImgHead;

    @BindView(R.id.tv_name)
    TextView mTvName;

    @BindView(R.id.tv_grade)
    TextView mTvGrade;

    @BindView(R.id.tv_subject)
    TextView mTvSubject;

    @BindView(R.id.tv_time)
    TextView mTvTime;

    @BindView(R.id.tv_question)
    TextView mTvQuestion;

    @BindView(R.id.img_photo)
    ImageView mImgPhoto;

    OnlineAnswerInfoModel mOnlineAnswerInfoModel;

    String mPhotoPath;

    private String mPrompt = "";

    @BindView(R.id.rl_answer_immediately)
    RelativeLayout mRlAnswerImmediately;

    @BindView(R.id.img_solved)
    ImageView mImgSolved;

    @BindView(R.id.tv_solved)
    TextView mTvSolved;

    private String mType3 = "";

    private String mType2 = "";

    @OnClick({R.id.ll_back, R.id.rl_answer_immediately, R.id.img_photo})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            /**
             * 立即抢答
             */
            case R.id.rl_answer_immediately:
                if (!HGTool.isEmpty(BaseApplication.getApplication().isRespondent())) {
                    if (BaseApplication.getApplication().isRespondent().equals("1")) {//是否认证答题者 认证
                        if (BaseApplication.getApplication().isPushRange().equals("1")) {//是否填写推送范围 已填写
                            if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getModel().getUserOid())) {
                                if (mOnlineAnswerInfoModel.getModel().getExercisesStatus().equals("3")) {
                                    return;
                                } else {
                                    if (!mOnlineAnswerInfoModel.getModel().getUserOid().equals(BaseApplication.getApplication().getOid())) {
                                        if (mType2.equals("2")) {
                                            mPrompt = "请在两分钟内与发布问题的同学联系，超过两分钟您将失去答题资格";
                                        } else {
                                            mPrompt = "请在两分钟内与发布问题的同学联系，超过两分钟您将失去答题资格。您的回答被采纳以后，平台将收取" + mProportion + "作为平台服务费，剩余奖金将发放到您的App账户";
                                        }
                                        isPromptDialog(1, mOnlineAnswerInfoModel.getModel().getUserOid(), mPrompt);
                                    } else {
                                        IToast.show(Activity_OnlineAnswerInfo.this, "自己不能回答自己所发问题");
                                    }
                                }
                            }
                        } else {//未填写
                            mPrompt = "您当前尚未添加答题者推送范围，请先添加!";
                            isPromptDialog(3, mOnlineAnswerInfoModel.getModel().getUserOid(), mPrompt);
                        }
                    } else if (BaseApplication.getApplication().isRespondent().equals("0")) {
                        //未认证
                        if (BaseApplication.getApplication().isClub().equals("2")) {//俱乐部教练
                            if (mType2.equals("2")) {
                                mPrompt = "请在两分钟内与发布问题的同学联系，超过两分钟您将失去答题资格";
                            }
                            isPromptDialog(1, mOnlineAnswerInfoModel.getModel().getUserOid(), mPrompt);
                        } else if (mOnlineAnswerInfoModel.getModel().getExercisesStatus().equals("3")) {//问题已解答
                            return;
                        } else {
                            mPrompt = "您当前尚未认证成为答题者，请先认证!";
                            isPromptDialog(2, mOnlineAnswerInfoModel.getModel().getUserOid(), mPrompt);
                        }
                    }
                }
                break;

            case R.id.img_photo:
                mPhotoPath = Api.PATH + mOnlineAnswerInfoModel.getModel().getPhoto();
                initPhotoPow(mPhotoPath);
                break;

        }

    }

    PromptDialogView mPromptDialogView;

    private void isPromptDialog(final int type, final String mUserOid, String mPrompt) {
        try {
            mPromptDialogView = new PromptDialogView(Activity_OnlineAnswerInfo.this);
            mPromptDialogView.setTitle("温馨提示");
            mPromptDialogView.setContent(mPrompt);
            if (type == 1) {
                mPromptDialogView.setYes("立即抢答");
                mPromptDialogView.setNo("取消抢答");
            } else if (type == 2) {
                mPromptDialogView.setYes("立即认证");
                mPromptDialogView.setNo("暂不认证");
            } else if (type == 3) {
                mPromptDialogView.setYes("立即添加");
                mPromptDialogView.setNo("暂不添加");
            }
            mPromptDialogView.setCancelable(false);
            mPromptDialogView.setCustomOnClickListenerYes(new PromptDialogView.OnCustomDialogListener() {
                @Override
                public void setYesOnclick() {
                    if (type == 1) {
                        /**
                         * 立即抢答
                         */
                        initAnswerImmediately(mUserOid);
                    } else if (type == 2) {
                        /**
                         * 认证开通答题者权限
                         */
                        if (!BaseApplication.getApplication().isActor()) {
                            Intent mIntentCertifiedAgreement = new Intent(Activity_OnlineAnswerInfo.this, Activity_CertifiedAgreement.class);
                            mIntentCertifiedAgreement.putExtra("type", "3");//1家教 2代课老师 3答题者
                            startActivity(mIntentCertifiedAgreement);
                        } else {
                            Intent mIntentCertifiedAgreement = new Intent(Activity_OnlineAnswerInfo.this, Activity_Certified.class);
                            mIntentCertifiedAgreement.putExtra("type", "3");//1家教 2代课老师 3答题者
                            startActivity(mIntentCertifiedAgreement);
                        }
                        mPromptDialogView.dismiss();
                    } else if (type == 3) {
                        //没有设置
                        Intent mIntentPushRange = new Intent(Activity_OnlineAnswerInfo.this, Activity_PushRange.class);
                        mIntentPushRange.putExtra("type", "2");
                        startActivity(mIntentPushRange);
                        mPromptDialogView.dismiss();
                    }
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

    private void initAnswerImmediately(final String mUserOid) {

        OkHttpUtils
                .post()
                .url(Api.ANSWER)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_OnlineAnswerInfo.this, "服务器开小差！请稍后重试");
                        Log.e("==立即抢答：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==立即抢答：：：", response);
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            if (HGTool.isEmpty(mUserOid)) {
                                IToast.show(Activity_OnlineAnswerInfo.this, "对方ID不能空");
                                return;
                            }
                            Intent mIntentChat = new Intent(Activity_OnlineAnswerInfo.this, Activity_Chat.class);
                            mIntentChat.putExtra(EaseConstant.EXTRA_USER_ID, mUserOid.trim().toLowerCase());
                            mIntentChat.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
                            mIntentChat.putExtra("oid", mOid);
                            mIntentChat.putExtra("is_chat", "1");
                            mIntentChat.putExtra(EaseConstant.ISCARRYON, "yes");//自己的token
                            mIntentChat.putExtra(EaseConstant.TOKEN, BaseApplication.getApplication().getToken());//问题ID
                            mIntentChat.putExtra(EaseConstant.PROBLEM_OID, mOid);//问题ID
                            mIntentChat.putExtra(EaseConstant.ME_ID, BaseApplication.getApplication().getOid());//自己的ID
                            mIntentChat.putExtra(EaseConstant.ME_HEAD, BaseApplication.getApplication().getHead());//自己的头像
                            mIntentChat.putExtra(EaseConstant.ME_NAME, BaseApplication.getApplication().getNickName());//自己的名字
                            startActivity(mIntentChat);
                            finish();
                            mPromptDialogView.dismiss();
                            Log.e("===信息互相解答：：：", mUserOid.trim().toLowerCase());
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(), Activity_OnlineAnswerInfo.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_OnlineAnswerInfo.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                if (!mSuccessModel.getExercisesStatus().equals("0")) {
                                    finish();
                                }
                                IToast.show(Activity_OnlineAnswerInfo.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
    }

    View popView;

    PopupWindow mPopupWindow;

    @BindView(R.id.ll_online_answer_info)
    RelativeLayout mLlOnlineAnswerInfo;

    /**
     * 弹出底部对话框，达到背景背景透明效果
     * <p>
     * 实现原理：弹出一个全屏popupWindow,将Gravity属性设置bottom,根背景设置为一个半透明的颜色，
     * 弹出时popupWindow的半透明效果背景覆盖了在Activity之上 给人感觉上是popupWindow弹出后，背景变成半透明
     *
     * @param mPhotoPath
     */
    public void initPhotoPow(String mPhotoPath) {
        popView = LayoutInflater.from(Activity_OnlineAnswerInfo.this).inflate(R.layout.popup_window_online_answer_info, null);
        ZoomImageView mZoomImageView = popView.findViewById(R.id.web_view_img);
        Glide.with(Activity_OnlineAnswerInfo.this).load(mPhotoPath).into(mZoomImageView);
        mPopupWindow = new PopupWindow(popView,
                GridLayoutManager.LayoutParams.MATCH_PARENT,
                GridLayoutManager.LayoutParams.MATCH_PARENT);
        // 设置弹出动画
        mPopupWindow.setAnimationStyle(R.style.take_photo_anim);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        // 顯示在根佈局的底部
        mPopupWindow.showAtLocation(mLlOnlineAnswerInfo, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_online_answer_info;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("问题详情");
    }

    @Override
    protected void initData() {
        mOid = getIntent().getStringExtra("oid");
        mType3 = getIntent().getStringExtra("type3");
        mType2 = getIntent().getStringExtra("type2");
        initOnlineAnswerInfo();
    }

    private String mProportion = "";

    private void initClubProportion() {
        OkHttpUtils
                .post()
                .url(Api.GET_CLUB_PROPORTION)
                .addParams("token", BaseApplication.getApplication().getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("==获取俱乐部教师抽成比例:::", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==获取俱乐部教师抽成比例:::", response);
                        Log.e("==获取俱乐部教师抽成比例:::", Api.GET_CLUB_PROPORTION);
                        Log.e("==获取俱乐部教师抽成比例:::", BaseApplication.getApplication().getToken());
                        ClubProportionBean mClubProportionBean = JSONObject.parseObject(response, ClubProportionBean.class);
                        if (RequestType.SUCCESS.equals(mClubProportionBean.getStatus())) {
                            mProportion = mClubProportionBean.getProportion();
                        } else {
                            if (mClubProportionBean.getStatus().equals("0")) {
                                IToast.show(Activity_OnlineAnswerInfo.this, mClubProportionBean.getMessage());
                            }
                        }
                    }
                });
    }

    private void initOnlineAnswerInfo() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_OnlineAnswerInfo.this, "加载中", true, false, null);

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
                        IToast.show(Activity_OnlineAnswerInfo.this, "服务器开小差！请稍后重试");
                        Log.e("==互动解答详情：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==互动解答详情：：：", response);
                        Log.e("==互动解答详情：：：", Api.EXERCISES_INFORMATION);
                        Log.e("==互动解答详情：：：", BaseApplication.getApplication().getToken());
                        Log.e("==互动解答详情：：：", mOid);
                        mOnlineAnswerInfoModel = JSONObject.parseObject(response, OnlineAnswerInfoModel.class);
                        mProgressHUD.dismiss();
                        if (RequestType.SUCCESS.equals(mOnlineAnswerInfoModel.getStatus())) {
                            if (mType3.equals("2")) {
                                if (mType2.equals("1")
                                        && !BaseApplication.getApplication().isClub().equals("1")) {
                                    initClubProportion();
                                }
                            } else {
                                mProportion = mOnlineAnswerInfoModel.getModel().getProportion();
                            }
                            initOnlineAnswerInfoDisplay(mOnlineAnswerInfoModel.getModel());
                        } else {
                            if (mOnlineAnswerInfoModel.getStatus().equals("9") || mOnlineAnswerInfoModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mOnlineAnswerInfoModel.getStatus(), Activity_OnlineAnswerInfo.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_OnlineAnswerInfo.this, "登录失效");
                                }
                            } else if (mOnlineAnswerInfoModel.getStatus().equals("0")) {
                                IToast.show(Activity_OnlineAnswerInfo.this, mOnlineAnswerInfoModel.getMessage());
                            }
                        }
                    }
                });

    }

    private String mMeId;
    private String mMeHead;
    private String mMeName;

    private String mToHead;
    private String mToName;

    @BindView(R.id.tv_bounty)
    TextView mTvBounty;

    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;

    @BindView(R.id.ll_subject)
    LinearLayout mLlSubject;

    private void initOnlineAnswerInfoDisplay(OnlineAnswerInfoModel.OnlineAnswerInfoModelModel mOnlineAnswerInfoModel) {

        if (mOnlineAnswerInfoModel.getType().equals("1")) {
            mLlSubject.setVisibility(View.VISIBLE);
        } else if (mOnlineAnswerInfoModel.getType().equals("2")) {
            mLlSubject.setVisibility(View.INVISIBLE);
        }

        //界面：1首页互动答题 2我的学校互动答题
        if (mType3.equals("2")) {
            if (BaseApplication.getApplication().isClub().equals("0")
                    || BaseApplication.getApplication().isClub().equals("1")) {
                mLlBottom.setVisibility(View.INVISIBLE);
            } else {
                mLlBottom.setVisibility(View.VISIBLE);
            }
        } else if (mType3.equals("1")) {
            if (BaseApplication.getApplication().isSchool().equals("1")) {
                mLlBottom.setVisibility(View.INVISIBLE);
            } else {
                mLlBottom.setVisibility(View.VISIBLE);
            }
        }

        if (!HGTool.isEmpty(BaseApplication.getApplication().getOid())) {
            mMeId = BaseApplication.getApplication().getOid();
        }

        if (!HGTool.isEmpty(BaseApplication.getApplication().getHead())) {
            mMeHead = BaseApplication.getApplication().getHead();
        }

        if (!HGTool.isEmpty(BaseApplication.getApplication().getNickName())) {
            mMeName = BaseApplication.getApplication().getNickName();
        }

        if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getHead())) {
            Glide.with(Activity_OnlineAnswerInfo.this).load(Api.PATH + mOnlineAnswerInfoModel.getHead()).into(mImgHead);
            mToHead = Api.PATH + mOnlineAnswerInfoModel.getHead();
        }

        if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getExercisesStatus())) {
            if (mOnlineAnswerInfoModel.getExercisesStatus().equals("3")) {
                mImgSolved.setImageDrawable(ContextCompat.getDrawable(Activity_OnlineAnswerInfo.this.getApplicationContext(), R.drawable.shape_light_gray_fillet100_line));
                mTvSolved.setText("已解答");
            }
        }

        if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getNickName())) {
            if (mOnlineAnswerInfoModel.getNickName().length() > 11) {
                mTvName.setMaxLines(1);
                mTvName.setMaxEms(6);
                mTvName.setEllipsize(TextUtils.TruncateAt.END);
                mTvName.setSingleLine();
                mTvName.setText(mOnlineAnswerInfoModel.getNickName());
            } else {
                mTvName.setText(mOnlineAnswerInfoModel.getNickName());
            }
            mToName = mOnlineAnswerInfoModel.getNickName();
        }

        if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getGrade())) {
            mTvGrade.setText(mOnlineAnswerInfoModel.getGrade());
        } else if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getChessSpecies())) {
            if (mOnlineAnswerInfoModel.getChessSpecies().equals("1")) {
                mTvGrade.setText("国际象棋");
            } else if (mOnlineAnswerInfoModel.getChessSpecies().equals("2")) {
                mTvGrade.setText("国际跳棋");
            } else if (mOnlineAnswerInfoModel.getChessSpecies().equals("3")) {
                mTvGrade.setText("围棋");
            } else if (mOnlineAnswerInfoModel.getChessSpecies().equals("4")) {
                mTvGrade.setText("五子棋");
            } else if (mOnlineAnswerInfoModel.getChessSpecies().equals("5")) {
                mTvGrade.setText("象棋");
            }
        }

        if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getSubject())) {
            mTvSubject.setText(mOnlineAnswerInfoModel.getSubject());
        }

        if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getSubmissionTime())) {
            mTvTime.setText("发布时间：" + mOnlineAnswerInfoModel.getSubmissionTime());
        }

        if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getPrice())) {
            if (mOnlineAnswerInfoModel.getPrice().equals("0")) {
                mTvBounty.setVisibility(View.GONE);
            } else {
                mTvBounty.setVisibility(View.VISIBLE);
                mTvBounty.setText("￥ " + mOnlineAnswerInfoModel.getPrice());
            }
        }

        if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getContent().trim())) {
            mTvQuestion.setVisibility(View.VISIBLE);
            mTvQuestion.setText(mOnlineAnswerInfoModel.getContent());
        } else {
            mTvQuestion.setVisibility(View.GONE);
        }

        if (!HGTool.isEmpty(mOnlineAnswerInfoModel.getPhoto())) {
            mImgPhoto.setVisibility(View.VISIBLE);
            Glide.with(Activity_OnlineAnswerInfo.this).load(Api.PATH + mOnlineAnswerInfoModel.getPhoto()).into(mImgPhoto);
        } else {
            mImgPhoto.setVisibility(View.GONE);
        }

    }

}
