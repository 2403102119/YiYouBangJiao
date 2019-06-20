package com.tangchaoke.yiyoubangjiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.tangchaoke.yiyoubangjiao.model.AnsweredInfoModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
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
* description: 解答详情
*/
public class Activity_AnsweredInfo extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    /**
     * 已采纳按钮
     */
    @BindView(R.id.tv_adopted)
    TextView mTvAdopted;

    /**
     * 立即采纳
     */
    @BindView(R.id.tv_adopt_now)
    TextView mTvAdoptNow;

    /**
     * 抢答信息
     */
    @BindView(R.id.ll_actor_info)
    LinearLayout mLlActorinfo;

    /**
     * 暂无抢答
     */
    @BindView(R.id.ll_no_time_actor)
    LinearLayout mLlNoTimeActor;

    /**
     * 题目ID
     */
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

    @BindView(R.id.img_phono)
    ImageView mImgPhono;

    @BindView(R.id.img_respondent_head)
    CircleImageView mImgRespondentHead;

    @BindView(R.id.tv_respondent_nick_name)
    TextView mTvRespondentNickName;

    AnsweredInfoModel mAnsweredInfoModel;

    private String mType;

    private String mPhotoPath;

    @OnClick({R.id.ll_back, R.id.img_phono, R.id.tv_adopt_now, R.id.tv_adopted, R.id.img_immediately, R.id.img_unable_to})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
                mEditor.putString("activity", "");
                mEditor.commit();
                break;

            case R.id.img_phono:
                mPhotoPath = Api.PATH + mAnsweredInfoModel.getModel().getPhoto();
                initPhotoPow(mPhotoPath);
                break;

            case R.id.img_immediately:
                if (!HGTool.isEmpty(mAnsweredInfoModel.getModel().getExercisesStatus())) {
                    if (mAnsweredInfoModel.getModel().getExercisesStatus().equals("0")) {
                        initAnswerIntent();
                    } else if (mAnsweredInfoModel.getModel().getExercisesStatus().equals("1")) {
                        initAnswerIntent();
                    } else if (mAnsweredInfoModel.getModel().getExercisesStatus().equals("2")) {
                        initAnswerIntent();
                    }
                }
                break;

            case R.id.img_unable_to:
                if (!HGTool.isEmpty(mAnsweredInfoModel.getModel().getExercisesStatus())) {
                    if (mAnsweredInfoModel.getModel().getExercisesStatus().equals("3")) {
                        IToast.show(Activity_AnsweredInfo.this, "该问题已解答");
                    } else if (mAnsweredInfoModel.getModel().getExercisesStatus().equals("4")) {
                        IToast.show(Activity_AnsweredInfo.this, "该问题后台已介入");
                    } else if (mAnsweredInfoModel.getModel().getExercisesStatus().equals("5")) {
                        IToast.show(Activity_AnsweredInfo.this, "该问题已关闭");
                    }
                }
                break;

            case R.id.tv_adopt_now:
                if (HGTool.isEmpty(mAnsweredInfoModel.getModel().getRespondentOid())) {
                    return;
                }
                Intent mIntentChat = new Intent(Activity_AnsweredInfo.this, Activity_Chat.class);
                mIntentChat.putExtra(EaseConstant.EXTRA_USER_ID, mAnsweredInfoModel.getModel().getRespondentOid().trim().toLowerCase());
                mIntentChat.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
                mIntentChat.putExtra("oid", mOid);
                mIntentChat.putExtra(EaseConstant.TOKEN, BaseApplication.getApplication().getToken());//自己的token
                mIntentChat.putExtra(EaseConstant.ISCARRYON, "no");//自己的token
                mIntentChat.putExtra(EaseConstant.PROBLEM_OID, mOid);//问题ID
                mIntentChat.putExtra(EaseConstant.ME_ID, BaseApplication.getApplication().getOid());//自己的ID
                mIntentChat.putExtra(EaseConstant.ME_HEAD, BaseApplication.getApplication().getHead());//自己的头像
                mIntentChat.putExtra(EaseConstant.ME_NAME, BaseApplication.getApplication().getNickName());//自己的名字
                startActivity(mIntentChat);
                finish();
                break;

            case R.id.tv_adopted:
                if (mAnsweredInfoModel.getModel().getExercisesStatus().equals("3")) {
                    IToast.show(Activity_AnsweredInfo.this, "该问题已解答");
                } else if (mAnsweredInfoModel.getModel().getExercisesStatus().equals("4")) {
                    IToast.show(Activity_AnsweredInfo.this, "该问题后台已介入");
                } else if (mAnsweredInfoModel.getModel().getExercisesStatus().equals("5")) {
                    IToast.show(Activity_AnsweredInfo.this, "该问题已关闭");
                }
                break;

        }
    }

    View popView;

    PopupWindow mPopupWindow;

    @BindView(R.id.ll_answer_info)
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
        popView = LayoutInflater.from(Activity_AnsweredInfo.this).inflate(R.layout.popup_window_online_answer_info, null);
        ZoomImageView mZoomImageView = popView.findViewById(R.id.web_view_img);
        Glide.with(Activity_AnsweredInfo.this).load(mPhotoPath).into(mZoomImageView);
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

    /**
     * 答题者跳转 聊天  界面
     */
    private void initAnswerIntent() {
        Intent mIntentChat = new Intent(Activity_AnsweredInfo.this, Activity_Chat.class);
        mIntentChat.putExtra(EaseConstant.EXTRA_USER_ID, mAnsweredInfoModel.getModel().getUserOid().trim().toLowerCase());
        mIntentChat.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
        mIntentChat.putExtra("oid", mOid);
        mIntentChat.putExtra("is_chat", "1");
        if (mAnsweredInfoModel.getModel().getExercisesStatus().equals("1")) {
            mIntentChat.putExtra(EaseConstant.ISCARRYON, "yes");
        } else if (mAnsweredInfoModel.getModel().getExercisesStatus().equals("2")) {
            mIntentChat.putExtra(EaseConstant.ISCARRYON, "no");
        }
        mIntentChat.putExtra(EaseConstant.TOKEN, BaseApplication.getApplication().getToken());//问题ID
        mIntentChat.putExtra(EaseConstant.PROBLEM_OID, mOid);//问题ID
        mIntentChat.putExtra(EaseConstant.ME_ID, BaseApplication.getApplication().getOid());//自己的ID
        mIntentChat.putExtra(EaseConstant.ME_HEAD, BaseApplication.getApplication().getHead());//自己的头像
        mIntentChat.putExtra(EaseConstant.ME_NAME, BaseApplication.getApplication().getNickName());//自己的名字
        startActivity(mIntentChat);
        finish();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_answered_info;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("解答详情");
    }

    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;

    @Override
    protected void initData() {
        mOid = getIntent().getStringExtra("oid");
        mType = getIntent().getStringExtra("type");
        initAnsweredInfo();


        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tangchaoke.yiyoubangjiao.answered.me");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);


    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra("tuisong");
            String mTOid = intent.getStringExtra("oid");
            /**
             * 如果有新的消息，在此界面要 及时进行刷新
             */
            if (num.equals("answered_me")) {
                if (mTOid.equals(mOid)) {
                    initAnsweredInfo();
                } else {
                    return;
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putString("activity", "Activity_AnsweredInfo");
        mEditor.commit();
    }

    private void initAnsweredInfo() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(Activity_AnsweredInfo.this, "加载中", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.DETAILS_PROBLEM)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.oid", mOid)
                .addParams("type1", "Android")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_AnsweredInfo.this, "服务器开小差！请稍后重试");
                        Log.e("==问题详情：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==问题详情：：：", response);
                        Log.e("==问题详情：：：", Api.DETAILS_PROBLEM);
                        Log.e("==问题详情：：：", BaseApplication.getApplication().getToken());
                        Log.e("==问题详情：：：", mOid);
                        mProgressHUD.dismiss();
                        mAnsweredInfoModel = JSONObject.parseObject(response, AnsweredInfoModel.class);
                        if (RequestType.SUCCESS.equals(mAnsweredInfoModel.getStatus())) {
                            initAnsweredInfoDisplay(mAnsweredInfoModel.getModel());
                        } else {
                            if (mAnsweredInfoModel.getStatus().equals("9") || mAnsweredInfoModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mAnsweredInfoModel.getStatus(),
                                        Activity_AnsweredInfo.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_AnsweredInfo.this, "登录失效");
                                }
                            } else if (mAnsweredInfoModel.getStatus().equals("0")) {
                                IToast.show(Activity_AnsweredInfo.this, mAnsweredInfoModel.getMessage());
                            }
                        }
                    }
                });

    }

    @BindView(R.id.tv_prompt)
    TextView mTvPrompt;

    @BindView(R.id.ll_me)
    LinearLayout mLlMe;

    @BindView(R.id.ll_help)
    LinearLayout mLlHelp;

    @BindView(R.id.tv_help_prompt)
    TextView mTvHelpPrompt;

    @BindView(R.id.img_immediately)
    ImageView mImgImmediately;

    @BindView(R.id.img_unable_to)
    ImageView mImgUnableTo;

    private String mMeId;
    private String mMeHead;
    private String mMeName;

    private String mToId;
    private String mToHead;
    private String mToName;

    @BindView(R.id.tv_bounty)
    TextView mTvBounty;

    @BindView(R.id.ll_subject)
    LinearLayout mLlSubject;

    private void initAnsweredInfoDisplay(AnsweredInfoModel.AnsweredInfoModelModel mAnsweredInfoModel) {

        if (mAnsweredInfoModel.getType().equals("1")) {
            mLlSubject.setVisibility(View.VISIBLE);
        } else if (mAnsweredInfoModel.getType().equals("2")) {
            mLlSubject.setVisibility(View.INVISIBLE);
        }

        /**
         * 获取自己的ID  头像  昵称
         */
        if (!HGTool.isEmpty(BaseApplication.getApplication().getOid())) {
            mMeId = BaseApplication.getApplication().getOid();
        }
        if (!HGTool.isEmpty(BaseApplication.getApplication().getHead())) {
            mMeHead = BaseApplication.getApplication().getHead();
        }

        if (!HGTool.isEmpty(BaseApplication.getApplication().getNickName())) {
            mMeName = BaseApplication.getApplication().getNickName();
        }

        if (!HGTool.isEmpty(mAnsweredInfoModel.getHead())) {
            Glide.with(Activity_AnsweredInfo.this).load(Api.PATH + mAnsweredInfoModel.getHead()).into(mImgHead);
        }

        if (!HGTool.isEmpty(mAnsweredInfoModel.getNickName())) {
            if (mAnsweredInfoModel.getNickName().length() > 11) {
                mTvName.setMaxLines(1);
                mTvName.setMaxEms(6);
                mTvName.setEllipsize(TextUtils.TruncateAt.END);
                mTvName.setSingleLine();
                mTvName.setText(mAnsweredInfoModel.getNickName());
            } else {
                mTvName.setText(mAnsweredInfoModel.getNickName());
            }
            mToName = mAnsweredInfoModel.getNickName();
        }

        if (!HGTool.isEmpty(mAnsweredInfoModel.getGrade())) {
            mTvGrade.setText(mAnsweredInfoModel.getGrade());
        } else if (!HGTool.isEmpty(mAnsweredInfoModel.getChessSpecies())) {
            if (mAnsweredInfoModel.getChessSpecies().equals("1")) {
                mTvGrade.setText("国际象棋");
            } else if (mAnsweredInfoModel.getChessSpecies().equals("2")) {
                mTvGrade.setText("国际跳棋");
            } else if (mAnsweredInfoModel.getChessSpecies().equals("3")) {
                mTvGrade.setText("围棋");
            } else if (mAnsweredInfoModel.getChessSpecies().equals("4")) {
                mTvGrade.setText("五子棋");
            } else if (mAnsweredInfoModel.getChessSpecies().equals("5")) {
                mTvGrade.setText("象棋");
            }
        }

        if (!HGTool.isEmpty(mAnsweredInfoModel.getSubject())) {
            mTvSubject.setText(mAnsweredInfoModel.getSubject());
        }

        if (!HGTool.isEmpty(mAnsweredInfoModel.getSubmissionTime())) {
            mTvTime.setText("发布时间：" + mAnsweredInfoModel.getSubmissionTime());
        }

        if (!HGTool.isEmpty(mAnsweredInfoModel.getPrice())) {
            if (mAnsweredInfoModel.getPrice().equals("0")) {
                mTvBounty.setVisibility(View.GONE);
            } else {
                mTvBounty.setVisibility(View.VISIBLE);
                mTvBounty.setText("￥ " + mAnsweredInfoModel.getPrice());
            }
        }

        if (!HGTool.isEmpty(mAnsweredInfoModel.getContent().trim())) {
            mTvQuestion.setVisibility(View.VISIBLE);
            mTvQuestion.setText(mAnsweredInfoModel.getContent());
        } else {
            mTvQuestion.setVisibility(View.GONE);
        }

        if (!HGTool.isEmpty(mAnsweredInfoModel.getPhoto())) {
            mImgPhono.setVisibility(View.VISIBLE);
            Glide.with(Activity_AnsweredInfo.this).load(Api.PATH + mAnsweredInfoModel.getPhoto()).into(mImgPhono);
        } else {
            mImgPhono.setVisibility(View.GONE);
        }

        if (!HGTool.isEmpty(mAnsweredInfoModel.getRespondenthead())) {
            Glide.with(Activity_AnsweredInfo.this).load(Api.PATH + mAnsweredInfoModel.getRespondenthead()).into(mImgRespondentHead);
        }

        if (!HGTool.isEmpty(mAnsweredInfoModel.getRespondentnickName())) {
            mTvRespondentNickName.setText(mAnsweredInfoModel.getRespondentnickName());
        }

        /**
         * 获取对方的头像  昵称
         */
        if (!HGTool.isEmpty(mType)) {
            if (mType.equals("help")) {
                /**
                 * 问题状态 1已抢答 2解答中
                 */
                if (!HGTool.isEmpty(mAnsweredInfoModel.getExercisesStatus())) {
                    if (mAnsweredInfoModel.getExercisesStatus().equals("1")) {
                        mToId = mAnsweredInfoModel.getUserOid();
                        mToName = mAnsweredInfoModel.getNickName();
                        mToHead = Api.PATH + mAnsweredInfoModel.getHead();
                    } else if (mAnsweredInfoModel.getExercisesStatus().equals("2")) {
                        mToId = mAnsweredInfoModel.getUserOid();
                        mToName = mAnsweredInfoModel.getNickName();
                        mToHead = Api.PATH + mAnsweredInfoModel.getHead();
                    }
                }
            } else if (mType.equals("me")) {
                /**
                 * 问题状态1已抢答 2解答中
                 */
                if (!HGTool.isEmpty(mAnsweredInfoModel.getExercisesStatus())) {
                    if (mAnsweredInfoModel.getExercisesStatus().equals("1")) {
                        mToId = mAnsweredInfoModel.getRespondentOid();
                        mToName = mAnsweredInfoModel.getRespondentnickName();
                        mToHead = Api.PATH + mAnsweredInfoModel.getRespondenthead();
                    } else if (mAnsweredInfoModel.getExercisesStatus().equals("2")) {
                        mToId = mAnsweredInfoModel.getRespondentOid();
                        mToName = mAnsweredInfoModel.getRespondentnickName();
                        mToHead = Api.PATH + mAnsweredInfoModel.getRespondenthead();
                    }
                }
            }
        }

        if (!HGTool.isEmpty(mType)) {
            if (mType.equals("help")) {
                mLlMe.setVisibility(View.GONE);
                mLlHelp.setVisibility(View.VISIBLE);
                /**
                 * 问题状态 0待抢答 1已抢答 2解答中 3已解答 4后台处理 5已关闭
                 */
                if (!HGTool.isEmpty(mAnsweredInfoModel.getExercisesStatus())) {
                    if (mAnsweredInfoModel.getExercisesStatus().equals("0")) {
                        mTvHelpPrompt.setText("立即抢答");
                        mImgImmediately.setVisibility(View.VISIBLE);
                        mImgUnableTo.setVisibility(View.GONE);
                    } else if (mAnsweredInfoModel.getExercisesStatus().equals("1")) {
                        mTvHelpPrompt.setText("等待沟通");
                        mImgImmediately.setVisibility(View.VISIBLE);
                        mImgUnableTo.setVisibility(View.GONE);
                    } else if (mAnsweredInfoModel.getExercisesStatus().equals("2")) {
                        mTvHelpPrompt.setText("继续解答");
                        mImgImmediately.setVisibility(View.VISIBLE);
                        mImgUnableTo.setVisibility(View.GONE);
                    } else if (mAnsweredInfoModel.getExercisesStatus().equals("3")) {
                        mTvHelpPrompt.setText("已解答");
                        mImgImmediately.setVisibility(View.GONE);
                        mImgUnableTo.setVisibility(View.VISIBLE);
                    } else if (mAnsweredInfoModel.getExercisesStatus().equals("4")) {
                        mTvHelpPrompt.setText("后台处理");
                        mImgImmediately.setVisibility(View.GONE);
                        mImgUnableTo.setVisibility(View.VISIBLE);
                    } else if (mAnsweredInfoModel.getExercisesStatus().equals("5")) {
                        mTvHelpPrompt.setText("已关闭");
                        mImgImmediately.setVisibility(View.GONE);
                        mImgUnableTo.setVisibility(View.VISIBLE);
                    }
                }
            } else if (mType.equals("me")) {
                mLlMe.setVisibility(View.VISIBLE);
                mLlHelp.setVisibility(View.GONE);
                /**
                 * 问题状态 0待抢答 1已抢答 2解答中 3已解答 4后台处理 5已关闭
                 */
                if (!HGTool.isEmpty(mAnsweredInfoModel.getExercisesStatus())) {
                    if (mAnsweredInfoModel.getExercisesStatus().equals("0")) {
                        mLlNoTimeActor.setVisibility(View.VISIBLE);
                        mTvPrompt.setText("暂无人抢答");
                        mLlActorinfo.setVisibility(View.GONE);
                    } else if (mAnsweredInfoModel.getExercisesStatus().equals("1")) {
                        mLlNoTimeActor.setVisibility(View.VISIBLE);
                        mTvPrompt.setText("已抢答，若2分钟内答题人未与您沟通，则该题将重新为您回归答题大厅。");
                        mLlActorinfo.setVisibility(View.GONE);
                    } else if (mAnsweredInfoModel.getExercisesStatus().equals("2")) {
                        mLlNoTimeActor.setVisibility(View.GONE);
                        mLlActorinfo.setVisibility(View.VISIBLE);
                        mTvAdoptNow.setVisibility(View.VISIBLE);
                        mTvAdoptNow.setText("继续沟通");
                        mTvAdopted.setVisibility(View.GONE);
                    } else if (mAnsweredInfoModel.getExercisesStatus().equals("3")) {
                        mLlNoTimeActor.setVisibility(View.GONE);
                        mLlActorinfo.setVisibility(View.VISIBLE);
                        mTvAdoptNow.setVisibility(View.GONE);
                        mTvAdopted.setVisibility(View.VISIBLE);
                        mTvAdopted.setText("已解决");
                    } else if (mAnsweredInfoModel.getExercisesStatus().equals("4")) {
                        mLlNoTimeActor.setVisibility(View.GONE);
                        mLlActorinfo.setVisibility(View.VISIBLE);
                        mTvAdoptNow.setVisibility(View.GONE);
                        mTvAdopted.setVisibility(View.VISIBLE);
                        mTvAdopted.setText("后台处理");
                    } else if (mAnsweredInfoModel.getExercisesStatus().equals("5")) {
                        mLlNoTimeActor.setVisibility(View.GONE);
                        mLlActorinfo.setVisibility(View.VISIBLE);
                        mTvAdoptNow.setVisibility(View.GONE);
                        mTvAdopted.setVisibility(View.VISIBLE);
                        mTvAdopted.setText("已关闭");
                    }
                }
            }
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
